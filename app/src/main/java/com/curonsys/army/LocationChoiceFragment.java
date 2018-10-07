package com.curonsys.army;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 * 위치 설정 프래그먼트
 */



public class LocationChoiceFragment extends Fragment implements OnMapReadyCallback {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Context thisContext;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    private Geocoder geocoder;
    LocationManager lm;
    DBManager dbManager = DBManager.getInstance();
    CallBackListener callBackListener;
    TextView tv;
    MaterialDialog.Builder builder = null;
    MaterialDialog materialDialog = null;
    private MapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_choice, container, false);
        /*FragmentManager fragmentManager = this.getChildFragmentManager();

        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        callBackListener = (CallBackListener)getActivity();
        callBackListener.onDoneBack();
        */
        lm = (LocationManager) thisContext.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(thisContext);

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }

        builder = new MaterialDialog.Builder(thisContext)
                .title("위치 수신중")
                .content("현재 위치를 확인중입니다...")
                .progress(true,0);
        materialDialog = builder.build();
        materialDialog.show();

        FragmentManager fragmentManager = this.getChildFragmentManager();
        callBackListener = (CallBackListener) getActivity();
        callBackListener.onDoneBack();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //자동완성
        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment)
                fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);

        fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("clicked","d");
                Log.d("", "Place: " + place.getName());//get place details here
                search_adress(place.getName().toString());
            }
            @Override
            public void onError(Status status) {
                Log.d("status",status.toString());
            }
        });
        fragment.setHint("위치를 검색하세요.");
        fragment.getView().setBackgroundColor(Color.WHITE);

        /*
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build((Activity)thisContext);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), (Activity)thisContext, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            // Handle the exception
        }*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        thisContext=activity;

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(thisContext);

        // 맵 클릭시 마커생성과 gps 얻기

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; //위도
                Double longitude = point.longitude; //경도tn
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                mMap.clear();
                //googleMap.addMarker(mOptions);
            }
        });
        //LatLng seoul = new LatLng(37.541, 126.986);
        //mMap.addMarker(new MarkerOptions().position(seoul).title("Marker in Seoul"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //if location data is change, listener event alert.

            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
    //        double altitude = location.getAltitude();   //고도//          float accuracy = location.getAccuracy();    //정확도//            String provider = location.getProvider();   //위치제공자
            materialDialog.dismiss();

            LatLng currentLocation = new LatLng(latitude,longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title("현재 위치");
            markerOptions.snippet("ARZone");
            //mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
            lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
            dbManager.markerLongtitude = longitude;
            dbManager.markerLatitude = latitude;
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    public void search_adress(String placeName){
        String str = placeName;
        mMap.clear();
        List<Address> addressList = null;
        try {
            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
            addressList = geocoder.getFromLocationName(
                    str, // 주소
                    10); // 최대 검색 결과 개수

            // 콤마를 기준으로 split
            System.out.println(addressList.get(0).toString());
            String[] splitStr = addressList.get(0).toString().split(",");
            String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 8); // 주소 //1부터 최대길이의 -2 까지
            System.out.println(address);

            String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도  //????
            String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도   ????
            System.out.println(latitude);
            System.out.println(longitude);

            // 좌표(위도, 경도) 생성
            LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            // 마커 생성
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title(str);
            mOptions2.snippet(address); //latitude.toString() + ", " + longitude.toString()
            mOptions2.snippet(latitude.toString() + ", " + longitude.toString());
            mOptions2.position(point);
            // 마커 추가
            //mMap.addMarker(mOptions2);
            // 해당 좌표로 화면 줌
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,16));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(thisContext, "잘못된 입력입니다.", Toast.LENGTH_SHORT).show();
        }

    }
}

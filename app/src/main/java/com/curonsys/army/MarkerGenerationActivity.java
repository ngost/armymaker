package com.curonsys.army;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

public class MarkerGenerationActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    Button btn1,btn2;


    static {
        try {
            System.loadLibrary("opencv_java");
        } catch (UnsatisfiedLinkError e) {
            System.load("opencv_java");
        }
        try {
            System.loadLibrary("nonfree");
        } catch (UnsatisfiedLinkError e) {
            System.load("nonfree");
        }
        //System.loadLibrary("opencv_java");
        //System.loadLibrary("nonfree");
    }

    Button btn_capture, btn_album, btn_sift;
    ImageView iv_view;
    String mCurrentPhotoPath;
    TextView tv, sift_tv_result;
    ToggleButton tb;


    Uri imageUri;
    Uri photoURI, albumURI;

    //private ImageView imageView; //아직 쓰지말자
    private Bitmap inputImage; // make bitmap from image resource
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);

        sift_tv_result = findViewById(R.id.sift_result_tv);

        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_album = (Button) findViewById(R.id.btn_album);
        btn_sift = (Button) findViewById(R.id.btn_sift);
        iv_view = (ImageView) findViewById(R.id.iv_view);
        tv = (TextView) findViewById(R.id.textView2);
        tv.setText("위치정보 미수신중");

        tb = (ToggleButton) findViewById(R.id.toggle1);

        // LocationManager 객체를 얻어온다
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(tb.isChecked()){
                        tv.setText("수신중..");
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                    }else{
                        tv.setText("위치정보 미수신중");
                        lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    }
                }catch(SecurityException ex){
                }
            }
        });

        btn_sift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sift();
                }catch(Exception e)
                {
                    Log.e("captureCamera Error", e.toString());
                }
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_simple,new MarkerGenerationFragment1());

    }



    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            tv.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : " + accuracy);
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
    // end of class

    @Override
    public void onResume(){
        super.onResume();
    }

    public void sift(){
        Mat rgba=new Mat();
        Utils.bitmapToMat(inputImage,rgba);
        MatOfKeyPoint keyPoints=new MatOfKeyPoint();
        Imgproc.cvtColor(rgba,rgba,Imgproc.COLOR_RGBA2GRAY);
        detector.detect(rgba,keyPoints);
        Features2d.drawKeypoints(rgba,keyPoints,rgba);
        Utils.matToBitmap(rgba,inputImage);
        iv_view.setImageBitmap(inputImage);
        sift_tv_result.setText("Keypoint 갯수 : " + keyPoints.toArray().length);
    }
}

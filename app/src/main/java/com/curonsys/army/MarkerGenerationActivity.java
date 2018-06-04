package com.curonsys.army;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MarkerGenerationActivity extends AppCompatActivity {
    static final int FRAGMENT1 = 1;
    static final int FRAGMENT2 = 2;
    static final int FRAGMENT3 = 3;
    static final int FRAGMENT4 = 4;
    int current_fragment=1;
    Button btn1,btn2;
    Button nextBtn;


    Uri imageUri;
    Uri photoURI, albumURI;

    //private ImageView imageView; //아직 쓰지말자
  //  private Bitmap inputImage; // make bitmap from image resource
//    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_generation);

        nextBtn = findViewById(R.id.nextstepBtn);

//        tv.setText("위치정보 미수신중");

        // LocationManager 객체를 얻어온다



//        tb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    if(tb.isChecked()){
//                        tv.setText("수신중..");
//                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
//                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
//                                100, // 통지사이의 최소 시간간격 (miliSecond)
//                                1, // 통지사이의 최소 변경거리 (m)
//                                mLocationListener);
//                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
//                                100, // 통지사이의 최소 시간간격 (miliSecond)
//                                1, // 통지사이의 최소 변경거리 (m)
//                                mLocationListener);
//                    }else{
//                        tv.setText("위치정보 미수신중");
//                        lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
//                    }
//                }catch(SecurityException ex){
//                }
//            }
//        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_simple,new MarkerGenerationFragment1());
        fragmentTransaction.commit();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (current_fragment){
                    case FRAGMENT1:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new MarkerGenerationFragment2());
                        fragmentTransaction.commit();
                        current_fragment = FRAGMENT2;
                        break;
                    case FRAGMENT2:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new MarkerGenerationFragment3());
                        fragmentTransaction.commit();
                        current_fragment = FRAGMENT3;
                        break;
                    case FRAGMENT3:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new MarkerGenerationFragment4());
                        fragmentTransaction.commit();
                        current_fragment = FRAGMENT4;
                    default:
                        break;

                }


            }
        });

    }




    // end of class

    @Override
    public void onResume(){
        super.onResume();
    }
}

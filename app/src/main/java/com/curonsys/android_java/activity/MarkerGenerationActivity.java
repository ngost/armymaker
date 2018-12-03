package com.curonsys.android_java.activity;

//마커 생성 프래그먼트 관리 엑티비티
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.curonsys.android_java.CallBackListener;
import com.curonsys.android_java.R;
import com.curonsys.android_java.fragment.ContentsChoiceFragment;
import com.curonsys.android_java.fragment.ImageChoiceFragment;
import com.curonsys.android_java.fragment.LocationChoiceFragment;
import com.curonsys.android_java.fragment.MarkerConfirmFragment;
import com.curonsys.android_java.util.DBManager;
import com.curonsys.android_java.util.MarkerUploader;

public class MarkerGenerationActivity extends AppCompatActivity implements CallBackListener {
    static final int FRAGMENT1 = 1;
    static final int FRAGMENT2 = 2;
    static final int FRAGMENT3 = 3;
    static final int FRAGMENT4 = 4;
    int current_fragment=1;
    Button btn1,btn2;
    Button nextBtn;
    Activity mActivity;

    Uri imageUri;
    Uri photoURI, albumURI;

    @Override
    public void onSucces(String message) {

    }

    @Override
    public void onDoneBack() {
        nextBtn.setEnabled(true);
        nextBtn.setBackgroundColor(getResources().getColor(R.color.button_blue));
        nextBtn.setTextColor(Color.WHITE);
        Drawable btnDraw = nextBtn.getBackground();
        btnDraw.setAlpha(255);
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_marker_generation);




        mActivity = this;

        nextBtn = findViewById(R.id.nextstepBtn);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_simple,new ImageChoiceFragment());
        fragmentTransaction.commit();
        nextBtn.setEnabled(false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                nextBtn.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (current_fragment){
                    case FRAGMENT1:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new LocationChoiceFragment());
                        fragmentTransaction.commit();
                        nextBtn.setTextColor(Color.BLACK);
                        current_fragment = FRAGMENT2;
                        break;
                    case FRAGMENT2:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new ContentsChoiceFragment());
                        fragmentTransaction.commit();
                        nextBtn.setTextColor(Color.BLACK);
                        current_fragment = FRAGMENT3;
                        break;
                    case FRAGMENT3:
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,0,0);
                        fragmentTransaction.replace(R.id.fragment_simple,new MarkerConfirmFragment());
                        fragmentTransaction.commit();
                        nextBtn.setTextColor(Color.BLACK);
                        current_fragment = FRAGMENT4;
                        break;
                    case FRAGMENT4:
                        MarkerUploader markerUploader = new MarkerUploader(mActivity);
                        markerUploader.start(true);
                    default:
                        break;

                }


            }
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        if (v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            // calculate the relative position of the clicking position against the position of the view
            float x = event.getRawX() - scrcoords[0];
            float y = event.getRawY() - scrcoords[1];

            // check whether action is up and the clicking position is outside of the view
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < 0 || x > v.getRight() - v.getLeft()
                    || y < 0 || y > v.getBottom() - v.getTop())) {
                if (v.getOnFocusChangeListener() != null) {
                    v.getOnFocusChangeListener().onFocusChange(v, false);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // end of class

    @Override
    public void onResume(){
        super.onResume();
    }
}

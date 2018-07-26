package com.curonsys.army;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_generation);

        nextBtn = findViewById(R.id.nextstepBtn);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_simple,new ImageChoiceFragment());
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

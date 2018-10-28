package com.curonsys.android_java.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.curonsys.android_java.activity.MarkerTestActivity;
import com.curonsys.android_java.R;
import com.curonsys.android_java.util.DBManager;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 * 마커 등록 최종 확인 엑티비티
 */



public class MarkerConfirmFragment extends Fragment {

    Context thisContext;

    MaterialDialog.Builder builder = null;
    MaterialDialog materialDialog = null;
    TextView tv;
    DBManager dbManager = DBManager.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_confirm, container, false);
        FragmentManager fragmentManager = this.getChildFragmentManager();
        tv = view.findViewById(R.id.markerInfoTv);
        tv.setText(dbManager.generatorId+",\n"
                        +dbManager.markerRating+",\n"
                        +dbManager.imageURI.toString()+",\n"
                        +dbManager.currentLongtitude+",\n"
                        +dbManager.currentLatitude+",\n"
                        +dbManager.contentId+",\n"
                        +dbManager.contentName+",\n");
        Button btn = view.findViewById(R.id.markerTestBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext,MarkerTestActivity.class);
                startActivityForResult(intent,0);
            }
        });
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
        //step 4

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","okok");
        switch (resultCode) {
            case 1:
                Log.d("resultInput","ok");
                Toast.makeText(thisContext,""+data.getStringExtra("scale"),Toast.LENGTH_SHORT).show();
//                Log.d("scaleString",data.getFloatExtra("scale",1.0f)+"");
                tv.setText(tv.getText()+",\n scale : "+data.getStringExtra("scale"));
                break;
        }
    }
}

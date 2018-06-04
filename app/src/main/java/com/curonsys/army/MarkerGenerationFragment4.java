package com.curonsys.army;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 */



public class MarkerGenerationFragment4 extends Fragment {

    Context thisContext;

    MaterialDialog.Builder builder = null;
    MaterialDialog materialDialog = null;
    TextView tv;
    DBManager dbManager = DBManager.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_generation4, container, false);
        FragmentManager fragmentManager = this.getChildFragmentManager();
        tv = view.findViewById(R.id.markerInfoTv);
        tv.setText(dbManager.generatorId+",\n"
                        +dbManager.markerRating+",\n"
                        +dbManager.imageURI.toString()+",\n"
                        +dbManager.markerLongtitude+",\n"
                        +dbManager.markerLatitude+",\n"
                        +dbManager.contentId+",\n"
                        +dbManager.contentName+",\n"
                        +dbManager.contentFileName+",\n"
                        +dbManager.contentTextureFiles[0]+",\n"
                        +dbManager.contentHasAnimation
                        +dbManager.textureCount);
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

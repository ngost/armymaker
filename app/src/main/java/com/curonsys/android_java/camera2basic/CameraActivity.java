/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.curonsys.android_java.camera2basic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.curonsys.android_java.CallBackListener;
import com.curonsys.android_java.R;

public class CameraActivity extends AppCompatActivity implements CallBackListener {
    Camera2BasicFragment cameraFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (null == savedInstanceState) {
            cameraFrag = Camera2BasicFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

//        CallBackListener callBackListener = new CallBackListener() {
//            @Override
//            public void onDoneBack() {
//
//            }
//
//            @Override
//            public void onSucces(String message) {
//
//                switch (message){
//                    case "capture":
//                        Log.d("captured","sucess");
//                        cameraFrag.initLoaction();
//                        break;
//                    case "location":
//                        cameraFrag.uploadData();
//                        break;
//                    case "upload":
//                        Toast.makeText(getApplicationContext(),cameraFrag.getMarkerUrl(),Toast.LENGTH_LONG).show();
//                }
//
//            }
//        };
    }

    @Override
    public void onDoneBack() {

    }

    @Override
    public void onSucces(String message) {

        switch (message){
            case "capture":
                Log.d("capture","sucess");
//                cameraFrag.initLoaction(this);
                cameraFrag.uploadData(this);
                break;
            case "upload":
                Log.d("upload","sucess");
                Toast.makeText(getApplicationContext(),cameraFrag.getMarkerUrl(),Toast.LENGTH_LONG).show();
        }

    }

}

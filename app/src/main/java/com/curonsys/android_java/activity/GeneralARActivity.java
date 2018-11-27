package com.curonsys.android_java.activity;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.curonsys.android_java.R;
import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.util.DBManager;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;


public class GeneralARActivity extends ARActivity {

    ContentModel contentModel;
    DBManager mDBManager;
    Vibrator vibrator;
    ARModelNode node3d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_ar);
        Log.d("ar_activity","oncreate");
        contentModel = (ContentModel) getIntent().getSerializableExtra("contents_model");
        mDBManager = DBManager.getInstance();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//        Log.d("1",contentModel.getTextures().toString());
//        Log.d("2",contentModel.getModel());
//        Log.d("3", mDBManager.imageURI.toString());
//        Log.d("4",mDBManager.contentRotation.toString());
//        Log.d("5",mDBManager.contentScale+"");
    }

    @Override
    public void setup() {
        super.setup();
        Log.d("ar_activity","setup");

        Log.d("setup","called");

        String path = mDBManager.imageURI.toString();
        //step1, 추적 가능한 이미지를 등록해라. 여기서 해야 할 일은 Trackable 객체 만들기, Tracker 생성
        ARImageTrackable imageTrackable;
        //이름지정
        imageTrackable = new ARImageTrackable("MarkerForAR");
        //에셋에서 이미지 로딩
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "kudan");
        //Log.d("123123", file.getAbsolutePath());
        if (path != null) {
            Uri uri = Uri.parse(path);
            imageTrackable.loadFromPath(uri.getPath());
            Log.d("marker_path",uri.getPath());
        } else {
            Toast.makeText(getApplicationContext(), "마커를 먼저 등록해주세요.", Toast.LENGTH_LONG).show();
        }

        // Get the single instance of the image tracker.
        ARImageTracker imageTracker;
        //인스턴스생성
        imageTracker = ARImageTracker.getInstance();
        //트래커 초기화
        imageTracker.initialise();
        imageTracker.addTrackable(imageTrackable);

        //트래커 객체생성



        ARModelImporter arModelImporter = new ARModelImporter();
        arModelImporter.loadFromPath(contentModel.getModel());
        //model's file name
        ArrayList<String> textures = contentModel.getTextures();

        if(textures.size()<2){

//                arModelImporter.loadFromAsset(dbManager.contentFileName);

            //model info[0]
            node3d = arModelImporter.getNode();
            node3d.setName("somthing");

//                node3d.rotateByDegrees(90.0f, 1.0f, 0.0f, 0.0f);
//                node3d.rotateByDegrees(180.0f, 1.0f, 100.0f, 0.0f);
            ARTexture2D texture2D = new ARTexture2D();
            if(textures.size()<2){
//                    texture2D.loadFromAsset(dbManager.contentTextureFiles[0]);
                Log.d("textures_path",textures.get(0));
                texture2D.loadFromPath(textures.get(0));
            }
            //model info[1]
            final ARLightMaterial material = new ARLightMaterial();
            material.setTexture(texture2D);
            material.setColour(1, 1, 1);
            material.setAmbient(0.8f, 0.8f, 0.8f);//조명

            for (ARMeshNode meshNode : arModelImporter.getMeshNodes()) {
                meshNode.setMaterial(material);
            }

        }
        else if (textures.size()>1){
            //snake, animation
//                arModelImporter.loadFromAsset(dbManager.contentFileName);

            node3d = arModelImporter.getNode();
            node3d.setName("somthing");

            ARTexture2D[] texture2DS = new ARTexture2D[textures.size()];

            ARLightMaterial[] material = new ARLightMaterial[textures.size()];

            int i = 0;
            HashMap<String,ARLightMaterial> materialMap = new HashMap();

            for(i=0;i<textures.size();i++){
                Log.d("textures's",i+"");
                Log.d("textures_path",textures.get(i));
                texture2DS[i] = new ARTexture2D();
//                    texture2DS[i].loadFromAsset(dbManager.contentTextureFiles[i]);
                texture2DS[i].loadFromPath(textures.get(i));
                material[i] = new ARLightMaterial();
                material[i].setTexture(texture2DS[i]);
                material[i].setColour(1,1,1);
                material[i].setAmbient(0.8f,0.8f,0.8f);

                String material_name = textures.get(i).substring(textures.get(i).lastIndexOf("/")+1,textures.get(i).length()-4);
                material[i].setName(material_name);
                Log.d("material_name",material_name);
                materialMap.put(material[i].getName(),material[i]);
            }
//            i = 0;


            for(ARMeshNode meshNode : arModelImporter.getMeshNodes()){
                String name = meshNode.getName();
                Log.d("mate_name",name);
                Log.d("\n",materialMap.toString());
                meshNode.setMaterial(materialMap.get(name));

            }
        }
        initRoateAndScale();

        imageTrackable.getWorld().addChild(node3d);
        imageTrackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect (ARImageTrackable arImageTrackable){
                if(true){
                    node3d.play();
                    vibrator.vibrate(500);
                }
            }

            @Override
            public void didTrack (ARImageTrackable arImageTrackable){

            }

            @Override
            public void didLose (ARImageTrackable arImageTrackable){
                if(true){
                    node3d.pause();
                }
            }
        });
    }

    public void initRoateAndScale(){
        node3d.scaleByUniform(mDBManager.contentScale);
        setRotate(String.valueOf(mDBManager.contentRotation.get(0)),"x");
        setRotate(String.valueOf(mDBManager.contentRotation.get(1)),"y");
        setRotate(String.valueOf(mDBManager.contentRotation.get(2)),"z");
    }

    public void setRotate(String num, String axis){
        int angle = 45;
        int count = (int)Float.parseFloat(num);
        switch (axis){
            case "x":
                for(int i=1; i<count; i++){
                    node3d.rotateByDegrees(angle,1, 0, 0);
                }
                break;
            case "y":
                for(int i=1; i<count; i++){
                    node3d.rotateByDegrees(angle,0, 1, 0);
                }
                break;
            case "z":
                for(int i=1; i<count; i++){
                    node3d.rotateByDegrees(angle,0, 0, 1);
                }
                break;
        }
    }
}

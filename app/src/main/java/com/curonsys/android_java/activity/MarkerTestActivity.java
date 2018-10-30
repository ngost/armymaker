package com.curonsys.android_java.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.curonsys.android_java.CallBackListener;
import com.curonsys.android_java.R;
import com.curonsys.android_java.http.RequestManager;
import com.curonsys.android_java.model.ContentModel;
import com.curonsys.android_java.model.TransferModel;
import com.curonsys.android_java.util.DBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static android.content.ContentValues.TAG;

public class MarkerTestActivity extends ARActivity {

    boolean contents_down_state = false;
    DBManager dbManager=DBManager.getInstance();
    String path;
    SeekBar scale_seekBar;
    ImageButton uxbtn,dxbtn,uybtn,dybtn,uzbtn,dzbtn;
    float scaleValue = 1.0f;
    ARModelNode node3d;
    Button completeBtn;
    float setScale,setRotateX,setRotateY,setRotateZ = 1.0f;
    MaterialDialog.Builder builder = null;
    MaterialDialog materialDialog = null;
    ContentModel contentModel;
    ArrayList<String> textures = new ArrayList<String>();
    String modelUrl;
    CallBackListener callBackListener;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {



        getContentsFiles();

        super.onCreate(savedInstanceState);

        callBackListener = new CallBackListener() {
            @Override
            public void onDoneBack() {
                setup();
                setContentView(R.layout.activity_marker_test);
                scale_seekBar = findViewById(R.id.scaleSeekbar);
                uxbtn = findViewById(R.id.arrow_ux);
                dxbtn = findViewById(R.id.arrow_dx);
                uybtn = findViewById(R.id.arrow_uy);
                dybtn = findViewById(R.id.arrow_dy);
                uzbtn = findViewById(R.id.arrow_uz);
                dzbtn = findViewById(R.id.arrow_dz);
                builder = new MaterialDialog.Builder(getApplicationContext())
                        .title("유효성 검사중")
                        .content("시간이 조금 걸릴 수 있습니다...")
                        .progress(true,0);
                materialDialog = builder.build();

                completeBtn  = findViewById(R.id.completeBtn);
                scale_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("seekbar Value",progress+"");
                        scaleValue = Float.parseFloat(String.valueOf(progress))/50;
                        scaleValue = scaleValue * scaleValue;
                        Log.d("scaleValue",scaleValue+"");
//                Vector3f scales = node3d.getScale();
                        float newX;
                        float newY;
                        float newZ;
                        newX = 1 * scaleValue;
                        newY = 1 * scaleValue;
                        newZ = 1 * scaleValue;
                        setScale = newX;
                        node3d.setScale(newX,newY,newZ);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                uxbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateX++;
                        node3d.rotateByDegrees(45,1, 0, 0);
                    }
                });
                dxbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateX++;
                        node3d.rotateByDegrees(-45,1, 0, 0);

                    }
                });
                uybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateY++;
                        node3d.rotateByDegrees(45,0, 1, 0);
                    }
                });
                dybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateY++;
                        node3d.rotateByDegrees(-45,0, 1, 0);
                    }
                });
                uzbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateZ++;
                        node3d.rotateByDegrees(45,0, 0, 1);
                    }
                });
                dzbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateZ++;
                        node3d.rotateByDegrees(-45,0, 0, 1);
                    }
                });

        /*rotate_x_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(Float.parseFloat(String.valueOf(progress))<1){
                    setRotateX = 0;
                }else
                    setRotateX = 1;
//                node3d.rotateByDegrees(0f, setRotateX, setRotateY, setRotateZ);


                node3d.rotateByDegrees(90,setRotateX, 0, 0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rotate_y_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(Float.parseFloat(String.valueOf(progress))<1){
                    setRotateY = 0;
                }else
                    setRotateY = 1;
//                node3d.rotateByDegrees(0f, setRotateX, setRotateY, setRotateZ);


                node3d.rotateByDegrees(90,0, setRotateY, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rotate_z_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(Float.parseFloat(String.valueOf(progress))<1){
                    setRotateZ = 0;
                }else
                    setRotateZ = 1;
//                node3d.rotateByDegrees(0f, setRotateX, setRotateY, setRotateZ);


                node3d.rotateByDegrees(90,0, 0, setRotateZ);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

                completeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRotateX = setRotateX%8;
                        setRotateY = setRotateY%8;
                        setRotateZ = setRotateZ%8;
                        Toast.makeText(getApplicationContext(),setRotateX+""+','+setRotateY+""+','+setRotateZ+"",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent();

//                intent.putExtra("scale", String.valueOf(setScale));
//                setResult(1, intent);

//                finish();
//                setup();
                    }
                });
            }
        };
    }

    private void getContentsFiles() {
        final String contentId = dbManager.contentId;
        RequestManager requestManager = RequestManager.getInstance();


        requestManager.requestGetContentInfo(contentId, new RequestManager.ContentCallback() {
            @Override
            public void onResponse(ContentModel response) {
                contentModel = response;

                try {
                    String url = contentModel.getModel();
                    String suffix = url.substring(url.indexOf('.'), url.length());
                    RequestManager mRequestManager = RequestManager.getInstance();
                    mRequestManager.requesetDownloadFileFromStorage(contentModel.getContentName(), url, suffix, new RequestManager.TransferCallback() {
                        @Override
                        public void onResponse(TransferModel response) {
                            if (response.getSuffix().compareTo(".jet") == 0) {
                                //modelUrl = response.getPath();
                                //Log.d("model file:",modelUrl.toString());
                                String model_file_name=response.getPath().substring(response.getPath().lastIndexOf("/")+1,response.getPath().length()-4);
                                Log.d("getModel_name",model_file_name);
                                try {
                                    FileInputStream file_readed = new FileInputStream(new File(response.getPath()));
                                    saveTemptoJet(file_readed,contentModel.getContentName(),model_file_name);
                                }catch (FileNotFoundException e){
                                    e.printStackTrace();
                                }


                            }
                            Log.d(TAG, "onResponse: content download complete ");

                            //contetns Model down confirm and
                            try{
                                Log.d("markertest",contentModel.toString());

                                for(int i=0;i<contentModel.getTextures().size();i++){
                                    Log.d("texture real name",contentModel.getTextures().get(i)+"");
                                    String texture_url = contentModel.getTextures().get(i);

                                    getImage(contentModel.getContentName(),texture_url,i,contentModel.getTextures().size()-1);
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        }
                    });
                }catch (StringIndexOutOfBoundsException e){e.printStackTrace();}



            }
        });

    }

    @Override
    public void setup() {
//        materialDialog.show();

        if(contents_down_state && contentModel!=null){
            Log.d("setup","called");

            path = dbManager.imageURI.toString();
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
            arModelImporter.loadFromPath(modelUrl);
            //model's file name

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
                node3d.scaleByUniform(1.0f);
            }
            else if (textures.size()>1){
                //snake, animation
//                arModelImporter.loadFromAsset(dbManager.contentFileName);

                node3d = arModelImporter.getNode();
                node3d.setName("somthing");
        //        node3d.rotateByDegrees(90.0f,1.0f,0.0f,0.0f);
         //       node3d.rotateByDegrees(180.0f,1.0f,100.0f,0.0f);
//            ARTexture2D texture2D_1 = new ARTexture2D();

//            texture2D_1.loadFromAsset(dbManager.contentTextureFiles[0]);
//            ARLightMaterial material1 = new ARLightMaterial();
//            material1.setTexture(texture2D_1);
//            material1.setColour(1,1,1);
//            material1.setAmbient(0.8f,0.8f,0.8f);
//            material1.setName("");

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
                node3d.scaleByUniform(3.0f);

            }





            imageTrackable.getWorld().addChild(node3d);
            imageTrackable.addListener(new ARImageTrackableListener() {
                @Override
                public void didDetect (ARImageTrackable arImageTrackable){
                    if(true){
                        node3d.play();
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
            contents_down_state = true;
        }
        else {
            contents_down_state = false;
        }
    }
    public void getImage(final String name, final String url, final int request_count, final int last_count){
        try {
            String suffix = url.substring(url.indexOf('.'), url.length());
            Log.d("texture_request_suffix",suffix);
            Log.d("texture_request_url",url);
            RequestManager mRequestManager = RequestManager.getInstance();
            mRequestManager.requesetDownloadFileFromStorage(name, url, suffix, new RequestManager.TransferCallback() {
                @Override
                public void onResponse(TransferModel response) {
                    if (response.getSuffix().compareTo(".jpg") == 0 || response.getSuffix().compareTo(".png") == 0) {
                        Bitmap downBitmap = BitmapFactory.decodeFile(response.getPath());
                        //imgView.setImageBitmap(downBitmap);

                        //String texture_file_name=response.getPath().substring(response.getPath().lastIndexOf("/")+1,response.getPath().length()-4);
                        String texture_file_name = url.substring(url.lastIndexOf("/")+1,url.length()-4);
                        Log.d("getTexture_name",texture_file_name);
                        saveBitmaptoJpeg(downBitmap,name,texture_file_name);
                    }
                    Log.d(TAG, "onResponse: content download complete ");
                    String texture_url = response.getPath();
                    //Log.d("texture_path",texture_url);
                    //textures.add(texture_url);
                    contents_down_state =true;
                    //setup();

                    //very important
                    if(request_count == last_count){
                        callBackListener.onDoneBack();
                    }

                }
            });
        }catch (StringIndexOutOfBoundsException e){e.printStackTrace();}
    }


    /*
    public void getFile(final String name, String url){
        try {
            String suffix = url.substring(url.indexOf('.'), url.length());
            RequestManager mRequestManager = RequestManager.getInstance();
            mRequestManager.requesetDownloadFileFromStorage(name, url, suffix, new RequestManager.TransferCallback() {
                @Override
                public void onResponse(TransferModel response) {
                    if (response.getSuffix().compareTo(".jet") == 0) {

                    }
                        String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard
                        String foler_name = "/kudan/"+name+"/";
                        String file_name = name+".jet";
                        String string_path = ex_storage+foler_name;

                        File file_path;
                        try {
                            file_path = new File(string_path);
                            if (!file_path.isDirectory()) {
                                file_path.mkdirs();
                            }
                            File file = new File(response.getPath());
                            FileInputStream in = new FileInputStream(file);
                            in.close();
                            FileOutputStream out = new FileOutputStream(string_path + file_name);
                        }catch (FileNotFoundException e) {
                            e.getMessage();
                        }catch (IOException e){
                            e.getMessage();
                    }
                    Log.d(TAG, "onResponse: content download complete ");
                }
            });
        }catch (StringIndexOutOfBoundsException e){e.printStackTrace();}
    }*/

    public void saveTemptoJet(FileInputStream file_input,String folder, String name){
        String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard
        String foler_name = "/kudan/"+folder+"/";
        String file_name = name+".jet";
        String string_path = ex_storage+foler_name;
        File file_path;
        try{
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);
            int data;
            while ((data = file_input.read()) != -1) {
                // TODO : use data
                out.write(data);
            }
            file_input.close();
            out.close();
            file_input.close();
//            textures.add(string_path+file_name);
            modelUrl = string_path+file_name;
            Log.d("model_path",string_path+file_name);

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }
    public void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
        String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard
        String foler_name = "/kudan/"+folder+"/";
        String file_name = name+".jpg";
        String string_path = ex_storage+foler_name;
        File file_path;
        try{
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); out.close();
            textures.add(string_path+file_name);
            //Log.d("textures_path",string_path+file_name);
        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }

    }


}



package com.curonsys.army;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 */



public class MarkerGenerationFragment1 extends Fragment{
    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    public int REQUEST_GET_IMAGE_CHOICE = 0;
    public String str[] = {"meet","white"};
    ImageView previewImg;
    ImageView iv_view;
    RatingBar ratingBar;
    Button next_btn;

    Context thisContext;
    Activity mActivity;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;
    private Bitmap inputImage; // make bitmap from image resource
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);
    MyAsyncTask myAsyncTask;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_generation1, container, false);
        //next_btn = view.findViewById(R.id.next_btn_step1);
        ratingBar = view.findViewById(R.id.ratingbar);
        previewImg = view.findViewById(R.id.preview_img);
        previewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(thisContext)
                        .title("이미지 선택")
                        .titleColor(getResources().getColor(R.color.colorToTintWith))
                        .items(R.array.image_get_choice_list)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if(which==0){
                                    captureCamera();
                                }else {
                                    getAlbum();
                                }
                            }
                        })
                        .show();
            }
        });

       // next_btn.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
//                Fragment fragment2 = new MarkerGenerationFragment2();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_simple,fragment2);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
   //         }
    //    });

        return view;
    }

    private void captureCamera(){
        String state= Environment.getExternalStorageState();
        // 외장 메모리 검사
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takePictureIntent.resolveActivity(thisContext.getPackageManager())!=null){
                File photoFile=null;
                try{
                    photoFile=createImageFile();
                }catch(IOException ex){
                    Log.e("captureCamera Error",ex.toString());
                }
                if(photoFile!=null){
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                    Uri providerURI= ContentUriProvider.getUriForFile(thisContext,thisContext.getPackageName(),photoFile);
                    imageUri=providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,providerURI);


                    startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
                }
            }
        }else{
            Toast.makeText(thisContext,"저장공간이 접근 불가능한 기기입니다",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public File createImageFile()throws IOException{
        // Create an image file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG_"+timeStamp+".jpg";
        File imageFile=null;
        File storageDir=new File(Environment.getExternalStorageDirectory()+"/Pictures");
        if(!storageDir.exists()){
            Log.i("mCurrentPhotoPath1",storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile=new File(storageDir,imageFileName);
        mCurrentPhotoPath=imageFile.getAbsolutePath();

        return imageFile;
    }

    //앨범을 가져오기
    private void getAlbum(){
        Log.i("getAlbum","Call");
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent,REQUEST_TAKE_ALBUM);
    }

    // 갤러리에서 이미지 가져오기
    private void galleryAddPic(){
        Log.i("galleryAddPic","Call");
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f=new File(mCurrentPhotoPath);
        Uri contentUri=Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        thisContext.sendBroadcast(mediaScanIntent);
        Toast.makeText(thisContext,"사진이 앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show();
    }
    // 카메라 전용 크랍
    public void cropImage(){
        Log.i("cropImage","Call");
        Log.i("cropImage","photoURI : "+photoURI+" / albumURI : "+albumURI);

        Intent cropIntent=new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI,"image/*");
        //cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기, 결과물의 크기
        //cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX",1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY",1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale",true);
        cropIntent.putExtra("output",albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent,REQUEST_IMAGE_CROP);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case REQUEST_TAKE_PHOTO:
                if(resultCode== Activity.RESULT_OK){
                    try{
                        Log.i("REQUEST_TAKE_PHOTO","OK");
                        galleryAddPic();

                        previewImg.setImageURI(imageUri);
                        inputImage=MediaStore.Images.Media.getBitmap(thisContext.getContentResolver(),imageUri);
                        //myAsyncTask = new MyAsyncTask();
                        //myAsyncTask.execute();
                        Log.d("camera's uri",imageUri.toString());
                    }catch(Exception e){
                        Log.e("REQUEST_TAKE_PHOTO",e.toString());
                    }
                }else{
                    Toast.makeText(thisContext,"사진찍기를 취소하였습니다.",Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if(resultCode==Activity.RESULT_OK){

                    if(data.getData()!=null){
                        try{
                            File albumFile=null;
                            albumFile=createImageFile();
                            photoURI=data.getData();
                            albumURI=Uri.fromFile(albumFile);
                            cropImage();
                            inputImage=MediaStore.Images.Media.getBitmap(thisContext.getContentResolver(),data.getData());
                            Log.d("album's uri",data.getData().toString());
                        }catch(Exception e){
                            Log.e("TAKE_ALBUM_SINGLE ERROR",e.toString());
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if(resultCode==Activity.RESULT_OK){

                    galleryAddPic();
                    previewImg.setImageURI(albumURI);
                    try{
                        inputImage=MediaStore.Images.Media.getBitmap(thisContext.getContentResolver(),albumURI);
                        myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute();
                    }catch(FileNotFoundException e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }catch(IOException e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    break;
                }
        }
    }

    public double sift(){
        Mat rgba=new Mat();
        Utils.bitmapToMat(inputImage,rgba);
        MatOfKeyPoint keyPoints=new MatOfKeyPoint();
        Imgproc.cvtColor(rgba,rgba,Imgproc.COLOR_RGBA2GRAY);
        detector.detect(rgba,keyPoints);
        Features2d.drawKeypoints(rgba,keyPoints,rgba);
        Utils.matToBitmap(rgba,inputImage);
//        iv_view.setImageBitmap(inputImage);
        //sift_tv_result.setText("Keypoint 갯수 : " + keyPoints.toArray().length);
        double image_size = rgba.size().width*rgba.size().height;
        return keyPoints.toArray().length/image_size;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        thisContext=activity;
        if(activity.getClass()== MarkerGenerationActivity.class){
            mActivity = (MarkerGenerationActivity) activity;
        }
    }

    public class MyAsyncTask extends AsyncTask<Double, Void, Double> {
        MaterialDialog.Builder builder = null;
        MaterialDialog materialDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder = new MaterialDialog.Builder(thisContext)
                    .title("유효성 검사중")
                    .content("시간이 조금 걸릴 수 있습니다...")
                    .progress(true,0);
            materialDialog = builder.build();
            materialDialog.show();
        }

        @Override
        protected void onPostExecute(Double value) {
            super.onPostExecute(value);
            materialDialog.dismiss();
            Toast.makeText(thisContext,"유효성 검사가 완료되었습니다.",Toast.LENGTH_SHORT).show();
            ratingBar.setVisibility(View.VISIBLE);
            if(value>0.002){
                ratingBar.setRating(5);
            }else if(value>0.0015 && value<0.002)
            {
                ratingBar.setRating(4);
            }else if(value>0.0010 && value<0.0015)
            {
                ratingBar.setRating(3);
            }else if(value>0.0005 && value<0.0010)
            {
                ratingBar.setRating(2);
            }else if(value>0.0001 && value<0.0005)
            {
                ratingBar.setRating(1);
            }

            Button nextStepBtn = mActivity.findViewById(R.id.nextstepBtn);
            nextStepBtn.setClickable(true);
            nextStepBtn.setEnabled(true);
            Log.d("asyctask result",value+"");
        }

        @Override
        protected Double doInBackground(Double... params) {
            return sift();
        }
    }
}

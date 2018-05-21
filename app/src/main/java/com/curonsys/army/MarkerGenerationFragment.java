package com.curonsys.army;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 */



public class MarkerGenerationFragment extends Fragment{
    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    public int REQUEST_GET_IMAGE_CHOICE = 0;
    public String str[] = {"meet","white"};
    ImageView previewImg;
    ImageView iv_view;

    Context thisContext;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;
    private Bitmap inputImage; // make bitmap from image resource

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_generation, container, false);
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

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        thisContext=activity;
    }

}

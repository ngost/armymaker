package com.curonsys.army;

import java.util.ArrayList;

/**
 * Created by ijin-yeong on 2018. 7. 24..
 */

public class ARContents {
    public static final int MODEL_CONTENTS = 0;
    public static final int VIDEO_CONTENTS = 1;
    public static final int IMAGE_CONTENTS = 2;

    private int contentsType;
    private String contentsId="";
    private String markerImgPath="";
    private String contentsPath="";
    private ArrayList<String> texturePath =null;
    private String soundPath ="";

    //3d model
    public ARContents(String contentsId, int contentsType, String markerImgPath, String contentsPath, ArrayList<String> texture, ModelSetData modelData,String soundPath){
        this.contentsId = contentsId;
        this.contentsPath = contentsPath;
        this.markerImgPath = markerImgPath;

        if(soundPath!=null)
            this.soundPath = soundPath;

        if(contentsType == 0){
            this.contentsType = MODEL_CONTENTS;

            if(texturePath!=null)
                this.texturePath = texturePath;

        }else if(contentsType == 1){
            this.contentsType = VIDEO_CONTENTS;
        }else {
            this.contentsType = IMAGE_CONTENTS;
        }
    }

    public int getContentsType(){
        return this.contentsType;
    }

    public String getContentsId(){
        return this.contentsId;
    }

    public String getContentsPath(){
        return this.contentsPath;
    }

    public String getMarkerImgPath(){
        return this.markerImgPath;
    }
}

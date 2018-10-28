package com.curonsys.android_java;


public class ContentsListModel {


    String contents_name;
    String contents_describe;
    String contents_id;
    String thumbNailUrl;

    public ContentsListModel( String id, String name, String describe, String imgUrl) {
        this.contents_id = id;
        this.contents_name = name;
        this.contents_describe = describe;
        this.thumbNailUrl=imgUrl;
    }


    public String getName() {
        return contents_name;
    }

    public String getDescribe() {
        return contents_describe;
    }

    public String getImageURL() {
        return thumbNailUrl;
    }

    public String getId() {
        return contents_id;
    }
}
package com.curonsys.army;

import android.media.Image;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-03-28.
 */

public class mongoConnection {
    private String simpleData, URL;
    private ArrayList<Double> marker_longitude;
    private ArrayList<Double> marker_latitude;
    private ArrayList<Image> marker_image;

    InputStream is = null;
    HttpClient httpClient;
    HttpParams Params;
    HttpPost httpPost;
    private int count = 0;

    public mongoConnection(String url, String data) {
        //this.user_id = new ArrayList<Double>();
        this.simpleData = data;
        this.URL = url;
        this.marker_longitude = new ArrayList<Double>();
        this.marker_latitude = new ArrayList<Double>();
        this.marker_image = new ArrayList<Image>();

    }
    //시작포인트
    public boolean requestJoin(String mid, String mpw, String mtel, String memail, String mtype, String mserial, String mgender) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("MID", marker_longitude));
        nameValuePairs.add(new BasicNameValuePair("MPW", marker_latitude));
        nameValuePairs.add(new BasicNameValuePair("MTEL", marker_image));
        nameValuePairs.add

        /*
        nameValuePairs.add(new BasicNameValuePair("MEMAIL", memail));
        nameValuePairs.add(new BasicNameValuePair("MTYPE", mtype));
        nameValuePairs.add(new BasicNameValuePair("MSERIAL", mserial));
        nameValuePairs.add(new BasicNameValuePair("MGENDER", mgender));*/

        try {
            httpPost = new HttpPost(URL);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            httpPost.setEntity(entityRequest);
            httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d("inStream",is+"");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public int getCount() {
        return count;
    }

    public String get_marker_longitude(int index) {return marker_longitude.get(index);}

    public String get_marker_latitude(int index)  {return marker_latitude.get(index);}

    public String get_marker_image(int index)     {return marker_image.get(index);}


    public String getTPYE(int index) {
        return m_type.get(index);
    }

}

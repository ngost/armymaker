package com.curonsys.army.Connector;

/**
 * Created by Leejuhwan on 2018-06-03.
 */
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class JoinConnection {

    private String simpleData, URL;
    private ArrayList<String> user_id;
    private ArrayList<String> m_pw;
    private ArrayList<String> m_tel;
    private ArrayList<String> m_email;
    private ArrayList<String> m_type;
    private ArrayList<String> m_serial;
    private ArrayList<String> m_gender;

    InputStream is = null;
    HttpClient httpClient;
    HttpParams Params;
    HttpPost httpPost;
    private int count = 0;

    public JoinConnection(String url, String data) {
        this.user_id = new ArrayList<String>();
        this.m_pw = new ArrayList<String>();
        this.m_tel = new ArrayList<String>();
        this.m_email = new ArrayList<String>();
        this.m_type = new ArrayList<String>();
        this.m_serial = new ArrayList<String>();
        this.m_gender = new ArrayList<String>();
        this.simpleData = data;
        this.URL = url;
    }

    public boolean requestJoin(String mid, String mpw, String mtel, String memail, String mtype, String mserial, String mgender) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("MID", mid));
        nameValuePairs.add(new BasicNameValuePair("MPW", mpw));
        nameValuePairs.add(new BasicNameValuePair("MTEL", mtel));
        nameValuePairs.add(new BasicNameValuePair("MEMAIL", memail));
        nameValuePairs.add(new BasicNameValuePair("MTYPE", mtype));
        nameValuePairs.add(new BasicNameValuePair("MSERIAL", mserial));
        nameValuePairs.add(new BasicNameValuePair("MGENDER", mgender));

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

    public String getUserID(int index) {
        return user_id.get(index);
    }

    public String getPW(int index) {
        return m_pw.get(index);
    }

    public String getTPYE(int index) {
        return m_type.get(index);
    }

}

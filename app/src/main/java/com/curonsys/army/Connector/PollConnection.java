package com.curonsys.army.Connector;

/**
 * Created by Leejuhwan on 2018-06-01.
 */
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class PollConnection {
    private String StartDate, EndDate, Poll_url, simpleData;
    private ArrayList<String> CODE;
    private ArrayList<String> A_POLL;
    private ArrayList<String> B_POLL;
    private ArrayList<String> C_POLL;
    InputStream is = null;
    HttpClient httpClient;
    HttpParams Params;
    HttpPost httpPost;
    private int count = 0;

    public PollConnection(String url, String data) {
        this.CODE = new ArrayList<String>();
        this.A_POLL = new ArrayList<String>();
        this.B_POLL = new ArrayList<String>();
        this.C_POLL = new ArrayList<String>();
        this.simpleData = data;
        this.Poll_url = url;
    }

    public boolean requestPoll(String sCODE, String sPart, String sstartDate, String sendDate) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("CODE", sCODE));
        nameValuePairs.add(new BasicNameValuePair("PART", sPart));
        nameValuePairs.add(new BasicNameValuePair("STARTDATE", sstartDate));
        nameValuePairs.add(new BasicNameValuePair("ENDDATE", sendDate));
        Log.d("Part1111", sstartDate+" "+sendDate);
        String[] getJsonData = {"","","",""};

        try {
            httpPost = new HttpPost(Poll_url);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            httpPost.setEntity(entityRequest);
            Params = httpClient.getParams();
            HttpResponse response = httpClient.execute(httpPost); // 보낸 뒤, 리턴 되는 결과값을
            // 받음
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d("is1111",is + "");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            String page = "";

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), page에 저장함
            while ((line = reader.readLine()) !=null) {
                page += line;
                Log.d("page", page);
            }

            //읽어들인 내용을 json 객체에 담아 그 중 dataSend로 정의 된 내용을
            //불러온다. 그럼 json 중 원하는 내용을 하나의 json 배열에 담게 된다.

            JSONObject json = new JSONObject(page);
            JSONArray jArr = json.getJSONArray("dataSend");

            //Json이 가진 크기만큼 데이터를 받아옴

            Log.d("count1111", ""+ jArr.length());
            for (int i=0; i< jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                getJsonData[0] = json.getString("CODE");
                getJsonData[1] = json.getString("A_POLL");
                getJsonData[2] = json.getString("B_POLL");
                getJsonData[3] = json.getString("C_POLL");

                CODE.add(getJsonData[0]);
                A_POLL.add(getJsonData[1]);
                B_POLL.add(getJsonData[2]);
                C_POLL.add(getJsonData[3]);
            }
            this.count = jArr.length();
            is.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public int getCount() { return count; }

    public String getCODE(int index) { return CODE.get(index); }

    public String getA_POLL(int index) { return CODE.get(index); }

    public String getB_POLL(int index) { return CODE.get(index); }

    public String getC_POLL(int index) { return CODE.get(index); }


}












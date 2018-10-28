package com.curonsys.android_java;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleData {

    //contentName
    public static String[] nameArray = {"snake", "car", "helicopter", "bigben"};

    //contentDescribe
    public static String[] versionArray = {"뱀", "자동차", "헬리콥터", "빅밴"};

    //contentPreview
    public static Integer[] drawableArray = {R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.froyo};

    //contentId
    public static Integer[] id_ = {0, 1, 2, 3};
    //contentFileName
    public static String[] contentFileName = {"snake.jet","car.jet","helicopter.jet","bigben.jet"};
    public static ArrayList<String[]> contentTextureFiles = new ArrayList<>();
    public static ArrayList<String[]> contentTextureNames = new ArrayList<>();
    public static boolean[] contentHasAnimation = {true,false,false,false};
    public static int[] contentTextureCount = {3,1,1,1};

    public SampleData(){
        contentTextureFiles.add(new String[]{"lengua.jpg","ojo.jpg","cuerpo.jpg"});
        contentTextureFiles.add(new String[]{"car.jpg"});
        contentTextureFiles.add(new String[]{"helicopter.jpg"});
        contentTextureFiles.add(new String[]{"bigben.png"});

        contentTextureNames.add(new String[]{"lengua","ojo","cuerpo"});
        contentTextureNames.add(new String[]{"car"});
        contentTextureNames.add(new String[]{"helicopter"});
        contentTextureNames.add(new String[]{"bigben"});
    }

    private static SampleData instance;

    public static SampleData getInstance () {
        if (instance == null)
            instance = new SampleData();
        return instance;
    }

    public static JSONArray getFakeContentsList(){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("ContentsIndentify","1");
            jsonObject.put("ContentsName","snake");
            jsonObject.put("ContentsDescribe","뱀이다");
            jsonObject.put("ThumbNailUrl","https://i.pinimg.com/236x/16/a7/24/16a724c210c8dd04a3b664b4a3fab98b--orange-orange-orange-crush.jpg");
            jsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("ContentsIndentify","2");
            jsonObject.put("ContentsName","ben");
            jsonObject.put("ContentsDescribe","시계탑이다");
            jsonObject.put("ThumbNailUrl","http://www.schoolfix.com/media/catalog/product/cache/3/image/354x354/987a42a32237cecc17340b40c2347d5a/x/9/x90-16-in-double-faced-atomic-clock-wall-clocks_1.jpg");
            jsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("ContentsIndentify","3");
            jsonObject.put("ContentsName","heli");
            jsonObject.put("ContentsDescribe","헬기다");
            jsonObject.put("ThumbNailUrl","https://english.kalerkantho.com/assets/news_images/2018/01/13/thumbnails/164020Indian_copter.jpg");
            jsonArray.put(jsonObject);
        }catch (JSONException e){e.printStackTrace();}


        return jsonArray;
    }
}

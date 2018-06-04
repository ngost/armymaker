package com.curonsys.army;


import java.util.ArrayList;

public class MyData {

    //contentName
    static String[] nameArray = {"snake", "car", "helicopter", "bigben"};

    //contentDescribe
    static String[] versionArray = {"뱀", "자동차", "헬리콥터", "빅밴"};

    //contentPreview
    static Integer[] drawableArray = {R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.froyo};

    //contentId
    static Integer[] id_ = {0, 1, 2, 3};
    //contentFileName
    static String[] contentFileName = {"snake.jet","car.jet","helicopter.jet","bigben.jet"};
    static ArrayList<String[]> contentTextureFiles = new ArrayList<>();
    static boolean[] contentHasAnimation = {true,false,false,false};
    static int[] contentTextureCount = {2,1,1,1};

    public MyData(){
        contentTextureFiles.add(new String[]{"snake1.png","snake2.png"});
        contentTextureFiles.add(new String[]{"car1.png"});
        contentTextureFiles.add(new String[]{"helicopter1.png"});
        contentTextureFiles.add(new String[]{"bigben1.png"});
    }

    private static MyData instance;

    public static MyData getInstance () {
        if (instance == null)
            instance = new MyData();
        return instance;
    }
}

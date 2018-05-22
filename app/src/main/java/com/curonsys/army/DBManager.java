package com.curonsys.army;

import android.content.Context;
import android.util.Log;

/**
 * Created by Leejuhwan on 2018-05-22.
 */

public class DBManager {
    Context context;
    public  DBManager(Context context){
        this.context = context;
    }

    public boolean initDb(){
        Log.d("DBManager","inited");
        return true;
    }
}

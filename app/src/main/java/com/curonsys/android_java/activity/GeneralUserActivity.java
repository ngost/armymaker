package com.curonsys.android_java.activity;

import android.os.Bundle;

import com.curonsys.android_java.R;

import eu.kudan.kudan.ARActivity;

public class GeneralUserActivity extends ARActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user);
    }
}

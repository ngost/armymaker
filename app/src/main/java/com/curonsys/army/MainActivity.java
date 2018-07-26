package com.curonsys.army;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import eu.kudan.kudan.ARAPIKey;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("lNNBHsE9P8I7VtEZ4Y/NnO0Vs+CkOdKQDdfP2JfLRBqZigQ3iQaaC7mnxCYwatWKMrXVCRk8U972dXK7qFbAC+b7cHk5tiDufSXLKOE9ZGEu5PebF7HDc+zSE18WfthhZHbyL9VWk5/2EO7dLO8Y5WVaADfc2aIs3ITuJ+FtrzjsfzsbTKjwDdkzrxDBOX4DQRO4yB0S2RP6dmPkkf2bjAqj704C/mM6iXm2ARZDLzgzm83oosCv3v4nYGdIBCz+9BASSljzLy2/2H3wXj7N9dk0YVGSNMzy59yyZZv/TaZ1f8m8crfXjyRlZy8tcFCr9SzZxTXMpz2SCCFy7SxBx5Tkv0nJPhOwiLXXaKumwdMDsZAmeUk5W/EDmFTVjQdXOGvIRwG4Xxg3uoWCBR1Dx49OLf8GnkHdU+ogbARjYeqBnDx3eNDaS6bYCtzUhf6PF15atk5r94oPIyi+89CQ+bD7MO7Fm5USv6pwJ2Hl4h+LYc54nLq39ZRVH1ScSPzTA/tycwQ6/2m7VBaU9q51B0jZwFyfiXW1OFUouKPgY0w1roJGxaVKX2QYJgr0sVv05GXoP2pjwFY7wwWhJ+uTWNUj6Nk0Zj/ejXJLBvq1EnuLBlsqh+7SwmZSo99qSymDMQwEAEh4smDXjd0dDEUyEyUpGvZfgHBakvlWpp3M8OY=");

        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.permissionCheck();

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MarkerGenerationActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeneralUserActivity.class);
                startActivity(intent);

            }
        });
    }


}

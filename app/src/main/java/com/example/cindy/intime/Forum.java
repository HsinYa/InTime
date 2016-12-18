package com.example.cindy.intime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        send = (Button)findViewById(R.id.send);
        
    }
}

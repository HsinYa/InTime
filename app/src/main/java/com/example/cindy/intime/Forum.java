package com.example.cindy.intime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    private Button send;
    private Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(sendListener);

        go = (Button)findViewById(R.id.go);
        //go.setOnClickListener(goListener);
    }

    //send action
    private Button.OnClickListener sendListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v){
                    // 取得 LinearLayout 物件
                    LinearLayout ll = (LinearLayout)findViewById(R.id.ll_in_sv);

                    // 將feedviews加入到 LinearLayout 中
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.feedviews , null, true);
                    ll.addView(view);

                }
            };

//    //go action
//    private Button.OnClickListener goListener =
//            new Button.OnClickListener() {
//                @Override
//                public void onClick(View v){
//
////                    // 取得 LinearLayout 物件
////                    LinearLayout display = (LinearLayout)findViewById(R.id.display);
////
////                    // 將response加入到 LinearLayout 中
////                    LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                    View view2 = inflater2.inflate(R.layout.response, null, true);
////                    display.addView(view2);
//                }
//            };

}

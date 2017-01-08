package com.example.cindy.intime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    private Button send;
    private Button go;
    private EditText msg;
    private TextView askmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(sendListener);

        msg = (EditText)findViewById(R.id.message);
    }

    //send action
    private Button.OnClickListener sendListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v){
                    //取得chatroom message 的內容
                    String content = msg.getText().toString();

                    // 取得 LinearLayout 物件
                    LinearLayout ll = (LinearLayout)findViewById(R.id.ll_in_sv);

                    // 將feedviews加入到 LinearLayout 中
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(R.layout.feedviews , null, true);
                    ll.addView(view);

                    //取得新產生的feedviews layout
                    askmsg = (TextView)view.findViewById(R.id.askmsg);
                    //替換feedviews的內容
                    askmsg.setText(content);
                    //clear ask message
                    msg.setText("");

                    //go action
                    go = (Button)view.findViewById(R.id.go);
                    go.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            // 取得 LinearLayout 物件
                            LinearLayout display = (LinearLayout)view.findViewById(R.id.display);

                            // 將response加入到 feedviews layout中
                            LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view2 = inflater2.inflate(R.layout.response, null, true);
                            display.addView(view2);
                        }
                    });
                }
            };


}
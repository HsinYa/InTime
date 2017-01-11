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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    private Button send;
    private Button go;
    private EditText msg,reply_text;
    private TextView askmsg,show_reply,askid,relyid;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("name");

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
                    askid = (TextView)view.findViewById(R.id.askID);
                    //替換feedviews的內容
                    askid.setText(userName);
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

                            reply_text = (EditText)view.findViewById(R.id.reply_text);
                            //取得reply的內容
                            String text = reply_text.getText().toString();
                            //取得新產生的respnse layout
                            show_reply = (TextView)view2.findViewById(R.id.show_reply);
                            relyid = (TextView)view2.findViewById(R.id.replyID);
                            //替換response的內容
                            relyid.setText(userName);
                            show_reply.setText(text);
                            //clear replay content
                            reply_text.setText("");
                        }
                    });
                }
            };


}

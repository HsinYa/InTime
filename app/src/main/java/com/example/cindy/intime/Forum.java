package com.example.cindy.intime;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
//MA
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    private Button send;
    private Button go;
    private EditText msg,reply_text;
    private TextView askmsg,show_reply;
    //MA
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private String uid;
    private String owner;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference cFirebaseDatabase;
    private DatabaseReference rFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(sendListener);

        msg = (EditText)findViewById(R.id.message);

        //MA
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        // get reference to 'contents' node
        cFirebaseDatabase = mFirebaseInstance.getReference("contents");
        // get reference to 'response' node
        rFirebaseDatabase = mFirebaseInstance.getReference("response");
    }


    //send action
    private Button.OnClickListener sendListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v){
                    //取得chatroom message 的內容
                    String content = msg.getText().toString();

                    //MA
                    //Get the object of  FirebaseAuth
                    auth = FirebaseAuth.getInstance();
                    //Judge the state of login or logout
                    authListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(
                                @NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user!=null) {
                                Log.d("onAuthStateChanged", "登入:"+
                                        user.getUid());
                                uid = user.getUid();
                            }else{
                                Log.d("onAuthStateChanged", "未登入");
                            }
                        }
                    };

                    Query queryRef = mFirebaseDatabase.orderByChild("users").equalTo(uid);
                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                            User user = snapshot.getValue(User.class);
                            Log.d("FireBaseTraining", "name = " + user.getName());
                            owner = user.getName();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    Calendar c = Calendar.getInstance();
                    final long time = c.getTimeInMillis();

                    Content content1 = new Content(content,owner,time);
                    cFirebaseDatabase.push().setValue(content1);

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

                            reply_text = (EditText)view.findViewById(R.id.reply_text);
                            //取得reply的內容
                            String text = reply_text.getText().toString();
                            //取得新產生的respnse layout
                            show_reply = (TextView)view2.findViewById(R.id.show_reply);
                            //替換response的內容
                            show_reply.setText(text);
                            //clear replay content
                            reply_text.setText("");
                        }
                    });
                }
            };


}

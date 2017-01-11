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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

//import static com.example.cindy.intime.R.id.askID;
//end MA

/**
 * Created by cindy on 2016/12/18.
 */

public class Forum extends AppCompatActivity{
    private Button send;
    private Button go;
    private EditText msg,reply_text;
    private TextView askmsg,show_reply,whoask,replyID;
    //MA
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String uid;
    public String owner;
    public String content;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference cFirebaseDatabase;
    private DatabaseReference rFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        Context mContext = Forum.this;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View feed = inflater.inflate(R.layout.feedviews, null);
        //View res = inflater.inflate(R.layout.response, null);

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

        loadUser();
        loadContent();
        loadResponse();
        loader();
        //end MA
    }

    //send action
    private Button.OnClickListener sendListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v){
                    //取得chatroom message 的內容
                    content = msg.getText().toString();

                    //---------------------------------------------------//
                    //MA

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currUser = mAuth.getCurrentUser();
                    Query queryRef = mFirebaseDatabase.orderByKey().equalTo(currUser.getUid());
                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                            User user = snapshot.getValue(User.class);
                            Log.d("抓取使用者：", "name = " + user.getName());
                            owner = user.getName();
                            //--------------------------------------------------//
                            Calendar c = Calendar.getInstance();

                            final long time = -1* c.getTimeInMillis();

                            Content content1 = new Content(content,owner,time);

                            whoask.setText(owner);

                            cFirebaseDatabase.push().setValue(content1);
                            //--------------------------------------------------//

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


                    //end MA
                    //---------------------------------------------------//

                    // 取得 LinearLayout 物件
                    LinearLayout ll = (LinearLayout)findViewById(R.id.ll_in_sv);

                    // 將feedviews加入到 LinearLayout 中
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(R.layout.feedviews , null, true);
                    whoask = (TextView)view.findViewById(R.id.askID);//MA
                    //ll.addView(view);

                    //取得新產生的feedviews layout
                    //askmsg = (TextView)view.findViewById(R.id.askmsg);
                    //替換feedviews的內容

                   // askmsg.setText(content);
                    //clear ask message
                   // msg.setText("");

                    //go action
//                    go = (Button)view.findViewById(R.id.go);
//                    go.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v){
//                            // 取得 LinearLayout 物件
//                            LinearLayout display = (LinearLayout)view.findViewById(R.id.display);
//
//                            // 將response加入到 feedviews layout中
//                            LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            View view2 = inflater2.inflate(R.layout.response, null, true);
//                            replyID = (TextView)view2.findViewById(R.id.replyID);//MA
//                            replyID.setText(owner);//MA
//                            display.addView(view2);
//
//                            reply_text = (EditText)view.findViewById(R.id.reply_text);
//                            //取得reply的內容
//                            String response = reply_text.getText().toString();
//
//                            //---------------------------------------------------//
//                            //MA
//
//                            Calendar c2 = Calendar.getInstance();
//
//                            final long time2 = -1* c2.getTimeInMillis();
//
//                            Response response1 = new Response(response,owner,time2,content);
//
//                            rFirebaseDatabase.push().setValue(response1);
//
//                            //end MA
//                            //---------------------------------------------------//
//
//                            String text = reply_text.getText().toString();
//                            //取得新產生的respnse layout
//                            show_reply = (TextView)view2.findViewById(R.id.show_reply);
//                            //替換response的內容
//                            show_reply.setText(text);
//                            //clear replay content
//                            reply_text.setText("");
//                        }
//                    });
                }
            };


    public void loader(){
        Query queryContent = cFirebaseDatabase.orderByChild("c_time");

        queryContent.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Content content2 = dataSnapshot.getValue(Content.class);

                // 取得 LinearLayout 物件
                LinearLayout ll = (LinearLayout)findViewById(R.id.ll_in_sv);

                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.feedviews , null, true);

                whoask = (TextView)view.findViewById(R.id.askID);
                askmsg = (TextView)view.findViewById(R.id.askmsg);

                askmsg.setText(content2.getC_title());
                whoask.setText(content2.getC_owner());

                ll.addView(view);
                responseloader(content2.getC_title(),view);

                //askmsg.setText(content);
                //clear ask message
                msg.setText("");

                go = (Button)view.findViewById(R.id.go);
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        // 取得 LinearLayout 物件
                        LinearLayout display = (LinearLayout)view.findViewById(R.id.display);

                        // 將response加入到 feedviews layout中
                        LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view2 = inflater2.inflate(R.layout.response, null, true);
                        replyID = (TextView)view2.findViewById(R.id.replyID);//MA
                        replyID.setText(content2.getC_owner());//MA
                        //display.addView(view2);

                        reply_text = (EditText)view.findViewById(R.id.reply_text);
                        //取得reply的內容
                        String response = reply_text.getText().toString();

                        //---------------------------------------------------//
                        //MA

                        Calendar c2 = Calendar.getInstance();

                        final long time2 = -1* c2.getTimeInMillis();

                        Response response1 = new Response(response,content2.getC_owner(),time2,content2.getC_title());

                        rFirebaseDatabase.push().setValue(response1);

                        //end MA
                        //---------------------------------------------------//

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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Content content2 = dataSnapshot.getValue(Content.class);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Content content2 = dataSnapshot.getValue(Content.class);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Content content2 = dataSnapshot.getValue(Content.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    public void responseloader(String title, final View view){
       Query queryRes = rFirebaseDatabase.orderByChild("whichcontent").equalTo(title);
        //Query q2 = rFirebaseDatabase.orderByChild("r_time");
        queryRes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Response res = dataSnapshot.getValue(Response.class);

                Log.d("動態新增XXXXD" ,"使用者："+ res.getR_owner() + " , Response = " + res.getR_text());

                // 取得 LinearLayout 物件
                LinearLayout display = (LinearLayout)view.findViewById(R.id.display);

                // 將response加入到 feedviews layout中
                LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater2.inflate(R.layout.response, null, true);

                replyID = (TextView)view2.findViewById(R.id.replyID);
                show_reply = (TextView) view2.findViewById(R.id.show_reply);

                replyID.setText(res.getR_owner());
                show_reply.setText(res.getR_text());

                display.addView(view2);

                //show_reply.setText("");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Response res = dataSnapshot.getValue(Response.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Response res = dataSnapshot.getValue(Response.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Content content2 = dataSnapshot.getValue(Content.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }


    public void loadUser(){
        //myFirebaseRef = new Firebase("your_reference_path/users");
        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("動態新增1" ,"使用者："+ user.getName() + " , User = " + user.getTitle());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("動態新增2" ,"使用者："+ user.getName() + " , User = " + user.getTitle());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("動態新增3" ,"使用者："+ user.getName() + " , User = " + user.getTitle());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("動態新增4" ,"使用者："+ user.getName() + " , User = " + user.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadResponse(){
        rFirebaseDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Response response = dataSnapshot.getValue(Response.class);
                Log.d("動態新增1" ,"使用者："+ response.getR_owner() + " , Response = " + response.getR_text());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Response response = dataSnapshot.getValue(Response.class);
                Log.d("動態新增2" ,"使用者："+ response.getR_owner() + " , Response = " + response.getR_text());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Response response = dataSnapshot.getValue(Response.class);
                Log.d("動態新增3" ,"使用者："+ response.getR_owner() + " , Response = " + response.getR_text());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Response response = dataSnapshot.getValue(Response.class);
                Log.d("動態新增4" ,"使用者："+ response.getR_owner() + " , Response = " + response.getR_text());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadContent(){
        cFirebaseDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Content con = dataSnapshot.getValue(Content.class);
                Log.d("動態新增1" ,"使用者："+ con.getC_owner() + " , Content = " + con.getC_title());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Content con = dataSnapshot.getValue(Content.class);
                Log.d("動態新增2" ,"使用者："+ con.getC_owner() + " , Content = " + con.getC_title());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Content con = dataSnapshot.getValue(Content.class);
                Log.d("動態新增3" ,"使用者："+ con.getC_owner() + " , Content = " + con.getC_title());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Content con = dataSnapshot.getValue(Content.class);
                Log.d("動態新增4" ,"使用者："+ con.getC_owner() + " , Content = " + con.getC_title());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

     /*public void load(){
        //Get datasnapshot at your "users" root node
        DatabaseReference ref = cFirebaseDatabase.child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectContent((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }*/
    /*private void collectContent(Map<String,Object> contents) {

        ArrayList<Long> Content = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : contents.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            Content.add((Long) singleUser.get("phone"));
        }

        Log.d("留言：", "內容 = " + Content.toString());
        //System.out.println(Content.toString());
    }*/



}

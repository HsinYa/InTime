package com.example.cindy.intime;
//Collaborator: MA
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    private RadioGroup rgroup;
    private RadioButton rstudent;
    private RadioButton rteacher;
    private String title; //student or teacher

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                    userUID =  user.getUid();
                }else{
                    Log.d("onAuthStateChanged", "已登出");
                }
            }
        };

        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        //RadioGroup checked and handle
        //life cycle problem
        rgroup = (RadioGroup)findViewById(R.id.rgroup);
        rstudent = (RadioButton)findViewById(R.id.radioStudent);
        rteacher = (RadioButton)findViewById(R.id.radioTeacher);
        rgroup.setOnCheckedChangeListener(listener);

    }

    //when MainActivity  first display or run from background
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    //when MainActivity stop or entry background
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    //Login with id ,email,password and title
    public void login(View v){
        final String id = ((EditText)findViewById(R.id.idName))
                .getText().toString();
        final String email = ((EditText)findViewById(R.id.email))
                .getText().toString();
        final String password = ((EditText)findViewById(R.id.password))
                .getText().toString();
        Log.d("AUTH", email+"/"+password);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Log.d("onComplete", "登入失敗");
                            register(id,email, password,title);
                        }
                    }
                });
    }

    //RadioGroup checked and handle the title of teacher or student
    private RadioGroup.OnCheckedChangeListener listener = new
            RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup group,int checkedId){
                    int p = group.indexOfChild((RadioButton)findViewById(checkedId));
                    switch (checkedId){
                        case R.id.radioStudent:
                            title = "student";
                            break;
                        case R.id.radioTeacher:
                            title = "teacher";
                            break;
                    }
                }
            };

    //register a new user
    private void register(final String id,final String email, final String password,final String title) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createUser(id,email, password,title);
                            }
                        })
                .setNeutralButton("取消", null)
                .show();
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(final String id,final String email, final String password,final String title) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message =
                                        task.isSuccessful() ? "註冊成功" : "註冊失敗";

                                if(task.isSuccessful()==true){
                                    //this userId should be fetched by implementing firebase auth
                                    // Creating new user node, which returns the unique key value
                                    // new user node would be /users/$userid/
                                    if(TextUtils.isEmpty(userUID)){
                                        userUID = mFirebaseDatabase.push().getKey();
                                    }

                                    User user = new User(id,email,password,title);
                                    String fireBaseId = task.getResult().getUser().getUid();
                                    //get the reference to ‘users’ node using child() method
                                    //use setValue() method to store the user data
                                    mFirebaseDatabase.child(fireBaseId).setValue(user);
                                }

                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });

    }

}

package com.example.cindy.intime;

/**
 * Created by cindy on 2016/12/10.
 */

public class User {
    //information of user account
    public String name; //id
    public String email;
    public String password;
    public String title; //student or teacher

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User(){

    }

    public User(String name,String email,String password,String title){
        this.name = name;
        this.email = email;
        this.password = password;
        this.title = title;
    }

    public String getName(){
        return name;
    }
}

package com.example.cindy.intime;

/**
 * Created by Solitude6060 on 2017/1/11.
 */

public class Content {
    public String c_title;
    public String c_owner;
    public long c_time;


    public Content(){

    }

    public Content(String c_title, String c_owner, long c_time){
        this.c_owner = c_owner;
        this.c_time = c_time;
        this.c_title = c_title;
    }

    public void setC_owner(){
        this.c_owner = c_owner;
    }

    public String getC_owner(){
        return c_owner;
    }

    public void setC_title(){
        this.c_title = c_title;
    }

    public String getC_title(){
        return c_title;
    }

    public void setC_time(){
        this.c_time = c_time;
    }

    public long getC_time(){
        return c_time;
    }


}

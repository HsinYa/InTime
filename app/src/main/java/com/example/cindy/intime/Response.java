package com.example.cindy.intime;

/**
 * Created by Solitude6060 on 2017/1/11.
 */

public class Response {
    public String r_text;
    public String r_owner;
    public long r_time;
    public  String whichcontent;


    public Response(){

    }

    public Response(String r_text, String r_owner, long r_time, String whichcontent){
        this.r_owner = r_owner;
        this.r_time = r_time;
        this.r_text = r_text;
        this.whichcontent = whichcontent;
    }

    public void setR_owner(){
        this.r_owner = r_owner;
    }

    public String getR_owner(){
        return r_owner;
    }

    public void setR_text(){
        this.r_text = r_text;
    }

    public String getR_text(){
        return r_text;
    }

    public void setR_time(){
        this.r_time = r_time;
    }

    public long getR_time(){
        return r_time;
    }

    public void setWhichcontent(){
        this.whichcontent = whichcontent;
    }




}

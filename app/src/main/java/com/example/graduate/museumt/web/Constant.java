package com.example.graduate.museumt.web;

import android.os.Environment;

public class Constant {
    public static final String PATH;
    public static final String CODE_SUCCESS = "200";
    public static final String BASEURL = "http://139.199.64.249:8080/museum-0.0.1-SNAPSHOT/";
    public static final String WEBDIR = "/log_web";

    static {
        PATH = Environment.getExternalStorageDirectory().getPath() + "/museum/phone";
    }
}

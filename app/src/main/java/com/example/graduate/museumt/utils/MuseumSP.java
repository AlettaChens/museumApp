package com.example.graduate.museumt.utils;

import android.content.Context;

import com.example.graduate.museumt.base.BaseSPUtil;


public class MuseumSP extends BaseSPUtil {
    public MuseumSP(Context context) {
        super(context, "museum_sp");
    }


    public void putIsLogin(boolean isLogin) {
        putBoolean("isLogin", isLogin);
    }

    public Boolean getisLogin() {
        return getBoolean("isLogin", false);
    }

    public void putUserId(long userId) {
        putLong("userId", userId);
    }

    public long getUserId() {
        return getLong("userId", 0);
    }

    public void putNickName(String nickname) {
        putString("nickname", nickname);
    }

    public String getNickName() {
        return getString("nickname", "");
    }

    public void putPassword(String password) {
        putString("password", password);
    }

    public String getPassword() {
        return getString("password", "");
    }


    public void putSex(String sex) {
        putString("sex", sex);
    }

    public String getSex() {
        return getString("sex", "");
    }

    public void putAddress(String address) {
        putString("address", address);
    }

    public String getAddress() {
        return getString("address", "");
    }

    public void putAge(String age) {
        putString("age", age);
    }

    public String getAge() {
        return getString("age", "");
    }


    public void putAvatar(String avatar) {
        putString("avatar", avatar);
    }

    public String getAvatar() {
        return getString("avatar", "");
    }

}

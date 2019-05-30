package com.example.graduate.museumt.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;
import com.example.graduate.museumt.utils.MuseumSP;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {
    MuseumSP museumSP;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onInit() {
        museumSP = new MuseumSP(WelcomeActivity.this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindData() {
        Observable.timer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (museumSP.getisLogin()) {
                    skip(WelcomeActivity.this, MainActivity.class);
                } else {
                    skip(WelcomeActivity.this, LoginActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}

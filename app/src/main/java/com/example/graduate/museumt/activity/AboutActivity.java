package com.example.graduate.museumt.activity;

import android.widget.ImageView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity {


	@BindView(R.id.backend)
	ImageView backend;
	@BindView(R.id.logo)
	ImageView logo;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_about;
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onBindData() {

	}

	@OnClick(R.id.backend)
	public void onClick() {
		onBackPressed();
	}
}

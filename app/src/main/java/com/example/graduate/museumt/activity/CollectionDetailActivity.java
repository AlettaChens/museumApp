package com.example.graduate.museumt.activity;

import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;
import com.example.graduate.museumt.bean.Collection;
import com.example.graduate.museumt.utils.GlideX;
import com.example.graduate.museumt.utils.MessageUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectionDetailActivity extends BaseActivity {
	@BindView(R.id.backend)
	ImageView backend;
	@BindView(R.id.pic)
	ImageView pic;
	@BindView(R.id.name)
	TextView name;
	@BindView(R.id.type)
	TextView type;
	@BindView(R.id.voice)
	ImageView voice;
	@BindView(R.id.des)
	TextView des;
	@BindView(R.id.cv_recipe_picture)
	CardView cvRecipePicture;
	Collection collection;
	private TextToSpeech mSpeech;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_collection_detail;
	}

	@Override
	public void onInit() {
		collection = (Collection) getIntent().getSerializableExtra("collection");
		mSpeech = new TextToSpeech(CollectionDetailActivity.this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mSpeech.setLanguage(Locale.CHINA);
					if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
						MessageUtils.showLongToast(CollectionDetailActivity.this, "语音初始化失败");
					}
				}
			}
		});
	}

	@Override
	public void onBindData() {
		GlideX.getInstance().loadImage(CollectionDetailActivity.this, collection.getAvatar(), pic);
		name.setText(collection.getName());
		type.setText(collection.getType());
		des.setText("描述:" + collection.getDescription());
	}

	@OnClick({R.id.backend, R.id.voice})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.backend:
				onBackPressed();
				break;
			case R.id.voice:
				mSpeech.speak(collection.getDescription(), TextToSpeech.QUEUE_FLUSH, null);
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSpeech != null) {
			mSpeech.stop();
			mSpeech.shutdown();
		}

	}
}

package com.example.graduate.museumt.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.activity.CollectionDetailActivity;
import com.example.graduate.museumt.adapter.ClassAdapter;
import com.example.graduate.museumt.base.BaseFragment;
import com.example.graduate.museumt.bean.Collection;
import com.example.graduate.museumt.utils.MessageUtils;
import com.example.graduate.museumt.web.WebAPIManager;
import com.example.graduate.museumt.web.WebResponse;
import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.graduate.museumt.web.Constant.CODE_SUCCESS;

public class ClassFragment extends BaseFragment {


	private static final int REQUEST_SCAN = 0;
	List<Fragment> fragments;
	List<String> titles;
	ClassAdapter classAdapter;
	@BindView(R.id.iv_scan)
	ImageView ivScan;
	@BindView(R.id.recipe_class_title)
	RelativeLayout recipeClassTitle;
	@BindView(R.id.tl_class)
	TabLayout tlClass;
	@BindView(R.id.vp_class)
	ViewPager vpClass;
	LifecycleProvider<Lifecycle.Event> lifecycleProvider;


	private void getRuntimeRight() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
		} else {
			jumpScanPage();
		}
	}

	@Override
	protected int getContentViewId() {
		return R.layout.fragment_class;
	}

	@Override
	public void onInit() {
		lifecycleProvider= AndroidLifecycle.createLifecycleProvider(this);
		if (fragments == null) {
			fragments = new ArrayList<>();
		}
		if (titles == null) {
			titles = new ArrayList<>();
		}
		titles.add("推荐");
		titles.add("玉器");
		titles.add("铜器");
		titles.add("金银器");
		titles.add("瓷器");
		titles.add("玻璃器");
		titles.add("纺织品");
		titles.add("纸类文物");
		HomeFragment homeFragment = new HomeFragment();
		fragments.add(homeFragment);
		for (int i = 1; i < titles.size(); i++) {
			Bundle args = new Bundle();
			args.putString("title", titles.get(i));
			CollectionListFragment fragment = new CollectionListFragment();
			fragment.setArguments(args);
			fragments.add(fragment);
		}
		classAdapter = new ClassAdapter(getChildFragmentManager(), fragments, titles);
	}

	@Override
	public void onBindData() {
		vpClass.setAdapter(classAdapter);
		tlClass.setupWithViewPager(vpClass);
	}

	private void jumpScanPage() {
		startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_SCAN);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					jumpScanPage();
				} else {
					Toast.makeText(getActivity(), "用户拒绝授权", Toast.LENGTH_LONG).show();
				}
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
			getInfo(Long.parseLong(data.getStringExtra("barCode")));
		}
	}

	@SuppressLint("CheckResult")
	private void getInfo(long infoId) {
		WebAPIManager.getInstance(getActivity()).getCollectionById(infoId).subscribeOn(Schedulers.io())
				.compose(lifecycleProvider.bindToLifecycle())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<WebResponse<Collection>>() {
					@Override
					public void accept(WebResponse<Collection> collectionWebResponse) throws Exception {
						if(collectionWebResponse.getCode().equals(CODE_SUCCESS)){
							Intent intent=new Intent(getActivity(), CollectionDetailActivity.class);
							intent.putExtra("collection",collectionWebResponse.getData());
							startActivity(intent);
						}else {
							MessageUtils.showLongToast(getActivity(),"获取失败");
						}
					}
				});
	}


	@OnClick(R.id.iv_scan)
	public void onClick() {
		getRuntimeRight();
	}
}

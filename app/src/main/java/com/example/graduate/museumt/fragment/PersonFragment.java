package com.example.graduate.museumt.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.activity.AboutActivity;
import com.example.graduate.museumt.activity.UpdateInfoActivity;
import com.example.graduate.museumt.base.BaseFragment;
import com.example.graduate.museumt.dialog.PhotoSelectDialog;
import com.example.graduate.museumt.utils.DataCleanManager;
import com.example.graduate.museumt.utils.GlideImageLoader;
import com.example.graduate.museumt.utils.GlideX;
import com.example.graduate.museumt.utils.MessageUtils;
import com.example.graduate.museumt.utils.MuseumSP;
import com.example.graduate.museumt.web.WebAPIManager;
import com.example.graduate.museumt.web.WebResponse;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.graduate.museumt.web.Constant.CODE_SUCCESS;

public class PersonFragment extends BaseFragment {

	@BindView(R.id.user_avatar)
	CircleImageView userAvatar;
	@BindView(R.id.userName)
	TextView userName;
	@BindView(R.id.iv_share_friend)
	ImageView ivShareFriend;
	@BindView(R.id.iv_edit_info)
	ImageView ivEditInfo;
	@BindView(R.id.rl_edit_info)
	RelativeLayout rlEditInfo;
	@BindView(R.id.iv_remove_cache)
	ImageView ivRemoveCache;
	@BindView(R.id.cacheText)
	TextView cacheText;
	@BindView(R.id.right_arrow)
	ImageView rightArrow;
	@BindView(R.id.rl_remove_cache)
	RelativeLayout rlRemoveCache;
	@BindView(R.id.iv_about)
	ImageView ivAbout;
	@BindView(R.id.rl_about)
	RelativeLayout rlAbout;
	@BindView(R.id.btn_logout)
	Button btnLogout;
	@BindView(R.id.vp_banner)
	Banner vpBanner;
	LifecycleProvider<Lifecycle.Event> lifecycleProvider;
	PhotoSelectDialog photoSelectDialog;
	MuseumSP museumSP;
	List<Integer> picList;

	@Override
	protected int getContentViewId() {
		return R.layout.fragment_person;
	}

	@Override
	public void onInit() {
		museumSP = new MuseumSP(getActivity());
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
		if (picList == null) {
			picList = new ArrayList<>();
		}
		picList.add(R.mipmap.pic1);
		picList.add(R.mipmap.pic2);
		picList.add(R.mipmap.pic3);
		picList.add(R.mipmap.pic4);
		picList.add(R.mipmap.pic5);
		picList.add(R.mipmap.pic6);
		picList.add(R.mipmap.pic7);
		picList.add(R.mipmap.pic8);
		picList.add(R.mipmap.pic9);
	}

	@Override
	public void onBindData() {
		setBanner();
		if (!museumSP.getAvatar().equals("")) {
			GlideX.getInstance().loadImage(getActivity(), museumSP.getAvatar(), userAvatar);
		}
		userName.setText(museumSP.getNickName());
		try {
			cacheText.setText(DataCleanManager.getTotalCacheSize(getActivity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (museumSP.getAvatar() != null) {
			GlideX.getInstance().loadImage(getActivity(), museumSP.getAvatar(), userAvatar);
		}
		userName.setText(museumSP.getNickName());
	}

	@OnClick({R.id.user_avatar, R.id.iv_share_friend, R.id.rl_edit_info, R.id.rl_remove_cache, R.id.rl_about, R.id.btn_logout})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.user_avatar:
				showPhotoChoice();
				break;
			case R.id.iv_share_friend:
				shareClass();
				break;
			case R.id.rl_edit_info:
				startActivity(new Intent(getActivity(), UpdateInfoActivity.class));
				break;
			case R.id.rl_remove_cache:
				removeCache();
				break;
			case R.id.rl_about:
				startActivity(new Intent(getActivity(), AboutActivity.class));
				break;
			case R.id.btn_logout:
				clearSP();
				getActivity().finish();
				break;
		}
	}

	private void setBanner() {
		vpBanner.setImageLoader(new GlideImageLoader());
		vpBanner.setImages(picList);
		vpBanner.setBannerAnimation(Transformer.Default);
		vpBanner.isAutoPlay(true);
		vpBanner.setDelayTime(2000);
		vpBanner.start();
	}

	private void showPhotoChoice() {
		photoSelectDialog = new PhotoSelectDialog(getActivity(), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				photoSelectDialog.dismiss();
				switch (v.getId()) {
					case R.id.btn_take_photo:
						PictureSelector.create(PersonFragment.this).openCamera(PictureMimeType.ofImage()).enableCrop(true).compress(true).forResult
								(PictureConfig.CHOOSE_REQUEST);
						break;
					case R.id.btn_pick_photo:
						PictureSelector.create(PersonFragment.this).openGallery(PictureMimeType.ofImage()).enableCrop(true).compress(true).selectionMode
								(PictureConfig.SINGLE).forResult(PictureConfig.CHOOSE_REQUEST);
						break;
					default:
						break;
				}
			}
		});
		photoSelectDialog.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		List<LocalMedia> images;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case PictureConfig.CHOOSE_REQUEST:
					images = PictureSelector.obtainMultipleResult(data);
					File file = new File(images.get(0).getCompressPath());
					RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
					MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
					uploadUserAvatar(body);
					break;
			}
		}
	}

	private void shareClass() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
		intent.putExtra(Intent.EXTRA_TEXT, "嗨，推荐一个超级好用的博物馆藏品app给你，https://www.jianshu.com/u/d5988e23ed20");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "推荐给好友"));
	}

	private void uploadUserAvatar(MultipartBody.Part body) {
		loadDialog(getActivity(), "更新头像数据中...");
		WebAPIManager.getInstance(getActivity()).uploadUserAvatar(museumSP.getUserId(), body).subscribeOn(Schedulers.io()).compose(lifecycleProvider
				.bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse<String>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse<String> s) {
				if (s.getCode().equals(CODE_SUCCESS)) {
					museumSP.putAvatar(s.getData());
					GlideX.getInstance().loadImage(getActivity(), s.getData(), userAvatar);
					MessageUtils.showShortToast(getActivity(), "更新成功");
				} else {
					MessageUtils.showShortToast(getActivity(), "更新失败");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showShortToast(getActivity(), e.toString());
				Log.e("test", e.toString());
			}

			@Override
			public void onComplete() {
				loadDialogDismiss();
			}
		});
	}

	private void clearSP() {
		museumSP.putAvatar("");
		museumSP.putNickName("");
		museumSP.putUserId(1);
		museumSP.putIsLogin(false);
	}

	private void removeCache() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				DataCleanManager.clearAllCache(getActivity());
				cacheText.setText("0.00k");
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		builder.setTitle("缓存清理确认");
		builder.setMessage("确认要清除缓存吗？");
		builder.show();
	}
}

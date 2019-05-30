package com.example.graduate.museumt.activity;

import android.arch.lifecycle.Lifecycle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;
import com.example.graduate.museumt.dialog.UpdateUserInfoDialog;
import com.example.graduate.museumt.utils.GlideX;
import com.example.graduate.museumt.utils.MessageUtils;
import com.example.graduate.museumt.utils.MuseumSP;
import com.example.graduate.museumt.web.WebAPIManager;
import com.example.graduate.museumt.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.museumt.web.Constant.CODE_SUCCESS;

public class UpdateInfoActivity extends BaseActivity {


	MuseumSP museumSP;
	LifecycleProvider<Lifecycle.Event> lifecycleProvider;
	UpdateUserInfoDialog updateUserInfoDialog;
	@BindView(R.id.backend)
	ImageView backend;
	@BindView(R.id.ci_user)
	CircleImageView ciUser;
	@BindView(R.id.tv_user_nick)
	TextView tvUserNick;
	@BindView(R.id.tv_nick)
	TextView tvNick;
	@BindView(R.id.rl_nick)
	RelativeLayout rlNick;
	@BindView(R.id.tv_user_sex)
	TextView tvUserSex;
	@BindView(R.id.tv_sex)
	TextView tvSex;
	@BindView(R.id.rl_sex)
	RelativeLayout rlSex;
	@BindView(R.id.tv_user_address)
	TextView tvUserAddress;
	@BindView(R.id.tv_address)
	TextView tvAddress;
	@BindView(R.id.rl_address)
	RelativeLayout rlAddress;
	@BindView(R.id.tv_user_age)
	TextView tvUserAge;
	@BindView(R.id.tv_age)
	TextView tvAge;
	@BindView(R.id.rl_age)
	RelativeLayout rlAge;
	@BindView(R.id.btn_update)
	Button btnUpdate;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_update_user;
	}

	@Override
	public void onInit() {
		museumSP = new MuseumSP(UpdateInfoActivity.this);
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
	}

	@Override
	public void onBindData() {
		if (museumSP.getAvatar() != null) {
			GlideX.getInstance().loadImage(UpdateInfoActivity.this, museumSP.getAvatar(), ciUser);
		}
		tvNick.setText(museumSP.getNickName());
		tvAddress.setText(museumSP.getAddress());
		tvAge.setText(museumSP.getAge());
		tvSex.setText(museumSP.getSex());
	}

	@OnClick({R.id.backend, R.id.rl_nick, R.id.rl_sex, R.id.rl_address, R.id.rl_age, R.id.btn_update})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rl_nick:
				updateUserInfoDialog = new UpdateUserInfoDialog(UpdateInfoActivity.this, new UpdateCompelet(), "修改昵称");
				updateUserInfoDialog.show();
				break;
			case R.id.rl_sex:
				updateUserInfoDialog = new UpdateUserInfoDialog(UpdateInfoActivity.this, new UpdateCompelet(), "修改性别");
				updateUserInfoDialog.show();
				break;
			case R.id.rl_address:
				updateUserInfoDialog = new UpdateUserInfoDialog(UpdateInfoActivity.this, new UpdateCompelet(), "修改地址");
				updateUserInfoDialog.show();
				break;
			case R.id.rl_age:
				updateUserInfoDialog = new UpdateUserInfoDialog(UpdateInfoActivity.this, new UpdateCompelet(), "修改年龄");
				updateUserInfoDialog.show();
				break;
			case R.id.btn_update:
				updateInfo();
				break;
			default:
				onBackPressed();
				break;
		}
	}

	private void updateInfo() {
		loadDialog(UpdateInfoActivity.this, "信息更新中...");
		WebAPIManager.getInstance(UpdateInfoActivity.this).UserInfoUpdate(museumSP.getUserId(), tvNick.getText().toString(), tvSex.getText().toString(), tvAge
				.getText().toString(), tvAddress.getText().toString()).subscribeOn(Schedulers.io()).compose(lifecycleProvider.bindToLifecycle()).observeOn
				(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse webResponse) {
				if (webResponse.getCode().equals(CODE_SUCCESS)) {
					museumSP.putNickName(tvNick.getText().toString());
					museumSP.putSex(tvSex.getText().toString());
					museumSP.putAddress(tvAddress.getText().toString());
					museumSP.putAge(tvAge.getText().toString());
					tvNick.setText(museumSP.getNickName());
					tvAddress.setText(museumSP.getAddress());
					tvAge.setText(museumSP.getAge());
					tvSex.setText(museumSP.getSex());
					MessageUtils.showLongToast(UpdateInfoActivity.this, "修改成功");
				} else {
					MessageUtils.showLongToast(UpdateInfoActivity.this, "修改失败");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showLongToast(UpdateInfoActivity.this, e.toString());
			}

			@Override
			public void onComplete() {
				loadDialogDismiss();
			}
		});
	}


	public class UpdateCompelet implements UpdateUserInfoDialog.UpdateCompelet {
		@Override
		public void onCompelet(String type, String info) {
			switch (type) {
				case "修改昵称": {
					tvNick.setText(info);
					break;
				}
				case "修改性别": {
					tvSex.setText(info);
					break;
				}
				case "修改地址": {
					tvAddress.setText(info);
					break;
				}
				case "修改年龄": {
					tvAge.setText(info);
					break;
				}
			}
		}
	}
}

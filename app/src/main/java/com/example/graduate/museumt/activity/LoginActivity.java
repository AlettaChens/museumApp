package com.example.graduate.museumt.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;
import com.example.graduate.museumt.bean.User;
import com.example.graduate.museumt.utils.MessageUtils;
import com.example.graduate.museumt.utils.MuseumSP;
import com.example.graduate.museumt.web.WebAPIManager;
import com.example.graduate.museumt.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.museumt.web.Constant.CODE_SUCCESS;

public class LoginActivity extends BaseActivity {
	@BindView(R.id.nick_name_ed_login)
	EditText nickNameEdLogin;
	@BindView(R.id.pwd_ed_login)
	EditText pwdEdLogin;
	@BindView(R.id.login_btn)
	Button loginBtn;
	@BindView(R.id.go_to_register)
	TextView goToRegister;
	@BindView(R.id.activity_login)
	LinearLayout activityLogin;

	MuseumSP museumSP;

	LifecycleProvider<Lifecycle.Event> lifecycleProvider;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_login;
	}

	@Override
	public void onInit() {
		museumSP = new MuseumSP(LoginActivity.this);
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
	}

	@Override
	public void onBindData() {

	}

	@OnClick({R.id.login_btn, R.id.go_to_register})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.login_btn:
				login();
				break;
			case R.id.go_to_register:
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				break;
		}
	}

	private void login() {
		loadDialog(LoginActivity.this, "登陆中...");
		WebAPIManager.getInstance(LoginActivity.this).login(nickNameEdLogin.getText().toString(), pwdEdLogin.getText().toString(), "游客").subscribeOn
				(Schedulers.io()).compose(lifecycleProvider.bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse<User>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse<User> userWebResponse) {
				if (userWebResponse.getCode().equals(CODE_SUCCESS)) {
					UpdateUserInfo(userWebResponse.getData());
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				} else {
					MessageUtils.showLongToast(LoginActivity.this, "登录失败");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showLongToast(LoginActivity.this, e.toString());
			}

			@Override
			public void onComplete() {
				loadDialogDismiss();
			}
		});
	}

	private void UpdateUserInfo(User user) {
		museumSP.putUserId(user.getId());
		museumSP.putNickName(user.getUsername());
		museumSP.putAvatar(user.getAvatar());
		museumSP.putAddress(user.getAddress());
		museumSP.putAge(user.getAge());
		museumSP.putSex(user.getSex());
		museumSP.putIsLogin(true);
	}


}

package com.example.graduate.museumt.activity;

import android.arch.lifecycle.Lifecycle;
import android.widget.Button;
import android.widget.EditText;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.base.BaseActivity;
import com.example.graduate.museumt.utils.MessageUtils;
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

public class RegisterActivity extends BaseActivity {
	@BindView(R.id.nick_name_ed_reg)
	EditText nickNameEdReg;
	@BindView(R.id.pwd_ed_reg)
	EditText pwdEdReg;
	@BindView(R.id.register_btn)
	Button registerBtn;
	LifecycleProvider<Lifecycle.Event> lifecycleProvider;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_register;
	}

	@Override
	public void onInit() {
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
	}

	@Override
	public void onBindData() {

	}

	@OnClick(R.id.register_btn)
	public void onClick() {
		register();
	}

	private void register() {
		loadDialog(RegisterActivity.this, "注册中...");
		WebAPIManager.getInstance(RegisterActivity.this).register(nickNameEdReg.getText().toString(), pwdEdReg.getText().toString(), "游客").subscribeOn
				(Schedulers.io())
				.compose(lifecycleProvider.bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse webResponse) {
				if (webResponse.getCode().equals(CODE_SUCCESS)) {
					MessageUtils.showLongToast(RegisterActivity.this, "注册成功");
					finish();
				} else {
					MessageUtils.showLongToast(RegisterActivity.this, "注册失败");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showLongToast(RegisterActivity.this, e.toString());
			}

			@Override
			public void onComplete() {
				loadDialogDismiss();
			}
		});
	}
}

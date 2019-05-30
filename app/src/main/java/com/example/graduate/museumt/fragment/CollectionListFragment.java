package com.example.graduate.museumt.fragment;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.activity.CollectionDetailActivity;
import com.example.graduate.museumt.adapter.CollectionListAdapter;
import com.example.graduate.museumt.base.BaseFragment;
import com.example.graduate.museumt.bean.Collection;
import com.example.graduate.museumt.utils.MessageUtils;
import com.example.graduate.museumt.web.WebAPIManager;
import com.example.graduate.museumt.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.museumt.web.Constant.CODE_SUCCESS;

public class CollectionListFragment extends BaseFragment {

	@BindView(R.id.rv_collection_list)
	RecyclerView rvCollectionList;
	private LifecycleProvider<Lifecycle.Event> lifecycleProvider;
	private CollectionListAdapter collectionListAdapter;

	String title;
	private TextToSpeech mSpeech;

	@Override
	protected int getContentViewId() {
		return R.layout.fragment_collection_list;
	}

	@Override
	public void onInit() {
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
		title = getArguments().getString("title");
		mSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mSpeech.setLanguage(Locale.CHINA);
					if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
						MessageUtils.showLongToast(getActivity(), "语音初始化失败");
					}
				}
			}
		});

	}

	@Override
	public void onBindData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		getCollectionList();
	}

	private void getCollectionList() {
		WebAPIManager.getInstance(getActivity()).searchAllByType(title).subscribeOn(Schedulers.io()).compose(lifecycleProvider.bindToLifecycle()).observeOn
				(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse<List<Collection>>>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse<List<Collection>> listWebResponse) {
				if (listWebResponse.getCode().equals(CODE_SUCCESS)) {
					collectionListAdapter = new CollectionListAdapter(getActivity(), listWebResponse.getData());
					collectionListAdapter.setVioceCallBack(new VoiceCallBackT());
					rvCollectionList.setLayoutManager(new LinearLayoutManager(getActivity()));
					rvCollectionList.setAdapter(collectionListAdapter);
				} else {
					MessageUtils.showLongToast(getActivity(), "获取失败");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showLongToast(getActivity(), e.toString());
			}

			@Override
			public void onComplete() {
			}
		});
	}

	public class VoiceCallBackT implements CollectionListAdapter.VioceCallBack {
		@Override
		public void speekText(String text) {
			mSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}

		@Override
		public void ListDetail(Collection item) {
			Intent intent = new Intent(getContext(), CollectionDetailActivity.class);
			intent.putExtra("collection", item);
			startActivity(intent);
		}
	}
}

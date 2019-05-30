package com.example.graduate.museumt.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.museumt.R;
import com.example.graduate.museumt.bean.Collection;
import com.example.graduate.museumt.utils.GlideImageLoader;
import com.example.graduate.museumt.utils.GlideX;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final int RECOMMEND_VIEW_TYPE = 1;

	private List<Collection> recommendList;
	private Context context;
	private VioceCallBack vioceCallBack;


	public HomeAdapter(List<Collection> recommendList, Context context) {
		this.recommendList = recommendList;
		this.context = context;
	}

	@Override
	public int getItemViewType(int position) {
		return RECOMMEND_VIEW_TYPE;
	}


	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		RecipeHolder recommendHolder = (RecipeHolder) viewHolder;
		recommendHolder.update(recommendList.get(i), i);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_list, parent, false);
		return new RecipeHolder(view);
	}


	@Override
	public int getItemCount() {
		return recommendList.size();
	}

	public class RecipeHolder extends RecyclerView.ViewHolder {
		private TextView collect_name;
		private ImageView collection_pic;
		private TextView tv_des;
		private TextView type;
		private ImageView iv_voice;
		private TextView tv_date;
		private int position;

		public RecipeHolder(@NonNull View itemView) {
			super(itemView);
			collect_name = itemView.findViewById(R.id.collect_name);
			collection_pic = itemView.findViewById(R.id.collection_pic);
			tv_des = itemView.findViewById(R.id.tv_des);
			type = itemView.findViewById(R.id.type);
			iv_voice = itemView.findViewById(R.id.iv_voice);
			tv_date = itemView.findViewById(R.id.tv_date);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					vioceCallBack.ListDetail(recommendList.get(position));
				}
			});
		}

		public void update(Collection item, int i) {
			position = i;
			GlideX.getInstance().loadImage(context, item.getAvatar(), collection_pic);
			collect_name.setText(item.getName());
			tv_des.setText(item.getDescription());
			type.setText(item.getType());
			tv_date.setText(item.getDate());
			iv_voice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					vioceCallBack.speekText(item.getDescription());
				}
			});
		}
	}

	public void setVioceCallBack(@NonNull VioceCallBack vioceCallBack) {
		this.vioceCallBack = vioceCallBack;
	}

	public interface VioceCallBack {
		void speekText(String text);

		void ListDetail(Collection item);
	}

}

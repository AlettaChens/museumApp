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
import com.example.graduate.museumt.utils.GlideX;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.RecipeHolder> {
	private Context context;
	private List<Collection> collectionList;
	private VioceCallBack vioceCallBack;

	public CollectionListAdapter(Context context, List<Collection> collectionList) {
		this.context = context;
		this.collectionList = collectionList;
	}

	@NonNull
	@Override
	public RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_collection_list, viewGroup, false);
		return new RecipeHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecipeHolder recipeHolder, int i) {
		RecipeHolder recommendHolder = recipeHolder;
		recommendHolder.update(collectionList.get(i), i);
	}

	@Override
	public int getItemCount() {
		return collectionList.size();
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
					vioceCallBack.ListDetail(collectionList.get(position));
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

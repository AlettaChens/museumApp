package com.example.graduate.museumt.web;


import com.example.graduate.museumt.bean.Collection;
import com.example.graduate.museumt.bean.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RxHttpRequestHandler {
	@POST("user/register")
	Observable<WebResponse> register(@Query("username") String username, @Query("password") String password, @Query("type") String type);

	@POST("user/login")
	Observable<WebResponse<User>> login(@Query("username") String username, @Query("password") String password, @Query("type") String type);

	@Multipart
	@POST("user/avatarUpdate")
	Observable<WebResponse<String>> uploadUserAvatar(@Query("id") long id, @Part MultipartBody.Part file);


	@POST("user/updateUserInfo")
	Observable<WebResponse> UserInfoUpdate(@Query("id") long id, @Query("username") String username, @Query("sex") String sex, @Query("age") String age, @Query
			("address") String address);


	@POST("collection/searchAllByRecommend")
	Observable<WebResponse<List<Collection>>> searchAllByRecommend(@Query("recommend") String recommend);


	@POST("collection/searchAllByType")
	Observable<WebResponse<List<Collection>>> searchAllByType( @Query("type") String type);


	@POST("collection/getCollectionById")
	Observable<WebResponse<Collection>> getCollectionById( @Query("id") long id);

}

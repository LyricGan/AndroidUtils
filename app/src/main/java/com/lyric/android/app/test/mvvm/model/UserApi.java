package com.lyric.android.app.test.mvvm.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 16:34
 */
public interface UserApi {

    @GET("users/{username}/repos")
//    Call<List<Repository>> getRepositoryList(@Path("username") String username);
    Observable<List<Repository>> getRepositoryList(@Path("username") String username);

    @GET
//    Call<User> getUserDetails(@Url String userUrl);
    Observable<User> getUserDetails(@Url String userUrl);
}

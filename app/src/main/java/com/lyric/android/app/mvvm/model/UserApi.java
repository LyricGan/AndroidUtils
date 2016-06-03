package com.lyric.android.app.mvvm.model;

import com.lyric.android.app.constants.ApiPath;
import com.lyric.android.library.network.Api;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Url;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 16:34
 */
public interface UserApi {

    @GET("users/{username}/repos")
    Call<List<Repository>> getRepositoryList(@Path("username") String username);

    @GET
    Call<User> getUserDetails(@Url String userUrl);

    class Factory {
        public static UserApi getInstance() {
            return Api.Factory.getInstance(ApiPath.BASE_URL, UserApi.class);
        }
    }
}

package com.example.api;

import com.example.model.ChangePassword;
import com.example.model.User;
import com.example.model.UserLogin;
import com.example.model.UserSignup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
;

public interface UserApi {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public UserApi userAPI = new Retrofit.Builder()
            .baseUrl("https://d19cqcnpm01-api.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(UserApi.class);

    @GET("api/auth/signin")
    Call<User> getAllUser();

    @POST("api/auth/signin")
    Call<User> postLogin(@Body UserLogin user);

    @POST("api/auth/signup")
    Call<ResponseBody> postSignup(@Body UserSignup user);

    @POST("api/auth/changePassByEmail")
    Call<ResponseBody> postChangePassByEmail(@Body ChangePassword password);
}

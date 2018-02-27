package com.hackfsu.android.api;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hackfsu.android.api.templates.HacksResponse;
import com.hackfsu.android.api.templates.ProfileRequest;
import com.hackfsu.android.api.templates.ProfileResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;
import com.hackfsu.android.app.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.hackfsu.android.api.util.AddCookiesInterceptor.PREF_COOKIES;

/**
 * Created by Randy Bruno-Piverger on 2/22/2018.
 */

public interface RetroAPI {


    final static String Test_Base = "https://testapi.hackfsu.com/";
    final static String Test_Login = "api/user/login";
    final static String Test_Profile = "api/user/get/profile";
    final static String Test_getHacks = "api/judge/hacks";
    final static String Test_sendHacks = "api/judge/hacks/upload";



    @POST(Test_Login)
    Call<ResponseBody> postLogin(@Body RequestBody requestBody);

    @POST(Test_sendHacks)
    Call<ResponseBody> postHacks(@Body RequestBody requestbody);

    @GET(Test_Profile)
    Call<ProfileResponse> GetProfile(@Header("Cookie") AddCookiesInterceptor addCookiesInterceptor);


    @GET(Test_getHacks)
    Call<HacksResponse> GetHacks(@Header("Cookie") AddCookiesInterceptor addCookiesInterceptor);


}

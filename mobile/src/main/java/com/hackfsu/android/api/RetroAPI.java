package com.hackfsu.android.api;

import com.hackfsu.android.api.templates.EventsResponse;
import com.hackfsu.android.api.templates.HacksResponse;
import com.hackfsu.android.api.templates.ProfileResponse;
import com.hackfsu.android.api.util.AddCookiesInterceptor;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Randy Bruno-Piverger on 2/22/2018.
 */

public interface RetroAPI {


    String ROUTE_LOGIN = "/api/user/login";
    String ROUTE_PROFILE = "/api/user/get/profile";
    String ROUTE_GET_HACKS = "/api/judge/hacks";
    String ROUTE_POST_HACKS = "/api/judge/hacks/upload";
    String ROUTE_GET_EVENTS = "/api/user/get/events";


    @POST(ROUTE_LOGIN)
    Call<ResponseBody> postLogin(@Body RequestBody requestBody);

    @POST(ROUTE_POST_HACKS)
    Call<ResponseBody> postHacks(@Body RequestBody requestbody);

    @GET(ROUTE_PROFILE)
    Call<ProfileResponse> GetProfile(@Header("Cookie") AddCookiesInterceptor addCookiesInterceptor);

    @GET(ROUTE_GET_HACKS)
    Call<HacksResponse> GetHacks(@Header("Cookie") AddCookiesInterceptor addCookiesInterceptor);

    @GET(ROUTE_GET_EVENTS)
    Call<EventsResponse> GetEvents(@Header("Cookie") AddCookiesInterceptor addCookiesInterceptor);


}

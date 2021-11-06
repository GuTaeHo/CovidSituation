package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.response.ResponseNotice;
import com.cosiguk.covidsituation.network.response.ResponseVersion;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RetrofitClient {
    public static final String URL = "http://test.byeonggook.shop/";

    private final RetrofitInterface retrofitInterface;
    public static RetrofitClient retrofitClient = new RetrofitClient();

    // 싱글톤으로 객체 생성
    private RetrofitClient() {
        // HTTP 로깅 객체 생성
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    // API 통신 객체 반환
    public static RetrofitClient getInstance() {
        return retrofitClient;
    }

    public RetrofitInterface getInterface() {
        return retrofitInterface;
    }

    public interface RetrofitInterface {
        @GET("api/version")
        Call<Response<ResponseVersion>> version();

        @GET("api/notice/noticeList")
        Call<Response<ResponseNotice>> notice();
    }
}

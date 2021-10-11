package com.cosiguk.covidsituation.network;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public class RetrofitNewsClient {
    public static final String URL = "https://openapi.naver.com/";

    private final RetrofitNewsInterface retrofitCityInterface;
    public static RetrofitNewsClient retrofitCityClient = new RetrofitNewsClient();

    // 싱글톤으로 객체 생성
    private RetrofitNewsClient() {
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

        retrofitCityInterface = retrofit.create(RetrofitNewsClient.RetrofitNewsInterface.class);
    }

    // API 통신 객체 반환
    public static RetrofitNewsClient getInstance() {
        return retrofitCityClient;
    }

    public RetrofitNewsClient.RetrofitNewsInterface getInterface() {
        return retrofitCityInterface;
    }

    public interface RetrofitNewsInterface {
        @GET("/v1/search/news.json")
        Call<ResponseNews> news(@HeaderMap HashMap<String, String> headers, @QueryMap HashMap<String, Object> queryString);
    }
}

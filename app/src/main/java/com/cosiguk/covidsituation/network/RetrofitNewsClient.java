package com.cosiguk.covidsituation.network;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public class RetrofitNewsClient {
    public static final String URL = "https://openapi.naver.com/";

    // public static final String CLIENT_ID = "CjNak_XK2e36qG8q4MCz";
    // public static final String CLIENT_SECRET = "Rl9xI44lj3";

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
                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
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
        @Headers({
                "X-Naver-Client-Id: CjNak_XK2e36qG8q4MCz",
                "X-Naver-Client-Secret: Rl9xI44lj3"})
        @GET("/v1/search/news.json")
        Call<ResponseTotal> total(@QueryMap HashMap<String, String> queryString);
    }
}

package com.cosiguk.covidsituation.network;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

// 일별 현황
public class RetrofitPublicClient {
    public static final String URL = "http://openapi.data.go.kr/";

    private final RetrofitCityInterface retrofitCityInterface;
    public static RetrofitPublicClient retrofitCityClient = new RetrofitPublicClient();

    // 싱글톤으로 객체 생성
    private RetrofitPublicClient() {
        // HTTP 로깅 객체 생성
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 헤더에 값 적재
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL)
                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build();

        retrofitCityInterface = retrofit.create(RetrofitCityInterface.class);
    }

    // API 통신 객체 반환
    public static RetrofitPublicClient getInstance() {
        return retrofitCityClient;
    }

    public RetrofitCityInterface getInterface() {
        return retrofitCityInterface;
    }

    public interface RetrofitCityInterface {
        @GET("openapi/service/rest/Covid19/getCovid19SidoInfStateJson")
        Call<ResponseCity> boardList(@QueryMap HashMap<String, String> queries);

        @GET("openapi/service/rest/Covid19/getCovid19InfStateJson")
        Call<ResponseInfection> total(@QueryMap HashMap<String, String> queries);
    }
}

package com.cosiguk.covidsituation.network;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class RetrofitVaccineClient {
    public static final String URL = "http://api.odcloud.kr/api/";

    private final RetrofitVaccineInterface retrofitCityInterface;
    public static RetrofitVaccineClient retrofitCityClient = new RetrofitVaccineClient();

    // 싱글톤으로 객체 생성
    private RetrofitVaccineClient() {
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

        retrofitCityInterface = retrofit.create(RetrofitVaccineInterface.class);
    }

    public static RetrofitVaccineClient getInstance() {
        return retrofitCityClient;
    }

    public RetrofitVaccineInterface getInterface() {
        return retrofitCityInterface;
    }

    public interface RetrofitVaccineInterface {
        @GET("/openapi/service/rest/Covid19/getCovid19SidoInfStateJson")
        Call<ResponseCity> vaccinateList(@QueryMap HashMap<String, String> queries);
    }
}

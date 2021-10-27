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

    private final RetrofitVaccineInterface retrofitVaccineInterface;
    public static RetrofitVaccineClient retrofitVaccineClient = new RetrofitVaccineClient();

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

        retrofitVaccineInterface = retrofit.create(RetrofitVaccineInterface.class);
    }

    public static RetrofitVaccineClient getInstance() {
        return retrofitVaccineClient;
    }

    public RetrofitVaccineInterface getInterface() {
        return retrofitVaccineInterface;
    }

    public interface RetrofitVaccineInterface {
        // 백신 접종 현황 요청
        @GET("15077756/v1/vaccine-stat")
        Call<ResponseVaccineTotal> totalVaccine(@QueryMap HashMap<String, String> queries);

        // 선별 진료소 요청
        @GET("15077586/v1/centers")
        Call<ResponseHospital> hospital(@QueryMap HashMap<String, String> queries);
    }
}

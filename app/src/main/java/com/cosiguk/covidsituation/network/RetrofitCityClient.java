package com.cosiguk.covidsituation.network;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class RetrofitCityClient {
    public static final String URL = "http://openapi.data.go.kr/";

    public static final String SERVICE_KEY = "DeivebZo5FvQEqWf6qINuWV6UVXIrIJko8Da%2F46wqa5jd0IhOZnTOsqqBc3X6yEhqbaKCcoq4kQlmh%2BosbIMhg%3D%3D";

    private final RetrofitCityInterface retrofitCityInterface;
    public static RetrofitCityClient retrofitCityClient = new RetrofitCityClient();

    // 싱글톤으로 객체 생성
    private RetrofitCityClient() {
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
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .build();

        retrofitCityInterface = retrofit.create(RetrofitCityInterface.class);
    }

    // API 통신 객체 반환
    public static RetrofitCityClient getInstance() {
        return retrofitCityClient;
    }

    public RetrofitCityInterface getInterface() {
        return retrofitCityInterface;
    }

    public interface RetrofitCityInterface {
        @GET("/openapi/service/rest/Covid19/getCovid19SidoInfStateJson")
        Call<ResponseCity> boardList(@Query("ServiceKey") String ServiceKey);

        // @POST("project/walkietalkie/api/updatetoken/")
        // Call<Response<ResponseUpdateToken>> updateToken(@Header("Authorization") String header, @Body RequestUpdateToken token);
    }
}

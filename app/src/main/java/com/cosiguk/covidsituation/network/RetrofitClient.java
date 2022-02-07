package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.response.ResponseBoard;
import com.cosiguk.covidsituation.network.response.ResponseBoardDetail;
import com.cosiguk.covidsituation.network.response.ResponseChat;
import com.cosiguk.covidsituation.network.response.ResponseCityWeeks;
import com.cosiguk.covidsituation.network.response.ResponseNotice;
import com.cosiguk.covidsituation.network.response.ResponseVersion;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
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

        @Multipart
        @POST("api/board/boardList")
        Call<Response<ResponseBoard>> boardList(@PartMap Map<String, RequestBody> params);

        @GET("api/board/{boardID}")
        Call<Response<ResponseBoardDetail>> boardDetail(@Path("boardID") int boardID);

        @Multipart
        @POST("api/board/boardAdd")
        Call<Response<Object>> boardAdd(@PartMap Map<String, RequestBody> params);

        @GET("api/board/{boardID}/recommend")
        Call<Response<Object>> boardRecommend(@Path("boardID") int boardID);

        @GET("api/board/{boardID}/deprecate")
        Call<Response<Object>> boardDeprecate(@Path("boardID") int boardID);

        @POST("api/board/{boardID}/delete")
        Call<Response<Object>> boardDelete(@Path("boardID") int boardID);

        @GET("api/chat/{boardID}/chatList")
        Call<Response<ResponseChat>> chatList(@Path("boardID") int boardID);

        @Multipart
        @POST("api/chat/{boardID}/chatAdd")
        Call<Response<Object>> chatAdd(@Path("boardID") int boardID, @PartMap Map<String, RequestBody> params);

        @GET("api/chat/{chatID}/recommend")
        Call<Response<Object>> chatRecommend(@Path("chatID") int chatID);

        @GET("api/chat/{chatID}/deprecate")
        Call<Response<Object>> chatDeprecate(@Path("chatID") int chatID);

        @GET("api/data/infectionCity7Day")
        Call<Response<ResponseCityWeeks>> infectionCityWeeks();
    }
}

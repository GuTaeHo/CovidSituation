package com.cosiguk.covidsituation.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

// 통신 성공 & 실패의 리스너를 정의한 클래스
public class NetworkPresenter implements NetworkPresenterInterface {

    private Response<?> getError(ResponseBody errorBody) {
        Gson gson = new Gson();
        return gson.fromJson(errorBody.charStream(), Response.class);
    }
}

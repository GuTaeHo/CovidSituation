package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.HospitalListener;
import com.cosiguk.covidsituation.network.resultInterface.NewsListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.network.resultInterface.VaccineListener;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NetworkPresenter implements NetworkPresenterInterface {
    @Override
    // 전체 현황 요청 (금일, 작일)
    public void total(HashMap<String, String> requestTotal, TotalListener listener) {
        RetrofitPublicClient
                .getInstance()
                .getInterface()
                .total(requestTotal)
                .enqueue(new Callback<ResponseTotal>() {
                    @Override
                    public void onResponse(Call<ResponseTotal> call, retrofit2.Response<ResponseTotal> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getBody().getItems().getItem());
                            } else {
                                listener.fail(response.errorBody().string());
                            }
                        } catch (IndexOutOfBoundsException exception) {
                            listener.reRequest(response.body().getBody().getItems().getItem());
                        } catch (Exception e) {
                            listener.fail("Total Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTotal> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    // 시,도 별 현황 요청
    @Override
    public void boardList(HashMap<String, String> requestBoardList, BoardListListener listener) {
        RetrofitPublicClient
                .getInstance()
                .getInterface()
                .boardList(requestBoardList)
                .enqueue(new Callback<ResponseCity>() {
                    @Override
                    public void onResponse(Call<ResponseCity> call, retrofit2.Response<ResponseCity> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getBody().getItems().getItem());
                            } else {
                                listener.fail(response.errorBody().string());
                            }
                        } catch (Exception e) {
                            listener.fail("BoardList Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCity> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    // 뉴스 리스트
    @Override
    public void news(HashMap<String, String> headers, HashMap<String, Object> requestNews, NewsListener listener) {
        RetrofitNewsClient
                .getInstance()
                .getInterface()
                .news(headers, requestNews)
                .enqueue(new Callback<ResponseNews>() {
                    @Override
                    public void onResponse(Call<ResponseNews> call, retrofit2.Response<ResponseNews> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getItems());
                            } else {
                                listener.fail(response.errorBody().toString());
                            }
                        } catch (Exception e) {
                            listener.fail("News Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseNews> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void vaccineTotal(HashMap<String, String> requestQuery, VaccineListener listener) {
        RetrofitVaccineClient
                .getInstance()
                .getInterface()
                .totalVaccine(requestQuery)
                .enqueue(new Callback<ResponseVaccineTotal>() {
                    @Override
                    public void onResponse(Call<ResponseVaccineTotal> call, retrofit2.Response<ResponseVaccineTotal> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getData());
                            } else {
                                listener.fail(response.errorBody().toString());
                            }
                        } catch (IndexOutOfBoundsException exception) {
                            listener.request(response.body().getData());
                        } catch (Exception e) {
                            listener.fail("vaccineTotal Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseVaccineTotal> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void hospital(HashMap<String, String> requestQuery, HospitalListener listener) {
        RetrofitVaccineClient
                .getInstance()
                .getInterface()
                .hospital(requestQuery)
                .enqueue(new Callback<ResponseHospital>() {
                    @Override
                    public void onResponse(Call<ResponseHospital> call, retrofit2.Response<ResponseHospital> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getData());
                            } else {
                                listener.fail(response.errorBody().toString());
                            }
                        } catch (Exception e) {
                            listener.fail("hospital Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHospital> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }


    private Response<?> getError(ResponseBody errorBody) {
        Gson gson = new Gson();
        return gson.fromJson(errorBody.charStream(), Response.class);
    }
}

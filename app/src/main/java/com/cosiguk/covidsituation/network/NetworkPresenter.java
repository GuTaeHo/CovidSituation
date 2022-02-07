package com.cosiguk.covidsituation.network;

import androidx.annotation.NonNull;

import com.cosiguk.covidsituation.network.response.ResponseBoard;
import com.cosiguk.covidsituation.network.response.ResponseBoardDetail;
import com.cosiguk.covidsituation.network.response.ResponseChat;
import com.cosiguk.covidsituation.network.response.ResponseCity;
import com.cosiguk.covidsituation.network.response.ResponseCityWeeks;
import com.cosiguk.covidsituation.network.response.ResponseHospital;
import com.cosiguk.covidsituation.network.response.ResponseInfection;
import com.cosiguk.covidsituation.network.response.ResponseNews;
import com.cosiguk.covidsituation.network.response.ResponseNotice;
import com.cosiguk.covidsituation.network.response.ResponseVaccineTotal;
import com.cosiguk.covidsituation.network.response.ResponseVersion;
import com.cosiguk.covidsituation.network.resultInterface.BoardAddListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardReportListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatAddListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.DeleteBoardListener;
import com.cosiguk.covidsituation.network.resultInterface.HospitalListener;
import com.cosiguk.covidsituation.network.resultInterface.InfectionCityWeeks;
import com.cosiguk.covidsituation.network.resultInterface.NewsListener;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.network.resultInterface.SituationBoardListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.network.resultInterface.VaccineListener;
import com.cosiguk.covidsituation.network.resultInterface.VersionListener;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NetworkPresenter implements NetworkPresenterInterface {
    private static final int SUCCESS = 0;
    private static final int FAIL = -1;

    @Override
    public void version(VersionListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .version()
                .enqueue(new Callback<Response<ResponseVersion>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<ResponseVersion>> call, @NonNull retrofit2.Response<Response<ResponseVersion>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getResultData().getVersion());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).getMessage());
                            }
                        } catch (Exception e) {
                            listener.fail("Total Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<ResponseVersion>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void notice(NoticeListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .notice()
                .enqueue(new Callback<Response<ResponseNotice>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<ResponseNotice>> call, @NonNull retrofit2.Response<Response<ResponseNotice>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getResultData().getData());
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getMessage());
                            }
                        } catch (Exception e) {
                            listener.fail("Notice Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<ResponseNotice>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    };

    @Override
    // 전체 현황 요청 (금일, 작일)
    public void total(HashMap<String, String> requestTotal, TotalListener listener) {
        RetrofitPublicClient
                .getInstance()
                .getInterface()
                .total(requestTotal)
                .enqueue(new Callback<ResponseInfection>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseInfection> call, @NonNull retrofit2.Response<ResponseInfection> response) {
                        try {
                            if (response.body() != null && response.isSuccessful() && response.body().getBody().getTotalCount() <= 1) {
                                listener.request();
                            } else if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getBody().getItems().getItem());
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getMessage());
                            }
                        } catch (Exception e) {
                            listener.fail("Total Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseInfection> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    // 시,도 별 현황 요청
    @Override
    public void situationBoardList(HashMap<String, String> requestBoardList, SituationBoardListener listener) {
        RetrofitPublicClient
                .getInstance()
                .getInterface()
                .boardList(requestBoardList)
                .enqueue(new Callback<ResponseCity>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseCity> call, @NonNull retrofit2.Response<ResponseCity> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getBody().getItems().getItem());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).getMessage());
                            }
                        } catch (Exception e) {
                            listener.fail("BoardList Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseCity> call, @NonNull Throwable t) {
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
                    public void onResponse(@NonNull Call<ResponseNews> call, @NonNull retrofit2.Response<ResponseNews> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                // 통신 성공 시 http 바디 반환
                                listener.success(response.body().getItems());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (Exception e) {
                            listener.fail("News Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseNews> call, @NonNull Throwable t) {
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
                    public void onResponse(@NonNull Call<ResponseVaccineTotal> call, @NonNull retrofit2.Response<ResponseVaccineTotal> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getData());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (IndexOutOfBoundsException exception) {
                            listener.request(response.body().getData());
                        } catch (Exception e) {
                            listener.fail("vaccineTotal Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseVaccineTotal> call, @NonNull Throwable t) {
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
                    public void onResponse(@NonNull Call<ResponseHospital> call, @NonNull retrofit2.Response<ResponseHospital> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getData());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (Exception e) {
                            listener.fail("hospital Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseHospital> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void boardList(HashMap<String, RequestBody> request, BoardListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardList(request)
                .enqueue(new Callback<Response<ResponseBoard>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<ResponseBoard>> call, @NonNull retrofit2.Response<Response<ResponseBoard>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getResultData());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (Exception e) {
                            listener.fail("boardList Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<ResponseBoard>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void boardAdd(HashMap<String, RequestBody> requestBoardAdd, BoardAddListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardAdd(requestBoardAdd)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success();
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (Exception e) {
                            listener.fail("boardAdd Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void detailBoard(int boardID, BoardDetailListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardDetail(boardID)
                .enqueue(new Callback<Response<ResponseBoardDetail>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<ResponseBoardDetail>> call, @NonNull retrofit2.Response<Response<ResponseBoardDetail>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getResultData().getData());
                            } else if (response.errorBody() != null){
                                listener.fail(getError(response.errorBody()).toString());
                            }
                        } catch (Exception e) {
                            listener.fail("boardDetail Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<ResponseBoardDetail>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void recommendBoard(int boardID, BoardRecommendListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardRecommend(boardID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getCode());
                            } else if (response.errorBody() != null)  {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("boardRecommend Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void deprecateBoard(int boardID, BoardDeprecateListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardDeprecate(boardID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getCode());
                            } else if (response.errorBody() != null)  {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("boardDeprecate Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void reportBoard(int boardID, BoardReportListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardReport(boardID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body());
                            } else if (response.errorBody() != null)  {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("reportBoard Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }


    @Override
    public void deleteBoard(int boardID, DeleteBoardListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .boardDelete(boardID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getCode());
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("deleteBoard Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void chatList(int boardID, ChatListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .chatList(boardID)
                .enqueue(new Callback<Response<ResponseChat>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<ResponseChat>> call, @NonNull retrofit2.Response<Response<ResponseChat>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getResultData().getData());
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("chatList Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<ResponseChat>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void chatAdd(int boardID, HashMap<String, RequestBody> requestChatAdd, ChatAddListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .chatAdd(boardID, requestChatAdd)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success();
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("chatAdd Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void chatRecommend(int chatID, ChatRecommendListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .chatRecommend(chatID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success();
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("chatRecommend Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void chatDeprecate(int chatID, ChatDeprecateListener listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .chatDeprecate(chatID)
                .enqueue(new Callback<Response<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<Response<Object>> call, @NonNull retrofit2.Response<Response<Object>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success();
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("chatDeprecate Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Response<Object>> call, @NonNull Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }

    @Override
    public void infectionCityWeeks(InfectionCityWeeks listener) {
        RetrofitClient
                .getInstance()
                .getInterface()
                .infectionCityWeeks()
                .enqueue(new Callback<Response<ResponseCityWeeks>>() {
                    @Override
                    public void onResponse(Call<Response<ResponseCityWeeks>> call, retrofit2.Response<Response<ResponseCityWeeks>> response) {
                        try {
                            if (response.body() != null && response.isSuccessful()) {
                                listener.success(response.body().getResultData().getData());
                            } else if (response.errorBody() != null) {
                                listener.fail(getError(response.errorBody()).getError());
                            }
                        } catch (Exception e) {
                            listener.fail("infectionCityWeeksDeprecate Exception " + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ResponseCityWeeks>> call, Throwable t) {
                        listener.fail(t.toString());
                    }
                });
    }


    private Response<?> getError(ResponseBody errorBody) {
        Gson gson = new Gson();
        return gson.fromJson(errorBody.charStream(), Response.class);
    }
}

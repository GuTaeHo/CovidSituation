package com.cosiguk.covidsituation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.activity.BoardActivity;
import com.cosiguk.covidsituation.activity.BoardAddActivity;
import com.cosiguk.covidsituation.adapter.BaseRecyclerViewAdapter;
import com.cosiguk.covidsituation.adapter.BoardListAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.network.response.ResponseBoard;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.NetworkUtil;
import com.cosiguk.covidsituation.util.ToastUtil;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BoardFragment extends Fragment {
    // 최초 요청 페이지 번호
    private static final int PAGE_INDEX = 0;
    // 요청 시 게시글 사이즈
    private static final int PAGE_SIZE = 10;
    private FragmentBoardBinding binding;
    private BoardListAdapter adapter;
    private Context context;
    private ActivityResultLauncher<Intent> boardDetailLauncher;
    private ActivityResultLauncher<Intent> boardAddLauncher;
    private OnStatusColorUpdateListener onStatusColorUpdateListener;
    private HashMap<String, RequestBody> requestMap;
    private boolean displayProgressStatus;
    private boolean isEndPage;
    // 전체 게시글 개수
    private int totalCount;
    // 요청 시 페이지 번호
    private int page;
    // 요청 시 페이지 개수
    private int size;

    public BoardFragment() {}

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);

        initValue();
        initLayout();
        initActivityLauncher();
        initEvent();
        initRefreshListener();
        setRequestParams();
        return binding.getRoot();
    }

    // 액티비티 실행 후 최초 1번만 실행
    private void initValue() {
        context = getActivity();
        adapter = new BoardListAdapter(context);
        requestMap = new HashMap<>();
        displayProgressStatus = true;
        initRequestParams();
    }

    private void initRequestParams() {
        isEndPage = false;
        page = PAGE_INDEX;
        size = PAGE_SIZE;
    }

    private void setRequestParams() {
        RequestBody requestPage = RequestBody.create(String.valueOf(page), MediaType.parse("multipart/form-data"));
        RequestBody requestSize = RequestBody.create(String.valueOf(size), MediaType.parse("multipart/form-data"));

        requestMap.put("page", requestPage);
        requestMap.put("size", requestSize);

        if (!isEndPage) {
            requestBoard();
        }
    }

    private void requestBoard() {
        if (NetworkUtil.isConnected(context)) {
            if (displayProgressStatus) MyApplication.showProgressDialog(getActivity(), getString(R.string.progress_search));
            MyApplication
                    .getNetworkPresenterInstance()
                    .boardList(requestMap, new BoardListener() {
                        @Override
                        public void success(ResponseBoard response) {
                            if (response.getTotalCount() == 0) {
                                showBoardEmpty();
                                isEndPage = true;
                            } else {
                                totalCount = response.getTotalCount();
                                adapter.addItems(response.getData());
                                showContent();
                            }
                            binding.loSwipe.setRefreshing(false);
                            if (displayProgressStatus) {
                                MyApplication.dismissProgressDialog();
                                displayProgressStatus = false;
                            };
                        }

                        @Override
                        public void fail(String message) {
                            binding.loSwipe.setRefreshing(false);
                            new NoticeDialog(getActivity())
                                    .setMsg(message)
                                    .show();
                            if (displayProgressStatus) {
                                MyApplication.dismissProgressDialog();
                                displayProgressStatus = false;
                            };
                        }
                    });
        } else
            showNetworkError();
    }

    private void initLayout() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 스크롤이 끝에 도달 시 호출
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("adapterEnd", adapter.getItemCount() + "");
                    // 마지막 게시글 확인
                    if ((page + 1) * size >= totalCount) {
                        isEndPage = true;
                    } else {
                        page += 1;
                        setRequestParams();
                        // 어댑터 마지막 부분에 프로그래스 아이템 추가하기!!
                    }
                }
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(ActivityUtil.DATA, adapter.getItem(position).getId());
                boardDetailLauncher.launch(intent);
            }
        });
        binding.recyclerview.setAdapter(adapter);
    }
    private void initEvent() {
        binding.loBoardAdd.setOnClickListener(v -> {
            if (NetworkUtil.isConnected(context)) {
                boardAddLauncher.launch(new Intent(getActivity(), BoardAddActivity.class));
            } else {
                ToastUtil.showDarkToast(context, getString(R.string.network_fail_content));
            }
        });
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(() -> {
            adapter.clear();
            page = PAGE_INDEX;
            size = PAGE_SIZE;
            isEndPage = false;
            setRequestParams();
        });
    }
    
    // 인터넷 연결 상태에 따른 레이아웃 호출
    private void showNetworkError() {
        binding.loSwipe.setRefreshing(false);
        binding.tvErrorTitle.setText(getString(R.string.network_fail_text));
        binding.tvErrorContent.setText(getString(R.string.network_fail_content));
        binding.loErrorLayout.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);
        binding.loBoardAdd.setVisibility(View.GONE);
    }

    private void showBoardEmpty() {
        binding.loSwipe.setRefreshing(false);
        binding.tvErrorTitle.setText(getString(R.string.board_empty));
        binding.tvErrorContent.setText("");
        binding.loErrorLayout.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);
        binding.loBoardAdd.setVisibility(View.GONE);
    }
    
    private void showContent() {
        binding.loSwipe.setRefreshing(false);
        binding.loErrorLayout.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.VISIBLE);
        binding.loBoardAdd.setVisibility(View.VISIBLE);
    }

    private void initActivityLauncher() {
        boardDetailLauncher = registerForActivityResult(
                new ActivityResultContract<Intent, Object>() {
                    @NonNull
                    @Override
                    // ActivityResultLauncher 의 launch() 호출 시, 가장 먼저 호출됨
                    // 인텐트를 초기화한 뒤, 목표 액티비티로 넘김
                    public Intent createIntent(@NonNull Context context, Intent input) {
                        input.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        return input;
                    }

                    @Override
                    // 목표 액티비티에서 반환된 결과 처리
                    public Object parseResult(int resultCode, @Nullable Intent intent) {
                        if (resultCode == Activity.RESULT_OK) {
                            return ActivityUtil.RESPONSE_OK;
                        } else
                            return ActivityUtil.RESPONSE_CANCEL;
                    }
                },
                // parseResult 처리 후 호출
                new ActivityResultCallback<Object>() {
                    @Override
                    public void onActivityResult(Object result) {
                        if (result.equals(ActivityUtil.RESPONSE_OK)) {
                            adapter.clear();
                            initRequestParams();
                            setRequestParams();
                        }
                    }
                });
        boardAddLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // 상태바 업데이트 이벤트 호출
                        onStatusColorUpdateListener.onUpdate(
                                requireActivity().getColor(R.color.app_background));
                        // 액티비티 응답 처리
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            adapter.clear();
                            initRequestParams();
                            setRequestParams();
                        }
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnStatusColorUpdateListener) {
            onStatusColorUpdateListener = (OnStatusColorUpdateListener) context;
        }
    }

    // 액티비티 상태바 색상변환 인터페이스
    public interface OnStatusColorUpdateListener {
        void onUpdate(int color);
    }
}
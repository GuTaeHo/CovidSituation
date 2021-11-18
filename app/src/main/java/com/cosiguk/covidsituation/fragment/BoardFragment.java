package com.cosiguk.covidsituation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.activity.BoardActivity;
import com.cosiguk.covidsituation.activity.BoardAddActivity;
import com.cosiguk.covidsituation.adapter.BaseRecyclerViewAdapter;
import com.cosiguk.covidsituation.adapter.BoardListAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;
import com.cosiguk.covidsituation.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Objects;

public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;
    private BoardListAdapter adapter;
    private Context context;
    private ActivityResultLauncher<Intent> boardDetailLauncher;
    private ActivityResultLauncher<Intent> boardAddLauncher;
    private OnStatusColorUpdateListener onStatusColorUpdateListener;

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

        initLayout();
        initActivityLauncher();
        initEvent();
        initRefreshListener();
        requestBoard();
        return binding.getRoot();
    }

    private void requestBoard() {
        MyApplication.showProgressDialog(getActivity(), getString(R.string.progress_search));
        MyApplication
                .getNetworkPresenterInstance()
                .boardList(new BoardListener() {
                    @Override
                    public void success(ArrayList<Board> items) {
                        adapter.addItems(items);
                        binding.loSwipe.setRefreshing(false);
                        MyApplication.dismissProgressDialog();
                    }

                    @Override
                    public void fail(String message) {
                        MyApplication.dismissProgressDialog();
                        binding.loSwipe.setRefreshing(false);
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    private void initLayout() {
        context = getActivity();
        adapter = new BoardListAdapter(context);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("onStage()", adapter.getItemCount() + "");
                }
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(ActivityUtil.NOTICE_ID, adapter.getItem(position).getId());
                boardDetailLauncher.launch(intent);
            }
        });
        binding.recyclerview.setAdapter(adapter);
    }

    private void initEvent() {
        binding.tvBoardAdd.setOnClickListener(v -> {
            boardAddLauncher.launch(new Intent(getActivity(), BoardAddActivity.class));
        });
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(() -> {
            adapter.clear();
            requestBoard();
        });
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
                            requestBoard();
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
                            requestBoard();
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
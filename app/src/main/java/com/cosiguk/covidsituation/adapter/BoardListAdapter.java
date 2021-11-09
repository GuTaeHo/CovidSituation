package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.activity.BoardActivity;
import com.cosiguk.covidsituation.databinding.ItemBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.util.ActivityUtil;

import java.util.ArrayList;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Board> items;

    public BoardListAdapter(Context context, ArrayList<Board> items) {
        this.context = context;
        this.items = items;
    }

    public ArrayList<Board> getBoardList() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void addAll(ArrayList<Board> list) {
        items.addAll(list);
    }

    public void setItemListEmpty() {
        this.items.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Board item = items.get(position);

        if (holder.binding != null) {
            holder.binding.tvTitle.setText(item.getTitle());
            holder.binding.tvDate.setText(item.getCreatedDate());
            holder.binding.tvNickname.setText(String.format("작성자 %s", item.getNickname()));
            holder.binding.tvHit.setText(String.format("조회수 %s", item.getHit()));
            holder.binding.tvLikeCount.setText(String.valueOf(item.getRecommend()));
            holder.binding.tvUnlikeCount.setText(String.valueOf(item.getDeprecate()));
            holder.binding.container.setOnClickListener(v -> ActivityUtil.startSingleActivityExtra(context, BoardActivity.class, item.getId()));
            holder.binding.tvLike.setOnClickListener(v -> {
            });
            holder.binding.tvUnlike.setOnClickListener(v -> {

            });
            holder.binding.tvReport.setOnClickListener(v ->
                    new NoticeDialog(context)
                            .setMsg(context.getResources().getString(R.string.board_report_notice))
                            .setPositiveMsg(context.getResources().getString(R.string.dialog_yes))
                            .setNegativeMsg(context.getResources().getString(R.string.dialog_no))
                            .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                @Override
                                public void positive() {
                                    Toast.makeText(context, "plz insert report code ", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void negative() {}
                            }));
            holder.binding.tvDelete.setOnClickListener(v ->
                    new NoticeDialog(context)
                            .setMsg(context.getResources().getString(R.string.board_delete_notice))
                            .setPositiveMsg(context.getResources().getString(R.string.dialog_yes))
                            .setNegativeMsg(context.getResources().getString(R.string.dialog_no))
                            .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                @Override
                                public void positive() {
                                    Toast.makeText(context, "plz insert delete code ", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void negative() {}
                            }));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBoardBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }
}

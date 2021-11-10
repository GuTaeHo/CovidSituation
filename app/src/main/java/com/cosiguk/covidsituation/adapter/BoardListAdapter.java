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

public class BoardListAdapter extends BaseRecyclerViewAdapter<Board, BoardListAdapter.ViewHolder> {

    public BoardListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false));
    }

    @Override
    public void onBindView(@NonNull ViewHolder holder, int position) {
        Board item = items.get(position);

        if (holder.binding != null) {
            holder.binding.tvTitle.setText(item.getTitle());
            holder.binding.tvDate.setText(item.getCreatedDate());
            holder.binding.tvNickname.setText(String.format("%s", item.getNickname()));
            holder.binding.tvHit.setText(String.format("조회수 %s", item.getHit()));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBoardBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemChatBinding;
import com.cosiguk.covidsituation.model.Board;

public class ChatAdapter extends BaseRecyclerViewAdapter<Board, ChatAdapter.ViewHolder>{

    public ChatAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindView(@NonNull ViewHolder holder, int position) {
        Board item = items.get(position);

        if (holder.binding != null) {
            holder.binding.tvDate.setText(item.getCreatedDate());
            holder.binding.tvNickname.setText(String.format("%s", item.getNickname()));
            holder.binding.tvContent.setText(item.getContent());
            holder.binding.tvRecommendCount.setText(String.format("%s", item.getRecommend()));
            holder.binding.tvDeprecateCount.setText(String.format("%s", item.getDeprecate()));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

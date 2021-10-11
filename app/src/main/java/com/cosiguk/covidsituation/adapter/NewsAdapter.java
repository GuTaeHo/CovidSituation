package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemNewsBinding;
import com.cosiguk.covidsituation.model.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final ArrayList<News> items;
    private static OnItemClickListener listener = null;

    public NewsAdapter(ArrayList<News> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        NewsAdapter.listener = listener;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate(삽입될 레이아웃, 부모 레이아웃)
        View view = inflater.inflate(R.layout.item_news, parent, false);

        // 뷰 홀더 생성
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News item = items.get(position);

        if (holder.binding != null) {
            // 제목
            holder.binding.tvTitle.setText(Html.fromHtml(item.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            // 내용
            holder.binding.tvDescription.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            // 일자
            holder.binding.tvDate.setText(item.getPubDate());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.loNews.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(v, pos);
                }
            });
        }
    }
}

package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemNewsBinding;
import com.cosiguk.covidsituation.databinding.ItemProgressBinding;
import com.cosiguk.covidsituation.model.News;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.NewsComparator;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String COMMON_VIEW = "common_view";
    private static final String PROGRESS_VIEW = "progress_view";

    private ArrayList<News> items;
    private static OnItemClickListener listener = null;

    public NewsAdapter() {
        this.items = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        NewsAdapter.listener = listener;
    }

    // 리스트 초기화
    public void initItemList(ArrayList<News> list) {
        this.items = list;
    }

    // 리스트 비우기
    public void setItemListEmpty() {
        this.items.clear();
    }

    // 리스트 반환
    public ArrayList<News> getList() {
        return this.items;
    }

    // 리스트 추가
    public void addAll(ArrayList<News> list) {
        this.items.addAll(list);
    }

    // 리스트 소팅
    public void sort() {
        this.items.sort(new NewsComparator());
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final ItemProgressBinding binding;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

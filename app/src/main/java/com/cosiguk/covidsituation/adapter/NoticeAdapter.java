package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemNoticeBinding;
import com.cosiguk.covidsituation.model.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private ArrayList<Notice> items;
    // Item 클릭 상태 저장 (1 == 열림, 0 == 닫힘)
    private SparseBooleanArray selectedItems;
    // 직전에 클릭된 Item 위치
    private int prePosition = -1;

    public NoticeAdapter() {
        this.items = new ArrayList<>();
        this.selectedItems = new SparseBooleanArray();
    }

    // 리스트 비우기
    public void setItemListEmpty() {
        this.items.clear();
    }

    // 리스트 반환
    public ArrayList<Notice> getList() {
        return this.items;
    }

    // 리스트 추가 (페이징)
    public void addAll(ArrayList<Notice> list) {
        this.items.addAll(list);
    }

    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate(삽입될 레이아웃, 부모 레이아웃)
        View view = inflater.inflate(R.layout.item_notice, parent, false);
        // 뷰 홀더 생성
        return new NoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notice item = items.get(position);

        if (holder.binding != null) {
            // 제목
            holder.binding.tvTitle.setText(item.getTitle());
            // 일자
            holder.binding.tvDate.setText(item.getCreatedDate());
            // 내용
            holder.binding.tvContent.setText(item.getContent());
            // 내용 표시
            changeVisibility(selectedItems.get(position), holder.binding.tvContent);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoticeBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.loNotice.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (selectedItems.get(pos)) {
                    // 펼처진 item 닫음
                    selectedItems.delete(pos);
                } else {
                    // 최근 클릭된 Item 클릭 상태 해제
                    selectedItems.delete(prePosition);
                    // 클릭한 Item position 저장
                    selectedItems.put(pos, true);
                }
                // 아이템 갱신
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(pos);
                // position 저장
                prePosition = pos;
            });
        }
    }
    private void changeVisibility(boolean isExpanded, TextView view) {
        view.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }
}

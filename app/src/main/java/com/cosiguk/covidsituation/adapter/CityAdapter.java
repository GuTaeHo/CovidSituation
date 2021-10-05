package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemCityBinding;
import com.cosiguk.covidsituation.model.ItemCity;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private final List<ItemCity> items;
    private OnItemClickListener listener = null;

    public CityAdapter(List<ItemCity> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate(삽입될 레이아웃, 부모 레이아웃)
        View view = inflater.inflate(R.layout.item_city, parent, false);

        // 뷰 홀더 생성
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        ItemCity item = items.get(position);

        if (holder.binding != null) {
            // 지역
            holder.binding.tvCity.setText(item.getGubun());
            // 격리중 환자
            holder.binding.tvCareContents.setText(item.getIsolIngCnt()+"");
            // 확진 환자
            holder.binding.tvInfectContents.setText(item.getDefCnt()+"");
            // 전일 대비 증가
            holder.binding.tvCompareContents.setText("+"+item.getIncDec());
            // 격리 해제
            holder.binding.tvCompleteContents.setText(item.getIsolClearCnt()+"");
            // 사망자
            holder.binding.tvDeathContents.setText(item.getDeathCnt()+"");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCityBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            /*
            binding.loChat.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(v, pos);
                }
            });
             */
        }
    }
}

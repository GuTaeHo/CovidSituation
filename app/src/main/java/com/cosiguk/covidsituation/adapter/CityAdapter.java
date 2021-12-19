package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemCityBinding;
import com.cosiguk.covidsituation.model.City;

public class CityAdapter extends BaseRecyclerViewAdapter<City, CityAdapter.ViewHolder>{

    public CityAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindView(@NonNull ViewHolder holder, int position) {
        City item = items.get(position);

        if (holder.binding != null) {
            if (position == 1) {
                holder.binding.loCity.setBackgroundColor(ContextCompat.getColor(context, R.color.current_location));
            }
                // 지역
                holder.binding.tvCity.setText(item.getGubun());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCityBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

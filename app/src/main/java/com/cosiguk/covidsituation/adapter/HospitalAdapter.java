package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ItemHospitalBinding;
import com.cosiguk.covidsituation.model.Hospital;
import com.cosiguk.covidsituation.util.ConvertUtil;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    private final ArrayList<Hospital> items;

    public HospitalAdapter(ArrayList<Hospital> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_hospital, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hospital item = items.get(position);

        if (holder.binding != null) {
            holder.binding.tvFacilityName.setText(item.getFacilityName());
            holder.binding.tvCenterName.setText(ConvertUtil.splitString(item.getCenterName(), 1));
            holder.binding.tvAddress.setText(item.getAddress());
            holder.binding.tvPhoneNumber.setText(item.getPhoneNumber());
            holder.binding.tvDistance.setText(String.format("%.2f", item.getDistance()) + "km");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHospitalBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

package com.cosiguk.covidsituation.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// 어댑터의 상위에 있는 추상 클래스
// 어댑터 생성 시, model type 과 홀더를 받아 저장하며, 각 아이템에 대한 클릭 리스너가 정의되어있음

public abstract class BaseRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected ArrayList<T> items;
    protected Context context;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(view, position);
                }

                return true;
            }
        });

        // 리스너가 구현된 추상메서드 onBindView 호출
        onBindView((H) holder, position);
    }

    // 하위 클래스에서 반드시 구현해줘야하는 메서드.
    // 각 어댑터의 뷰홀더와, 아이템의 위치가 포함됨
    abstract public void onBindView(H holder, int position);

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }

        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void initItems(ArrayList<T> list) {
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        if (list != null) {
            items.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void setItem(T item) {
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }

        if (item != null) {
            items.add(item);
        }

        notifyDataSetChanged();
    }

    public void addItems(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        if (item != null) {
            items.add(item);
        }

        notifyDataSetChanged();
    }

    public void addItems(ArrayList<T> items) {
        if (this.items == null) {
            this.items = items;
        } else {
            this.items.addAll(items);
        }

        notifyDataSetChanged();
    }

    public ArrayList<T> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }

        return items;
    }

    public T getItem(int position) {
        if (items != null) {
            return items.get(position);
        }

        return null;
    }

    public void clear() {
        if (items != null) {
            items.clear();
        } else {
            items = new ArrayList<>();
        }

        notifyDataSetChanged();
    }

    public void updateItem(int position, T item) {
        if(items == null){
            return;
        }

        items.set(position, item);
        notifyDataSetChanged();
    }

    public void remove(int position){
        if(items == null){
            return;
        }

        items.remove(position);
        notifyDataSetChanged();
    }
}

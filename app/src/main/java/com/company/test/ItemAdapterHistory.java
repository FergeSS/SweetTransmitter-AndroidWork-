package com.company.test;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapterHistory extends RecyclerView.Adapter<ItemAdapterHistory.ViewHolder> {
    private List<ItemHistory> items;
    private Context context;

    public ItemAdapterHistory(List<ItemHistory> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false); // todo: itemBasket.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemHistory item = items.get(position);
        holder.image.setImageResource(item.getImage());
        holder.title.setText(item.getTitle()+(position+1));
        holder.timeAndDate.setText(item.getTimeAndDate());
        holder.total.setText(item.getTotal());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView total;
        ImageView image;
        TextView timeAndDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            image = itemView.findViewById(R.id.itemImage);
            total = itemView.findViewById(R.id.itemTotal);
            timeAndDate = itemView.findViewById(R.id.itemTime);
        }
    }
}

package com.example.android.sa3ed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notification_adapter extends RecyclerView.Adapter <Notification_adapter.ViewHolder>{
    ArrayList<Notification_Manger> notify_mngr;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification_Manger current_card = notify_mngr.get(position);
        holder.img.setImageResource(current_card.getImg());
        holder.name.setText(current_card.getName());
        holder.status.setText(current_card.getStatus());
        holder.you_for.setText(current_card.getYou_for());
        holder.item.setText(current_card.getItem());
        holder.quantity.setText(current_card.getQuatntity());
    }

    @Override
    public int getItemCount() {
        return notify_mngr.size();
    }

    public Notification_adapter(ArrayList<Notification_Manger> notification_mangers){
        notify_mngr = notification_mangers;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView name;
        public TextView status;
        public TextView you_for;
        public TextView item;
        public TextView quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.status);
            you_for = itemView.findViewById(R.id.you_for);
            quantity = itemView.findViewById(R.id.quantity);
            item = itemView.findViewById(R.id.item);
        }
    }
}

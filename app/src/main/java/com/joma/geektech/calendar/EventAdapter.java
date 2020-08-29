package com.joma.geektech.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joma.geektech.R;
import com.joma.geektech.homework.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private List<String> list = new ArrayList<>();

    public void setList(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void clear(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addItem(String task){
        this.list.add(task);
        notifyDataSetChanged();
    }

    public List<String> getList(){
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView event;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            event = itemView.findViewById(R.id.item_event_text);
        }

        public void bind(String task, int pos){
            event.setText(task);
        }
    }
}

package com.joma.geektech.homework;

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

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<String> list = new ArrayList<>();

    public void setList(List<String> list){
        this.list = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
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

        private TextView taskText;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskText = itemView.findViewById(R.id.item_task_task);
            checkBox = itemView.findViewById(R.id.item_task_checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkBox.getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit().putBoolean(getAdapterPosition()+"", b).apply();
                }
            });
        }

        public void bind(String task, int pos){
            taskText.setText(task);
            if (checkBox.getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getBoolean(pos+"", false)){
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }
}

package com.joma.geektech.homework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joma.geektech.R;
import com.joma.geektech.model.Homework;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder>{

    private List<Homework> list = new ArrayList<>();

    public void setList(List<Homework> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Homework homework){
        this.list.add(homework);
        notifyDataSetChanged();
    }

    public List<Homework> getList(){
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private RecyclerView recyclerView;
        private TaskAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            adapter = new TaskAdapter();
            title = itemView.findViewById(R.id.item_homework_homework);
            recyclerView = itemView.findViewById(R.id.item_homework_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(adapter);
        }

        public void bind(Homework homework){
            title.setText(homework.getLesson());
            adapter.setList(homework.getTasks());
        }
    }
}
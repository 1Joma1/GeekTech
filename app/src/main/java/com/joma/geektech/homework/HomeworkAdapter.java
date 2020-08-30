package com.joma.geektech.homework;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.Homework;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {

    private List<Homework> list = new ArrayList<>();
    private boolean teacher;

    public void setList(List<Homework> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public HomeworkAdapter(boolean teacher) {
        this.teacher = teacher;
    }

    public void addItem(Homework homework) {
        this.list.add(homework);
        notifyDataSetChanged();
    }
    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public List<Homework> getList() {
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

    class ViewHolder extends RecyclerView.ViewHolder {

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

        public void bind(final Homework homework) {
            title.setText(homework.getLesson());
            adapter.setList(homework);
            if (teacher) {
                title.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                        alert.setTitle(title.getContext().getResources().getString(R.string.are_you_sure_to_delet));
                        alert.setNeutralButton(title.getContext().getResources().getString(R.string.cancel), null);
                        alert.setPositiveButton(title.getContext().getResources().getString(R.string.delete), (dialogInterface, i) -> {
                            list.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            FirebaseFirestore.getInstance().collection("Homework").document(homework.getId()).delete();
                        });
                        alert.show();
                        return true;
                    }
                });
            }
        }
    }
}
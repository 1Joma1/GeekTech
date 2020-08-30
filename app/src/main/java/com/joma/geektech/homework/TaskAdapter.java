package com.joma.geektech.homework;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.Homework;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private Homework homework = new Homework();

    public void setList(Homework homework){
        this.homework = homework;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(homework.getTasks().get(position));
    }

    @Override
    public int getItemCount() {
        return homework.getTasks().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView taskText;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskText = itemView.findViewById(R.id.item_task_task);
            checkBox = itemView.findViewById(R.id.item_task_checkbox);
        }

        public void bind(String task){
            taskText.setText(task);
            taskText.setOnLongClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle(taskText.getContext().getResources().getString(R.string.are_you_sure_to_delet));
                alert.setNeutralButton(taskText.getContext().getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(taskText.getContext().getResources().getString(R.string.delete), (dialogInterface, i) -> {
                    homework.getTasks().remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    FirebaseFirestore.getInstance().collection("Homework").document(homework.getId()).set(homework);
                });
                alert.show();
                return true;
            });
        }
    }
}

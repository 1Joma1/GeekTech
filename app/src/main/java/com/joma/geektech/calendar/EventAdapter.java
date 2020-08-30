package com.joma.geektech.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<String> list = new ArrayList<>();
    private String date;

    public void setList(List<String> list, String date) {
        this.date = date;
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addItem(String task) {
        this.list.add(task);
        notifyDataSetChanged();
    }

    public List<String> getList() {
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
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView event;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event = itemView.findViewById(R.id.item_event_text);

        }

        public void bind(String task) {
            event.setText(task);
            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle(itemView.getContext().getResources().getString(R.string.are_you_sure_to_delet));
                alert.setNeutralButton(itemView.getContext().getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(itemView.getContext().getResources().getString(R.string.delete), (dialogInterface, i) -> {
                    list.remove(getAdapterPosition());
                    Map<String, Object> map = new HashMap<>();
                    map.put("events", list);
                    FirebaseFirestore.getInstance().collection("Calendar").document(date).set(map);
                    notifyItemRemoved(getAdapterPosition());
                });
                alert.show();
                return true;
            });
        }
    }
}

package com.joma.geektech.profile;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.chat.ChatAdapter;
import com.joma.geektech.model.Chat;
import com.joma.geektech.model.GeekCoin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GeekCoinHistoryAdapter extends RecyclerView.Adapter<GeekCoinHistoryAdapter.ViewHolder> {

    private List<GeekCoin> list = new ArrayList<>();

    public void setList(List<GeekCoin> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(GeekCoin geekCoin) {
        this.list.add(geekCoin);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public List<GeekCoin> getList() {
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_geektech_history, parent, false);
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

        private TextView nameFrom, nameTo, date;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            nameFrom = itemView.findViewById(R.id.item_geekcoin_history_name_from);
            nameTo = itemView.findViewById(R.id.item_geekcoin_history_name_to);
            date = itemView.findViewById(R.id.item_geekcoin_history_date);
        }

        public void bind(GeekCoin geekCoin) {
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy  HH:mm",
                    Locale.getDefault());
            nameFrom.setText(geekCoin.getFromName());
            nameTo.setText(geekCoin.getToName());
            date.setText(sfd.format(new Date(geekCoin.getTime().getSeconds() * 1000)));
        }
    }

}

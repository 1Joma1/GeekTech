package com.joma.geektech.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.homework.HomeworkAdapter;
import com.joma.geektech.homework.TaskAdapter;
import com.joma.geektech.model.Chat;
import com.joma.geektech.model.Homework;
import com.joma.geektech.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> list = new ArrayList<>();
    private String phone;

    public ChatAdapter(Context context) {
        phone = Utils.getPhone(context);
    }

    public void setList(List<Chat> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Chat chat) {
        this.list.add(chat);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public List<Chat> getList() {
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
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

        private View receive, send;
        private ImageView receiveImage, sendImage;
        private TextView receiveMessage, sendMessage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            receive = itemView.findViewById(R.id.item_chat_receive);
            send = itemView.findViewById(R.id.item_chat_send);
            receiveImage = itemView.findViewById(R.id.item_chat_receive_profile);
            sendImage = itemView.findViewById(R.id.item_chat_send_profile);
            receiveMessage = itemView.findViewById(R.id.item_chat_receive_message);
            sendMessage = itemView.findViewById(R.id.item_chat_send_message);

        }

        public void bind(final Chat chat) {
            if (phone.equals(chat.getFrom())) {
                receive.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                if (!chat.getFromImage().isEmpty()) {
                    Glide.with(sendImage.getContext())
                            .load(chat.getFromImage())
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                            .into(sendImage);
                }
                sendMessage.setText(chat.getMessage());
                send.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                        alert.setTitle("Are you sure to delete?");
                        alert.setNeutralButton("Cancel", null);
                        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore.getInstance().collection("Chat").document(chat.getId()).delete();
                            }
                        });
                        alert.show();
                        return true;
                    }
                });
            } else {
                send.setVisibility(View.GONE);
                send.setOnLongClickListener(null);
                receive.setVisibility(View.VISIBLE);
                Glide.with(receiveImage.getContext())
                        .load(chat.getFromImage())
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                        .into(receiveImage);
                receiveMessage.setText(chat.getMessage());
            }
        }
    }
}
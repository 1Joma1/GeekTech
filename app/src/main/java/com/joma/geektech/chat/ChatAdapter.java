package com.joma.geektech.chat;

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
import com.joma.geektech.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> list = new ArrayList<>();
    private String id;

    public ChatAdapter(String id) {
        this.id = id;
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
        private TextView receiveMessage, sendMessage, name;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            receive = itemView.findViewById(R.id.item_chat_receive);
            send = itemView.findViewById(R.id.item_chat_send);
            receiveImage = itemView.findViewById(R.id.item_chat_receive_profile);
            sendImage = itemView.findViewById(R.id.item_chat_send_profile);
            receiveMessage = itemView.findViewById(R.id.item_chat_receive_message);
            sendMessage = itemView.findViewById(R.id.item_chat_send_message);
            name = itemView.findViewById(R.id.item_chat_receive_name);

        }

        public void bind(final Chat chat) {
            if (id.equals(chat.getFromId())) {
                receive.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                if (chat.getFromImage() != null) {
                    Glide.with(sendImage.getContext())
                            .load(chat.getFromImage())
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                            .into(sendImage);
                }
                sendMessage.setText(chat.getMessage());
                send.setOnLongClickListener(view -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                    alert.setTitle(send.getContext().getResources().getString(R.string.are_you_sure_to_delet));
                    alert.setNeutralButton(send.getContext().getResources().getString(R.string.cancel), null);
                    alert.setPositiveButton(send.getContext().getResources().getString(R.string.delete), (dialogInterface, i) -> FirebaseFirestore.getInstance().collection("Chat").document(chat.getId()).delete());
                    alert.show();
                    return true;
                });
            } else {
                send.setVisibility(View.GONE);
                send.setOnLongClickListener(null);
                receive.setVisibility(View.VISIBLE);
                if (chat.getFromImage() != null) {
                    Glide.with(receiveImage.getContext())
                            .load(chat.getFromImage())
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                            .into(receiveImage);
                }
                receiveMessage.setText(chat.getMessage());
                name.setText(chat.getFrom());
            }
        }
    }
}
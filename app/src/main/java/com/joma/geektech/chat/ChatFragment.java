package com.joma.geektech.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.joma.geektech.R;
import com.joma.geektech.model.Chat;
import com.joma.geektech.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText message;
    private ImageView send;

    private ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view) {
        adapter = new ChatAdapter(getContext());
        message = view.findViewById(R.id.chat_message);
        send = view.findViewById(R.id.chat_send);
        recyclerView = view.findViewById(R.id.chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utils.getText(message).length() < 1) {
                    send.setVisibility(View.GONE);
                } else {
                    send.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat(Utils.getPhone(getContext()), Utils.getText(message), Utils.getProfile(getContext()));
                send.setVisibility(View.GONE);
                message.setText("");
                FirebaseFirestore.getInstance().collection("Chat").add(chat);
            }
        });

        FirebaseFirestore.getInstance().collection("Chat").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    adapter.clear();
                    for (DocumentSnapshot d : value.getDocuments()) {
                        Chat chat = d.toObject(Chat.class);
                        chat.setId(d.getId());
                        adapter.addItem(chat);
                    }
                    recyclerView.scrollToPosition(adapter.getList().size() - 1);
                }
            }
        });
    }
}

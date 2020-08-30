package com.joma.geektech.homework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.joma.geektech.R;
import com.joma.geektech.model.Homework;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

import java.io.Serializable;

public class HomeworkFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private ImageView add;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, null);

        user = Utils.getUser(getContext());
        initView(view);
        listener();
        return view;
    }

    private void initView(View view) {
        adapter = new HomeworkAdapter(user.isTeacher());
        add = view.findViewById(R.id.homework_add);
        recyclerView = view.findViewById(R.id.homework_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        if (user.isTeacher()) {
            add.setVisibility(View.VISIBLE);
        }
        add.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddHomeworkActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore.getInstance().collection("Homework").whereEqualTo("group", user.getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            adapter.clear();
            for (DocumentSnapshot d : queryDocumentSnapshots) {
                Homework homework = d.toObject(Homework.class);
                homework.setId(d.getId());
                adapter.addItem(homework);
            }
        });

    }
}

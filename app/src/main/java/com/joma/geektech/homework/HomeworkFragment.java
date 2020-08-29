package com.joma.geektech.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.joma.geektech.R;
import com.joma.geektech.model.Homework;

public class HomeworkFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view) {
        adapter = new HomeworkAdapter();
        recyclerView = view.findViewById(R.id.homework_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        FirebaseFirestore.getInstance().collection("Homework").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter.setList(queryDocumentSnapshots.toObjects(Homework.class));
            }
        });
    }
}

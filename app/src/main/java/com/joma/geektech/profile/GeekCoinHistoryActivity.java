package com.joma.geektech.profile;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.joma.geektech.R;
import com.joma.geektech.model.GeekCoin;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

public class GeekCoinHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GeekCoinHistoryAdapter adapter;
    private User user;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geekcoin_history);

        id = getIntent().getStringExtra("id");
        user = Utils.getUser(this);
        recyclerView = findViewById(R.id.geekcoin_history_recycler);
        adapter = new GeekCoinHistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.e("-----------id", id);
        FirebaseFirestore.getInstance().collection("GeekCoin").whereEqualTo("to", id).get().addOnSuccessListener(q -> adapter.setList(q.toObjects(GeekCoin.class)));
    }
}

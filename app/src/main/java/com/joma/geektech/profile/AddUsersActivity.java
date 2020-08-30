package com.joma.geektech.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.joma.geektech.R;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

public class AddUsersActivity extends AppCompatActivity implements UsersAdapter.onItemClick {

    private RecyclerView recyclerView;
    private EditText name, password, thread, coin;
    private CheckBox admin, teacher;
    private Button add;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        findView();
        listener();
    }

    private void findView() {
        adapter = new UsersAdapter(this);
        recyclerView = findViewById(R.id.add_user_recycler);
        name = findViewById(R.id.add_user_name);
        password = findViewById(R.id.add_user_password);
        thread = findViewById(R.id.add_user_thread);
        coin = findViewById(R.id.add_user_coin);
        admin = findViewById(R.id.add_user_admin);
        teacher = findViewById(R.id.add_user_teacher);
        add = findViewById(R.id.add_user_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        add.setOnClickListener(view -> {
            if (Utils.getText(name).length() < 1) {
                name.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(password).length() < 1) {
                password.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(thread).length() < 1) {
                thread.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(coin).length() < 1) {
                coin.setError(getResources().getString(R.string.must_fill));
            } else {
                add.setText(getResources().getString(R.string.add));
                User user = new User();
                user.setName(Utils.getText(name));
                user.setPassword(Utils.getText(password));
                user.setGroup(Utils.getText(thread));
                user.setCoin(Utils.getText(coin));
                user.setAdmin(admin.isChecked());
                user.setTeacher(teacher.isChecked());
                FirebaseFirestore.getInstance().collection("User").add(user);
                name.setText("");
                password.setText("");
                thread.setText("");
                coin.setText("");
                Toast.makeText(AddUsersActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
            }
        });

        admin.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                teacher.setChecked(false);
            }
        });
        teacher.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                admin.setChecked(false);
            }
        });

        FirebaseFirestore.getInstance().collection("User").addSnapshotListener((value, error) -> {
            if (value != null) {
                adapter.clear();
                for (DocumentSnapshot d : value) {
                    User user = d.toObject(User.class);
                    user.setId(d.getId());
                    adapter.addItem(user);
                }
            }

        });
    }

    @Override
    public void edit(int pos) {
        User user = adapter.getList().get(pos);
        String profile = user.getProfile();
        name.setText(user.getName());
        password.setText(user.getPassword());
        thread.setText(user.getGroup());
        coin.setText(user.getCoin());
        admin.setChecked(user.isAdmin());
        teacher.setChecked(user.isTeacher());
        add.setText(getResources().getString(R.string.edit));
        add.setOnClickListener(view -> {
            if (Utils.getText(name).length() < 1) {
                name.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(password).length() < 1) {
                password.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(thread).length() < 1) {
                thread.setError(getResources().getString(R.string.must_fill));
            } else if (Utils.getText(coin).length() < 1) {
                coin.setError(getResources().getString(R.string.must_fill));
            } else {
                add.setText(getResources().getString(R.string.add));
                user.setName(Utils.getText(name));
                user.setPassword(Utils.getText(password));
                user.setGroup(Utils.getText(thread));
                user.setCoin(Utils.getText(coin));
                user.setAdmin(admin.isChecked());
                user.setTeacher(teacher.isChecked());
                user.setProfile(profile);
                FirebaseFirestore.getInstance().collection("User").document(user.getId()).set(user);
                name.setText("");
                password.setText("");
                thread.setText("");
                coin.setText("");
                Toast.makeText(AddUsersActivity.this, getResources().getString(R.string.edited), Toast.LENGTH_SHORT).show();
                listener();
            }
        });
    }
}

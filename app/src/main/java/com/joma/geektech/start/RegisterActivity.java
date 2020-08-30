package com.joma.geektech.start;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joma.geektech.main.MainActivity;
import com.joma.geektech.R;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    public static void start(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    private Button login;
    private EditText password, name;
    private ImageView profile;
    private User user = new User();
    private StorageReference storageReference;

    private boolean profileExists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findView();
        listener();

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void findView() {
        login = findViewById(R.id.register_login);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        profile = findViewById(R.id.register_profile);
    }

    private void listener() {
        profile.setDrawingCacheEnabled(true);
        profile.buildDrawingCache();
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 21);
        });
        login.setOnClickListener(view -> {
            String n = Utils.getText(name);
            String p = Utils.getText(password);
            if (n.length() < 1) {
                name.setError(getResources().getString(R.string.must_fill));
                return;
            }
            if (p.length() < 1) {
                password.setError(getResources().getString(R.string.must_fill));
                return;
            }
            login.setVisibility(View.GONE);
            signIn(n, p);
        });
    }


    private void signIn(String nameS, String passwordS) {
        user.setName(Utils.getText(name));
        FirebaseFirestore.getInstance()
                .collection("User")
                .whereEqualTo("name", nameS)
                .whereEqualTo("password", passwordS)
                .get()
                .addOnSuccessListener(q -> {
                    if (!q.getDocuments().isEmpty() && q.getDocuments().get(0).exists()) {
                        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("login", false).apply();
                        final User user = q.getDocuments().get(0).toObject(User.class);
                        user.setId(q.getDocuments().get(0).getId());
                        if (profileExists) {
                            Bitmap bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                            storageReference.child(user.getId() + ".jpg").putBytes(baos.toByteArray()).addOnCompleteListener(task -> storageReference.child(user.getId() + ".jpg").getDownloadUrl().addOnCompleteListener(task1 -> {
                                user.setProfile(task1.getResult() + "");
                                Map<String, Object> map = new HashMap<>();
                                map.put("profile", user.getProfile());
                                FirebaseFirestore.getInstance().collection("User").document(user.getId()).update(map);
                                Utils.setUser(RegisterActivity.this, user);
                                MainActivity.start(RegisterActivity.this);
                                finish();
                            }));
                        } else {
                            Utils.setUser(RegisterActivity.this, user);
                            MainActivity.start(RegisterActivity.this);
                            finish();
                        }
                    } else {
                        login.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> login.setVisibility(View.VISIBLE));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 21 && data != null) {
            profileExists = true;
            Glide.with(this)
                    .asBitmap()
                    .load(data.getData())
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                    .into(profile);
        }
    }
}

package com.joma.geektech.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

public class ProfileFragment extends Fragment {

    private ImageView profile;
    private TextView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view){
        profile = view.findViewById(R.id.profile_profile);
        name = view.findViewById(R.id.profile_name);

    }

    private void listener(){
        String phone = Utils.getPhone(getContext());
        FirebaseFirestore.getInstance().collection("User").document(phone).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                name.setText(user.getName());
                Glide.with(getContext())
                        .asBitmap()
                        .load(user.getProfile())
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                        .into(profile);
            }
        });
    }
}

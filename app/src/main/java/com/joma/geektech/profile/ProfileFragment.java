package com.joma.geektech.profile;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joma.geektech.R;
import com.joma.geektech.model.GeekCoin;
import com.joma.geektech.model.User;
import com.joma.geektech.start.RegisterActivity;
import com.joma.geektech.util.SimpleAnimatorListener;
import com.joma.geektech.util.Utils;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ImageView profile, addUser, code, logOut;
    private TextView name, coin, role, thread;
    private Button give, get, lesson1, lesson2, lesson3, lesson4, lesson5, lesson6, lesson7, lesson8, lesson9;
    private View lessons, geekcoinLayout;
    private String forWhat;
    private User user;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view) {
        profile = view.findViewById(R.id.profile_profile);
        profile.setDrawingCacheEnabled(true);
        profile.buildDrawingCache();
        name = view.findViewById(R.id.profile_name);
        addUser = view.findViewById(R.id.profile_add_user);
        get = view.findViewById(R.id.profile_get);
        give = view.findViewById(R.id.profile_give);
        code = view.findViewById(R.id.profile_code);
        lessons = view.findViewById(R.id.profile_lessons);
        lesson1 = view.findViewById(R.id.profile_lesson1);
        lesson2 = view.findViewById(R.id.profile_lesson2);
        lesson3 = view.findViewById(R.id.profile_lesson3);
        lesson4 = view.findViewById(R.id.profile_lesson4);
        lesson5 = view.findViewById(R.id.profile_lesson5);
        lesson6 = view.findViewById(R.id.profile_lesson6);
        lesson7 = view.findViewById(R.id.profile_lesson7);
        lesson8 = view.findViewById(R.id.profile_lesson8);
        lesson9 = view.findViewById(R.id.profile_lesson9);
        logOut = view.findViewById(R.id.profile_log_out);
        coin = view.findViewById(R.id.profile_geek_coin);
        role = view.findViewById(R.id.profile_role);
        thread = view.findViewById(R.id.profile_thread);
        geekcoinLayout = view.findViewById(R.id.profile_layout);
    }

    private void listener() {
        storageReference = FirebaseStorage.getInstance().getReference();
        user = Utils.getUser(getContext());
        name.setText(user.getName());
        coin.setText(user.getCoin());
        thread.setText(user.getGroup());
        if (user.getProfile() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(user.getProfile())
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                    .into(profile);
        }
        if (user.isAdmin()) {
            addUser.setVisibility(View.VISIBLE);
        } else if (!user.isTeacher()){
            geekcoinLayout.setVisibility(View.VISIBLE);
        }
        addUser.setOnClickListener(view -> startActivity(new Intent(getContext(), AddUsersActivity.class)));
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lessons.getVisibility() == View.VISIBLE) {
                    lessons.animate().alpha(0).setDuration(300).setListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            lessons.setVisibility(View.GONE);
                        }
                    });
                }
                if (code.getAlpha() == 0) {
                    code.animate().alpha(1).setDuration(300);
                } else {
                    code.animate().alpha(0).setDuration(300);
                }
            }
        });
        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code.getAlpha() == 1) {
                    code.animate().alpha(0).setDuration(300);
                }
                if (lessons.getVisibility() == View.GONE) {
                    lessons.setVisibility(View.VISIBLE);
                    lessons.animate().alpha(1).setDuration(300).setListener(null);
                } else {
                    lessons.animate().alpha(0).setDuration(300).setListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            lessons.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(getResources().getString(R.string.are_you_sure_to_log_out));
                alert.setNeutralButton(getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(getResources().getString(R.string.log_out), (dialogInterface, i) -> {
                    if (getActivity() != null) {
                        Utils.clear(getActivity());
                        getActivity().startActivity(new Intent(getContext(), RegisterActivity.class));
                        getActivity().finish();
                    }
                });
                alert.show();
            }
        });

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(user.getId(), BarcodeFormat.QR_CODE, 600, 600);
            code.setImageBitmap(bitmap);
        } catch (Exception e) {

        }
        if (user.isAdmin()){
            role.setText(getResources().getString(R.string.admin));
        } else if (user.isTeacher()){
            role.setText(getResources().getString(R.string.teacher));
        } else {
            role.setText(getResources().getString(R.string.student));
        }
        lesson1.setOnClickListener(view -> {
            forWhat = "Урок 1";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson2.setOnClickListener(view -> {
            forWhat = "Урок 2";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson3.setOnClickListener(view -> {
            forWhat = "Урок 3";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson4.setOnClickListener(view -> {
            forWhat = "Урок 4";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson5.setOnClickListener(view -> {
            forWhat = "Урок 5";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson6.setOnClickListener(view -> {
            forWhat = "Урок 6";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson7.setOnClickListener(view -> {
            forWhat = "Урок 7";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson8.setOnClickListener(view -> {
            forWhat = "Урок 8";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
        lesson9.setOnClickListener(view -> {
            forWhat = "Другое";
            IntentIntegrator.forSupportFragment(ProfileFragment.this).setBeepEnabled(false).setOrientationLocked(false).initiateScan();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.cancelled), Toast.LENGTH_SHORT).show();
            } else {
                if (Integer.valueOf(user.getCoin()) < 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.you_dont_have_geekcoin), Toast.LENGTH_LONG).show();
                    return;
                }
                int coins = Integer.valueOf(user.getCoin());
                int resultCoin = coins - 1;
                Map<String, Object> map = new HashMap<>();
                map.put("coin", resultCoin+"");
                FirebaseFirestore.getInstance().collection("User").document(user.getId()).update(map);
                coin.setText(resultCoin + "");
                user.setCoin(resultCoin+"");
                Utils.setUser(getContext(), user);
                FirebaseFirestore.getInstance().collection("User").document(result.getContents()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        int coins = Integer.valueOf(d.getString("coin"));
                        int resultCoin = coins + 1;
                        Map<String, Object> map = new HashMap<>();
                        map.put("coin", resultCoin+"");
                        FirebaseFirestore.getInstance().collection("User").document(result.getContents()).update(map);
                        GeekCoin geekCoin = new GeekCoin(user.getId(), user.getName(), result.getContents(), d.getString("name"), forWhat);
                        FirebaseFirestore.getInstance().collection("GeekCoin").add(geekCoin);
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

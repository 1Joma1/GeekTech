package com.joma.geektech.start;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joma.geektech.main.MainActivity;
import com.joma.geektech.R;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {


    public static void start(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    private Button login;
    private EditText phone, code, name;
    private ImageView profile;
    private TextView codeTextView;
    private User user = new User();
    private StorageReference storageReference;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private boolean profileExists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findView();
        listener();

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                name.setEnabled(false);
                phone.setEnabled(false);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phone.setError("invalid_phone_number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    phone.setError("quota_exceeded");
                }
                name.setEnabled(true);
                phone.setEnabled(true);
                resetView(true);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                resetView(false);
                name.setEnabled(false);
                phone.setEnabled(false);
                mVerificationId = verificationId;

            }
        };
    }

    private void findView() {
        login = findViewById(R.id.register_login);
        phone = findViewById(R.id.register_phone);
        name = findViewById(R.id.register_name);
        profile = findViewById(R.id.register_profile);
        code = findViewById(R.id.register_code);
        codeTextView = findViewById(R.id.register_code_text);
    }

    private void listener() {
        profile.setDrawingCacheEnabled(true);
        profile.buildDrawingCache();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 21);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getText(name).length() < 1) {
                    name.setError("Must fill");
                    return;
                }
                login.setVisibility(View.GONE);
                if (code.getVisibility() == View.INVISIBLE) {
                    startVerification(Utils.getText(phone));
                } else {
                    if (Utils.getText(code).length() < 5) {
                        code.setError("Must fill");
                    } else {
                        verifyPhoneNumberWithCode(mVerificationId, Utils.getText(code));
                    }
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utils.getText(phone).length() == 0) {
                    phone.setText("+");
                    phone.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void startVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setName(Utils.getText(name));
                            user.setPhone(Utils.getText(phone));
                            if (profileExists) {
                                Bitmap bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                storageReference.child(Utils.getText(phone) + ".jpg").putBytes(baos.toByteArray()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        storageReference.child(Utils.getText(phone) + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                user.setProfile(task.getResult() + "");
                                                FirebaseFirestore.getInstance().collection("User").document(user.getPhone()).set(user);
                                                MainActivity.start(RegisterActivity.this);
                                                Utils.setUser(RegisterActivity.this, Utils.getText(phone), user.getProfile());
                                                finish();
                                            }
                                        });
                                    }
                                });
                            } else {
                                FirebaseFirestore.getInstance().collection("User").document(user.getPhone()).set(user);
                                MainActivity.start(RegisterActivity.this);
                                Utils.setUser(RegisterActivity.this, Utils.getText(phone), "");
                                finish();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this, "wrong code", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "failed", Toast.LENGTH_LONG).show();
                            }
                            login.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (code.getVisibility() == View.VISIBLE) {
            resetView(true);
        } else {
            super.onBackPressed();
        }
    }

    private void resetView(boolean fail) {
        login.setVisibility(View.VISIBLE);
        if (fail) {
            codeTextView.animate().setDuration(200).translationY(20).alpha(0);
            code.animate().setDuration(200).translationY(20).alpha(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    code.setVisibility(View.INVISIBLE);
                    codeTextView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            codeTextView.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            codeTextView.animate().setDuration(200).translationY(0).alpha(1);
            code.animate().setDuration(200).translationY(0).alpha(1).setListener(null);
        }
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

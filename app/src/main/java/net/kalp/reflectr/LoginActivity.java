
package net.kalp.reflectr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.fredporciuncula.phonemoji.PhonemojiTextInputEditText;
import com.fredporciuncula.phonemoji.PhonemojiTextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.kalp.reflectr.logineduser.HomeActivity;
import net.kalp.reflectr.logineduser.ProfileFormActivity;
import net.kalp.reflectr.models.User;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    PhonemojiTextInputLayout phone_number_layout;
    PhonemojiTextInputEditText phone_number;
    Button get_otp,resend_otp;
    ProgressBar progress_bar;
    PinView otp;
    String phone_number_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        if (getActionBar()!=null)
            getActionBar().hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        //noinspection deprecation
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setAppearanceLightStatusBars(true);
        phone_number_layout = findViewById(R.id.phone_number_layout);
        phone_number = findViewById(R.id.phone_number);
        get_otp = findViewById(R.id.get_otp);
        progress_bar = findViewById(R.id.progress_bar);
        otp = findViewById(R.id.otp_view);
        resend_otp = findViewById(R.id.resend_otp);
        otp.setHideLineWhenFilled(false);

        resend_otp.setOnClickListener(v -> {
            resend_otp.setVisibility(View.INVISIBLE);
            resend_otp.setClickable(false);
            progress_bar.setVisibility(View.VISIBLE);
            signIn();
        });
        get_otp.setOnClickListener(v -> {
            phone_number_text = Objects.requireNonNull(phone_number.getText()).toString();
            if (phone_number_text.equals("")) {
                phone_number_layout.setError("Please enter your phone number");
                Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
            } else if (phone_number_text.split(" ").length<=1) {
                phone_number_layout.setError("Please enter your phone number");
                Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
            } else if (phone_number_text.split(" ")[1].length()<10) {
                phone_number_layout.setError("Please enter a valid phone number");
                Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            } else {
                phone_number_layout.setError(null);
                get_otp.setVisibility(View.INVISIBLE);
                get_otp.setClickable(false);
                progress_bar.setVisibility(View.VISIBLE);
                signIn();
            }
        });
    }
    void signIn() {
        phone_number_layout = findViewById(R.id.phone_number_layout);
        phone_number = findViewById(R.id.phone_number);
        get_otp = findViewById(R.id.get_otp);
        progress_bar = findViewById(R.id.progress_bar);
        otp = findViewById(R.id.otp_view);
        resend_otp = findViewById(R.id.resend_otp);
        otp.setHideLineWhenFilled(false);
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phone_number_text)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(getApplicationContext(), "Verification Completed", Toast.LENGTH_SHORT).show();
                        userAfterLogin();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        get_otp.setVisibility(View.VISIBLE);
                        get_otp.setClickable(true);
                        resend_otp.setVisibility(View.INVISIBLE);
                        resend_otp.setClickable(false);
                        phone_number_layout.setVisibility(View.VISIBLE);
                        otp.setVisibility(View.INVISIBLE);
                        progress_bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
                        otp.setVisibility(View.VISIBLE);
                        otp.setClickable(true);
                        get_otp.setVisibility(View.INVISIBLE);
                        get_otp.setClickable(false);
                        resend_otp.setVisibility(View.VISIBLE);
                        resend_otp.setClickable(true);
                        progress_bar.setVisibility(View.INVISIBLE);
                        phone_number_layout.setVisibility(View.INVISIBLE);
                        otp.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (Objects.requireNonNull(otp.getText()).toString().length()==6) {
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp.getText().toString());
                                    FirebaseAuth.getInstance().signInWithCredential(credential)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                    userAfterLogin();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }
    public void userAfterLogin(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userUid = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userUid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    if (Boolean.TRUE.equals(document.getBoolean("is_profile_completed"))){
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), ProfileFormActivity.class));
                        finish();
                    }
                } else {
                    User user = new User(false,firebaseUser.getPhoneNumber());
                    db.collection("users").document(userUid).set(user)
                            .addOnSuccessListener(aVoid -> {
                                startActivity(new Intent(getApplicationContext(),ProfileFormActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
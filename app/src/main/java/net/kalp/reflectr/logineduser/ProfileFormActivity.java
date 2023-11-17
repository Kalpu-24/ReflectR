package net.kalp.reflectr.logineduser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.kalp.reflectr.R;
import net.kalp.reflectr.models.User;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileFormActivity extends AppCompatActivity {

    ImageView profile_pic;
    TextInputEditText name, email, age, bio, city, country;
    Button submit;
    Boolean isProfilePicSelected = false;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            result -> {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Glide.with(getApplicationContext()).load(result).into(profile_pic);
                    isProfilePicSelected = true;
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
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
        profile_pic = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        bio = findViewById(R.id.bio);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        submit = findViewById(R.id.Submit);
        profile_pic.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
        submit.setOnClickListener(view -> {
            String nameString = Objects.requireNonNull(name.getText()).toString();
            String emailString = Objects.requireNonNull(email.getText()).toString();
            String ageString = Objects.requireNonNull(age.getText()).toString();
            String bioString = Objects.requireNonNull(bio.getText()).toString();
            String cityString = Objects.requireNonNull(city.getText()).toString();
            String countryString = Objects.requireNonNull(country.getText()).toString();
            if (nameString.isEmpty() || emailString.isEmpty() || ageString.isEmpty() || bioString.isEmpty() || cityString.isEmpty() || countryString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (!isProfilePicSelected) {
                Toast.makeText(getApplicationContext(), "Please select a profile picture", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    //upload profile pic and get the url
                    profile_pic.setDrawingCacheEnabled(true);
                    profile_pic.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profile_pic.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference storageReference =  storage.getReference().child("profile_pics").child(firebaseUser.getUid());
                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.continueWithTask(task1 -> {
                        if (!task1.isSuccessful()) {
                            throw Objects.requireNonNull(task1.getException());
                        }
                        return storageReference.getDownloadUrl();
                    }).addOnCompleteListener(task12 -> {
                        if (task12.isSuccessful()) {
                            Uri downloadUri = task12.getResult();
                            String profile_pic_url = downloadUri.toString();
                            User user = new User.Builder()
                                    .setUsername(nameString)
                                    .setEmail(emailString)
                                    .setAge(Integer.parseInt(ageString))
                                    .setPhone(firebaseUser.getPhoneNumber())
                                    .setBio(bioString)
                                    .setCity(cityString)
                                    .setCountry(countryString)
                                    .setProfile_pic(profile_pic_url)
                                    .setIs_profile_completed(true)
                                    .build();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
                            docRef.set(user).addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Profile Completed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(getApplicationContext(), "Error uploading profile pic", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
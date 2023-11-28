package net.kalp.reflectr.logineduser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masoudss.lib.WaveformSeekBar;

import net.kalp.reflectr.DefaultMoods;
import net.kalp.reflectr.R;
import net.kalp.reflectr.models.Journal;
import net.kalp.reflectr.models.Mood;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    public static final Integer IMAGE = 0;
    public static final Integer VIDEO = 1;
    public static final Integer AUDIO = 2;
    Integer file_type;
    Uri file_uri;
    ScrollView rootScrollView;
    RecyclerView gridView;
    Integer SelectedItem;
    TextView dateInput,timeInput;
    ImageView imageInput;
    VideoView videoInput;
    WaveformSeekBar audioInput;
    GridLayout quickNoteFileGrid;
    MaterialCardView videoInputFrame;
    LinearLayout dateLayout,timeLayout,imageInputLayout,videoInputLayout,audioInputLayout;
    TextInputEditText titleInput,contentInput,categoryInput;
    ChipGroup emotionsGroup;
    MaterialButtonToggleGroup privacyToggleGroup;
    MaterialButton publicButton, privateButton, addButton;
    Boolean isMediaPresent=false;
    List<String> selectedEmotions = new ArrayList<>(),emotions = new ArrayList<>(Arrays.asList("Anger","Sadness","Fear","Excitement","Disgust","Anxiety","Embarrassment","Surprise","Love","Shame","Depression","Satisfaction","Envy","Boredom","Guilt","Contempt","Compassion","Hatred","Frustration","Pride","Affection","Self-confidence","Jealousy","Nostalgia","Relief","Hope","Disappointment","Regret","Optimism","Trust","Gratitude","Loneliness","Confusion","Shyness","Amusement","Curiosity"));
    ActivityResultLauncher<PickVisualMediaRequest> pickImage = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            result -> {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Glide.with(getApplicationContext()).load(result).override(Target.SIZE_ORIGINAL).into(imageInput);
                    isMediaPresent = true;
                    file_type = IMAGE;
                    file_uri = result;
                    imageInput.setVisibility(ImageView.VISIBLE);
                    quickNoteFileGrid.setVisibility(GridLayout.GONE);
                }
            });

    ActivityResultLauncher<PickVisualMediaRequest> pickVideo = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            result -> {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "No video Selected", Toast.LENGTH_SHORT).show();
                } else {
                    videoInput.setVideoURI(result);
                    isMediaPresent = true;
                    file_type = VIDEO;
                    file_uri = result;
                    videoInputFrame.setVisibility(VideoView.VISIBLE);
                    quickNoteFileGrid.setVisibility(GridLayout.GONE);
                    videoInput.start();
                }
            });

    ActivityResultLauncher<Intent> pickAudio = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "No audio Selected", Toast.LENGTH_SHORT).show();
                } else {
                    assert result.getData() != null;
                    audioInput.setSampleFrom(Objects.requireNonNull(result.getData().getData()));
                    isMediaPresent = true;
                    file_type = AUDIO;
                    file_uri = result.getData().getData();
                    audioInput.setVisibility(WaveformSeekBar.VISIBLE);
                    quickNoteFileGrid.setVisibility(GridLayout.GONE);
                }
            });
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        //getting system informtions
        Calendar date = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        int year = date.get(Calendar.YEAR);
        String month = simpleDateFormat.format(date.getTime());
        int day = date.get(Calendar.DAY_OF_MONTH);
        Date currentTime = Calendar.getInstance().getTime();


        //hiding action bar and setting status bar and nav bar color
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        if (getActionBar()!=null)
            getActionBar().hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        //noinspection deprecation
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setAppearanceLightStatusBars(true);


        //initializing views
        rootScrollView = findViewById(R.id.rootScrollView);
        emotionsGroup = findViewById(R.id.emotions_chip_group);
        privacyToggleGroup = findViewById(R.id.privacyToggleGroup);
        publicButton = findViewById(R.id.publicButton);
        privateButton = findViewById(R.id.privateButton);
        gridView = findViewById(R.id.gridView);
        dateLayout = findViewById(R.id.dateLayout);
        dateInput = findViewById(R.id.date);
        timeLayout = findViewById(R.id.timeLayout);
        timeInput = findViewById(R.id.time);
        addButton = findViewById(R.id.addButton);
        titleInput = findViewById(R.id.title);
        contentInput = findViewById(R.id.quick_note);
        categoryInput = findViewById(R.id.categoryInput);
        imageInputLayout = findViewById(R.id.imageInputLayout);
        videoInputLayout = findViewById(R.id.videoInputLayout);
        audioInputLayout = findViewById(R.id.audioInputLayout);
        imageInput = findViewById(R.id.imageInput);
        videoInput = findViewById(R.id.videoInput);
        audioInput = findViewById(R.id.audioInput);
        quickNoteFileGrid = findViewById(R.id.quick_note_file_grid);
        videoInputFrame = findViewById(R.id.videoInputFrame);


        //setting up views
        publicButton.performClick();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoInput);
        videoInput.setMediaController(mediaController);


        //hiding media controller on scroll
        rootScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });


        //setting up chips
        for (String emotion : emotions) {
            Chip chip = LayoutInflater.from(this).inflate(R.layout.emotion_chip, emotionsGroup, false).findViewById(R.id.emotion_chip);
            chip.setText(emotion);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary99);
            chip.setChipStrokeColorResource(com.google.android.material.R.color.material_dynamic_neutral10);
            chip.setChipStrokeWidth(2);
            chip.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral10,null));
            emotionsGroup.addView(chip);
            chip.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    selectedEmotions.add(emotion);
                    chip.setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary90);
                    chip.setCheckedIconTint(ColorStateList.valueOf(getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral10,null)));
                }
                else{
                    selectedEmotions.remove(emotion);
                    chip.setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary99);
                }
            });
        }


        //setting up date and time
        String dateText = day + " " + month + " " + year;
        String timeText = dateFormat.format(currentTime);
        timeInput.setText(timeText);
        dateInput.setText(dateText);


        //setting up mood grid
        List<Mood> defaultMoods = Arrays.asList(DefaultMoods.moods);
        defaultMoods.sort(Comparator.comparing(Mood::getLevel).reversed());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
        gridView.setLayoutManager(gridLayoutManager);
        MoodAdapter.OnItemClickListener listener = item -> {
            for (int j = 0; j < gridView.getChildCount(); j++) {
                LinearLayout linearLayout = (LinearLayout) gridView.getChildAt(j).findViewById(R.id.moodLayout);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.transparent,null));
            }
            LinearLayout linearLayout = (LinearLayout) gridView.getChildAt(defaultMoods.indexOf(item)).findViewById(R.id.moodLayout);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.rounded_box,null));
            SelectedItem = defaultMoods.indexOf(item);
        };
        MoodAdapter moodAdapter = new MoodAdapter(defaultMoods, getApplicationContext(), listener);
        gridView.setAdapter(moodAdapter);


        //setting up Date picker
        dateLayout.setOnClickListener(view -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setValidator(DateValidatorPointBackward.now());
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Date").setCalendarConstraints(constraintsBuilder.build());
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                int year1 = calendar.get(Calendar.YEAR);
                String month1 = simpleDateFormat.format(calendar.getTime());
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                String dateText1 = day1 + " " + month1 + " " + year1;
                dateInput.setText(dateText1);
            });
        });


        //setting up Time picker
        timeLayout.setOnClickListener(view -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(currentTime.getHours())
                    .setMinute(currentTime.getMinutes())
                    .setTitleText("Select Time")
                    .build();
            materialTimePicker.show(getSupportFragmentManager(), materialTimePicker.toString());
            materialTimePicker.addOnPositiveButtonClickListener(view1 -> {
                String timeText1 = materialTimePicker.getHour() + ":" + materialTimePicker.getMinute();
                timeInput.setText(timeText1);
            });
        });


        //setting up image input
        imageInputLayout.setOnClickListener(view -> {
            pickImage.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });


        //setting up video input
        videoInputLayout.setOnClickListener(view -> {
            pickVideo.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                    .build());
        });


        //setting up audio input
        audioInputLayout.setOnClickListener(view -> {
            pickAudio.launch(new Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI));
        });


        //Managing Journal Input
        addButton.setOnClickListener(view -> {
            if(SelectedItem==null){
                Toast.makeText(getApplicationContext(), "Select A Mood", Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(titleInput.getText()).toString().isEmpty()||titleInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(contentInput.getText()).toString().isEmpty()||contentInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Note", Toast.LENGTH_SHORT).show();
            } else if (selectedEmotions.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Select Emotion tags", Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(categoryInput.getText()).toString().isEmpty()||categoryInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Category", Toast.LENGTH_SHORT).show();
            } else {
                if (isMediaPresent) {
                    Log.d("TAG", "onCreate: "+file_uri);
//                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                    assert firebaseUser != null;
//                    String UID = firebaseUser.getUid();
//                    FirebaseStorage storage = FirebaseStorage.getInstance();
//                    StorageReference storageRef = storage.getReference().child("Files").child(UID);
//                    storageRef.putFile(file_uri).addOnSuccessListener(taskSnapshot -> {
//                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            Toast.makeText(getApplicationContext(), "Upload Complete", Toast.LENGTH_SHORT).show();
//                        });
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
//                    });
                } else {
                    Journal journal = new Journal.Builder()
                            .setMood(defaultMoods.get(SelectedItem))
                            .setTitle(Objects.requireNonNull(titleInput.getText()).toString())
                            .setContent(Objects.requireNonNull(contentInput.getText()).toString())
                            .setEmotion_tags(selectedEmotions)
                            .setCategory(Objects.requireNonNull(categoryInput.getText()).toString())
                            .setIs_private(privacyToggleGroup.getCheckedButtonId() == R.id.publicButton)
                            .setDate(dateInput.getText().toString())
                            .setTime(timeInput.getText().toString())
                            .setIs_media_present(isMediaPresent)
                            .setMedia(null)
                            .setOwner_uid(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .build();
                    Log.d("Journal", journal.toString());
                }
            }
        });
    }
}
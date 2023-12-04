package net.kalp.reflectr.logineduser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masoudss.lib.WaveformSeekBar;

import net.kalp.reflectr.adapters.MoodAdapter;
import net.kalp.reflectr.setup.DefaultMoods;
import net.kalp.reflectr.R;
import net.kalp.reflectr.models.Journal;
import net.kalp.reflectr.models.Media;
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
    MediaPlayer mediaPlayer = new MediaPlayer();
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
    ConstraintLayout loadingLayout;
    MaterialCardView videoInputFrame;
    ImageButton playButton,pauseButton;
    LinearLayout dateLayout,timeLayout,imageInputLayout,videoInputLayout,audioInputLayout,audioInputFrame;
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
                loadingLayout.setVisibility(View.VISIBLE);
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "No audio Selected", Toast.LENGTH_SHORT).show();
                    loadingLayout.setVisibility(View.GONE);
                } else {
                    assert result.getData() != null;
                    try {
                        audioInput.setSampleFrom(Objects.requireNonNull(result.getData().getData()));
                    }catch (SecurityException e){
                        //get permission
                        askForRequestPermission();
                    }
                    try {
                        mediaPlayer.setDataSource(getApplicationContext(),result.getData().getData());
                        mediaPlayer.prepareAsync();
                        //media player on prepared listener
                        mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                            loadingLayout.setVisibility(View.GONE);
                            audioInput.setMaxProgress(mediaPlayer.getDuration());
                            playButton.setOnClickListener(view -> {
                                if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration())
                                    mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                                new Thread(() -> {
                                    while (mediaPlayer.getCurrentPosition()<mediaPlayer.getDuration()) {
                                        audioInput.setProgress(mediaPlayer.getCurrentPosition());
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                playButton.setVisibility(ImageButton.GONE);
                                pauseButton.setVisibility(ImageButton.VISIBLE);
                            });
                            pauseButton.setOnClickListener(view -> {
                                mediaPlayer.pause();
                                pauseButton.setVisibility(ImageButton.GONE);
                                playButton.setVisibility(ImageButton.VISIBLE);
                            });
                            new Thread(() -> {
                                while (mediaPlayer.getCurrentPosition()<mediaPlayer.getDuration()) {
                                    audioInput.setProgress(mediaPlayer.getCurrentPosition());
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            audioInput.setOnProgressChanged((waveformSeekBar, v, b) -> {
                                if (b) mediaPlayer.seekTo((int) v);
                            });
                            mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                                pauseButton.setVisibility(ImageButton.GONE);
                                playButton.setVisibility(ImageButton.VISIBLE);
                            });
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    isMediaPresent = true;
                    file_type = AUDIO;
                    file_uri = result.getData().getData();
                    audioInputFrame.setVisibility(WaveformSeekBar.VISIBLE);
                    playButton.setVisibility(ImageButton.VISIBLE);
                    pauseButton.setVisibility(ImageButton.GONE);
                    quickNoteFileGrid.setVisibility(GridLayout.GONE);
                }
            });

    ActivityResultLauncher<String> requestPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    // Permission is granted. Continue the action or workflow in your app.
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setTitle("Storage Permission")
                            .setMessage("We need storage permission to access your files to attach them to your journal")
                            .setPositiveButton("OK", (dialogInterface, i) -> askForRequestPermission())
                            .setNegativeButton("Cancel", (dialogInterface, i) -> finish());
                    builder.show();
                }
            });
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        //ask for storage permission
        askForRequestPermission();


        //getting system information
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
        loadingLayout = findViewById(R.id.loadingLayout);
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
        audioInputFrame = findViewById(R.id.audioInputFrame);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);


        //setting up views
        privateButton.performClick();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoInput);
        videoInput.setMediaController(mediaController);


        //hiding media controller on scroll
        rootScrollView.getViewTreeObserver().addOnScrollChangedListener(mediaController::hide);


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
                LinearLayout linearLayout = gridView.getChildAt(j).findViewById(R.id.moodLayout);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.transparent,null));
            }
            LinearLayout linearLayout = gridView.getChildAt(defaultMoods.indexOf(item)).findViewById(R.id.moodLayout);
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
        imageInputLayout.setOnClickListener(view -> pickImage.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));


        //setting up video input
        videoInputLayout.setOnClickListener(view -> pickVideo.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                .build()));


        //setting up audio input
        audioInputLayout.setOnClickListener(view -> pickAudio.launch(new Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)));


        //Managing Journal Input
        addButton.setOnClickListener(view -> {
            loadingLayout.setVisibility(View.VISIBLE);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            String UID = firebaseUser.getUid();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collRef = db.collection("users").document(UID).collection("journals");
            if(SelectedItem==null){
                Toast.makeText(getApplicationContext(), "Select A Mood", Toast.LENGTH_SHORT).show();
                loadingLayout.setVisibility(View.GONE);
            } else if (Objects.requireNonNull(titleInput.getText()).toString().isEmpty()||titleInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Title", Toast.LENGTH_SHORT).show();
                loadingLayout.setVisibility(View.GONE);
            } else if (Objects.requireNonNull(contentInput.getText()).toString().isEmpty()||contentInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Note", Toast.LENGTH_SHORT).show();
                loadingLayout.setVisibility(View.GONE);
            } else if (selectedEmotions.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Select Emotion tags", Toast.LENGTH_SHORT).show();
                loadingLayout.setVisibility(View.GONE);
            } else if (Objects.requireNonNull(categoryInput.getText()).toString().isEmpty()||categoryInput.getText().toString().equals(" ")) {
                Toast.makeText(getApplicationContext(), "Enter Category", Toast.LENGTH_SHORT).show();
                loadingLayout.setVisibility(View.GONE);
            } else {
                if (isMediaPresent) {
                    Log.d("TAG", "onCreate: "+file_uri);
                    StorageReference storageRef = storage.getReference().child("Files").child(UID);
                    storageRef.putFile(file_uri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Journal journal = new Journal.Builder()
                                .setTitle(Objects.requireNonNull(titleInput.getText()).toString())
                                .setContent(Objects.requireNonNull(contentInput.getText()).toString())
                                .setEmotion_tags(selectedEmotions)
                                .setCategory(Objects.requireNonNull(categoryInput.getText()).toString())
                                .setIs_private(privacyToggleGroup.getCheckedButtonId() == R.id.publicButton)
                                .setDate(dateInput.getText().toString())
                                .setTime(timeInput.getText().toString())
                                .setIs_media_present(isMediaPresent)
                                .setMoodLevel(defaultMoods.get(SelectedItem).getLevel())
                                .setMedia(new Media(file_type, uri.toString()))
                                .setOwner_uid(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .build();
                        collRef.document(journal.getTitle()).set(journal).addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(), "Journal Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Journal Addition Failed", Toast.LENGTH_SHORT).show());
                    })).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show());
                } else {
                    Journal journal = new Journal.Builder()
                            .setTitle(Objects.requireNonNull(titleInput.getText()).toString())
                            .setContent(Objects.requireNonNull(contentInput.getText()).toString())
                            .setEmotion_tags(selectedEmotions)
                            .setCategory(Objects.requireNonNull(categoryInput.getText()).toString())
                            .setIs_private(privacyToggleGroup.getCheckedButtonId() == R.id.publicButton)
                            .setDate(dateInput.getText().toString())
                            .setTime(timeInput.getText().toString())
                            .setIs_media_present(isMediaPresent)
                            .setMedia(null)
                            .setMoodLevel(defaultMoods.get(SelectedItem).getLevel())
                            .setOwner_uid(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .build();
                    collRef.document(journal.getTitle()).set(journal).addOnSuccessListener(aVoid -> {Toast.makeText(getApplicationContext(), "Journal Added", Toast.LENGTH_SHORT).show();finish();}).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Journal Addition Failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer = new MediaPlayer();
    }

    public void askForRequestPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // You can use the API that requires the permission.
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Storage Permission")
                    .setMessage("We need storage permission to access your files to attach them to your journal")
                    .setPositiveButton("OK", (dialogInterface, i) -> requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> finish());
            builder.show();
        } else {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
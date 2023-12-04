package net.kalp.reflectr.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.QuerySnapshot;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.masoudss.lib.WaveformSeekBar;

import net.kalp.reflectr.R;
import net.kalp.reflectr.models.Journal;
import net.kalp.reflectr.models.Mood;
import net.kalp.reflectr.module.DayContainer;
import net.kalp.reflectr.module.MonthViewContainer;
import net.kalp.reflectr.module.MonthYearPickerDialog;
import net.kalp.reflectr.setup.DefaultMoods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kotlin.Unit;

public class JournalsHomeAdapter extends RecyclerView.Adapter{
    private Context context;
    private FragmentManager fragmentManager;
    private List<Journal> journals;
    public JournalsHomeAdapter(Context context,FragmentManager fragmentManager, List<Journal> journals){
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.journals = journals;
    }

    public void setJournals(List<Journal> journals){
        this.journals = journals;
        notifyDataSetChanged();
    }
    public void clear(){
        journals.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_view_extracted, parent, false);
            return new CalendarViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_list_item, parent, false);
            return new JournalsHomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position!=0){
            Journal journal = journals.get(position-1);
            JournalsHomeViewHolder journalsHomeViewHolder = (JournalsHomeViewHolder) holder;
            journalsHomeViewHolder.journalTitle.setText(journal.getTitle());
            journalsHomeViewHolder.journalContent.setText(journal.getContent());
            journalsHomeViewHolder.journalDate.setText(journal.getDate()+" "+journal.getTime());
            Mood[] moods = DefaultMoods.moods;
            for (Mood mood : moods) {
                if (mood.getLevel().equals(journal.getMoodLevel())) {
                    journalsHomeViewHolder.moodImage.setImageResource(mood.getEmoji());
                }
            }
            for (String emotion : journal.getEmotion_tags()) {
                Chip chip = new Chip(context);
                chip.setText(emotion);
                chip.setChipBackgroundColorResource(com.google.android.material.R.color.material_dynamic_primary90);
                chip.setTextColor(context.getResources().getColor(com.fredporciuncula.phonemoji.R.color.material_dynamic_primary10));
                journalsHomeViewHolder.emotionChipGroup.addView(chip);
            }
        }else {
            CalendarViewHolder calendarViewHolder = (CalendarViewHolder) holder;
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            calendarViewHolder.calendarView.setDayBinder(new MonthDayBinder<DayContainer>() {
                @Override
                public void bind(@NonNull DayContainer container, CalendarDay calendarDay) {
                    container.textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
                    String date = String.valueOf(calendarDay.getDate().getDayOfMonth());
                    String month = String.valueOf(calendarDay.getDate().getMonth());
                    String year = String.valueOf(calendarDay.getDate().getYear());
                    String fullDate = date+" "+month+" "+year;
                    int sum =0;
                    int count =0;
                    for (Journal journal : journals) {
                        try {
                            if (sdf.parse(journal.getDate()).equals(sdf.parse(fullDate))) {
                                sum += journal.getMoodLevel();
                                count++;
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int avgMoodLevel =0;
                    if(count!=0){
                        avgMoodLevel = Math.round(sum/count);
                    }
                    container.imageView.setImageResource(R.drawable.emoticon_placeholder);
                    for(Mood mood : DefaultMoods.moods){
                        if(mood.getLevel()==avgMoodLevel){
                            container.imageView.setImageResource(mood.getEmoji());
                        }
                    }
                    // if the dates are in the current month then set the text color to black
                    if(calendarDay.getPosition()== DayPosition.MonthDate){
                        container.textView.setTextColor(context.getResources().getColor(R.color.black));
                    }
                    else{
                        container.textView.setTextColor(context.getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral80));
                    }
                }

                @NonNull
                @Override
                public DayContainer create(@NonNull View view) {
                    return new DayContainer(view);
                }

            });
        }
    }

    @Override
    public int getItemViewType(final int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
           return journals.size()+1;
    }
    public class JournalsHomeViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView journalImageView;
        MaterialCardView journalVideoFrame;
        TextView journalTitle, journalDate, journalContent;
        ImageView moodImage;
        ChipGroup emotionChipGroup;
        LinearLayout audioFrame,journalDateLayout;
        ImageButton playAudioButton, pauseAudioButton;
        WaveformSeekBar waveformSeekBar;

        public JournalsHomeViewHolder(@NonNull View viewItem) {
            super(viewItem);
            journalImageView = viewItem.findViewById(R.id.journalImageView);
            journalVideoFrame = viewItem.findViewById(R.id.journalVideoFrame);
            journalTitle = viewItem.findViewById(R.id.journalTitleTextView);
            journalDate = viewItem.findViewById(R.id.journalDateTextView);
            journalContent = viewItem.findViewById(R.id.journalContentTextView);
            moodImage = viewItem.findViewById(R.id.moodImage);
            emotionChipGroup = viewItem.findViewById(R.id.emotionChipGroup);
            audioFrame = viewItem.findViewById(R.id.audioInputFrame);
            journalDateLayout = viewItem.findViewById(R.id.journalDateLayout);
            playAudioButton = viewItem.findViewById(R.id.playButton);
            pauseAudioButton = viewItem.findViewById(R.id.pauseButton);
            waveformSeekBar = viewItem.findViewById(R.id.audioInput);

        }
    }
    public class CalendarViewHolder extends RecyclerView.ViewHolder{

        CalendarView calendarView;
        public CalendarViewHolder(@NonNull View viewItem) {
            super(viewItem);
            calendarView = viewItem.findViewById(R.id.calendarView);

            //setting up the calendar view
            YearMonth currentMonth = YearMonth.now();
            YearMonth firstMonth = YearMonth.of(2020, 11);
            DayOfWeek[] daysOfWeek = DayOfWeek.values();
            DayOfWeek firstDayOfWeek = DayOfWeek.of(Calendar.getInstance().getFirstDayOfWeek());

                    // setting up the month header
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @SuppressLint("DefaultLocale")
            @Override
           public void bind(@NonNull MonthViewContainer container, CalendarMonth calendarMonth) {
                container.monthTitle.setText(String.format("%s-%d", calendarMonth.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()), calendarMonth.getYearMonth().getYear()));
                int childCount = container.dayLabels.getChildCount();
               for (int i = 0; i < childCount; i++) {
                   ((TextView) container.dayLabels.getChildAt(i)).setText(daysOfWeek[i].getDisplayName(TextStyle.SHORT, Locale.getDefault()));
                }
               container.monthTitle.setOnClickListener(v -> {
                    MonthYearPickerDialog pd = new MonthYearPickerDialog();
                    pd.setListener((date ,year,month,day) -> {
                        calendarView.scrollToMonth(YearMonth.of(year, month));
                    });
                    pd.show(fragmentManager, "MonthYearPickerDialog");
                });

            }
            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }
        });
            calendarView.setup(firstMonth, currentMonth, firstDayOfWeek);
            calendarView.scrollToMonth(currentMonth);
            calendarView.setMonthScrollListener(calendarMonth -> Unit.INSTANCE);
        }
    }
}

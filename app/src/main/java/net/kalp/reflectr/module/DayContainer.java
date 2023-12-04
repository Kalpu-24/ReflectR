package net.kalp.reflectr.module;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendar.view.ViewContainer;

import net.kalp.reflectr.R;

public class DayContainer extends ViewContainer {
    public TextView textView;
    public ImageView imageView;
    public DayContainer(@NonNull View view) {
        super(view);
        textView = view.findViewById(R.id.calendarDayText);
        imageView = view.findViewById(R.id.calendarDayImage);
    }
}

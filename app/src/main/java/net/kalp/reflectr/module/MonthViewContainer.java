package net.kalp.reflectr.module;

import android.app.FragmentManager;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DayViewDecorator;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.kizitonwose.calendar.view.ViewContainer;

import net.kalp.reflectr.R;
import net.kalp.reflectr.logineduser.HomeActivity;
import net.kalp.reflectr.logineduser.HomeFragemnts.JournalsFragment;

public class MonthViewContainer extends ViewContainer {
    public LinearLayout dayLabels;
    public TextView monthTitle;

    public MonthViewContainer(@NonNull View view){
        super(view);
        dayLabels = view.findViewById(R.id.dayTitlesContainer);
        monthTitle = view.findViewById(R.id.monthTextView);
    }
}

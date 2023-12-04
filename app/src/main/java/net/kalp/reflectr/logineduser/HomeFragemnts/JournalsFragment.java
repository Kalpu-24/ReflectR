package net.kalp.reflectr.logineduser.HomeFragemnts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;

import net.kalp.reflectr.R;
import net.kalp.reflectr.adapters.JournalsHomeAdapter;
import net.kalp.reflectr.logineduser.AddActivity;
import net.kalp.reflectr.models.Journal;
import net.kalp.reflectr.module.DayContainer;
import net.kalp.reflectr.module.MonthViewContainer;
import net.kalp.reflectr.module.MonthYearPickerDialog;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;

public class JournalsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseUser firebaseuser;
    FirebaseFirestore db;
    String uid;
    MaterialToolbar topappbar;
    CalendarView calendarView;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView journalrecyclerview;
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentManager fragmentManager;
    JournalsHomeAdapter journalsHomeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journals, container, false);


        // initializing views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        journalrecyclerview = view.findViewById(R.id.journal_recycler_view);
        topappbar = view.findViewById(R.id.topappbar);

        // setting up fragment manager
        fragmentManager = requireActivity().getSupportFragmentManager();

        // setting up recycler view
        RecyclerView.LayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(requireContext());
        journalrecyclerview.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);
        fetchJournals();



        // setting up top app bar
        topappbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add){
                startActivity(new Intent(requireActivity().getApplicationContext(), AddActivity.class));
                return true;
            }
            return false;
        });

        return view;
    }

    @Override
    public void onRefresh() {
        fetchJournals();
    }


    // fetching the journals from the database
    private void fetchJournals() {
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseuser.getUid();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.collection("journals").get().addOnSuccessListener(queryDocumentSnapshots -> {


            // setting up shimmer effect
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            journalrecyclerview.setVisibility(View.VISIBLE);


            // getting journals
            List<Journal> journals = queryDocumentSnapshots.toObjects(Journal.class);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());
            // sorting the journals according to the date and time, latest first
            journals.sort((o1, o2) -> {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getDate()+" "+o2.getTime())).compareTo(simpleDateFormat.parse(o1.getDate()+" "+o1.getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            });

            // setting up adapter
            journalsHomeAdapter = new JournalsHomeAdapter(requireContext(), fragmentManager, journals);
            journalrecyclerview.setAdapter(journalsHomeAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}
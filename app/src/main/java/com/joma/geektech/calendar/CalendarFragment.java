package com.joma.geektech.calendar;

import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view){
        adapter = new EventAdapter();
        calendarView = view.findViewById(R.id.calendar_calendar);
        recyclerView = view.findViewById(R.id.calendar_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener(){
        String date = (String) DateFormat.format("dd.M.yyyy", new Date());
        Log.e("-------------", date);
        FirebaseFirestore.getInstance().collection("Calendar").document(date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null) {
                    if (task.getResult().exists()) {
                        Event event = task.getResult().toObject(Event.class);
                        if (event != null)
                            adapter.setList(event.getEvents());
                    } else {
                        adapter.clear();
                    }
                }
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                Log.e("------------=", d+"."+m+"."+y);
                FirebaseFirestore.getInstance().collection("Calendar").document(d+"."+m+"."+y).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult()!=null) {
                            if (task.getResult().exists()) {
                                Event event = task.getResult().toObject(Event.class);
                                if (event != null)
                                    adapter.setList(event.getEvents());
                            } else {
                                adapter.clear();
                            }
                        }
                    }
                });
            }
        });
    }
}

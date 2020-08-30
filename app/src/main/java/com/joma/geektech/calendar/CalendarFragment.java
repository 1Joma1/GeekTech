package com.joma.geektech.calendar;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.Event;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private EditText event;
    private Button save;

    private String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        initView(view);
        listener();
        return view;
    }

    private void initView(View view) {
        adapter = new EventAdapter();
        calendarView = view.findViewById(R.id.calendar_calendar);
        event = view.findViewById(R.id.calendar_event);
        save = view.findViewById(R.id.calendar_save);
        recyclerView = view.findViewById(R.id.calendar_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        User user = Utils.getUser(getContext());
        if (user.isAdmin()){
            event.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
        }
        date = (String) DateFormat.format("dd.M.yyyy", new Date());
        getCalendar();
        calendarView.setOnDateChangeListener((calendarView, y, m, d) -> {
            date = d + "." + m + "." + y;
            getCalendar();
        });
        save.setOnClickListener(view -> {
            if (Utils.getText(event).length() < 1) {
                event.setError(getResources().getString(R.string.must_fill));
            } else {
                List<String> list = adapter.getList();
                list.add(Utils.getText(event));
                Map<String, Object> map = new HashMap<>();
                map.put("events", list);
                FirebaseFirestore.getInstance().collection("Calendar").document(date).set(map);
                event.setText("");
                getCalendar();
            }
        });
    }

    private void getCalendar(){
        FirebaseFirestore.getInstance().collection("Calendar").document(date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    if (task.getResult().exists()) {
                        Event event = task.getResult().toObject(Event.class);
                        if (event != null)
                            adapter.setList(event.getEvents(), date);
                    } else {
                        adapter.clear();
                    }
                }
            }
        });
    }
}

package com.example.eventsphere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eventsphere.entity.DatabaseHelper;
import com.example.eventsphere.entity.Event;
import com.example.eventsphere.entity.EventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewEvents);
        eventList = new ArrayList<>();

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);

        // Load events from the database
        loadEvents();
    }

    private void loadEvents() {
        eventList.clear();
        eventList.addAll(databaseHelper.getAllEvents()); // Use your method to fetch all events
        eventAdapter.notifyDataSetChanged();
    }
}

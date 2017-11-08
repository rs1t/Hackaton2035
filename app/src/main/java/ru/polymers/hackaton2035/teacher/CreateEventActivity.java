package ru.polymers.hackaton2035.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.polymers.hackaton2035.EventListAdapter;
import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.UserGreetingActivity;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;
import ru.polymers.hackaton2035.student.ChooseEventActivity;

public class CreateEventActivity extends AppCompatActivity {

    BackendInterface backend = new MainBackend(new FrontendInterface() {

        @Override
        public void setEventNames(BackendInterface.Event[] events) {
            CreateEventActivity.this.events = Arrays.asList(events);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void setEvent(BackendInterface.Event event) {
            CreateEventActivity.this.events.add(event);
            adapter.notifyDataSetChanged();
        }
    }, "");

    private List<BackendInterface.Event> events;
    private EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        if (!PreferenceManager.getDefaultSharedPreferences(this)
                .contains("student")) {
            startActivity(new Intent(this, UserGreetingActivity.class));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        events = new ArrayList<>();
        ListView eventListView = findViewById(R.id.events_list_view);
        adapter = new EventListAdapter(
                this,
                R.layout.item_event,
                events);
        eventListView.setAdapter(adapter);
        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            BackendInterface.Event event = adapter.getItem(position);
            startActivity(new Intent(this, TeacherEventActivity.class));
        });

        FloatingActionButton fab = findViewById(R.id.create_event_fab);
        fab.setOnClickListener(view -> startActivityForResult(
                new Intent(this, CreateEventFormActivity.class), 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        BackendInterface.Event newEvent = new BackendInterface.Event(
                extras.getString("event_name"),
                extras.getString("lecturer_name"),
                extras.getString("date_and_time"),
                BackendInterface.Event.getId());
        events.add(newEvent);
        try {
            backend.sendEvent(newEvent);
        } catch (IOException e) {
            Toast.makeText(this, "Problems with connection :(", Toast.LENGTH_LONG).show();
            Log.e("Connection error", e.toString());
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("student", true)) {
            startActivity(new Intent(this, ChooseEventActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_profile:
                startActivity(new Intent(this, UserGreetingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

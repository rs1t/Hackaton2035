package ru.polymers.hackaton2035.student;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.polymers.hackaton2035.EventListAdapter;
import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.UserGreetingActivity;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;
import ru.polymers.hackaton2035.teacher.CreateEventActivity;

public class ChooseEventActivity extends AppCompatActivity {
    
    BackendInterface backend = new MainBackend(new FrontendInterface() {
        
        @Override
        public void setEventNames(BackendInterface.Event[] events) {
            ChooseEventActivity.this.events = Arrays.asList(events);
            
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
        
        @Override
        public void setEvent(BackendInterface.Event event) {
            ChooseEventActivity.this.events.add(event);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
    }, "");
    
    private List<BackendInterface.Event> events;
    private EventListAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event);
        
        if (!PreferenceManager.getDefaultSharedPreferences(this)
                .contains("student")) {
            startActivity(new Intent(this, UserGreetingActivity.class));
        }
        
        events = new ArrayList<>();
        ListView eventListView = findViewById(R.id.events_list_view);
        adapter = new EventListAdapter(
                this,
                R.layout.item_event,
                events);
        eventListView.setAdapter(adapter);
        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            BackendInterface.Event event = adapter.getItem(position);
            startActivity(new Intent(this, StudentEventActivity.class)); //maybe put some extra
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (!PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("student", true)) {
            startActivity(new Intent(this, CreateEventActivity.class));
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
            case R.id.update_events:
                backend.getEventNames(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

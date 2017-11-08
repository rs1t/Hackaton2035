package ru.polymers.hackaton2035.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.UserGreetingActivity;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.student.ChooseEventActivity;

public class CreateEventActivity extends AppCompatActivity {
    
    private ArrayList<BackendInterface.Event> events;
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
            //TODO
        });
    
        FloatingActionButton fab = findViewById(R.id.create_event_fab);
        fab.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, CreateEventFormActivity.class), 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        Bundle extras = data.getExtras();
        events.add(new BackendInterface.Event(
                extras.getString("event_name"),
                extras.getString("teacher_name"),
                extras.getString("date_and_time"),
                BackendInterface.Event.getId()));
        adapter.notifyDataSetChanged();
    }
    
    private class EventListAdapter extends ArrayAdapter<BackendInterface.Event> {
        Context context;
        
        public EventListAdapter(@NonNull Context context, int resource, @NonNull List<BackendInterface.Event> events) {
            super(context, resource, events);
            this.context = context;
        }
    
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            
            View listItem = convertView;
            
            if (listItem == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                listItem = layoutInflater.inflate(R.layout.item_event, null);
            }
    
            TextView eventNameTextView = listItem.findViewById(R.id.event_name);
            eventNameTextView.setText(getItem(position).name);
    
            TextView teacherNameTextView = listItem.findViewById(R.id.teacher_name);
            teacherNameTextView.setText(getItem(position).teacher_name);
    
            TextView dateAndTimeTextView = listItem.findViewById(R.id.event_date_and_time);
            dateAndTimeTextView.setText(getItem(position).start_time);
            
            return listItem;
        }
        
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

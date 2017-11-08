package ru.polymers.hackaton2035.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.UserGreetingActivity;
import ru.polymers.hackaton2035.student.ChooseEventActivity;

public class CreateEventActivity extends AppCompatActivity {
    
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
        
        FloatingActionButton fab = findViewById(R.id.create_event_fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        
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

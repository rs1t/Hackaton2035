package ru.polymers.hackaton2035.student;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.UserGreetingActivity;
import ru.polymers.hackaton2035.teacher.CreateEventActivity;

public class ChooseEventActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event);
    
        if (!PreferenceManager.getDefaultSharedPreferences(this)
                .contains("student")) {
            startActivity(new Intent(this, UserGreetingActivity.class));
        }
    
        EditText eventIdEditText = findViewById(R.id.event_id_edit_text);
        eventIdEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                
                return true;
            }
            return false;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

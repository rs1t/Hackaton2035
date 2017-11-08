package ru.polymers.hackaton2035;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class UserGreetingActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_greeting);
        
        findViewById(R.id.next_button).setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.student_radio_button:
                    //start choose event activity
                    //put data in shared prefs
                    break;
                case R.id.teacher_radio_button:
                    //start create event activity
                    //put data in shared prefs
                    break;
            }
        });
    }
}

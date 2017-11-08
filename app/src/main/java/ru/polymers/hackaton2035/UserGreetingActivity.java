package ru.polymers.hackaton2035;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class UserGreetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_greeting);

        findViewById(R.id.next_button).setOnClickListener(view -> {
            switch (((RadioGroup) findViewById(R.id.student_teacher_radio_group)).getCheckedRadioButtonId()) {
                case R.id.student_radio_button:
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit().remove("student").putBoolean("student", true).apply();
                    finish();
                    break;
                case R.id.teacher_radio_button:
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit().remove("student").putBoolean("student", false).apply();
                    finish();
                    break;
            }
        });
    }
}

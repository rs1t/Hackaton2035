package ru.polymers.hackaton2035.teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.polymers.hackaton2035.R;

public class CreateEventFormActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_form);
        
        Intent intent = getIntent();
    
        EditText eventName = findViewById(R.id.event_name_edit_text);
        EditText teacherName = findViewById(R.id.teacher_name_edit_text);
        EditText dateAndTime = findViewById(R.id.date_and_time_edit_text);
        
        eventName.setText("Дискретная математика");
        teacherName.setText("В.Г. Пак");
        dateAndTime.setText("14:00-15:30");
    
        Button addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(v -> {
            intent.putExtra("event_name", eventName.getText().toString());
            intent.putExtra("teacher_name", teacherName.getText().toString());
            intent.putExtra("date_and_time", dateAndTime.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
        
        dateAndTime.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                intent.putExtra("event_name", eventName.getText().toString());
                intent.putExtra("teacher_name", teacherName.getText().toString());
                intent.putExtra("date_and_time", dateAndTime.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            return false;
        });
        
    }
}

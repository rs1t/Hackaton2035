package ru.polymers.hackaton2035.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import ru.polymers.hackaton2035.R;

public class TeacherEventActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_event);
    
        LineChart timeline = findViewById(R.id.timeline);
        //entry - pair of x - time and y - number of actions
        
    }
}

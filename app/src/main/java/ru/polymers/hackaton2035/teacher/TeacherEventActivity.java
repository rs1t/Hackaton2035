package ru.polymers.hackaton2035.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;

import static ru.polymers.hackaton2035.backend.BackendInterface.*;

public class TeacherEventActivity extends AppCompatActivity {
    
    List<Entry> entries;
    LineChart timeline;
    
    private BackendInterface backend = new MainBackend(new FrontendInterface() {
    
        @Override
        public void setEventNames(Event[] events) {
        
        }
    
        @Override
        public void setEvent(Event event) {
        
        }
    
        @Override
        public void setEntry(Entry entry) {
            entries.add(entry);
            timeline.invalidate();
        }
    }, "");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_event);
    
        ImageButton playStopEventButton = findViewById(R.id.play_stop_event_button);
        playStopEventButton.setOnClickListener(v -> {
        
        });

        timeline = findViewById(R.id.timeline);
        //entry - pair of x - time and y - number of actions
    
        entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));
        
        entries.add(new Entry(5f, 2f));
        entries.add(new Entry(10f, 1f));
        entries.add(new Entry(15f, 0f));
        entries.add(new Entry(20f, 5f));
    
        LineDataSet dataSet = new LineDataSet(entries, "Активность");
        LineData lineData = new LineData(dataSet);
        timeline.setData(lineData);
        timeline.invalidate(); // refresh
        
    }
}


package ru.polymers.hackaton2035.student;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.util.ArrayList;
import java.util.List;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;

public class StudentEventActivity extends AppCompatActivity {
    
    List<Entry> entries;
    LineChart timeline;
    
    private BackendInterface backend = new MainBackend(new FrontendInterface() {
        
        @Override
        public void setEventNames(BackendInterface.Event[] events) {}
        @Override
        public void setEvent(BackendInterface.Event event) {}
        
        @Override
        public void setEntry(Entry entry) {
            entries.add(entry);
            timeline.invalidate();
        }
    }, "");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event);
    
        timeline = findViewById(R.id.timeline);
        //entry - pair of x - time and y - number of actions
    
        entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));
    
        entries.add(new Entry(5f, 2f));
        entries.add(new Entry(10f, 1f));
        entries.add(new Entry(15f, 0f));
        entries.add(new Entry(20f, 5f));
        entries.add(new Entry(25f, 4f));
        entries.add(new Entry(30f, 0f));
        entries.add(new Entry(35f, 0f));
    
        LineDataSet dataSet = new LineDataSet(entries, "Активность");
        LineData lineData = new LineData(dataSet);
        
        styleChart(dataSet, lineData);
        
        timeline.setData(lineData);
        timeline.invalidate(); // refresh
    }
    
    public void sendMark(View view) {
//        backend.sendMark(new BackendInterface.Mark());
    }
    
    
    private void styleChart(LineDataSet dataSet, LineData lineData) {
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(15f);
        
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        
        IAxisValueFormatter formatter = (value, axis) -> String.valueOf((int) value);
        IValueFormatter valueFormatter = (value, entry, dataSetIndex, viewPortHandler) -> String.valueOf((int) value);
        lineData.setValueFormatter(valueFormatter);
        
        XAxis xAxis = timeline.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        
        timeline.getAxisLeft().setDrawGridLines(false);
        timeline.getAxisLeft().setEnabled(false);
        
        timeline.getAxisRight().setDrawGridLines(false);
        timeline.getAxisRight().setEnabled(false);
        
        Description description = new Description();
        description.setText("");
        timeline.setDescription(description);
        timeline.setHighlightPerDragEnabled(false);
        timeline.setHighlightPerTapEnabled(false);
        
        timeline.setDrawBorders(false);
        timeline.setGridBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        
        lineData.setValueTextColor(R.color.colorPrimary);
    }
}

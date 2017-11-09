package ru.polymers.hackaton2035.student;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;

import static ru.polymers.hackaton2035.backend.BackendInterface.Event;
import static ru.polymers.hackaton2035.backend.BackendInterface.Graph;

public class StudentEventActivity extends AppCompatActivity {

    List<Entry> entries;
    LineChart timeline;
    static int event_id;

    float x = 0f;

    private BackendInterface backend = new MainBackend(new FrontendInterface() {
        @Override
        public void setEventNames(Event[] events) {
        }

        @Override
        public void setEvent(Event event) {
        }

        @Override
        public void setGraph(Graph graph) {
            updateGraph(graph);
        }

    }, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event);

        event_id = getIntent().getIntExtra("id", 1);

        timeline = findViewById(R.id.timeline);
        //entry - pair of x - time and y - number of actions

        entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));

        LineDataSet dataSet = new LineDataSet(entries, "Активность");
        LineData lineData = new LineData(dataSet);

        styleChart(dataSet, lineData);

        timeline.setData(lineData);
        timeline.invalidate(); // refresh
    }

    public void sendMark(View view) {
        try {
            backend.sendMark(new BackendInterface.Mark(event_id, (int)((Calendar.getInstance()).getTimeInMillis() % 1000), new BackendInterface.Timestamp()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void updateGraph(BackendInterface.Graph graph) {
        runOnUiThread(() -> {

            for (int i = 0; i < graph.t_gap.length; i++) {
                x += 5;
                Entry newEntry = new Entry(graph.t_gap[i], graph.amount[i]);
                entries.add(newEntry);
                LineDataSet dataSet = new LineDataSet(entries, "Активность");
                LineData lineData = new LineData(dataSet);
                styleChart(dataSet, lineData);

                timeline.setData(lineData);
                timeline.notifyDataSetChanged();
                timeline.invalidate();
                timeline.moveViewToX(lineData.getEntryCount() - 1);
            }
        });
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

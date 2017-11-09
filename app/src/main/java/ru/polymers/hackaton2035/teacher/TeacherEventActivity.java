package ru.polymers.hackaton2035.teacher;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import java.util.List;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;

import static ru.polymers.hackaton2035.backend.BackendInterface.*;

public class TeacherEventActivity extends AppCompatActivity {

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
        public void setGraph(Graph[] graph) {
            updateGraph(graph);
        }

    }, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_event);

        event_id = getIntent().getIntExtra("id", 1);

        ImageButton playStopEventButton = findViewById(R.id.play_stop_event_button);
        playStopEventButton.setOnClickListener(v -> {
            backend.graphUpdater(TeacherEventActivity.event_id, 2000);
        });

        timeline = findViewById(R.id.timeline);

        entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));

        LineDataSet dataSet = new LineDataSet(entries, "Активность");

        LineData lineData = new LineData(dataSet);

        styleChart(dataSet, lineData);

        timeline.setData(lineData);
        timeline.invalidate(); // refresh

        ImageView presentation = findViewById(R.id.presentation);
        findViewById(R.id.next_slide_button).setOnClickListener(
                v -> presentation.setImageDrawable(getResources().getDrawable(R.drawable.presentation_sample2)));
        findViewById(R.id.previous_slide_button).setOnClickListener(
                v -> presentation.setImageDrawable(getResources().getDrawable(R.drawable.presentation_sample1)));


        findViewById(R.id.play_stop_event_button).setOnClickListener(v -> {
            try {
                backend.startEvent(event_id);
            } catch (IOException e) {
                Log.e("IOEXCEPTION", "IOException during backend.startEvent()");
            }
        });
    }

    void updateGraph(BackendInterface.Graph[] graph) {
        runOnUiThread(() -> {

            for (int i = 0; i < graph.length; i++) {
                x += 5;
                Entry newEntry = new Entry(graph[i].t_gap, graph[i].amount);
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
        timeline.setTouchEnabled(true);
        timeline.setDragEnabled(true);
        timeline.setScaleEnabled(true);
        timeline.setDrawGridBackground(false);
        timeline.setPinchZoom(true);
        timeline.enableScroll();

        timeline.setVisibleXRangeMaximum(100);
        timeline.getAxisLeft().setDrawGridLines(false);
        timeline.getAxisLeft().setEnabled(false);
        timeline.getAxisRight().setDrawGridLines(false);
        timeline.getAxisRight().setEnabled(false);
        timeline.setHighlightPerDragEnabled(false);
        timeline.setHighlightPerTapEnabled(false);
        timeline.setDrawBorders(false);

        Description description = new Description();
        description.setText("");
        timeline.setDescription(description);

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

        lineData.setValueTextColor(R.color.colorPrimary);
    }
}


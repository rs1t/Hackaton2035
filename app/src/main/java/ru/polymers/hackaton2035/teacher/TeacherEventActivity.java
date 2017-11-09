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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ru.polymers.hackaton2035.R;
import ru.polymers.hackaton2035.backend.BackendInterface;
import ru.polymers.hackaton2035.backend.FrontendInterface;
import ru.polymers.hackaton2035.backend.MainBackend;

import static ru.polymers.hackaton2035.backend.BackendInterface.Event;

public class TeacherEventActivity extends AppCompatActivity {

    List<Entry> entries;
    LineChart timeline;
    int event_id;

    float x = 0f;

    private BackendInterface backend = new MainBackend(new FrontendInterface() {

        @Override
        public void setEventNames(Event[] events) {
        }

        @Override
        public void setEvent(Event event) {
        }
    }, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_event);

        ImageButton playStopEventButton = findViewById(R.id.play_stop_event_button);
        playStopEventButton.setOnClickListener(v -> {
            //start event
        });

        event_id = getIntent().getIntExtra("id", 1);

        timeline = findViewById(R.id.timeline);
        //entry - pair of x - time and y - number of actions

        entries = new ArrayList<>();

        entries.add(new Entry(0f, 0f));
//        entries.add(new Entry(5f, 2f));
//        entries.add(new Entry(10f, 1f));
//        entries.add(new Entry(15f, 0f));
//        entries.add(new Entry(20f, 5f));
//        entries.add(new Entry(25f, 4f));
//        entries.add(new Entry(30f, 0f));
//        entries.add(new Entry(35f, 0f));
//
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

            Timer timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    //put real data here


                    runOnUiThread(() -> {
                        x += 5;
                        Entry newEntry = new Entry(x, new Random().nextInt(6));
                        entries.add(newEntry);
                        LineDataSet dataSet = new LineDataSet(entries, "Активность");
                        LineData lineData = new LineData(dataSet);
                        styleChart(dataSet, lineData);

                        timeline.setData(lineData);
                        timeline.notifyDataSetChanged();
                        timeline.invalidate();
                        timeline.moveViewToX(lineData.getEntryCount() - 1);
                    });


                }
            };

            timer.scheduleAtFixedRate(timerTask, 0, TimeUnit.SECONDS.toMillis(2));
            findViewById(R.id.pause_event_button).setOnClickListener(view -> timer.cancel());
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


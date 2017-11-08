package ru.polymers.hackaton2035;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.polymers.hackaton2035.backend.BackendInterface;

public class EventListAdapter extends ArrayAdapter<BackendInterface.Event> {
    private Context context;

    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<BackendInterface.Event> events) {
        super(context, resource, events);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            listItem = layoutInflater.inflate(R.layout.item_event, null);
        }

        TextView eventNameTextView = listItem.findViewById(R.id.event_name);
        eventNameTextView.setText(getItem(position).name);

        TextView teacherNameTextView = listItem.findViewById(R.id.lecturer_name);
        teacherNameTextView.setText(getItem(position).lecturer_name);

        TextView dateAndTimeTextView = listItem.findViewById(R.id.event_date_and_time);
        dateAndTimeTextView.setText(getItem(position).date_start);

        return listItem;
    }

}

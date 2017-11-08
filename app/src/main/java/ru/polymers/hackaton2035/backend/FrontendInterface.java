package ru.polymers.hackaton2035.backend;

import com.github.mikephil.charting.data.Entry;

public interface FrontendInterface {
    void setEventNames(BackendInterface.Event[] events);

    void setEvent(BackendInterface.Event event);
    
    void setEntry(Entry entry);
}

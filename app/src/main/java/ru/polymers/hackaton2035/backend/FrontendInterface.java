package ru.polymers.hackaton2035.backend;

import static ru.polymers.hackaton2035.backend.BackendInterface.*;

public interface FrontendInterface {
    void setEventNames(Event[] events);

    void setEvent(Event event);

    void setGraph(Graph graph);
}

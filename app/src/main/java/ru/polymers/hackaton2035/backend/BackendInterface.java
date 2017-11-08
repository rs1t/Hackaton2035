package ru.polymers.hackaton2035.backend;

import java.io.IOException;

// Бекенд посылает во фронтенд данные по запросу через FrontendInterface;
public interface BackendInterface {
    void getEventNames(int student_id);

    void sendFeedback(int lecture_id, Feedback feedback) throws IOException;

    void sendEvent(Event event) throws IOException;

    void getEvent(int event_id);

    class Event {
        public Event(int id, String name) {
            this.id = id;
            this.name = name;
        }

        int id;
        String name;
        String teacher_name;
        long start_time, end_time;
        String video_link;
        String[] file_links;
        Timeline timeline;

        boolean hasVideo() {
            return video_link != null && !video_link.equals("");
        }

        boolean hasFiles() {
            return video_link != null && !video_link.equals("");
        }
    }
    
    class Timeline {

    }

    class Timestamp {
        int YYYY, MM, DD, hh, mm, ss;
    }

    class Feedback {
        int id_lect;
        int id_stud;
        int offset;
        int complexity;
        String emoji;
        String comment;
    }
}
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
        String start_time, end_time;
        String video_link;
        String[] file_links;
        Timeline timeline;

        boolean hasVideo() {
            return video_link != null && !video_link.equals("");
        }

        boolean hasFiles() {
            return video_link != null && !video_link.equals("");
        }

        Timestamp getStartTime() {
            return new Timestamp(start_time);
        }

        Timestamp getEndTime() {
            return new Timestamp(end_time);
        }
    }
    
    class Timeline {

    }

    class Timestamp {
        int YYYY, MM, DD, hh, mm, ss;
        Timestamp(String s) {
            char[] chars = s.toCharArray();
            YYYY = 1000 * chars[0] + 100 * chars[1] + 10 * chars[2] + chars[3];
            MM = 10 * chars[5] + chars[6];
            DD = 10 * chars[8] + chars[9];
            hh = 10 * chars[11] + chars[12];
            mm = 10 * chars[14] + chars[15];
            ss = 10 * chars[17] + chars[18];
        }
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
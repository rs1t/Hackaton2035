package ru.polymers.hackaton2035.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

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
    
        public Event(String name, String teacher_name, String start_time, int id) {
            this.id = id;
            this.name = name;
            this.teacher_name = teacher_name;
            this.start_time = new Timestamp(start_time).toString();
            this.end_time = new Timestamp(start_time, 1.5).toString();
        }

        public Event(String json) {
            new Gson().fromJson(json, Event.class);
        }

        String toJson() {
            return new Gson().toJson(this);
        }

        int id;
        public String name;
        public String teacher_name;
        public String start_time, end_time; //HH:MM
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
    
        public static int getId() {
            Calendar cal = Calendar.getInstance();
            return (int) cal.getTimeInMillis() % 1000;
        }
    }
    
    class Timeline {

    }

    class Timestamp {
        int YYYY, MM, DD, hh, mm, ss;
        Timestamp(String s) {
            parse(s);
        }
    
        Timestamp(String s, double v) {
            parse(s);
            mm += (v % 1) * 60;
            hh += (int) v + (mm > 60 ? mm - 60 : 0);
            DD += hh > 24 ? hh - 24 : 0;
        }

        void parse(String s) {
            char[] chars = s.toCharArray();
            if (chars.length == 19) { // YYYY-MM-DD hh-mm-ss
                YYYY = 1000 * chars[0] + 100 * chars[1] + 10 * chars[2] + chars[3];
                MM = 10 * chars[5] + chars[6];
                DD = 10 * chars[8] + chars[9];
                hh = 10 * chars[11] + chars[12];
                mm = 10 * chars[14] + chars[15];
                ss = 10 * chars[17] + chars[18];
            } else if (chars.length == 5) { // hh:mm
                hh = 10 * chars[0] + chars[1];
                mm = 10 * chars[3] + chars[4];
            } else if (chars.length == 4) {
                hh = chars[0];
                mm = 10 * chars[2] + chars[3];
            }
        }

        @Override
        public String toString() {
            return YYYY + "-" + MM + "-" + DD + " " + hh +  ":" + mm + "-" + ss;
        }
    }

    class Feedback {
        int id_lect;
        int id_stud;
        int offset;
        int complexity;
        String emoji;
        String comment;

        public String toJson() {
            return new Gson().toJson(this);
        }
    }
}

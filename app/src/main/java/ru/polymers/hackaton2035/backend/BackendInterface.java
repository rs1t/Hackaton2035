package ru.polymers.hackaton2035.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

// Бекенд посылает во фронтенд данные по запросу через FrontendInterface;
public interface BackendInterface {
    void getEventNames(int student_id);

    void eventsUpdater(int student_id, int interval);

    void sendFeedback(Feedback feedback) throws IOException;

    void sendEvent(Event event) throws IOException;

    void getEvent(int event_id);

    class Event {
        public Event(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Event(String name, String lecturer_name, String date_start, int id) {
            this.id = id;
            this.name = name;
            this.lecturer_name = lecturer_name;
            this.date_start = date_start;
//          this.date_end = new Timestamp(date_start, 1.5).toString();
        }

        public Event(String json) {
            new Gson().fromJson(json, Event.class);
        }

        String toJson() {
            date_start = new Timestamp(date_start).toString();
            date_end = new Timestamp(date_end).toString();
            return new Gson().toJson(this);
        }

        int id;
        public String name;
        public String lecturer_name;
        public String date_start, date_end = null; //HH:MM
        String video_link;
        String[] file_links;
        Timeline[] timeline;

        boolean hasVideo() {
            return video_link != null && !video_link.equals("");
        }

        boolean hasFiles() {
            return video_link != null && !video_link.equals("");
        }

        Timestamp getStartTime() {
            return new Timestamp(date_start);
        }

        Timestamp getEndTime() {
            return new Timestamp(date_end);
        }

        public static int getId() {
            return 1;
        }
    }

    class Timeline {
        int[] flags;
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

        void setDefaultToday() {
            Calendar cal = Calendar.getInstance();
            Date t = cal.getTime();
            YYYY = t.getYear();
            MM = t.getMonth();
            DD = t.getDay();
            hh = t.getHours();
            mm = t.getMinutes();
        }

        void parse(String s) {
            if (s == null) return;

            setDefaultToday();
            char[] chars = s.toCharArray();
            for (int i = 0; i < s.length(); i++)
                chars[i] -= '0';
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
            return (YYYY < 2000 ? 2017 : YYYY) + "-" + (MM > 9 ? "" : "0") + MM + "-" + (DD > 9 ? "" : "0") + DD + " " + (hh > 9 ? "" : "0")
                    + hh + ":" + (mm > 9 ? "" : "0") + mm + ":" + (ss > 9 ? "" : "0") + ss;
        }
    }

    class Feedback {
        int event_id;
        int user_id;
        int offset;
        int complexity;
        String emoji;
        String comment;

        public String toJson() {
            return new Gson().toJson(this);
        }
    }
}

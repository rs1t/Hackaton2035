package ru.polymers.hackaton2035.backend;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;

// Бекенд посылает во фронтенд данные по запросу через FrontendInterface;
public interface BackendInterface {
    void getEventNames(int student_id);

    void eventsUpdater(int student_id, int interval);

    void graphUpdater(int event_id, int interval);

    void sendMark(Mark... marks) throws IOException;

    void sendEvent(Event... events) throws IOException;

    void getEvent(int event_id);

    void getGraph(int event_id);

    void startEvent(int event_id) throws IOException;

    void endEvent(int event_id) throws IOException;

    void deleteEvent(Integer... event_ids);

    void downloadFile(String url, String path, Context context);

    class Event {
        
        int event_id;
        public String name;
        public String teacher_name;
        public String start_time, end_time = null; //HH:MM
        String file_link;
        Timeline[] timeline;

        public Event(String name, String teacher_name, String start_time, int id) {
            this.event_id = event_id;
            this.name = name;
            this.teacher_name = teacher_name;
            this.start_time = start_time;
//          this.date_end = new Timestamp(start_time, 1.5).toString();
        }

        public Event(String json) {
            new Gson().fromJson(json, Event.class);
        }
        
        public int getEvent_id() { return event_id; }
        
        String toJson() {
            start_time = new Timestamp(start_time).toString();
            end_time = new Timestamp(end_time).toString();
            return new Gson().toJson(this);
        }


        boolean hasVideo() {
            return file_link != null && !file_link.equals("");
        }

        Timestamp getStartTime() {
            return new Timestamp(start_time);
        }

        Timestamp getEndTime() {
            return new Timestamp(end_time);
        }
    }

//    class Graph {
//        public int[] t_gap;
//        public int[] amount;
//
//        public Graph(String json) {
//            new Gson().fromJson(json, Event.class);
//        }
//
//        String toJson() {
//            return new Gson().toJson(this);
//        }
//
//    }

    class Graph {
        public int t_gap;
        public int amount;
    }

    class Timeline {
        int[] flags;
    }

    class Timestamp {
        int YYYY, MM, DD, hh, mm, ss;

        public Timestamp() {
            setDefaultToday();
        }

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
            ss = t.getSeconds();
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

        public String toJson() {
            return "\"timestamp\":\"" + toString() + "\"";
        }
    }

    class Mark {
        int event_id;
        int student_id;
        Timestamp time_gap;
        short complexity;
        String comment;
        String emoticon;

        public String toJson2() {
            return new Gson().toJson(this);
        }

        public String toJson() {
            return "{" +
                    "\"event_id\":\"" + event_id +'\"' +
                    ", \"student_id\":\"" + student_id + '\"'+
                    ", \"time_gap\":\"" + time_gap + '\"'+
                    ", \"complexity\":\"" + complexity + '\"' +
                    ", \"comment\":\"" + comment + '\"' +
                    ", \"emoticon\":\"" + emoticon + '\"' +
                    "}";
        }

        public Mark(int event_id, int student_id, Timestamp time_gap) {
            this.event_id = event_id;
            this.student_id = student_id;
            this.time_gap = time_gap;
        }

        public Mark(int event_id, int student_id, Timestamp time_gap, short complexity, String comment, String emoticon) {
            this.event_id = event_id;
            this.student_id = student_id;
            this.time_gap = time_gap;
            this.complexity = complexity;
            this.comment = comment;
            this.emoticon = emoticon;
        }
    
        public Mark(String json) {
            new Gson().fromJson(json, this.getClass());
        }
    }
}

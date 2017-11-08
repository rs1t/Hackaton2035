package ru.polymers.hackaton2035;


public interface BackendInterface {
    String[] getEventNames(int student_id);
    //alert - когда студент нажимает кнопку.
    void requestAlert(int button_code);
    void requestQuestion(String question);
    
    Event getEvent(int event_id);
    
    class Event {
        int id;
        String name;
        String link;
        String video_link;
        String file_link;
        Timeline timeline;
    }
    
    class Timeline {
    
    }
}
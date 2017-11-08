package ru.polymers.hackaton2035.backend;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

import static ru.polymers.hackaton2035.backend.MainBackend.sendDataToUrl;

class MainBackend implements BackendInterface {
    private String Server_IP = "http://192.168.88.214:8000";

    @Override
    public Event[] getEventNames(int student_id) {
        Event[] result;
        String json;
        try {
            json = getDataFromUrl(Server_IP + "/lectures"); ///" + student_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        result = new Gson().fromJson(json, Event[].class);
        return result;
    }

    @Override
    public void sendFeedback(int lecture_id, Feedback feedback) throws IOException {
        sendDataToUrl(Server_IP + "/" + lecture_id + "/add", new Gson().toJson(feedback));
    }

    @Override
    public void sendEvent(Event event) throws IOException {
        sendDataToUrl(Server_IP + "/add", new Gson().toJson(event));
    }

    @Override
    public Event getEvent(int event_id) {
        Event result;
        String json;
        try {
            json = getDataFromUrl(Server_IP + "/lecture/" + event_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        result = new Gson().fromJson(json, Event.class);
        return result;
    }

    private static String getDataFromUrl(String url_s) throws Exception {
        String result = null;

        BufferedReader reader = null;
        URLConnection uc = null;

        try {
            URL url = new URL(url_s);
            uc = url.openConnection();
            uc.setConnectTimeout(1000);
            uc.connect();
            reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            result = buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

        if (result.charAt(0) == '{')
            Log.i("GOT ONLINE DATA!!!", result);
        return result;
    }

    private static void sendDataToUrl(String url_s, String data) throws IOException {
        URL url = new URL(url_s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");


        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("json", data);
        String query = builder.build().getEncodedQuery();

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        conn.connect();
    }
}
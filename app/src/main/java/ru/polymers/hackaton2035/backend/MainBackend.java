package ru.polymers.hackaton2035.backend;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

@SuppressLint("StaticFieldLeak")
public class MainBackend implements BackendInterface {
    public static String Server_Url = "http://192.168.88.214:8000"; // дефолтный
    FrontendInterface fi;

    public MainBackend(FrontendInterface fi, String server_Url) {
        if (!server_Url.equals("")) {
            Server_Url = server_Url;
        }
        this.fi = fi;
    }

    @Override
    public void sendFeedback(Feedback feedback) {
        new AsyncTask<Feedback, Void, Void>() {
            @Override
            protected Void doInBackground(Feedback... feedbacks) {
                try {
                    sendDataToUrl(Server_Url + "/" + feedbacks[0].event_id + "/add", feedbacks[0].toJson());
                    Log.e("Feedback sender", "Successfully sent");
                } catch (IOException e) {
                    Log.e("Feedback sender failed", e.toString());
                }
                return null;
            }
        }.execute(feedback);
    }

    @Override
    public void sendEvent(Event event) {
        new AsyncTask<Event, Void, Void>() {
            @Override
            protected Void doInBackground(Event... events) {
                try {
                    sendDataToUrl(MainBackend.Server_Url +"/lectures/add", events[0].toJson());
                    Log.e("Event sender", "No exceptions caught");
                } catch (IOException e) {
                    Log.e("Event sender failed", e.toString());
                }
                return null;
            }
        }.execute(event);
    }

    @Override
    public void getEventNames(int student_id) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                Event[] result;
                String json;
                try {
                    json = getDataFromUrl(Server_Url + "/lectures"); ///" + student_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                result = new Gson().fromJson(json, Event[].class);

                fi.setEventNames(result);
                return null;
            }
        }.execute(student_id);
    }

    @Override
    public void getEvent(int event_id) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... id) {
                Event result;
                String json;
                try {
                    json = getDataFromUrl(Server_Url + "/lecture/" + id[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                result = new Event(json);
                fi.setEvent(result);
                return null;
            }
        }.execute(event_id);

    }

    private static String getDataFromUrl(String url_s) throws Exception {
        DefaultHttpClient hc = new DefaultHttpClient();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_s);

        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String result = "", line = "";
        while ((line = rd.readLine()) != null) {
            result += line;
        }
        Log.d("POST get", "Возвращено:" + result);

        rd.close();
        return result;
    }

    private static void sendDataToUrl(String url_s, String data) throws IOException {
        DefaultHttpClient hc = new DefaultHttpClient();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url_s);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", data));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String result = "", line = "";
        while ((line = rd.readLine()) != null) {
            result += line;
        }

        Log.d("POST send", "Возвращено:" + result);
        rd.close();

    }
}

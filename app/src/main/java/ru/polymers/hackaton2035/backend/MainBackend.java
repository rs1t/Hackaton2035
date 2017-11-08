package ru.polymers.hackaton2035.backend;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
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
    public void sendMark(Mark... marks) {
        new AsyncTask<Mark, Void, Void>() {
            @Override
            protected Void doInBackground(Mark... marks) {
                try {
                    for (Mark mark : marks) {

                        sendDataToUrl(Server_Url + "/" + mark.event_id + "/add", mark.toJson());
                    }
                    Log.e("Mark sender", "Successfully sent");
                } catch (IOException e) {
                    Log.e("Mark sender failed", e.toString());
                }
                return null;
            }
        }.execute(marks);
    }

    @Override
    public void sendEvent(Event... events) {
        new AsyncTask<Event, Void, Void>() {
            @Override
            protected Void doInBackground(Event... events) {
                try {
                    for (Event event : events) {
                        sendDataToUrl(MainBackend.Server_Url + "/lectures/add", event.toJson());
                    }
                    Log.e("Event sender", "No exceptions caught");
                } catch (IOException e) {
                    Log.e("Event sender failed", e.toString());
                }
                return null;
            }
        }.execute(events);
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
    public void eventsUpdater(int student_id, int interval) {
        CountDownTimer timer = new CountDownTimer(Long.MAX_VALUE, interval) {
            @Override
            public void onTick(long l) {
                getEventNames(student_id);
            }

            @Override
            public void onFinish() {
                start();
            }
        }.start();
    }

    @Override
    public void getEvent(int event_id) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... ids) {
                Event result;
                String json;
                try {
                    for (Integer id : ids) {
                        json = getDataFromUrl(Server_Url + "/lecture/" + id);
                        result = new Event(json);
                        fi.setEvent(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        }.execute(event_id);

    }

    @Override
    public void startEvent(int event_id) throws IOException {
        sendDataToUrl(Server_Url + "/" + event_id + "/m/s", new Timestamp().toString());
    }

    @Override
    public void deleteEvent(Integer... event_ids) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... ids) {
                try {
                    for (int id : ids) {
                        sendDataToUrl(Server_Url + "/lecture/" + id + "/d", null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(event_ids);
    }

    @Override
    public void downloadFile(String url, String path, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Some descrition");
        request.setTitle("Some title");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf('/')));

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
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

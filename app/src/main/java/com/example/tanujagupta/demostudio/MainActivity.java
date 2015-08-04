package com.example.tanujagupta.demostudio;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.MailTo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity implements View.OnClickListener {


    private Context context;
    private static String url = "http://docs.blackberry.com/sampledata.json";

    private static final String type = "vehicleType";
    private static final String color = "vehicleColor";
    private static final String fuel = "fuel";

    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    ListView lv;


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnShowNotification).setOnClickListener(MainActivity.this);
        findViewById(R.id.btnCancelNotification).setOnClickListener(MainActivity.this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        try {
            new ProgressTask(MainActivity.this).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnShowNotification) {
            showNotification();
        } else if (v.getId() == R.id.btnCancelNotification) {
            cancelNotification(0);
        }

    }



    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;

        public ProgressTask(ListActivity activity) {

            Log.i("1", "Called");
            context = MainActivity.this;
            dialog = new ProgressDialog(context);
        }

        private Context context;

        protected void onPreExecute() {
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            ListAdapter adapter = new SimpleAdapter(context, jsonlist, R.layout.list_item, new String[]{type, color, fuel}, new int[]{R.id.vehicleType, R.id.vehicleColor, R.id.fuel});
            setListAdapter(adapter);
            lv = getListView();
        }

        protected Boolean doInBackground(final String... args) {

            JSONParser jParser = new JSONParser();
            String json1 = "";
            JSONArray json = null;

            Log.i("errdd", "getting data" + jParser);
            try {
                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                json1 = jParser.makeHttpRequest(url, "GET", param);
                Log.i("errdd", "getting data" + json1);
                json = new JSONArray(json1);
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (int i = 0; i < json.length(); i++) {

                try {
                    JSONObject c = json.getJSONObject(i);
                    String vtype = c.getString(type);

                    String vcolor = c.getString(color);
                    String vfuel = c.getString(fuel);

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(type, vtype);
                    map.put(color, vcolor);
                    map.put(fuel, vfuel);


                    jsonlist.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {

        // define sound URI, the sound to be played when there's a notification

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)
                .setContentTitle("New Post!")
                .setContentText("Here's an awesome update for you!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .addAction(R.drawable.abc_ic_search_api_mtrl_alpha, "View", pIntent)
                .addAction(0, "Remind", pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mNotification);
    }


    public void cancelNotification(int notificationId) {


        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);

        }

    }


}
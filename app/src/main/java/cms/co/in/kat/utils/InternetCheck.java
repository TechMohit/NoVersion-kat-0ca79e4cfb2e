package cms.co.in.kat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Happy on 14-04-2017.
 */


public class InternetCheck extends AsyncTask<Object, Object, Boolean> {

    private volatile boolean running = true;
    private Context activity;

    public InternetCheck(Context x) {

        activity = x;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        return hasInternetAccess();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        ShowDilog.internet = result;
        EventBus.getDefault().post(result);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean hasInternetAccess() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Internetcheck", "No network available!");
        }
        return false;
    }

}
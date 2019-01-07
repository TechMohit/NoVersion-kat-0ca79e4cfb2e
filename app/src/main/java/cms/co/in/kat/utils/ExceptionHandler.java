package cms.co.in.kat.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.cms.kat.cws.BuildConfig;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cms.co.in.kat.activity.ErrorReport;

/**
 * Created by  subham Kumar Naik and satyam Kumar Naik .
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Activity Contex;

    public ExceptionHandler(Activity Context) {

        this.Contex = Context;

    }

    @SuppressWarnings("deprecation")
    public void uncaughtException(Thread thread, Throwable exception) {

        JSONObject res = new JSONObject();

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        try {
            res.put("app_name", "" + Contex.getApplicationInfo().loadLabel(Contex.getPackageManager()) + "(" + Contex.getPackageName() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int versionCode = BuildConfig.VERSION_CODE;

            res.put("app_version_code", "" + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String versionName = BuildConfig.VERSION_NAME;

            res.put("app_version_name", "" + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            res.put("crash_time", "" + ft.format(dNow));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            res.put("crash_class", "" + Contex.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            res.put("current_tag", "" + Constant.CURRENT_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            res.put("crash", "" + stackTrace.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jobj = new JSONObject();
        try {
            jobj.put("brand", Build.BRAND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("device", Build.DEVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("model", Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("id", Build.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("product", Build.PRODUCT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("sdk", Build.VERSION.SDK_INT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("release", Build.VERSION.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobj.put("incremental", Build.VERSION.INCREMENTAL);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            res.put("device_info", jobj);
        } catch (Exception e) {
            e.printStackTrace();
        }


        MemoryInfo mi = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager) Contex.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        //Percentage can be calculated for API 16+
        long percentAvail = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            percentAvail = mi.availMem / mi.totalMem;
        }

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        JSONObject jobjres = new JSONObject();
        try {
            jobjres.put("avialable", "" + availableMegs + "mb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobjres.put("allocate", "" + megAvailable + "mb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jobjres.put("percentage", "" + percentAvail + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            res.put("device_memory", jobjres);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        writeinsd(res.toString());

        Log.e("**","**"+res.toString());
        Intent intent = new Intent(Contex, ErrorReport.class);
        intent.putExtra("error", res.toString());
        Contex.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
        Contex.finish();
    }

    private void writeinsd(String errorReport) {
        File root = Environment.getExternalStorageDirectory();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        String str = "Androidcrash" + date + ".txt";
        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, str);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(errorReport);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




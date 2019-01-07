package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.CaseInfo;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

public class ErrorReport extends AppCompatActivity {

    private URLConstants urlConst = new URLConstants();
    private Volley vl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        Intent intent = getIntent();
        String error = intent.getStringExtra("error");
        HashMap<String, String> params = new HashMap<>();
        params.put("log", error);
        Log.e("***",""+error);

        volly(urlConst.CRASH_LOG_TAG, Request.Method.POST, urlConst.CRASH_LOG_URL, params);

    }
    private void volly(final String tag, int post, String link, HashMap<String, String> params) {

        vl = new Volley(ErrorReport.this);

        vl.makeStringReq(tag, post, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, @NonNull String response) {
                Log.d(urlConst.CRASH_LOG_TAG, "result code " + response);

                if ((tag == urlConst.CRASH_LOG_TAG)) {
                    Log.d(urlConst.CRASH_LOG_TAG, "result code " + response);
//                    Toast.makeText(ErrorReport.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (result == 1) {

                    } else {

                    }
                    getHome();
                }
            }


            @Override
            public void onVolleyError(String error) {

                Log.d(urlConst.CRASH_LOG_TAG, "" + error);
                getHome();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

      getHome();
    }

    private void getHome() {
        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String name = prefs.getString(Constant.MY_PREF_USERNAME, null);
        Intent i;
        if (name != null) {
            i = new Intent(getApplicationContext(), LoginHome.class);
        } else {
            i = new Intent(getApplicationContext(), GuestHome.class);
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);
    }
}

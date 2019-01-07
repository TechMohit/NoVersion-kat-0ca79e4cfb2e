package cms.co.in.kat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.AVLoadingIndicatorView;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

/**
 * Created by Happy on 14-04-2017.
 */

public class SplashScreen extends Activity {

    // Splash screen timer
    private int SPLASH_TIME_OUT = 1500;
    @NonNull
    private URLConstants urlConst = new URLConstants();
    private Volley vl;
    private ImageView logo;
    private String android_id = "";
    private SharedPreferences sharepref;
    private TextView txt_header;
    private Typeface face;
    private RelativeLayout relInernet;
    private DatabaseHandler db;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharepref = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        db = new DatabaseHandler(SplashScreen.this);
        txt_header = (TextView) findViewById(R.id.txt_header);
        logo = (ImageView) findViewById(R.id.logo);

        relInernet = (RelativeLayout) findViewById(R.id.rel_inernet);
        new InternetCheck(SplashScreen.this).execute();

        try {
            db.deleteTable(SplashScreen.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnRetry=(Button)findViewById(R.id.btn_retry);

        face = Typeface.createFromAsset(getAssets(), Constant.FONT_MEDIUM);
        txt_header.setTypeface(face);
        checkAuthentication();
    }

    private void checkAuthentication() {
        Log.d("test","checkAuthentication");

        String name = sharepref.getString(Constant.MY_PREF_USERNAME, null);
        String pass = sharepref.getString(Constant.MY_PREF_PASSWORD, null);
        relInernet.setVisibility(View.GONE);

        if (name != null) {
            if (pass != null) {
                HashMap<String, String> params = new HashMap<>();
                params.put("login", name);
                params.put("password", pass);
                Constant.CURRENT_TAG = urlConst.LOGIN_TAG;
                volly(Constant.CURRENT_TAG, Request.Method.POST, urlConst.LOGIN_URL, params);

            } else {
                showSplash();
            }
        } else {
            showSplash();
        }
    }

    private void showSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityLogin();
            }
        }, SPLASH_TIME_OUT);
    }


    private void login(int result, String response) {
        Log.d(urlConst.LOGIN_TAG, "result code " + response);
        if (result == 1) {

            startActivityHome();

        } else {

            startActivityLogin();
        }
    }

    private void startActivityHome() {
        Intent i = new Intent(SplashScreen.this, LoginHome.class);
        startActivity(i);
    }

    private void startActivityLogin() {
        if (getValueFromSharepreference()) {
            Log.d("onboard","Test1");
            Intent i = new Intent(SplashScreen.this, Login.class);
            startActivity(i);
        } else {
            Log.d("onboard","Test");
            startActivity(new Intent(SplashScreen.this, OnBoardingActivity.class));

        }
        finish();
    }

    public void onRetryConnectivity(View v){
        checkAuthentication();
    }

    private boolean getValueFromSharepreference() {

        if (sharepref.getBoolean(Constant.ONBOARDING_FLAG, false)) {
            Log.d("onboard","Test2");
            return true;
        }
        return false;
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        vl = new Volley(this);
        vl.showdilog(false);
        vl.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, String response) {

                if (tag == urlConst.LOGIN_TAG) {
                    login(result, response);
                }
            }
            @Override
            public void onVolleyError(String result) {
                relInernet.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
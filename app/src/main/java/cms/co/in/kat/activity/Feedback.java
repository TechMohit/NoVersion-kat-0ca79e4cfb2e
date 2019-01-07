package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class Feedback extends AppCompatActivity implements View.OnClickListener {

    private Button btnSubmit;
    private LinearLayout parentLayout;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private EditText txtName, comments;
    private RatingBar ratingBar;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private String androidId = "", didFind = "0", recommend = "0";
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private RadioButton radio1, radio2;
    private String userEmail = "", userPhone = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getdata();
        initLayout();
        initListner();
        footerListner();

        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private void getdata() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            userEmail = (String) b.get("email");
            userPhone = (String) b.get("phone");
            if (userPhone.isEmpty()) {
                userPhone = "0";
            }
        }
    }

    private void initLayout() {
        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        parentLayout = (LinearLayout) findViewById(R.id.parent_layout);

        txtName = (EditText) findViewById(R.id.name);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        comments = (EditText) findViewById(R.id.comments);

        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox4);

        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);

    }

    private void initListner() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        if (ShowDilog.internet) {

            switch (v.getId()) {
                case R.id.btn_submit:

                    if (txtName.getText().toString().isEmpty()) {
                        GeneralUtilities.showMessage(Feedback.this, parentLayout, "Name cannot be Empty");
                    } else if (ratingBar.getRating() == 0.0) {
                        GeneralUtilities.showMessage(Feedback.this, parentLayout, "Please Rate our Application ");
                    } else if (comments.getText().toString().isEmpty() &&
                            (!radio1.isChecked() && !radio2.isChecked()) &&
                            (!checkBox1.isChecked() && (!checkBox2.isChecked()) && !checkBox3.isChecked() && !checkBox4.isChecked())) {
                        GeneralUtilities.showMessage(Feedback.this, parentLayout, "Please select one Feedback");

                    } else {
                        if (radio1.isChecked()) {
                            recommend = "1";
                        }
                        if (radio2.isChecked()) {
                            recommend = "2";
                        }
                        String versionName = "", versionCode = "";
                        try {

                            versionName = GeneralUtilities.getVersionName(Feedback.this);
                            versionCode = "" + GeneralUtilities.getVersionCode(Feedback.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HashMap<String, String> params = new HashMap<>();
                        params.put("name", txtName.getText().toString());
                        params.put("mobileRating", String.valueOf(ratingBar.getRating()));
                        params.put("didFind", didFind);
                        params.put("comment", comments.getText().toString());
                        params.put("recommend", recommend);
                        params.put("systemType", "" + Constant.FEEDBACK_TYPE);
                        params.put("email", userEmail);
                        params.put("mobileNumber", userPhone);
                        params.put("androidId", androidId);

                        params.put("versionName", versionName);
                        params.put("versionCode", versionCode);

                        Log.e("*********", "*******" + params);
                        Constant.CURRENT_TAG = urlConstants.FEEDBACK_TAG;
                        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.FEEDBACK_URL, params);
                    }
                    break;
            }
        } else {
            GeneralUtilities.showMessage(Feedback.this, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(Feedback.this).execute();
        }
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        Volley vl = new Volley(this);

        vl.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, @NonNull String response) {
                if (tag == urlConstants.FEEDBACK_TAG) {
                    if (result == 1) {
                        GeneralUtilities.showMessage(Feedback.this, parentLayout, "Thank you for your Feedback ");
                        radio1.setChecked(false);
                        radio2.setChecked(false);
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        txtName.setText("");
                        comments.setText("");
                        ratingBar.setRating(0F);

                    } else {
                        GeneralUtilities.showMessage(Feedback.this, parentLayout, response);
                    }
                } else {
                    GeneralUtilities.showMessage(Feedback.this, parentLayout, " Please try again");
                }
            }

            @Override
            public void onVolleyError(String result) {

                GeneralUtilities.showMessage(Feedback.this, parentLayout, " Please try again");
            }
        });
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {

            case R.id.checkbox1:

                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                didFind = "1";
                break;

            case R.id.checkbox2:

                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                didFind = "2";
                break;
            case R.id.checkbox3:

                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                didFind = "3";
                break;
            case R.id.checkbox4:

                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                checkBox2.setChecked(false);
                didFind = "4";
                break;
        }
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Feedback.this, CaseTrack.class);
                startActivity(i);
            }
        });

        footerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        footerJudgment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Feedback.this, Judgement.class);
                startActivity(i);
            }
        });
    }

}

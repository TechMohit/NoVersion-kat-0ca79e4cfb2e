package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class ChangePassword extends AppCompatActivity {

    private Button submit;
    private String userid = "", currentPass, newPass, conformPass;
    private EditText etCurrent, etNew, etConform;
    private LinearLayout parentLayout;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private TextInputLayout et_lay_current, et_lay_new, et_lay_conform;
    boolean passMismatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initLayout();
        initListner();

        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        userid = prefs.getString(Constant.MY_PREF_USERID, null);

    }

    private void initListner() {
        etCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                // this will show characters remaining
                et_lay_current.setError(null);
            }
        });
        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                et_lay_new.setError(null);
                if (passMismatch) {
                    et_lay_conform.setError(null);
                }
            }
        });
        etConform.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                et_lay_conform.setError(null);
                if (passMismatch) {
                    et_lay_new.setError(null);
                }
            }
        });
    }

    private void initLayout() {
        etCurrent = (EditText) findViewById(R.id.et_current);
        etNew = (EditText) findViewById(R.id.et_new);
        etConform = (EditText) findViewById(R.id.et_conform);
        submit = (Button) findViewById(R.id.btn_submit);
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        et_lay_conform = (TextInputLayout) findViewById(R.id.et_lay_conform);
        et_lay_new = (TextInputLayout) findViewById(R.id.et_lay_new);
        et_lay_current = (TextInputLayout) findViewById(R.id.et_lay_current);

    }


    public void chnagePassword(View v) {
        if (ShowDilog.internet) {
            Intent i;
            switch (v.getId()) {
                case R.id.btn_submit:

                    if (validate()) {

                        HashMap<String, String> params = new HashMap<>();
                        params.put("userId", userid);
                        params.put("oldPassword", currentPass);
                        params.put("newPassword", newPass);
                        Constant.CURRENT_TAG = urlConstants.PASSWORD_CHANGE_TAG;
                        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.PASSWORD_CHANGE_URL, params);
                    }
                    break;
            }
        } else {

            GeneralUtilities.showMessage(ChangePassword.this, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(ChangePassword.this).execute();
        }
    }

    private boolean validate() {

        currentPass = etCurrent.getText().toString();
        newPass = etNew.getText().toString();
        conformPass = etConform.getText().toString();
        passMismatch = false;

        if (currentPass.isEmpty()) {
            et_lay_current.setError("Please Enter Password");
            return false;
        }
        if (newPass.isEmpty()) {
            et_lay_new.setError("Please Enter Password");
            return false;
        }
        if (conformPass.isEmpty()) {
            et_lay_conform.setError("Please Enter Password");
            return false;
        }

        if(newPass.length()<8){
            et_lay_new.setError("Password must contain minimum of 8 characters");
            return false;
        }
        if(!GeneralUtilities.validatePassword(newPass)){
            et_lay_new.setError("Password must contain atleast 1 lower,upper,number & special character");
            return false;
        }
        if(conformPass.length()<8){
            et_lay_conform.setError("Password must contain minimum of 8 characters");
            return false;
        }
        if(!GeneralUtilities.validatePassword(conformPass)){
            et_lay_conform.setError("Password must contain atleast 1 lower,upper,number & special character");
            return false;
        }

        if (!conformPass.equals(newPass)) {
            et_lay_new.setError(" Password mismatch ");
            et_lay_conform.setError(" Password mismatch ");
            passMismatch = true;
            return false;
        }
        if (currentPass.equals(newPass) || currentPass.equals(conformPass)) {
            et_lay_current.setError("Current and Confirm password should not be same");

            return false;
        }
        return true;
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


                if (tag == urlConstants.PASSWORD_CHANGE_TAG) {

                    if (result == 1) {
                        GeneralUtilities.showMessage(ChangePassword.this, parentLayout, "Password changed successfully");
                        etConform.setText("");
                        etCurrent.setText("");
                        etNew.setText("");
                        SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
                        editor.putString(Constant.MY_PREF_PASSWORD, newPass);
                        editor.apply();
                    } else {
                        GeneralUtilities.showMessage(ChangePassword.this, parentLayout, response);
                    }
                } else {
                    GeneralUtilities.showMessage(ChangePassword.this, parentLayout, " Please try again");
                }
            }


            @Override
            public void onVolleyError(String result) {

                GeneralUtilities.showMessage(ChangePassword.this, parentLayout, " Please try again");
            }
        });
    }

}

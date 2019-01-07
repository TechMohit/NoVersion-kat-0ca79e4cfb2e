package cms.co.in.kat.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.DpToPx;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class Login extends Activity implements View.OnClickListener,
        TextWatcher, View.OnFocusChangeListener, View.OnTouchListener {

    private TextInputLayout et_uName_lay, et_pswd_lay;
    private TextView tv_forgot, txt_header;
    private EditText et_pswd, et_uName;
    private Button btn_guest, btn_signup, btn_login;
    private Typeface face, faceLight;
    @NonNull
    private ShowDilog dilog = new ShowDilog();
    private boolean userTouchedView, userTouchedView1;
    @NonNull
    private GeneralUtilities gr = new GeneralUtilities(this);
    private Volley vl;
    @Nullable
    private String otpRequestUser = "";
    private boolean internet;
    private ImageView logo;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private View parentLayout;
    static final Integer WRITE_EXST = 0x3;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.act_login);

        initLayout();
        initFontStyle();
        initColorText();
        //initSize();
        initListner();
        initHtWt();

        try {
//            SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
//            editor.putString(Constant.MY_PREF_ANDROIDID, FirebaseInstanceId.getInstance().getToken());
//            Log.e("****** Firebase", " ****token "+ FirebaseInstanceId.getInstance().getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);

    }
//    @Override
//    protected Session getDefaultSession() {
//        return new SessionImpl("http://192.168.0.106:8080",new BasicAuthentication("test@liferay.com","test"));
//    }
//
//    @Override
//    protected void onPushNotificationReceived(JSONObject jsonObject) {
//
//        startNotification(""+jsonObject,"Demo notify","2");
//    }
//
//    @Override
//    protected void onErrorRegisteringPush(String message, Exception e) {
//
//
//        Log.e("****","****"+message +"\n\n"+e);
//    }
//
//    @Override
//    protected String getSenderId() {
//        return "1001649271331";
//    }



    private void initHtWt() {

        ViewGroup.LayoutParams params = btn_guest.getLayoutParams();
        params.height = (int) DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.btn_height));
        ViewGroup.LayoutParams params1 = btn_guest.getLayoutParams();
        params1.height = (int) DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.btn_height));
        ViewGroup.LayoutParams params2 = btn_guest.getLayoutParams();
        params2.height = (int) DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.btn_height));
        btn_guest.setLayoutParams(params);
        btn_login.setLayoutParams(params1);
        btn_signup.setLayoutParams(params2);

    }

    private void initListner() {

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_guest.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);

        et_uName.addTextChangedListener(this);
        et_pswd.addTextChangedListener(this);
        et_uName.setOnFocusChangeListener(this);
        et_pswd.setOnFocusChangeListener(this);
    }

    private void initColorText() {

        et_uName.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.user_name))));
        et_pswd.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.password))));
    }

    private void initSize() {

        txt_header.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_header)));
        btn_guest.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        btn_login.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        btn_signup.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        tv_forgot.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_pswd.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_uName.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));

    }

    private void initFontStyle() {
        face = Typeface.createFromAsset(getAssets(), Constant.FONT_MEDIUM);
        faceLight = Typeface.createFromAsset(getAssets(), Constant.FONT_LIGHT);
        et_pswd.setTypeface(face);
        et_uName.setTypeface(face);
        tv_forgot.setTypeface(face);
        btn_guest.setTypeface(face);
        btn_signup.setTypeface(face);
        btn_login.setTypeface(face);
        txt_header.setTypeface(face);
        et_uName_lay.setTypeface(face);
        et_pswd_lay.setTypeface(face);
    }

    private void initLayout() {

        logo            = (ImageView) findViewById(R.id.logo);
        parentLayout    = findViewById(R.id.parentLayout);
        txt_header      = (TextView) findViewById(R.id.txt_header);
        btn_login       = (Button) findViewById(R.id.btn_login);
        btn_signup      = (Button) findViewById(R.id.btn_sign_up);
        btn_guest       = (Button) findViewById(R.id.btn_guest);
        tv_forgot       = (TextView) findViewById(R.id.tv_forgot);
        et_uName        = (EditText) findViewById(R.id.et_uName);
        et_pswd         = (EditText) findViewById(R.id.et_pswd);

        et_uName_lay    = (TextInputLayout) findViewById(R.id.et_uName_lay);
        et_pswd_lay     = (TextInputLayout) findViewById(R.id.et_pswd_lay);

        // mImageView = (ImageView) findViewById(R.id.customImageVIew1);
       // layinternet = (RelativeLayout) findViewById(R.id.layinternet);
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        EventBus.getDefault().unregister(this);
//    }
//
//    public void onEvent(Boolean result) {
//
//
//        if(!result){
//            dilog.showSnackBar(Login.this,parentLayout);
//        }
//    }
    @Override
    public void onClick(@NonNull View v) {
        Intent i;

        if (ShowDilog.internet) {

            switch (v.getId()) {
                case R.id.btn_login:
//                    Intent i1 = new Intent(Login.this, LoginHome.class);
//                    startActivity(i1);
                    if (validation()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("login", et_uName.getText().toString());
                        params.put("password", et_pswd.getText().toString());
                        Constant.CURRENT_TAG = urlConstants.LOGIN_TAG;
                        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.LOGIN_URL, params);
                    }
                    break;
                case R.id.btn_sign_up:

                    i = new Intent(this, SignupPre.class);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                Login.this,
                                logo, "logo");
                        startActivity(i, options.toBundle());
                    } else {
                        startActivity(i);
                    }

                    break;
                case R.id.btn_guest:


                    i = new Intent(this, GuestHome.class);
                    startActivity(i);
                    break;
                case R.id.tv_forgot:

                    dilog.createDialog(this, Constant.DILOG_FORGT_PASS, new DialogListener() {
                        @Override
                        public void onFinish(int code, String user_id) {
//                            new GeneralUtilities(this).showToastMessage("OTP Sent");
                            forgotPswdUrl(code, user_id);
                        }
                    });
                    break;
                default:
                    break;
            }
        } else {

            GeneralUtilities.showMessage(Login.this, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(Login.this).execute();
        }
    }

    private void forgotPswdUrl(int code, @Nullable String user_id) {
        if (code == Constant.DILOG_RESEND && user_id == null) {
            user_id = otpRequestUser;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user_id);
        otpRequestUser = user_id;
        System.out.println(" Tag Constant.DILOG_RESEND -  Params " + params);
        Constant.CURRENT_TAG = urlConstants.FORGOT_PSWD_TAG;
        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.FORGOT_PSWD_URL, params);
    }

    private boolean validation() {

        if (et_uName.getText().toString().equalsIgnoreCase("")) {
            et_uName_lay.setErrorEnabled(true);
            et_uName_lay.setError("Enter Your UserID");
            return false;
        } else if (et_pswd.getText().toString().equalsIgnoreCase("")) {
            et_pswd_lay.setErrorEnabled(true);
            et_pswd_lay.setError("Enter Your Password");
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        vl = new Volley(this);

        vl.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, @NonNull String response) {
                if (tag == urlConstants.LOGIN_TAG) {
                    login(result, response);
                } else if (tag == urlConstants.FORGOT_PSWD_TAG) {
                    verifyOtp(result, response);
                } else if (tag == urlConstants.OTP_VERIFY_TAG) {
                    updatePswd(result, response);
                } else if (tag == urlConstants.UPDATE_PSWD_TAG) {
                    if (result == 1) {

                        et_pswd.setText("");

                        dilog.createDialog(Login.this, Constant.DILOG_DISMISS, null);
                    }
                    GeneralUtilities.showMessage(Login.this, parentLayout, " Password updated Successfully");

                } else {
                    GeneralUtilities.showMessage(Login.this, parentLayout, " Please try again");
                }
            }


            @Override
            public void onVolleyError(String result) {

                GeneralUtilities.showMessage(Login.this, parentLayout, " Please try again");
            }
        });
    }

    private void login(int result, @NonNull String response) {
        Log.d(urlConstants.LOGIN_TAG, "result code " + response);
        if (result == 1) {
            try {

                JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
                String userId = jArr.getJSONObject(0).getString("userId");

                Boolean passReset = jArr.getJSONObject(0).getBoolean("userPassword");
                if (passReset) {
                    dilog.createDialog(Login.this, Constant.DILOG_UPDATE_PASS, new DialogListener() {
                        @Override
                        public void onFinish(int code, String pswd) {

                            HashMap<String, String> params = new HashMap<>();
                            params.put("screenName", et_uName.getText().toString());
                            params.put("password", pswd);
                            System.out.println(" Tag -" + urlConstants.UPDATE_PSWD_TAG + " FORGOT_PSWD_URL " + urlConstants.UPDATE_PSWD_URL + " Params " + params);
                            Constant.CURRENT_TAG = urlConstants.UPDATE_PSWD_TAG;

                            volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.UPDATE_PSWD_URL, params);

                        }
                    });

                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
                    editor.putString(Constant.MY_PREF_USERNAME, et_uName.getText().toString());
                    editor.putString(Constant.MY_PREF_USERID, userId);
                    editor.putString(Constant.MY_PREF_PASSWORD, et_pswd.getText().toString());
                    editor.apply();

                    Intent i = new Intent(Login.this, LoginHome.class);
                    startActivity(i);
                    finish();
                }
            } catch (Exception e) {

            }

        } else {

            GeneralUtilities.showMessage(Login.this, parentLayout, response);

        }
    }


    private void verifyOtp(int result, String response) {
        Log.d(urlConstants.OTP_VERIFY_TAG, "result code " + response);
        if (result == 1) {
            dilog.createDialog(Login.this, Constant.DILOG_DISMISS, null);
            dilog.createDialog(Login.this, Constant.DILOG_OTP, new DialogListener() {
                @Override
                public void onFinish(int code, String pswd) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("userName", otpRequestUser);
                    params.put("otp", pswd);
                    System.out.println(" Tag -" + urlConstants.UPDATE_PSWD_TAG + " FORGOT_PSWD_URL " + urlConstants.UPDATE_PSWD_URL + " Params " + params);
                    Constant.CURRENT_TAG = urlConstants.OTP_VERIFY_TAG;
                    volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.OTP_VERIFY_URL, params);
                }
            });
        }
    }

    private void updatePswd(int result, @NonNull String response) {
        Log.d(urlConstants.UPDATE_PSWD_TAG, "result code " + response);
        if (result == 1) {
            dilog.createDialog(Login.this, Constant.DILOG_DISMISS, null);
            dilog.createDialog(Login.this, Constant.DILOG_UPDATE_PASS, new DialogListener() {
                @Override
                public void onFinish(int code, String pswd) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("screenName", otpRequestUser);
                    params.put("password", pswd);
                    System.out.println(" Tag -" + urlConstants.UPDATE_PSWD_TAG + " FORGOT_PSWD_URL " + urlConstants.UPDATE_PSWD_URL + " Params " + params);
                    Constant.CURRENT_TAG = urlConstants.UPDATE_PSWD_TAG;
                    volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.UPDATE_PSWD_URL, params);

                }
            });
        } else {
            Log.d("msgcheck","123"+response);
           // GeneralUtilities.showMessage(Login.this, parentLayout, response);

            dilog.otpNotValid();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        et_pswd_lay.setError(null);
        et_uName_lay.setError(null);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == et_uName) {

            if (hasFocus && !userTouchedView) {
                //YOUR  2
                et_uName.setHint("");

            } else {

                et_uName.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.user_name))));
            }
        } else if (v == et_pswd) {
            if (hasFocus && !userTouchedView1) {
                //YOUR  2
                et_pswd.setHint("");

            } else {
                et_pswd.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.password))));
            }
        }
        if (!hasFocus) {
            userTouchedView = false;
            userTouchedView1 = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v == et_uName) {
            userTouchedView = true;
            et_uName.setHint("");

        } else if (v == et_pswd) {
            userTouchedView1 = true;
            et_pswd.setHint("");
        }
        return false;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Login.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            GeneralUtilities.showMessage(Login.this, parentLayout, "Permission granted");

        } else {
            GeneralUtilities.showMessage(Login.this, parentLayout, "Permission denied");
        }
    }
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            GeneralUtilities.showMessage(Login.this, parentLayout, "Press again to Exit");
        }

        mBackPressed = System.currentTimeMillis();

    }

    private void startNotification(String tittle, String text, String code) {
        int mNotificationId = (int) (333 * Math.random());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(tittle)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setOngoing(false);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Login.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

}
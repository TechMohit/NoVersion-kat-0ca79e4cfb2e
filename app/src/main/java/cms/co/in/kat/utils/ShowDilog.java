package cms.co.in.kat.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import cms.co.in.kat.activity.Login;
import cms.co.in.kat.activity.MyApplication;
import cms.co.in.kat.activity.Signup;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.SmsListener;
import cms.co.in.kat.recevier.SmsReceiver;

/**
 * Created by Happy on 17-02-2017.
 */

public class ShowDilog {

    private Context context;
    @Nullable
    private Dialog pDialog;
    private TextView txt_msg, txt_otp_msg, txt_user_id, tvTitle1, tvTitle2, tvDidntRec, tvResend, txtUpdate, txtUpdateHead;
    private Typeface face, faceLight;
    private Button btn_retry, btn_submit, btn_fp_submit, btnUpdate;
    private EditText etOtp, etOtp1, etOtp2, etOtp3, et_user_id, etPswd;
    private String otpMessage = "";
    private int red, blue;
    @Nullable
    private DialogListener listener_forgot;

    public static boolean internet = false;

    public void createDialog(@NonNull Context context, int code, DialogListener listener) {

        this.context = context;
        face = Typeface.createFromAsset(context.getAssets(), Constant.FONT_MEDIUM);
        faceLight = Typeface.createFromAsset(context.getAssets(), Constant.FONT_LIGHT);
        red = ContextCompat.getColor(context, R.color.red);
        blue = ContextCompat.getColor(context, R.color.colorPrimary);

        Constant.FLAG = code;

        switch (code) {
            case Constant.DILOG_SUCESS:
                showSucess();
                break;
            case Constant.DILOG_DISMISS:
                dismiss();
                break;
            case Constant.DILOG_NWTWORK:
                showNetworkDilog();
                break;
            case Constant.DILOG_FORGT_PASS:
                pDialog = null;
                showForgotPswdDialog(listener);
                break;
            case Constant.DILOG_OTP:
                pDialog = null;
                showOTPDilog(listener);
                break;
            case Constant.DILOG_UPDATE_PASS:
                pDialog = null;
                showNewPswdDilog(listener);
                break;

            case Constant.DILOG_UPDATE:
                pDialog = null;
                showUpdate();
                break;

        }
    }

    private void showUpdate() {
        pDialog = new Dialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);

        pDialog.setContentView(R.layout.update);
        pDialog.setCancelable(false);
        if (!pDialog.isShowing())
            pDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = pDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        txtUpdateHead = (TextView) pDialog.findViewById(R.id.txt_update_head);

        txtUpdate = (TextView) pDialog.findViewById(R.id.txt_update);
        btnUpdate = (Button) pDialog.findViewById(R.id.btn_update);

        btnUpdate.setTypeface(face);
        txtUpdateHead.setTypeface(face);
        txtUpdate.setTypeface(faceLight);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+context.getPackageName())); // Open precisely @link SmartBooks
                boolean tryAgain = false; // Flag to denote that normal attempt to launch GooglePlay update failed

                try
                {
                    context.startActivity(intent);
                }
                catch(Exception e)
                {
                    tryAgain = true;
                }

                if (!tryAgain) return;

                // Try to launch GooglePlay with SB in browser !
                try
                {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName()));
                    context.startActivity(intent);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Unable to run app update automatically. Please run it from GooglePlay manualy.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showNewPswdDilog(@Nullable final DialogListener listener) {
        if (listener != null) {
            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);

            pDialog.setContentView(R.layout.new_pswd);
            pDialog.setCancelable(false);
            if (!pDialog.isShowing())
                pDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = pDialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            btn_submit = (Button) pDialog.findViewById(R.id.btn_submit);
            tvTitle1 = (TextView) pDialog.findViewById(R.id.tv_title1);
            tvTitle2 = (TextView) pDialog.findViewById(R.id.tv_title2);
            etPswd = (EditText) pDialog.findViewById(R.id.et_new_pswd);
            final EditText etConfirmPswd = (EditText) pDialog.findViewById(R.id.et_confirm_pswd);

            btn_submit.setTypeface(face);
            tvTitle1.setTypeface(face);
            tvTitle2.setTypeface(face);
            etPswd.setTypeface(face);
            etConfirmPswd.setTypeface(face);

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pass1="", pass2="";
                    pass1 = etPswd.getText().toString().trim();
                    pass2 = etConfirmPswd.getText().toString().trim();
                    if(pass1.equalsIgnoreCase("")||pass2.equalsIgnoreCase("")){
                        new GeneralUtilities(context).showToastMessage("Passwords is empty");
                    }else if (pass1.equals(pass2)) {
                        if(pass1.length()<8){
                            new GeneralUtilities(context).showToastMessage("Password must contain minimum of 8 characters");
                        }else if(!GeneralUtilities.validatePassword(pass1)){
                            new GeneralUtilities(context).showToastMessage("Password must contain atleast 1 lower case, 1 upper case, 1 number & 1 special character");

                        }else{
                            listener.onFinish(Constant.DILOG_UPDATE_PASS, pass1);
                        }
                    } else {
                        new GeneralUtilities(context).showToastMessage("Passwords Should Match");

                    }
                }
            });
        }
    }

    private void showOTPDilog(@Nullable final DialogListener mlistener) {
        if (mlistener != null) {
            pDialog = new Dialog(context);
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);
            pDialog.setContentView(R.layout.otp);
            pDialog.setCancelable(true);
            if (!pDialog.isShowing())
                pDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = pDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            btn_submit = (Button) pDialog.findViewById(R.id.btn_submit);
            etOtp = (EditText) pDialog.findViewById(R.id.et_otp);
            etOtp1 = (EditText) pDialog.findViewById(R.id.et_otp1);
            etOtp2 = (EditText) pDialog.findViewById(R.id.et_otp2);
            etOtp3 = (EditText) pDialog.findViewById(R.id.et_otp3);

            tvTitle1 = (TextView) pDialog.findViewById(R.id.tv_title1);
            tvTitle2 = (TextView) pDialog.findViewById(R.id.tv_title2);
            tvDidntRec = (TextView) pDialog.findViewById(R.id.tv_dont_rec);
            tvResend = (TextView) pDialog.findViewById(R.id.tv_resend);

            btn_submit.setTypeface(face);
            tvTitle1.setTypeface(face);
            tvTitle2.setTypeface(face);
            etOtp.setTypeface(face);
            etOtp1.setTypeface(face);
            etOtp2.setTypeface(face);
            etOtp3.setTypeface(face);

            tvDidntRec.setTypeface(faceLight);
            tvResend.setTypeface(faceLight);

            tvTitle2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

//            tvTitle1.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            tvTitle2.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            etOtp.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            etOtp1.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            etOtp2.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            etOtp3.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            tvDidntRec.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            tvResend.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            btn_submit.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));

//            ViewGroup.LayoutParams params = btn_submit.getLayoutParams();
//            params.height = (int) DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.btn_height));
//            btn_submit.setLayoutParams(params);

            etOtp.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(@NonNull Editable s) {

                    if (s.length() == 1) {
                        etOtp1.requestFocus();
                        etOtp.setSelection(1);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    etOtp.setTextColor(ContextCompat.getColor(context, R.color.black));
                    tvTitle2.setTextColor(blue);
                    tvTitle2.setText(context.getResources().getString(R.string.otp_status));

                }
            });
            etOtp1.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(@NonNull Editable s) {

                    if (s.length() == 1) {
                        etOtp2.requestFocus();
                        etOtp1.setSelection(1);

                    } else if (s.length() == 0) {
                        etOtp.requestFocus();

                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    etOtp1.setTextColor(ContextCompat.getColor(context, R.color.black));
                    tvTitle2.setText(context.getResources().getString(R.string.otp_status));
                    tvTitle2.setTextColor(blue);
                }
            });
            etOtp2.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(@NonNull Editable s) {
                    if (s.length() == 1) {
                        etOtp3.requestFocus();
                        etOtp2.setSelection(1);
                    } else if (s.length() == 0) {
                        etOtp1.requestFocus();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    etOtp2.setTextColor(ContextCompat.getColor(context, R.color.black));
                    tvTitle2.setTextColor(blue);
                    tvTitle2.setText(context.getResources().getString(R.string.otp_status));
                }
            });
            etOtp3.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(@NonNull Editable s) {

                    if (s.length() == 1) {
                        btn_submit.requestFocus();
                        etOtp3.setSelection(1);
                    } else if (s.length() == 0) {
                        etOtp2.requestFocus();

                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    etOtp3.setTextColor(ContextCompat.getColor(context, R.color.black));
                    tvTitle2.setTextColor(blue);
                    tvTitle2.setText(context.getResources().getString(R.string.otp_status));

                }
            });

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String otpMessageTemp) {
                    Log.d("Text", "-----*** " + otpMessageTemp);
                    otpMessage = otpMessageTemp;
                    etOtp.setText("");
                    etOtp1.setText("");
                    etOtp2.setText("");
                    etOtp3.setText("");

                    if (otpMessage.length() == 4) {
                        etOtp.setText("" + otpMessage.charAt(0));
                        etOtp1.setText("" + otpMessage.charAt(1));
                        etOtp2.setText("" + otpMessage.charAt(2));
                        etOtp3.setText("" + otpMessage.charAt(3));
                        mlistener.onFinish(Constant.FLAG, otpMessage);
                    }
                }
            });
            tvResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener_forgot!=null){
                        Constant.FLAG = Constant.DILOG_RESEND;
                        listener_forgot.onFinish(Constant.FLAG, et_user_id.getText().toString());

                    }
                    else{

                        Constant.FLAG = Constant.DILOG_RESEND;
                        mlistener.onFinish(Constant.FLAG,null);

                    }

                }
            });
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otpMessage = etOtp.getText().toString() + etOtp1.getText().toString() + etOtp2.getText().toString() + etOtp3.getText().toString();
                    if (otpMessage.equalsIgnoreCase("")) {

                        Toast.makeText(context, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                    } else {

                        mlistener.onFinish(Constant.FLAG, otpMessage);

                    }
                }
            });
        }
    }


    private void showForgotPswdDialog(@Nullable final DialogListener listener) {
        if (listener != null) {
            this.listener_forgot = listener;
            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);
            pDialog.setContentView(R.layout.forgot_password);
            pDialog.setCancelable(true);
            if (!pDialog.isShowing())
                pDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = pDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            btn_fp_submit = (Button) pDialog.findViewById(R.id.btn_fp_submit);
            et_user_id = (EditText) pDialog.findViewById(R.id.et_user_id);
            txt_otp_msg = (TextView) pDialog.findViewById(R.id.txt_otp_msg);
            txt_user_id = (TextView) pDialog.findViewById(R.id.txt_user_id);

            btn_fp_submit.setTypeface(face);
            et_user_id.setTypeface(face);
            txt_otp_msg.setTypeface(faceLight);
            txt_user_id.setTypeface(face);

            btn_fp_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String res=et_user_id.getText().toString().trim();
                    if(res.equalsIgnoreCase("") && res.length()<1){
                        Toast.makeText(context, "Enter a Valid User ID", Toast.LENGTH_SHORT).show();
                    }else {
                        listener.onFinish(Constant.DILOG_FORGT_PASS, et_user_id.getText().toString().trim());
                    }

                }
            });
        }
    }

    private void showNetworkDilog() {
        if (pDialog == null) {
            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);
            pDialog.setContentView(R.layout.connectivity);
            pDialog.setCancelable(false);
            if (!pDialog.isShowing())
                pDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = pDialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            txt_msg = (TextView) pDialog.findViewById(R.id.txt_msg);
//            txt_msg_serch = (TextView) pDialog.findViewById(R.id.txt_msg_serch);
            btn_retry = (Button) pDialog.findViewById(R.id.btn_retry);

            btn_retry.setTypeface(faceLight);
            txt_msg.setTypeface(face);
//            txt_msg_serch.setTypeface(faceLight);

//            txt_msg_serch.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            txt_msg.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));
//            btn_retry.setTextSize(DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.txt_normal)));

//            ViewGroup.LayoutParams params = btn_retry.getLayoutParams();
//            params.height = (int) DpToPx.dpToPixel(context, context.getResources().getInteger(R.integer.btn_height));
//            btn_retry.setLayoutParams(params);

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }

    private void dismiss() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }

    }

    private void showSucess() {
        if (pDialog == null) {
            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.getWindow().setWindowAnimations(R.style.DialogNoAnimation);
            pDialog.setContentView(R.layout.custom);
            pDialog.setCancelable(false);
            if (!pDialog.isShowing())
                pDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = pDialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

        }
    }

    public void showSnackBar(@NonNull Context context, @NonNull View view) {

        this.context = context;
        final Snackbar snackbar = Snackbar
                .make(view, " No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("Offline Mode On", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.blue_light));
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        Button snackbarActionButton = (Button) sbView.findViewById(android.support.design.R.id.snackbar_action);
//        snackbarActionButton.setBackgroundResource(android.R.drawable.ic_dialog_alert);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void otpNotValid() {
        Animation animFadein = AnimationUtils.loadAnimation(context,
                R.anim.bounce);
        etOtp.startAnimation(animFadein);
        etOtp1.startAnimation(animFadein);
        etOtp2.startAnimation(animFadein);
        etOtp3.startAnimation(animFadein);
        etOtp.setTextColor(red);
        etOtp1.setTextColor(red);
        etOtp2.setTextColor(red);
        etOtp3.setTextColor(red);

        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        tvTitle2.setText(context.getResources().getString(R.string.invalid_otp));
        tvTitle2.setTextColor(ContextCompat.getColor(context, R.color.red));
    }


}



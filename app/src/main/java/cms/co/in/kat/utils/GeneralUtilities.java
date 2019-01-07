package cms.co.in.kat.utils;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cms.co.in.kat.activity.Login;


/**
 * Created by vishal_mokal on 3/2/15.
 */
public class GeneralUtilities {

    private Context context;

    public static final String MIME_TYPE_PDF = "application/pdf";

    public GeneralUtilities(Context context) {
        this.context = context;
    }

    public boolean validateEmailAddress(@NonNull String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateUserName(@NonNull String name) {
        String ePattern = "^[a-zA-Z /.]*$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(name);
        return m.matches();
    }


    public void writeDataToSharedPreferences(String emailAddress, String appUserName) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences("tnecs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("FullName", appUserName);
            editor.putString("EmailAddress", emailAddress);
            editor.apply();
        } catch (Exception e) {
            Log.d("error", e.getLocalizedMessage());
        }
    }

    @Nullable
    public String readDataFromSharedPreferences() {

        SharedPreferences sharedPref = context.getSharedPreferences("tnecs", Context.MODE_PRIVATE);
        String email = sharedPref.getString("EmailAddress", null);
        return email;
    }


    public boolean validatePhoneNumber(@NonNull String phonenumber) {
        boolean isValidPhoneNumber;

        if (phonenumber.length() >= 10 && phonenumber.length() <= 12) {

            if (phonenumber.startsWith("91") || phonenumber.startsWith("7") ||  phonenumber.startsWith("0") ||
                    phonenumber.startsWith("8") || phonenumber.startsWith("9")) {

                String ePattern = "^[0-9]*$";
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(phonenumber);
                return m.matches();

            } else {
                return false;
            }


        } else {
            return false;
        }
    }

    public static boolean validatePassword(@NonNull String password) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void showToastMessage(@NonNull String message) {
        Toast.makeText(context, message.toUpperCase(), Toast.LENGTH_LONG).show();
    }

    @NonNull
    public String colorText(String text) {

        String string = "<font color='#808080'>" + text + " </font>" + "<font color='#ef5350'>* </font>";
        return string;
    }

    @NonNull
    public String colorText(String text,String textolor,int color) {

        String string = "<font color='#808080'>" + text + " </font>" + "<font color='"+color+"'>"+textolor+"</font>";
        return string;
    }

    public void openFilePDFFromInstalledApp(String pdfUrl) {

        try {
            Intent inte = new Intent(Intent.ACTION_VIEW);
            inte.setDataAndType(
                    Uri.parse(pdfUrl), MIME_TYPE_PDF);

            if (inte != null && canDisplayPdf(context)) {
                Toast.makeText(context, "Opening PDF... ", Toast.LENGTH_SHORT).show();
                context.startActivity(inte);
            } else{
                Toast.makeText(context, "Install PDF Viwer ", Toast.LENGTH_SHORT).show();
                Log.e("Viewer not installed", "PDF Viewer not available ");

            }
        } catch (ActivityNotFoundException e) {
//            Toast.makeText(context, "Install PDF Viwer ", Toast.LENGTH_SHORT).show();

            Log.e("Viewer not installed", e.getMessage());
        }
    }


    public static boolean canDisplayPdf(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType(MIME_TYPE_PDF);
        return packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //DATE TIME PICKER and RETURNS SELECTED DATE
    static TextView btnV;
    static Calendar myCalendar = Calendar.getInstance();
    @NonNull
    static DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private static void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        btnV.setText(sdf.format(myCalendar.getTime()));
        java.util.Date dt = new java.util.Date();
        myCalendar.setTime(dt);
    }

    public void setDate(TextView btnView) {
        btnV = btnView;
        Calendar caltemp = Calendar.getInstance();
        caltemp.add(Calendar.DAY_OF_YEAR, 1);

        DatePickerDialog dpd=  new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = dpd.getDatePicker();
        dp.setMaxDate(caltemp.getTimeInMillis());
        dpd.show();

    }

    public static int getVersionCode(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {}
        return 0;
    }

    public static String getVersionName(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {}
        return "";
    }

    public static void showMessage(Context mcontext, View parentlayout, String message){

        try {
            Snackbar snackbar = Snackbar
                    .make(parentlayout, message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.white));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

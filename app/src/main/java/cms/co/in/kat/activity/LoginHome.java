package cms.co.in.kat.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.fragment.FragmentDashboard;
import cms.co.in.kat.fragment.FragmentEWallet;
import cms.co.in.kat.fragment.FragmentHome;
import cms.co.in.kat.fragment.FragmentProfile;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class LoginHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //    private PieChart mChart;
    @NonNull
    private ShowDilog dilog = new ShowDilog();
    private LinearLayout parentLayout;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private Volley vl;
    private TextView userName, userEmail;
    private long mBackPressed;
    private String CURRENT_FRAGMENT_TAG = "HOME", email = "", phone;
    private static final int TIME_INTERVAL = 1000; // # milliseconds, desired time passed between two back presses.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        if (savedInstanceState==null) {
            displaySelectedScreen(R.id.nav_camera);
        }
        userName = (TextView) header.findViewById(R.id.user_name);
        userEmail = (TextView) header.findViewById(R.id.user_email);

//        initChart();

//        if (!ShowDilog.internet) {
//            dilog.showSnackBar(LoginHome.this, parentLayout);
//        }
        Constant.CURRENT_TAG = urlConstants.CASE_SEARCH_AUTO_TAG;
        volly(urlConstants.UPDATE_TAG, Request.Method.GET, urlConstants.UPDATE_URL, null);

        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String userid = prefs.getString(Constant.MY_PREF_USERID, null);

        Constant.CURRENT_TAG = urlConstants.NOTIFICATION_API_TAG;
        HashMap<String, String> params = new HashMap<>();
        params.put("userID", userid);
        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.NOTIFICATION_API_URL, params);

        userName.setText("");
        userEmail.setText("");

//      updateNotificationToken();
    }

    //    private void updateNotificationToken() {
//        SharedPreferences prefs = this.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
//        String userid = prefs.getString(Constant.MY_PREF_USERID, null);
//        String refreshedToken = prefs.getString(Constant.MY_PREF_ANDROIDID, null);
//        if(refreshedToken==null){
//            refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        }
//        Log.e("********* ", "*********** " + refreshedToken);
//        if(refreshedToken!=null && !refreshedToken.isEmpty()) {
//            SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
//            editor.putString(Constant.MY_PREF_ANDROIDID, refreshedToken);
//            if(userid==null){
//                userid=Constant.GUEST_USER;
//            }
//            Log.e("** notification","Notify token updtae to user id*** "+userid);
//            HashMap<String, String> params = new HashMap<>();
//            params.put("token", refreshedToken);
//            params.put("platform", Constant.OS_TYPE);
//            params.put("userId", userid);
//            Constant.CURRENT_TAG = urlConstants.NOTIFICATION_TAG;
//            volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.NOTIFICATION_URL, params);
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        return id == R.id.action_settings || super.onOptionsItemSelected(item);
//
//    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (itemId) {
            case R.id.nav_camera:
                fragment = new FragmentHome();
                ft.replace(R.id.content_frame, fragment);
                CURRENT_FRAGMENT_TAG = "HOME";
                break;
            case R.id.nav_gallery:
                fragment = new FragmentDashboard();
                ft.replace(R.id.content_frame, fragment);
                CURRENT_FRAGMENT_TAG = "DASHBOARD";
                break;
            case R.id.nav_profile:
                fragment = new FragmentProfile();
                ft.replace(R.id.content_frame, fragment);
                CURRENT_FRAGMENT_TAG = "PROFILE";
                break;

            case R.id.nav_ewallet:
                fragment = new FragmentEWallet();
                ft.replace(R.id.content_frame, fragment);
                CURRENT_FRAGMENT_TAG = "EWALLET";
                break;

//            case R.id.nav_link:
//                fragment = new FragmentLinkedCases();
//                ft.replace(R.id.content_frame, fragment);
//                CURRENT_FRAGMENT_TAG = "LINKEDCASES";
//                break;

            case R.id.nav_contact_us:

                Intent i1 = new Intent(this, ContactUs.class);
                startActivity(i1);

                break;
            case R.id.nav_logout:
                DatabaseHandler db = new DatabaseHandler(LoginHome.this);
                db.deleteTable(LoginHome.this);

                SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
                editor.putString(Constant.MY_PREF_USERNAME, null);
                editor.putString(Constant.MY_PREF_USERID, null);
                editor.putString(Constant.MY_PREF_PASSWORD, null);

                editor.apply();

//                updateNotificationToken();

                Intent intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

//                new GeneralUtilities(this).showToastMessage(" Logout sucessfull");
                break;
            case R.id.nav_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "KAT CWS");
                    String sAux = "\nPlease enroll yourself in CWS to get updates regarding your cases.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + LoginHome.this.getPackageName() + " \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {

                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "KAT CWS");
                        String sAux = "\nPlease enroll yourself in CWS to get updates regarding your cases.\n\n";
                        sAux = sAux + "market://details?id=" + LoginHome.this.getPackageName() + " \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    //e.toString();
                }
//                new GeneralUtilities(this).showToastMessage(" Clicked help!");
                break;
            case R.id.nav_feedback:
                Intent i2 = new Intent(this, Feedback.class);
                i2.putExtra("email", email);
                i2.putExtra("phone", phone);
                startActivity(i2);
                break;

            case R.id.nav_terms:
                Intent i3 = new Intent(this, TermCondition.class);
                startActivity(i3);
            default:
                fragment = new FragmentHome();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        if (ShowDilog.internet) {

                displaySelectedScreen(item.getItemId());

        } else {
            GeneralUtilities.showMessage(LoginHome.this, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(LoginHome.this).execute();
        }
        //make this method blank
        return true;
    }

    @Override
    public void onBackPressed() {
//        FragmentHomeBack back = new FragmentHomeBack();
//        back.setBackPress(true);
//        EventBus.getDefault().post(back);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (CURRENT_FRAGMENT_TAG.equalsIgnoreCase("HOME")) {
                 finish();
                /*if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                } else {
                    GeneralUtilities.showMessage(LoginHome.this, parentLayout, "Press again to Exit");
                }*/
                mBackPressed = System.currentTimeMillis();
            } else {
                Fragment fragment = null;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                fragment = new FragmentHome();
                ft.replace(R.id.content_frame, fragment);
                CURRENT_FRAGMENT_TAG = "HOME";
                ft.commit();
            }
        }
    }

    public void setProfile(String name, String email, String userPhone) {
        userEmail.setText("" + email);
        userName.setText("" + name);
        this.phone = userPhone;
        this.email = email;
    }

    public void caseNumbers(int length, int length1, int length2, int length3, int length4, int scr) {

        Log.e("** activity ", "** " + length + " " + length1 + " " + length2 + " " + length3 + " " + length4 + " " + scr);

    }

    public void toolBarName(String name) {
        getSupportActionBar().setTitle(name);
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
                Log.e("Notificatin", "***  res notify " + response);
                if ((tag == urlConstants.NOTIFICATION_API_TAG)) {
                    Log.d(urlConstants.NOTIFICATION_API_TAG, "result code " + response);
                    if (result == 1) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            for (int i = 0; i < data_result_array.length(); i++) {
                                String tittle = data_result_array.getJSONObject(i).getString("title");
                                String description = data_result_array.getJSONObject(i).getString("notificationContent");
                                notification(tittle, description);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onVolleyError(String result) {
                Log.e("Notificatin", "*** notify " + result);
            }
        });
    }

    private void notification(String tittle, String description) {
        try {
            Random random = new Random();
            int uniqueID = random.nextInt(9999 - 1000) + 1000;

            Intent myIntent = new Intent(this, LoginHome.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, myIntent, 0);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(description);
            Notification bigTextStyleNotification = new NotificationCompat.Builder(this)
                    .setContentTitle(tittle)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentIntent(pi)
                    .setCategory(Notification.CATEGORY_STATUS)
                    .setStyle(bigTextStyle)
                    .build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(uniqueID, bigTextStyleNotification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
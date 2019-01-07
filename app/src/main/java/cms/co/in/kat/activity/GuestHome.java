package cms.co.in.kat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.CountAnimationTextView;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class GuestHome extends Activity implements View.OnClickListener {

    private LinearLayout cvCaseTrack, cvCauseList, cvJudgement, cvSittingList, cvLCD, cvCaseList,cvReports;
    private GeneralUtilities gu;
    private CountAnimationTextView txt_count, txt_count1;
    //    private PieChart mChart,mChart1;
//    private Button save,save1;
    private ShowDilog dilog = new ShowDilog();
    private CoordinatorLayout parentLayout;

    @NonNull
    private URLConstants urlConstants = new URLConstants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_guest_home);
        gu = new GeneralUtilities(this);
        initLayout();
        initFont();
        initListner();
//        initChart(mChart);
        Constant.CURRENT_TAG = urlConstants.UPDATE_TAG;
        volly(urlConstants.UPDATE_TAG, Request.Method.GET, urlConstants.UPDATE_URL, null);

//        updateNotificationToken();
    }


    private void initLayout() {

        parentLayout    = (CoordinatorLayout) findViewById(R.id.coordinator);
        txt_count       = (CountAnimationTextView) findViewById(R.id.count);
        txt_count1      = (CountAnimationTextView) findViewById(R.id.count1);
        cvCaseTrack     = (LinearLayout) findViewById(R.id.cvCaseTrack);
        cvCauseList     = (LinearLayout) findViewById(R.id.cvCauseList);
        cvJudgement     = (LinearLayout) findViewById(R.id.cvJudgement);
        cvSittingList   = (LinearLayout) findViewById(R.id.cvSittingList);
        cvLCD           = (LinearLayout) findViewById(R.id.cvLCD);
        cvCaseList      = (LinearLayout) findViewById(R.id.cvCaseList);
        cvReports       = (LinearLayout)findViewById(R.id.cvReport);

        /*save            = (Button) findViewById(R.id.save);
        save1           = (Button) findViewById(R.id.save1);
        mChart          = (PieChart) findViewById(R.id.chart);
        mChart1         = (PieChart) findViewById(R.id.chart1);*/
    }


    private void initListner() {

        cvCaseTrack.setOnClickListener(this);
        cvCauseList.setOnClickListener(this);
        cvJudgement.setOnClickListener(this);
        cvSittingList.setOnClickListener(this);
        cvLCD.setOnClickListener(this);
        cvReports.setOnClickListener(this);
        cvCaseList.setOnClickListener(this);
//        save.setOnClickListener(this);
//        mChart.setOnChartValueSelectedListener(this);
    }

    private void initFont() {
    }



    private void initValues(int totalCount, int judgementCount) {
        txt_count
                .setAnimationDuration(this.getResources().getInteger(R.integer.case_count_anim))
                .countAnimation(0, judgementCount);
        txt_count1
                .setAnimationDuration(this.getResources().getInteger(R.integer.case_count_anim))
                .countAnimation(0, totalCount);

    }






    @Override
    public void onClick(@NonNull View v) {

        if (ShowDilog.internet) {

            Intent i;
            switch (v.getId()) {

                case R.id.cvCaseTrack:

                    i = new Intent(this, CaseTrack.class);
                    startActivity(i);
                    break;
                case R.id.cvCauseList:

                    i = new Intent(this, CauseList.class);
                    startActivity(i);
                    break;
                case R.id.cvJudgement:

                    i = new Intent(this, Judgement.class);
                    startActivity(i);
                    break;
                case R.id.cvSittingList:

                    i = new Intent(this, SittingList.class);
                    startActivity(i);
                    break;
                case R.id.cvLCD:
                    i = new Intent(this, CourtLcd.class);
                    startActivity(i);
                    break;
                case R.id.cvCaseList:

                    i = new Intent(this, MyCases.class);
                    startActivity(i);
//                    i = new Intent(this, HearingDate.class);
//                    startActivity(i);
                    break;
                case R.id.cvReport:
                    i = new Intent(this, Reports.class);
                    startActivity(i);
                    break;
//            case R.id.save:
                // mChart.saveToPath("title"+System.currentTimeMillis());
//                mChart.saveToGallery("KAT" + System.currentTimeMillis(),"","KAT", Bitmap.CompressFormat.JPEG,100);
//                if (mChart.saveToGallery("KAT" + System.currentTimeMillis(),"KAT_ChartPics","KAT Graph", Bitmap.CompressFormat.JPEG,100)) {
//                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            case R.id.btnPdf:
//
//                openFilePDFFromInstalledApp();
//                break;
            }
        } else {

            GeneralUtilities.showMessage(GuestHome.this, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(GuestHome.this).execute();

        }
    }

//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//        initChart( mChart1);
//
//    }
//
//    @Override
//    public void onNothingSelected() {
//
//    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        Volley v = new Volley(GuestHome.this);

        v.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, String response) {
                if (tag == urlConstants.UPDATE_TAG) {
                    Log.d(tag, "" + response);
                    if (result == 1) {
                        try {
                            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
                            int totalCount = Integer.parseInt(jArr.getJSONObject(0).getString("totalCount"));
                            int judgementCount = Integer.parseInt(jArr.getJSONObject(0).getString("judgementCount"));
                            initValues(totalCount, judgementCount);
                            try {
                                int version = Integer.parseInt(jArr.getJSONObject(0).getString("version"));
                                if (version > GeneralUtilities.getVersionCode(getApplicationContext())) {
                                    dilog.createDialog(GuestHome.this, Constant.DILOG_UPDATE, new DialogListener() {
                                        @Override
                                        public void onFinish(int code, String user_id) {
//                                        new GeneralUtilities(LoginHome.this).showToastMessage(code+" - "+user_id);
                                        }
                                    });

                                    SharedPreferences.Editor editor = getApplication().getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
                                    editor.putString(Constant.MY_PREF_USERNAME, null);
                                    editor.putString(Constant.MY_PREF_USERID, null);
                                    editor.apply();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onVolleyError(@Nullable String result) {

                if (result != null && tag != urlConstants.UPDATE_TAG) {
//                    if(!tag.equalsIgnoreCase(urlConstants.NOTIFICATION_TAG)) {
//                        GeneralUtilities.showMessage(GuestHome.this, parentLayout, "Please Try Again");
//                    }
                }
            }
        });
    }

    private void updateNotificationToken() {

        SharedPreferences prefs = this.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String userid = prefs.getString(Constant.MY_PREF_USERID, null);
        String refreshedToken = prefs.getString(Constant.MY_PREF_ANDROIDID, null);
//        if(refreshedToken==null){
//             refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        }
        Log.e("********* ", "*********** " + refreshedToken);
//        if(refreshedToken!=null && !refreshedToken.isEmpty()) {
//            SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
//            editor.putString(Constant.MY_PREF_ANDROIDID, refreshedToken);
//
//            if(userid==null){
//                userid=Constant.GUEST_USER;
//            }
//            Log.e("** notification","Notify token updtae to user id*** "+userid);
//
//            HashMap<String, String> params = new HashMap<>();
//            params.put("token", refreshedToken);
//            params.put("platform", Constant.OS_TYPE);
//            params.put("userId", userid);
////            Constant.CURRENT_TAG = urlConstants.NOTIFICATION_TAG;
////            volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.NOTIFICATION_URL, params);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    /*  private void initChart(PieChart mChart) {

        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.YELLOW);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(GuestHome.this);

        setData(mChart);

        mChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(1000, 0, 360, Easing.EasingOption.EaseInOutQuad);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.TRANSPARENT);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    private void setData(PieChart mChart) {


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

         NOTE: The order of the entries when being added to the entries array determines their position around the center of
        the chart.
        for (int i = 1; i <= count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
                    "1",
                    getResources().getDrawable(R.drawable.star)));
       entries.add(new PieEntry((20), "1 judment"));
        entries.add(new PieEntry((40), "2 hearing"));
        entries.add(new PieEntry((25), "3 case serch"));
        entries.add(new PieEntry((15), " file case"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(1f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
       mChart.setData(data);

         undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }*/


}
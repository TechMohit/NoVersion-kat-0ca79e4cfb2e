package cms.co.in.kat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.activity.CaseList;
import cms.co.in.kat.activity.CaseTrack;
import cms.co.in.kat.activity.CauseList;
import cms.co.in.kat.activity.CourtLcd;
import cms.co.in.kat.activity.HearingDate;
import cms.co.in.kat.activity.Judgement;
import cms.co.in.kat.activity.LoginHome;
import cms.co.in.kat.activity.MyCases;
import cms.co.in.kat.activity.SittingList;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.CountAnimationTextView;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.eventbus.AssignmentFloating;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment implements View.OnClickListener {

    private LinearLayout cvCaseTrack, cvCauseList, cvJudgement, cvSittingList, cvLCD, cvCaseList;
    private GeneralUtilities gu;
    private CountAnimationTextView txt_count, txt_count1;
    //    private PieChart mChart, mChart1;
//    private Button save, save1;
    private LoginHome a;
    private ShowDilog dilog = new ShowDilog();
    private Volley v;
    //    private FloatingActionButton caseExp, caseTemp, casePer;
//    private FloatingActionsMenu fAm;
//    private RelativeLayout framelay;
    private NestedScrollView nestedScrolling;
    private ArrayList<String> caseExpList = new ArrayList<>();
    private ArrayList<String> caseTempList = new ArrayList<>();
    private ArrayList<String> casePerList = new ArrayList<>();

    private CoordinatorLayout parentLayout;
    private DatabaseHandler db;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static String userid;

    private ImageView codinateImage;
    @NonNull
    private URLConstants urlConstants = new URLConstants();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initLayout(v);
        gu = new GeneralUtilities(a);
        initFont();
        initListner();
//        initChart(mChart);
        SharedPreferences prefs = a.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        userid = prefs.getString(Constant.MY_PREF_USERID, null);
        swipeToRefresh();

        if (userid == null) {
            a.finish();
        }

//        caseExp.setIcon(R.drawable.discardcase);
//        casePer.setIcon(R.drawable.permanentcase);
//        caseTemp.setIcon(R.drawable.temporarycase);
//
//        caseExp.setTitle("Discarded Cases (0)");
//        casePer.setTitle("Permanent Cases (0)");
//        caseTemp.setTitle("Temporary Cases (0)");

        if (caseExpList != null && caseExpList.size() > 0) {
            caseExpList.clear();
        }
        if (caseTempList != null && caseTempList.size() > 0) {
            caseTempList.clear();
        }
        if (casePerList != null && casePerList.size() > 0) {
            casePerList.clear();
        }
        db = new DatabaseHandler(a);
        checkResult();
        a.toolBarName("Home");
        return v;

    }

    private void swipeToRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                HashMap<String, String> params = new HashMap<>();
                params.put("userID", userid);
                Constant.CURRENT_TAG = urlConstants.LOGIN_DETAILS_TAG;
                volly(urlConstants.LOGIN_DETAILS_TAG, Request.Method.POST, urlConstants.LOGIN_DETAILS_URL, params);
            }
        });
    }

    private void checkResult() {

        HashMap<String, String> params = new HashMap<>();
        params.put("userID", userid);

        if (db.getDetailsCheck(a)) {
            Constant.CURRENT_TAG = urlConstants.LOGIN_DETAILS_TAG;
            volly(urlConstants.LOGIN_DETAILS_TAG, Request.Method.POST, urlConstants.LOGIN_DETAILS_URL, params);
        } else {
            String response = db.getDetailsAll(a);
            if (response != null && !response.equalsIgnoreCase("")) {
                getDetailsJson(response);
            } else {
                Constant.CURRENT_TAG = urlConstants.LOGIN_DETAILS_TAG;
                volly(urlConstants.LOGIN_DETAILS_TAG, Request.Method.POST, urlConstants.LOGIN_DETAILS_URL, params);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            a = (LoginHome) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initValues(int totalCount, int judgementCount) {


        if (a != null) {
            Log.e("****", "aati ky khandalaaa " + totalCount + "  " + judgementCount);
            txt_count
                    .setAnimationDuration(this.getResources().getInteger(R.integer.case_count_anim))
                    .countAnimation(0, judgementCount);
            txt_count1
                    .setAnimationDuration(this.getResources().getInteger(R.integer.case_count_anim))
                    .countAnimation(0, totalCount);

            txt_count.invalidate();
            txt_count1.invalidate();
        }
    }

    private void initListner() {
        cvCaseTrack.setOnClickListener(this);
        cvCauseList.setOnClickListener(this);
        cvJudgement.setOnClickListener(this);
        cvSittingList.setOnClickListener(this);
        cvLCD.setOnClickListener(this);
        cvCaseList.setOnClickListener(this);
//        caseExp.setOnClickListener(this);
//        caseTemp.setOnClickListener(this);
//        casePer.setOnClickListener(this);
//        save.setOnClickListener(this);
//        mChart.setOnChartValueSelectedListener(this);

//        nestedScrolling.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e("**","*** scrolling nested");
//                if(fAm.isExpanded()){
//                    fAm.collapse();
//                }else{
//                    fAm.expand();
//                }
//            }
//        });

    }

    private void initFont() {
    }

    private void initLayout(@NonNull View v) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layout);

        codinateImage = (ImageView) v.findViewById(R.id.codinateImage);

        parentLayout = (CoordinatorLayout) v.findViewById(R.id.coordinator);
        txt_count = (CountAnimationTextView) v.findViewById(R.id.count);
        txt_count1 = (CountAnimationTextView) v.findViewById(R.id.count1);
//        save = (Button) v.findViewById(R.id.save);
//        save1 = (Button) v.findViewById(R.id.save1);
//        mChart = (PieChart) v.findViewById(R.id.chart);
//        mChart1 = (PieChart) v.findViewById(R.id.chart1);

        cvCaseTrack = (LinearLayout) v.findViewById(R.id.cvCaseTrack);
        cvCauseList = (LinearLayout) v.findViewById(R.id.cvCauseList);
        cvJudgement = (LinearLayout) v.findViewById(R.id.cvJudgement);
        cvSittingList = (LinearLayout) v.findViewById(R.id.cvSittingList);
        cvLCD = (LinearLayout) v.findViewById(R.id.cvLCD);
        cvCaseList = (LinearLayout) v.findViewById(R.id.cvCaseList);

//        casePer = (FloatingActionButton) v.findViewById(R.id.case_per);
//        caseTemp = (FloatingActionButton) v.findViewById(R.id.case_temp);
//        caseExp = (FloatingActionButton) v.findViewById(R.id.case_exp);
//        framelay = (RelativeLayout) v.findViewById(R.id.framelay);
////        fAm = new FloatingActionsMenu(a);
//        fAm = (FloatingActionsMenu) v.findViewById(R.id.multiple_actions);

        nestedScrolling = (NestedScrollView) v.findViewById(R.id.nestedScrolling);

    }


//    private void initChart(PieChart mChart) {
//
////        mChart.setDrawGridBackground(false);
//
//        // no description text
//        mChart.setUsePercentValues(true);
//        mChart.getDescription().setEnabled(false);
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//        mChart.setDragDecelerationFrictionCoef(0.95f);
//
////        mChart.setCenterTextTypeface(mTfLight);
////        mChart.setCenterText(generateCenterSpannableText());
//
//        mChart.setDrawHoleEnabled(false);
////        mChart.setHoleColor(Color.WHITE);
//
//        mChart.setTransparentCircleColor(Color.YELLOW);
//        mChart.setTransparentCircleAlpha(110);
//
//        mChart.setHoleRadius(58f);
//        mChart.setTransparentCircleRadius(61f);
//
//        mChart.setDrawCenterText(true);
//
//        mChart.setRotationAngle(0);
//        // enable rotation of the chart by touch
//        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);
//
//        // mChart.setUnit(" €");
//        // mChart.setDrawUnitsInChart(true);
//
//        // add a selection listener
////        mChart.setOnChartValueSelectedListener(a);
//
//        setData(mChart);
//
//        mChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
////        mChart.spin(1000, 0, 360, Easing.EasingOption.EaseInOutQuad);
//
//
//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//
//        // entry label styling
//        mChart.setEntryLabelColor(Color.TRANSPARENT);
////        mChart.setEntryLabelTypeface(mTfRegular);
//        mChart.setEntryLabelTextSize(12f);
//    }
//
//    private void setData(PieChart mChart) {
//
//
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
////        for (int i = 1; i <= count ; i++) {
////            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
////                    "1",
////                    getResources().getDrawable(R.drawable.star)));
//        entries.add(new PieEntry((20), "1 judment"));
//        entries.add(new PieEntry((40), "2 hearing"));
//        entries.add(new PieEntry((25), "3 case serch"));
//        entries.add(new PieEntry((15), " file case"));
////        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "");
//
//        dataSet.setDrawIcons(false);
//
//        dataSet.setSliceSpace(1f);
//        dataSet.setIconsOffset(new MPPointF(0, 40));
//        dataSet.setSelectionShift(5f);
//
//        // add a lot of colors
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
//        //dataSet.setSelectionShift(0f);
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.BLACK);
////        data.setValueTypeface(mTfLight);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValues(null);
//
//        mChart.invalidate();
//    }

    @Override
    public void onClick(@NonNull View v) {

        if (ShowDilog.internet) {
            Intent i;
            switch (v.getId()) {

                case R.id.cvCaseTrack:

                    i = new Intent(a, CaseTrack.class);
                    startActivity(i);
                    break;
                case R.id.cvCauseList:

                    i = new Intent(a, CauseList.class);
                    startActivity(i);
                    break;
                case R.id.cvJudgement:

                    i = new Intent(a, Judgement.class);
                    startActivity(i);
                    break;
                case R.id.cvSittingList:

                    i = new Intent(a, SittingList.class);
                    startActivity(i);
                    break;
                case R.id.cvLCD:

                    i = new Intent(a, CourtLcd.class);
                    startActivity(i);
                    break;
                case R.id.cvCaseList:

                    i = new Intent(a, MyCases.class);
                    startActivity(i);
//                    i = new Intent(a, HearingDate.class);
//                    startActivity(i);

                    break;

//                case R.id.case_exp:
//
//                    if (caseExpList != null && caseExpList.size() > 0) {
//
////                        fabHide();
//
//                        i = new Intent(a, CaseList.class);
//                        i.putExtra("caseType", Constant.CASE_DISCARD);
//                        a.startActivity(i);
//                    } else {
//                        GeneralUtilities.showMessage(a, parentLayout, "Empty");
//                    }
//                    break;
//
//                case R.id.case_per:
//                    if (casePerList != null && casePerList.size() > 0) {
//
////                        fabHide();
//
//                        i = new Intent(a, CaseList.class);
//                        i.putExtra("caseType", Constant.CASE_PER);
//                        a.startActivity(i);
//                    } else {
//                        GeneralUtilities.showMessage(a, parentLayout, "Empty");
//                    }
//
//                    break;
//                case R.id.case_temp:
//
//                    if (caseTempList != null && caseTempList.size() > 0) {
////                        fabHide();
//                        i = new Intent(a, CaseList.class);
//                        i.putExtra("caseType", Constant.CASE_TEMP);
//
//                        a.startActivity(i);
//                    } else {
//                        GeneralUtilities.showMessage(a, parentLayout, "Empty");
//                    }
//                    break;
//            case R.id.save:
                // mChart.saveToPath("title"+System.currentTimeMillis());
//                mChart.saveToGallery("KAT" + System.currentTimeMillis(),"","KAT", Bitmap.CompressFormat.JPEG,100);
//                if (mChart.saveToGallery("KAT" + System.currentTimeMillis(), "KAT_ChartPics", "KAT Graph", Bitmap.CompressFormat.JPEG, 100)) {
//                    Toast.makeText(a, "Saving SUCCESSFUL!",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(a, "Saving FAILED!", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            case R.id.btnPdf:
//
//                openFilePDFFromInstalledApp();
//                break;
            }
        } else {

            GeneralUtilities.showMessage(a, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(a).execute();

        }
    }
    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        v = new Volley(getActivity());

        v.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, String response) {
                Log.e("****", "aati ky khandalaaa " + tag + "  " + result + "  " + response);
                mSwipeRefreshLayout.setRefreshing(false);

                if (tag == urlConstants.LOGIN_DETAILS_TAG) {
                    Log.d(tag, "" + response);
                    if (result == 1) {
                        try {
//                            response = temp;
                            mSwipeRefreshLayout.setEnabled(false);
                            db.getDetailsInsert(a, response);
                            getDetailsJson(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                            mSwipeRefreshLayout.setEnabled(true);
                            GeneralUtilities.showMessage(a, parentLayout, "Try Again");
                        }
                    } else {
                        mSwipeRefreshLayout.setEnabled(true);
                        GeneralUtilities.showMessage(a, parentLayout, "" + response);
                    }
                }
            }

            @Override
            public void onVolleyError(@Nullable String result) {
                Log.e("****", "uiimaaaa " + result);
                mSwipeRefreshLayout.setRefreshing(false);

                GeneralUtilities.showMessage(a, parentLayout, "Please Try Again");
//                if (tag == urlConstants.STATIC_DETAIL_TAG) {
//                    callVersion();
//                }
            }
        });
    }

    private void getDetailsJson(String response) {

        try {
            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
            try {
                JSONObject countJsonObject = jArr.getJSONObject(0).getJSONObject("profile");
                String firstName = countJsonObject.getString("firstName");
                String middleNmae = countJsonObject.getString("middleNmae");
                String lastNmae = countJsonObject.getString("lastNmae");
                String userName = firstName + " " + middleNmae + " " + lastNmae;
                String userEmail = countJsonObject.getString("emailId");
                String userPhone = "";
                try {
                    userPhone = countJsonObject.getString("mobileNumber");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                a.setProfile(userName, userEmail, userPhone);

            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject countJsonObject = jArr.getJSONObject(0).getJSONObject("count");
            try {
                int totalCount = countJsonObject.getInt("totalCount");
                int judgementCount = countJsonObject.getInt("judgementCount");
                initValues(totalCount, judgementCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                int version = Integer.parseInt(countJsonObject.getString("version"));
                if (version > GeneralUtilities.getVersionCode(a)) {
                    dilog.createDialog(a, Constant.DILOG_UPDATE, new DialogListener() {
                        @Override
                        public void onFinish(int code, String user_id) {
//                                        new GeneralUtilities(LoginHome.this).showToastMessage(code+" - "+user_id);
                        }
                    });
                    SharedPreferences.Editor editor = a.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
                    editor.putString(Constant.MY_PREF_USERNAME, null);
                    editor.putString(Constant.MY_PREF_USERID, null);
                    editor.apply();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

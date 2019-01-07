package cms.co.in.kat.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.adapters.CourtLcdAdapter;
import cms.co.in.kat.adapters.CourtLcdNextAdapter;
import cms.co.in.kat.adapters.CourtLcdPassAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.demono.AutoScrollViewPager;
import cms.co.in.kat.customview.faboption.FloatingActionButton;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

import static android.R.attr.data;

public class CourtLcd extends AppCompatActivity {

    private List<JSONArray> maiList = new ArrayList<JSONArray>();
    private List<JSONObject> nextHearingList = new ArrayList<JSONObject>();
    private List<JSONObject> passOverList = new ArrayList<JSONObject>();
    private CourtLcdNextAdapter mAdapterNextHearing;
    private CourtLcdPassAdapter mAdapterPassOver;
    private CourtLcdAdapter mAdapter;
    private AutoScrollViewPager viewPagerNextHearing, viewPagerPassedOver, viewPager_main;
    private Volley vl;
    private RelativeLayout parentLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @NonNull
    private URLConstants urlConstants = new URLConstants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_court_lcd);

        initLayout();
        loadAdapter();
        swipeToRefresh();

        Constant.CURRENT_TAG = urlConstants.COURT_LCD_TAG;
        volly(Constant.CURRENT_TAG, Request.Method.GET, urlConstants.COURT_LCD_URL, null);
    }

    private void swipeToRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Constant.CURRENT_TAG = urlConstants.COURT_LCD_TAG;
                volly(Constant.CURRENT_TAG, Request.Method.GET, urlConstants.COURT_LCD_URL, null);
            }
        });
    }

    private void retriveJson(String response) {

//        response=temp;

        try {
            if (nextHearingList != null && nextHearingList.size() > 0) {
                nextHearingList.clear();
            }
            if (passOverList != null && passOverList.size() > 0) {
                passOverList.clear();
            }

            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
            JSONArray nextHearingArray = data_result_array.getJSONObject(0).getJSONArray("nextHearingDate");
            for (int i = 0; i < nextHearingArray.length(); i++) {
                nextHearingList.add(nextHearingArray.getJSONObject(i));
            }
            if (nextHearingList != null && nextHearingList.size() > 0) {
                mAdapterNextHearing = new CourtLcdNextAdapter(CourtLcd.this, nextHearingList);
                viewPagerNextHearing.setAdapter(mAdapterNextHearing);
                viewPagerNextHearing.startAutoScroll();
            }

            JSONArray passOverArray = data_result_array.getJSONObject(0).getJSONArray("passedOverCases");
            for (int i = 0; i < passOverArray.length(); i++) {
                passOverList.add(passOverArray.getJSONObject(i));
            }
            if (passOverList != null && passOverList.size() > 0) {

                mAdapterPassOver = new CourtLcdPassAdapter(CourtLcd.this, passOverList);
                viewPagerPassedOver.setAdapter(mAdapterPassOver);
                viewPagerPassedOver.startAutoScroll();
            }
            JSONArray mainArray = data_result_array.getJSONObject(0).getJSONArray("mainScreen");
//            for (int i = 0; i < mainArray.length(); i++) {
            maiList.add(mainArray);
//            }
            if (maiList != null && maiList.size() > 0) {

                mAdapter = new CourtLcdAdapter(CourtLcd.this, maiList);
                viewPager_main.setAdapter(mAdapter);
                viewPager_main.startAutoScroll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAdapter() {


//        viewPagerPassedOver.setAdapter(mAdapterNextHearing);
//        viewPagerPassedOver.startAutoScroll();
    }

    private void initLayout() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        viewPager_main = (AutoScrollViewPager) findViewById(R.id.viewPager_main);
        viewPagerNextHearing = (AutoScrollViewPager) findViewById(R.id.viewPager_nxt_hearing);
        viewPagerPassedOver = (AutoScrollViewPager) findViewById(R.id.viewPager_pass_over);
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
                mSwipeRefreshLayout.setRefreshing(false);

                if (tag == urlConstants.COURT_LCD_TAG) {
                    Log.d(urlConstants.COURT_LCD_TAG, "result code " + response);
                    if (result == 1) {
                        retriveJson(response);
                    } else {
                        GeneralUtilities.showMessage(CourtLcd.this, parentLayout, " Please try again");
                    }
                }
            }


            @Override
            public void onVolleyError(String result) {
                mSwipeRefreshLayout.setRefreshing(false);
                GeneralUtilities.showMessage(CourtLcd.this, parentLayout, " Please try again");
            }
        });
    }

    String temp = "{\n" +
            "  \"message\": \"Success\",\n" +
            "  \"status\": \"1\",\n" +
            "  \"data_result\": [\n" +
            "    {\n" +
            "      \"nextHearingDate\": [\n" +
            "        {\n" +
            "          \"cases\": [\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-766/2009\",\n" +
            "              \"nhd\": \"20/09/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.RVN-47/2010\",\n" +
            "              \"nhd\": \"04/07/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-309/2010\",\n" +
            "              \"nhd\": \"20/09/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-388/2013\",\n" +
            "              \"nhd\": \"12/09/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-459/2013\",\n" +
            "              \"nhd\": \"30/08/2017\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"name\": \"Court Hall 1 Pre\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-943/2008\",\n" +
            "              \"nhd\": \"12/07/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"REV.APL-117/2009\",\n" +
            "              \"nhd\": \"10/07/2017\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"name\": \"Court Hall 2\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [],\n" +
            "          \"name\": \"Court Hall 2 Pre\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [],\n" +
            "          \"name\": \"Court Hall 3\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [\n" +
            "            {\n" +
            "              \"caseNo\": \"ST.APL-908/2016\",\n" +
            "              \"nhd\": \"03/08/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"ST.APL-106/2017\",\n" +
            "              \"nhd\": \"04/08/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"ST.APL-126/2017\",\n" +
            "              \"nhd\": \"07/08/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"ST.APL-127/2017\",\n" +
            "              \"nhd\": \"07/08/2017\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"caseNo\": \"ST.APL-128/2017\",\n" +
            "              \"nhd\": \"07/08/2017\"\n" +
            "            }\n" +
            "          ],\n" +
            "          \"name\": \"Court Hall 4\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [],\n" +
            "          \"name\": \"Court Hall 5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [],\n" +
            "          \"name\": \"Court Hall 6\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cases\": [],\n" +
            "          \"name\": \"Court Hall 8\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"mainScreen\": [\n" +
            "        {\n" +
            "          \"courtHallId\": \"1345181\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"COP Belagavi\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courtHallId\": \"953339\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"COP Mangaluru\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courtHallId\": \"21283\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"Court Hall 1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"772229\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 1 Pre\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21284\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 2\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"772232\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 2 Pre\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21285\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 3\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21286\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 4\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21287\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 5\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21288\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 6\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courtHallId\": \"21289\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"Court Hall 7\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"currentCaseNo\": \"\",\n" +
            "          \"courtHallId\": \"21290\",\n" +
            "          \"status\": \"Active\",\n" +
            "          \"courtName\": \"Court Hall 8\",\n" +
            "          \"currentSlNo\": \"\",\n" +
            "          \"currentCaseLevel\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courtHallId\": \"857620\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"REV Mangaluru\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courtHallId\": \"953344\",\n" +
            "          \"status\": \"InActive\",\n" +
            "          \"courtName\": \"ST Mangaluru\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"passedOverCases\": [\n" +
            "        {\n" +
            "          \"courtHallId\": \"\",\n" +
            "          \"status\": \"\",\n" +
            "          \"courtName\": \"\",\n" +
            "          \"passedOut\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}

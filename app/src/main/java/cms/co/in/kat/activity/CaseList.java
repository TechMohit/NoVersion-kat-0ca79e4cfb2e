package cms.co.in.kat.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.adapters.CaseListAdapter;
import cms.co.in.kat.adapters.SittingListAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.fragment.FragmentHome;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

import static cms.co.in.kat.activity.CaseTrack.multiListCase;

public class CaseList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvSittingList;
    private CaseListAdapter adapter;
    private ArrayList<String> sittingList, sittingListUrls;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    //    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
    private EditText etDate;
    private Volley v;
    private URLConstants urlConst = new URLConstants();
    private String click = "";
    private String clicknxt = "";
    private int caseType;
    private Button one, two, three, four, five, six;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment,footer;
    private String msection = "", mcourthall = "", mbranch = "", mcaseType = "", mdistrict = "", mappellantName = "",
            mrespondentName = "", madvocateName = "", mstartDate = "", mendDate = "", mlcoNumber = "", mlcoAuthority = "";
    private RelativeLayout parentLayout,bottomTabLay;
    private ObjectAnimator animation, animation2;
    private boolean isEntered = false;
    private int i = 10;
    private int j = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);

        getCaseType();
        initLayout();
        footerListner();
        initListner();
//        scrollUpDownRecycleview();

//         if (caseType == Constant.CASE_IN_COURT) {
//            adapter = new CaseListAdapter(this, FragmentHome.inCourt);
//
//        } else if (caseType == Constant.CASE_MY_CASES) {
//            adapter = new CaseListAdapter(this, FragmentHome.myCases);
//
//        } else if (caseType == Constant.CASE_DISCARD) {
//            adapter = new CaseListAdapter(this, FragmentHome.caseExpList);
//
//        } else if (caseType == Constant.CASE_PER) {
//            adapter = new CaseListAdapter(this, FragmentHome.casePerList);
//
//        } else if (caseType == Constant.CASE_TEMP) {
//            adapter = new CaseListAdapter(this, FragmentHome.caseTempList);
//
//        } else

         if (caseType == Constant.MULTI_CASE_SEARCH) {
            adapter = new CaseListAdapter(this, multiListCase);
        }
        Log.d("loadtes","leng"+ CaseTrack.multiListCase.size());
        if(CaseTrack.multiListCase.size()<10)
        {
            one.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSittingList.setLayoutManager(mLayoutManager);
        rvSittingList.setItemAnimator(new DefaultItemAnimator());
        rvSittingList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void initListner() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        /*three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
*/
    }

    private void getCaseType() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            caseType = extras.getInt("caseType");
            msection = extras.getString("msection");
            mcourthall = extras.getString("mcourthall");
            mbranch = extras.getString("mbranch");
            mcaseType = extras.getString("mcaseType");
            mdistrict = extras.getString("mdistrict");
            mappellantName = extras.getString("mappellantName");
            mrespondentName = extras.getString("mrespondentName");
            madvocateName = extras.getString("madvocateName");
            mstartDate = extras.getString("mstartDate");
            mendDate = extras.getString("mendDate");
            mlcoNumber = extras.getString("mlcoNumber");
            mlcoAuthority = extras.getString("mlcoAuthority");

        }
    }



    private void initLayout() {
        parentLayout = (RelativeLayout) findViewById(R.id.coordinator);
        footer = (LinearLayout) findViewById(R.id.footer);
        bottomTabLay= (RelativeLayout) findViewById(R.id.bottomTabLay);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        rvSittingList = (RecyclerView) findViewById(R.id.rv_sitting_list);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        /*three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);*/

    }
    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CaseList.this, CaseTrack.class);
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
                Intent i = new Intent(CaseList.this, Judgement.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onClick(View v) {

        if (ShowDilog.internet) {
            HashMap<String, String> params = new HashMap<>();

            switch (v.getId()) {
                case R.id.one:

                    if(clicknxt.equals("last"))
                    {
                        i=i-20;
                        j=j-20;
                        params.put("startValue", ""+i);
                        params.put("endValue", ""+j);
                        clicknxt ="";

                        two.setVisibility(View.VISIBLE);

                    }



                    else {
                        if ((i == 10) & (j == 20)) {
                            one.setVisibility(View.GONE);
                            two.setVisibility(View.VISIBLE);
                        } else {
                            i = i - 20;
                            j = j - 20;
                            params.put("startValue", "" + i);
                            params.put("endValue", "" + j);
                            click = "1";

                            two.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case R.id.two:
                    click = "2";
                    params.put("startValue", ""+i);
                    params.put("endValue", ""+j);
                    one.setVisibility(View.VISIBLE);


                    break;
                /*case R.id.three:
                    click = "3";
                    params.put("startValue", "30");
                    params.put("endValue", "40");
                    break;
                case R.id.four:
                    click = "4";
                    params.put("startValue", "40");
                    params.put("endValue", "50");
                    break;
                case R.id.five:
                    click = "5";
                    params.put("startValue", "50");
                    params.put("endValue", "60");
                    break;
                case R.id.six:
                    click = "6";
                    params.put("startValue", "60");
                    params.put("endValue", "70");
                    break;*/
            }


            params.put("section", msection);
            params.put("courtHall", mcourthall);
            params.put("caseNumber", "");
            params.put("branch", mbranch);
            params.put("caseType", mcaseType);
            params.put("district", mdistrict);
            params.put("appellantName", mappellantName);
            params.put("respondentName", mrespondentName);
            params.put("advocateName", madvocateName);
            params.put("startDate", mstartDate);
            params.put("endDate", mendDate);
            params.put("lcoNumber", mlcoNumber);
            params.put("lcoAuthority", mlcoAuthority);
            params.put("memberName", "");

            Constant.CURRENT_TAG = urlConst.CASE_SEARCH_TAG;
            volly(urlConst.CASE_SEARCH_TAG, Request.Method.POST, urlConst.CASE_SEARCH_URL, params);
        } else {
            GeneralUtilities.showMessage(CaseList.this, parentLayout, getResources().getString(R.string.internet));

        }
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        v = new Volley(this);

        v.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, String response) {

                if (tag == urlConst.CASE_SEARCH_TAG) {

                    Log.d(urlConst.CASE_SEARCH_TAG, "result code " + response);
                    if (result == 1) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            multiListCase.clear();

                            for (int i = 0; i < data_result_array.length(); i++) {
                                multiListCase.add("" + data_result_array.getJSONObject(i).get("caseId"));

                            }
                            /*if (click.equalsIgnoreCase("1")) {
                                one.setEnabled(false);
                                one.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));

                            } else if (click.equalsIgnoreCase("2")) {
                                two.setEnabled(false);
                                two.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));
                            } else if (click.equalsIgnoreCase("3")) {
                                three.setEnabled(false);
                                three.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));

                            } else if (click.equalsIgnoreCase("4")) {
                                four.setEnabled(false);
                                four.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));

                            } else if (click.equalsIgnoreCase("5")) {
                                five.setEnabled(false);
                                five.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));

                            } else if (click.equalsIgnoreCase("6")) {
                                six.setEnabled(false);
                                six.getBackground().setAlpha(CaseList.this.getResources().getInteger(R.integer.paging_alpha));

                            }*/

                            adapter.updateCaselist(multiListCase);
                            adapter.notifyDataSetChanged();
                            i = i+10;
                            j = j+10;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        clicknxt ="last";
                        two.setVisibility(View.GONE);
                        if ((i == 10) & (j == 20)) {
                            two.setVisibility(View.VISIBLE);
                        }

                        GeneralUtilities.showMessage(CaseList.this, parentLayout, "No Record Found");

                    }
                }
            }

            @Override
            public void onVolleyError(String result) {

                Log.d(urlConst.CASE_SEARCH_TAG, "" + result);
                GeneralUtilities.showMessage(CaseList.this, parentLayout, "Please Try Again");
            }
        });
    }
//    private void scrollUpDownRecycleview() {
//        rvSittingList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // scroll up
//                    hideBottomLay();
//
//
//                } else {
//                    // Scrolling down
//                    showBottomLay();
//
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // scroll started
//                } else {
//                    // done with scrolling
//
//                }
//            }
//        });
//    }

    public void hideBottomLay() {
//        if(bottomlay.getVisibility()==View.VISIBLE) {

        if (!isEntered) {


            if (animation == null) {
                animation = ObjectAnimator.ofFloat(bottomTabLay, "translationY", 0, footer.getHeight());
                animation2 = ObjectAnimator.ofFloat(bottomTabLay, "translationY", 0);

            }

            animation.setDuration(300);
            animation.setRepeatCount(0);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());

            if (!animation2.isRunning()) {
                animation.start();
                isEntered = true;

            }
        }
    }

    public void showBottomLay() {
        if (isEntered) {

            if (animation2 == null) {
                animation2 = ObjectAnimator.ofFloat(bottomTabLay, "translationY", 0);
            }
            animation2.setDuration(300);
            animation2.setRepeatCount(0);
            animation2.setInterpolator(new AccelerateDecelerateInterpolator());

            if (!animation.isRunning()) {
                animation2.start();
                isEntered = false;
            }
        }
    }
}

package cms.co.in.kat.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.adapters.CaseSearchAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.CaseInfo;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

public class CaseTrackHeader extends AppCompatActivity {

    @Nullable
    private String caseno = "";
    @NonNull
    private URLConstants urlConst = new URLConstants();
    private RecyclerView recyclerView;
    @Nullable
    private CaseSearchAdapter adapter;
    private Button btn_click;
    //    public List<CaseInfo> allresultList;
    public static List<CaseInfo> allresultListDetail;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private RelativeLayout parentLayout;
    private Boolean error = false;
    private boolean isEntered = false;
    private Button btn_scroll,btn_jud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_case_track_header);

        getValue();
        initLayout();
        initCall();
        initListner();
        footerListner();
//        scrollUpDownRecycleview();
    }

    private void initLayout() {
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btn_click = (Button) findViewById(R.id.btn_click);
        btn_scroll=(Button)findViewById(R.id.btn_scroll);
        btn_jud=(Button)findViewById(R.id.btn_jud);
    }
    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CaseTrack.class);
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
                Intent i = new Intent(getApplicationContext(), Judgement.class);
                startActivity(i);
            }
        });
    }

    private void initListner() {
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!error) {
                    Intent i = new Intent(CaseTrackHeader.this, CaseTrackDetails.class);
                    startActivity(i);
                }
            }
        });

        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        btn_jud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(allresultListDetail.get(0).getJudgmentArry().size()>0) {
                        Intent i = new Intent(CaseTrackHeader.this, CaseTrackJudDetails.class);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getValue() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            error = true;
        } else {
            caseno = extras.getString("caseNo");
            Log.d("testcaseno",""+caseno);
        }
    }

    private void initCall() {
        HashMap<String, String> params = new HashMap<>();
        params.put("caseNumber", caseno);
        params.put("branch", "");
        params.put("section", "");
        params.put("caseType", "");
        params.put("appellantName", "");
        params.put("respondentName", "");
        params.put("advocateName", "");
        params.put("startDate", "");
        params.put("endDate", "");
        params.put("courtHall", "");
        params.put("district", "");
        params.put("lcoNumber", "");
        params.put("startValue", "0");
        params.put("endValue", "0");
        params.put("lcoAuthority", "");
        params.put("memberName", "");

        Constant.CURRENT_TAG = urlConst.CASE_SEARCH_TAG;
        volly(urlConst.CASE_SEARCH_TAG, Request.Method.POST, urlConst.CASE_SEARCH_URL, params);

    }

    private void setRecycleadapter(@Nullable ArrayList<HashMap> mallresultList) {

        adapter = new CaseSearchAdapter(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CaseTrackHeader.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        if (mallresultList != null) {
            adapter.updateEntry(mallresultList);
            adapter.notifyDataSetChanged();

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
            public void onVolleyRespondString(int result, String response) {

                if (tag == urlConst.CASE_SEARCH_TAG) {
//                    allresultList = new ArrayList<>();
                    allresultListDetail = new ArrayList<>();
                    ArrayList<HashMap> list = null;
                    ArrayList<HashMap> listJud = null;
                    ArrayList<String> listHearing = null;
                    ArrayList<String> procedingTree = null;
                    HashMap<String, String> procedingTreeCourtHall = null;

                    Log.d(urlConst.CASE_SEARCH_TAG, "result code " + response);
                    if (result == 1) {
                        try {
                            CaseInfo allValueDetail = new CaseInfo();

                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            for (int i = 0; i < data_result_array.length(); i++) {
                                JSONObject actor = data_result_array.getJSONObject(i);


                                if (!actor.isNull("provisionLaw"))
                                    allValueDetail.setProvisionlaw(actor.getString("provisionLaw"));

                                if (!actor.isNull("authPassOrder"))
                                    allValueDetail.setAuthPassOrder(actor.getString("authPassOrder"));

                                if (!actor.isNull("otherAuthPassOrder"))
                                    allValueDetail.setOtherAuthPassOrder(actor.getString("otherAuthPassOrder"));

                                if (!actor.isNull("dateEfiling"))
                                    allValueDetail.setDateOfEfiling(actor.getString("dateEfiling"));

                                if (!actor.isNull("caseLevel"))
                                    allValueDetail.setCaseLevel(actor.getString("caseLevel"));

                                if (!actor.isNull("caseFileWith"))
                                    allValueDetail.setCaseFileWith(actor.getString("caseFileWith"));

                                if (!actor.isNull("caseNo"))
                                    allValueDetail.setCaseNo(actor.getString("caseNo"));

                                if (!actor.isNull("lcoNo"))
                                    allValueDetail.setLcoNo(actor.getString("lcoNo"));

//                                if (!actor.isNull("provisionLco"))
//                                    allValueDetail.setProvisionLco(actor.getString("provisionLco"));

                                try {
                                    if (!actor.isNull("appelentInfo")) {
                                        JSONArray appelentInfoArray = actor.getJSONArray("appelentInfo");
                                        if (appelentInfoArray != null && appelentInfoArray.length() > 0) {
                                            JSONObject jobject1 = appelentInfoArray.getJSONObject(0);
                                            allValueDetail.setAppelentInfoName(jobject1.getString("name"));
                                            allValueDetail.setAppelentInfoAddress(jobject1.getString("address"));
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (!actor.isNull("respondentInfo")) {
                                        JSONArray respondentInfoArray = actor.getJSONArray("respondentInfo");
                                        if (respondentInfoArray != null && respondentInfoArray.length() > 0) {
                                            JSONObject jobject = respondentInfoArray.getJSONObject(0);
                                            allValueDetail.setRespondaneInfoName(jobject.getString("name"));
                                            allValueDetail.setRespondaneInfoAddress(jobject.getString("address"));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                listJud = new ArrayList<>();
                                if (!actor.isNull("judgement")) {
                                    JSONArray data_proce = actor.getJSONArray("judgement");
                                    for (int p = 0; p < data_proce.length(); p++) {
                                        JSONObject pro = data_proce.getJSONObject(p);
                                        HashMap<String, String> proListEach = new HashMap<>();
                                        proListEach.put("result",pro.getString("result"));
                                        proListEach.put("courtHall",pro.getString("courtHall"));
                                        proListEach.put("status",pro.getString("status"));
                                        proListEach.put("judmentDate",pro.getString("judmentDate"));
                                        listJud.add(proListEach);
                                    }
                                }

                                list = new ArrayList<>();
                                listHearing = new ArrayList<>();

                                procedingTree = new ArrayList<String>();
                                procedingTreeCourtHall = new HashMap<String, String>();

                                if (!actor.isNull("proceeding")) {
                                    JSONArray data_proce = actor.getJSONArray("proceeding");
                                    for (int p = 0; p < data_proce.length(); p++) {
                                        JSONObject pro = data_proce.getJSONObject(p);
                                        HashMap<String, String> proListEach = new HashMap<>();
                                        proListEach.put("slno",""+(p+1));
                                        if (!pro.isNull("status"))
                                            proListEach.put("status", pro.getString("status"));
                                        if (!pro.isNull("hearingDate")) {
                                            proListEach.put("hearingDate", pro.getString("hearingDate"));
                                            listHearing.add(pro.getString("hearingDate"));
//                                            procedingTree.remove(pro.getString("hearingDate"));
                                            procedingTree.add(pro.getString("hearingDate"));

                                        }
                                        if (!pro.isNull("procedingNote"))
                                            proListEach.put("procedingNote", pro.getString("procedingNote"));

                                        if (!pro.isNull("nextHearingDate")) {
                                            proListEach.put("nextHearingDate", pro.getString("nextHearingDate"));
//                                            procedingTree.remove(pro.getString("nextHearingDate"));
//                                            procedingTree.add(pro.getString("nextHearingDate"));
                                        }
                                        if (!pro.isNull("courtHall"))
                                            proListEach.put("courtHall", pro.getString("courtHall"));
                                        try {
                                            procedingTreeCourtHall.put(pro.getString("hearingDate"), pro.getString("courtHall"));
                                        } catch (Exception e) {

                                        }
                                        try {
                                            procedingTreeCourtHall.put(pro.getString("nextHearingDate"), pro.getString("courtHall"));
                                        } catch (Exception e) {

                                        }
                                        list.add(proListEach);
                                    }
                                }
                                allValueDetail.setProcedingArry(list);
                                allValueDetail.setJudgmentArry(listJud);
                                allValueDetail.setProcedingArryTree(procedingTree);
                                allValueDetail.setProdedingTreeHashmap(procedingTreeCourtHall);

                                allresultListDetail.add(allValueDetail);
                            }
                            error = false;
                            setRecycleadapter(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                            error = true;
                        }
                    } else {
                        GeneralUtilities.showMessage(CaseTrackHeader.this, parentLayout, response);
                        error = true;
//                        finish();
                    }
                }
            }

            @Override
            public void onVolleyError(String result) {
                error = true;
                Log.d(urlConst.CASE_SEARCH_TAG, "" + result);
            }
        });
    }

//    public void hideBottomLay() {
////        if(bottomlay.getVisibility()==View.VISIBLE) {
//
//        if (!isEntered) {
//
//
//            if (animation == null) {
//                animation = ObjectAnimator.ofFloat(parentLayout, "translationY", 0, footer.getHeight());
//                animation2 = ObjectAnimator.ofFloat(parentLayout, "translationY", 0);
//
//            }
//
//            animation.setDuration(300);
//            animation.setRepeatCount(0);
//            animation.setInterpolator(new AccelerateDecelerateInterpolator());
//
//            if (!animation2.isRunning()) {
//                animation.start();
//                isEntered = true;
//
//            }
//        }
//    }
//
//    public void showBottomLay() {
//        if (isEntered) {
//
//            if (animation2 == null) {
//                animation2 = ObjectAnimator.ofFloat(parentLayout, "translationY", 0);
//            }
//            animation2.setDuration(300);
//            animation2.setRepeatCount(0);
//            animation2.setInterpolator(new AccelerateDecelerateInterpolator());
//
//            if (!animation.isRunning()) {
//                animation2.start();
//                isEntered = false;
//            }
//
//        }
//
//    }
}

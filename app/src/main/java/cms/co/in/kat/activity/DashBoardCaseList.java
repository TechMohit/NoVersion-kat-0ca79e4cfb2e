package cms.co.in.kat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.adapters.CaseListDashAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.faboption.FloatingActionButton;
import cms.co.in.kat.customview.faboption.FloatingActionsMenu;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.eventbus.AssignmentFloating;
import cms.co.in.kat.fragment.FragmentHome;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.DashboardCaseListHolder;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

import static cms.co.in.kat.activity.CaseTrack.multiListCase;

public class DashBoardCaseList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvSittingList;
    private CaseListDashAdapter adapter;
    private String caseType;
    private RelativeLayout parentLayout;
    private TextView txt_head;
    private ArrayList<DashboardCaseListHolder> caseList;

    private URLConstants urlConstants = new URLConstants();
    private Volley v;
    private ImageView search;
    private EditText et_search;
    private InputMethodManager imm;
    private String caseNo;
    private String typeList;
    private String divisionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_dash_case_list);
        initLayout();
        getCaseType();
        initListner();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        HashMap<String, String> params = new HashMap<>();
        params.put("userID", FragmentHome.userid);

        params.put("resultType", caseType);

        Constant.CURRENT_TAG = urlConstants.DASHBOARD_CASE_DETAIL_TAG;
        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.DASHBOARD_CASE_DETAIL_URL, params);


    }

    private void initListner() {

        search.setOnClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    private void getCaseType() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            caseType = extras.getString("caseType");
            if (caseType.equalsIgnoreCase(Constant.PERMANENT_CASE)) {
                txt_head.setText("Permanent Cases");
            } else if (caseType.equalsIgnoreCase(Constant.TEMPORARYCASES)) {
                txt_head.setText("Temporary Cases");
            } else if (caseType.equalsIgnoreCase(Constant.DISCARDEDCASE)) {
                txt_head.setText("Case Discarded");
            } else if (caseType.equalsIgnoreCase(Constant.IN_COURT)) {
                txt_head.setText("In Court");
            } else if (caseType.equalsIgnoreCase(Constant.MY_CASES)) {
                txt_head.setText("My Cases");
                      } else if (caseType.equalsIgnoreCase(Constant.DEFECTCASES)) {
                txt_head.setText("Defect Cases");
            } else if (caseType.equalsIgnoreCase(Constant.NODEFECTCASES)) {
                txt_head.setText("No Defect Cases");
            }
        }
    }


    private void initLayout() {

        search = (ImageView) findViewById(R.id.search);
        et_search = (EditText) findViewById(R.id.et_search);
        parentLayout = (RelativeLayout) findViewById(R.id.coordinator);
//        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
//        footerHome = (LinearLayout) findViewById(R.id.footerHome);
//        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);
        txt_head = (TextView) findViewById(R.id.txt_head);
        rvSittingList = (RecyclerView) findViewById(R.id.rv_sitting_list);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search:

                et_search.setText("");
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                et_search.setCursorVisible(true);
                et_search.setVisibility(View.VISIBLE);
                int cx = (int) search.getX() + (search.getWidth() / 2);
                int cy = (int) search.getY() + (search.getWidth() / 2);
                int endRadius = (int) Math.hypot(cx, cy);
                Animator anim;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(et_search, cx, cy, 0, endRadius);
                    anim.start();
                }
                break;
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
                Log.e("****", "aati ky khandalaaa " + tag + "  " + result + "  " + response);

                if (tag == urlConstants.DASHBOARD_CASE_DETAIL_TAG) {
                    Log.d(tag, "" + response);
                    if (result == 1) {
                        getDetailsJson(response);
                    } else {
                        GeneralUtilities.showMessage(DashBoardCaseList.this, parentLayout, "" + response);
                    }
                }

            }

            @Override
            public void onVolleyError(@Nullable String result) {
                Log.e("****", "uiimaaaa " + result);
                finish();
                GeneralUtilities.showMessage(DashBoardCaseList.this, parentLayout, "Please Try Again");
//                if (tag == urlConstants.STATIC_DETAIL_TAG) {
//                    callVersion();
//                }
            }
        });
    }

    private void getDetailsJson(String response) {
        try {
            caseList = new ArrayList<DashboardCaseListHolder>();
            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
            Log.e("**result**", "***RESULT " + jArr.length());
            for (int i = 0; i < jArr.length(); i++) {
                DashboardCaseListHolder ds = new DashboardCaseListHolder();

                String caseNo = "", caseType = "", caseDivision = "";
                try {
                    caseNo = jArr.getJSONObject(i).getString("caseNo");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    caseType = jArr.getJSONObject(i).getString("caseType");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    caseDivision = jArr.getJSONObject(i).getString("caseDivision");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ds.setCaseNo(caseNo);
                ds.setTypeList(caseType);
                ds.setDivisionList(caseDivision);


                caseList.add(ds);
            }
            adapter = new CaseListDashAdapter(this, caseList);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            rvSittingList.setLayoutManager(mLayoutManager);
            rvSittingList.setItemAnimator(new DefaultItemAnimator());
            rvSittingList.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (et_search.getVisibility() == View.INVISIBLE) {
            super.onBackPressed();
            return;
        } else {
            int cx = (int) search.getX() + (search.getWidth() / 2);
            int cy = (int) search.getY() + (search.getWidth() / 2);
            int endRadius = (int) Math.hypot(cx, cy);
            Animator anim;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = ViewAnimationUtils.createCircularReveal(et_search, cx, cy, endRadius, 0);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        et_search.setVisibility(View.INVISIBLE);
                        et_search.setText("");
                    }
                });
                anim.start();
            } else {
                et_search.setVisibility(View.INVISIBLE);
                et_search.setText("");

            }
        }

    }


}

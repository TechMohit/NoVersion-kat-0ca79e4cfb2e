package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cms.co.in.kat.adapters.ExpandableListAdapter.ExpandableListAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.Hearing;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class CauseList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private Spinner spnCourtHall, spnCauseList;
    private TextView etDate, tvCourtNo, tvBenchMem, tvHearingDate;
    private Button btnView;
    @NonNull
    private String courtHall_sel = "Select Court Hall", camp_sel = "Select Camp",cause_list_type_sel="1";
    private ArrayList courtId, courtHall, causeList;

    @NonNull
    private List<Hearing> preList = new ArrayList<>();
    private List<String> groupList;
    @NonNull
    private List<String> childList = new ArrayList<>();
    private List<String> childListKey;

    private Map<String, List<String>> laptopCollection;
    private ExpandableListView expListView;
    private ExpandableListAdapter expListAdapter;
    private Volley v;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_cause_list);

        initLayout();
        initAdapter();

        footerListner();
        Constant.CURRENT_TAG = urlConstants.GET_COURT_HALLS_TAG;
        volly(urlConstants.GET_COURT_HALLS_TAG, Request.Method.GET, urlConstants.GET_COURT_HALLS_URL, null);

        expListAdapter = new ExpandableListAdapter(this);
        expListView.setDividerHeight(0);
        expListView.setAdapter(expListAdapter);

    }


    private void initLayout() {
        parentLayout = (RelativeLayout) findViewById(R.id.coordinator);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);


        etDate = (TextView) findViewById(R.id.et_date);
        etDate.setOnClickListener(this);

        spnCauseList = (Spinner) findViewById(R.id.spn_cause_list);
        spnCauseList.setOnItemSelectedListener(this);

        spnCourtHall = (Spinner) findViewById(R.id.spn_court_hall);
        spnCourtHall.setOnItemSelectedListener(this);

        btnView = (Button) findViewById(R.id.btn_view);
        btnView.setOnClickListener(this);

        expListView = (ExpandableListView) findViewById(R.id.exp_case_all);
        courtId = new ArrayList();
        courtHall = new ArrayList();
        causeList = new ArrayList();
        causeList.add("Main Cause List");
        causeList.add("Supplementary Cause List");

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


    private void initAdapter() {

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courtHall);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCourtHall.setAdapter(dataAdapter1);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, causeList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCauseList.setAdapter(dataAdapter2);
    }

    @Override
    public void onClick(@NonNull View v) {

        if (ShowDilog.internet) {
            switch (v.getId()) {
                case R.id.et_date:

                    new GeneralUtilities(this).setDate(etDate);

                    break;
                case R.id.btn_view:
                    String date = etDate.getText().toString().trim();
                    if (!date.equals("Click")) {
                        if (!courtHall_sel.equals("Select Court Hall") || !camp_sel.equals("Select Camp")) {

                            expListAdapter.updateValue(this, null, null, null);
                            expListAdapter.notifyDataSetChanged();
//                        setListViewHeight(expListView);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("hearingDate", date);
                            params.put("courtHall", courtHall_sel);
                            params.put("selectCauseList", cause_list_type_sel);
                            Constant.CURRENT_TAG = urlConstants.CAUSE_LIST_TAG;
                            volly(urlConstants.CAUSE_LIST_TAG, Request.Method.POST, urlConstants.CAUSE_LIST_URL, params);
                        } else {
                            GeneralUtilities.showMessage(CauseList.this, parentLayout, "Select Court Hall or Camp");
                        }
                    } else {
                        GeneralUtilities.showMessage(CauseList.this, parentLayout, "Select Hearing Date");

                    }
                    break;
                default:
                    break;
            }
        } else {
            GeneralUtilities.showMessage(CauseList.this, parentLayout, getResources().getString(R.string.internet));
        }
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        v = new Volley(CauseList.this);

        v.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, String response) {
                if (tag == urlConstants.CAUSE_LIST_TAG) {

                    if (preList != null && preList.size() > 0) {

                        preList.clear();
                    }
                    groupList = new ArrayList<>();
                    laptopCollection = new LinkedHashMap<>();

                    if (result == 1) {
                        CauseListResp(response);

                    } else {
                        GeneralUtilities.showMessage(CauseList.this, parentLayout, response);
                        return;
                    }
                } else if (tag == urlConstants.GET_COURT_HALLS_TAG) {
                    if (result == 1) {
                        CourtList(response);

                    } else {
                        GeneralUtilities.showMessage(CauseList.this, parentLayout, response);

                        return;
                    }
                }
            }

            @Override
            public void onVolleyError(@Nullable String result) {

                if (result != null) {
                    GeneralUtilities.showMessage(CauseList.this, parentLayout, "Please Try Again");
                }
            }
        });
    }

    private void CourtList(String response) {
        try {
            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
            int len = jArr.length();
            for (int i = 0; i < len; i++) {
                courtHall.add(jArr.getJSONObject(i).getString("name"));
                courtId.add(jArr.getJSONObject(i).getString("categoryId"));
            }
            initAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void CauseListResp(String response) {

        try {
            // response=temp;
            JSONArray jArryCaseLevel = new JSONObject(response).getJSONArray("data_result").getJSONObject(0).getJSONArray("caselevels");
            JSONObject jObj = new JSONObject(response).getJSONArray("data_result").getJSONObject(0).getJSONObject("data");

            if( jObj.length()<=0){
                GeneralUtilities.showMessage(CauseList.this,parentLayout,"No Data Found");
                return;
            }

            int resLenCaseL = jArryCaseLevel.length();
            ArrayList<String> preLevelList = new ArrayList<>();
            for (int i = 0; i < resLenCaseL; i++) {
                Hearing h = new Hearing();

                String caseLevels = jArryCaseLevel.getString(i);

                try {
                    JSONArray jArr = jObj.getJSONArray(caseLevels);

                    int preLen = jArr.length();
                    if (preLen > 0) {
                        preLevelList.add("" + caseLevels);
                        h.setCaseLevels(caseLevels);
                        h.setCaseLevelsCount(preLen);

                        ArrayList<String> value = new ArrayList<>();
                        for (int j = 0; j < preLen; j++) {
                            JSONObject obj = jArr.getJSONObject(j);
                            value.add(obj.getString("slNo"));
                            value.add(obj.getString("caseNo"));
                            value.add(obj.getString("appellantNames"));
                            value.add(obj.getString("appellantAdvocateNames"));
                            value.add(obj.getString("respondentNames"));
                            value.add(obj.getString("respondentAdvocateNames"));
                        }
                        h.setAllValue(value);
                        preList.add(h);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            childListKey = new ArrayList<>();

            for (int i = 0; i < preList.size(); i++) {
                Log.e("***", "*****" + preList.get(i).getCaseLevels());
                groupList.add(preList.get(i).getCaseLevels() + " (" + preList.get(i).getCaseLevelsCount() + ")");
                childList = new ArrayList<>();

                for (int j = 0; j < preList.get(i).getAllValue().size(); j++) {
                    Log.e("***", "*****" + preList.get(i).getAllValue().get(j));
                    childList.add(preList.get(i).getAllValue().get(j));
                    childListKey.add("Serial Number");
                    childListKey.add("Case Number");
                    childListKey.add("Appellant Name");
                    childListKey.add("Appellant Advocate Name");
                    childListKey.add("Respondent Name");
                    childListKey.add("Respondent Advocate Name");
                }
                laptopCollection.put(preList.get(i).getCaseLevels() + " (" + preList.get(i).getCaseLevelsCount() + ")", childList);
            }
            expListAdapter.updateValue(this, groupList, laptopCollection, childListKey);
            expListAdapter.notifyDataSetChanged();
//            setListViewHeight(expListView);

        } catch (JSONException e) {
            e.printStackTrace();
            GeneralUtilities.showMessage(CauseList.this, parentLayout, "Please Try Again");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spn_court_hall) {

            courtHall_sel = (String) courtId.get(position);
        }
        else if (spinner.getId() == R.id.spn_cause_list) {
            cause_list_type_sel =""+(position+1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

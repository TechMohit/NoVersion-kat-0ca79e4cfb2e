package cms.co.in.kat.activity;

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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.adapters.MyCasesAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.MyCasesItem;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

public class MyCases extends AppCompatActivity {

    private RecyclerView recycler_view;
    private MyCasesAdapter adapter;
    private ArrayList<String> myCasesList;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
    private EditText etDate;
    private Volley v;
    private List<MyCasesItem> listAll;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cases);
        initLayout();
        myCasesList = new ArrayList();
        footerListner();
        getMyCaseList();
        adapter = new MyCasesAdapter(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(adapter);
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyCases.this, CaseTrack.class);
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
                Intent i = new Intent(MyCases.this, Judgement.class);
                startActivity(i);
            }
        });
    }

    private void initLayout() {

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void getMyCaseList() {
        SharedPreferences sharepref = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String user_id = sharepref.getString(Constant.MY_PREF_USERID, null);

        HashMap<String, String> params = new HashMap<>();
        params.put("UserId", user_id);
        Constant.CURRENT_TAG = urlConstants.MY_CASES_TAG;
        volly(urlConstants.MY_CASES_TAG, Request.Method.POST, urlConstants.MY_CASES_URL, params);

    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        v = new Volley(MyCases.this);
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

                if (tag == urlConstants.MY_CASES_TAG) {
                    Log.d(tag, "" + response);
                    if (result == 1){
//                    finish();
                        try {
                            listAll = new ArrayList<>();
                            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
                            for (int i = 0; i < jArr.length(); i++) {
                                MyCasesItem mycase = new MyCasesItem();
                                String caseNumber = jArr.getJSONObject(i).getString("caseNumber");
                                if (caseNumber.equalsIgnoreCase("")) {
                                  //  mycase.setCaseNumber("Not Available");
                                } else {
                                    mycase.setCaseNumber("" + caseNumber);
                                }
                                String lcoProvisionOfLaw = jArr.getJSONObject(i).getString("lcoProvisionOfLaw");
                                if (lcoProvisionOfLaw.equalsIgnoreCase("")) {
                                  //  mycase.setLcoProvisionOfLaw("Not Available");
                                } else
                                    mycase.setLcoProvisionOfLaw("" + lcoProvisionOfLaw);
                                String lcoNo = jArr.getJSONObject(i).getString("lcoNo");
                                if (lcoNo.equalsIgnoreCase("")) {
                                   // mycase.setLcoNo("Not Available");
                                } else
                                    mycase.setLcoNo("" + lcoNo);
                                String division = jArr.getJSONObject(i).getString("division");
                                if (division.equalsIgnoreCase("")) {
                                  //  mycase.setDivision("Not Available");
                                } else
                                    mycase.setDivision("" + division);
                                String provisionOfLaw = jArr.getJSONObject(i).getString("provisionOfLaw");
                                if (provisionOfLaw.equalsIgnoreCase("")) {
                                  //  mycase.setProvisionOfLaw("Not Available");
                                } else
                                    mycase.setProvisionOfLaw(provisionOfLaw);
                                String caseId = jArr.getJSONObject(i).getString("caseId");
                                if (caseId.equalsIgnoreCase("")) {
                                  //  mycase.setCaseId("Not Available");
                                } else
                                    mycase.setCaseId(caseId);
                                String branch = jArr.getJSONObject(i).getString("branch");
                                if (branch.equalsIgnoreCase("")) {
                                   // mycase.setBranch("Not Available");
                                } else
                                    mycase.setBranch(branch);
                                String serveyNo = jArr.getJSONObject(i).getString("serveyNo");
                                if (serveyNo.equalsIgnoreCase("")) {
                                  //  mycase.setServeyno("Not Available");
                                } else
                                    mycase.setServeyno(serveyNo);
                                String locDate = jArr.getJSONObject(i).getString("lcoDate");
                                if (locDate.equalsIgnoreCase("")) {
                                  //  mycase.setLocDate("Not Available");
                                } else
                                    mycase.setLocDate(locDate);
                                String otherProvisionOfLaw = jArr.getJSONObject(i).getString("otherProvisionOfLaw");
                                if (otherProvisionOfLaw.equalsIgnoreCase("")) {
                                   // mycase.setOtherProvisionOfLaw("Not Available");
                                } else
                                    mycase.setOtherProvisionOfLaw(otherProvisionOfLaw);
                                String efillingDate = jArr.getJSONObject(i).getString("efillingDate");
                                if (efillingDate.equalsIgnoreCase("")) {
                                   // mycase.setEfillingDate("Not Available");
                                } else
                                    mycase.setEfillingDate(efillingDate);
                                String district = jArr.getJSONObject(i).getString("district");
                                if (district.equalsIgnoreCase("")) {
                                   // mycase.setDistrict("Not Available");
                                } else
                                    mycase.setDistrict(district);
                                Log.e("III", "" + caseNumber);
                                listAll.add(mycase);
                            }
                            adapter.updateEntry(listAll);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{

                    }
                }
            }

            @Override
            public void onVolleyError(@Nullable String result) {
                Log.e("****", "uiimaaaa " + result);

            }
        });
    }

}

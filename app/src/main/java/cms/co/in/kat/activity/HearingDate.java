package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cms.kat.cws.R;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

/**
 * Created by subham_naik on 5/11/2017.
 */

public class HearingDate extends AppCompatActivity {

    private EditText et1, et2, et3, et4;
    private Button search;
    @NonNull
    private String caseno;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private CoordinatorLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_date);

        initLayout();
        initListnr();
        footerListner();
    }

    private void initListnr() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caseno = validate();
                if (caseno != null && caseno.length() > 1) {

                    Intent i = new Intent(HearingDate.this, CaseTrackHeader.class);
                    i.putExtra("caseNo", caseno);
                    startActivity(i);
                } else {
                    GeneralUtilities.showMessage(HearingDate.this, parentLayout, "Please Enter a valid case Number");

                }
            }
        });
    }

    private String validate() {


        if (et1.getText().toString().equalsIgnoreCase("") || et2.getText().toString().equalsIgnoreCase("") ||
                et3.getText().toString().equalsIgnoreCase("") || et4.getText().toString().equalsIgnoreCase("")) {
            GeneralUtilities.showMessage(HearingDate.this, parentLayout, "Please Enter a Case Number");

        } else {
            caseno = et1.getText().toString() + "." + et2.getText().toString() + "-" + et3.getText().toString() + "/" + et4.getText().toString();
        }

        return caseno;
    }

    private void initLayout() {

        parentLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);

        search = (Button) findViewById(R.id.search);

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


//    private void volly(final String tag, int post, String link, HashMap<String, String> params) {
//
//        vl = new Volley(HearingDate.this);
//
//        vl.makeStringReq(tag, post, link, params, new VolleyListner() {
//            @Override
//            public void onVolleyRespondJSONObject(JSONObject result) {
//            }
//
//            @Override
//            public void onVolleyRespondJSONArray(JSONArray result) {
//            }
//
//            @Override
//            public void onVolleyRespondString(int result, String response) {
//
//                if (tag == urlConst.CASE_SEARCH_TAG) {
//                    if (result == 1) {
//                        Log.d(urlConst.CASE_SEARCH_TAG, "result response " + response);
//                        if (response.equalsIgnoreCase("")) {
//                            Toast.makeText(HearingDate.this, "Data Not Found", Toast.LENGTH_SHORT).show();
//                            emptyViewShow();
//                        } else {
//                            Toast.makeText(HearingDate.this, "Sucess", Toast.LENGTH_SHORT).show();
//                            try {
//
//                                List<CaseInfo> allresultList = new ArrayList<CaseInfo>();
//                                CaseInfo allValue = new CaseInfo();
//
//                                JSONObject jsonResponse = new JSONObject(response);
//                                JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
//                                for (int i = 0; i < data_result_array.length(); i++) {
//                                    JSONObject actor = data_result_array.getJSONObject(i);
//                                    allValue.setCaseNo(actor.getString("caseNo"));
//                                    JSONArray jArrynexthearingDate = actor.getJSONArray("nexthearingDate");
//                                    ArrayList<String> nexthearingDates=new ArrayList<String>();
//                                    for(int j=0;j<jArrynexthearingDate.length();j++){
//                                        if(jArrynexthearingDate.getString(j)!=null&& !jArrynexthearingDate.getString(j).equalsIgnoreCase("")) {
//                                            nexthearingDates.add(jArrynexthearingDate.getString(j));
//                                        }
//                                    }
//
//                                    JSONArray jArrynexthearingDate = actor.getJSONArray("courthall");
//                                    ArrayList<String> nexthearingDates=new ArrayList<String>();
//                                    for(int j=0;j<jArrynexthearingDate.length();j++){
//                                        if(jArrynexthearingDate.getString(j)!=null&& !jArrynexthearingDate.getString(j).equalsIgnoreCase("")) {
//                                            nexthearingDates.add(jArrynexthearingDate.getString(j));
//                                        }
//                                    }
//                                    allValue.setHearingDateLast(nexthearingDates.get(nexthearingDates.size()));
//                                    allValue.setDateOfEfiling(actor.getString("dateEfiling"));
//                                    allValue.setCaseLevel(actor.getString("caseLevel"));
//
//                                    allresultList.add(allValue);
//                                }
//                                setValue(allresultList);
//                            } catch (Exception e) {
//                                Log.d(urlConst.CASE_SEARCH_TAG, "json error" + e.getMessage());
//                            }
//                        }
//                    } else {
//                        emptyViewShow();
//                        Toast.makeText(HearingDate.this, response, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                }
//
//
//            @Override
//            public void onVolleyError(String error) {
//
//                emptyViewShow();
//                Log.d(urlConst.REGISTER_TAG, "" + error);
//                Toast.makeText(HearingDate.this, error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void setValue(List<CaseInfo> allresultList) {
//        emptyViewHide();
//        case_no.setText(""+allresultList.get(0).getCaseNo());
//        court_hall.setText(""+allresultList.get(0).getCourtHall());
//        next_herng_date.setText(""+allresultList.get(0).getHearingDateLast());
//        applent.setText(""+allresultList.get(0).getAppelentInfo().get(0));
//        respondent.setText(""+allresultList.get(0).getRespondaneInfo().get(0));
//    }
//
//    private void emptyViewHide() {
//        linr_empty.setVisibility(View.GONE);
//        card_view.setVisibility(View.VISIBLE);
//    }
//
//    private void emptyViewShow() {
//        linr_empty.setVisibility(View.VISIBLE);
//        card_view.setVisibility(View.GONE);
//    }

}

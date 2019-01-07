//package cms.co.in.kat.fragment;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.cms.kat.cws.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import cms.co.in.kat.adapters.CaseSearchAdapter;
//import cms.co.in.kat.asynchronous.Volley;
//import cms.co.in.kat.interfaces.VolleyListner;
//import cms.co.in.kat.objectholders.CaseInfo;
//import cms.co.in.kat.utils.URLConstants;
//
//public class CaseSearchResult extends Fragment {
//
//    private RecyclerView recyclerView;
//    private CaseSearchAdapter adapter;
//    private URLConstants urlConst = new URLConstants();
//    private Context context;
//    private TextView txt_case_no, txt_petitioner, txt_respondent, txt_section, txt_filing_date;
//    private String casenumber;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        casenumber = getArguments().getString("case_number");
//        String section = getArguments().getString("section");
//        String casetype = getArguments().getString("caseType");
//        String year = getArguments().getString("year");
//        String start = getArguments().getString("start");
//        String end = getArguments().getString("end");
//
//        View view = inflater.inflate(R.layout.case_search_result, container, false);
//        initLayout(view);
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("caseNumber", casenumber);
//        params.put("section", section);
//        params.put("caseType", casetype);
//        params.put("year", year);
//        params.put("start", start);
//        params.put("end", end);
//
//        setRecycleadapter(null);
//
//        volly(urlConst.CASE_SEARCH_TAG, Request.Method.POST, urlConst.CASE_SEARCH_URL, params);
//        return view;
//    }
//
//    private void initLayout(View view) {
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        txt_case_no = (TextView) view.findViewById(R.id.case_no);
//        txt_filing_date = (TextView) view.findViewById(R.id.filing_date);
//        txt_petitioner = (TextView) view.findViewById(R.id.petitioner);
//        txt_respondent = (TextView) view.findViewById(R.id.respondent);
//        txt_section = (TextView) view.findViewById(R.id.section);
//    }
//
//    private void volly(final String tag, int post, String link, HashMap<String, String> params) {
//
//        Volley vl = new Volley(context);
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
//
//                        Log.d(urlConst.CASE_SEARCH_TAG, "result response " + response);
//                        if (response.equalsIgnoreCase("")) {
//                            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT).show();
//                            try {
//
//                                List<CaseInfo> allresultList = new ArrayList<CaseInfo>();
//                                CaseInfo allValue = new CaseInfo();
//
//                                JSONObject jsonResponse = new JSONObject(response);
//                                JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
//                                for (int i = 0; i < data_result_array.length(); i++) {
//                                    JSONObject actor = data_result_array.getJSONObject(i);
//
//                                    allValue.setCaseType(actor.getString("caseType"));
//                                    allValue.setDateOfEfiling(actor.getString("dateOfEfiling"));
//                                    allValue.setCaseDivision(actor.getString("caseDivision"));
//                                    allValue.setCaseLevel(actor.getString("caseLevel"));
//                                    allValue.setStatus(actor.getString("Status"));
//                                    allValue.setCourtHall(actor.getString("courtHall"));
//                                    allValue.setCaseId(actor.getString("caseId"));
//                                    allValue.setBranch(actor.getString("branch"));
//                                    allValue.setCaseNo(actor.getString("caseNo"));
//                                    allValue.setLcoDate(actor.getString("lcoDate"));
//                                    allValue.setLco(actor.getString("lco"));
//                                    try {
//
//                                        allValue.setSection(actor.getString("section"));
//                                    } catch (Exception e) {
//                                        allValue.setSection("");
//
//                                    }
//
//                                    try {
//                                        allValue.setHearingDate(actor.getString("hearingDate"));
//                                    } catch (Exception e) {
//                                        allValue.setHearingDate("");
//
//                                    }
//                                    try {
//
//                                        allValue.setProceeding(actor.getString("proceeding"));
//                                    } catch (Exception e) {
//                                        allValue.setProceeding("");
//
//                                    }
//                                    try {
//
//                                        allValue.setJudgmentDate(actor.getString("judgmentDate"));
//                                    } catch (Exception e) {
//                                        allValue.setJudgmentDate("");
//
//                                    }
//
//                                    try {
//                                        ArrayList<String> appelentInfo = new ArrayList<String>();
//                                        JSONArray appelentInfoArray = actor.getJSONArray("appelentInfo");
//                                        for (int j = 0; j < appelentInfoArray.length(); j++) {
//                                            JSONObject jobject = appelentInfoArray.getJSONObject(j);
//                                            appelentInfo.add(jobject.getString("name"));
//
//                                        }
//                                        allValue.setAppelentInfo(appelentInfo);
//                                    } catch (Exception e) {
//                                        ArrayList<String> appelentInfo = new ArrayList<String>();
//                                        allValue.setAppelentInfo(appelentInfo);
//                                    }
//
//                                    ArrayList<String> respondentInfo = new ArrayList<String>();
//                                    JSONArray respondentInfoArray = actor.getJSONArray("respondentInfo");
//                                    for (int j = 0; j < respondentInfoArray.length(); j++) {
//                                        JSONObject jobject = respondentInfoArray.getJSONObject(j);
//                                        respondentInfo.add(jobject.getString("name"));
//
//                                    }
//                                    allValue.setRespondaneInfo(respondentInfo);
//
//                                    ArrayList<String> notice = new ArrayList<String>();
//                                    JSONArray noticeArray = actor.getJSONArray("notice");
//                                    for (int j = 0; j < respondentInfoArray.length(); j++) {
////                                        JSONObject jobject = respondentInfoArray.getJSONObject(i);
////                                        String name = jobject.getString("name");
////                                        respondentInfo.add(name);
//                                    }
//                                    allValue.setNotice(notice);
//                                    allresultList.add(allValue);
//                                }
//
//                                setRecycleadapter(allresultList);
//
//                            } catch (Exception e) {
//                                Log.d(urlConst.CASE_SEARCH_TAG, "json error" + e.getMessage());
//                            }
//                        }
//                    } else {
//                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                    }
//                } else if (tag == urlConst.SECTION_TAG) {
//
//                    if (result == 1) {
//
//                        Log.d(urlConst.SECTION_TAG, "result response " + response);
////                        if (response.equalsIgnoreCase("")) {
////                            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT).show();
////                            JSONObject jsonResponse = null;
////                            try {
////                                jsonResponse = new JSONObject(response);
////
////                                JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
////                                for (int i = 0; i < data_result_array.length(); i++) {
////                                    section.add(data_result_array.getJSONObject(i).getString("name"));
////                                }
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////
////                        }
//                    } else {
//
////                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onVolleyError(String error) {
//
//                Log.d(urlConst.REGISTER_TAG, "" + error);
//                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
//                setRecycleadapter(null);
//            }
//        });
//
//    }
//
//    private void setRecycleadapter(List<CaseInfo> allresultList) {
//
//        if (allresultList == null) {
//            adapter = new CaseSearchAdapter(null);
//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setAdapter(adapter);
//        } else {
//            adapter.updateEntry(allresultList);
//            if (casenumber != null && casenumber.length() > 0) {
//                txt_case_no.setText(allresultList.get(0).getCaseNo());
//
//                for (int i = 0; i < allresultList.get(0).getAppelentInfo().size(); i++) {
//                    txt_petitioner.setText(allresultList.get(0).getAppelentInfo().get(i));
//                }
//
//                for (int i = 0; i < allresultList.get(0).getRespondaneInfo().size(); i++) {
//                    txt_respondent.setText(allresultList.get(0).getRespondaneInfo().get(i));
//                }
//                txt_section.setText(allresultList.get(0).getBranch());
//                txt_filing_date.setText(allresultList.get(0).getDateOfEfiling());
//            }
//
//        }
//        adapter.notifyDataSetChanged();
//
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//
//        super.onAttach(context);
//        if (context instanceof Activity) {
//            this.context = (Activity) context;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//}

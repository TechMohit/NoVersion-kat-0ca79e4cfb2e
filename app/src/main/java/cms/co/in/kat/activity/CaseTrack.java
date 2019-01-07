package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.CaseInfo;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

public class CaseTrack extends AppCompatActivity {

    private AutoCompleteTextView et3, et4;
    private EditText etAppllent, etRespondent, etAdvocate, etLcoNumber, etLcoAuthority;
    private Button search, expand_button, expand_button2;
    @NonNull
    private String caseno = "";
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private MaterialBetterSpinner et2;
    private EditText et1;
    //    private String[] drop2 = {"REV", "COP", "ST"};
    private String[] drop1 = {"APL", "RVN", "REW", "RECT", "MIS", "CAV"};
    private String[] drop2 = {"APL", "REW", "RECT", "MIS","CAV","CROSS"};

    private ExpandableLayout expandableLayout1, expandableLayout2, expandableLayout11;
    private Button multiCaseSearch, expand_rev, expand_sale, expand_cop;
    private String msection = "", mcourthall = "", mbranch = "", mcaseType = "", mdistrict = "", mappellantName = "",
            mrespondentName = "", madvocateName = "", mstartDate = "", mendDate = "", mlcoNumber = "", mlcoAuthority = "";
    public static ArrayList<String> multiListCase = new ArrayList<>();
    private ArrayList courtHall, district, branch, casetype, revenueDivision, salesTaxDivision, coOperationDivision;

    private HashMap<String, String> courthallmap, branchMap, casetypeMap, revenueDivisionMap, salesTaxDivisionMap, coOperationDivisionMap;
    private URLConstants urlConst = new URLConstants();
    private MaterialBetterSpinner spBranch, spCaseType, spSection, spDistrict, spCourtHall;
    private String[] SPINNERLIST = {};
    private TextView etDateStart, etDateEnd;
    private RelativeLayout parentLayout;
    private List<String> caseAutoSearch = new ArrayList<>();
    private int AUTOSEARCH = 0;
    private ArrayAdapter<?> adapter, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_case_track);
        initLayout();
        initFontStyle();
        initListner();
        footerListner();
        initAdapter();
        initvalue();

        courtHall = new ArrayList();
        branch = new ArrayList();
        casetype = new ArrayList();
        district = new ArrayList();
        revenueDivision = new ArrayList();
        coOperationDivision = new ArrayList();
        salesTaxDivision = new ArrayList();



        /*courtHall.add("");
        branch.add("");
        casetype.add("");
        district.add("");
        coOperationDivision.add("");
        revenueDivision.add("");
        salesTaxDivision.add("");*/

        courthallmap = new HashMap<>();
        branchMap = new HashMap<>();
        casetypeMap = new HashMap<>();
        revenueDivisionMap = new HashMap<>();
        coOperationDivisionMap = new HashMap<>();
        salesTaxDivisionMap = new HashMap<>();

        Constant.CURRENT_TAG = urlConst.CASE_SEARCH_PRE_TAG;
        volly(urlConst.CASE_SEARCH_PRE_TAG, Request.Method.GET, urlConst.CASE_SEARCH_PRE_URL, null);

    }

    private void initvalue() {

    }

    private void initAdapter() {

        caseAutoSearch.add("");
        String[] data = caseAutoSearch.toArray(new String[caseAutoSearch.size()]);  // terms is a List<String>
        adapter = new ArrayAdapter<Object>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, data);
        adapter2 = new ArrayAdapter<Object>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, data);

        et3.setAdapter(adapter);  // keywordField is a AutoCompleteTextView
        et4.setAdapter(adapter2);
        et3.setThreshold(1);
        et4.setThreshold(1);

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, drop1);
//        et1.setAdapter(arrayAdapter);


        ArrayAdapter<String> arrayAdapterBranch = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, branch);
        spBranch.setAdapter(arrayAdapterBranch);
        ArrayAdapter<String> arrayAdapterCaseType = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, casetype);
        spCaseType.setAdapter(arrayAdapterCaseType);
        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spSection.setAdapter(arrayAdapterSection);
        ArrayAdapter<String> arrayAdapterDistrict = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, district);
        spDistrict.setAdapter(arrayAdapterDistrict);
        ArrayAdapter<String> arrayAdapterCourthall = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, courtHall);
        spCourtHall.setAdapter(arrayAdapterCourthall);


    }

    private void initListner() {

        multiCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShowDilog.internet) {
                    Constant.caseTrack = "set";
                    HashMap<String, String> params = new HashMap<>();
                    if (spBranch.getText().toString().equalsIgnoreCase("Revenue")) {
                        msection = spSection.getText().toString().equalsIgnoreCase("") ? "" : revenueDivisionMap.get(spSection.getText().toString());
                    } else if (spBranch.getText().toString().equalsIgnoreCase("Cooperation")) {
                        msection = spSection.getText().toString().equalsIgnoreCase("") ? "" : coOperationDivisionMap.get(spSection.getText().toString());
                    } else if (spBranch.getText().toString().equalsIgnoreCase("Sales Tax")) {
                        msection = spSection.getText().toString().equalsIgnoreCase("") ? "" : salesTaxDivisionMap.get(spSection.getText().toString());
                    }
                    params.put("section", msection);
                    mcourthall = spCourtHall.getText().toString().equalsIgnoreCase("") ? "" : courthallmap.get(spCourtHall.getText().toString());
                    params.put("courtHall", mcourthall);
                    params.put("caseNumber", "");
                    mbranch = spBranch.getText().toString().equalsIgnoreCase("") ? "" : spBranch.getText().toString();
                    params.put("branch", mbranch);
                    mcaseType = spCaseType.getText().toString().equalsIgnoreCase("") ? "" : spCaseType.getText().toString();
                    params.put("caseType", mcaseType);
                    mdistrict = spDistrict.getText().toString().equalsIgnoreCase("") ? "" : spDistrict.getText().toString();
                    params.put("district", mdistrict);
                    mappellantName = etAppllent.getText().toString().equalsIgnoreCase("") ? "" : etAppllent.getText().toString();
                    params.put("appellantName", mappellantName);
                    mrespondentName = etRespondent.getText().toString().equalsIgnoreCase("") ? "" : etRespondent.getText().toString();
                    params.put("respondentName", mrespondentName);
                    madvocateName = etAdvocate.getText().toString().equalsIgnoreCase("") ? "" : etAdvocate.getText().toString();
                    params.put("advocateName", madvocateName);
                    mstartDate = etDateStart.getText().toString().equalsIgnoreCase("click") ? "" : etDateStart.getText().toString();
                    params.put("startDate", mstartDate);
                    mendDate = etDateEnd.getText().toString().equalsIgnoreCase("click") ? "" : etDateEnd.getText().toString();
                    params.put("endDate", mendDate);
                    mlcoNumber = etLcoNumber.getText().toString().equalsIgnoreCase("") ? "" : etLcoNumber.getText().toString();
                    params.put("lcoNumber", mlcoNumber);
                    params.put("startValue", "0");
                    params.put("endValue", "10");
                    mlcoAuthority = etLcoAuthority.getText().toString().equalsIgnoreCase("") ? "" : etLcoAuthority.getText().toString();
                    params.put("lcoAuthority", mlcoAuthority);
                    params.put("memberName", "");

                    if (msection.equalsIgnoreCase("") && mcourthall.equalsIgnoreCase("") && mbranch.equalsIgnoreCase("") && mcaseType.equalsIgnoreCase("")
                            && mdistrict.equalsIgnoreCase("") && mstartDate.equalsIgnoreCase("") && mendDate.equalsIgnoreCase("") &&
                            mlcoAuthority.equalsIgnoreCase("") && mlcoNumber.equalsIgnoreCase("")) {
                        GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Please Select one value");

                    } else if (mappellantName.equalsIgnoreCase("") && mrespondentName.equalsIgnoreCase("") && madvocateName.equalsIgnoreCase("")) {
                        GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Enter Appellant/Respondent/Advocate name");

                    } else {
                        if (multiListCase != null && multiListCase.size() > 0) {
                            multiListCase.clear();
                        }
                        Constant.CURRENT_TAG = urlConst.CASE_SEARCH_TAG;
                        volly(urlConst.CASE_SEARCH_TAG, Request.Method.POST, urlConst.CASE_SEARCH_URL, params);
                    }
                } else {
                    GeneralUtilities.showMessage(CaseTrack.this, parentLayout, getResources().getString(R.string.internet));
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShowDilog.internet) {
                    caseno = validate();
                    if (caseno != null && caseno.length() > 1) {
                        if (expandableLayout2.isExpanded()) {
                            expandableLayout2.collapse();
                        }
                        if (expandableLayout1.isExpanded()) {
                            expandableLayout1.collapse();
                        }
                        Intent i = new Intent(CaseTrack.this, CaseTrackHeader.class);
                        i.putExtra("caseNo", caseno);
                        caseno="";
                        startActivity(i);
                    }
                } else {
                    GeneralUtilities.showMessage(CaseTrack.this, parentLayout, getResources().getString(R.string.internet));
                }
            }
        });

        expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout2.isExpanded()) {
                    etAppllent.setText("");
                    etLcoAuthority.setText("");
                    etRespondent.setText("");
                    etAdvocate.setText("");
                    etLcoAuthority.setText("");
                    etLcoNumber.setText("");
                    etDateStart.setText("Click");
                    etDateEnd.setText("Click");
                    expandableLayout2.collapse();


                    Log.d("resetfieldcasesearch","done");
                }
                if (expandableLayout11.isExpanded()) {
                    expandableLayout1.collapse();
                    expandableLayout11.collapse();
                } else {
//                    expandableLayout1.expand();
                    expandableLayout11.expand();

                }
            }
        });

        expand_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse();
                    expandableLayout11.collapse();
                }
                if (expandableLayout2.isExpanded()) {
                    expandableLayout2.collapse();
                } else {
                    expandableLayout2.expand();
                }
                if(expandableLayout11.isExpanded()){
                    expandableLayout11.collapse();
                }

            }
        });

        expand_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expandableLayout2.isExpanded()) {
                    expandableLayout2.collapse();
                }
                if (!expandableLayout1.isExpanded()) {
                    expandableLayout1.expand();
                }
                et1.setText("REV");
                et2.setText("");
                et3.setText("");
                et4.setText("");

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, drop1);
                et2.setAdapter(arrayAdapter1);

            }
        });
        expand_cop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (expandableLayout2.isExpanded()) {
                    expandableLayout2.collapse();

                }
                if (!expandableLayout1.isExpanded()) {
                    expandableLayout1.expand();
                }
                et1.setText("COP");
                et2.setText("");
                et3.setText("");
                et4.setText("");

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, drop1);
                et2.setAdapter(arrayAdapter1);


            }
        });
        expand_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expandableLayout2.isExpanded()) {
                    expandableLayout2.collapse();

                }
                if (!expandableLayout1.isExpanded()) {
                    expandableLayout1.expand();
                }
                et1.setText("ST");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, drop2);
                et2.setAdapter(arrayAdapter1);

            }
        });

//        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
//            @Override
//            public void onExpansionUpdate(float expansionFraction, int state) {
//                Log.d("ExpandableLayout0", "State: " + state);
//            }
//        });
//
//        expandableLayout2.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
//            @Override
//            public void onExpansionUpdate(float expansionFraction, int state) {
//                Log.d("ExpandableLayout1", "State: " + state);
//            }
//        });

        spSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spBranch.getText().toString().equalsIgnoreCase("Revenue")) {

                    ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<>(CaseTrack.this,
                            android.R.layout.simple_dropdown_item_1line, revenueDivision);
                    spSection.setAdapter(arrayAdapterSection);
                } else if (spBranch.getText().toString().equalsIgnoreCase("Cooperation")) {
                    ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<>(CaseTrack.this,
                            android.R.layout.simple_dropdown_item_1line, coOperationDivision);
                    spSection.setAdapter(arrayAdapterSection);

                } else if (spBranch.getText().toString().equalsIgnoreCase("Sales Tax")) {
                    ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<>(CaseTrack.this,
                            android.R.layout.simple_dropdown_item_1line, salesTaxDivision);
                    spSection.setAdapter(arrayAdapterSection);
                } else {
                    GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Please select Branch");
                }
            }
        });

        etDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GeneralUtilities(CaseTrack.this).setDate(etDateStart);

            }
        });
        etDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GeneralUtilities(CaseTrack.this).setDate(etDateEnd);

            }
        });


        et3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et1.getText().toString().equalsIgnoreCase("") || et2.getText().toString().equalsIgnoreCase("") ){

                }else {
                    if (s.length() != 0) {

                        if (caseAutoSearch != null && caseAutoSearch.size() > 0) {
                            caseAutoSearch.clear();
                        }
                        String tempCaseNo = et1.getText().toString() + "." + et2.getText().toString() + "-" + s.toString();
                        AUTOSEARCH = 1;

                        Log.e("***", "**start" + tempCaseNo);
                        HashMap<String, String> params = new HashMap<>();
                        params.put("caseNo", tempCaseNo);
                        params.put("resultType", "" + AUTOSEARCH);

                        Constant.CURRENT_TAG = urlConst.CASE_SEARCH_AUTO_TAG;
                        volly(urlConst.CASE_SEARCH_AUTO_TAG, Request.Method.POST, urlConst.CASE_SEARCH_AUTO_URL, params);


                    }
                }
            }
        });

        et4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(et1.getText().toString().equalsIgnoreCase("") || et2.getText().toString().equalsIgnoreCase("") ){

                }else {
                    if (s.length() != 0) {

                        if (caseAutoSearch != null && caseAutoSearch.size() > 0) {
                            caseAutoSearch.clear();
                        }
                        String tempCaseNo = et1.getText().toString() + "." + et2.getText().toString() + "-" + et3.getText().toString() + "/" + s.toString();
                        AUTOSEARCH = 2;

                        Log.e("***", "**start" + tempCaseNo);
                        HashMap<String, String> params = new HashMap<>();
                        params.put("caseNo", tempCaseNo);
                        params.put("resultType", "" + AUTOSEARCH);

                        Constant.CURRENT_TAG = urlConst.CASE_SEARCH_AUTO_TAG;
                        volly(urlConst.CASE_SEARCH_AUTO_TAG, Request.Method.POST, urlConst.CASE_SEARCH_AUTO_URL, params);


                    }
                }
            }
        });


    }

    private String validate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        if (et1.getText().toString().equalsIgnoreCase("") || et2.getText().toString().equalsIgnoreCase("") ||
                et3.getText().toString().equalsIgnoreCase("") || et4.getText().toString().equalsIgnoreCase("")) {
            GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Case Number is Invalid");

        } else if (et3.getText().toString().startsWith("0") || et4.getText().toString().startsWith("0")) {
            GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Please Enter a Valid Case Number");

        } else if (year < Integer.parseInt(et4.getText().toString())) {
            GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Enter a valid Year");

        } else {
            caseno = et1.getText().toString() + "." + et2.getText().toString() + "-" + et3.getText().toString() + "/" + et4.getText().toString();
        }


        return caseno;
    }

    private void initFontStyle() {
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i=new Intent(getApplicationContext(),CaseTrack.class);
//                startActivity(i);
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


    private void initLayout() {

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        expand_rev = (Button) findViewById(R.id.btn_rev);
        expand_cop = (Button) findViewById(R.id.btn_cop);
        expand_sale = (Button) findViewById(R.id.btn_sale);

        expandableLayout2 = (ExpandableLayout) findViewById(R.id.expandable_layout_2);
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.expandable_layout_1);
        expandableLayout11 = (ExpandableLayout) findViewById(R.id.expandable_layout_11);

        expand_button = (Button) findViewById(R.id.expand_button);
        expand_button2 = (Button) findViewById(R.id.expand_button2);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (MaterialBetterSpinner) findViewById(R.id.et2);

        et3 = (AutoCompleteTextView) findViewById(R.id.et3);
        et4 = (AutoCompleteTextView) findViewById(R.id.et4);

        search = (Button) findViewById(R.id.search);
        multiCaseSearch = (Button) findViewById(R.id.multi_search);

        spBranch = (MaterialBetterSpinner) findViewById(R.id.sp_branch);
        spCaseType = (MaterialBetterSpinner) findViewById(R.id.sp_case_type);
        spSection = (MaterialBetterSpinner) findViewById(R.id.sp_section);
        spDistrict = (MaterialBetterSpinner) findViewById(R.id.sp_district);
        spCourtHall = (MaterialBetterSpinner) findViewById(R.id.sp_court_hall);

        etDateStart = (TextView) findViewById(R.id.et_date_start);
        etDateEnd = (TextView) findViewById(R.id.et_date_end);

        etAppllent = (EditText) findViewById(R.id.et_appellant);
        etRespondent = (EditText) findViewById(R.id.et_respondent);
        etAdvocate = (EditText) findViewById(R.id.et_advocate);

        etLcoNumber = (EditText) findViewById(R.id.et_lco_number);
        etLcoAuthority = (EditText) findViewById(R.id.et_lco_authority);

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
                if ((tag == urlConst.CASE_SEARCH_AUTO_TAG)) {
                    Log.d(urlConst.CASE_SEARCH_AUTO_TAG, "result code " + response);
                    if (result == 1) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            for (int i = 0; i < data_result_array.length(); i++) {
                                int temp = 0;
                                try {
                                    temp = data_result_array.getJSONObject(i).getInt("label");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (temp != 0) {
                                    caseAutoSearch.add("" + temp);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String[] data = caseAutoSearch.toArray(new String[caseAutoSearch.size()]);  // terms is a List<String>

                        if (AUTOSEARCH == 1) {
                            adapter = new ArrayAdapter<Object>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, data);

                            et3.setAdapter(adapter);  // keywordField is a AutoCompleteTextView
                            et3.setThreshold(1);
                            adapter.notifyDataSetChanged();

                        } else if (AUTOSEARCH == 2) {
                            adapter2 = new ArrayAdapter<Object>(CaseTrack.this, android.R.layout.simple_dropdown_item_1line, data);

                            et4.setAdapter(adapter2);  // keywordField is a AutoCompleteTextView
                            et4.setThreshold(1);
                            adapter2.notifyDataSetChanged();

                        }

                    }
                } else if (tag == urlConst.CASE_SEARCH_TAG) {

                    Log.d(urlConst.CASE_SEARCH_TAG, "result code " + response);
                    if (result == 1) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            for (int i = 0; i < data_result_array.length(); i++) {
                                multiListCase.add("" + data_result_array.getJSONObject(i).get("caseId"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showCaseList();
                    } else {
                        GeneralUtilities.showMessage(CaseTrack.this, parentLayout, response);

                    }
                } else if (tag == urlConst.CASE_SEARCH_PRE_TAG) {
                    Log.d(urlConst.CASE_SEARCH_PRE_TAG, "result code " + response);
                    if (result == 1) {
                        try {
                            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
                            JSONArray districArray = jArr.getJSONObject(0).getJSONArray("districtObj");

                            for (int j = 0; j < districArray.length(); j++) {
                                Log.e("", "" + districArray.get(j));
                                district.add(districArray.get(j));
                            }

                            JSONArray courtHallArray = jArr.getJSONObject(0).getJSONArray("courtHall");
                            for (int i = 0; i < courtHallArray.length(); i++) {
                                courtHall.add(courtHallArray.getJSONObject(i).get("name"));
                                courthallmap.put(courtHallArray.getJSONObject(i).get("name").toString(),
                                        courtHallArray.getJSONObject(i).get("categoryId").toString());
                            }

                            JSONArray branchArray = jArr.getJSONObject(0).getJSONArray("branch");
                            for (int i = 0; i < branchArray.length(); i++) {
                                branch.add(branchArray.getJSONObject(i).get("name"));
                                branchMap.put(branchArray.getJSONObject(i).get("name").toString(),
                                        branchArray.getJSONObject(i).get("id").toString());
                            }

                            JSONArray casetypeArray = jArr.getJSONObject(0).getJSONArray("casetype");
                            for (int i = 0; i < casetypeArray.length(); i++) {
                                casetype.add(casetypeArray.getJSONObject(i).get("name"));
                                casetypeMap.put(casetypeArray.getJSONObject(i).get("name").toString(),
                                        casetypeArray.getJSONObject(i).get("id").toString());
                            }

                            JSONArray revenueDivisionArray = jArr.getJSONObject(0).getJSONArray("revenueDivision");
                            for (int i = 0; i < revenueDivisionArray.length(); i++) {
                                revenueDivision.add(revenueDivisionArray.getJSONObject(i).get("name"));
                                revenueDivisionMap.put(revenueDivisionArray.getJSONObject(i).get("name").toString(),
                                        revenueDivisionArray.getJSONObject(i).get("id").toString());
                            }

                            JSONArray salesTaxDivisionArray = jArr.getJSONObject(0).getJSONArray("salesTaxDivision");
                            for (int i = 0; i < salesTaxDivisionArray.length(); i++) {
                                salesTaxDivision.add(salesTaxDivisionArray.getJSONObject(i).get("name"));
                                salesTaxDivisionMap.put(salesTaxDivisionArray.getJSONObject(i).get("name").toString(),
                                        salesTaxDivisionArray.getJSONObject(i).get("id").toString());
                            }

                            JSONArray coOperationDivisionArray = jArr.getJSONObject(0).getJSONArray("coOperationDivision");
                            for (int i = 0; i < coOperationDivisionArray.length(); i++) {
                                coOperationDivision.add(coOperationDivisionArray.getJSONObject(i).get("name"));
                                coOperationDivisionMap.put(coOperationDivisionArray.getJSONObject(i).get("name").toString(),
                                        coOperationDivisionArray.getJSONObject(i).get("id").toString());
                            }

                            initAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        finish();
                    }
                }
            }

            @Override
            public void onVolleyError(String result) {

                Log.d(urlConst.CASE_SEARCH_TAG, "" + result);
                GeneralUtilities.showMessage(CaseTrack.this, parentLayout, "Please Try Again");

            }
        });
    }

    private void showCaseList() {
        Intent i = new Intent(CaseTrack.this, CaseList.class);
        i.putExtra("caseType", Constant.MULTI_CASE_SEARCH);
        i.putExtra("msection", msection);
        i.putExtra("mcourthall", mcourthall);
        i.putExtra("mbranch", mbranch);
        i.putExtra("mcaseType", mcaseType);
        i.putExtra("mdistrict", mdistrict);
        i.putExtra("mappellantName", mappellantName);
        i.putExtra("mrespondentName", mrespondentName);
        i.putExtra("madvocateName", madvocateName);
        i.putExtra("mstartDate", mstartDate);
        i.putExtra("mendDate", mendDate);
        i.putExtra("mlcoNumber", mlcoNumber);
        i.putExtra("mlcoAuthority", mlcoAuthority);
        startActivity(i);
    }

}

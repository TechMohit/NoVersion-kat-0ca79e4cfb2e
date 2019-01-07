package cms.co.in.kat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.objectholders.CaseInfo;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.URLConstants;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;

public class CaseJudgmentInfo extends AppCompatActivity {
    TextView case_no,appleant_name,repondent_name,date_judgment,status,courthaii_no;
    String intValue;
    private ProgressBar progressBar;
    private URLConstants urlConst = new URLConstants();
    private Volley vl;
    private RelativeLayout parentLayout;
    private WebView webview;
    private Button viewjudgment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_judgment_info);
        Intent mIntent = getIntent();
        intValue = mIntent.getStringExtra("caseNo");

        initLayout();
        initCall();
        initListner();
       // setValue();


    }

    private void initListner() {
        viewjudgment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getJudgementpdf(intValue);
            }
        });
    }

    private void getJudgementpdf(String intValue) {
        if (intValue != null && intValue.length() > 1) {


            HashMap<String, String> params = new HashMap<>();
            params.put("caseNo", intValue);

            progressBar.setVisibility(View.VISIBLE);
            Constant.CURRENT_TAG = urlConst.JUDGEMENT_TAG;

            volly(urlConst.JUDGEMENT_TAG, Request.Method.POST, urlConst.JUDGEMENT_URL, params);

        } else {
            progressBar.setVisibility(View.GONE);

        }
    }

    private void setValue() {
        case_no.setText(intValue);
    }

    private void initLayout() {
        case_no = (TextView) findViewById(R.id.case_no);
        appleant_name = (TextView) findViewById(R.id.petitioner);
        repondent_name = (TextView) findViewById(R.id.respondent);
        courthaii_no = (TextView) findViewById(R.id.courthallno);
        date_judgment = (TextView) findViewById(R.id.judgmentdate);
        status = (TextView) findViewById(R.id.status);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        parentLayout = (RelativeLayout) findViewById(R.id.coordinator);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        viewjudgment = (Button) findViewById(R.id.btn_view);
    }

    private void initCall() {
        HashMap<String, String> params = new HashMap<>();
        params.put("caseNumber", intValue);
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

    private void volly(final String tag, int post, String link, HashMap<String, String> params) {

        vl = new Volley(CaseJudgmentInfo.this);

        vl.makeStringReq(tag, post, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, @NonNull String response) {

                progressBar.setVisibility(View.GONE);
                if(tag == urlConst.CASE_SEARCH_TAG) {
                    if(response.equalsIgnoreCase("No Data Found")) {

                        Toast.makeText(CaseJudgmentInfo.this,"No Data Found",Toast.LENGTH_LONG).show();

                        GeneralUtilities.showMessage(getApplication(), parentLayout, "No Data Found");

                    }
                    if (result == 1) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                            for (int i = 0; i < data_result_array.length(); i++) {
                                JSONObject actor = data_result_array.getJSONObject(i);
                                if (!actor.isNull("caseNo"))
                                    case_no.setText(actor.getString("caseNo"));


                                try {
                                    if (!actor.isNull("appelentInfo")) {
                                        JSONArray appelentInfoArray = actor.getJSONArray("appelentInfo");
                                        if (appelentInfoArray != null && appelentInfoArray.length() > 0) {
                                            JSONObject jobject1 = appelentInfoArray.getJSONObject(0);
                                            appleant_name.setText(jobject1.getString("name"));


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
                                            repondent_name.setText(jobject.getString("name"));

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (!actor.isNull("judgement")) {
                                    JSONArray data_proce = actor.getJSONArray("judgement");
                                    for (int p = 0; p < data_proce.length(); p++) {
                                        JSONObject pro = data_proce.getJSONObject(p);
                                        courthaii_no.setText( pro.getString("courtHall"));
                                        date_judgment.setText(pro.getString("judmentDate"));
                                        status.setText(pro.getString("status"));


                                    }
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                else if (tag == urlConst.JUDGEMENT_TAG) {
                    Log.d(urlConst.CASE_SEARCH_TAG, "result response3 " );
                    if(response.equalsIgnoreCase("No Data Found")) {
                        Log.d(urlConst.CASE_SEARCH_TAG, "result response55 " + response);
                        Toast.makeText(CaseJudgmentInfo.this,"No Data Found",Toast.LENGTH_LONG).show();

                        GeneralUtilities.showMessage(getApplication(), parentLayout, "No Data Found");

                    }
                        if (result == 1) {
                            Log.d(urlConst.JUDGEMENT_TAG, "result response " + response);
                            if (response.equalsIgnoreCase("")) {
                                Toast.makeText(CaseJudgmentInfo.this,"No Data Found",Toast.LENGTH_LONG).show();

                                GeneralUtilities.showMessage(CaseJudgmentInfo.this, parentLayout, "Data Not Found");

                            } else {
//                            Toast.makeText(Judgement.this, "Sucess", Toast.LENGTH_SHORT).show();
                                try {

                                    List<CaseInfo> allresultList = new ArrayList<>();
                                    CaseInfo allValue = new CaseInfo();

                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray data_result_array = jsonResponse.getJSONArray("data_result");
                                    for (int i = 0; i < data_result_array.length(); i++) {
                                        JSONObject actor = data_result_array.getJSONObject(i);
                                        if (!actor.isNull("judgementURL")) {
                                            allValue.setJudgmentURL(actor.getString("judgementURL"));
                                        }

                                        allresultList.add(allValue);
                                    }
                                    setValue(allresultList.get(0).getJudgmentURL());
                                } catch (Exception e) {
                                    Log.d(urlConst.JUDGEMENT_TAG, "json error" + e.getMessage());
                                }
                            }
                        }
                    }

                }



            @Override
            public void onVolleyError(String error) {
                progressBar.setVisibility(View.GONE);

                Log.d(urlConst.REGISTER_TAG, "" + error);
                GeneralUtilities.showMessage(CaseJudgmentInfo.this, parentLayout, "Please Try Again");

            }
        });
    }

    private void setValue(@Nullable String judgmentURL) {
        if (judgmentURL != null && judgmentURL.length() > 0) {
            webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + judgmentURL);

            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.VISIBLE);

                    }
                }
            });

        } else {
            GeneralUtilities.showMessage(CaseJudgmentInfo.this, parentLayout, "Data Not Found");

        }
    }

}

package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import cms.co.in.kat.adapters.SittingListAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.URLConstants;

public class SittingList extends AppCompatActivity {

    private RecyclerView rvSittingList;
    private SittingListAdapter adapter;
    private ArrayList<String> sittingList,sittingListUrls;
    @NonNull
    private URLConstants urlConstants = new URLConstants();
//    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
    private EditText etDate;
    private Volley v;
    private LinearLayout footerCaseSearch,footerHome,footerJudgment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitting_list);
        initLayout();
        sittingList = new ArrayList();
        sittingListUrls= new ArrayList();

        footerListner();
        getSittingList();
//        sittingList.add(0,"Camp 2016 New");
//        sittingList.add(1,"Court hall Wise member");
//        sittingList.add(2,"Misc Informration");
//        sittingList.add(3,"Single Member List");
//        sittingList.add(4,"Sitting List July 2016");
//
//        if(sittingList.size()==0){
//            sittingList.add("Camp 2016 New");
//            sittingList.add("Court hall Wise member");
//            sittingList.add("Misc Informration");
//            sittingList.add("Single Member List");
//            sittingList.add("Sitting List July 2016");
//        }

        adapter = new SittingListAdapter(this, sittingList, sittingListUrls);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSittingList.setLayoutManager(mLayoutManager);
        rvSittingList.setItemAnimator(new DefaultItemAnimator());
        rvSittingList.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(SittingList.this,CaseTrack.class);
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
                }else {
                    i = new Intent(getApplicationContext(), GuestHome.class);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);

            }
        });
        footerJudgment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SittingList.this,Judgement.class);
                startActivity(i);
            }
        });
    }

    private void initLayout() {
//        btn1 = (Button) findViewById(R.id.btn_court_1);
//        btn2 = (Button) findViewById(R.id.btn_court_2);
//        btn3 = (Button) findViewById(R.id.btn_court_3);
//        btn4 = (Button) findViewById(R.id.btn_court_4);
//        btn5 = (Button) findViewById(R.id.btn_court_5);
//        btn6 = (Button) findViewById(R.id.btn_court_6);
//        btn7 = (Button) findViewById(R.id.btn_court_7);
//        btn8 = (Button) findViewById(R.id.btn_court_8);
//        etDate = (EditText) findViewById(R.id.et_date);
//        etDate.setOnClickListener(this);

        footerCaseSearch=(LinearLayout)findViewById(R.id.footerCaseSearch);
        footerHome=(LinearLayout)findViewById(R.id.footerHome);
        footerJudgment=(LinearLayout)findViewById(R.id.footerJudgement);

        rvSittingList = (RecyclerView) findViewById(R.id.rv_sitting_list);
    }

    private void getSittingList() {
        v = new Volley(this);
        Constant.CURRENT_TAG = urlConstants.SITTING_LIST_TAG;
        v.makeStringReq(urlConstants.SITTING_LIST_TAG, Request.Method.GET, urlConstants.SITTING_LIST_URL, null, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {

            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {

            }

            @Override
            public void onVolleyRespondString(int result, String response) {

                if(result==1)
                {
                    if(sittingList!=null&& sittingList.size()>0){
                        sittingList.clear();
                    }
                    if(sittingListUrls!=null&& sittingListUrls.size()>0){
                        sittingListUrls.clear();
                    }
                    try {
                        JSONObject jObj= new JSONObject(response);
                        JSONArray jArr = jObj.getJSONArray("data_result");
                        int len =jArr.length();
                        for (int i =0; i<len; i++)
                        {
                            JSONObject obj = jArr.getJSONObject(i);
                            sittingList.add(obj.getString("name"));
                            sittingListUrls.add(obj.getString("url"));
                        }
                         ArrayList<String> sittingList1=new ArrayList();
                        ArrayList<String> sittingList2=new ArrayList();
                        sittingList1.addAll(sittingList);
                        sittingList2.addAll(sittingListUrls);

                        adapter.updateEntry(sittingList1,sittingList2);
                        adapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        e.printStackTrace();
                        finish();
                    }
                }
            }

            @Override
            public void onVolleyError(@Nullable String result) {

                if(result != null)
                {
                    finish();
                }
            }
        });

    }
}

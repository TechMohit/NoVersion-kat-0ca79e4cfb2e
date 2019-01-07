package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.adapters.CaseSearchAdapter;
import cms.co.in.kat.adapters.CaseSearchDetailAdapter;
import cms.co.in.kat.utils.Constant;

import static cms.co.in.kat.activity.CaseTrackHeader.allresultListDetail;

public class CaseTrackDetails extends AppCompatActivity {

    private TextView caseNo,caseFile,appellantName,appellantAdress,respondentName,respondentAdress,provisionlaw,
    dateEfiling,caseFilelevel,locNo,authoritypass,authorityOther;
    private RecyclerView recyclerView;
    private CaseSearchDetailAdapter adapter;
    private LinearLayout footerCaseSearch,footerHome,footerJudgment;
    private CardView card_case_file_with;
    private Button btn_scroll;
    private NestedScrollView nestedScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_track_details);
        initLayout();
        setValue();
        setRecycleadapter();
        footerListner();
        checkLogin();
        initListner();
    }

    private void initListner() {
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrolling.scrollTo(0,0);
            }
        });
    }

    private void checkLogin() {
        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String userid = prefs.getString(Constant.MY_PREF_USERID, null);

        if(userid!=null){
            card_case_file_with.setVisibility(View.VISIBLE);
        }else{
            card_case_file_with.setVisibility(View.GONE);
        }
    }

    private void setRecycleadapter() {

        adapter = new CaseSearchDetailAdapter(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CaseTrackDetails.this);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        if (allresultListDetail != null) {
            adapter.updateEntry(allresultListDetail);
            adapter.notifyDataSetChanged();

        }

    }

    private void setValue() {

        if(allresultListDetail!=null && allresultListDetail.size()>0) {
            if(allresultListDetail.get(0).getCaseNo()!=null && !allresultListDetail.get(0).getCaseNo().isEmpty())
            caseNo.setText("" + allresultListDetail.get(0).getCaseNo());

            if(allresultListDetail.get(0).getCaseFileWith()!=null && !allresultListDetail.get(0).getCaseFileWith().isEmpty())
                caseFile.setText("" + allresultListDetail.get(0).getCaseFileWith());

            if(allresultListDetail.get(0).getAppelentInfoName()!=null && !allresultListDetail.get(0).getAppelentInfoName().isEmpty())
                appellantName.setText("" + allresultListDetail.get(0).getAppelentInfoName());

            if(allresultListDetail.get(0).getAppelentInfoAddress()!=null && !allresultListDetail.get(0).getAppelentInfoAddress().isEmpty())
                appellantAdress.setText("" + allresultListDetail.get(0).getAppelentInfoAddress());

            if(allresultListDetail.get(0).getRespondaneInfoName()!=null && !allresultListDetail.get(0).getRespondaneInfoName().isEmpty())
                respondentName.setText("" + allresultListDetail.get(0).getRespondaneInfoName());

            if(allresultListDetail.get(0).getRespondaneInfoAddress()!=null && !allresultListDetail.get(0).getRespondaneInfoAddress().isEmpty())
                respondentAdress.setText("" + allresultListDetail.get(0).getRespondaneInfoAddress());

            if(allresultListDetail.get(0).getProvisionlaw()!=null && !allresultListDetail.get(0).getProvisionlaw().isEmpty())
                provisionlaw.setText("" + allresultListDetail.get(0).getProvisionlaw());

            if(allresultListDetail.get(0).getDateOfEfiling()!=null && !allresultListDetail.get(0).getDateOfEfiling().isEmpty())
                dateEfiling.setText("" + allresultListDetail.get(0).getDateOfEfiling());

            if(allresultListDetail.get(0).getCaseLevel()!=null && !allresultListDetail.get(0).getCaseLevel().isEmpty())
                caseFilelevel.setText("" + allresultListDetail.get(0).getCaseLevel());

            if(allresultListDetail.get(0).getLcoNo()!=null && !allresultListDetail.get(0).getLcoNo().isEmpty())
                locNo.setText("" + allresultListDetail.get(0).getLcoNo());

            if(allresultListDetail.get(0).getAuthPassOrder()!=null && !allresultListDetail.get(0).getAuthPassOrder().isEmpty())
                authoritypass.setText("" + allresultListDetail.get(0).getAuthPassOrder());

            if(allresultListDetail.get(0).getOtherAuthPassOrder()!=null && !allresultListDetail.get(0).getOtherAuthPassOrder().isEmpty())
                authorityOther.setText("" + allresultListDetail.get(0).getOtherAuthPassOrder());
        }
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),CaseTrack.class);
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
                Intent i=new Intent(getApplicationContext(),Judgement.class);
                startActivity(i);
            }
        });
    }



    private void initLayout() {

        footerCaseSearch=(LinearLayout)findViewById(R.id.footerCaseSearch);
        footerHome=(LinearLayout)findViewById(R.id.footerHome);
        footerJudgment=(LinearLayout)findViewById(R.id.footerJudgement);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        caseNo=(TextView)findViewById(R.id.case_number);
        caseFile=(TextView)findViewById(R.id.case_file);
        appellantName=(TextView)findViewById(R.id.appellant_name);
        appellantAdress=(TextView)findViewById(R.id.appellant_address);
        respondentName=(TextView)findViewById(R.id.respondent_name);
        respondentAdress=(TextView)findViewById(R.id.respondent_address);
        provisionlaw=(TextView)findViewById(R.id.provision_law);
        dateEfiling=(TextView)findViewById(R.id.date_efilling);
        caseFilelevel=(TextView)findViewById(R.id.case_file_level);
        locNo=(TextView)findViewById(R.id.loc_no);
        authoritypass=(TextView)findViewById(R.id.authority_pass);
        authorityOther=(TextView)findViewById(R.id.authority_other);
        card_case_file_with=(CardView)findViewById(R.id.card_case_file_with);
        btn_scroll=(Button)findViewById(R.id.btn_scroll);
        nestedScrolling=(NestedScrollView)findViewById(R.id.nestedScrolling);
    }
}

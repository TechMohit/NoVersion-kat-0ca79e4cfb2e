package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cms.kat.cws.R;

import cms.co.in.kat.adapters.CaseSearchAdapter;
import cms.co.in.kat.adapters.CaseSearchJudAdapter;
import cms.co.in.kat.utils.Constant;

import static cms.co.in.kat.activity.CaseTrackHeader.allresultListDetail;

public class CaseTrackJudDetails extends AppCompatActivity {

    private RelativeLayout parentLayout;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_track_jud_details);

        initLayout();
        footerListner();


        CaseSearchJudAdapter adapter = new CaseSearchJudAdapter(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CaseTrackJudDetails.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.updateEntry(allresultListDetail.get(0).getJudgmentArry());
        adapter.notifyDataSetChanged();


    }

    private void initLayout() {
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
}

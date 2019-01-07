package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cms.kat.cws.R;

import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;

public class Reports extends AppCompatActivity implements View.OnClickListener {

    private TextView txt;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment, footer;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initLayout();
        footerListner();
        initListner();
    }

    private void initListner() {
        txt.setOnClickListener(this);

    }

    private void initLayout() {
        parentLayout = (RelativeLayout) findViewById(R.id.coordinator);

        footerCaseSearch = (LinearLayout) findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) findViewById(R.id.footerJudgement);

        txt = (TextView) findViewById(R.id.txt);
    }

    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Reports.this, CaseTrack.class);
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
                Intent i = new Intent(Reports.this, Judgement.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (ShowDilog.internet) {
            switch (v.getId()) {
                case R.id.txt:
                    Intent i = new Intent(Reports.this, ReportDetail.class);
                    startActivity(i);
                    break;
            }
        } else {
            GeneralUtilities.showMessage(Reports.this, parentLayout, getResources().getString(R.string.internet));
        }
    }
}

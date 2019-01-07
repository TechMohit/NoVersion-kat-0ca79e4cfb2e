package cms.co.in.kat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cms.kat.cws.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cms.co.in.kat.activity.DashBoardCaseList;
import cms.co.in.kat.activity.LoginHome;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;


public class FragmentDashboard extends Fragment implements View.OnClickListener {

    private TextView txtMy, txtPer, txtDis, txtTemp, txtIn, txtScr;
    private LoginHome context;
    private RelativeLayout relMy, relPer, relTemp, relDis, relScr, relIn;
    private LinearLayout parentLayout;
    private DatabaseHandler db;

    private int caseInCourt = 0;
    private int caseMyCases = 0;
    private int caseExpList = 0;
    private int caseTempList = 0;
    private int casePerList = 0;
    private int caseScr = 0,caseNoDefCount=0,caseDefCount=0;
    private ExpandableLayout expandableLayout;
    private Button defect, nodefect;

//    private int noDefect = 0;
//    private int defect = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initLayout(v);
        initListner();

        db = new DatabaseHandler(context);
        String response = db.getDetailsAll(context);
        if (response != null && !response.equalsIgnoreCase("")) {
            getDetailsJson(response);
            txtMy.setText("" + caseMyCases);
            txtPer.setText("" + casePerList);
            txtTemp.setText("" + caseTempList);
            txtDis.setText("" + caseExpList);
            txtScr.setText("" + caseScr);
            txtIn.setText("" + caseInCourt);
            defect.setText("DEFECT CASES ("+caseDefCount+")");
            nodefect.setText("NO DEFECT CASES ("+caseNoDefCount+")");
        }

        context.toolBarName("My DashBoard");

        return v;
    }

    private void initListner() {
        relMy.setOnClickListener(this);
        relPer.setOnClickListener(this);
        relTemp.setOnClickListener(this);
        relDis.setOnClickListener(this);
        relScr.setOnClickListener(this);
        relIn.setOnClickListener(this);
        defect.setOnClickListener(this);
        nodefect.setOnClickListener(this);
    }

    private void initLayout(View v) {

        expandableLayout = (ExpandableLayout) v.findViewById(R.id.expandable_layout);

        parentLayout = (LinearLayout) v.findViewById(R.id.parentLayout);

        defect = (Button) v.findViewById(R.id.defect);
        nodefect = (Button) v.findViewById(R.id.nodefect);

        txtMy = (TextView) v.findViewById(R.id.my);
        txtPer = (TextView) v.findViewById(R.id.per);
        txtTemp = (TextView) v.findViewById(R.id.temp);
        txtDis = (TextView) v.findViewById(R.id.dis);
        txtScr = (TextView) v.findViewById(R.id.scr);
        txtIn = (TextView) v.findViewById(R.id.in);

        relMy = (RelativeLayout) v.findViewById(R.id.rel_my);
        relPer = (RelativeLayout) v.findViewById(R.id.rel_per);
        relTemp = (RelativeLayout) v.findViewById(R.id.rel_temp);
        relDis = (RelativeLayout) v.findViewById(R.id.rel_dis);
        relScr = (RelativeLayout) v.findViewById(R.id.rel_scr);
        relIn = (RelativeLayout) v.findViewById(R.id.rel_in);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context= (LoginHome)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        if (ShowDilog.internet) {
            Intent i;
            switch (v.getId()) {

                case R.id.rel_my:
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                    if (caseMyCases != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.MY_CASES);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;

                case R.id.rel_per:
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                    if (casePerList != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.PERMANENT_CASE);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;
                case R.id.rel_temp:
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                    if (caseTempList != 0) {
                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.TEMPORARYCASES);

                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;
                case R.id.rel_dis:
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                    if (caseExpList != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.DISCARDEDCASE);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;

                case R.id.rel_in:
                    if (expandableLayout.isExpanded()) {
                        expandableLayout.collapse();
                    }
                    if (caseInCourt != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.IN_COURT);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;
                case R.id.rel_scr:

                    if (expandableLayout.isExpanded()) {
                    } else {
                        expandableLayout.expand();
                    }
//                    if (caseScr != 0) {
//
//                        i = new Intent(context, DashBoardCaseList.class);
//                        i.putExtra("caseType", Constant.DEFECTCASES);
//                        context.startActivity(i);
//                    } else {
//                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
//                    }
                    break;
                case R.id.defect:
                    if (caseScr != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.DEFECTCASES);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;
                case R.id.nodefect:
                    if (caseScr != 0) {

                        i = new Intent(context, DashBoardCaseList.class);
                        i.putExtra("caseType", Constant.NODEFECTCASES);
                        context.startActivity(i);
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, "Empty");
                    }
                    break;
            }
        } else {

            GeneralUtilities.showMessage(context, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(context).execute();

        }
    }

    private void getDetailsJson(String response) {

        try {
            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");

            JSONObject js = jArr.getJSONObject(0).getJSONObject("caseDetails");
//                            String re = "" + detailsJsonObject;
//                            JSONObject js = new JSONObject(re);
            try {
                int tempCaseNo = js.getInt("tempCaseNo");
                int discardCaseNo = js.getInt("discardCaseNo");
                int parementCaseNo = js.getInt("parementCaseNo");
                int myCaseNo = js.getInt("myCases");
                int inCourtCaseNo = js.getInt("inCourtCases");
                int scrCase = js.getInt("scrutinyCaseNo");
                int defCount = js.getInt("defCount");
                int noDefCount = js.getInt("noDefCount");


//                Log.e("**", "**" + tempCaseNo);
//                Log.e("**", "**" + discardCaseNo.length());
//                Log.e("**", "**" + parementCaseNo.length());

//                caseExp.setTitle("Discarded Cases (" + discardCaseNo.length() + ")");
//                casePer.setTitle("Permanent Cases (" + parementCaseNo.length() + ")");
//                caseTemp.setTitle("Temporary Cases (" + tempCaseNo.length() + ")");

//                                CaseNumber list=new CaseNumber();

                caseTempList = tempCaseNo;
                caseExpList = discardCaseNo;
                casePerList = parementCaseNo;
                caseScr = scrCase;
                caseInCourt = inCourtCaseNo;
                caseMyCases = myCaseNo;
                caseDefCount = defCount;
                caseNoDefCount = noDefCount;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
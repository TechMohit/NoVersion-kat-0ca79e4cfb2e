package cms.co.in.kat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cms.co.in.kat.activity.CaseList;
import cms.co.in.kat.activity.CaseTrack;
import cms.co.in.kat.activity.ChangePassword;
import cms.co.in.kat.activity.GuestHome;
import cms.co.in.kat.activity.Judgement;
import cms.co.in.kat.activity.LoginHome;
import cms.co.in.kat.adapters.EWalletHistoryAdapter;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.objectholders.EWalletModel;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

import static android.content.Context.MODE_PRIVATE;


public class FragmentEWallet extends Fragment implements View.OnClickListener {

    private LoginHome context;
    private EWalletHistoryAdapter adapter;
    private ArrayList<EWalletModel> walletHistoryList;
    private RecyclerView walletListView;
    private String currenBalance = "0.0";
    private TextView txtCurrentBal;
    private RelativeLayout parentLayout;
    private LinearLayout footerCaseSearch, footerHome, footerJudgment;

    @NonNull
    private URLConstants urlConstants = new URLConstants();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ewallet, container, false);
        initLayout(v);
        initListner();
        footerListner();
        SharedPreferences prefs = context.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        String userName = prefs.getString(Constant.MY_PREF_USERNAME, null);

        HashMap<String, String> params = new HashMap<>();
        params.put("ewalletNo", userName);

        walletHistoryList = new ArrayList<>();
        Constant.CURRENT_TAG = urlConstants.EWALLET_HISTORY_TAG;
        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.EWALLET_HISTORY_URL, params);

        context.toolBarName("My Wallet Details");

        return v;
    }

    private void initListner() {
    }

    private void initLayout(View v) {
        walletListView = (RecyclerView) v.findViewById(R.id.wallet_list_view);
        txtCurrentBal=(TextView)v.findViewById(R.id.txt_current_bal);

        parentLayout = (RelativeLayout) v.findViewById(R.id.parentLayout);
        footerCaseSearch = (LinearLayout) v.findViewById(R.id.footerCaseSearch);
        footerHome = (LinearLayout) v.findViewById(R.id.footerHome);
        footerJudgment = (LinearLayout) v.findViewById(R.id.footerJudgement);

    }
    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, CaseTrack.class);
                startActivity(i);
            }
        });

        footerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
                String name = prefs.getString(Constant.MY_PREF_USERNAME, null);
                Intent i;
                if (name != null) {
                    i = new Intent(context, LoginHome.class);
                } else {
                    i = new Intent(context, GuestHome.class);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);

            }
        });
        footerJudgment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Judgement.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (LoginHome) context;
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
                case R.id.pass_change:
                    i = new Intent(context, ChangePassword.class);
                    startActivity(i);
                    break;
            }
        } else {
            GeneralUtilities.showMessage(context, parentLayout, getResources().getString(R.string.internet));
            new InternetCheck(context).execute();
        }
    }

    private void volly(final String tag, int get, String link, HashMap<String, String> params) {

        Volley vl = new Volley(context);
        vl.makeStringReq(tag, get, link, params, new VolleyListner() {
            @Override
            public void onVolleyRespondJSONObject(JSONObject result) {
            }

            @Override
            public void onVolleyRespondJSONArray(JSONArray result) {
            }

            @Override
            public void onVolleyRespondString(int result, @NonNull String response) {
                if (tag == urlConstants.EWALLET_HISTORY_TAG) {
                    if (result == 1) {
                        try {
//                            response=temp;
                            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");
                            String transectionId = "",particulars="",debit="",credit="",transactionDate="",type="";
                            for (int i = 0; i < jArr.length(); i++) {

                                EWalletModel wallet=new EWalletModel();

                                transectionId = jArr.getJSONObject(i).getString("transectionId");
                                particulars = jArr.getJSONObject(i).getString("particulars");
                                debit = jArr.getJSONObject(i).getString("debit");
                                credit = jArr.getJSONObject(i).getString("credit");
                                transactionDate = jArr.getJSONObject(i).getString("transactionDate");
                                type = jArr.getJSONObject(i).getString("type");

                                wallet.setTransectionId(transectionId);
                                wallet.setParticulars(particulars);
                                wallet.setDebit(debit);
                                wallet.setCredit(credit);
                                wallet.setType(type);
                                wallet.setTransactionDate(transactionDate);

                                walletHistoryList.add(wallet);
                                currenBalance = jArr.getJSONObject(i).getString("currentBalance");
                            }

                            adapterNotify();
                            txtCurrentBal.setText("Current Balance = "+currenBalance);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        GeneralUtilities.showMessage(context, parentLayout, response);
                    }
                } else {
                    GeneralUtilities.showMessage(context, parentLayout, " Please try again");
                }
            }

            @Override
            public void onVolleyError(String result) {
                GeneralUtilities.showMessage(context, parentLayout, " Please try again");
            }
        });
    }

    private void adapterNotify() {
        adapter = new EWalletHistoryAdapter(context, walletHistoryList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        walletListView.setLayoutManager(mLayoutManager);
        walletListView.setItemAnimator(new DefaultItemAnimator());
        walletListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

}
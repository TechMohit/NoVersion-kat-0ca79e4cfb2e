package cms.co.in.kat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import cms.co.in.kat.activity.ChangePassword;
import cms.co.in.kat.activity.ContactUs;
import cms.co.in.kat.activity.DashBoardCaseList;
import cms.co.in.kat.activity.LoginHome;
import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;

import static android.content.Context.MODE_PRIVATE;


public class FragmentProfile extends Fragment implements View.OnClickListener {

    private LoginHome context;
    private LinearLayout parentLayout;
    private Button passChange, verify;
    private TextView txtUserName;
    private String salutation = "", email = "", middleName = "", firstName = "", lastName = "", state = "", city = "",
            pincode = "", division = "", adress = "", subDivision = "", mobileNo = "", district = "", taluka = "",userName="";
    private EditText etSalutation, etFirstN, etMiddleN, etLastN, etPhone, etEmail, etAdress, etCity, etState, etPincode;
    private boolean emailVeify = false, mobileVerify = false;

    @NonNull
    private URLConstants urlConstants = new URLConstants();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initLayout(v);
        initListner();

        String userid = null;
        try {
            SharedPreferences prefs = context.getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
            userid = prefs.getString(Constant.MY_PREF_USERID, null);
            userName= prefs.getString(Constant.MY_PREF_USERNAME, null);
            txtUserName.setText(""+userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userid);
        Constant.CURRENT_TAG = urlConstants.USER_DETAIL_TAG;
        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.USER_DETAIL_URL, params);

        context.toolBarName("My Profile");

        return v;
    }

    private void initListner() {
        passChange.setOnClickListener(this);
        verify.setOnClickListener(this);
    }

    private void initLayout(View v) {
        parentLayout = (LinearLayout) v.findViewById(R.id.parentLayout);
        passChange = (Button) v.findViewById(R.id.pass_change);
        verify = (Button) v.findViewById(R.id.btn_verify);
        etSalutation = (EditText) v.findViewById(R.id.et_salutation);
        etFirstN = (EditText) v.findViewById(R.id.et_fName);
        etMiddleN = (EditText) v.findViewById(R.id.et_mName);
        etLastN = (EditText) v.findViewById(R.id.et_lName);
        etEmail = (EditText) v.findViewById(R.id.et_email);
        etPhone = (EditText) v.findViewById(R.id.et_phone);
        etAdress = (EditText) v.findViewById(R.id.et_adress);
        etCity = (EditText) v.findViewById(R.id.et_city);
        etState = (EditText) v.findViewById(R.id.et_state);
        etPincode = (EditText) v.findViewById(R.id.et_pincode);
        txtUserName=(TextView)v.findViewById(R.id.txt_user_name);
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
                case R.id.btn_verify:
                    if(!mobileVerify){
//                        HashMap<String, String> params = new HashMap<>();
//                        params.put("userId", userid);
//                        Constant.CURRENT_TAG = urlConstants.USER_DETAIL_TAG;
//                        volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.USER_DETAIL_URL, params);

                    }
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

                Log.e("*****", "** response profile detail *** " + response);

                if (tag == urlConstants.USER_DETAIL_TAG) {
                    if (result == 1) {
                        try {
                            JSONArray jArr = new JSONObject(response).getJSONArray("data_result");

                            try {
                                email = jArr.getJSONObject(0).getJSONObject("email").getString("emailId");
                                emailVeify = jArr.getJSONObject(0).getJSONObject("email").getBoolean("emailAddressVerfied");
                                middleName = jArr.getJSONObject(0).getString("middleName");
                                firstName = jArr.getJSONObject(0).getString("firstName");
                                lastName = jArr.getJSONObject(0).getString("lastName");
                                salutation = jArr.getJSONObject(0).getString("salutation");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                state = jArr.getJSONObject(0).getString("state");
                                Log.d("statetest",""+state);
                                pincode = jArr.getJSONObject(0).getString("pincode");
                                division = jArr.getJSONObject(0).getString("division");
                                subDivision = jArr.getJSONObject(0).getString("subDivision");
                                district = jArr.getJSONObject(0).getString("district");
                                taluka = jArr.getJSONObject(0).getString("taluk");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                mobileNo = jArr.getJSONObject(0).getJSONObject("mobile").getString("mobileNo");
                                mobileVerify = jArr.getJSONObject(0).getJSONObject("mobile").getBoolean("otpVerified");

                                city = jArr.getJSONObject(0).getString("city");
                                adress = jArr.getJSONObject(0).getString("address");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        updateValue();
//                        GeneralUtilities.showMessage(context, parentLayout, response);

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

    private void updateValue() {

        etSalutation.setText("" + salutation);
        etEmail.setText("" + email);
        GeneralUtilities gr = new GeneralUtilities(context);
        if (mobileVerify) {
//            verify.setVisibility(View.INVISIBLE);
            etPhone.setText(Html.fromHtml(gr.colorText(mobileNo, "  Verified",
                    getResources().getColor(R.color.green_cs))));
        } else {
//            verify.setVisibility(View.VISIBLE);
            etPhone.setText(Html.fromHtml(gr.colorText(mobileNo, "  NotVerify",
                    getResources().getColor(R.color.red))));
        }
        etFirstN.setText("" + firstName);
        etLastN.setText("" + lastName);
        etMiddleN.setText("" + middleName);
        etAdress.setText("" + adress);
        etPincode.setText("" + pincode);
        etCity.setText("" + city);
        etState.setText("" + state);
    }

//    String temp = "{\"message\":\"Success\",\"status\":\"1\",\"data_result\":[{\"middleName\":\"kumar\"," +
//            "\"lastName\":\"naik\",\"state\":\"karnataka\",\"city\":\"bangalore\",\"pincode\":560201,\"" +
//            "division\":\"hsr\",\"email\":{\"emailId\":\"subamnaik15@gmail.com\",\"emailAddressVerfied\":true}" +
//            ",\"address\":\"hsr layout\",\"subDivision\":\"bangalore\",\"firstName\":\"subham\",\"district\"" +
//            ":\"bangalore\",\"taluk\":\"bangalore\",\"mobile\":{\"otpVerified\":false,\"mobileNo\":\"9632209789\"}}]}\n";
}
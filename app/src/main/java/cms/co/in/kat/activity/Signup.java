package cms.co.in.kat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;

import cms.co.in.kat.asynchronous.Volley;
import cms.co.in.kat.interfaces.DialogListener;
import cms.co.in.kat.interfaces.VolleyListner;

import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.DpToPx;
import cms.co.in.kat.utils.ExceptionHandler;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.ShowDilog;
import cms.co.in.kat.utils.URLConstants;


public class Signup extends AppCompatActivity implements TextWatcher, View.OnClickListener {


    private TextView txt_header, txt_header1, txt_header2, txt_header3;
    private TextInputLayout et_fName_lay, et_lName_lay, et_uName_lay, et_password_lay, et_email_lay,
            et_mobile_lay, et_addr_lay, et_pincode_lay, et_village_lay, et_enroll_lay, et_opt_number_lay, et_opt_name_lay;
    @NonNull
    private URLConstants urlConst = new URLConstants();
    @NonNull
    private String firstName = "", lastName = "", userName = "", password = "", email = "", phone = "",
            address = "", pincode = "", division = "", state = "", subdivision = "",
            district = "", taluk = "", village = "", regNumber = "", userType = "", pincodeData = "", enrollTemp = "",uname;
    private int flag = 0;
    private Typeface face, faceLight;
    private EditText et_uName, et_fName, et_lName, et_password, et_email, et_mobileNo, et_pincode, et_village, et_addr,
            et_enroll, et_opt_name, et_opt_number;
    @NonNull
    private GeneralUtilities gr = new GeneralUtilities(this);
    private Button btn_reg_submit, verify;
    private View profile;
    private LinearLayout optional;
    private ShowDilog dilog = new ShowDilog();
    private URLConstants urlConstants = new URLConstants();
    private Spinner titlespin;



    @NonNull
   private String[] SPINNERLIST = {"Mr.", "Mrs.", "Miss.", "Shri.","Smt."};
    private MaterialBetterSpinner materialDesignSpinner;


    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdaptertitle;
    @NonNull
    private ArrayList<String> typelist = new ArrayList<>();

    //    @NonNull
//    private ArrayList<String> typelistId = new ArrayList<>();
    @NonNull
    private String type = "0", name = "", number = "";
    @NonNull
    private HashMap<String, String> typeHas = new HashMap<>();
    private RelativeLayout parentLayout;
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.act_sign_up);


        usertype();
        initLayout();
        initFontStyle();
        initColorText();
        initListner();
        initHtWt();
        setValue();

        // Spinner element
         titlespin = (Spinner) findViewById(R.id.spinnertitle);
         titlespin.setPrompt("Title");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SPINNERLIST);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        titlespin.setAdapter(dataAdapter);

        titlespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title = parent.getItemAtPosition(position).toString();
                Log.d("checkspin", title);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        //type = typeHas.get(materialDesignSpinnertitle.getText().toString());
      //  Log.d("spin","test"+type);



        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isSpaceChar(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        et_uName.setFilters(new InputFilter[]{filter });

    }

    private void setValue() {

        verify.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txt_header.setTransitionName(userType);
        }
        if (flag == Constant.SIGNUP_CITZEN) {

            et_password.setVisibility(View.VISIBLE);
            et_password_lay.setVisibility(View.VISIBLE);

            txt_header.setText(getResources().getString(R.string.citzen));

            profile.setVisibility(View.GONE);
        } else {
            et_password.setVisibility(View.GONE);
            et_password_lay.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
//
            if (flag == Constant.SIGNUP_ADVOCATE || flag == Constant.SIGNUP_CA) {
                et_enroll.setVisibility(View.VISIBLE);
                et_enroll_lay.setVisibility(View.VISIBLE);
                optional.setVisibility(View.GONE);

                if (flag == Constant.SIGNUP_ADVOCATE) {
                    verify.setVisibility(View.VISIBLE);
                    txt_header.setText(getResources().getString(R.string.advocate));

                    et_enroll.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.advocate_entroll))));
                } else if (flag == Constant.SIGNUP_CA) {
                    txt_header.setText(getResources().getString(R.string.ca));

                    et_enroll.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.ca_icai))));
                }

            } else {

                selectType();

                et_enroll.setVisibility(View.GONE);
                et_enroll_lay.setVisibility(View.GONE);
                optional.setVisibility(View.VISIBLE);

                if (flag == Constant.SIGNUP_COMPANIES) {
                    txt_header.setText(getResources().getString(R.string.companies));
                    et_opt_number.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.companies_number))));
                    et_opt_name.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.companies_name))));

                } else if (flag == Constant.SIGNUP_SOCIETIES) {
                    txt_header.setText(getResources().getString(R.string.society));

                    et_opt_number.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.socities_number))));
                    et_opt_name.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.socities_name))));
                }
            }
        }
    }

    private void selectType() {
        HashMap<String, String> params = new HashMap<>();
        if (flag == Constant.SIGNUP_SOCIETIES) {
            params.put("userType", "SocietyType");
        } else if (flag == Constant.SIGNUP_COMPANIES) {
            params.put("userType", "CompanyType");
        }
        Constant.CURRENT_TAG = urlConst.CS_TYPE_TAG;
        volly(urlConst.CS_TYPE_TAG, Request.Method.POST, urlConst.CS_TYPE_URL, params);

    }

    private void usertype() {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            userType = extras.getString("userType");
            flag = extras.getInt("flag");

        }

    }

    private void initHtWt() {
        ViewGroup.LayoutParams params = btn_reg_submit.getLayoutParams();
        params.height = (int) DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.btn_height));
        btn_reg_submit.setLayoutParams(params);
    }

    private void initListner() {

        btn_reg_submit.setOnClickListener(this);
        et_fName.addTextChangedListener(this);
        et_lName.addTextChangedListener(this);
        et_uName.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_mobileNo.addTextChangedListener(this);
        et_pincode.addTextChangedListener(this);
        et_village.addTextChangedListener(this);
        et_addr.addTextChangedListener(this);
        et_enroll.addTextChangedListener(this);


        verify.setOnClickListener(this);

        et_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                // this will show characters remaining
                validatePincode(s.toString());


            }
        });
    }

    private void initSize() {

        txt_header1.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_header)));
        txt_header2.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_header)));
        txt_header3.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_header)));

        et_fName.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_lName.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_uName.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_password.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_email.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_mobileNo.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_pincode.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_addr.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
        et_village.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));

        btn_reg_submit.setTextSize(DpToPx.dpToPixel(this, this.getResources().getInteger(R.integer.txt_normal)));
    }

    private void initColorText() {

//        eg_regType.setHint(Html.fromHtml(gr.colorText("eee")));
        et_fName.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.first_name))));
        et_lName.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.last_name))));
        et_uName.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.user_name))));
        et_password.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.password))));
        /*if (flag != Constant.SIGNUP_CITZEN) {
            et_email.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.email_id))));
        } else {
            et_email.setHint(getResources().getString(R.string.email_id));
        }*/
        et_email.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.email_id))));
        et_mobileNo.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.mobile_number))));
        et_pincode.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.pincode))));
        et_addr.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.address))));
        et_village.setHint(Html.fromHtml(gr.colorText(getResources().getString(R.string.village_city_town))));

    }

    private void initFontStyle() {
        face = Typeface.createFromAsset(getAssets(), Constant.FONT_MEDIUM);
        faceLight = Typeface.createFromAsset(getAssets(), Constant.FONT_LIGHT);
        txt_header1.setTypeface(face);
        txt_header.setTypeface(face);
        txt_header2.setTypeface(face);
        txt_header3.setTypeface(face);

//        eg_regType.setTypeface(face);
        et_fName.setTypeface(face);
        et_lName.setTypeface(face);
        et_uName.setTypeface(face);
        et_password.setTypeface(face);
        et_email.setTypeface(face);
        et_mobileNo.setTypeface(face);
        btn_reg_submit.setTypeface(face);
        et_pincode.setTypeface(face);
        et_addr.setTypeface(face);
        et_village.setTypeface(face);
        et_opt_name.setTypeface(face);
        et_opt_number.setTypeface(face);



    }

    private void initLayout() {

        verify = (Button) findViewById(R.id.verify);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);

        profile = findViewById(R.id.profile);
        optional = (LinearLayout) findViewById(R.id.optional);
        et_opt_name = (EditText) findViewById(R.id.et_opt_name);
        et_opt_number = (EditText) findViewById(R.id.et_opt_number);
        et_opt_number_lay = (TextInputLayout) findViewById(R.id.et_opt_number_lay);
        et_opt_name_lay = (TextInputLayout) findViewById(R.id.et_opt_name_lay);

        materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.android_material_design_spinner);
       // materialDesignSpinnertitle = (Spinner) findViewById(R.id.android_material_design_spinner_title);

        txt_header = (TextView) findViewById(R.id.txt_head);
        txt_header1 = (TextView) findViewById(R.id.txt_head1);
        txt_header2 = (TextView) findViewById(R.id.txt_head2);
        txt_header3 = (TextView) findViewById(R.id.txt_head3);

        et_fName_lay = (TextInputLayout) findViewById(R.id.et_fName_lay);
        et_lName_lay = (TextInputLayout) findViewById(R.id.et_lName_lay);
        et_uName_lay = (TextInputLayout) findViewById(R.id.et_uName_lay);
        et_password_lay = (TextInputLayout) findViewById(R.id.et_password_lay);
        et_email_lay = (TextInputLayout) findViewById(R.id.et_email_lay);
        et_mobile_lay = (TextInputLayout) findViewById(R.id.et_mobile_lay);
        et_addr_lay = (TextInputLayout) findViewById(R.id.et_addr_lay);
        et_pincode_lay = (TextInputLayout) findViewById(R.id.et_pincode_lay);
        et_village_lay = (TextInputLayout) findViewById(R.id.et_village_lay);
        et_enroll_lay = (TextInputLayout) findViewById(R.id.et_enroll_lay);

//        eg_regType = (EditText) findViewById(R.id.eg_regType);
        et_fName = (EditText) findViewById(R.id.et_fName);
        et_lName = (EditText) findViewById(R.id.et_lName);
        et_uName = (EditText) findViewById(R.id.et_uName);
        et_password = (EditText) findViewById(R.id.et_password);
        et_mobileNo = (EditText) findViewById(R.id.et_mobileNo);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pincode = (EditText) findViewById(R.id.et_pincode);
        et_addr = (EditText) findViewById(R.id.et_addr);
        et_village = (EditText) findViewById(R.id.et_village);
        et_enroll = (EditText) findViewById(R.id.et_enroll);

        btn_reg_submit = (Button) findViewById(R.id.btn_reg_submit);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        eg_regType_.setError(null);
        et_enroll_lay.setError(null);
        et_fName_lay.setError(null);
        et_lName_lay.setError(null);
        et_uName_lay.setError(null);
        et_password_lay.setError(null);
        et_email_lay.setError(null);
        et_mobile_lay.setError(null);
        et_pincode_lay.setError(null);
        et_addr_lay.setError(null);
        et_village_lay.setError(null);
        et_opt_name_lay.setError(null);
        et_opt_number_lay.setError(null);

    }

    private boolean validatePincode(@NonNull String pincode) {
        if (pincode.length() == 6) {
            if (ShowDilog.internet) {
                getPincodeData(pincode);
            } else {
                GeneralUtilities.showMessage(Signup.this, parentLayout,getResources().getString(R.string.internet));
            }
            return true;
        } else {
            return false;
        }
    }

    private void getPincodeData(String pincode) {
        this.pincode = pincode;
        HashMap<String, String> params = new HashMap<>();
        params.put("pincode", this.pincode);
        Constant.CURRENT_TAG = urlConst.PINCODE_TAG;
        volly(urlConst.PINCODE_TAG, Request.Method.POST, urlConst.PINCODE_URL, params);
    }

    @Override
    public void onClick(@NonNull View v) {

        regNumber = et_enroll.getText().toString();


        if (flag == Constant.SIGNUP_COMPANIES || flag == Constant.SIGNUP_SOCIETIES) {
            name = et_opt_name.getText().toString();
            number = et_opt_number.getText().toString();
            type = typeHas.get(materialDesignSpinner.getText().toString());
//            Toast.makeText(this, "" + number, Toast.LENGTH_SHORT).show();



        }
        firstName = et_fName.getText().toString();
        lastName = et_lName.getText().toString();
        userName = et_uName.getText().toString();
        uname = et_uName.getText().toString().trim();
        try {
            password = et_password.getText().toString();
        } catch (Exception e) {

        }
        email = et_email.getText().toString();
        phone = et_mobileNo.getText().toString();
        pincode = et_pincode.getText().toString();
        address = et_addr.getText().toString();
        village = et_village.getText().toString();

        Intent i;
        if (ShowDilog.internet) {
            switch (v.getId()) {
                case R.id.btn_reg_submit:
                   if (emptyCheck() && validation()) {


                        HashMap<String, String> params = new HashMap<>();
                        params.put("icaiNumber", flag == Constant.SIGNUP_CA ? regNumber : "");
                        params.put("enrollmentNumber", flag == Constant.SIGNUP_ADVOCATE ? regNumber : "");


                        params.put("companyName", flag == Constant.SIGNUP_COMPANIES ? name : "");
                        params.put("tinNumber", flag == Constant.SIGNUP_COMPANIES ? number : "");
                        params.put("companyType", flag == Constant.SIGNUP_COMPANIES ? type : "0");

                        params.put("societyName", flag == Constant.SIGNUP_SOCIETIES ? name : "");
                        params.put("registrationNumber", flag == Constant.SIGNUP_SOCIETIES ? number : "");
                        params.put("societyType", flag == Constant.SIGNUP_SOCIETIES ? type : "0");


                        params.put("userType", userType);
                        params.put("salutation", title);

                        params.put("middleName", "");

                        params.put("firstName", firstName);
                        params.put("lastName", lastName);
                        params.put("userName", userName);
                        params.put("emailAddress", email);
                        params.put("password", password);
                        params.put("mobileNumber", phone);
                        params.put("address", address);

                        params.put("pincode", pincode);
                        params.put("division", division);
                        params.put("subDivision", subdivision);
                        params.put("district", district);
                        params.put("taluk", taluk);
                        params.put("village", village);
                        params.put("state", state);

                        Log.e(urlConst.REGISTER_TAG, "" + urlConst.REGISTER_URL + "\n" + "" + urlConst.REGISTER_URL + "\n" +
                                "Params = " + userType + " , " + regNumber + " , " + firstName + " , " + lastName + " , " + userName + " , " + email + " , " + password +
                                " , " + phone + " , " + pincode + " , " + address + " , " + village);
                        Constant.CURRENT_TAG = urlConst.REGISTER_TAG;
                        /*dilog.createDialog(Signup.this,Constant.DILOG_OTP, new DialogListener() {
                            @Override
                            public void onFinish(int code, String user_id) {
                                Log.d("resendclick","registrationfinish"+userName);
                                if (code == Constant.DILOG_RESEND && user_id == null) {
                                    Log.d("resendclick","registrationfinish1");
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("userId", userName);
                                    System.out.println(" Tag Constant.DILOG_RESEND -  Params " + params);
                                    Constant.CURRENT_TAG = urlConstants.FORGOT_PSWD_TAG;
                                    volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.FORGOT_PSWD_URL, params);

                                }

                            }
                        });*/
                       volly(urlConst.REGISTER_TAG, Request.Method.POST, urlConst.REGISTER_URL, params);
                       // Toast.makeText(this,"Done",Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.verify:
                    if (flag == Constant.SIGNUP_ADVOCATE) {
                        if (regNumber != null && regNumber.equalsIgnoreCase("")) {
                            et_enroll_lay.setErrorEnabled(true);
                            et_enroll_lay.setError("Empty Enrollment Number");
                            GeneralUtilities.showMessage(Signup.this, parentLayout,"Please Enter Enrollment Number");

                        } else {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("enrollNumber", regNumber);
                            Constant.CURRENT_TAG = urlConst.ENROLLMENT_CHECK_TAG;
                            volly(urlConst.ENROLLMENT_CHECK_TAG, Request.Method.POST, urlConst.ENROLLMENT_URL, params);

                        }
                    }
                    break;
            }
        } else {

           GeneralUtilities.showMessage(Signup.this, parentLayout,getResources().getString(R.string.internet));

        }
    }

    private boolean validation() {

        GeneralUtilities util = new GeneralUtilities(this);
//        if (!email.equalsIgnoreCase("") && !util.validateEmailAddress(email)) {
//            et_email_lay.setErrorEnabled(true);
//            et_email_lay.setError("Email ID is not correct");
//            return false;
//        } else

        if (!util.validatePhoneNumber(phone)) {
            et_mobile_lay.setErrorEnabled(true);
            et_mobile_lay.setError("Not a valid Mobile Number");
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return ( Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean emptyCheck() {


        if (flag == Constant.SIGNUP_ADVOCATE || flag == Constant.SIGNUP_CA) {
            if (regNumber.equalsIgnoreCase("")) {
                et_enroll_lay.setErrorEnabled(true);
                et_enroll_lay.setError("Please Enter value");
                return false;
            }
            if (flag == Constant.SIGNUP_ADVOCATE && !enrollTemp.equalsIgnoreCase(regNumber)) {
                et_enroll_lay.setErrorEnabled(true);
                et_enroll_lay.setError("Please Verify Enrollment number");
                return false;
            }
            if (email.equalsIgnoreCase("")) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Your Email");
                return false;
            }
            if (!isValidEmail(email)) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Valid Email");
                return false;
            }

        }

        if (flag == Constant.SIGNUP_SOCIETIES || flag == Constant.SIGNUP_COMPANIES) {

            if (name.equalsIgnoreCase("")) {
                et_opt_name_lay.setErrorEnabled(true);
                et_opt_name_lay.setError("Enter Your FirstName");
                return false;
            }
            if (number.equalsIgnoreCase("")) {
                et_opt_number_lay.setErrorEnabled(true);
                et_opt_number_lay.setError("Enter Your PhoneNumber");
                return false;
            }
            if (type.equalsIgnoreCase("0")) {
                GeneralUtilities.showMessage(Signup.this, parentLayout, "Please Enter Type ");
                return false;
            }

            if (email.equalsIgnoreCase("")) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Your Email");
                return false;
            }
            if (!isValidEmail(email)) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Valid Email");
                return false;
            }
        }
        if (flag == Constant.SIGNUP_CITZEN) {
            if (firstName.equalsIgnoreCase("")) {
                et_fName_lay.setErrorEnabled(true);
                et_fName_lay.setError("Enter Your FirstName");
                return false;
            }
            else if (firstName.length() <4|| firstName.length() >150 ) {
                et_fName_lay.setErrorEnabled(true);
                et_fName_lay.setError("You must have enter valid FirstName");
                return false;
            }
            else if (lastName.equalsIgnoreCase("")) {
                et_lName_lay.setErrorEnabled(true);
                et_lName_lay.setError("Enter Your LastName");
                return false;
            }
            else if (lastName.length() <4|| lastName.length() >150 ) {
                et_lName_lay.setErrorEnabled(true);
                et_lName_lay.setError("You must have enter valid LastName");
                return false;
            }
            else if (userName.equalsIgnoreCase("")) {
                et_uName_lay.setErrorEnabled(true);
                et_uName_lay.setError("Enter Your UserName");
                return false;
            }
            else if(uname.matches("")){
                et_uName_lay.setErrorEnabled(true);
                et_uName_lay.setError("Enter Your UserName not spaces");
                return false;

            }
            else if (password.equalsIgnoreCase("")) {
                et_password_lay.setErrorEnabled(true);
                et_password_lay.setError("Enter Your Password");
                return false;
            }else if(password.length()<8){
                et_password_lay.setErrorEnabled(true);
                et_password_lay.setError("Password must contain minimum of 8 characters");
                return false;
            }else if(!GeneralUtilities.validatePassword(password)){
                et_password_lay.setErrorEnabled(true);
                et_password_lay.setError("Password must contain atleast 1 lower case, 1 upper case, 1 number & 1 special character");
                return false;
            }


            /*else if (email.length() <4|| email.length() >150 ) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("You must have enter valid  Email Address");
                return false;
            }*/

            else if (email.equalsIgnoreCase("")) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Your Email");
                return false;
            }
            else if (!isValidEmail(email)) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("Enter Valid Email");
                return false;
            }

             else if (email.length() <4|| email.length() >150 ) {
                et_email_lay.setErrorEnabled(true);
                et_email_lay.setError("You must have enter valid  Email Address");
                return false;
            }
            else if (address.equalsIgnoreCase("")) {
                et_addr_lay.setErrorEnabled(true);
                et_addr_lay.setError("Enter Your Address");
                return false;
            }
             else if (address.length() <4|| address.length() >150 ) {
                et_addr_lay.setErrorEnabled(true);
                et_addr_lay.setError("You must have enter valid Address");
                return false;
            }
        }


        if (firstName.equalsIgnoreCase("")) {
            et_fName_lay.setErrorEnabled(true);
            et_fName_lay.setError("Enter Your FirstName");
            return false;
        } else if (lastName.equalsIgnoreCase("")) {
            et_lName_lay.setErrorEnabled(true);
            et_lName_lay.setError("Enter Your LastName");
            return false;
        } else if (userName.equalsIgnoreCase("")) {
            et_uName_lay.setErrorEnabled(true);
            et_uName_lay.setError("Enter Your UserName");
            return false;
        } else if (flag != Constant.SIGNUP_CITZEN && email.equalsIgnoreCase("")) {
            et_email_lay.setErrorEnabled(true);
            et_email_lay.setError("Enter Your Email");
            return false;
        } else if (phone.equalsIgnoreCase("")) {
            et_mobile_lay.setErrorEnabled(true);
            et_mobile_lay.setError("Enter Your Phone");
            return false;
        } else if (address.equalsIgnoreCase("")) {
            et_addr_lay.setErrorEnabled(true);
            et_addr_lay.setError("Enter Your Address");
            return false;
        } else if (pincode.equalsIgnoreCase("") || pincode.length()!=6) {
            et_pincode_lay.setErrorEnabled(true);
            et_pincode_lay.setError("Enter Correct Pincode");
            return false;
        } else if (village.equalsIgnoreCase("")) {
            et_village_lay.setErrorEnabled(true);
            et_village_lay.setError("Enter Your Village");
            return false;
        }

        else if (userName!=null && userName.length()!=8) {
            et_uName_lay.setErrorEnabled(true);
            et_uName_lay.setError("User Name must contain exactly 8 characters");
            return false;
        }
        return true;
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

                if (tag == urlConst.CS_TYPE_TAG) {
                    Log.e(urlConst.CS_TYPE_TAG, "cs type code " + response);
                    if (result == 1) {
                        try {
                            JSONObject root = new JSONObject(response);
                            JSONArray jsonarray = root.getJSONArray("data_result");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String name = jsonobject.getString("name");
//                                Log.e("********","*********"+name+"\n"+id);
                                typelist.add(name);
//                                typelistId.add(id);
                                typeHas.put(name, id);
                            }
                            arrayAdapter = new ArrayAdapter<>(Signup.this, android.R.layout.simple_dropdown_item_1line, typelist);

                            materialDesignSpinner.setAdapter(arrayAdapter);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        finish();
                    }
                } else if (tag == urlConst.REGISTER_TAG) {
                    Log.e(urlConst.REGISTER_TAG, "result code " + response);
                    if (result == 1) {

                        dilog.createDialog(Signup.this, Constant.DILOG_OTP, new DialogListener() {
                            @Override
                            public void onFinish(int code, String data) {
                                if (code == Constant.DILOG_RESEND && data == null) {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("userId", userName);
                                    System.out.println(" Tag Constant.DILOG_RESEND -  Params " + params);
                                    Constant.CURRENT_TAG = urlConstants.FORGOT_PSWD_TAG;
                                    volly(Constant.CURRENT_TAG, Request.Method.POST, urlConstants.FORGOT_PSWD_URL, params);

                                } else {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("userName", userName);
                                    params.put("otp", data);
                                    Constant.CURRENT_TAG = urlConst.OTP_VERIFY_TAG;
                                    volly(urlConst.OTP_VERIFY_TAG, Request.Method.POST, urlConst.OTP_VERIFY_URL, params);
                                }
                            }
                        });

                    } else {
                        Log.d("regtest","00"+response);
                        GeneralUtilities.showMessage(Signup.this, parentLayout, response);

                    }
                } else if (tag == urlConst.OTP_VERIFY_TAG) {
                    Log.e(urlConst.OTP_VERIFY_TAG, "result code " + response);

                    if (result == 1) {
                        GeneralUtilities.showMessage(Signup.this, parentLayout, "OTP Verify SucessFully");


                        if (flag != Constant.SIGNUP_CITZEN) {
                            GeneralUtilities.showMessage(Signup.this, parentLayout, "Please Verify Your Email-ID");

                        } else if (!email.equalsIgnoreCase("")) {
                            GeneralUtilities.showMessage(Signup.this, parentLayout, "Please Verify Your Email-ID");

                        }
                        setemailverify();
                        /*Intent i = new Intent(Signup.this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();*/

                    } else {
                        dilog.otpNotValid();
                        GeneralUtilities.showMessage(Signup.this, parentLayout, response);

                    }
                } else if (tag == urlConst.PINCODE_TAG) {
                    Log.e(urlConst.PINCODE_TAG, "result code " + response);

                    if (result == 1) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            division = jObj.getJSONArray("data_result").getJSONObject(0).getString("division");
                            state = jObj.getJSONArray("data_result").getJSONObject(0).getString("state");
                            subdivision = jObj.getJSONArray("data_result").getJSONObject(0).getString("sudivision");
                            district = jObj.getJSONArray("data_result").getJSONObject(0).getString("district");
                            taluk = jObj.getJSONArray("data_result").getJSONObject(0).getString("taluk");
                           // GeneralUtilities.showMessage(Signup.this, parentLayout,"Pincode is Correct");


                            pincodeData = "\ndivision :" + division
                                    + "\nstate :" + state
                                    + "\nsubdivision :" + subdivision
                                    + "\ndistrict :" + district
                                    + "\ntaluk :" + taluk;
//                            btn_reg_submit.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
//                        btn_reg_submit.setEnabled(false);
                        division="";
                        state="";
                        subdivision="";
                        district="";
                        taluk="";
                        pincodeData = "";
                    }
                } else if (tag == urlConst.ENROLLMENT_CHECK_TAG) {
                    Log.e(urlConst.ENROLLMENT_CHECK_TAG, "cs type code " + response);
                    if (result == 1) {
                        enrollTemp = regNumber;
                        try {
//
//                            "pincode": "563     101",
//                                    "email": "email@mail.com",
//                                    "name": "VENKATARAJU         K    M.",
//                                    "communicationAddress": "NO.  161,           2ND MAIN ROAD,",
//                                    "communicationState": "16",
//                                    "mobileNo": "9900123456",
//                                    "city": "KOLAR"
                            JSONObject root = new JSONObject(response);
                            JSONArray jsonarray = root.getJSONArray("data_result");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                if (!jsonobject.isNull("email")) {
                                    String email = jsonobject.getString("email");
                                    et_email.setText(email);
                                }
                                if (!jsonobject.isNull("name")) {
                                    String name = jsonobject.getString("name");
                                    et_fName.setText(name);
                                }
                                if (!jsonobject.isNull("communicationAddress")) {
                                    String communicationAddress = jsonobject.getString("communicationAddress");
                                    et_addr.setText(communicationAddress);
                                }
                                if (!jsonobject.isNull("communicationState")) {
                                    String communicationState = jsonobject.getString("communicationState");
                                    state = communicationState;
                                }
                                if (!jsonobject.isNull("mobileNo")) {
                                    String mobileNo = jsonobject.getString("mobileNo");
                                    et_mobileNo.setText(mobileNo);
                                }
                                if (!jsonobject.isNull("city")) {
                                    String city = jsonobject.getString("city");
                                    et_village.setText(city);
                                }
                                if (!jsonobject.isNull("pincode")) {
                                    String pincode = jsonobject.getString("pincode").replaceAll("\\s+", "");
                                    et_pincode.setText(pincode);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            regNumber = "";
                            enrollTemp = "";
                            et_enroll.setText("");
                        }
                    } else {
                        regNumber = "";
                        enrollTemp = "";
                        et_enroll.setText("");

                        et_enroll_lay.setErrorEnabled(true);
                        et_enroll_lay.setError("Enrollment number is invalid");
                        GeneralUtilities.showMessage(Signup.this, parentLayout,"Enrollment number is invalid");

                    }
                }
            }

            @Override
            public void onVolleyError(String result) {

                if (tag == urlConst.CS_TYPE_TAG) {
                    finish();
                }
                Log.e(urlConst.REGISTER_TAG, "" + result);
                GeneralUtilities.showMessage(Signup.this, parentLayout,"Please Try again");
            }
        });
    }

    private void setemailverify() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sign Up Successful Please Verify Email to Proceed");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //Toast.makeText(Signup.this,"You clicked yes ",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Signup.this, Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}

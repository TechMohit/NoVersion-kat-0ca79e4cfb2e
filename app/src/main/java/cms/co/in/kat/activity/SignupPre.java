package cms.co.in.kat.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cms.kat.cws.R;

import cms.co.in.kat.utils.Constant;

public class SignupPre extends AppCompatActivity {

    private TextView btnSociety, btnCitizen, btnAdvocate, btnCaOrStp, btnCompanies,txt_head;
    private String userType;
    private int flag;
    private Typeface face;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_pre);
        initLayout();
        initFontStyle();
        setListner();
    }

    private void setListner() {
        btnCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = Constant.SIGNUP_CITZEN;
//                txtRegTypeTitle.setVisibility(View.GONE);
//                eg_regType.setVisibility(View.GONE);
//                dialog.dismiss();
                userType = "Citizen";
                setIntent(btnCitizen,flag,userType);

            }
        });
        btnAdvocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = Constant.SIGNUP_ADVOCATE;
//
//                txtRegTypeTitle.setVisibility(View.VISIBLE);
//                eg_regType.setVisibility(View.VISIBLE);
//                txtRegTypeTitle.setText("Enter Enrollment-Number");
//                dialog.dismiss();
                userType = "Advocate";
                setIntent(btnAdvocate,flag,userType);

            }
        });
        btnCaOrStp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = Constant.SIGNUP_CA;
//
//                txtRegTypeTitle.setVisibility(View.VISIBLE);
//                eg_regType.setVisibility(View.VISIBLE);
//                txtRegTypeTitle.setText("Enter ICAI-Number");
//                dialog.dismiss();
                userType = "CA/STP";
                setIntent(btnCaOrStp,flag,userType);

            }
        });
        btnCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = Constant.SIGNUP_COMPANIES;
//
//                txtRegTypeTitle.setVisibility(View.VISIBLE);
//                eg_regType.setVisibility(View.VISIBLE);
//                txtRegTypeTitle.setText("Enter Company Id");
//                dialog.dismiss();
                userType = "Companies";
                setIntent(btnCompanies,flag,userType);

            }
        });
        btnSociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = Constant.SIGNUP_SOCIETIES;
//
//                txtRegTypeTitle.setVisibility(View.VISIBLE);
//                eg_regType.setVisibility(View.VISIBLE);
//                txtRegTypeTitle.setText("Enter Company Id");
//                dialog.dismiss();
                userType = "Societies";
                setIntent(btnSociety,flag,userType);

            }
        });
    }

    private void setIntent(@NonNull TextView view, int flag, String userType) {

        Intent i = new Intent(SignupPre.this, Signup.class);
        i.putExtra("userType",userType);
        i.putExtra("flag", flag);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(userType);

            Pair<View, String> p1 = Pair.create((View)logo, "logo");
            Pair<View, String> p2 = Pair.create((View)view, userType);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    SignupPre.this,
                    p1,p2);
            startActivity(i,options.toBundle());

        }else{
            startActivity(i);
        }

    }

    private void initFontStyle() {
        face = Typeface.createFromAsset(getAssets(), Constant.FONT_MEDIUM);
        btnCitizen.setTypeface(face);
        btnSociety.setTypeface(face);
        btnAdvocate.setTypeface(face);
        btnCaOrStp.setTypeface(face);
        btnCompanies.setTypeface(face);
        txt_head.setTypeface(face);

    }

    private void initLayout() {
        logo=(ImageView)findViewById(R.id.logo);
        btnCitizen = (TextView) findViewById(R.id.btn_citizen);
        btnAdvocate = (TextView) findViewById(R.id.btn_advocate);
        btnCaOrStp = (TextView) findViewById(R.id.btn_ca_or_stp);
        btnCompanies = (TextView) findViewById(R.id.btn_companies);
        btnSociety = (TextView) findViewById(R.id.btn_societies);
        txt_head = (TextView) findViewById(R.id.txt_head);

    }
}
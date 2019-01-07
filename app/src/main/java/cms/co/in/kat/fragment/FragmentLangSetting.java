package cms.co.in.kat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import cms.co.in.kat.activity.DashBoardCaseList;
import cms.co.in.kat.activity.LoginHome;
import cms.co.in.kat.activity.SplashScreen;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.database.DatabaseHandler;
import cms.co.in.kat.utils.Constant;
import cms.co.in.kat.utils.GeneralUtilities;
import cms.co.in.kat.utils.InternetCheck;
import cms.co.in.kat.utils.LangConstant;
import cms.co.in.kat.utils.ShowDilog;

import static android.content.Context.MODE_PRIVATE;


public class FragmentLangSetting extends Fragment implements View.OnClickListener {

    private LoginHome context;
    private LinearLayout lang_en, lang_ka;
    public static int LangCurrent = 1;
    private SharedPreferences sharepref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lang, container, false);

        initLayout(v);
        initListner();
        context.toolBarName("Change Langauage");
        return v;
    }

    private void initListner() {

        lang_en.setOnClickListener(this);
        lang_ka.setOnClickListener(this);

    }

    private void initLayout(View v) {
        lang_en = (LinearLayout) v.findViewById(R.id.lang_en);
        lang_ka = (LinearLayout) v.findViewById(R.id.lang_ka);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (LoginHome)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        sharepref = context.getSharedPreferences(Constant.MY_PREF_LANG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();

        switch (v.getId()) {

            case R.id.lang_en:

                LangCurrent = LangConstant.Lang_English;
                editor = sharepref.edit();
                editor.putInt(Constant.MY_PREF_CURRENT_LANG, LangCurrent);
                editor.apply();
//                    Toast.makeText(context, "English", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lang_ka:
                LangCurrent = LangConstant.Lang_Kannada;
               editor = sharepref.edit();
                editor.putInt(Constant.MY_PREF_CURRENT_LANG, LangCurrent);
                editor.apply();
//                    Toast.makeText(context, "Kannada", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
package cms.co.in.kat.fragment.onboardingFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.kat.cws.R;

import cms.co.in.kat.activity.OnBoardingActivity;
import cms.co.in.kat.utils.Constant;

public class OnboardingFragment2 extends Fragment  {

    private View view;
    private OnBoardingActivity activity;
    private TextView headTxt, detailsTxt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(
                R.layout.frag_onboardingscreentwo,
                container,
                false
        );
        findViewByID();
        setTextFont();
        return view;
    }
    private void setTextFont() {
        Typeface faceLight = Typeface.createFromAsset(activity.getAssets(), Constant.FONT_LIGHT);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), Constant.FONT_MEDIUM);

        headTxt.setTypeface(face);
        detailsTxt.setTypeface(faceLight);
        headTxt.setText("");
        detailsTxt.setText("");
    }

    private void findViewByID() {
        headTxt = (TextView) view.findViewById(R.id.textSplashHeader);
        detailsTxt = (TextView) view.findViewById(R.id.textSplashDetails);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (OnBoardingActivity) context;
    }
}

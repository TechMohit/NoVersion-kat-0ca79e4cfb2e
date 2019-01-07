package cms.co.in.kat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.cms.kat.cws.R;

import cms.co.in.kat.customview.CircleIndicatorView;
import cms.co.in.kat.customview.Demo;
import cms.co.in.kat.fragment.onboardingFragment.OnboardingFragment1;
import cms.co.in.kat.fragment.onboardingFragment.OnboardingFragment2;
import cms.co.in.kat.fragment.onboardingFragment.OnboardingFragment3;
import cms.co.in.kat.utils.Constant;

public class OnBoardingActivity extends FragmentActivity {

    private ViewPager pager;
    private boolean isTouched = true;
    private SharedPreferences sharepref;
    private int currentPage = 0;
    private CircleIndicatorView circleIndicatorView;
    private TextView txtgetstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding);
        findViewById();
        setStatusBackgroundColor();
        clicklistener();
        sharepref = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
        txtgetstarted.setText("GET STARTED");
        initFontStyle();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new OnboardingFragment1();
                    case 1:
                        return new OnboardingFragment2();
                    case 2:
                        return new OnboardingFragment3();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new Demo());


        pager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isTouched = false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isTouched = true;
                }
                return false;

            }
        });


        circleIndicatorView.setPageIndicators(3);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                circleIndicatorView.setCurrentPage(position);
                if (position == 2) {
                    showReveal(txtgetstarted);
                } else if (position == 1) {
                    if ((txtgetstarted).getVisibility() == View.VISIBLE)
                        exitReveal(txtgetstarted);
                    else
                        txtgetstarted.setVisibility(View.INVISIBLE);

                } else {
                    txtgetstarted.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void clicklistener() {
        txtgetstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, Login.class));
                onboardingPreferenceAdded();
                finish();
            }
        });
    }

    private void onboardingPreferenceAdded() {
        SharedPreferences.Editor editor = sharepref.edit();
        editor.putBoolean(Constant.ONBOARDING_FLAG, true);
        editor.commit();

    }

    private void findViewById() {
        circleIndicatorView = (CircleIndicatorView) findViewById(R.id.indicator);
        txtgetstarted = (TextView) findViewById(R.id.txtgetstarted);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void initFontStyle() {
       Typeface faceLight = Typeface.createFromAsset(getAssets(), Constant.FONT_LIGHT);
        txtgetstarted.setTypeface(faceLight);
    }
    public float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


    private void setStatusBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
        }
    }

    void showReveal(View v) {

        // get the center for the clipping circle
        int cx = v.getMeasuredWidth() / 2;
        int cy = v.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(v.getWidth(), v.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);


            // make the view visible and start the animation
            v.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            v.setVisibility(View.VISIBLE);

        }
    }

    void exitReveal(final View mLoadViewLong) {

        // get the center for the clipping circle
        int cx = mLoadViewLong.getMeasuredWidth() / 2;
        int cy = mLoadViewLong.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = mLoadViewLong.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(mLoadViewLong, cx, cy, initialRadius, 0);


            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mLoadViewLong.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
//            TranslateAnimation slide = new TranslateAnimation(0,0,0,100 );
//            slide.setDuration(100);
//            slide.setFillAfter(false);
//            mLoadViewLong.startAnimation(slide);
            mLoadViewLong.setVisibility(View.INVISIBLE);
        }
        // start the animation

    }
//    public void setListener(Respond res){
//        this.res=res;
//    }
}

package cms.co.in.kat.customview;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cms.kat.cws.R;


/**
 * Created by Satyam on 08/01/16.
 */
public class Demo implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.15f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        ImageView whole = (ImageView) view.findViewById(R.id.onbording_img);
        TextView textSplashHeader = (TextView) view.findViewById(R.id.textSplashHeader);
        TextView textSplashDetails = (TextView) view.findViewById(R.id.textSplashDetails);


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            if (whole != null) {

                whole.setTranslationX(0);
                textSplashHeader.setTranslationX(0);
                textSplashDetails.setTranslationX(0);

            }

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page


            doTrans(view, position, whole);
            doTrans(view, position, textSplashHeader);
            doTrans(view, position, textSplashDetails);


        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            doTrans(view, position, whole);
            doTrans(view, position, textSplashHeader);
            doTrans(view, position, textSplashDetails);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            if (whole != null) {

                whole.setTranslationX(0);
                textSplashHeader.setTranslationX(0);
                textSplashDetails.setTranslationX(0);
            }
        }
    }

    private void doTrans(View view, float position, View description) {
        if (position != 0) {

            final float translationX = view.getWidth() * position;
            description.setTranslationX(-translationX);

        } else {

            description.setTranslationX(0);
        }
    }

}

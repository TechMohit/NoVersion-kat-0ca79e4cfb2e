package cms.co.in.kat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


import com.cms.kat.cws.R;

import cms.co.in.kat.customview.AVLoadingIndicatorView;

/**
 * Created by Happy on 20-02-2017.
 */

public class LoadingView {

    private Context mcontext;
    private Dialog dialog;
    private AVLoadingIndicatorView load;

    public LoadingView(Context context) {

        this.mcontext = context;
        if(context!=null) {
            dialog = new Dialog(mcontext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_loading);
            load = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
            load.setIndicator("BallPulseIndicator");
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    public void showLoading() {
        try {
            if (dialog != null && !dialog.isShowing())
                dialog.show();
            if(load!=null)
            load.smoothToShow();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissLoading() {
//        if (dialog != null && dialog.isShowing())
//            dialog.dismiss();

        if(dialog != null) {
            if(dialog.isShowing()) { //check if dialog is showing.

                try {
                    //get the Context object that was used to great the dialog
                    Context context = ((ContextWrapper)dialog.getContext()).getBaseContext();

                    //if the Context used here was an activity AND it hasn't been finished or destroyed
                    //then dismiss it
                    if(context instanceof Activity) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                                dialog.dismiss();
                        }
                    } else {//if the Context used wasnt an Activity, then dismiss it too
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialog = null;
        }


        if(load!=null)
        load.smoothToHide();
    }
}

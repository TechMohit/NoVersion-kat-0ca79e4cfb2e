package cms.co.in.kat.asynchronous;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cms.co.in.kat.activity.MyApplication;
import cms.co.in.kat.interfaces.VolleyListner;
import cms.co.in.kat.utils.LoadingView;
import cms.co.in.kat.utils.URLConstants;

/**
 * Created by Happy on 01-03-2017.
 */

public class Volley {

    private Context mcontext;
    private String TAG = Volley.class.getSimpleName();
    private LoadingView load;
    private boolean showDilog = false;
    private URLConstants urlConstants = new URLConstants();


    public Volley(Context context) {
        this.mcontext = context;
        this.load = new LoadingView(mcontext);
    }

    private void showProgressDialog() {
        load.showLoading();
    }

    private void dismissProgressDialog() {
        load.dismissLoading();
    }

    public void makeJsonArryReq(final String tag, String url, final HashMap<String, String> params, @NonNull final VolleyListner mlistner) {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(@NonNull JSONArray response) {
                        Log.d(TAG, response.toString());
                        dismissProgressDialog();
                        mlistner.onVolleyRespondJSONArray(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dismissProgressDialog();
                mlistner.onVolleyError(error.getMessage());

            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {

                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(req, tag);
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    /**
     * Making json String request
     */
    public void makeStringReq(final String tag, int getPost, String url, final HashMap<String, String> params, @NonNull final VolleyListner mlistner) {

        if(tag.equalsIgnoreCase(urlConstants.CASE_SEARCH_AUTO_TAG)||tag.equalsIgnoreCase(urlConstants.COURT_LCD_TAG)
                ||tag.equalsIgnoreCase(urlConstants.NOTIFICATION_API_TAG)
                ){
            showDilog=true;
        }

        if (!showDilog) {
            showProgressDialog();
            showDilog = true;
        }
        StringRequest strReq = new StringRequest(getPost,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (showDilog) {
                    dismissProgressDialog();
                    showDilog = false;
                }
                MyApplication.getInstance().setRequestQueue();

//                if(!tag.equalsIgnoreCase(urlConstants.NOTIFICATION_TAG)) {

                    Log.d(TAG, response);
                    String status = "0", message = "";
                    JSONObject myJson = null;
                    try {
                        myJson = new JSONObject(response);
                        status = myJson.optString("status");
                        message = myJson.optString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (status.equalsIgnoreCase("1")) {
                        mlistner.onVolleyRespondString(1, response);
                    } else {
                        mlistner.onVolleyRespondString(0, message);
                    }
//                }else{
//                    Log.e("**** Notify ","***** "+tag+response);
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                MyApplication.getInstance().setRequestQueue();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    mlistner.onVolleyError("Timeout Error");
                } else if (error instanceof AuthFailureError) {
                    mlistner.onVolleyError("AuthFailure Error");

                } else if (error instanceof ServerError) {
                    mlistner.onVolleyError("Server Error");

                } else if (error instanceof NetworkError) {
                    mlistner.onVolleyError("Network Error");

                } else {
                    mlistner.onVolleyError("Try Again");
                }


                if (showDilog) {
                    dismissProgressDialog();
                    showDilog = false;
                }
            }
        })

        {
            @Override
            protected HashMap<String, String> getParams() {
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "username:password";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
////                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", auth);
//                return headers;
//            }

        };

        // Adding request to request queue
        strReq.setShouldCache(false);
        RetryPolicy policy = new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        MyApplication.getInstance().getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(@NonNull Request<Object> request) {
                dismissProgressDialog();
                Log.e("RequesFinish", "-----> Finish" + tag + request.toString());
                MyApplication.getInstance().stopRequest();
                MyApplication.getInstance().setRequestQueue();
            }
        });
        MyApplication.getInstance().addToRequestQueue(strReq, tag);

    }

    public void showdilog(boolean b) {
        showDilog = b;
    }
}

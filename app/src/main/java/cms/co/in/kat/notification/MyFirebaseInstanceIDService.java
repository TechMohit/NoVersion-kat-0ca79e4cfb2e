//package cms.co.in.kat.notification;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.volley.Request;
////import com.google.firebase.iid.FirebaseInstanceId;
////import com.google.firebase.iid.FirebaseInstanceIdService;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//import cms.co.in.kat.activity.Login;
//import cms.co.in.kat.asynchronous.Volley;
//import cms.co.in.kat.interfaces.VolleyListner;
//import cms.co.in.kat.utils.Constant;
//import cms.co.in.kat.utils.GeneralUtilities;
//import cms.co.in.kat.utils.URLConstants;
//
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//    @Override
//    public void onTokenRefresh() {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e("********* ", "*********** " + refreshedToken);
//        if(refreshedToken!=null && !refreshedToken.isEmpty()) {
//            SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE).edit();
//            editor.putString(Constant.MY_PREF_ANDROIDID, refreshedToken);
//        }
//    }
//}

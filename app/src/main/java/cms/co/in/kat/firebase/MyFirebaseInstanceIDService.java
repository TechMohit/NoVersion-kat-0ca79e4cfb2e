//package cms.co.in.kat.firebase;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//import cms.co.in.kat.utils.Constant;
//
///**
// * Created by Ifly on 04-05-2017.
// */
//
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
//    private SharedPreferences sharepref;
//    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        if(refreshedToken!=null) {
//            sharepref = getSharedPreferences(Constant.MY_PREF, MODE_PRIVATE);
//            Log.e("REG TOKEN", "FCM ----> " + refreshedToken);
//
//            SharedPreferences.Editor editor = sharepref.edit();
//            editor.putString(Constant.MY_PREF_REGTOKEN, refreshedToken);
//            editor.commit();
//
//        }
//    }
//}
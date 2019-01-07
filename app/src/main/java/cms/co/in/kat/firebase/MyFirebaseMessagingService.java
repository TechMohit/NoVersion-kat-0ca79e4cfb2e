//package cms.co.in.kat.firebase;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.cms.kat.cws.R;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import org.json.JSONObject;
//
//import cms.co.in.kat.activity.Login;
//import de.greenrobot.event.EventBus;
//
///**
// * Created by Ifly on 04-05-2017.
// */
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
//
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage == null)
//            return;
//
//        if (remoteMessage.getData() != null) {
//            Log.e(TAG, "From: --->" + remoteMessage.getData());
//            startNotification(""+remoteMessage.getData(),"Demo data","1");
//
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body:-----> " + remoteMessage.getNotification().getBody());
//           // startNotification(""+remoteMessage.getData(),"Demo notify","2");
//
//        }
//    }
//
//    private void startNotification(String tittle, String text,String code) {
//        int mNotificationId = (int) (333 * Math.random());
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(tittle)
//                        .setContentText(text)
//                        .setAutoCancel(true)
//                        .setOngoing(false);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, Login.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(contentIntent);
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
//
//    }
//}
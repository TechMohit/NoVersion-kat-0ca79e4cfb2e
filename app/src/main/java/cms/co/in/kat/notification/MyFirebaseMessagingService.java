//package cms.co.in.kat.notification;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.media.RingtoneManager;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.cms.kat.cws.R;
////import com.google.firebase.messaging.FirebaseMessagingService;
////import com.google.firebase.messaging.RemoteMessage;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Random;
//
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e("********", "*****" + remoteMessage);
//
//        Random random = new Random();
//        int uniqueID = random.nextInt(9999 - 1000) + 1000;
//
////        if(remoteMessage!=null && remoteMessage.getData()!=null) {
////            String body = "";
////            JSONObject myJson = null;
////            try {
////                myJson = new JSONObject(remoteMessage.getData());
////                body = myJson.optString("");
////
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//
//
//            NotificationManager notificationManager =
//                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("" + remoteMessage.getData())
//                    .setContentText("" + remoteMessage.getNotification())
//                    .setAutoCancel(true)
//                    .setContentIntent(null)
//                    .build();
//
//            notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            notificationManager.notify(uniqueID, notification);
//        Log.e("****","***notify result "+remoteMessage.getData());
//
//    }
////    }
//}

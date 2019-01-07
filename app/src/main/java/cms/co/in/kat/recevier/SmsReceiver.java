package cms.co.in.kat.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;
import android.util.Log;

import cms.co.in.kat.interfaces.SmsListener;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, @NonNull Intent intent) {

        final Object[] pdusObj = (Object[]) intent.getExtras().get("pdus");
        try {
            for (Object aPdusObj : pdusObj) {

                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                String phoneNumber = currentMessage.getDisplayOriginatingAddress();
//You must check here if the sender is your provider and not another one with same text.
                String message = currentMessage.getDisplayMessageBody().split(",")[1];
                message = message.substring(1, 5);
                Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);
                //Pass on the text to our listener.
                if (phoneNumber.equalsIgnoreCase("DM-REVKAT")) {
                    mListener.messageReceived(message);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
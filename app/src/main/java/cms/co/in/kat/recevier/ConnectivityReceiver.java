package cms.co.in.kat.recevier;

/*
  Created by Lincoln on 18/03/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cms.co.in.kat.utils.InternetCheck;

public class ConnectivityReceiver
        extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {

       new InternetCheck(context).execute();
    }
}

package com.swifty.maptrackerlib;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.swifty.maptrackerlib.gps.LocationManager;
import com.swifty.maptrackerlib.tel.TelManager;

/**
 * Created by swifty on 16/1/2017.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private int mId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm Received at:" + System.currentTimeMillis());
        Toast.makeText(context, context.getString(R.string.time_up), Toast.LENGTH_SHORT).show();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.time_up))
                .setSmallIcon(R.drawable.ic_add_location_red_500_24dp);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, builder.build());
        LocationManager.getInstance().getSpeechSynthesizer().speak(context.getString(R.string.time_up));
        //TelManager.getInstance().stopAutoAccept();
    }
}

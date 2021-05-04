package io.userwise.userwise_android_example;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {
    private String TAG = getClass().getName();
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        String messageId = intent.getStringExtra("messageId");
        String action = intent.getStringExtra("action");


        if (action.equals("action1")) {
            performAction1(context, messageId);
        } else if (action.equals("action2")) {
            performAction2();
        }
        //This is used to close the notification tray
//        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
//        context.sendBroadcast(it)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    public void performAction1(Context context, String messageId) {
        Toast.makeText(context,"recieved " + messageId + " action1", Toast.LENGTH_SHORT).show();
    }

    public void performAction2() {}
}

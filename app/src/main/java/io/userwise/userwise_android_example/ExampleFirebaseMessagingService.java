package io.userwise.userwise_android_example;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseFirebaseMessagingService;

public class ExampleFirebaseMessagingService extends UserWiseFirebaseMessagingService {
    private String TAG = getClass().getName();
    private UserWise userWise = UserWise.INSTANCE;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
            sendNotification(remoteMessage.getMessageId(), remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    // TODO
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageData FCM message body received.
     */
    private void sendNotification(String messageId, Map<String, String> messageData) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //This is the intent of PendingIntent
        Intent intentAction = new Intent(this, ActionReceiver.class);

        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("action", "action1");
        intentAction.putExtra("messageId", messageId);
        PendingIntent pIntentlogin = PendingIntent.getBroadcast(this,0,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        if (messageData.get("channelId") != null) {
            channelId = messageData.get("channelId");
        }

        String imageId = messageData.get("imageId");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Miscellaneous",
                    NotificationManager.IMPORTANCE_HIGH
            );

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            channel.setSound(defaultSoundUri, att);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.userwise_logo)
            .setContentTitle(messageData.get("title"))
            .setStyle(new NotificationCompat.BigTextStyle().bigText(messageData.get("body")))
    //                .setContentText(messageData.get("body"))
    //                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(
    //                        this.getResources(),
    //                        R.drawable.userwise_logo)))
            .setAutoCancel(true)
    //            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.userwise_logo, "Custom Action", pIntentlogin)
    //            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
    //            .setNotificationSilent()
            .setOnlyAlertOnce(false)
            .setColor(ContextCompat.getColor(this, R.color.userWisePrimaryColor));

        if (imageId != null) {
            ExampleMediaInfoHandler handler = new ExampleMediaInfoHandler(notificationManager, notificationBuilder);

            userWise.loadBitmapFromMediaId(imageId, true, handler);
        }
        else {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}

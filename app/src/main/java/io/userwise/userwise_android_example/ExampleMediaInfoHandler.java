package io.userwise.userwise_android_example;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import io.userwise.userwise_sdk.MediaRawDataHandler;
import io.userwise.userwise_sdk.UserWise;

public class ExampleMediaInfoHandler implements MediaRawDataHandler {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    public ExampleMediaInfoHandler(NotificationManager notificationManager, NotificationCompat.Builder notificationBuilder) {
        this.notificationManager = notificationManager;
        this.notificationBuilder = notificationBuilder;
    }


    @Override
    public void onSuccess(@NotNull Bitmap bitmap) {
        notificationBuilder.setLargeIcon(bitmap);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onError() {

    }
}

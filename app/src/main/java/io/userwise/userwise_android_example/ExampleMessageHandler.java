package io.userwise.userwise_android_example;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.messages.Message;
import io.userwise.userwise_sdk.messages.MessageEventListener;
import io.userwise.userwise_sdk.messages.MessagesModule;

public class ExampleMessageHandler implements MessageEventListener {
    private String TAG = getClass().getName();
    private MessagesModule messageModule;

    ExampleMessageHandler(MessagesModule messagesModule) {
        this.messageModule = messagesModule;
    }

    @Override
    public void onMessagesLoaded(boolean fromCache) {
        Log.d(TAG, "Messages have been loaded... From cache? " + fromCache);

        Log.d(TAG, "Total Available Messages: " + this.messageModule.getActiveMessages().toArray().length);
        Log.d(TAG, "Total Upcoming Messages: " + this.messageModule.getUpcomingMessages().toArray().length);
    }

    @Override
    public void onMessageAvailable(@NotNull Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A message is available!\n");
        stringBuilder.append("  ID: ").append(message.getId()).append('\n');
        stringBuilder.append("  Name: ").append(message.getName()).append('\n');
        stringBuilder.append("  Title: ").append(message.getTitle()).append('\n');
        stringBuilder.append("  Body: ").append(message.getBody()).append('\n');
        stringBuilder.append("  Portrait Image ID: ").append(message.getPortraitImageId()).append('\n');
        stringBuilder.append("  Landscape Image ID: ").append(message.getLandscapeImageId()).append('\n');
        stringBuilder.append("  Additional Fields Count: ").append(message.getAdditionalFields().length()).append('\n');

        Log.d(TAG, stringBuilder.toString());
    }
}

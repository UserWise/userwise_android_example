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
    }

    @Override
    public void onMessageAvailable(@NotNull Message message) {
        Log.i(TAG,
                "\nMessage Available:" +
                        "\n|- ID: " + message.getId() +
                        "\n|- Name: " + message.getName() +
                        "\n|- Additional Data:" + message.getAdditionalFields()
        );
    }

    @Override
    public void onMessageUnavailable(@NotNull Message message) {
        Log.i(
                TAG,
                "\nMessage Unavailable:" +
                        "\n|- ID: " + message.getId() +
                        "\n|- Name: " + message.getName() +
                        "\n|- Additional Data:" + message.getAdditionalFields()
        );
    }
}

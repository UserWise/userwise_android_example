package io.userwise.userwise_android_example;

import android.util.Log;
import android.widget.Toast;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.messages.Message;
import io.userwise.userwise_sdk.messages.MessageEventListener;

public class ExampleMessageHandler implements MessageEventListener {

    private String TAG = getClass().getName();
    private MainActivity mainActivity;
    private UserWise userWise;

    public ExampleMessageHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.userWise = UserWise.INSTANCE;
    }

    @Override
    public void onMessagesLoaded(boolean fromCache) {
        Log.d(TAG, "Messages have been loaded... From cache? " + fromCache);

        Log.d(TAG, "Active Messages:");
        Log.d(TAG, userWise.getMessages().getActiveMessages().toString());
        Log.d(TAG, "Upcoming Messages:");
        Log.d(TAG, userWise.getMessages().getUpcomingMessages().toString());
    }

    @Override
    public void onMessageAvailable(Message message) {
        Log.d(TAG, "Message is available! Initializing message with id " + message.getId());

        Toast.makeText(mainActivity, "Title: " + message.getTitle() + " - Body: " + message.getBody() + " - Portrait image: " + message.getPortraitImageId() + " - Landscape image: " + message.getLandscapeImageId(), Toast.LENGTH_SHORT).show();

        userWise.getMessages().setMessageAsViewed(message);
    }
}

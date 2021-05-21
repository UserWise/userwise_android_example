package io.userwise.userwise_android_example;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.events.GameEvent;
import io.userwise.userwise_sdk.events.GameEventsListener;

public class ExampleEventHandler implements GameEventsListener {
    final String TAG = "ExampleEventHandler";

    @Override
    public void onEventsLoaded(boolean fromCache) {
        Log.i(TAG, "Events have been loaded... From cache? " + fromCache);

        List<GameEvent> upcomingEvents = UserWise.INSTANCE.getEvents().getAllUpcoming();
        if (upcomingEvents.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("Upcoming Events:");
            for (int i = 0; i < upcomingEvents.size(); i++) {
                GameEvent event = upcomingEvents.get(i);
                stringBuilder.append("\n|- ").append(event.toString());
            }
            Log.i(TAG, stringBuilder.toString());
        } else {
            Log.i(TAG, "No upcoming events.");
        }
    }

    @Override
    public void onEventActive(@NotNull GameEvent gameEvent) {
        Log.i(
                TAG,
                "\nEvent Active:" +
                        "\n|- ID: " + gameEvent.getId() +
                        "\n|- Name: " + gameEvent.getName() +
                        "\n|- Data: " + gameEvent.getData()
        );

        // All dynamically configured event data can be retrieved by accessing
        // the gameEvent.getData() JSONObject.

        // All Data Methods:
        // gameEvent.getName();
        // gameEvent.getData();
        // gameEvent.getEndAt();
        // gameEvent.getStartAt();
        // gameEvent.getExternalEventType();
        // gameEvent.getExternalId();
        // gameEvent.getId();
    }

    @Override
    public void onEventInactive(@NotNull GameEvent gameEvent) {
        Log.i(
                TAG,
                "\nEvent Inactive:" +
                        "\n|- ID: " + gameEvent.getId() +
                        "\n|- Name: " + gameEvent.getName() +
                        "\n|- Data: " + gameEvent.getData()
        );

        // All dynamically configured event data can be retrieved by accessing
        // the gameEvent.getData() JSONObject.

        // All Data Methods:
        // gameEvent.getName();
        // gameEvent.getData();
        // gameEvent.getEndAt();
        // gameEvent.getStartAt();
        // gameEvent.getExternalEventType();
        // gameEvent.getExternalId();
        // gameEvent.getId();
    }
}

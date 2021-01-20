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
        List<GameEvent> upcomingEvents = UserWise.INSTANCE.getEvents().getAllUpcoming();
        // or UserWise.INSTANCE.getEvents().getAllActive();

        if (upcomingEvents.size() == 0) {
            Log.i(TAG, "No upcoming events.");
            return;
        }

        Log.i(TAG, "Upcoming Events:");
        for (int i = 0; i < upcomingEvents.size(); i++) {
            GameEvent event = upcomingEvents.get(i);
            Log.i(TAG, "  - " + event.getName());
        }
    }

    @Override
    public void onEventActive(@NotNull GameEvent gameEvent) {
        Log.i(TAG, "Event Active Found: " + gameEvent.getName());

        // gameEvent.getName();
        // gameEvent.getData();
        // gameEvent.getEndAt();
        // gameEvent.getStartAt();
        // gameEvent.getExternalEventType();
        // gameEvent.getExternalId();
        // gameEvent.getId();
    }
}

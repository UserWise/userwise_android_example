package io.userwise.userwise_android_example;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import io.userwise.userwise_sdk.remote_configs.RemoteConfig;
import io.userwise.userwise_sdk.remote_configs.RemoteConfigListener;

public class ExampleRemoteConfigHandler implements RemoteConfigListener {
    final String TAG = "ExRemoteConfigHandler";

    @Override
    public void onRemoteConfigsLoaded(boolean fromCache) {
        Log.i(TAG, "Remote configs have been loaded... From cache? " + fromCache);
    }

    @Override
    public void onRemoteConfigActive(@NotNull RemoteConfig remoteConfig) {
        Log.i(TAG,
                "Remote Config Active:" +
                        "\n|- ID: " + remoteConfig.getId() +
                        "\n|- Name: " + remoteConfig.getName() +
                        "\n|- External ID: " + remoteConfig.getExternalId() +
                        "\n|- JSON: " + remoteConfig.getJson()
        );
    }

    @Override
    public void onRemoteConfigInactive(@NotNull RemoteConfig remoteConfig) {
        Log.i(TAG,
                "Remote Config Inactive:" +
                        "\n|- ID: " + remoteConfig.getId() +
                        "\n|- Name: " + remoteConfig.getName() +
                        "\n|- External ID: " + remoteConfig.getExternalId() +
                        "\n|- JSON: " + remoteConfig.getJson()
        );
    }
}

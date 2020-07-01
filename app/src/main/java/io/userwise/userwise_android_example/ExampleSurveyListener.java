package io.userwise.userwise_android_example;

import android.widget.Toast;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseSurveyListener;

public class ExampleSurveyListener implements UserWiseSurveyListener {
    private MainActivity mActivity;
    private UserWise mUserwise;

    ExampleSurveyListener(MainActivity activity, UserWise userWise) {
        mActivity = activity;
        mUserwise = userWise;
    }

    @Override
    public void onSurveyAvailable() {
        if (mUserwise.isTakingSurvey()) { return; }
        mActivity.initializeSurveyInvite();
    }

    @Override
    public void onSurveyEntered() {
        // Called the very moment the loading of a survey has been started
    }

    @Override
    public void onSurveysUnavailable() {
        // Called when no surveys are available for the app user
        Toast.makeText(mActivity, "No surveys are available to take.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyClosed() {
        // Called when a survey view has been closed
        // NOTE: May or may not be accompanied by onSurveyCompleted()
        Toast.makeText(mActivity, "Survey has been closed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEnterFailed() {
        // Called when a survey was unable to properly be loaded
        Toast.makeText(mActivity, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted() {
        // Called when a survey has been successfully completed
        Toast.makeText(mActivity, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }
}

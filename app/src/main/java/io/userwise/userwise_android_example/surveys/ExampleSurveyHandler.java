package io.userwise.userwise_android_example.surveys;

import android.widget.Toast;

import io.userwise.userwise_android_example.MainActivity;
import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.surveys.SurveyEventListener;

public class ExampleSurveyHandler implements SurveyEventListener {

    private MainActivity mainActivity;
    private UserWise userWise;

    public ExampleSurveyHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.userWise = UserWise.INSTANCE;
    }

    @Override
    public void onSurveyAvailable(String surveyResponseId) {
        if (userWise.getSurveysModule().isTakingSurvey()) { return; }
        userWise.getSurveysModule().initializeSurveyInvite(surveyResponseId);
    }

    @Override
    public void onSurveysUnavailable() {
        // Called when no surveys are available for the app user
        Toast.makeText(mainActivity, "No surveys are available to take.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyInviteInitialized(Boolean wasInitialized, String surveyResponseId, String surveyInviteId) {
        // wasInitialized=false
        //     UserWise failed to start the survey initialization process. Don't show the survey
        //     invite dialog to the user.
        if (!wasInitialized) { return; }
        mainActivity.showSurveyOffer(surveyResponseId, surveyInviteId);
    }

    @Override
    public void onSurveyEnterFailed(String surveyResponseId) {
        // Called when a survey was unable to be loaded properly
        Toast.makeText(mainActivity, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEntered(String surveyResponseId) {
        // Called once the survey has finished rendering for the user
    }

    @Override
    public void onSurveyClosed(String surveyResponseId) {
        // Called when a survey view has been closed
        // NOTE: May or may not be accompanied by onSurveyCompleted()
        Toast.makeText(mainActivity, "Survey has been closed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted(String surveyResponseId) {
        // Called when a survey has been successfully completed
        Toast.makeText(mainActivity, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }
}

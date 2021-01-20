package io.userwise.userwise_android_example;

import android.util.Log;
import android.widget.Toast;

import io.userwise.userwise_android_example.MainActivity;
import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.surveys.SurveyEventListener;
import io.userwise.userwise_sdk.surveys.Survey;

public class ExampleSurveyHandler implements SurveyEventListener {

    private String TAG = getClass().getName();
    private MainActivity mainActivity;
    private UserWise userWise;

    public ExampleSurveyHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.userWise = UserWise.INSTANCE;
    }

    @Override
    public void onSurveysLoaded(boolean fromCache) {
        Log.d(TAG, "Offers have been loaded... From cache? " + fromCache);
    }

    @Override
    public void onSurveyAvailable(Survey survey) {
        Log.d(TAG, "Offer is available! Initializing survey with id " + survey.getId());

        if (userWise.getSurveys().isTakingSurvey()) { return; }
        userWise.getSurveys().initializeSurveyInvite(survey);
    }

    @Override
    public void onSurveyInviteInitialized(Boolean wasInitialized, Survey survey, String surveyResponseId, String surveyInviteId) {
        // wasInitialized=false
        //     UserWise failed to start the survey initialization process. Don't show the survey
        //     invite dialog to the user.
        if (!wasInitialized) { return; }
        mainActivity.showSurveyOffer(survey, surveyResponseId, surveyInviteId);
    }

    @Override
    public void onSurveyEnterFailed(Survey survey, String surveyResponseId) {
        // Called when a survey was unable to be loaded properly
        Toast.makeText(mainActivity, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEntered(Survey survey, String surveyResponseId) {
        // Called once the survey has finished rendering for the user
    }

    @Override
    public void onSurveyClosed(Survey survey, String surveyResponseId) {
        // Called when a survey view has been closed
        // NOTE: May or may not be accompanied by onSurveyCompleted()
        Toast.makeText(mainActivity, "Survey has been closed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted(Survey survey, String surveyResponseId) {
        // Called when a survey has been successfully completed
        Toast.makeText(mainActivity, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }
}

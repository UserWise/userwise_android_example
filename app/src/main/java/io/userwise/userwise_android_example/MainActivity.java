package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.logging.Logger;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseSurveyListener;

public class MainActivity extends AppCompatActivity implements UserWiseSurveyListener {
    private static Logger logger = Logger.getLogger("userwise_example_app");

    private UserWise userWise = UserWise.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userWise.setDebugMode(true);
        userWise.setSurveyListener(this);
        logger.info("UserWise Survey Listener Set");

        Context context = this;
        String apiKey = "6b6552ebc324a570262deb6bdd4e";
        String userId = "userwise-android-example";

        userWise.setContext(context);
        userWise.setUserId(userId);
        userWise.setApiKey(apiKey);
        // or: userWise.initialize(context, apiKey, userId);
        logger.info("API Key and User ID Set");
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.refreshHasAvailableSurveys();
    }

    public void takeNextSurvey(View view) {
        if (userWise.hasSurveysAvailable()) {
            userWise.takeNextSurvey();
            return;
        }

        Toast.makeText(this, "No surveys available to take!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEntered() {
        // Called the very moment the loading of a survey has been started
        Toast.makeText(this, "Entering survey!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyAvailable() {
        // Called when surveys are available for the appuser
        Toast.makeText(this, "Surveys are available!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveysUnavailable() {
        // Called when no surveys are available for the appuser
        Toast.makeText(this, "No surveys are available to take.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyClosed() {
        // Called when a survey view has been closed
        // NOTE: May or may not be accompanied by onSurveyCompleted()
        Toast.makeText(this, "Survey has been closed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted() {
        // Called when a survey has been successfully completed
        Toast.makeText(this, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEnterFailed() {
        // Called when a survey was unable to properly be loaded
        Toast.makeText(this, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }
}

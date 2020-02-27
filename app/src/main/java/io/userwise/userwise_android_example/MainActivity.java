package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
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

        userWise.setApiKey("6b6552ebc324a570262deb6bdd4e");
        userWise.setUserId("david-test-android1");
        logger.info("API Key and User ID Set");
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.refreshHasAvailableSurveys();
    }

    public void takeNextSurvey(View view) {
        if (userWise.hasSurveysAvailable()) {
            userWise.takeNextSurvey(this);
            return;
        }

        Toast.makeText(this, "No surveys available to take!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyAvailable() {
        Toast.makeText(this, "Surveys are available!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyClosed() {
        Toast.makeText(this, "Survey has been exited early!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted() {
        Toast.makeText(this, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEntered() {
        Toast.makeText(this, "Entering survey!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEnterFailed() {
        Toast.makeText(this, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }
}

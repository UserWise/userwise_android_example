package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
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

        userWise.setContext(this);
        userWise.setUserId("userwise-android-example4");
        userWise.setApiKey("6b6552ebc324a570262deb6bdd4e");
        // or: userWise.initialize(context, apiKey, userId);

        // You can also update the styles of the loading screen. Uncomment to see the example app's
        // overrides.
        int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);
        userWise.setColors(primaryColor, backgroundColor);

        Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        userWise.setSplashScreenLogo(logo);

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

        Toast.makeText(this, "Cannot take survey. No surveys to take!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEntered() {
        // Called the very moment the loading of a survey has been started
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

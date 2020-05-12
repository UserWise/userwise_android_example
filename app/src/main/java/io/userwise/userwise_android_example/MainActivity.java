package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseSurveyInviteHandler;
import io.userwise.userwise_sdk.UserWiseSurveyListener;

public class MainActivity extends AppCompatActivity implements UserWiseSurveyListener, UserWiseSurveyInviteHandler {
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
        userWise.setUserId("userwise-android-example");
        userWise.setApiKey("fe6dc94555e366a36be767d6d937");
        // or: userWise.initialize(context, apiKey, userId);

        // You can also update the styles of the loading screen. Uncomment to see the example app's
        // overrides.
        //int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        //int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);
        //userWise.setColors(primaryColor, backgroundColor);

        //Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        //userWise.setSplashScreenLogo(logo);

        logger.info("API Key and User ID Set");
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.refreshHasAvailableSurveys();
    }

    public void takeNextSurvey(View view) {
        //try {
            //JSONObject attributes = new JSONObject().put("favorite_color", "blue");

            //userWise.assignEvent("event_logged_in", attributes);
            //userWise.setAttributes(attributes);
        //} catch (JSONException e) {}


        userWise.initializeSurveyInvite(this);
    }

    @Override
    public void onSurveyInviteInitialized(Boolean wasInitialized) {
        // UserWise failed to start the survey initialization process. Don't show the survey invite
        // dialog to the user.
        if (!wasInitialized) { return; }

        userWise.setSurveyInviteResponse(true);
    }

    @Override
    public void onSurveyEntered() {
        // Called the very moment the loading of a survey has been started
    }

    @Override
    public void onSurveyAvailable() {
        if (userWise.isTakingSurvey()) { return; }

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

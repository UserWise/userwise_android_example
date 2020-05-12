package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
    private Dialog surveyOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userWise.setDebugMode(true);
        userWise.setSurveyListener(this);
        logger.info("UserWise Survey Listener Set");

        userWise.setContext(this);
        userWise.setUserId("userwise_video_demo12");
        userWise.setApiKey("2fac619fdeecba9f3fb3c7228406");
        // or: userWise.initialize(context, apiKey, userId);

        // You can also update the styles of the loading screen. Uncomment to see the example app's
        // overrides.
        //int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        //int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);
        //userWise.setColors(primaryColor, backgroundColor);

        //Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        //userWise.setSplashScreenLogo(logo);


        //try {
        //JSONObject attributes = new JSONObject().put("favorite_color", "blue");

        //userWise.assignEvent("event_logged_in", attributes);
        //userWise.setAttributes(attributes);
        //} catch (JSONException e) {}


        logger.info("API Key and User ID Set");
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.refreshHasAvailableSurveys();
    }

    public void initializeSurveyInvite() {
        userWise.initializeSurveyInvite(this);
    }

    public void declineSurveyInvite(View view) {
        this.dismissSurveyOffer();
        userWise.setSurveyInviteResponse(false);
    }

    public void acceptSurveyInvite(View view) {
        this.dismissSurveyOffer();
        userWise.setSurveyInviteResponse(true);
    }

    private void dismissSurveyOffer() {
        if (this.surveyOffer != null) {
            this.surveyOffer.dismiss();
        }
    }

    private void showSurveyOffer() {
        if (this.surveyOffer == null) {
            this.surveyOffer = new Dialog(this);
            this.surveyOffer.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.surveyOffer.getWindow().setLayout(414, 736);
            this.surveyOffer.setContentView(getLayoutInflater().inflate(R.layout.survey_invite_layout,null));
        }

        this.surveyOffer.show();
    }

    @Override
    public void onSurveyInviteInitialized(Boolean wasInitialized) {
        // wasInitialized=false
        //     UserWise failed to start the survey initialization process. Don't show the survey
        //     invite dialog to the user.
        if (!wasInitialized) { return; }

        this.showSurveyOffer();
    }

    @Override
    public void onSurveyEntered() {
        // Called the very moment the loading of a survey has been started
    }

    @Override
    public void onSurveyAvailable() {
        if (userWise.isTakingSurvey()) { return; }
        this.initializeSurveyInvite();
    }

    @Override
    public void onSurveysUnavailable() {
        // Called when no surveys are available for the app user
    }

    @Override
    public void onSurveyClosed() {
        // Called when a survey view has been closed
        // NOTE: May or may not be accompanied by onSurveyCompleted()
        Toast.makeText(this, "Survey has been closed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyEnterFailed() {
        // Called when a survey was unable to properly be loaded
        Toast.makeText(this, "Survey failed to load!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurveyCompleted() {
        // Called when a survey has been successfully completed
        Toast.makeText(this, "Survey was successfully completed!", Toast.LENGTH_SHORT).show();
    }
}

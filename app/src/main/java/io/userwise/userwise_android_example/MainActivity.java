package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseSurveyInviteHandler;

public class MainActivity extends AppCompatActivity implements  UserWiseSurveyInviteHandler {
    private static Logger logger = Logger.getLogger("userwise_example_app");

    private UserWise userWise = UserWise.INSTANCE;
    private Dialog surveyOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1) Set our debug mode, our survey listener, and provide an app context
        userWise.setDebugMode(true);
        userWise.setSurveyListener(new ExampleSurveyListener(this, this.userWise));
        userWise.setContext(this);

        // Step 2) We set our app's api key and initialize the user by their _UNIQUE_ id.
        userWise.setApiKey("f0d040021dcb9f26765e25da6b57");

        // **psst** if you have no surveys available, change the user id below :-)
        userWise.setUserId("userwise-android-example-user");
        // or: userWise.initialize(context, apiKey, userId);

        // Step 3) We call the onStart lifecycle method
        userWise.onStart();

        // Step 4) We can override some of the loading screen designs (e.g. colors and logo)
        //Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        //userWise.setSplashScreenLogo(logo);
        //int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        //int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);
        //userWise.setColors(primaryColor, backgroundColor);

        // Step 5) You can assign your app user attributes and events directly within the SDK!
        //try {
        //JSONObject eventAttributes = new JSONObject().put("is_new_player", false);
        //userWise.assignEvent("event_logged_in", eventAttributes);

        //JSONObject attributes = new JSONObject().put("current_coins", 1000).put("current_diamonds": 20);
        //userWise.setAttributes(attributes);
        //} catch (JSONException e) {}
    }

    @Override
    protected void onStop() {
        super.onStop();
        userWise.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userWise.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userWise.onStop();
    }

    public void initializeSurveyInvite() {
        userWise.initializeSurveyInvite(this);
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.refreshHasAvailableSurveys();
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
            this.surveyOffer.setContentView(getLayoutInflater().inflate(R.layout.survey_invite_layout,null));
        }

        this.surveyOffer.show();
        this.surveyOffer.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onSurveyInviteInitialized(Boolean wasInitialized) {
        // wasInitialized=false
        //     UserWise failed to start the survey initialization process. Don't show the survey
        //     invite dialog to the user.
        if (!wasInitialized) { return; }

        this.showSurveyOffer();
    }
}

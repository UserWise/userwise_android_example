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

import io.userwise.userwise_android_example.offers.ExampleOfferHandler;
import io.userwise.userwise_android_example.surveys.ExampleSurveyHandler;
import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.offers.OffersModule;
import io.userwise.userwise_sdk.surveys.SurveysModule;

public class MainActivity extends AppCompatActivity {
    private static Logger logger = Logger.getLogger("userwise_example_app");
    private UserWise userWise = UserWise.INSTANCE;
    private Dialog surveyOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1) Set session-based UserWise configuration
        userWise.setContext(this);
        userWise.setDebugMode(true);
        userWise.setUserId("example-android-user");
        userWise.setApiKey("0af5b8279d1ae000b2f4836fa7e0");
        userWise.setLocalhostOverride("192.168.0.134:3000"); // TODO: REMOVE ME :-)

        // Step 2) Configure any modules you'd like to use (e.g. surveys & offers)
        Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);

        SurveysModule surveysModule = userWise.getSurveysModule();
        surveysModule.setSurveyListener(new ExampleSurveyHandler(this));
        surveysModule.setSplashScreenLogo(logo);
        surveysModule.setColors(primaryColor, backgroundColor);

        OffersModule offersModule = userWise.getOffersModule();
        offersModule.setOfferListener(new ExampleOfferHandler());

        // Step 4) Finally, you can assign your app user attributes and events directly within the SDK!
        try {
            JSONObject eventAttributes = new JSONObject().put("is_new_player", false);
            userWise.assignEvent("event_logged_in", eventAttributes);

            JSONObject attributes = new JSONObject().put("current_coins", 1000).put("current_diamonds", 20);
            userWise.setAttributes(attributes);
        } catch (JSONException e) {}
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

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.forcePollRequest();
    }

    public void declineSurveyInvite(View view) {
        this.dismissSurveyOffer();
        userWise.getSurveysModule().setSurveyInviteResponse(false);
    }

    public void acceptSurveyInvite(View view) {
        this.dismissSurveyOffer();
        userWise.getSurveysModule().setSurveyInviteResponse(true);
    }

    public void dismissSurveyOffer() {
        if (this.surveyOffer != null) {
            this.surveyOffer.dismiss();
        }
    }

    public void showSurveyOffer() {
        if (this.surveyOffer == null) {
            this.surveyOffer = new Dialog(this);
            this.surveyOffer.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.surveyOffer.setContentView(getLayoutInflater().inflate(R.layout.survey_invite_layout,null));
        }

        this.surveyOffer.show();
        this.surveyOffer.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }
}

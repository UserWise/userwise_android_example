package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import io.userwise.userwise_sdk.MediaInfo;
import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.MediaInfoHandler;
import io.userwise.userwise_sdk.offers.OffersModule;
import io.userwise.userwise_sdk.surveys.SurveysModule;
import io.userwise.userwise_sdk.variables.Variable;
import io.userwise.userwise_sdk.variables.VariableType;
import io.userwise.userwise_sdk.variables.VariablesModule;
import io.userwise.userwise_sdk.variables.VariablesEventListener;

public class MainActivity extends AppCompatActivity implements VariablesEventListener {
    private static Logger logger = Logger.getLogger("userwise_example_app");
    private UserWise userWise = UserWise.INSTANCE;
    private Dialog surveyOffer;

    private String surveyResponseId;
    private String surveyInviteId;

    private Variable maxLevel = new Variable("maxLevel", VariableType.INTEGER, 100);
    private Variable enableThingA = new Variable("enableThingA", VariableType.BOOLEAN, false);
    private Variable startThisThingAt = new Variable("startThisThingAt", VariableType.DATETIME, null);
    private Variable title = new Variable("title", VariableType.STRING, "Default Title");
    private Variable exchangeRate = new Variable("exchangeRate", VariableType.FLOAT, 0.0);
    private Variable headerImage = new Variable("headerImage", VariableType.FILE, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1) UserWise SDK Configuration
        userWise.setContext(this);
        userWise.setDebugMode(true);
        userWise.setHostOverride("staging.userwise.io"); // (uncomment me for staging)
        userWise.setUserId("userwise-demo-app-user-android");
        userWise.setApiKey("f1535363ad9ab340ebc9786337b0"); // (staging)

        // Step 2) Define any app variables -- *prior* to initializing the UserWise SDK
        userWise.getVariables().defineVariables(new Variable[]{
            maxLevel, enableThingA, startThisThingAt, title, exchangeRate, headerImage
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        userWise.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Step 3) Configure any modules you'd like to use (e.g. surveys & offers)
        Drawable logo = ContextCompat.getDrawable(this, R.drawable.userwise_herowars_logo);
        int primaryColor = ContextCompat.getColor(this, R.color.userWisePrimaryColorOverride);
        int backgroundColor = ContextCompat.getColor(this, R.color.userWiseSplashScreenBackgroundColorOverride);

        SurveysModule surveysModule = userWise.getSurveys();
        surveysModule.setSurveyListener(new ExampleSurveyHandler(this));
        surveysModule.setSplashScreenLogo(logo);
        surveysModule.setColors(primaryColor, backgroundColor);

        OffersModule offersModule = userWise.getOffers();
        offersModule.setOfferListener(new ExampleOfferHandler());

        VariablesModule variablesModule = userWise.getVariables();
        variablesModule.setVariableListener(this);

        // Step 3) Start The UserWise SDK
        userWise.onStart();

        // Step 4) Finally, you can also assign your app user attributes and events directly within the SDK!
        //try {
        //    JSONObject eventAttributes = new JSONObject().put("is_new_player", false);
        //    userWise.assignEvent("event_logged_in", eventAttributes);

        //    JSONObject attributes = new JSONObject().put("current_coins", 1000).put("current_diamonds", 20);
        //    userWise.setAttributes(attributes);
        //} catch (JSONException e) {}
    }

    @Override
    protected void onPause() {
        super.onPause();
        userWise.onStop();
    }

    @Override
    public void onVariablesInitialized() {
        Log.d("UserWiseExample", "Variable loaded!");

        Log.d("UserWiseExample", "Variables Defined:");
        Log.d("UserWiseExample", "maxLevel: " + this.maxLevel.getInteger());
        Log.d("UserWiseExample", "enableThingA: " + this.enableThingA.getBoolean());
        Log.d("UserWiseExample", "startThisThingAt: " + this.startThisThingAt.getDateTime());
        Log.d("UserWiseExample", "title: " + this.title.getString());
        Log.d("UserWiseExample", "exchangeRate: " + this.exchangeRate.getFloat());

        if (this.headerImage.getFileId() != null) {
            this.userWise.getMedia(this.headerImage.getFileId(), new MediaInfoHandler() {
                public void onSuccess(MediaInfo mediaInfo) {
                    //mediaInfo.getName();
                    //mediaInfo.getContentType();
                    Log.d("UserWiseExample", "Header Image URL: " + mediaInfo.getUrl());
                }

                public void onFailure() {

                }
            });
        }
    }

    public void forceRefreshHasSurveysAvailable(View view) {
        userWise.forcePollRequest();
    }

    public void declineSurveyInvite(View view) {
        String surveyResponseId = this.surveyResponseId;
        String surveyInviteId = this.surveyInviteId;
        Log.d("UserWiseExample", surveyResponseId);
        userWise.getSurveys().setSurveyInviteResponse(surveyResponseId, surveyInviteId, false);
        this.dismissSurveyInvite();
    }

    public void acceptSurveyInvite(View view) {
        String surveyResponseId = this.surveyResponseId;
        String surveyInviteId = this.surveyInviteId;
        userWise.getSurveys().setSurveyInviteResponse(surveyResponseId, surveyInviteId, true);
        this.dismissSurveyInvite();
    }

    public void dismissSurveyInvite() {
        if (this.surveyOffer != null) {
            this.surveyResponseId = null;
            this.surveyInviteId = null;
            this.surveyOffer.dismiss();
            this.surveyOffer = null;
        }
    }

    public void showSurveyOffer(String surveyResponseId, String surveyInviteId) {
        if (this.surveyOffer == null) {
            this.surveyOffer = new Dialog(this);
            this.surveyOffer.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.surveyOffer.setContentView(getLayoutInflater().inflate(R.layout.survey_invite_layout,null));

            this.surveyResponseId = surveyResponseId;
            this.surveyInviteId = surveyInviteId;
        }

        this.surveyOffer.show();
        this.surveyOffer.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }
}

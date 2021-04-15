package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.logging.Logger;

import io.userwise.userwise_sdk.MediaInfo;
import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.MediaRawDataHandler;
import io.userwise.userwise_sdk.events.EventsModule;
import io.userwise.userwise_sdk.messages.MessagesModule;
import io.userwise.userwise_sdk.offers.OffersModule;
import io.userwise.userwise_sdk.surveys.SurveysModule;
import io.userwise.userwise_sdk.surveys.Survey;
import io.userwise.userwise_sdk.variables.BooleanVariable;
import io.userwise.userwise_sdk.variables.DateTimeVariable;
import io.userwise.userwise_sdk.variables.FileVariable;
import io.userwise.userwise_sdk.variables.FloatVariable;
import io.userwise.userwise_sdk.variables.IntegerVariable;
import io.userwise.userwise_sdk.variables.StringVariable;
import io.userwise.userwise_sdk.variables.Variable;
import io.userwise.userwise_sdk.variables.VariablesModule;
import io.userwise.userwise_sdk.variables.VariablesEventListener;

public class MainActivity extends AppCompatActivity implements VariablesEventListener {
    private static String TAG = "UserWiseExample";
    private static Logger logger = Logger.getLogger("userwise_example_app");
    private UserWise userWise = UserWise.INSTANCE;
    private Dialog surveyOffer;

    private Survey survey;
    private String surveyResponseId;
    private String surveyInviteId;

    private final IntegerVariable maxLevel = new IntegerVariable("maxLevel", 100);
    private final BooleanVariable enableThingA = new BooleanVariable("enableThingA", false);
    private final DateTimeVariable startThisThingAt = new DateTimeVariable("startThisThingAt", Calendar.getInstance().getTime());
    private final StringVariable title = new StringVariable("title", "Default Title");
    private final FloatVariable exchangeRate = new FloatVariable("exchangeRate", 0.0f);
    private final FileVariable headerImage = new FileVariable("headerImage", null);

    private TextView userIdTxtEditor;
    private Button userIdChangeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userIdTxtEditor = findViewById(R.id.userIdField);
        userIdTxtEditor.setText("userwise-demo-app-user-android-dac");

        userIdChangeBtn = findViewById(R.id.changeUserBtn);

        final MainActivity self = this;
        userIdChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.stopSDK();
                self.startSDK();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1) Configure the SDK...
        if (!userWise.isSessionInitialized()) {
            this.configureUserWiseSDK();
        }

        // 2) Start the SDK
        this.startSDK();

        // Finally, you can also assign your app user attributes and events directly within the SDK!
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

    void stopSDK() {
        userWise.onStop();
    }

    void startSDK() {
        userWise.setUserId(userIdTxtEditor.getText().toString());
        userWise.onStart();
    }

    private void configureUserWiseSDK() {
        // UserWise SDK 'Global' Configuration
        userWise.setContext(this);
        userWise.setDebugMode(true);
        userWise.setHttpSchemeOverride("https");
        userWise.setHostOverride("userwise.io"); // (staging)
        userWise.setApiKey(""); // (staging)

        // UserWise SDK 'Module' Configuration
        //
        // Most module configuration can be handled dynamically, however, as of writing this
        // comment; you *must* define all variables prior to initializing the SDK.
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
        variablesModule.defineVariables(new Variable[]{ maxLevel, enableThingA, startThisThingAt, title, exchangeRate, headerImage });
        variablesModule.setVariableListener(this);

        MessagesModule messagesModule = userWise.getMessages();
        messagesModule.setMessageListener(new ExampleMessageHandler(messagesModule));

        EventsModule eventsModule = userWise.getEvents();
        eventsModule.setEventsListener(new ExampleEventHandler());
    }

    @Override
    public void onVariablesLoaded(boolean fromCache) {
        Log.d(TAG, "Variable loaded!");

        Log.d(TAG, "Variables Defined:");
        Log.d(TAG, "maxLevel: " + this.maxLevel.getValue());
        Log.d(TAG, "enableThingA: " + this.enableThingA.getValue());
        Log.d(TAG, "startThisThingAt: " + this.startThisThingAt.getISO8601());
        Log.d(TAG, "title: " + this.title.getValue());
        Log.d(TAG, "exchangeRate: " + this.exchangeRate.getValue());
    }

    @Override
    public void onVariableValueChanged(Variable variable) { }

    public void declineSurveyInvite(View view) {
        userWise.getSurveys().setSurveyInviteResponse(this.survey, this.surveyResponseId, this.surveyInviteId, false);
        this.dismissSurveyInvite();
    }

    public void acceptSurveyInvite(View view) {
        userWise.getSurveys().setSurveyInviteResponse(this.survey, this.surveyResponseId, this.surveyInviteId, true);
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

    public void showSurveyOffer(Survey survey, String surveyResponseId, String surveyInviteId) {
        if (this.surveyOffer == null) {
            this.surveyOffer = new Dialog(this);
            this.surveyOffer.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.surveyOffer.setContentView(getLayoutInflater().inflate(R.layout.survey_invite_layout,null));

            this.survey = survey;
            this.surveyResponseId = surveyResponseId;
            this.surveyInviteId = surveyInviteId;
        }

        this.surveyOffer.show();
        this.surveyOffer.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }
}

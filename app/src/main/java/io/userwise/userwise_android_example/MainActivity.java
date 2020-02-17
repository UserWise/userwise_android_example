package io.userwise.userwise_android_example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.UserWiseSurveyListener;

public class MainActivity extends AppCompatActivity {
    private static Logger logger = Logger.getLogger("userwise_example_app");

    private UserWise userWiseSingleton = UserWise.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userWiseSingleton.setDebugMode(true);
        userWiseSingleton.setParentContext(new WeakReference<Activity>(this));
        userWiseSingleton.setSurveyListener(new UserWiseSurveyHandler());
        logger.info("UserWise Survey Listener Set");

        userWiseSingleton.setApiKey("d28fea54eb83795868d198d52734");
        userWiseSingleton.setUserId("user123-android");
        logger.info("API Key and User ID Set");
    }

    public void loadSurveys(View view) {
        if (!userWiseSingleton.hasSurveysAvailable()) {
            userWiseSingleton.refreshHasAvailableSurveys();
        }
    }

    class UserWiseSurveyHandler implements UserWiseSurveyListener {

        @Override
        public void onSurveyAvailable() {
            MainActivity.logger.info("Surveys are available!");
            userWiseSingleton.takeNextSurvey();
        }

        @Override
        public void onSurveyClosed() {
            MainActivity.logger.info("Survey has been closed!");
        }

        @Override
        public void onSurveyCompleted() {
            MainActivity.logger.info("Survey was completed successfully!");
        }

        @Override
        public void onSurveyEntered() {
            MainActivity.logger.info("Survey is being entered into!");
        }

        @Override
        public void onSurveyEnterFailed() {
            MainActivity.logger.info("Survey failed to load!");
        }
    }
}

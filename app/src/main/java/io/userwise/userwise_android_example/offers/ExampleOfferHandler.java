package io.userwise.userwise_android_example.offers;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Iterator;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.offers.Bundle;
import io.userwise.userwise_sdk.offers.OfferEventListener;
import io.userwise.userwise_sdk.offers.OfferImpression;
import io.userwise.userwise_sdk.offers.OfferImpressionState;
import io.userwise.userwise_sdk.offers.OfferViewAttemptFailedReason;

public class ExampleOfferHandler implements OfferEventListener {

    private String TAG = getClass().getName();

    @Override
    public void onOfferAvailable(@NotNull String offerId) {
        Log.d(TAG, "Offer is available! Initializing offer with id " + offerId);
        UserWise.INSTANCE.getOffersModule().initializeOfferImpression(offerId);
    }

    @Override
    public void onOfferUnavailable() {
        Log.d(TAG, "No offers are available.");
    }

    @Override
    public void onOfferImpressionInitializationFailed(@NotNull String offerId) {
        Log.d(TAG, "Offer impression initialization failed for offer id " + offerId);
    }

    @Override
    public void onOfferImpressionInitialized(@NotNull OfferImpression offerImpression) {
        Log.d(TAG, "Offer impression initialized! Offer impression id " + offerImpression.getId());
        UserWise.INSTANCE.getOffersModule().showOffer(offerImpression);

        // Above, we tell the UserWise SDK to show the offer, as it was built in our dashboard.
        // However, if you find yourself wanting a data-only approach, you can hook into-and out of-
        // the UserWise OffersModule event flow, at any point.

        // Offer impressions contain information about their `offer`, including bundle information
        // (e.g. pricing, contents, etc.).

        // Offer impressions can have their state updated by calling  offerImpression.updateState();
    }

    @Override
    public void onOfferViewAttemptFailed(@NotNull OfferImpression offerImpression, @NotNull OfferViewAttemptFailedReason reason) {
        Log.d(TAG, "Offer failed to load properly. Reason: " + reason.toString());
    }

    @Override
    public void onOfferViewed(@NotNull OfferImpression offerImpression, @NotNull Bundle bundle) {
        Log.d(TAG, "Offer has loaded and is actively visible!");
    }

    @Override
    public void onOfferDismissed(@NotNull OfferImpression offerImpression, @NotNull Bundle bundleData) {
        Log.d(TAG, "Offer was dismissed!");
    }

    @Override
    public void onOfferAccepted(@NotNull OfferImpression offerImpression, @NotNull Bundle bundleData) {
        Log.d(TAG, "Offer was accepted!");

        JSONObject bundleContent = bundleData.getContents();
        Iterator<String> it = bundleContent.keys();

        StringBuilder str = new StringBuilder();
        while (it.hasNext()) {
            String currencyId = it.next();
            int currencyAmount = bundleContent.optInt(currencyId, 0);

            str.append(currencyAmount).append(" ").append(currencyId).append("\n");
        }

        Log.d(TAG, "Bundle Contains: \n" + str.toString());
        Log.d(TAG, "Raw Bundle Contents: \n" + bundleContent.toString());

        // Once an offer has been accepted the itself impression will stay in a state of "accepted"
        // on the UserWise servers.  There are currently two possible states beyond "accepted":
        //   - PURCHASED
        //   - PURCHASE_FAILED
        //
        // You can update to these states through the OfferImpression#updateState() method.
        //
        // Examples:

        // 1. You display buy screen. User purchases. You call:
        //offerImpression.updateState(OfferImpressionState.PURCHASED);

        // 2. You display the buy screen (or maybe that itself fails). User does not purchase. You call:
        //offerImpression.updateState(OfferImpressionState.PURCHASE_FAILED);
    }

}

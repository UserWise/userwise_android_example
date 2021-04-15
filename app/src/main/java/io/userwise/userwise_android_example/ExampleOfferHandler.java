package io.userwise.userwise_android_example;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Iterator;

import io.userwise.userwise_sdk.UserWise;
import io.userwise.userwise_sdk.offers.Offer;
import io.userwise.userwise_sdk.offers.OfferEventListener;
import io.userwise.userwise_sdk.offers.OfferImpression;
import io.userwise.userwise_sdk.offers.OfferImpressionState;
import io.userwise.userwise_sdk.offers.OfferViewAttemptFailedReason;

public class ExampleOfferHandler implements OfferEventListener {
    private String TAG = getClass().getName();

    @Override
    public void onOffersLoaded(@NotNull boolean fromCache) {
        Log.d(TAG, "Offers have been loaded... From cache? " + fromCache);
    }

    @Override
    public void onOfferAvailable(@NotNull Offer offer) {
        Log.d(TAG, "Offer is available! Initializing offer with id " + offer.getId());
        UserWise.INSTANCE.getOffers().initializeOfferImpression(offer);
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


        Offer offer = offerImpression.getOffer();

        String productId = offer.getAndroidProductId();
        Double cost = offer.getCost();
        JSONObject bundledCurrencies = offer.getCurrencies();
        JSONObject bundledItems = offer.getItems();

        Log.d(TAG, "Offer Title: " + offer.getTitle());
        Log.d(TAG, "Offer Body: " + offer.getBody());
        Log.d(TAG, "Offer Portrait Image ID: " + offer.getPortraitImageId());
        Log.d(TAG, "Offer Landscape Image ID: " + offer.getLandscapeImageId());
        Log.d(TAG, "Offer Product ID: " + productId);
        Log.d(TAG, "Offer Cost: " + cost.toString());
        Log.d(TAG, "Offer Bundled Currencies: \n" + bundledCurrencies.toString());
        Log.d(TAG, "Offer Bundled Items: \n" + bundledItems.toString());

        // Once an offer has been accepted the itself impression will stay in a state of "accepted"
        // on the UserWise servers.  There are currently two possible states beyond "accepted":
        //   - PURCHASED
        //   - PURCHASE_FAILED
        //
        // You can update to these states through the OfferImpression#updateState() method.
        //
        // Examples:

        // 1. You display buy screen. User purchases. You call:
        offerImpression.updateState(OfferImpressionState.PURCHASED);

        // 2. You display the buy screen (or maybe that itself fails). User does not purchase. You call:
        //offerImpression.updateState(OfferImpressionState.PURCHASE_FAILED);
    }

    // these are all ignored
    @Override
    public void onOfferViewAttemptFailed(@NotNull OfferImpression offerImpression, @NotNull OfferViewAttemptFailedReason reason) {}
    @Override
    public void onOfferViewed(@NotNull OfferImpression offerImpression) {}
    @Override
    public void onOfferDismissed(@NotNull OfferImpression offerImpression) {}
    @Override
    public void onOfferAccepted(@NotNull OfferImpression offerImpression) {}

}

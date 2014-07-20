package es.dsie.cordova.plugins.openx;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LinearLayoutSoftKeyboardDetect;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openx.ad.mobile.sdk.interfaces.AdEventsListener;
import com.openx.errors.AdError;
import com.openx.model.MRAIDAction;
import com.openx.view.AdBanner;
import com.openx.view.AdInterstitial;

import android.util.Log;


public class OpenXPlugin extends CordovaPlugin {
	private static final String LOGTAG = "OpenXPlugin";
	
    private static final String ACTION_INIT = "init";
    private static final String ACTION_SHOW_BANNER = "showBanner";
    private static final String ACTION_SHOW_INTERSTITIAL = "showInterstitial";
    private static final String ACTION_HIDE_BANNER = "hideBanner";
    
    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;
    
    private static final String JSON_KEY_DOMAIN = "domain";
    private static final String JSON_KEY_PORTRAIT_ID = "portraitId";
    private static final String JSON_KEY_LANDSCAPE_ID = "landscapeId";
    private static final String JSON_KEY_CHANGE_INTERVAL = "changeInterval";
    
    private String poolId = "";

    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        if (ACTION_INIT.equals(action)) {
            //executeCreateBannerView(inputs, callbackContext);
            //executeInit(inputs,callbackContext);
            return true;
        } else if (ACTION_SHOW_BANNER.equals(action)) {
            //executeCreateInterstitialView(inputs, callbackContext);
            executeShowBanner(inputs,callbackContext);
            return true;
        } else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {
        	executeShowInterstitial(inputs, callbackContext);
            //executeRequestAd(inputs, callbackContext);
            return true;
        } else if (ACTION_HIDE_BANNER.equals(action)) {
            //executeKillAd(callbackContext);
            return true;
        } else {
            Log.d(LOGTAG, String.format("Invalid action passed: %s", action));
            callbackContext.error("Invalid Action");
        }
        return false;
    }

    private String bannerDomain = "";
    private String bannerPortraitId = "";
    private String bannerLandscapeId = "";
    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
    
    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {
        
        try {
        	JSONObject data = inputs.getJSONObject(0);
        	this.bannerDomain = data.getString(JSON_KEY_DOMAIN);
        	this.bannerPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);
        	this.bannerLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);
        	this.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);
        	
    		// Create the AdView on the UI thread.
    		Log.w(LOGTAG, "createBannerView");
    		Runnable runnable = new Runnable() {
    			public void run() {
    				Log.w(LOGTAG, "ShowBanner:" + poolId);
    				Log.w(LOGTAG, String.valueOf(webView));
    					
    				LinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();
    				AdBanner banner =  new  AdBanner(webView.getContext(), bannerDomain, bannerPortraitId, bannerLandscapeId);
    				banner.setAdChangeInterval(bannerAdChangeInterval);
					parentView.addView(banner);
    			}
    		};
    		this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
    }
    
    private String interstitialDomain = "";
    private String interstitialPortraitId = "";
    private String interstitialLandscapeId = "";

    
    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {
        try {
        	JSONObject data = inputs.getJSONObject(0);
        	this.interstitialDomain = data.getString(JSON_KEY_DOMAIN);
        	this.interstitialPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);
        	this.interstitialLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);

        	// Create the AdView on the UI thread.
    		Log.w(LOGTAG, "createInterstitialView");
    		Runnable runnable = new Runnable() {
    			public void run() {
    				Log.w(LOGTAG, "ShowInterstitial:" + poolId);
    				Log.w(LOGTAG, String.valueOf(webView));
    					
    				final AdInterstitial adInterstitial = new AdInterstitial(webView.getContext(), interstitialDomain, interstitialPortraitId, interstitialLandscapeId);
    				adInterstitial.setAdEventsListener(new AdEventsListener() {
    				 @Override
    				  public void onAdFailedToLoad(AdError e) {
    					 Log.w(LOGTAG, "onAdFailedToLoad");
    				  }
    				 
    				  @Override
    				  public void onAdDidLoad() {
    					  Log.w(LOGTAG, "onAdDidLoad");
    					  adInterstitial.show();
    				  }
    				 
    				  @Override
    				  public void onActionWillBegin(MRAIDAction action) {
    					  Log.w(LOGTAG, "onActionWillBegin");
    				  }
    				 
    				  @Override
    				  public void onActionDidFinish(MRAIDAction action) {
    					  Log.w(LOGTAG, "onActionDidFinish");
    				  }
    				 
    				 @Override
    				   public void onActionDenied() {
    					 Log.w(LOGTAG, "onActionDenied"); 
    				   }

    				  @Override
    				  public void onActionDidBegin(MRAIDAction action) {
    					  Log.w(LOGTAG, "onActionDidBegin");
    				  }
    				 
    				  @Override
    				  public void onAdClose() {
    					  Log.w(LOGTAG, "onAdClose");
    				  }
    				 });
    				adInterstitial.startLoading();
    			}
    		};
    		this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
        
    }


}
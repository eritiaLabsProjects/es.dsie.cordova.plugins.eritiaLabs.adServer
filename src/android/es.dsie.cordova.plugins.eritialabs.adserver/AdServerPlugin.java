package es.dsie.cordova.plugins.eritialabs.adserver;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LinearLayoutSoftKeyboardDetect;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dsie.cordova.plugins.eritialabs.adserver.view.BannerView;

import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;


public class AdServerPlugin extends CordovaPlugin {
	private static final String LOGTAG = "OpenXPlugin";
	
    private static final String ACTION_INIT = "init";
    private static final String ACTION_SHOW_BANNER = "showBanner";
    private static final String ACTION_SHOW_INTERSTITIAL = "showInterstitial";
    private static final String ACTION_HIDE_BANNER = "hideBanner";
    
    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;
    
    private static final String JSON_KEY_DOMAIN = "domain";
    private static final String JSON_KEY_ZONE_ID = "zoneId";
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
    private String bannerZoneId = "";
    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
    
    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {
        
        try {
        	JSONObject data = inputs.getJSONObject(0);
        	this.bannerDomain = data.getString(JSON_KEY_DOMAIN);
        	this.bannerZoneId = data.getString(JSON_KEY_ZONE_ID);
        	this.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);
        	
    		// Create the AdView on the UI thread.
    		Log.w(LOGTAG, "createBannerView");
    		Runnable runnable = new Runnable() {
    			public void run() {
    				Log.w(LOGTAG, "ShowBanner:" + poolId);
    				Log.w(LOGTAG, String.valueOf(webView));
    					
    				LinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();
    				WebView banner = new BannerView(webView.getContext()).loadAd(bannerDomain,bannerZoneId,bannerAdChangeInterval);
					parentView.addView(banner,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 50));
    			}
    		};
    		this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
    }
    
    private String interstitialDomain = "";
    private String interstitialZoneId = "";

    
    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {
        try {
        	JSONObject data = inputs.getJSONObject(0);
        	this.interstitialDomain = data.getString(JSON_KEY_DOMAIN);
        	this.interstitialZoneId = data.getString(JSON_KEY_ZONE_ID);

        	// Create the AdView on the UI thread.
    		Log.w(LOGTAG, "createInterstitialView");
    		Runnable runnable = new Runnable() {
    			public void run() {
    				Log.w(LOGTAG, "ShowInterstitial:" + poolId);
    				Log.w(LOGTAG, String.valueOf(webView));
    			}
    		};
    		this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
        
    }


}
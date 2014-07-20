{"filter":false,"title":"AdServerPlugin.java","tooltip":"/src/android/es.dsie.cordova.plugins.eritialabs.adserver/AdServerPlugin.java","undoManager":{"mark":1,"position":1,"stack":[[{"group":"doc","deltas":[{"action":"insertText","range":{"start":{"row":0,"column":0},"end":{"row":0,"column":38}},"text":"package es.dsie.cordova.plugins.openx;"},{"action":"insertText","range":{"start":{"row":0,"column":38},"end":{"row":1,"column":0}},"text":"\n"},{"action":"insertLines","range":{"start":{"row":1,"column":0},"end":{"row":163,"column":0}},"lines":["","import org.apache.cordova.CallbackContext;","import org.apache.cordova.CordovaInterface;","import org.apache.cordova.CordovaPlugin;","import org.apache.cordova.LinearLayoutSoftKeyboardDetect;","import org.json.JSONArray;","import org.json.JSONException;","import org.json.JSONObject;","","import com.openx.ad.mobile.sdk.interfaces.AdEventsListener;","import com.openx.errors.AdError;","import com.openx.model.MRAIDAction;","import com.openx.view.AdBanner;","import com.openx.view.AdInterstitial;","","import android.util.Log;","","","public class OpenXPlugin extends CordovaPlugin {","\tprivate static final String LOGTAG = \"OpenXPlugin\";","\t","    private static final String ACTION_INIT = \"init\";","    private static final String ACTION_SHOW_BANNER = \"showBanner\";","    private static final String ACTION_SHOW_INTERSTITIAL = \"showInterstitial\";","    private static final String ACTION_HIDE_BANNER = \"hideBanner\";","    ","    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;","    ","    private static final String JSON_KEY_DOMAIN = \"domain\";","    private static final String JSON_KEY_PORTRAIT_ID = \"portraitId\";","    private static final String JSON_KEY_LANDSCAPE_ID = \"landscapeId\";","    private static final String JSON_KEY_CHANGE_INTERVAL = \"changeInterval\";","    ","    private String poolId = \"\";","","    @Override","    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {","        if (ACTION_INIT.equals(action)) {","            //executeCreateBannerView(inputs, callbackContext);","            //executeInit(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_BANNER.equals(action)) {","            //executeCreateInterstitialView(inputs, callbackContext);","            executeShowBanner(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {","        \texecuteShowInterstitial(inputs, callbackContext);","            //executeRequestAd(inputs, callbackContext);","            return true;","        } else if (ACTION_HIDE_BANNER.equals(action)) {","            //executeKillAd(callbackContext);","            return true;","        } else {","            Log.d(LOGTAG, String.format(\"Invalid action passed: %s\", action));","            callbackContext.error(\"Invalid Action\");","        }","        return false;","    }","","    private String bannerDomain = \"\";","    private String bannerPortraitId = \"\";","    private String bannerLandscapeId = \"\";","    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;","    ","    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {","        ","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.bannerDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.bannerPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);","        \tthis.bannerLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);","        \tthis.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);","        \t","    \t\t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createBannerView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowBanner:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t\t\t","    \t\t\t\tLinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();","    \t\t\t\tAdBanner banner =  new  AdBanner(webView.getContext(), bannerDomain, bannerPortraitId, bannerLandscapeId);","    \t\t\t\tbanner.setAdChangeInterval(bannerAdChangeInterval);","\t\t\t\t\tparentView.addView(banner);","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","    }","    ","    private String interstitialDomain = \"\";","    private String interstitialPortraitId = \"\";","    private String interstitialLandscapeId = \"\";","","    ","    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.interstitialDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.interstitialPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);","        \tthis.interstitialLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);","","        \t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createInterstitialView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowInterstitial:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t\t\t","    \t\t\t\tfinal AdInterstitial adInterstitial = new AdInterstitial(webView.getContext(), interstitialDomain, interstitialPortraitId, interstitialLandscapeId);","    \t\t\t\tadInterstitial.setAdEventsListener(new AdEventsListener() {","    \t\t\t\t @Override","    \t\t\t\t  public void onAdFailedToLoad(AdError e) {","    \t\t\t\t\t Log.w(LOGTAG, \"onAdFailedToLoad\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onAdDidLoad() {","    \t\t\t\t\t  Log.w(LOGTAG, \"onAdDidLoad\");","    \t\t\t\t\t  adInterstitial.show();","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionWillBegin(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionWillBegin\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionDidFinish(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionDidFinish\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t @Override","    \t\t\t\t   public void onActionDenied() {","    \t\t\t\t\t Log.w(LOGTAG, \"onActionDenied\"); ","    \t\t\t\t   }","","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionDidBegin(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionDidBegin\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onAdClose() {","    \t\t\t\t\t  Log.w(LOGTAG, \"onAdClose\");","    \t\t\t\t  }","    \t\t\t\t });","    \t\t\t\tadInterstitial.startLoading();","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","        ","    }","",""]},{"action":"insertText","range":{"start":{"row":163,"column":0},"end":{"row":163,"column":1}},"text":"}"}]}],[{"group":"doc","deltas":[{"action":"removeText","range":{"start":{"row":163,"column":0},"end":{"row":163,"column":1}},"text":"}"},{"action":"removeLines","range":{"start":{"row":0,"column":0},"end":{"row":163,"column":0}},"nl":"\n","lines":["package es.dsie.cordova.plugins.openx;","","import org.apache.cordova.CallbackContext;","import org.apache.cordova.CordovaInterface;","import org.apache.cordova.CordovaPlugin;","import org.apache.cordova.LinearLayoutSoftKeyboardDetect;","import org.json.JSONArray;","import org.json.JSONException;","import org.json.JSONObject;","","import com.openx.ad.mobile.sdk.interfaces.AdEventsListener;","import com.openx.errors.AdError;","import com.openx.model.MRAIDAction;","import com.openx.view.AdBanner;","import com.openx.view.AdInterstitial;","","import android.util.Log;","","","public class OpenXPlugin extends CordovaPlugin {","\tprivate static final String LOGTAG = \"OpenXPlugin\";","\t","    private static final String ACTION_INIT = \"init\";","    private static final String ACTION_SHOW_BANNER = \"showBanner\";","    private static final String ACTION_SHOW_INTERSTITIAL = \"showInterstitial\";","    private static final String ACTION_HIDE_BANNER = \"hideBanner\";","    ","    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;","    ","    private static final String JSON_KEY_DOMAIN = \"domain\";","    private static final String JSON_KEY_PORTRAIT_ID = \"portraitId\";","    private static final String JSON_KEY_LANDSCAPE_ID = \"landscapeId\";","    private static final String JSON_KEY_CHANGE_INTERVAL = \"changeInterval\";","    ","    private String poolId = \"\";","","    @Override","    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {","        if (ACTION_INIT.equals(action)) {","            //executeCreateBannerView(inputs, callbackContext);","            //executeInit(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_BANNER.equals(action)) {","            //executeCreateInterstitialView(inputs, callbackContext);","            executeShowBanner(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {","        \texecuteShowInterstitial(inputs, callbackContext);","            //executeRequestAd(inputs, callbackContext);","            return true;","        } else if (ACTION_HIDE_BANNER.equals(action)) {","            //executeKillAd(callbackContext);","            return true;","        } else {","            Log.d(LOGTAG, String.format(\"Invalid action passed: %s\", action));","            callbackContext.error(\"Invalid Action\");","        }","        return false;","    }","","    private String bannerDomain = \"\";","    private String bannerPortraitId = \"\";","    private String bannerLandscapeId = \"\";","    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;","    ","    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {","        ","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.bannerDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.bannerPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);","        \tthis.bannerLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);","        \tthis.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);","        \t","    \t\t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createBannerView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowBanner:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t\t\t","    \t\t\t\tLinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();","    \t\t\t\tAdBanner banner =  new  AdBanner(webView.getContext(), bannerDomain, bannerPortraitId, bannerLandscapeId);","    \t\t\t\tbanner.setAdChangeInterval(bannerAdChangeInterval);","\t\t\t\t\tparentView.addView(banner);","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","    }","    ","    private String interstitialDomain = \"\";","    private String interstitialPortraitId = \"\";","    private String interstitialLandscapeId = \"\";","","    ","    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.interstitialDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.interstitialPortraitId = data.getString(JSON_KEY_PORTRAIT_ID);","        \tthis.interstitialLandscapeId = data.getString(JSON_KEY_LANDSCAPE_ID);","","        \t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createInterstitialView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowInterstitial:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t\t\t","    \t\t\t\tfinal AdInterstitial adInterstitial = new AdInterstitial(webView.getContext(), interstitialDomain, interstitialPortraitId, interstitialLandscapeId);","    \t\t\t\tadInterstitial.setAdEventsListener(new AdEventsListener() {","    \t\t\t\t @Override","    \t\t\t\t  public void onAdFailedToLoad(AdError e) {","    \t\t\t\t\t Log.w(LOGTAG, \"onAdFailedToLoad\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onAdDidLoad() {","    \t\t\t\t\t  Log.w(LOGTAG, \"onAdDidLoad\");","    \t\t\t\t\t  adInterstitial.show();","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionWillBegin(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionWillBegin\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionDidFinish(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionDidFinish\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t @Override","    \t\t\t\t   public void onActionDenied() {","    \t\t\t\t\t Log.w(LOGTAG, \"onActionDenied\"); ","    \t\t\t\t   }","","    \t\t\t\t  @Override","    \t\t\t\t  public void onActionDidBegin(MRAIDAction action) {","    \t\t\t\t\t  Log.w(LOGTAG, \"onActionDidBegin\");","    \t\t\t\t  }","    \t\t\t\t ","    \t\t\t\t  @Override","    \t\t\t\t  public void onAdClose() {","    \t\t\t\t\t  Log.w(LOGTAG, \"onAdClose\");","    \t\t\t\t  }","    \t\t\t\t });","    \t\t\t\tadInterstitial.startLoading();","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","        ","    }","",""]},{"action":"insertText","range":{"start":{"row":0,"column":0},"end":{"row":0,"column":52}},"text":"package es.dsie.cordova.plugins.eritialabs.adserver;"},{"action":"insertText","range":{"start":{"row":0,"column":52},"end":{"row":1,"column":0}},"text":"\n"},{"action":"insertLines","range":{"start":{"row":1,"column":0},"end":{"row":115,"column":0}},"lines":["","import org.apache.cordova.CallbackContext;","import org.apache.cordova.CordovaPlugin;","import org.apache.cordova.LinearLayoutSoftKeyboardDetect;","import org.json.JSONArray;","import org.json.JSONException;","import org.json.JSONObject;","","import es.dsie.cordova.plugins.eritialabs.adserver.view.BannerView;","","import android.util.Log;","import android.view.ViewGroup.LayoutParams;","import android.webkit.WebView;","import android.widget.LinearLayout;","","","public class AdServerPlugin extends CordovaPlugin {","\tprivate static final String LOGTAG = \"OpenXPlugin\";","\t","    private static final String ACTION_INIT = \"init\";","    private static final String ACTION_SHOW_BANNER = \"showBanner\";","    private static final String ACTION_SHOW_INTERSTITIAL = \"showInterstitial\";","    private static final String ACTION_HIDE_BANNER = \"hideBanner\";","    ","    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;","    ","    private static final String JSON_KEY_DOMAIN = \"domain\";","    private static final String JSON_KEY_ZONE_ID = \"zoneId\";","    private static final String JSON_KEY_CHANGE_INTERVAL = \"changeInterval\";","    ","    private String poolId = \"\";","","    @Override","    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {","        if (ACTION_INIT.equals(action)) {","            //executeCreateBannerView(inputs, callbackContext);","            //executeInit(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_BANNER.equals(action)) {","            //executeCreateInterstitialView(inputs, callbackContext);","            executeShowBanner(inputs,callbackContext);","            return true;","        } else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {","        \texecuteShowInterstitial(inputs, callbackContext);","            //executeRequestAd(inputs, callbackContext);","            return true;","        } else if (ACTION_HIDE_BANNER.equals(action)) {","            //executeKillAd(callbackContext);","            return true;","        } else {","            Log.d(LOGTAG, String.format(\"Invalid action passed: %s\", action));","            callbackContext.error(\"Invalid Action\");","        }","        return false;","    }","","    private String bannerDomain = \"\";","    private String bannerZoneId = \"\";","    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;","    ","    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {","        ","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.bannerDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.bannerZoneId = data.getString(JSON_KEY_ZONE_ID);","        \tthis.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);","        \t","    \t\t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createBannerView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowBanner:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t\t\t","    \t\t\t\tLinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();","    \t\t\t\tWebView banner = new BannerView(webView.getContext()).loadAd(bannerDomain,bannerZoneId,bannerAdChangeInterval);","\t\t\t\t\tparentView.addView(banner,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 50));","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","    }","    ","    private String interstitialDomain = \"\";","    private String interstitialZoneId = \"\";","","    ","    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {","        try {","        \tJSONObject data = inputs.getJSONObject(0);","        \tthis.interstitialDomain = data.getString(JSON_KEY_DOMAIN);","        \tthis.interstitialZoneId = data.getString(JSON_KEY_ZONE_ID);","","        \t// Create the AdView on the UI thread.","    \t\tLog.w(LOGTAG, \"createInterstitialView\");","    \t\tRunnable runnable = new Runnable() {","    \t\t\tpublic void run() {","    \t\t\t\tLog.w(LOGTAG, \"ShowInterstitial:\" + poolId);","    \t\t\t\tLog.w(LOGTAG, String.valueOf(webView));","    \t\t\t}","    \t\t};","    \t\tthis.cordova.getActivity().runOnUiThread(runnable);","        } catch (JSONException exception) {","            Log.w(LOGTAG,String.format(\"Got JSON Exception: %s\",exception.getMessage()));","            callbackContext.error(exception.getMessage());","        }","        ","    }","",""]},{"action":"insertText","range":{"start":{"row":115,"column":0},"end":{"row":115,"column":1}},"text":"}"}]}]]},"ace":{"folds":[],"scrolltop":60,"scrollleft":0,"selection":{"start":{"row":23,"column":53},"end":{"row":23,"column":53},"isBackwards":false},"options":{"guessTabSize":true,"useWrapMode":false,"wrapToView":true},"firstLineState":{"row":61,"state":"start","mode":"ace/mode/java"}},"timestamp":1405845735762,"hash":"d56082cde39f15eb3f2c0c0685bd68da5d249489"}
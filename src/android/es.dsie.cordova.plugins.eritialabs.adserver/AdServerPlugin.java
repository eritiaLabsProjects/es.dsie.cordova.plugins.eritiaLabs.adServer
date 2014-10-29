package es.dsie.cordova.plugins.eritialabs.adserver;

import java.util.concurrent.Callable;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LinearLayoutSoftKeyboardDetect;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dsie.cordova.plugins.eritialabs.adserver.view.BannerView;
import es.dsie.cordova.plugins.eritialabs.adserver.view.InterstitialView;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import android.animation.AnimatorSet;
import android.transition.Visibility;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;


public class AdServerPlugin extends CordovaPlugin {
    private static final String LOGTAG = "AdServerPlugin";
    
    private static final String ACTION_INIT = "init";
    private static final String ACTION_SHOW_BANNER = "showBanner";
    private static final String ACTION_IS_BANNER_VISIBLE = "isBannerVisible";
    private static final String ACTION_SHOW_INTERSTITIAL = "showInterstitial";
    private static final String ACTION_HIDE_BANNER = "hideBanner";
    
    private static final int DEFAULT_AD_CHANGE_INTERVAL = 60000;
    private static final int DEFAULT_AD_HEIGHT = 60;
    
    private static final String JSON_KEY_DOMAIN = "domain";
    private static final String JSON_KEY_ZONE_ID = "zoneId";
    private static final String JSON_KEY_CHANGE_INTERVAL = "changeInterval";
    private static final String JSON_KEY_SOURCE = "source";
    private static final String JSON_KEY_HEIGHT = "height";
    
    private String poolId = "";
    private int _duration = 500;

    private String bannerSource = "";
    private String bannerDomain = "";
    private String bannerZoneId = "";
    private int    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
    private int    bannerHeight = DEFAULT_AD_HEIGHT;
    private BannerView webViewBanner = null;

    private String interstitialSource = "";
    private String interstitialDomain = "";
    private String interstitialZoneId = "";
    private int    interstitialAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;

    private InterstitialView webViewInterstitial = null;



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
        } else if (ACTION_IS_BANNER_VISIBLE.equals(action)) {
            //executeCreateInterstitialView(inputs, callbackContext);
            return executeIsBannerVisible(inputs,callbackContext);
        }
        else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {
            executeShowInterstitial(inputs, callbackContext);
            //executeRequestAd(inputs, callbackContext);
            return true;
        } else if (ACTION_HIDE_BANNER.equals(action)) {
            //executeKillAd(callbackContext);
            executeHideBanner();
            return true;
        } else {
            Log.d(LOGTAG, String.format("Invalid action passed: %s", action));
            callbackContext.error("Invalid Action");
        }
        return false;
    }
    
    private void executeShowBanner(JSONArray inputs, CallbackContext callbackContext) {
        
        try {
            JSONObject data = inputs.getJSONObject(0);
            this.bannerSource = data.getString(JSON_KEY_SOURCE);
            this.bannerDomain = data.getString(JSON_KEY_DOMAIN);
            this.bannerZoneId = data.getString(JSON_KEY_ZONE_ID);
            this.bannerAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);
            this.bannerHeight = data.getInt(JSON_KEY_HEIGHT);
            
            // Create the AdView on the UI thread.
            Log.w(LOGTAG, "createBannerView");
            Runnable runnable = new Runnable() {
                public void run() {
                    Log.i(LOGTAG, "ShowBanner:" + poolId);
                    Log.v(LOGTAG, String.valueOf(webView));
                    
                    Log.v(LOGTAG,"Density: " + webView.getResources().getDisplayMetrics().density);
                    
                    Float calcHeight = Float.valueOf(bannerHeight * webView.getResources().getDisplayMetrics().density);
                    Log.v(LOGTAG,"calcHeight: " + calcHeight);
                    
                    LinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();
                    if(webViewBanner == null) {
                        webViewBanner = new BannerView(webView.getContext());
                        webViewBanner.setVisibility(View.INVISIBLE);
                        parentView.addView(webViewBanner,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, calcHeight.intValue()));
                    }
                    webViewBanner = webViewBanner.loadAd(bannerSource,bannerDomain,bannerZoneId,bannerAdChangeInterval);
                    slideToTopAndShow(webViewBanner,true,null);
                }
            };
            this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
    }

    private void executeHideBanner() {
        Runnable runnable = new Runnable() {
            public void run() {
                if(webViewBanner != null) {
                    Log.v(LOGTAG,"Se ocultaría el Banner.");
                    slideToBottomAndHide(webViewBanner,true,null);
//                  ((ViewGroup)webViewBanner.getParent()).removeView(webViewBanner);
//                  webViewBanner.destroy();
                    //webViewBanner = null;
                    //webView.getParent().requestLayout();
                    //webView.requestLayout();
                }
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    private boolean executeIsBannerVisible(JSONArray inputs, CallbackContext callbackContext) {
        boolean result = (webViewBanner != null) && (webViewBanner.isShown() || webViewBanner.getVisibility() != View.VISIBLE);
        return result;
    }
    
    private void executeShowInterstitial(JSONArray inputs, CallbackContext callbackContext) {
        executeHideInterstitial();
        try {
            JSONObject data = inputs.getJSONObject(0);
            this.interstitialSource = data.getString(JSON_KEY_SOURCE);
            this.interstitialDomain = data.getString(JSON_KEY_DOMAIN);
            this.interstitialZoneId = data.getString(JSON_KEY_ZONE_ID);
            this.interstitialAdChangeInterval = data.getInt(JSON_KEY_CHANGE_INTERVAL);
            
            // Create the AdView on the UI thread.
            Log.w(LOGTAG, "createInterstitialView");
            Runnable runnable = new Runnable() {
                public void run() {
                    Log.i(LOGTAG, "ShowInterstitial:" + poolId);
                    Log.v(LOGTAG, String.valueOf(webView));

                    LinearLayoutSoftKeyboardDetect parentView = (LinearLayoutSoftKeyboardDetect) webView.getParent();
                    if(webViewInterstitial == null) {
                        webViewInterstitial= new InterstitialView(webView.getContext()) {
                            @Override
                            public boolean onKeyUp(int keyCode, KeyEvent event) {
                                Log.i(LOGTAG,"IntersticialView.onKeyDown.keyCode=" + keyCode);
                                Log.i(LOGTAG,"IntersticialView.onKeyDown.event.getKeyCode=" + event.getKeyCode());
                                Log.i(LOGTAG,"IntersticialView.onKeyDown.KeyEvent.KEYCODE_BACK=" + KeyEvent.KEYCODE_BACK);
                                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                    executeHideInterstitial();
                                    return true;
                                } else {
                                    return false;
                                }
                            };
                            @Override
                            public void doClose() {
                                executeHideInterstitial();
                            }
                        };
                        webViewInterstitial.setVisibility(View.INVISIBLE);
                        webViewInterstitial.setBackgroundColor(0x00000099);
                        parentView.addView(webViewInterstitial,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                    }
                    webViewInterstitial = webViewInterstitial.loadAd(interstitialSource,interstitialDomain,interstitialZoneId,interstitialAdChangeInterval);
                    
                    if(webViewBanner != null) {
                        Log.v(LOGTAG,"Se ocultaría el Banner por el Interstitial.");
                        webViewBanner.setVisibility(View.GONE);
                        //slideToBottomAndHide(webViewBanner,false);
                        /*slideToBottomAndHide(webViewBanner,true,new Callable() {
                            @Override
                            public Object call() throws Exception {*/
                                /*return*/ slideToLeftAndShow(webViewInterstitial,true,null);
                            /*}
                        });*/
                    } else {
                        slideToLeftAndShow(webViewInterstitial,true,null);
                    }
                }
            };
            this.cordova.getActivity().runOnUiThread(runnable);
        } catch (JSONException exception) {
            Log.w(LOGTAG,String.format("Got JSON Exception: %s",exception.getMessage()));
            callbackContext.error(exception.getMessage());
        }
        
    }
    
    private void executeHideInterstitial() {
        Runnable runnable = new Runnable() {
            public void run() {
                if(webViewInterstitial != null) {
                    Log.v(LOGTAG,"Se ocultaría el Interstitial.");
                    if((webViewBanner != null) && (webViewBanner.isShown() || webViewBanner.getVisibility() != View.VISIBLE)) {
                        Log.v(LOGTAG,"Se mostraría el Banner.");
                        //slideToTopAndShow(webViewBanner,true);
                        //webViewBanner.setVisibility(View.VISIBLE);
                        slideToRightAndHide(webViewInterstitial,true,new Callable() {
                            @Override
                            public Object call() throws Exception {
                                return slideToTopAndShow(webViewBanner,true,null);
                            }
                        });
                    } else {
                        slideToRightAndHide(webViewInterstitial,true,null);
                    }
                }
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    private boolean executeIsInterstitialVisible(JSONArray inputs, CallbackContext callbackContext) {
        boolean result = (webViewInterstitial != null) && (webViewInterstitial.isShown() || webViewInterstitial.getVisibility() != View.VISIBLE);
        return result;
    }
    
    public Animation slideToBottomAndHide(final View view, boolean autoStart,final Callable callback){
        final View parentView = (View)webView.getParent();
        int from = 0;//view.getTop();
        int to = view.getHeight();
        Log.v(LOGTAG,"slideToBottomAndHide de " + from + " a " + to + ".");
        TranslateAnimation animate = new TranslateAnimation(0,0,from,to);
        animate.setDuration(_duration);
        animate.setFillAfter(false);
        animate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                parentView.requestLayout();
                try {
                    if(callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                parentView.requestLayout();
            }
        });
        if(autoStart) {
            view.startAnimation(animate);
        }
        return animate;
    }
    
    public Animation slideToTopAndShow(final View view, boolean autoStart,final Callable callback){
        final  View parentView = (View)webView.getParent();
        int from = 0;//parentView.getHeight();
        int to = (-view.getHeight());
        Log.v(LOGTAG,"slideToTopAndShow de " + from + " a " + to + ".");
        TranslateAnimation animate = new TranslateAnimation(0,0,from,to);
        animate.setDuration(_duration);
        animate.setFillAfter(false);
        animate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                parentView.requestLayout();
                view.setVisibility(View.VISIBLE);
                try {
                    if(callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                parentView.requestLayout();
            }
        });
        if(autoStart) {
            view.startAnimation(animate);
        }
        return animate;
    }
    
    public Animation slideToLeftAndShow(final View view, boolean autoStart,final Callable callback){
        final  View parentView = (View)webView.getParent();
        TranslateAnimation animate = new TranslateAnimation(view.getWidth(),0,0,0);
        animate.setDuration(_duration);
        animate.setFillAfter(false);
        animate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                parentView.requestLayout();
                view.setVisibility(View.VISIBLE);
                view.requestFocus();
                try {
                    if(callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                parentView.requestLayout();
                
            }
        });
        if(autoStart) {
            view.startAnimation(animate);
        }
        return animate;
    }
    
    public Animation slideToRightAndHide(final View view, boolean autoStart,final Callable callback){
        final View parentView = (View)webView.getParent();
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(_duration);
        animate.setFillAfter(false);
        animate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                parentView.requestLayout();
                view.setVisibility(View.GONE);
                try {
                    if(callback != null) {
                        callback.call();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                parentView.requestLayout();
            }
        });
        if(autoStart) {
            view.startAnimation(animate);
        }
        return animate;
    }


}
package es.dsie.cordova.plugins.eritialabs.adserver.view;

import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class InterstitialView extends WebView {
	private static final String LOGTAG = "AdServerPlugin";
	
	private final String _TPL_URL = "%s/www/delivery/al.php?refresh=%d" +
									"&zoneid=%s" + 
									"&source=%s" + 
									"&target=%s" + 
									"&cb=%d" + 
									"&ct0=%s" + 
									"&adstext=Ads" + 
									"&closebutton=special" + 
									"&layerstyle=simple" + 
									"&align=center" + 
									"&valign=middle" + 
									"&padding=10" + 
									"&buttonsize=14" + 
									"&deferclose=5" +
									"&noborder=t";
	private final String _TPL_URL_S = "%s/www/delivery/afr.php?refresh=%d&zoneid=%s&source=%s&target=%s&cb=%d&ct0=%s";	
	
	/*
	 * ox_u += "zoneid=" + (QueryString.zoneid?QueryString.zoneid:"6");
   		ox_u += "&source=" + (QueryString.source?QueryString.source:"BacoApp");
   		ox_u += "&target=_system";
   		ox_u += "&ct0={clickurl_enc}";
   		ox_u += "&layerstyle=" + (QueryString.layerstyle?QueryString.layerstyle:"simple");
   		ox_u += "&align=" + (QueryString.align?QueryString.align:"center");
   		ox_u += "&valign=" + (QueryString.valign?QueryString.valign:"middle");
   		ox_u += "&padding=" + (QueryString.padding?QueryString.padding:"10");
   		ox_u += "&closetime=" + (QueryString.closetime?QueryString.closetime:"15");
   		ox_u += "&shifth=" + (QueryString.shifth?QueryString.shifth:"0");
   		ox_u += "&closebutton=" + (QueryString.closebutton?QueryString.closebutton:"special");
   		ox_u += "&nobg=" + (QueryString.nobg?QueryString.nobg:"t");
   		ox_u += "&noborder=" + (QueryString.noborder?QueryString.noborder:"t");
   		ox_u += "&buttonsize=" + (QueryString.buttonsize?QueryString.buttonsize:"14");
   		ox_u += "&shiftv=" + (QueryString.shiftv?QueryString.shiftv:"0");
   		ox_u += "&adstext=" + (QueryString.adstext?QueryString.adstext:"Ads");
   		ox_u += "&adstextstyle=" + (QueryString.adstextstyle?QueryString.adstextstyle:"");
   		ox_u += "&deferclose=" + (QueryString.deferclose?QueryString.deferclose:"5");
	 */
	
	
	private final String _RETURN_URL = "{clickurl_enc}";
	private final String _SOURCE = "BacoApp";
	private final String _TARGET = "_system";

	public InterstitialView(Context context) {
		super(context);
		this.getSettings().setJavaScriptEnabled(true);
		this.getSettings().setGeolocationEnabled(true);
		this.addJavascriptInterface(this, "iview");
		
		String script = 
				"javascript:window.injectPostMessage = function(e) { \n" +  
				"   if(typeof e == 'string') { \n" +  
				"		iview.doPostMessage(e); \n" +  
				"	} else { \n" +  
				"		iview.doPostMessage(e.message); \n" +  
				"	}\n" +  
				"};\n";
		
		Log.i(LOGTAG, "InterstitialView.loadAd.script=" + script);
		this.loadUrl(script);
	}
	
	public InterstitialView loadAd(String source, String domain,String zoneId,int changeInterval) {
		Random rand = new Random();
		Log.v(LOGTAG, "InterstitialView.loadAd.source:" + source);
		Log.v(LOGTAG, "InterstitialView.loadAd.domain:" + domain);
		Log.v(LOGTAG, "InterstitialView.loadAd.zoneId:" + zoneId);
		Log.v(LOGTAG, "InterstitialView.loadAd.changeInterval:" + changeInterval);

		String url = String.format(_TPL_URL,domain,changeInterval,zoneId,source,_TARGET,rand.nextInt(),_RETURN_URL);
		Log.i(LOGTAG, "InterstitialView.loadAd.URL:" + url);
		String script = 
				"javascript:setTimeout( \n" +  
				"   function(){ \n" +  
				"   	window.location.origin = 'http://adserver.eritialabs.es';  \n" +  
				"   	document.write(\"<scr\" + \"ipt type='text/javascript' src='" + url + "'></scr\" + \"ipt>\"); \n" +  
				"   },50 \n" +  
				"); \n";
		Log.i(LOGTAG, "InterstitialView.loadAd.script=" + script);
		this.loadUrl(script);
		
		/* Sin código, directamente delde el SERVER.
		String url = String.format(_TPL_URL_S,domain,changeInterval,zoneId,source,_TARGET,rand.nextInt(),_RETURN_URL);
		this.loadUrl(url);
		*/
		
		
		return this;
	}
	
	public void doClose() {
	}
	
	@JavascriptInterface	    
    public void doPostMessage(String ev) {
		Log.v(LOGTAG, "InterstitialView.doPostMessage.event=" + ev);
		if(ev instanceof String) {
			String text = (String) ev;
			Log.v(LOGTAG, "InterstitialView.doPostMessage.text=" + text);
			if(text.toLowerCase().indexOf("closed") >= 0) {
				Log.v(LOGTAG, "InterstitialView.doPostMessage.doClose()");
				this.doClose();
			}
		}
	}
	

}

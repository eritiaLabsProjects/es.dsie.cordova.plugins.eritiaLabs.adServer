package es.dsie.cordova.plugins.eritialabs.adserver.view;

import java.util.Random;

import android.content.Context;
import android.webkit.WebView;

public class BannerView extends WebView {
	//<iframe id='acaa89b6' name='acaa89b6' src='http://adserver.eritialabs.es/www/delivery/afr.php?refresh=600&zoneid=3&source=BacoApp&target=_blank&cb=" + (Math.floor(Math.random() * 100) + 1) + "&ct0=INSERT_ENCODED_CLICKURL_HERE' frameborder='0' scrolling='no' width='100%' height='50' allowtransparency='true'><a href='http://adserver.eritialabs.es/www/delivery/ck.php?n=a9f4cc9d&cb="+ Math.random() + "' target='_blank'><img src='http://adserver.eritialabs.es/www/delivery/avw.php?zoneid=1&source=BacoApp&cb=" + (Math.floor(Math.random() * 100) + 1) + "&n=a9f4cc9d&ct0=INSERT_ENCODED_CLICKURL_HERE' border='0' alt='' /></a></iframe>
	private final String _TPL_URL = "http://%s/www/delivery/afr.php?refresh=%d&zoneid=%s&source=%s&target=$s&cb=%d&ct0=%s";
	private final String _RETURN_URL = "INSERT_ENCODED_CLICKURL_HERE";
	private final String _SOURCE = "BacoApp";
	private final String _TARGET = "_system";

	public BannerView(Context context) {
		super(context);
	}
	
	public WebView loadAd(String source, String domain,String zoneId,int changeInterval) {
		Random rand = new Random();
		String url = String.format(_TPL_URL,domain,changeInterval,zoneId,source,_TARGET,rand.nextInt(),_RETURN_URL);
		this.loadUrl(url);
		
		return this;
	}

}

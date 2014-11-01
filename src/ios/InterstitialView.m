#import "InterstitialView.h"
@implementation InterstitialView

@synthesize interstitialView;
@synthesize bannerView;

#define _TPL_URL = "%s/www/delivery/al.php?refresh=%d&zoneid=%s&source=%s&target=%s&cb=%d&ct0=%s&adstext=Ads&closebutton=special&layerstyle=simple&align=center&valign=middle&padding=10&buttonsize=14&deferclose=5&noborder=t";
#define _TARGET = "_system";
#define _RETURN_URL = "URL";
#define _URL_SCHEMA = "bacoApp://";

- (UIWebView*) loadAd:(UIView *) parentView:(UIWebView *) mBannerView
{
    bannerView = mBannerView;
    
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.height]);
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.width]);
    
    int y = 20;
    int h = parentView.frame.size.height - y + bannerView.frame.size.height;
    
    //[bannerView setHidden:TRUE];
    interstitialView = [[UIWebView alloc] initWithFrame:CGRectMake(0,y,parentView.frame.size.width,h)];
    interstitialView.delegate = self;
    interstitialView.scalesPageToFit = NO;
    
    NSURL* reqUrl = [NSURL URLWithString:@""];//bacoSchemaApp://showInterstitial];
    NSURLRequest* request = [NSURLRequest requestWithURL:reqUrl];
    [interstitialView loadRequest:request];
    
    return interstitialView;
}

-(void)webViewDidFinishLoad:(UIWebView *)webView {
    NSString *auxUrl = [NSString stringWithFormat:@"%@/www/delivery/al.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%d&ct0=%@&adstext=Ads&closebutton=special&layerstyle=simple&align=center&valign=middle&padding=10&buttonsize=14&deferclose=5&noborder=t",domain,changeInterval,zoneId,source,@"_system",rand(),@"URL"];
    NSLog(auxUrl);
    NSString *url = [NSString stringWithFormat:@"javascript:setTimeout( \n"
                     "   function(){ \n"
                     "       window.location.origin = 'http://adserver.eritialabs.es';  \n"
                     "       document.write(\"<scr\" + \"ipt type='text/javascript' src='"
                     "%@"
                     "'></scr\" + \"ipt>\"); \n"
                     "   },50 \n"
                     "); \n",auxUrl];
    
    [interstitialView stringByEvaluatingJavaScriptFromString:url];
    
    NSString *closeScript = @"javascript:window.injectPostMessage = function(e) { \n"
    @"   if(typeof e == 'string') { \n"
    @"       window.location = 'bacoApp://doPostMessage/' + e; \n"
    @"   } else { \n"
    @"       window.location = 'bacoApp://doPostMessage/' + e.message; \n"
    @"   }\n"
    @"};\n";
    
    [interstitialView stringByEvaluatingJavaScriptFromString:closeScript];
}

-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    NSURL *url = [inRequest URL];
    NSString *urlStr = url.absoluteString;
    NSString *protocolPrefix = @"bacoapp://";
    
    //process only our custom protocol
    if ([[urlStr lowercaseString] hasPrefix:protocolPrefix]) {
        NSLog([NSString stringWithFormat:@"URLSchmea: %@",urlStr]);
        
        //strip protocol from the URL. We will get input to call a native method
        urlStr = [urlStr substringFromIndex:protocolPrefix.length];
        
        //Decode the url string
        urlStr = [urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSArray *components = [urlStr componentsSeparatedByString:@"/"];
        NSString *function = [components objectAtIndex:0];
        
        if([function isEqualToString:@"doPostMessage"]) {
            return [self doPostMessage:components];
        }
        
        return NO;
    }
    return YES;
}

-(BOOL) doPostMessage:(NSArray *)arguments{
    NSString *message = [arguments objectAtIndex:1];
    if([[message lowercaseString] rangeOfString:@"closed"].location != NSNotFound) {
        NSLog(@"InterstitialView.doPostMessage.closed");
        [self.interstitialView setHidden:true];
        [bannerView setHidden:false];
        
    }
}

@end
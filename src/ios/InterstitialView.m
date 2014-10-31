#import "InterstitialView.h"
@implementation InterstitialView

@synthesize interstitialView;

#define _TPL_URL = "%s/www/delivery/al.php?refresh=%d&zoneid=%s&source=%s&target=%s&cb=%d&ct0=%s&adstext=Ads&closebutton=special&layerstyle=simple&align=center&valign=middle&padding=10&buttonsize=14&deferclose=5&noborder=t";
#define _TARGET = "_system";
#define _RETURN_URL = "URL";

- (UIWebView*) loadAd:(UIView *) parentView
{
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.height]);
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.width]);
    
    int y = 20;
    int h = parentView.frame.size.height - y;
    
    interstitialView = [[UIWebView alloc] initWithFrame:CGRectMake(0,y,parentView.frame.size.width,h)];
    interstitialView.delegate = self;
    interstitialView.scalesPageToFit = YES;
    
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
}

-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

@end
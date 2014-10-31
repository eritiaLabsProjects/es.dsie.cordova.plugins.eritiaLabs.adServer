#import "InterstitialView.h"
@implementation InterstitialView

@synthesize interstitialView;

#define _TPL_URL = "%s/www/delivery/al.php?refresh=%d&zoneid=%s&source=%s&target=%s&cb=%d&ct0=%s&adstext=Ads&closebutton=special&layerstyle=simple&align=center&valign=middle&padding=10&buttonsize=14&deferclose=5&noborder=t";
#define _TARGET = "_system";
#define _RETURN_URL = "URL";

- (UIWebView*) loadAd:(UIView *) parentView
{
    NSString *auxUrl = [NSString stringWithFormat:@"%@/www/delivery/al.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%d&ct0=%@&adstext=Ads&closebutton=special&layerstyle=simple&align=center&valign=middle&padding=10&buttonsize=14&deferclose=5&noborder=t",domain,changeInterval,zoneId,source,@"_system",rand(),@"URL"];
    
    NSLog(auxUrl);
    
    
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.height]);
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.width]);
    
    interstitialView = [[UIWebView alloc] initWithFrame:CGRectMake(0,
                                                                   0,
                                                                   parentView.frame.size.width,
                                                                   parentView.frame.size.height)];
    //interstitialView = [UIWebView alloc];
    interstitialView.delegate = self;
    interstitialView.scalesPageToFit = YES;
    
    /*NSURL* url = [NSURL URLWithString:auxUrl];
     NSURLRequest* request = [NSURLRequest requestWithURL:url];
     [self.interstitialView loadRequest:request];*/
    NSString *url = [NSString stringWithFormat:@"javascript:setTimeout( \n"
                     "   function(){ \n"
                     "       window.location.origin = 'http://adserver.eritialabs.es';  \n"
                     "       document.write(\"<scr\" + \"ipt type='text/javascript' src='"
                     "%@"
                     "'></scr\" + \"ipt>\"); \n"
                     "   },50 \n"
                     "); \n",auxUrl];
    
    [interstitialView stringByEvaluatingJavaScriptFromString:url];
    NSURLRequest* request = [NSURLRequest requestWithURL:@"file://"];
    [interstitialView loadRequest:request];
    
    return interstitialView;
}

-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

@end
#import "BannerView.h"
@implementation BannerView

@synthesize bannerView;

#define _TPL_URL = "%@/www/delivery/afr.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%@&ct0=%@";
#define _TARGET = "_system";
#define _RETURN_URL = "URL";

- (UIWebView*) loadAd:(UIView *) parentView
{
    NSString *auxUrl = [NSString stringWithFormat:@"%@/www/delivery/afr.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%d&ct0=%@",domain,changeInterval,zoneId,source,@"_system",rand(),@"URL"];
    
    NSLog(auxUrl);
    
    
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.height]);
    NSLog([NSString stringWithFormat:@"%f",parentView.frame.size.width]);
    
    int y = parentView.frame.size.height - 60;
    int w = parentView.frame.size.width;
    
    bannerView = [[UIWebView alloc] initWithFrame:CGRectMake(0,y,w,60)];
    //bannerView = [UIWebView alloc];
    //bannerView.delegate = self;
    [bannerView setDelegate:self];
    [bannerView setScalesPageToFit:YES];
    
    NSURL* url = [NSURL URLWithString:auxUrl];
    NSURLRequest* request = [NSURLRequest requestWithURL:url];
    [bannerView loadRequest:request];
    
    return bannerView;
}

-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

@end
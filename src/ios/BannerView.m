#import "BannerView.h"
@implementation BannerView

@synthesize bannerView;

#define _TPL_URL = "%@/www/delivery/afr.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%@&ct0=%@";
#define _TARGET = "_system"; 
#define _RETURN_URL = "URL";

- (UIWebView*) loadAd
{
    NSString *auxUrl = [NSString stringWithFormat:@"%@/www/delivery/afr.php?refresh=%@&zoneid=%@&source=%@&target=%@&cb=%@&ct0=%@",domain,changeInterval,zoneId,source,@"_system",@"100",@"URL"];
    
    NSLog(auxUrl);
    
    bannerView = [[UIWebView alloc] initWithFrame:CGRectMake(0, 0, 0, 0)];
    //bannerView = [UIWebView alloc];
    bannerView.delegate = self;
    //bannerView.scalesPageToFit = YES;
    
    NSURL* url = [NSURL URLWithString:auxUrl];
    NSURLRequest* request = [NSURLRequest requestWithURL:url];
    [self.bannerView loadRequest:request];
    
    return bannerView;
}

@end
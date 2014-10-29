#import "AdServerPlugin.h"

@implementation AdServerPlugin

#define LOGTAG    @"AdServerPlugin"

#define ACTION_INIT    @"init"
#define ACTION_SHOW_BANNER   @"showBanner"
#define ACTION_IS_BANNER_VISIBLE         @"isBannerVisible"
#define ACTION_SHOW_INTERSTITIAL   @"showInterstitial"
#define ACTION_HIDE_BANNER         @"hideBanner"

#define DEFAULT_AD_CHANGE_INTERVAL   @60000
#define DEFAULT_AD_HEIGHT      @60

#define JSON_KEY_DOMAIN       @"domain"
#define JSON_KEY_ZONE_ID       @"zoneId"
#define JSON_KEY_CHANGE_INTERVAL       @"changeInterval"
#define JSON_KEY_SOURCE       @"source"
#define JSON_KEY_HEIGHT       @"height"

@synthesize parentView;
@synthesize webViewBanner;

#pragma mark Cordova JS bridge

NSString* poolId;
NSNumber* duration;
NSString* bannerSource;
NSString* bannerDomain;
NSString* bannerZoneId;
NSNumber* bannerAdChangeInterval;
NSString* bannerHeight;

NSString* interstitialSource;
NSString* interstitialDomain;
NSString* interstitialZoneId;
NSNumber* interstitialAdChangeInterval;


- (AdServerPlugin *)initWithWebView:(UIWebView *)theWebView {
	self = (AdServerPlugin *)[super initWithWebView:theWebView];
	if (self) {
		// These notifications are required for re-placing the ad on orientation
		// changes. Start listening for notifications here since we need to
		// translate the Smart Banner constants according to the orientation.
		/*[[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
		[[NSNotificationCenter defaultCenter]
			addObserver:self
			selector:@selector(deviceOrientationChange:)
			name:UIDeviceOrientationDidChangeNotification
			object:nil];*/

		//parentView = self.webView;
        parentView = [self.webView superview];
	}
    poolId = @"";
    duration = @500;
    
    bannerSource = @"";
    bannerDomain = @"";
    bannerZoneId = @"";
    bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
    bannerHeight = DEFAULT_AD_HEIGHT;

    interstitialSource = @"";
    interstitialDomain = @"";
    interstitialZoneId = @"";
    interstitialAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
    
    srand(time(NULL));
	return self;
}

- (void) showBanner:(CDVInvokedUrlCommand *)command
{
    NSLog(@"showBanner");
    
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    NSArray* args = command.arguments;
    
	NSUInteger argc = [args count];
    if( argc >= 1 ) {
        NSDictionary* options = [command.arguments objectAtIndex:0 withDefault:[NSNull null]];

        bannerSource = [options objectForKey:JSON_KEY_SOURCE];
        bannerDomain = [options objectForKey:JSON_KEY_DOMAIN];
        bannerZoneId = [options objectForKey:JSON_KEY_ZONE_ID];
        bannerAdChangeInterval = [options objectForKey:JSON_KEY_CHANGE_INTERVAL];
        bannerHeight = [options objectForKey:JSON_KEY_HEIGHT];
    }

    self.webViewBanner = [BannerView alloc];// initWithAdSize:adSize];

    webViewBanner->source = bannerSource;
    webViewBanner->domain = bannerDomain;
    webViewBanner->zoneId = bannerZoneId;
    webViewBanner->changeInterval = bannerAdChangeInterval;
    
    NSLog(@"showBanner.loadAd");
    [self.webViewBanner loadAd];

    NSLog(@"showBanner.adSubView");
    
    webViewBanner.bannerView.frame = CGRectMake(
                        0,
                        parentView.frame.size.height - 60,
                        parentView.frame.size.width,
                        60);
    self.parentView.frame = CGRectMake(0,
                               0,
                               parentView.frame.size.width,
                               parentView.frame.size.height - 60);

    
    [self.parentView addSubview:webViewBanner.bannerView];
    
    [self.parentView bringSubviewToFront:webViewBanner.bannerView];
    

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}


@ends
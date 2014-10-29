@import "AdServerPlugin.h"

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

@synthesize poolId = "";
@synthesize _duration = 500;

@synthesize parentView;

@synthesize bannerSource = "";
@synthesize bannerDomain = "";
@synthesize bannerZoneId = "";
@synthesize bannerAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;
@synthesize bannerHeight = DEFAULT_AD_HEIGHT;
@synthesize webViewBanner;

@synthesize interstitialSource = "";
@synthesize interstitialDomain = "";
@synthesize interstitialZoneId = "";
@synthesize interstitialAdChangeInterval = DEFAULT_AD_CHANGE_INTERVAL;

@synthesize webViewInterstitial;

#pragma mark Cordova JS bridge

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

		UIView* parentView = self.webView;// : [self.webView superview];
	}
    
    srand(time(NULL));
	return self;
}

- (void) executeShowBanner:(CDVInvokedUrlCommand *)command
{
    NSLog(@"executeShowBanner");
    
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

    self.bannerView = [[BannerView alloc] initWithAdSize:adSize];

    webViewBanner = webViewBanner.loadAd(bannerSource,bannerDomain,bannerZoneId,bannerAdChangeInterval);

    [self.webViewBanner loadAd:[self __buildAdRequest]];
    

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}


@end
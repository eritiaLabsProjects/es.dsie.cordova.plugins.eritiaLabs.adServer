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
@synthesize webViewInterstitial;

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
        
        parentView = self.webView;
        //parentView = [self.webView superview];
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
    bool need2Add = false;
    
    if(self.webViewBanner == nil ) {
        self.webViewBanner = [BannerView alloc];// initWithAdSize:adSize];
        
        NSLog(@"showBanner.adSubView");
        need2Add = true;
    }
    
    webViewBanner->source = bannerSource;
    webViewBanner->domain = bannerDomain;
    webViewBanner->zoneId = bannerZoneId;
    webViewBanner->changeInterval = bannerAdChangeInterval;
    
    NSLog(@"showBanner.loadAd");
    [webViewBanner loadAd:parentView ];
    if(need2Add == true) {
        [self.parentView addSubview:webViewBanner.bannerView];
        [self.parentView bringSubviewToFront:webViewBanner.bannerView];
    }
    
    int w = parentView.frame.size.width;
    int h = parentView.frame.size.height - 60;
    [self.webView setFrame:CGRectMake(0,0,w,h)];
    [self.parentView layoutSubviews];
    
    
    if(webViewBanner.bannerView.hidden == true) {
        NSLog(@"showBanner.Restore Hidden");
        webViewBanner.bannerView.hidden = false;
    }
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) hideBanner:(CDVInvokedUrlCommand *)command {
    NSLog(@"hideBanner");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    
    if(self.webViewBanner != nil) {
        webViewBanner.bannerView.hidden = true;
    }
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) showInterstitial:(CDVInvokedUrlCommand *)command
{
    NSLog(@"showInterstitial");
    
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    NSArray* args = command.arguments;
    
    NSUInteger argc = [args count];
    if( argc >= 1 ) {
        NSDictionary* options = [command.arguments objectAtIndex:0 withDefault:[NSNull null]];
        
        interstitialSource = [options objectForKey:JSON_KEY_SOURCE];
        interstitialDomain = [options objectForKey:JSON_KEY_DOMAIN];
        interstitialZoneId = [options objectForKey:JSON_KEY_ZONE_ID];
        interstitialAdChangeInterval = [options objectForKey:JSON_KEY_CHANGE_INTERVAL];
    }
    bool need2Add = false;
    if(self.webViewInterstitial == nil ) {
        self.webViewInterstitial = [InterstitialView alloc];// initWithAdSize:adSize];
        need2Add = true;
    }
    
    
    webViewInterstitial->source = interstitialSource;
    webViewInterstitial->domain = interstitialDomain;
    webViewInterstitial->zoneId = interstitialZoneId;
    webViewInterstitial->changeInterval = interstitialAdChangeInterval;
    
    NSLog(@"showInterstitial.loadAd");
    [webViewInterstitial loadAd:parentView:webViewBanner.bannerView ];
    if(need2Add == true) {
        [self.parentView addSubview:webViewInterstitial.interstitialView];
        [self.parentView bringSubviewToFront:webViewInterstitial.interstitialView];
    }
    if(webViewInterstitial.interstitialView.hidden == true) {
        NSLog(@"showInterstitial.Restore Hidden");
        webViewInterstitial.interstitialView.hidden = false;
    }
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) hideInterstitial:(CDVInvokedUrlCommand *)command {
    NSLog(@"hideInterstitial");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    
    if(self.webViewInterstitial != nil) {
        webViewInterstitial.interstitialView.hidden = true;
    }
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}



@end
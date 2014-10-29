#import <Cordova/CDV.h>

#import "BannerView.h"
#import "InterstitialView.h"

@interface AdServerPlugin : CDVPlugin <UIWebViewDelegate> {
	- (void) executeShowBanner:(CDVInvokedUrlCommand *)command;
	- (void) showBanner:(CDVInvokedUrlCommand *)command;
	- (void) hideBanner:(CDVInvokedUrlCommand *)command;
	- (void) isBannerVisible:(CDVInvokedUrlCommand *)command;
	- (void) showInterstitial:(CDVInvokedUrlCommand *)command;
	- (void) hideInterstitial:(CDVInvokedUrlCommand *)command;
	- (void) isInterstitialVisible:(CDVInvokedUrlCommand *)command;
}

@property (nonatomic, copy) NSString* callbackId;
@property (nonatomic, retain) UIView* parentView;
@property (nonatomic, retain) BannerView* webViewBanner;
@property (nonatomic, retain) InterstitialView* webViewInterstitial;



@end
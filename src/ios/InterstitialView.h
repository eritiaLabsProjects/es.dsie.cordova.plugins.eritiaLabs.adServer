@interface InterstitialView : UIViewController<UIWebViewDelegate> {
    @public UIWebView* interstitialView;
    @public NSString* source;
    @public NSString* domain;
    @public NSString* zoneId;
    @public NSNumber* changeInterval;
}

- (UIWebView*) loadAd:(UIView*) parentView:(UIWebView*) gannerView;

@property (nonatomic, retain) IBOutlet UIWebView* interstitialView;

@end
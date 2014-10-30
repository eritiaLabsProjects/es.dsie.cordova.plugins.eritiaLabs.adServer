@interface InterstitialView : UIViewController<UIWebViewDelegate> {
    @public UIWebView* interstitialView;
    @public NSString* source;
    @public NSString* domain;
    @public NSString* zoneId;
    @public NSNumber* changeInterval;
}

- (UIWebView*) loadAd:(UIView*) parentView;

@property (nonatomic, retain) IBOutlet UIWebView* interstitialView;

@end
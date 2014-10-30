@interface BannerView : UIViewController<UIWebViewDelegate> {
    @public UIWebView* bannerView;
    @public NSString* source;
    @public NSString* domain;
    @public NSString* zoneId;
    @public NSNumber* changeInterval;
}

- (UIWebView*) loadAd:(UIView*) parentView;

@property (nonatomic, retain) IBOutlet UIWebView* bannerView;

@end
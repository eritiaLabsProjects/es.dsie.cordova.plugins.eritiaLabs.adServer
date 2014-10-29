@interface BannerView : UIViewController<UIWebViewDelegate> {
    UIWebView* mBannerView;

    - (void) loadAd:(String *)command (String *) source (String *) domain (String *) zoneId (int *) changeInterval;
}

@property (nonatomic, retain) IBOutlet UIWebView* bannerView;

@end
@implementation BannerView
@synthesize bannerView = mBannerView;


- (void)dealloc
{
    [mBannerView release];
    [super dealloc];
}

- (void)viewDidUnload
{
    self.bannerView = nil;
    [super viewDidUnload];
}

@end
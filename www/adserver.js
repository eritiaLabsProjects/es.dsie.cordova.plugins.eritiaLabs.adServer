var AdServer = {};

AdServer.init= function(params,successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "AdServerPlugin", "init", [params]);
  };

AdServer.showInterstitial = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "AdServerPlugin", "showInterstitial", [params]);
	};

AdServer.showBanner = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "AdServerPlugin", "showBanner", [params]);
	};

AdServer.hideBanner = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "AdServerPlugin", "hideBanner", [params]);
	};
AdServer.isBannerVisible = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "AdServerPlugin", "isBannerVisible", [params]);
	};

	
module.exports = AdServer;
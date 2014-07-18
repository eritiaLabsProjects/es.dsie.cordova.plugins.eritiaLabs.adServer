var OpenX = {};

OpenX.init= function(params,successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "OpenXPlugin", "init", [params]);
  };

OpenX.showInterstitial = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "OpenXPlugin", "showInterstitial", [params]);
	};

OpenX.showBanner = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "OpenXPlugin", "showBanner", [params]);
	};

OpenX.hideBanner = function(params,successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "OpenXPlugin", "hideBanner", [params]);
	};

module.exports = OpenX;
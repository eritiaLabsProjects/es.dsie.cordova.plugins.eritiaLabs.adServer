{"filter":false,"title":"adserver.js","tooltip":"/www/adserver.js","undoManager":{"mark":0,"position":0,"stack":[[{"group":"doc","deltas":[{"action":"insertText","range":{"start":{"row":0,"column":0},"end":{"row":0,"column":15}},"text":"var OpenX = {};"},{"action":"insertText","range":{"start":{"row":0,"column":15},"end":{"row":1,"column":0}},"text":"\n"},{"action":"insertLines","range":{"start":{"row":1,"column":0},"end":{"row":18,"column":0}},"lines":["","OpenX.init= function(params,successCallback, errorCallback) {","    cordova.exec(successCallback, errorCallback, \"OpenXPlugin\", \"init\", [params]);","  };","","OpenX.showInterstitial = function(params,successCallback, errorCallback) {","\t\tcordova.exec(successCallback, errorCallback, \"OpenXPlugin\", \"showInterstitial\", [params]);","\t};","","OpenX.showBanner = function(params,successCallback, errorCallback) {","\t\tcordova.exec(successCallback, errorCallback, \"OpenXPlugin\", \"showBanner\", [params]);","\t};","","OpenX.hideBanner = function(params,successCallback, errorCallback) {","\t\tcordova.exec(successCallback, errorCallback, \"OpenXPlugin\", \"hideBanner\", [params]);","\t};",""]},{"action":"insertText","range":{"start":{"row":18,"column":0},"end":{"row":18,"column":23}},"text":"module.exports = OpenX;"}]}]]},"ace":{"folds":[],"scrolltop":0,"scrollleft":0,"selection":{"start":{"row":18,"column":23},"end":{"row":18,"column":23},"isBackwards":false},"options":{"guessTabSize":true,"useWrapMode":false,"wrapToView":true},"firstLineState":{"row":29,"mode":"ace/mode/javascript"}},"timestamp":1405688254673,"hash":"34233ba540d07011597a3a6142f90888c18d5c95"}
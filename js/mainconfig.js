// Currently not being used. But should be taken into consideration as a point to setup jquery mobile properties
// Be aware that, this script should be called right after jquery*.js and before jquery.mobile.*.js
// Vease: jquery.mobile.custom
$(document).bind("mobileinit", function(){
  $.extend(  $.mobile , {
      pageLoadErrorMessage: "Error loading page"
  });
  
});
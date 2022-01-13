//function checkActivity() {
//    $.getJSON("../alerts" , {}, function(data) {    	  
//    	  $.each(data, function(key, val) {
////    		  console.log ('Elemento :'+key+' valor: '+val.type);
//    	  });
//    	  console.log ('Elemento primero valor activity level: '+data[0].registeredLevel);	  
// });
//    
//}




$(document).ready(function() {
    console.log('el documento activity level está preparado');
//    checkActivity();
    var $page = $(this);

	  $page.find('.bar').each(function () {
	    var $bar   = $(this),
	        fHeight = $bar.attr('data-percent'),
	        tmo;

	    tmo = setTimeout(function () {
	      $bar.height(fHeight);
	      clearTimeout(tmo);
	    }, 500);
	  });
	  
	  
	  
	  
});





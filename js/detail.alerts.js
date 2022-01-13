$(document).ready(function() {
	console.log('Ready' );
	$('#close_event').attr("href", "../alerts/"+alertId+"/close");
	 $('#close_event').on('click',function(event) {		 
//	 $('body').delegate("#close_event", "click", function(){ 
//  	 		$.get("../alerts/"+alertId+"/close", function(data){
//  	 			$('[data-role=content]').html(data).trigger("create");
//			});
  });

});


(function () {
	$.fullCalendar.defaultOptions = {
		buttonText: {
			prev: '&#x3C;Ant',
			next: 'Sig&#x3E;',
			today: 'Heute'
		},
		monthNames: ["Januar", "Februar", "März", "April", "Mai", "Juni", 
		"Juli", "August", "September", "Oktober", "November", "Dezember"],
     	monthNamesShort: ["Jan","Feb","Mär","Apr","Mai","Jun","Jul","Aug","Sep","Okt","Nov","Dez"],
     	dayNames: ["Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"],
    	dayNamesShort: ["So","Mo","Di","Mi","Do","Fr","Sa"]
	};
})();
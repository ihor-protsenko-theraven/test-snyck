(function () {
	$.fullCalendar.defaultOptions = {
		buttonText: {
			prev: '&#x3C;Prec',
			next: 'Succ&#x3E;',
			today: 'Oggi'
		},
		monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
		     		'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
	 	monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
	 	         		'Lug','Ago','Set','Ott','Nov','Dic'],
	 	dayNames: ['Domenica','Lunedì','Martedì','Mercoledì','Giovedì','Venerdì','Sabato'],
	 	dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
	 	isRTL: false
	};
})();
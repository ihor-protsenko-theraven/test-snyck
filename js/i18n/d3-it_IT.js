var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "€"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['Domenica','Lunedì','Martedì','Mercoledì','Giovedì','Venerdì','Sabato'],
	shortDays: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
	months: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
	 		'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
	shortMonths: ['Gen','Feb','Mar','Apr','Mag','Giu',
	      		'Lug','Ago','Set','Ott','Nov','Dic']
});
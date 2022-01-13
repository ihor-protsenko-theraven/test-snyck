var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "€"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['Nedelja','Ponedeljek','Torek','Sreda','Četrtek','Petek','Sobota'],
	shortDays: ['Ned','Pon','Tor','Sre','Čet','Pet','Sob'],
	months: ['Januar','Februar','Marec','April','Maj','Junij',
	'Julij','Avgust','September','Oktober','November','December'],
	shortMonths: ['Jan','Feb','Mar','Apr','Maj','Jun',
	'Jul','Avg','Sep','Okt','Nov','Dec']
});

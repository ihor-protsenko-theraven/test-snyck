var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "kr"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['Söndag','Måndag','Tisdag','Onsdag','Torsdag','Fredag','Lördag'],
	shortDays: ['Sön','Mån','Tis','Ons','Tor','Fre','Lör'],
	months: ['Januari','Februari','Mars','April','Maj','Juni',
	     	'Juli','Augusti','September','Oktober','November','December'],
	shortMonths: ['Jan','Feb','Mar','Apr','Maj','Jun',
	          	'Jul','Aug','Sep','Okt','Nov','Dec']
});
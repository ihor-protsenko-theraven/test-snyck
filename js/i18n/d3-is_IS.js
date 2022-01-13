var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "ISK"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['Su','Má','Þr','Mi','Fi','Fö','La'],
	shortDays: ['Su','Má','Þr','Mi','Fi','Fö','La'],
	months: ['Janúar','Febrúar','Mars','Apríl','Maí','Júní',
	'Júlí','Ágúst','September','Október','Nóvember','Desember'],
	shortMonths: ['Jan','Feb','Mar','Apr','Maí','Jún',
	'Júl','Ágú','Sep','Okt','Nóv','Des']
});
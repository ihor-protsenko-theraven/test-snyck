var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "€"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['Zondag', 'Maandag', 'Dinsdag', 'Woensdag', 'Donderdag', 'Vrijdag', 'Zaterdag'],
	shortDays: ['Zon', 'Maa', 'Din', 'Woe', 'Don', 'Vri', 'Zat'],
	months: ['Januari', 'Februari', 'Maart', 'April', 'Mei', 'Juni',
		'Juli', 'Augustus', 'September', 'Oktober', 'November', 'December'],
	shortMonths: ['Jan', 'Feb', 'Mrt', 'Apr', 'Mei', 'Jun',
		'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dec']
});
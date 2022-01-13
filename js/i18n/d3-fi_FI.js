var d3GlobLocale = d3.locale({
	decimal: ",",
	thousands: ".",
	grouping: [3],
	currency: ["", "€"],
	dateTime: "%a %b %e %X %Y",
	date: "%d/%m/%Y",
	time: "%H:%M:%S",
	periods: ["AM", "PM"],
	days: ['sunnuntai', 'maanantai', 'tiistai', 'keskiviikko', 'torstai', 'perjantai', 'lauantai'],
	shortDays: ['Su', 'Ma', 'Ti', 'Ke', 'To', 'Pe', 'La'],
	months: ['tammikuu', 'helmikuu', 'maaliskuu', 'huhtikuu', 'toukokuu', 'kesäkuu', 'heinäkuu', 'elokuu', 'syyskuu', 'lokakuu', 'marraskuu', 'joulukuu'],
	shortMonths: ['tammi', 'helmi', 'maalis', 'huhti', 'touko', 'kesä', 'heinä', 'elo', 'syys', 'loka', 'marras', 'joulu']
});
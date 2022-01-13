/* Dutch (UTF-8) initialisation for the jQuery UI date picker plugin. */
/* Written by Mathias Bynens <http://mathiasbynens.be/> */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional.nl = {
	closeText: 'Sluiten',
	prevText: '←',
	nextText: '→',
	currentText: 'Vandaag',
	monthNames: ['Januari', 'Februari', 'Maart', 'April', 'Mei', 'Juni',
	'Juli', 'Augustus', 'September', 'Oktober', 'November', 'December'],
	monthNamesShort: ['jan', 'feb', 'mrt', 'apr', 'mei', 'jun',
	'jul', 'aug', 'sep', 'okt', 'nov', 'dec'],
	dayNames: ['Zondag', 'Maandag', 'Dinsdag', 'Woensdag', 'Donderdag', 'Vrijdag', 'Zaterdag'],
	dayNamesShort: ['zon', 'maa', 'din', 'woe', 'don', 'vri', 'zat'],
	dayNamesMin: ['zo', 'ma', 'di', 'wo', 'do', 'vr', 'za'],
	weekHeader: 'Wk',
//	dateFormat: 'dd-mm-yy',
	dateFormat: 'dd/mm/yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional.nl);

return datepicker.regional.nl;

}));

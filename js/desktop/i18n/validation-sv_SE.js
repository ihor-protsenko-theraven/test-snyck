/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Swedish
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Vänligen ange ett värde med rätt längd',
    creditcard: 'Var vänlig och ange kreditkortets nummer',
    date: 'Vänligen ange ett giltigt datum',
    dateISO: 'Vänligen ange ett giltigt datum (ÅÅÅÅ-MM-DD)',
    digits: 'Vänligen ange enbart nummer',
    email: 'Vänligen ange ett giltigt e-postadress',
    emailADL: 'Vänligen ange ett giltigt e-postadress',
    equalTo: 'Vänligen ange samma värde igen',
    max: jQuery.validator.format('Ange ett värde mindre än eller lika med {0}'),
    maxlength: jQuery.validator.format('Vänligen ange högst {0} tecken'),
    min: jQuery.validator.format('Ange ett värde som är större än eller lika med {0}'),
    minlength: 'Vänligen ange minst [0} tecken',
    number: 'Vänligen ange ett giltigt nummer',
    passwordADL: 'Använd minst två av de följande: små bokstaver, stura bokstaver, nummer, special mark',
    pattern: 'Ogiltigt format',
    range: jQuery.validator.format('Vänligen ange värde mellan {0} och {1}'),
    rangelength: jQuery.validator.format('Vänligen ange mellan {0} och {1} tecken'),
    remote: 'Detta värde används redan',
    required: 'Detta fält är obligatoriskt',
    url: 'Vänligen ange ett giltigt URL',
});}(jQuery));

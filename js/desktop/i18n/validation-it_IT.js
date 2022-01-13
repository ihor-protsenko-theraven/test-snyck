/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Italian
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Digita un valore con un\'estensione valida',
    creditcard: 'creditcard',
    date: 'Digita una data valida',
    dateISO: 'Digita una data valida (ISO)',
    digits: 'Digita solo cifre',
    email: 'Digita un indirizzo email valido',
    emailADL: 'Digita un indirizzo email valido',
    equalTo: 'Please enter the same value again.',
    max: jQuery.validator.format('Please enter a value less than or equal to {0}.'),
    maxlength: jQuery.validator.format('Digita non più di {0} caratteri'),
    min: jQuery.validator.format('Please enter a value greater than or equal to {0}.'),
    minlength: jQuery.validator.format('Digita almeno {0} caratteri'),
    number: 'Digita un numero valido',
    passwordADL: 'passwordADL',
    pattern: 'Formato non valido',
    range: jQuery.validator.format('Digita un valore fra {0} e {1}'),
    rangelength: jQuery.validator.format('Digita un valore lungo fra {0} e {1} caratteri'),
    remote: 'Questo valore è già in uso',
    required: 'Questo campo è obbligatorio',
    url: 'Digita un URL valido',
});}(jQuery));

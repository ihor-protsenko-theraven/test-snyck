/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Spanish - USA
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Introduzca un valor con una extensión válida.',
    creditcard: 'Por favor, introduzca un número de tarjeta de crédito válida.',
    date: 'Por favor, introduzca una fecha valida.',
    dateISO: 'Introduzca una fecha válida (AAAA-MM-DD)',
    digits: 'Por favor, introduzca sólo dígitos.',
    email: 'Por favor, introduzca una dirección de correo electrónico válida.',
    emailADL: 'Por favor, introduzca una dirección de correo electrónico válida.',
    equalTo: 'Por favor, introduzca el mismo valor de nuevo.',
    max: jQuery.validator.format('Introduzca un valor menor o igual a {0}.'),
    maxlength: jQuery.validator.format('No introduzca más de {0} caracteres.'),
    min: jQuery.validator.format('Introduzca un valor mayor o igual a {0}.'),
    minlength: jQuery.validator.format('Introduzca al menos {0} caracteres.'),
    number: 'Por favor, introduzca un número valido.',
    passwordADL: jQuery.validator.format('Introduzca caracteres de al menos {0} de los siguientes grupos: letras pequeñas, mayúsculas, símbolos especiales, números'),
    pattern: 'Formato inválido.',
    range: jQuery.validator.format('Introduzca un valor entre {0} y {1}.'),
    rangelength: jQuery.validator.format('Introduzca un valor entre {0} y {1} caracteres.'),
    remote: 'Este valor ya está en uso.',
    required: 'Este campo es obligatorio.',
    url: 'Por favor, introduzca una URL válida.',
});}(jQuery));

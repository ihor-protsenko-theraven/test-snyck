/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Slovene
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Prosimo, vnesite veljavno številko.',
    creditcard: 'Prosimo, vnesite veljavno številko kreditne kartice.',
    date: 'Prosimo, vnesite datum veljavnosti.',
    dateISO: 'Prosimo, vnesite datum veljavnosti (LLLL-MM-DD).',
    digits: 'Prosimo, vnesite le številke.',
    email: 'Prosimo, vnesite svoj e-naslov.',
    emailADL: 'Prosimo, vnesite svoj e-naslov.',
    equalTo: 'Prosimo, ponovno vnesite isto vrednost.',
    max: jQuery.validator.format('Prosimo, vnesite vrednost, ki je enaka ali manjša od {0}.'),
    maxlength: jQuery.validator.format('Prosimo, vnesite le {0} znakov.'),
    min: jQuery.validator.format('Prosimo, vnesite vrednost, ki je enaka ali večja od {0}.'),
    minlength: jQuery.validator.format('Prosimo, vnesite vsaj {0} znakov.'),
    number: 'Prosimo, vnesite veljavno številko.',
    passwordADL: 'Prosimo, vnesite znake iz vsaj dveh skupin: male črke, velike črke, posebni simboli, številke.',
    pattern: 'Nepravilna oblika',
    range: jQuery.validator.format('Prosimo, vnesite vrednost med {0} in {1}.'),
    rangelength: jQuery.validator.format('Prosimo, vnesite vrednost dolžine med {0} in {1} znakov.'),
    remote: 'Ta vrednost je že v uporabi.',
    required: 'Polje je obvezno.',
    url: 'Prosimo, vnesite veljaven spletni naslov.',
});}(jQuery));

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : French
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Veuillez entrer une valeur avec une extension valide.',
    creditcard: 'Veuillez entrer un numéro de carte de crédit valide.',
    date: 'Veuillez entrer une date valide.',
    dateISO: 'Veuillez entrer une date valide (AAAA-MM-JJ)',
    digits: 'Merci de n'entrer que des chiffres.',
    email: 'S'il vous plaît, mettez une adresse email valide.',
    emailADL: 'Veuillez entrer une adresse e-mail valide',
    equalTo: 'Entrez à nouveau la même valeur s'il vous plait.',
    max: jQuery.validator.format('Veuillez entrer une valeur inférieure ou égale à {0}.'),
    maxlength: jQuery.validator.format('Veuillez ne pas entrer plus de {0} caractères.'),
    min: jQuery.validator.format('Veuillez entrer une valeur supérieure ou égale à {0}.'),
    minlength: jQuery.validator.format('Veuillez entrer au moins {0} caractères.'),
    number: 'Veuillez entrer un nombre valide',
    passwordADL: 'Veuillez entrer des caractères d'au moins 2 des groupes suivants: lettres minuscules, majuscules, symboles spéciaux, nombres',
    pattern: 'Format invalide',
    range: jQuery.validator.format('Veuillez entrer une valeur entre {0} et {1}'),
    rangelength: jQuery.validator.format('Veuillez entrer une valeur entre {0} et {1} caractères de long'),
    remote: 'Cette valeur est déjà utilisée',
    required: 'Ce champ est requis',
    url: 'Veuillez entrer une URL valide',
});}(jQuery));

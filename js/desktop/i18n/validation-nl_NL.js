/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Dutch - Netherlands
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Vul een waarde met een geldige extensie',
    creditcard: 'Vul een geldig creditcardnummer',
    date: 'Vul alstublieft een geldige datum in',
    dateISO: 'Vul een geldige datum (YYYY-MM-DD)',
    digits: 'Alleen cijfers invoeren',
    email: 'Gelieve een geldig e-mailadres in te geven',
    emailADL: 'Gelieve een geldig e-mailadres in te geven',
    equalTo: 'Vul dezelfde waarde opnieuw',
    max: jQuery.validator.format('Vul een waarde minder dan of gelijk aan {0}'),
    maxlength: jQuery.validator.format('Voer niet meer dan {0} tekens'),
    min: jQuery.validator.format('Vul een waarde groter dan of gelijk aan {0}'),
    minlength: jQuery.validator.format('Voer ten minste {0} tekens'),
    number: 'Voer alstublieft een geldig nummer in',
    passwordADL: 'Vul de karakters van ten minste 2 van de volgende groepen: kleine letters, hoofdletters, speciale tekens, cijfers',
    pattern: 'Ongeldig formaat',
    range: jQuery.validator.format('Vul een waarde tussen {0} en {1}'),
    rangelength: jQuery.validator.format('Vul een waarde tussen {0} en {1} tekens lang zijn'),
    remote: 'Deze waarde is al in gebruik',
    required: 'Dit veld is verplicht',
    url: 'Geef een geldige URL',
});}(jQuery));

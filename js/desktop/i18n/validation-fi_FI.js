/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Finnish
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Ole hyvä ja anna oikean pituinen arvo',
    creditcard: 'Ole hyvä ja anna luottokortin numero.',
    date: 'Ole hyvä ja anna oikea päivämäärä.',
    dateISO: 'Ole hyvä ja anna oikea päivämäärä (VVVV-KK-PP)',
    digits: 'Ole hyvä ja anna ainostaan numeroita.',
    email: 'Ole hyvä ja anna oikea sähköpostiosoite.',
    emailADL: 'Ole hyvä ja anna oikea sähköpostiosoite.',
    equalTo: 'Ole hyvä ja syötä sama arvo uudelleen.',
    max: jQuery.validator.format('Ole hyvä ja syötä arvo, joka on vähemmän tai saman arvoinen kuin {0}.'),
    maxlength: jQuery.validator.format('Ole hyvä äläkä anna enempää kuin {0} merkkiä.'),
    min: jQuery.validator.format('Ole hyvä ja syötä arvo, joka on enemmän tai saman arvoinen kuin {0}.'),
    minlength: jQuery.validator.format('Ole hyvä ja anna vähintään {0} merkkiä.'),
    number: 'Ole hyvä ja anna oikea numero.',
    passwordADL: 'Käytä vähintään kahta seuraavista merkeistä: pienet kirjaimet, suuret kirjaimet, erikoismerkit, numerot.',
    pattern: 'Väärässä muodossa',
    range: jQuery.validator.format('Ole hyvä ja anna arvo välillä {0} ja {1}.'),
    rangelength: jQuery.validator.format('Ole hyvä ja anna arvo, jonka pituus on välillä {0} ja {1}.'),
    remote: 'Tämä arvo on jo käytössä.',
    required: 'Tämä on pakollinen kenttä.',
    url: 'Ole hyvä ja anna oikea URL.',
});}(jQuery));

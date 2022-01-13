/*##################################################
# Product             : Care @ Home
# Version             : 2.2.8
# Client              : Default
# Date                : 13/02/2017
# Translation Versión : Third version of translations for Care @ Home version 2.2.8
# Language            : English - USA
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // jQuery Validate module
    accept: 'Please enter a value with a valid extension.',
    creditcard: 'Please enter a valid credit card number.',
    date: 'Please enter a valid date.',
    dateISO: 'Please enter a valid date (YYYY-MM-DD)',
    digits: 'Please enter only digits.',
    email: 'Please enter a valid email address.',
    emailADL: 'Please enter a valid email address.',
    equalTo: 'Please enter the same value again.',
    max: jQuery.validator.format('Please enter a value less than or equal to {0}.'),
    maxlength: jQuery.validator.format('Please enter no more than {0} characters.'),
    min: jQuery.validator.format('Please enter a value greater than or equal to {0}.'),
    minlength: jQuery.validator.format('Please enter at least {0} characters.'),
    number: 'Please enter a valid number.',
    passwordADL: jQuery.validator.format('Please enter characters from at least {0} of the following groups: small letters, capital letters, special symbols, numbers'),
    mobilePhoneADL: jQuery.validator.format('Requested phone number format: \“+Country Code Phone Number\” No blanks or hyphens allowed'),
    e164ordigits: jQuery.validator.format('Requested phone number format: International format with prefix or just digits.'),
    pattern: 'Invalid format',
    range: jQuery.validator.format('Please enter a value between {0} and {1}'),
    rangelength: jQuery.validator.format('Please enter a value between {0} and {1} characters long'),
    remote: 'This value is already in use',
    required: 'This field is required',
    url: 'Please enter a valid URL',
});}(jQuery));

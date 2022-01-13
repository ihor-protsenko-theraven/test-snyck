/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Russian
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'Пожалуйста, введите значение с законным расширением',
    creditcard: 'Пожалуйста, введите правильный номер кредитной карты.',
    date: 'Пожалуйста, введите корректную дату.',
    dateISO: 'Пожалуйста, введите корректную дату в формате ISO.',
    digits: 'Пожалуйста, вводите только цифры.',
    email: 'Пожалуйста, введите корректный адрес электронной почты.',
    emailADL: 'Пожалуйста, введите корректный адрес электронной почты.',
    equalTo: 'Пожалуйста, введите такое же значение ещё раз.',
    max: jQuery.validator.format('Пожалуйста, введите число, меньшее или равное {0}.'),
    maxlength: jQuery.validator.format('Пожалуйста, введите не больше {0} символов.'),
    min: jQuery.validator.format('Пожалуйста, введите число, большее или равное {0}.")'),
    minlength: jQuery.validator.format('Пожалуйста, введите не меньше {0} символов.'),
    number: 'Пожалуйста, введите число.',
    passwordADL: 'passwordADL',
    pattern: 'Неверный формат',
    range: jQuery.validator.format('Пожалуйста, введите число от {0} до {1}.'),
    rangelength: jQuery.validator.format('Пожалуйста, введите значение длиной от {0} до {1} символов.'),
    remote: 'Пожалуйста, введите правильное значение.',
    required: 'Это поле обязательно для заполнения',
    url: 'Пожалуйста, введите корректный URL.',
});}(jQuery));

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Hebrew
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: 'אנא הכנס ערך עם סיומת חוקית',
    creditcard: 'אנא אכנס מספר כרטיס אשראי בתוקף',
    date: 'אנא הכנס תאריך תקין',
    dateISO: 'אנא הכנס תאריך תקין (ISO)',
    digits: 'אנא הכנס ספרות בלבד',
    email: 'אנא הכנס כתובת דואר אלקטרוני תקינה',
    emailADL: 'אנא הכנס כתובת דואר אלקטרוני תקינה',
    equalTo: 'אנא אכנס את אותו ערך שוב.',
    max: jQuery.validator.format('אנא אכנס מספר קטן או שווה  ל {0}'),
    maxlength: jQuery.validator.format('אנא הכנס לא יותר מ {0} תווים'),
    min: jQuery.validator.format('אנא אכנס מספר גדול או שווה ל {0}")'),
    minlength: jQuery.validator.format('אנא הכנס לפחות {0} תווים'),
    number: 'אנא הכנס מספר תקין',
    passwordADL: 'נא להזין את התווים 2 לפחות אחת מהקבוצות הבאות: אותיות קטנות, אותיות גדולות, סמלים מיוחדים, מספרים',
    pattern: 'פורמט לא תקין',
    range: jQuery.validator.format('אנא הכנס ערך בין {0} ל - {1}'),
    rangelength: jQuery.validator.format('אנא הכנס ערך בעל {0} עד {1} תווים'),
    remote: 'הערך שהוזן נמצא בשימוש',
    required: 'השדה הזה הינו שדה חובה',
    url: 'אנא הכנס כתובת אתר תקינה',
});}(jQuery));

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Chinese z
# Parse               : Jquery Validation
################################################## */
(function($) { $.extend($.validator.messages,{
    // JavaScript Mobile Other Items
    accept: '请输入有效值',
    creditcard: '请输入一个有效的信用卡号',
    date: '请输入一个有效的日期',
    dateISO: '请输入一个有效地日期（年-月-日)',
    digits: '请仅输入数字',
    email: '请输入一个有效的邮箱地址',
    emailADL: '请输入一个有效的邮箱地址',
    equalTo: '请重新输入相同内容',
    max: jQuery.validator.format('请输入一个小于或等于{0}的数值'),
    maxlength: '请不要输入字符',
    min: jQuery.validator.format('请输入一个大于或等于{0}的数值'),
    minlength: '请输入至少一个字符',
    number: '请输入一个有效数值',
    passwordADL: '请从以下选择至少两类输入字符：小写字母、大写字母、特殊字符、数字',
    pattern: '无效的格式',
    range: jQuery.validator.format('请输入一个介于{0}和{1}之间的数值'),
    rangelength: jQuery.validator.format('请输入一个长度介于{0}和{1}之间的数值'),
    remote: '此项数值已被使用',
    required: '此字段是必填的',
    url: '请输入一个有效的网址',
});}(jQuery));

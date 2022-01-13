/**
 * I18n object to manage javascript locales
 * 
 * @author Samuel Santiago <samuelsantia@gmail.com>
 */

(function (ns){
	"use strict";
	
	ns.translations = {};
	
	/** Constructor */
	function I18n() {
		this.setLocaleFromRoot();
	};
	
	var proto = I18n.prototype;
	
	/** defaults properties */
	proto.defaults = { 
		locale: 'en_US' 
	};
	
	/**
	 * set locale from lang attribute of root element
	 * 
	 * @param {DOMElement} root, element to set locale. Default: html element
	 */
	proto.setLocaleFromRoot = function (root) {
		var root = (typeof root !== 'undefined' 
					&& root !== null) ? root 
									  : document.getElementsByTagName('html')[0];
		
		this.setLocale(root.getAttribute('lang') || this.defaults.locale);
	};
	
	/**
	 * set locale
	 * 
	 * @param {String} locale, identification string for locale
	 */
	proto.setLocale = function (locale) {
		this.locale = locale;
	};
	
	/**
	 * get locale
	 * 
	 * @return {String} locale, current locale identification
	 */
	proto.getLocale = function () {
		return this.locale;
	};
	
	/**
	 * find a translation in scope of locale
	 * 
	 * @param {String|Array} scope, to find the translation in locale
	 * @param {String} locale, for search
	 * @return {String} translation or null if not found
	 * @throws will threow error when the translations locale object is not set
	 */
	proto.findTranslation = function (scope, locale) {
		var locale     = locale || this.locale,
		translations = ns.translations[locale],
		splitScope = (scope instanceof Array) ? scope : scope.split('.');
	
		if ( !translations ) {
			console.error("No translations for '" + locale + "' locale please define it.");
			
			return (locale != this.defaults.locale) ?
				this.findTranslation(scope, this.defaults.locale) 
				: null;
		}
		
		while ( splitScope.length ) {
			translations = translations[splitScope.shift()];
			
			if (translations === undefined || translations === null) {
				return null;
			}
		}
		
		return translations;
	};
	
	/**
	 * translation missing literal
	 * 
	 * @return {String} literal when not found a translation
	 */
	proto.translationMissing = function (scope, locale) {
		if ( typeof scope != "string" ) scope = scope.join('.');
		return "Translation missing: " + locale + "." + scope;
	};
	
	/**
	 * get translation for app
	 * if not setted and passed in options defaultValue returns that literal
	 * finally return translation missing if not translation found or not defaultValue
	 * 
	 * @param {String|Array} scope, to find the translation in locale
	 * @param {Object} options, you can pass the locale and defaultValue
	 * @return {String} the translation literal
	 */
	proto.translate = function (scope, options) {
		var options     = options || {}, 
			locale      = options.locale || this.locale,
			translation = this.findTranslation(scope, locale) 
							|| options.defaultValue 
							|| this.translationMissing(scope, locale);
		
		return translation;
	};
	
	// TODO: add support to translate dates, number formats, etc...
	
	// Alias methods
	proto.t = proto.translate;
	
	// Initialize a new I18n instance in scope
	ns.I18n = new I18n();
})(this);
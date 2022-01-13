(function ($) {
	
	function isBlank(value) {
		return typeof value === 'undefined' || value === null || value === '';
	};
	
	function isEmpty(value) {
		if ( Array.isArray(value) ) return !value.length;
		else if ( typeof value === 'object' ) return Object.keys(value).length === 0 && JSON.stringify(value) === JSON.stringify({});
		else return isBlank(value);
	};
	
	/**
	 * compare if two objects are equals
	 * 
	 * @param {Object} object - original object to compare
	 * @param {Object} compare - object to compare
	 * @param {Obejct} settings - settings to compare objects
	 * @param {Boolean} [settings.deep=false] - compare subObjects, be careful about performance
	 * @param {Boolean} [settings.sort=true] - sorts arrays in object
	 * @returns {Boolean} are two objects equals
	 */
	function compareObjects(object, compare, settings) {
		var settings = settings || {};
		var sort = !isBlank(settings.sort) ? settings.sort : true;
		var deep = !isBlank(settings.deep) ? settings.deep : false;
		var objectKeys = Object.keys(object).filter(function (key) { return !isBlank(object[key]); });
		var compareKeys = Object.keys(compare).filter(function (key) { return !isBlank(compare[key]); });
		
		// if the arrays of keys is distinct the objects is different
		if ( !compareArrays(objectKeys, compareKeys) ) { 
			return false; 
		}
		
		// iterate through keys to compare
		var i = 0;
		var len = objectKeys.length;
		for ( i; i < len; i++ ) {
			var key = objectKeys[i];
			var value = object[key];
			var compareValue = compare[key];
			
			if ( Array.isArray(value) ) {
				// if the values is arrays compare two arrays
				if (!compareArrays(value, compareValue, { sort: sort, deep: deep, deepSettings: settings })) {
					return false;
				}
			} else if ( $.isPlainObject(value) ) {
				// if the values is plain object compare two objects if deep is setted to true
				if ( deep && !compareObjects(value, compareValue, settings) ) {
					return false;
				}
			} else if ( value instanceof Date) {
				if ( value.getTime() !== compareValue.getTime() ) return false;
			} else {
				// else compare values
				if ( value !== compareValue && !isBlank(value) && !isBlank(compareValue) ) {
					return false;
				}
			}
		}
		
		return true;
	};
	
	/**
	 * compare if two arrays are equals
	 * 
	 * @param {Array} array, original array to compare
	 * @param {Array} compare, array to compare
	 * @param {Object} settings - settings to compare arrays
	 * @param {Boolean} [settings.sort=true] - Sorts arrays
	 * @param {Boolean} [settings.deep=false] - If array has objects compare objects, be careful about performance
	 * @param {Object} [settings.deepSettings={}] - Settings to objects compare
	 * @returns {Boolean} are two arrays equals
	 */
	function compareArrays(array, compare, settings) {
		var settings = settings || {};
		var sort = !isBlank(settings.sort) ? settings.sort : true;
		var deep = !isBlank(settings.deep) ? settings.deep : false;
		var deepSettings = settings.deepSettings || {};

		// if length is distinct are not equal
		if ( array.length !== compare.length ) return false;
		
		// if sort sorts both arrays
		if ( sort ) {
			var sortFn = typeof sort === 'function' ? sort : undefined;
			array.sort(sortFn);
			compare.sort(sortFn);
		}
		
		var i = 0;
		var len = array.length;
		for ( i; i < len; i++ ) {
			if ( Array.isArray(array[i] ) ){
				// if is array compare two arrays
				if ( !compareArrays(array[i], compare[i], settings) ) return false;
			} else if ( deep && $.isPlainObject(array[i]) ) {
				// if is deep and is plain object and not equal objects
				if ( !compareObjects(array[i], compare[i], deepSettings) ) return false;
			} else if ( array[i] !== compare[i] ) {
				// else compare that the values are equals
				return false;
			}
		}
		
		return true;
	};
	
	$.utils = $.extend($.utils, {
		compareObjects: compareObjects,
		compareArrays: compareArrays,
		isBlank: isBlank,
		isEmpty: isEmpty
	});
})(jQuery);
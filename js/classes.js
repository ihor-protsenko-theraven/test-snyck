// FUNCTIONS
var concatObjects = function(source, target) {
	if (!target) {
		return source;
	} else {
		for(var key in source) {
	    if (source.hasOwnProperty(key)) {
	        target[key] = source[key];
	    }
	  }
	  return target;
	}
};

// CLASSES
/**
 * This Class is used to make the remote calls asynchronously
 * Params:
 * - url: the url to call
 * - options: associative array with the values:
 *   > params: the params which are always sent to the server. i.e.: {a: 1, b: 2}
 *   > responseType: response type (To avoid the cross-domain HTTP requests te format used is jsonp), by default the data type is jsonp
 *   > done: function to execute when the response is correct
 *   > fail: function to execute when the request has failed
 * 	 > before: function to execute before request
 *   > always: function to execute always after the request is completed
 */
function RemoteCaller(url, options) {
	if (!url) throw "Url cannot be null.";
	if (!options['done']) throw "doneCallback must be defined.";
	if (!options['fail']) throw "failCallback must be defined.";

	this.url = url;
	this.doneCallback = options['done'];
	this.failCallback = options['fail'];
	this.alwaysCallback = options['always'];
	this.defaultParams = options['params'];
	this.responseType = (options['responseType']) ? options['responseType'] : 'jsonp';
	this.currentParams;

	params = {
		dataType: this.responseType,
		url: this.url
	};	
	
	$.ajaxSetup(params);
}

RemoteCaller.prototype.call = function(type, params) {
	if (params) this.currentParams = params;

    if(typeof options['before'] != 'undefined') {
		params.beforeSend = options['before'];
        delete options['before']
	}
    
	if ((this.defaultParams && this.currentParams) || this.defaultParams)
		params = $.param(concatObjects(this.defaultParams, this.currentParams));

    
    
	if (this.alwaysCallback) {
		$.ajax({
			type: type,
			data: params
		})
		.done(this.data = this.doneCallback)
		.fail(this.failCallback)
		.always(this.alwaysCallback);
	} else {
		$.ajax({
			type: type,
			data: params
		})
		.done(this.data = this.doneCallback)
		.fail(this.failCallback);
	}
};

RemoteCaller.prototype.get = function(params) {
	this.call('GET', params);
};

RemoteCaller.prototype.post = function(params) {
	this.call('POST', params);
};

/**
 * Object class extended
 */ 
Object.size = function(obj) {
	var size = 0, key;
	for (key in obj) {
		if (obj.hasOwnProperty(key)) size++;
	}
	return size;
};

Object.push = function(objReceiver, obj) {
	var keys = Object.keys(obj);
	
	for (i = 0; i < keys.length; i++) {
		objReceiver[keys[i]] = obj[keys[i]];
	}
};

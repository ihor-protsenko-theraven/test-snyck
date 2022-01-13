/**
 * Sound player
 * creates html audio element for play sound effects.
 * 
 * @author Samuel Santiago <samuelsantia@gmail.com>
 */
(function (ns) {
	"use strict";
	
	/**
	 * Return the original object with missing properties from other objects.
	 * internal use to merge objects properties.
	 * 
	 * @param {Object} obj - original object.
	 * @param {...Object} sources - objects to merge in the original object.
	 * @return {Object} original object merged with extra objects
	 */
	var __extend = function (obj, sources) {
		var __hasProp = Object.hasOwnProperty, 
			sources   = Array.prototype.slice.call(arguments, 1),
			i, len;
		
		for ( i = 0, len = sources.length; i < len; i++ ) {
			var source = sources[i], 
				prop;
			
			for ( prop in source ) {
				if ( !__hasProp.call(obj, prop) && __hasProp.call(source, prop) ) {
					obj[prop] = source[prop];
				}
			}
		}
		
		return obj;
	};
	
	/**
	 * Creates a new SoundPlayer.
	 * 
	 * @class
	 * @param {String} source - path to source to load.
	 * @param {Object} options - options for the player.
	 * @see defaults for defaults options.
	 */
	var SoundPlayer = function (source, options) {
		var options = options || {};
		
		this.source = source;
		this.opts   = __extend(options, this.defaults);
		
		this.init();
	}; 
	
	/** defaults properties */
	SoundPlayer.prototype.defaults = {
		formats: ['ogg', 'mp3', 'wav'], // audio formats to test is supported by order of priority
		poolLength: 5, // max number of sounds for pool
		volume: 1 // audio volume
	};
	
	/**
	 * Initializes new SoundPlayer.
	 * and load initial source if is defined.
	 * 
	 * @constructs SoundPlayer
	 * @throws throw an error if options audio formats are not supported by the browser.
	 */
	SoundPlayer.prototype.init = function () {
		this.pool  = new Array(this.opts.poolLength);
		this.type  = this.canPlayType();
		this.index = 0;

		if ( !this.type ) {
			throw "Audio formats { " + this.opts.formats.join(', ') + " } are not supported by your browser";
		}
		
		for ( var i = 0; i < this.opts.poolLength; i++ ) {
			var audio = document.createElement('audio');
			
			audio.volume  = this.opts.volume;
			this.pool[i] = audio;
		}
		
		if ( this.source ) {
			this.loadSource();
		}
	};
	
	/**
	 * Load source in audio element
	 * if source is not defined load the current source
	 * 
	 *  @param {String} [source=this.source] - path to source to load.
	 */
	SoundPlayer.prototype.loadSource = function (source) {
		var source = source || this.source;
		
		this.pool.forEach(loadAudioSource, this);
		
		function loadAudioSource (audio) {
			audio.setAttribute('src', source + "." + this.type);
			audio.load();
		};
	};
	
	/**
	 * Sets audio volume
	 * 
	 * @param {Number} volume - volume for audio track
	 */
	SoundPlayer.prototype.setVolume = function (volume) {
		this.opts.volume = volume;
		this.pool.forEach(changeAudioVolume, this);
		
		function changeAudioVolume (audio) {
			audio.volume = this.volume;
		};
	};
	
	/**
	 * Returns audio format type that browser can play.
	 * if can not play any of options defined types returns false.
	 * 
	 * @returns {String|Boolean} type can be played by the player or false.
	 */
	SoundPlayer.prototype.canPlayType = function () {
		var i, len;
		
		for ( i = 0, len = this.opts.formats.length; i < len; i++ ) {
			var type = this.opts.formats[i],
				canPlay = document.createElement('audio').canPlayType("audio/" + type);
			
			if ( /^(maybe|probably)$/.test(canPlay) ) {
				return type;
			}
		}
		
		return false;
	};

	/** Plays the loaded source */
	SoundPlayer.prototype.play = function () {
		var audio = this.pool[this.index++ % this.opts.poolLength];

		if ( audio.currentTime == 0 || audio.ended ) {
			// Safari and Chrome browsers don't resets audio and need to call audio load to play sound again
			if ( audio.ended && (window.chrome || window.safari) ) audio.load();
			
			audio.play();
		}
	};
	
	/**
	 * Returns loaded track duration
	 * 
	 * @return {Number} duration in seconds of current track
	 */
	SoundPlayer.prototype.getDuration = function () {
		return this.pool[0].duration;
	};
	
	ns.SoundPlayer = SoundPlayer;
})(this);
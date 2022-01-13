import DOMPurify from 'dompurify';
$(function ($) {
	var uuid = $.app.uid,
		storageKey = "u"+uuid+"settings",
		$headerNotifications = $('.main_header .notifications'),
		$monitoring          =  DOMPurify.sanitize($('#monitoring')),
		$history             = $('#history'),
		$refresh             = $('#refresh_button').fadeOut(0),
		$expiredSessionModal = $('#expired-session-modal'),
		path = $.app.rootPath + '/admin/alertCount',
		intervalRequestDefaultTiming = 60000,
		EXPIRE_SESSION_TIME_SAVE = 30000,
		reconnecting = false,
		tmo,
		userSettings = getUserSettings(),
		lastPatients = userSettings.lastPatients,
		$alert_sound_check = $('#alert_sound_check'),
		$alert_sound_again_check = $('#alert_sound_again_check'),
		alertSound   = new SoundPlayer($.app.rootPath + '/audio/alarm-clock_3'),
		remindSound  = new SoundPlayer($.app.rootPath + '/audio/message_01'),
		remindSoundTmo,
		remindSoundTiming = 30000,
		reminding    = false,
		isMonitoring = $monitoring.length > 0,
		isHistory    = $history.length > 0,
		unloading    = false,
		recentInteraction = false;
	
	// @deprecated
	// remove this conditional when all clients are updated

	if (window.sessionStorage.getItem("alertPatients")) window.sessionStorage.removeItem("alertPatients");
	if (typeof window.sessionStorage.getItem("alertsMore") !== 'undefined') window.sessionStorage.removeItem("alertsMore");
	
	$.ajaxSetup({ cache: false });

	if (userSettings.alert_sound) {
		$alert_sound_check.attr('checked', true);
	}
	if (userSettings.alert_sound_again) {
		$alert_sound_again_check.attr('checked', true);
	}
	
	function changeSoundCheckHandler(event) {
		var $check = $(this);
		
		userSettings.alert_sound = $alert_sound_check.is(':checked');
		userSettings.alert_sound_again = $alert_sound_again_check.is(':checked');
		storeUserSettings();
		
		if ( !$check.is(':checked') ) {
			clearTimeout(remindSoundTmo);
			reminding = false
		} else {
			if ( userSettings.alert_sound && userSettings.alert_sound_again && userSettings.alertStatusNew ) {
				playRemind();
			}
		}
	};
	
	function changeAlertSoundCheckHandler(event) {
		var $check = $(this);
		
		if ($check.is(':checked')) {
			$alert_sound_again_check.removeAttr('disabled');
		} else {
			$alert_sound_again_check.attr('disabled', true);
		}
	};
	
	function playRemind() {
		var duration  = remindSound.getDuration(),
			tmoTiming = isNaN(duration) 
				? remindSoundTiming 
				: remindSoundTiming + Math.round(duration * 1000);
		
		clearTimeout(remindSoundTmo);
		reminding = true;
		
		remindSoundTmo = setTimeout(function () {
			remindSound.play();
			$headerNotifications.addClass('pulse');
			playRemind();
		}, tmoTiming);
	};
	
	function storeUserSettings () {
		window.sessionStorage.setItem(storageKey, JSON.stringify(userSettings));
	};
	
	function getUserSettings () {
		if (window.sessionStorage.getItem(storageKey)) {
			return JSON.parse(window.sessionStorage.getItem(storageKey)); 
		}
		
		// @deprecated
		// sets defaults values when all clients are updated.
		var lastPatients = (window.sessionStorage.getItem("alertPatients")) 
							? JSON.parse(window.sessionStorage.getItem("alertPatients")) : [],
			alertsMore   = (window.sessionStorage.getItem("alertMore")) 
							? !!window.sessionStorage.getItem('alertsMore') : false
		return {
			lastPatients: lastPatients, 
			alertsMore: alertsMore,
			alertSound: true,
			alertSound_again: true 
		};
	};
	
	$alert_sound_check.on('change', changeAlertSoundCheckHandler);
	$alert_sound_check.on('change', changeSoundCheckHandler);
	$alert_sound_again_check.on('change', changeSoundCheckHandler);
	
	if ( !userSettings.alert_sound ) {
		$alert_sound_again_check.attr('disabled', true);
	}
	
	/*
	 * If the user clicks there was an interaction, and session expiration will be reset in the server
	 */
	$(document).bind('click', function() {
		recentInteraction = true;
	});
	
	function sendRequest(loop) {
		var url = path;
		if (recentInteraction) {
			url += '?activity';
			recentInteraction = false;
		}
		
		$.getJSON(url, function (data, textStatus, jQXhr) {
			var patients    = data.response || [],
				i           = 0,
				length      = patients.length,
				alertsCount = 0,
				hasChange   = false,
				hasMore     = false,
				hasNewStatus = false,
				tmoTime     = data.retryTime || intervalRequestDefaultTiming,
				expired     = data.controlFlag;
			
			if ( expired ) {
				var intVal = null,
					time   = EXPIRE_SESSION_TIME_SAVE / 1000,
					$countDown = $expiredSessionModal.find('#expired_session_countdown');
				
				$countDown.text(time);
				$expiredSessionModal.addClass('active');
				clearInterval(intVal);
				
				function expireIntVal(){
					time -= 1;
					if ( time <= 0 ) {
						clearInterval(intVal);
						window.location.href = $.app.rootPath + '/sessionTimeout';
					}
					$countDown.text(time);
				};
				
				intVal = setInterval(expireIntVal, 1000);
				
				$expiredSessionModal.one('modal:hide', function (event) {
					clearInterval(intVal);
					
					if (loop) {
						clearTimeout(tmo);
						tmo = setTimeout(function () {
							sendRequest(loop);
						}, tmoTime);
					}
				});
				
				return false;
			}
			
			//console.log(data);
			// IF CACHE ARRAY LENGTH IS DISTINCT THAT PATIENTS LENGTH THE ALARMS HAS CHANGES
			if (lastPatients.length != patients.length) {
				hasChange = true;
				if (lastPatients.length < patients.length) {
					hasMore = true;
				}
			} 
			
			for (i; i < length; i++) {
				var patient = patients[i];
				// SUM THE ALERTS COUNTS
				alertsCount += patient.numAlerts;
				if (patient.minAlertState.toLowerCase() === 'new') {
					hasNewStatus = true;
					if (userSettings.alertStatusNew != true) {
						hasChange = true;
					}
				}
				
				// IF DONT HAS CHANGE YET CHECK WITH THE JS CACHE ARRAY FOR CHECK IT
				//if (hasChange != true) {
					var founds = $.grep(lastPatients, function (item) { return item.userId == patient.userId; });
					if (founds.length == 0) {
						hasChange = true;
						hasMore   = true;
					} else if(founds.length == 1) {
						var lastPatient = founds[0];
						
						// if patient last alert id its greather than cache patient last alert id has change 
						if (patient.iLastAlrtId > lastPatient.iLastAlrtId) {
							hasChange = true;
							hasMore   = true;
						}
					} else {
						for (var j = 0; j < lastPatients.length; j++) {
							var lastPatient = lastPatients[j];
						
							if (patient.iLastAlrtId > lastPatient.iLastAlrtId) {
								hasChange = true;
								hasMore   = true;
								break;
							}
						}
					}
				}
			//}
			
			if (userSettings.alertStatusNew != hasNewStatus) {
				userSettings.alertStatusNew = hasNewStatus;
				hasChange = true;
			}
			
			// UPDATE ALERTES COUNT
			if (alertsCount > 0) {
				$headerNotifications.addClass('active').find('a').text(alertsCount);
			} else {
				$headerNotifications.removeClass('active');
			}
			
			if ( userSettings.alertsMore && isMonitoring ) {
				userSettings.alertsMore = false;
				storeUserSettings();
			}
			
			// Play sound
			if (hasMore && $alert_sound_check.is(':checked') ) {
				clearTimeout(remindSoundTmo);
				alertSound.play(true);
				
				if ( $alert_sound_again_check.is(':checked') ) {
					playRemind();
				}
			} else if (!reminding && userSettings.alertStatusNew && 
						$alert_sound_check.is(':checked') && 
						$alert_sound_again_check.is(':checked')) {
				playRemind();
			}
			
			if ( !userSettings.alertStatusNew ) {
				clearTimeout(remindSoundTmo);
				reminding = false;
			}
			
			// Pulse state
			if (hasMore || userSettings.alertsMore) {
				$headerNotifications.addClass('pulse');
				userSettings.alertsMore = true;
			} else if (!userSettings.alertsMore) {
				//clearTimeout(remindSoundTmo);
				$headerNotifications.removeClass('pulse');
			}
			
			// Refresh history page
			if (hasChange && isHistory) {
				var rid = parseInt($refresh.data('resident-id')),				
					newAlerts = 0,
					matched   = false;
				
				for (var i = 0; i < patients.length; i++) {
					var patient = patients[i];		
					if (patient.userId == rid) {		
						
						for (var j = 0; j < lastPatients.length; j++) {
							var lPatient = lastPatients[j];
						
							if (lPatient.userId == rid) {	
								newAlerts = patient.numAlerts - lPatient.numAlerts;									
								matched   = true; 
								break;
							}
						}
						if (matched == false) {
							newAlerts = patient.numAlerts;
							matched   = true;
						}
						break;
					}
				}
				console.log(matched, newAlerts);
				if (matched && newAlerts > 0) {
					$refresh.find('.count').text(newAlerts);
					$refresh.fadeIn();
				}
			}
			
			// IF HAS CHANGE AND IS IN MONITOR SCREEN UPDATE TABLE
			if (hasChange && isMonitoring) {
				$.get(window.location.href, function (data) {
					$monitoring.html( $(data).filter('.main_content').find('#monitoring').html() ).triggerHandler('alerts:change_table');
				}, 'html').done(function () {
					if (loop) {
						clearTimeout(tmo);
						tmo = setTimeout(function () {
							sendRequest(loop);
						}, tmoTime);
					}
				});
			} else {
				if (loop) {
					clearTimeout(tmo);
					tmo = setTimeout(function () {
						sendRequest(loop);
					}, tmoTime);
				}
			}
			
			if (hasChange) {
				userSettings.lastPatients = patients;
				storeUserSettings();
			}
			
			lastPatients = patients;
		}).fail(function (xhr, textStatus, errorThrown) {
			console.log(xhr.readyState);
			if (unloading) return false;
			
			if ( xhr.status === 0 ) { // the server is down and don't response nothing 
				$('#system-error-modal').addClass('active');
			} else if (xhr.status === 200) { // if loop enabled try again in timeout
				var n = document.open("text/html", "replace");
				
				n.write(xhr.responseText);
				n.close();
			}
		}).always(function () {
			if ( reconnecting ) {
				reconnecting = false;
				$.app.loader.hide();
			}
		});
	}
	
	$(window).on('beforeunload', function (event){
		unloading = true;
	});
	
	$(document).on('click', '#system-error-modal button', function (event) {
		$('#system-error-modal').removeClass('active');
		reconnecting = true;
		$.app.loader.show();
		setTimeout(function () {
			sendRequest(true);
		}, 2000);
	});
	
	sendRequest(true);
});
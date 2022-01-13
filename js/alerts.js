function preventDefault(e) {
  e = e || window.event;
  if (e.preventDefault)
      e.preventDefault();
  e.returnValue = false;  
}

function preventDefaultForScrollKeys(e) {
    if (keys[e.keyCode]) {
        preventDefault(e);
        return false;
    }
}

function disableScroll() {
  if (window.addEventListener) // older FF
      window.addEventListener('DOMMouseScroll', preventDefault, false);
  window.onwheel = preventDefault; // modern standard
  window.onmousewheel = document.onmousewheel = preventDefault; // older browsers, IE
  window.ontouchmove  = preventDefault; // mobile
}

function enableScroll() {
    if (window.removeEventListener)
        window.removeEventListener('DOMMouseScroll', preventDefault, false);
    window.onmousewheel = document.onmousewheel = null; 
    window.onwheel = null; 
    window.ontouchmove = null;  
    document.onkeydown = null;  
}

		
$(function ($) {
	$.app = $.app || {};
	
	var I18n       = window.I18n,
		rootPath   = $.app.rootPath || '',
		path       = rootPath + '/home/status',
		allPath    = path + '/all';
	
	var Alerts;
	
	Alerts = function (autoInitialize) {
		this.running = autoInitialize || false;
		
		if (autoInitialize) {
			this.init();
		}
	};
	
	/** CONSTANTS */
	Alerts.prototype = {
		PATH: path,
		REQUEST_TIMING: 10000,
		EXPIRE_SESSION_TIME_SAVE: 30000,
		EXPIRATION_TIME: 30000,
		LOOP: true,
		STATUSES: {
			WAITING: 0,
			OK: 1,
			KO: 2,
			DISCONNECTED: 3
		}
	};
	
	Alerts.prototype.tpls = {
		getNotificationIconTpl: function (icon) {
			switch(icon.toUpperCase()) {
			case 'PHOTO':
				icon = 'VIDEO';
				break;
			case 'EQUIPMENT':
				icon = 'TECHNICAL';
				break;
			case 'USER':
			case 'bGetFall':
				icon = 'FALL';
				break;
			case 'bGetSmoke':
				icon = 'FIRE';
				break;
			case 'bGetPanic':
				icon = 'SOS';
				break;
			case 'bGetWtLkge':
				icon = 'FLOOD';
				break;
			case 'bGetNtAtHm':
				icon = 'FRONT_DOOR';
				break;
			case 'bGetActvt':
				icon = 'ACTIVITY';
				break;
			case 'bGetDoor':
				icon = 'DOOR';
				break;
			default:
				icon = icon.toUpperCase();
			}
			return '<svg class="icon icon-notification">' +
						'<use xlink:href="' + rootPath + '/stylesheets/svg/sprite-icons.svg#' + icon + '" />' +
					'</svg>';
		},
		getServiceTypeResidentIcon: function (serviceType) {
			return '<img class="icon icon-service-type" src="' + rootPath +'/stylesheets/svg/service-types-icons/'+ (serviceType).toLowerCase() +'.svg">';
		},
		getPopUpTpl: function (patient, alert, statusType) {
			var startDate  = alert.formattedStartDateTime,
				endDate    = alert.formattedEndDateTime,
				eventUrl,
				html;

			//MAP
			var isMap = false;
			var isUnknow = true;
			console.log('EPA ALERT');
			if(alert.deviceType === 9 && alert.eventCode === 2 || alert.eventCode === 82){
				if (alert.location !== null || alert.location !== undefined){
					isMap = true;
				}else{
					if(alert.mobileId !== null && alert.mobileId !== undefined){
						isUnknow = false;
					}
				}
			}
			
			
			var timeFormat = $.app.timeFormatter;
			
			disableScroll();
			document.body.style.overflow = "hidden";
			html = '<header>' + patient + '</header>'
					+ '<div class="popup-content" data-dismissible="false" data-overlay-theme="a">';
					if (alert.type == 'VIDEO') {
						html += '<h1>' + I18n.t('alerts.popup.titles.photos') + ' <span class="icon icon-photo"></span></h1>';
//					} else if (statusType == 'TECH_ALARM') {
//						html += '<h1>TECHNICAL ALERT</h1>';
					} else {
						html += '<h1 class="in-alert">' + I18n.t('alerts.popup.titles.alarm') + '  <span class="icon icon-red-bell-anime"></span></h1>';
					}
						html += '<div class="alarm-wrapper" data-dismissible="false" data-overlay-theme="a">'
						+ '<h2>' + alert.title + '</h2>'
						+ this.getNotificationIconTpl( alert.type.toUpperCase() )
						+ '<p>'
							+ '<span class="label">' + I18n.t('alerts.popup.status') + '</span>'
							+ '<span class="right-side">'
								+ '<strong class="status ' + alert.currentState.toLowerCase() + '">' + I18n.t('alerts.popup.statuses.' + alert.currentState) + '</strong>'
							+ '</span>'
						+ '</p>';
			
			if (startDate) {
				
				html += '<p id="startDate" class="date">'
							+ '<span class="label">' + I18n.t('alerts.popup.detected') + '</span>'
							+ '<span class="right-side">'
							+ '<strong>' + startDate + '</strong> '
							+ '</span>'
						+ '</p>';
			}
			
			if (endDate) {
				
				var endDateArray = endDate.split(" ");
				
				html += '<p id="endDate" class="endDate">'
							+ '<span class="label">' + I18n.t('alerts.popup.updated') + '</span>'
							+ '<span class="right-side">'
							+ '<strong>' + endDate +'</strong> - '
							+ '</span>'
						+ '</p>';
			}
			
			
			if (isMap === true){
				html += '<div id="map" style="height: 150px"></div>';
			}else{
				if(isUnknow === false	){
					html += '<svg class="icon icon-notification"><use xlink:href="'+$.app.rootPath+'/stylesheets/svg/location-icons/at_home.svg"></use></svg>'; 
				}else{
					html += '<svg class="icon icon-notification"><use xlink:href="'+$.app.rootPath+'/stylesheets/svg/location-icons/unknow_location.svg"></use></svg>'; 
				}
			}
			
			
			eventUrl = (alert.type == 'VIDEO') 
					? $.app.rootPath + '/alerts/' + alert.id + '/photo_detail' 
					: $.app.rootPath + '/alerts/' + alert.id;
			
			html += '</div>'
					+ '<div class="popup-actions"><center><table><tr>'
						+ '<td><button data-rel="close" id="handle_it_later" onClick="enableScroll()" class="btn-inline btn-primary">' + I18n.t('alerts.popup.handle_it_later') + '</button></td>'
						+ '<td><form method="get" action="' + eventUrl + '"><button type="submit" onClick="enableScroll()" id="view_event" class="btn-inline btn-primary">'+ I18n.t('alerts.popup.view_event')+'</button></form></td>'
						//+ '<a href="' + eventUrl + '" class="btn-inline btn-primary">' + I18n.t('alerts.popup.view_event') + '</a>'
					+ '</tr></table></center></div>'
				+ '</div>';
			
				return html;
		},
		getPatientListItemTpl: function ( patient, showServiceType ) {
			var html  = '<li class="BBResident bbresidentt" id="patient_' + patient.userId + '">';			
					html += '<a href="'+ rootPath +'/home/setPatient?patientId=' + patient.userId + '" class="item-wrap" data-ajax="false">';
					if (patient.systemStatus.numAlert > 0) {
						html += '<span class="btn bullet icon-right">' + patient.systemStatus.numAlert + '</span>';
					}
					if (patient.systemStatus.statusType == 'ALARM' || patient.systemStatus.statusType == 'ALARM_IN_PROGRESS') {
						html += '<span class="icon icon-system-status icon-ko icon-right"></span>';
					} else if (patient.systemStatus.statusType == 'TECH_ALARM') {
						html += '<span class="icon icon-system-status icon-disconnected icon-right"></span>';
					} else if (patient.systemStatus.statusType == 'USER_PHOTO_REQUEST') {
						html += '<span class="icon icon-system-status icon-photos icon-right"></span>';
					}
					if (patient.gender) { 
						html += '<span class="icon icon-user icon-user-' + patient.gender + '"></span>';
					} else {
						html += '<span class="icon icon-user icon-user-male"></span>';
					}
					if ( showServiceType ) {
						html += this.getServiceTypeResidentIcon(patient.serviceType);
					}
						html += '<span class="item-text">' + patient.firstName + '</span>';
					html += '</a>';
				html += '</li>';
			
			return html;
		}
	};
	
	Alerts.prototype.init = function () {
		this.handledAlerts = [];
		this.status        = this.STATUSES.WAITING;
		this.popUpToReopen = false;	// this variable will be true when alarm popup is closed to be replaced by expiration, so we know it should be reopen after the user closes the expiration one 
		this.$mainHeader   = $('#main-header');
		this.$statusIcon   = $('#main-header .icon-system-status, #config-menu-panel .icon-system-status');
		this.$issuesCount  = $('#issue_count');
		this.$expiredPopUp = $('#expired-session-pop').popup({ history: false });
		
		this.$patientListView = $('#patient-slider');
		this.$patientBtn      = this.$mainHeader.find('[href="#patient-slider"]');
		
		this.bindEvents();
		
		this.recentInteraction = false;
		this.jsInteractionTimer = null;
		
		if (this.running) {
			this.requestResponse();
		}
	};
	
	Alerts.prototype.bindEvents = function () {
		var self = this;
		
		this.$statusIcon.parent().on('click', function (event) {
			if (self.status == self.STATUSES.OK) {
				return false;
			}
		});
		
		this.$patientListView.on('slider:before_show', function (event, cb) {
			$('.ui-loader').loader('show');
			$.getJSON(allPath, function (data, textStatus, jQXhr) {
				var residents = data.residents,
					i = 0,
					l = residents.length,
					showServiceType = data.vendorServiceTypes.length > 1;
				
				//$.mobile.loader('hide');
				self.$patientListView.find('ul').html('');
				
				for (i; i < l; i++) {
					var resident = residents[i];
					
					self.$patientListView.find('ul').append(self.tpls.getPatientListItemTpl(resident, showServiceType));
					
					self.$patientListView.find('ul a').on('click', function(){
						self.stop();
					});
				}
				cb();
			}).always(function () { $('.ui-loader').loader('hide'); });
			
			return false;
		});
		
		var hidden = "hidden";

	    // Standards:
	    if (hidden in document)
	        document.addEventListener("visibilitychange", onvisiblechange );
	    else if ((hidden = "mozHidden") in document)
	        document.addEventListener("mozvisibilitychange", onvisiblechange );
	    else if ((hidden = "webkitHidden") in document)
	        document.addEventListener("webkitvisibilitychange", onvisiblechange );
	    else if ((hidden = "msHidden") in document)
	        document.addEventListener("msvisibilitychange", onvisiblechange );
	    // IE 9 and lower:
	    else if ('onfocusin' in document)
	        document.onfocusin = document.onfocusout = onvisiblechange;
	    // All others:
	    else
	        window.onpageshow = window.onpagehide 
	            = window.onfocus = window.onblur = onvisiblechange;

	    function onvisiblechange (evt) {
	        var v = 'visible', h = 'hidden',
	            evtMap = { 
	                focus:v, focusin:v, pageshow:v, blur:h, focusout:h, pagehide:h 
	            }, status;
	        
	        evt = evt || window.event;
	        if (evt.type in evtMap)
	            status = evtMap[evt.type];
	        else        
	        	status  = this[hidden] ? "hidden" : "visible";
	        
	        if (status == v) {
	        	self.run();
	        } else if (status == h) {
	        	self.stop();
	        }
	    }
	};
	
	
	/**
	 * call to server to get new Alerts received
	 * and refresh History view
	 */
	Alerts.prototype.requestNewAlerts = function (numOccurs) {
		var self = this;
		$.ajax({
			type: "GET",
		    cache: false,
		    url:'/ADL/home/alertsnew/'+numOccurs,
		    success: function(data) {

		    	var historyCurrent = '';
				$.each(data.response, function(i, alert){
					historyCurrent +='<li class="' + alert.type.toLowerCase() +'" id="' +alert.id+'">' + 
					'<a href=/ADL/alerts/'+alert.id+' class="item-wrap with-next-icon" data-transition="slide">'+
					'<svg class="icon icon-notification"><use xlink:href="'+$.app.rootPath+'/stylesheets/svg/sprite-icons.svg#'+alert.type+'"></use></svg>'+
					'<strong class="notification item-text truncate ellipsis">'+alert.title+'</strong>'+
					'<span class="icon icon-listview-next"></span>';
					if (alert.currentState!=null) {
						historyCurrent +='<br><strong class="status '+alert.currentState.toString().toLowerCase()+'">'+
						alert.currentState+'</strong><br/>';
					}
					historyCurrent +='<br><span class="date-status"> '+ alert.formattedStartDateTime +'</span></a></li>';
				});
				$('.listview.notifications').prepend(historyCurrent);
		    },
		    fail: function() {
		    	console.log('Fail');
		    	if (self.running && self.LOOP) {
					self.timeout = setTimeout(function () {
						self.requestResponse();
					}, self.REQUEST_TIMING);
				}	
		    }
		});
	};
	
	
	
	/**
	 * call to server path
	 * and update layers
	 */
	Alerts.prototype.requestResponse = function () {
		var self = this;
		
		if(!self.running){
			return;
		}
		
		$.ajax({
			type: "GET",
		    cache: false,
		    url: self.PATH,
		    success: function(data) {
				var isHome  = $.mobile.activePage.is('.home'),
					serviceType = data.response.serviceType,
					status  = data.response.systemStatus,
					expired = data.controlFlag && !self.recentInteraction,
					activityLevel  = data.response.activityLevel;
							
				if (expired) {
					
					var intVal = null,
						time = self.EXPIRE_SESSION_TIME_SAVE / 1000,
						$countDown = self.$expiredPopUp.find('#countdown').text(time);
					
					$.app.popupManager.pushPopup("very_high", self.$expiredPopUp, function () {
						clearInterval(intVal);
						
						if (self.running && self.LOOP) {
							self.timeout = setTimeout(function () {
								self.requestResponse();
							}, self.REQUEST_TIMING);
						}
					});
					
					clearInterval(intVal);
					intVal = setInterval(function () {					
						time -= 1;
						
						if (time <= 0) {
							clearInterval(intVal);
							window.location.href = $.app.rootPath + '/sessionTimeout';
							self.stop();
						}
						$countDown.text(time);
					}, 1000);
									
					return false;
				}
				
				if (status) {
					switch (status.statusType) {
						case 'ALARM':
						case 'ALARM_IN_PROGRESS':
							self.$statusIcon.removeClass('icon-ok icon-disconnected icon-photos').addClass('icon-ko')
								.parent().find('.text').remove();
							
							self.status = self.STATUSES.KO;
							break;
						case 'TECH_ALARM':
							self.$statusIcon.removeClass('icon-ko icon-ok icon-photos').addClass('icon-disconnected')
								.parent().find('.text').remove();
							self.status = self.STATUSES.DISCONNECTED;
							break;
						case 'USER_PHOTO_REQUEST':
								self.$statusIcon.removeClass('icon-ko icon-ok icon-disconnected').addClass('icon-photos')
									.parent().find('.text').remove();
								break;
						default:
							self.$statusIcon.removeClass('icon-ko icon-disconnected icon-photos').addClass('icon-ok')
								.parent().find('.text').remove()
								.end()
								.append('<span class="text">OK</span>');
							self.status = self.STATUSES.OK;
							break;
					}
					
					if (status.alert) {
						self.createAlertPopup(data.response.firstName, status.alert, status.statusType);
					}
					
					
					if (status.numAlert > 0){
						//bring new alerts if they trigger and if in history view (self.$issuesCount.text() = counter)
						if ((window.location.pathname == "/ADL/home/history") && (self.$issuesCount.text() < status.numAlert)){
							self.requestNewAlerts(status.numAlert - self.$issuesCount.text());
						}
						
						self.$mainHeader.removeClass('btn-right1').addClass('btn-right2');
						self.$issuesCount.text(status.numAlert).css('display', 'block');
						
						
					} else {
						self.$mainHeader.removeClass('btn-right2').addClass('btn-right1');
						self.$issuesCount.text(status.numAlert).css('display', 'none');
					}
				}
				
				if (isHome) {
					if (activityLevel.patientAtHome) {
						var icon;
						
						if (activityLevel.lastLocation.locationType) {
							switch (activityLevel.lastLocation.locationType.toUpperCase()) {
							case 'BEDROOM':
							case 'SLEEP':
								icon = 'BEDROOM_SENSOR';
								break;
							case 'BATHROOM':
								icon = 'BATHROOM_SENSOR';
								break;
							case 'DINING_ROOM':
							case 'MEAL':
								icon = 'FRIDGE_DOOR';
								break;
							case 'RESTROOM':
							case 'TOILET':
								icon = 'TOILET_ROOM_SENSOR';
								break;
							case 'LIVINGROOM':
								icon = 'LIVING_ROOM';
								break;
							case 'FRONTDOOR':
								icon = 'MAIN_DOOR';
								break;
							default:
								icon = activityLevel.lastLocation.locationType.toUpperCase();
							}
						}
						
						$('#icon-in-home')
							.html('<use xlink:href="' + rootPath + '/stylesheets/svg/sprite-icons.svg#IN_HOME" />');
						$('#user_location').html(I18n.t('alerts.home.in_home'));
						$('#icon_last')
							.html('<use xlink:href="' + rootPath + '/stylesheets/svg/sprite-icons.svg#' + icon + '" />');
						//$('#last_location')
						//	.html(activityLevel.lastLocation.alias +' ('+activityLevel.lastLocation.description+')'+ '<br/> <span class="datetime">' + $.fullCalendar.formatDate(new Date(activityLevel.lastLocation.dateTimeEnd), 'dd-MM-yyyy HH:mm') + '</span>');
						$('#location-in-home').removeClass('hidden');
					} else {
						$('#icon-in-home')
							.html('<use xlink:href="' + rootPath + '/stylesheets/svg/sprite-icons.svg#FRONT_DOOR" />');
						//$('#user_location').html(I18n.t('alerts.home.not_at_home') + ' <span class="datetime">' + $.fullCalendar.formatDate(new Date(activityLevel.lastLocation.dateTimeEnd), 'dd-MM-yyyy HH:mm')+ '</span>');
						$('#location-in-home').addClass('hidden');
					}
					
					if (activityLevel && activityLevel.lastLocation.registeredLevel >= 0) {
						if (serviceType == "EXPRESS") {
							rL = activityLevel.activityLevelExpress;
						} else {
							rL = activityLevel.lastLocation.registeredLevel * 100;
						}
						if (rL > 0 && rL < 20) {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p1');
						} else if (rL >= 20 && rL < 40) {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p2');
						} else if (rL >= 40 && rL < 60) {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p3');
						} else if (rL >= 60 && rL < 80) {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p4');
						} else if (rL >= 80 && rL < 100) {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p5');
						} else {
							$('#icon_a_level').removeAttr('class').addClass('icon icon-home-action icon-movement-level p0');
						}
					}
				}
				
				if (self.running && self.LOOP) {
					self.timeout = setTimeout(function () {
						self.requestResponse();
					}, self.REQUEST_TIMING);
				}
		    },
		    fail: function() {
		    	if (self.running && self.LOOP) {
					self.timeout = setTimeout(function () {
						self.requestResponse();
					}, self.REQUEST_TIMING);
				}	
		    }
		});
	};
	
	Alerts.prototype.createAlertPopup = function (patientName, alert, statusType) {	
		if(alert.type != 'TECHNICAL'){
			if (this.handledAlerts.indexOf(alert.id) == -1) {
				console.log('ALERTA' + alert.id);
				var $popUp = $('<div data-role="popup" data-overlay-theme="a" data-dismissible="false" class="alarm-popup system-status-pop"></div>');
				$popUp.html(this.tpls.getPopUpTpl(patientName, alert, statusType));
				
				$popUp.insertAfter(this.$expiredPopUp);
				$popUp.find('a').click(function() {
					// the queue of popupManager is freezed before changing page to avoid losing pending popups
					$.app.popupManager.freeze();
				});
				$('body').css('overflow','hidden');
				$popUp.popup({
					positionTo: 'window',
					transition: 'fade'
				});
				
				var thereIsLoader = false;
				if ($('.ui-loader').css('display') == 'block') {
					// loader is shown , we hide it and open it again afterwards
					thereIsLoader = true;
					$('.ui-loader').css('visibility', 'hidden');
				}
				$.app.popupManager.pushPopup("high", $popUp, function () {
					if (thereIsLoader) {
						$('.ui-loader').css('visibility', 'visible');
					}
					$popUp.remove();
				});
				
				this.handledAlerts.push(alert.id);
				
				//CALL MAP API
				if(alert.deviceType === 9 && alert.eventCode === 2 || alert.eventCode === 82){
					if (alert.location !== null || alert.location !== undefined){
						initMap(alert.location.latitude, alert.location.longitude, alert.location.horizontalAccuracy);	
					}
				}
				
			}
		}
	};
	
	Alerts.prototype.run = function () {
		this.stop();
		this.running = true;
		this.requestResponse();
	};
	
	Alerts.prototype.stop = function () {
		if (this.timeout) {
			clearTimeout(this.timeout);
		}
		this.running = false;
	};
	
	/*
	 * This function is used to record interactions that don't imply a request to the server. 
	 * If the user did something lately we avoid the expire message, even if according to the server it is needed.
	 * This can happen if the user takes long time on deciding what to do with an alert, and then the expire message would appear right after the user closes the popup
	 */
	Alerts.prototype.jsInteraction = function () {
		this.recentInteraction = true;
		var self = this;
		if (this.jsInteractionTimer) {
			clearTimeout(this.jsInteractionTimer);
		}
		this.jsInteractionTimer = setTimeout(function() {
			self.recentInteraction = false
		}, self.EXPIRATION_TIME );
	}
	
	$.app.alerts = new Alerts(true);
});



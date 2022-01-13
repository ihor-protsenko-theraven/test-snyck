(function($, Session, Policy, Rules, Periods, Devices, Form, tpls) {
	'use strict';


	// ESC17-4590 Create own validation mode to prevent Xss Attacks
	jQuery.validator.addMethod("xssPrevention", function(value, element) {
		return this.optional(element) || /^((?![",&,<,>,',/]).)*$/.test(value);
	}, I18n.t('xssError.message'));
	
	// initialize
	function initRules() {
		var $rules = $('#rules');
		var $periods = $('#periods');

		if ($rules.length || $periods.length) {
			var isRules = !!$rules.length
			var $list = isRules ? $('#rules-list') : $('#periods-list');
			var $addButton = isRules ? $('#add-rule') : $('#add-period');
			var $commit = $('#commit-rules');
			var $reset = $('#reset-rules');
			var userId = $list.data('user-id');
			var panelId = $list.data('panel-id');
			var isPro = $list.data('is-pro');
			
			// EIC15-2454: 	For Wake & Well Rules the number of locations is only 
			//				one for Express (Family) residents -->
			
			
			window.isExpress = $list.data('is-express');
			if (typeof window.isExpress == 'undefined') window.isExpress = false;
		
			
			var session = new Session(panelId);
			var policy = new Policy();
			var periods = new Periods(panelId);
			var rules = new Rules(panelId);
			
			
			

			// Common events
			$(window).on('beforeunload', storeHandler);
			$('a').on('click', navigateHandle);
			$commit.on('click', sendRules);
			$reset.on('click', resetRules);

			rules.on('request', toggleLoader).on('change', toggleModified).on(
					'periods', periods.setList.bind(periods));
			periods.on('change', toggleModified);
			policy.on('request', toggleLoader);

			if ($rules.length) {
				var devices = new Devices(panelId);

				rules
						.on(
								'list',
								[ policy.updateRules.bind(policy),
										renderRulesList ])
						.on(
								'added',
								[ policy.addRule.bind(policy),
										checkPrevPeriodRules, addRuleItem ])
						.on(
								'updated',
								[ policy.updateRule.bind(policy),
										checkPrevPeriodRules, replaceRuleItem ])
						.on(
								'removed',
								[ policy.removeRule.bind(policy),
										checkPeriodRules, removeRuleItem ]).on(
								'toggleDeleted',
								[ policyToggleDeleteRule, checkPeriodRules,
										replaceRuleItem ]);
				policy.on('maximumRules', toggleAdd)
				devices.on('request', toggleLoader);

				$addButton.on('click', addRule);
				$list.on('click', '[data-edit]', editRule);
				$list.on('click', '[data-destroy]', destroyRule);
				$list.on('click', '[data-toggle-delete]', toggleDeleteRule);
				$list.on('change', '[data-toggle-enabled]', toggleEnabledRule);
			}

			if ($periods.length) {

				periods
						.on(
								'list',
								[ policy.updatePeriods.bind(policy),
										renderPeriodsList ])
						.on(
								'added',
								[ policy.addPeriod.bind(policy), addPeriodItem ])
						.on('updated', replacePeriodItem)
						.on(
								'removed',
								[ policy.removePeriod.bind(policy),
										removePeriodItem ])
						.on('toggleDeleted',
								[ policyToggleDeletePeriod, replacePeriodItem ]);
				policy.on('maximumPeriods', toggleAdd);

				$addButton.on('click', newPeriod);
				$list.on('click', '[data-edit]', editPeriod);
				$list.on('click', '[data-destroy]', destroyPeriod);
				$list.on('click', '[data-toggle-delete]', toggleDeletePeriod);
			}

			// Retrieve from session or get list from server
			if (session.retrieve()) {
				var sessRules = session.getRules();
				var sessPeriods = session.getPeriods();
				var sessPolicy = session.getPolicies();

				policy.set(sessPolicy);
				rules.setList(sessRules.stored, sessRules.originals);
				periods.setList(sessPeriods.stored, sessPeriods.originals);
			} else {
				policy.get().then(function() {
					rules.getList();
				});
			}
		} else {
			var i = 0;
			var len = sessionStorage.length;

			for (i; i < len; i++) {
				var sessKey = sessionStorage.key(i);

				if (/^rules/.test(sessKey))
					sessionStorage.removeItem(sessKey);
			}
		}

		// EVENTS HANDLERS
		function storeHandler() {
			session.store({
				rules : {
					stored : rules.rules,
					originals : rules.original
				},
				periods : {
					stored : periods.periods,
					originals : periods.original
				},
				policies : policy.counters
			});
		}
		;

		function navigateHandle(event) {
			var $link = $(this);
			var next = $link.attr('href');
			var inRules = /rules/.test(next);

			if (!inRules && (periods.hasChange || rules.hasChange)) {
				createConfirmExitModal(next);
				return false;
			}
		}
		;

		function toggleLoader(requesting) {
			requesting ? $.app.loader.show() : $.app.loader.hide();
		}
		;
		function toggleModified() {
			var hasChange = periods.hasChange || rules.hasChange;

			$rules.toggleClass('modified', hasChange);
			$periods.toggleClass('modified', hasChange);

			$.app.scroll.resize();
		}
		;

		function toggleAdd(isMaximum) {
			isMaximum ? $addButton.fadeOut(250) : $addButton.fadeIn(250);
		}
		;

		function policyToggleDeleteRule(rule, deleted) {
			deleted ? policy.removeRule(rule) : policy.addRule(rule);
		}
		;

		function policyToggleDeletePeriod(period, deleted) {
			deleted ? policy.removePeriod(period) : policy.addPeriod(period);
		}
		;

		function updateRuleListFromServer() {
			return rules.getList(true);
		}
		;

		function sendRules(event) {

			var data = {
				panelId : panelId,
				rules : {
					deleted : rules.getDeletedIds(),
					modified : rules.toServer()
				},
				periods : {
					deleted : periods.getDeletedIds(),
					modified : periods.toServer()
				}
			};

			for (var i=0, iLen=data.rules.modified.length; i<iLen; i++) {
			    if (data.rules.modified[i].ruleType == "AtHomeForTooLong" && data.rules.modified[i].delay == null) {
			    	data.rules.modified[i].delay = data.rules.modified[i].homeTime;
			    }
				if (data.rules.modified[i].ruleType == "OutOfHome" && data.rules.modified[i].delay == null) {
					data.rules.modified[i].delay = data.rules.modified[i].homeTime;
				}
			}
			
			var stringData = JSON.stringify(data).replace("homeTime", "delay");
			
			$.app.loader.show();
			$.ajax({
				type : 'POST',
				url : $.app.rootPath + '/admin/rules/set-rules',
				data : stringData,
				contentType : 'application/json'
			}).then(updateRuleListFromServer).fail(
					function(jqXHR, textStatus, errorThrown) {

						if (jqXHR.status == "401") {
							window.location = $.app.rootPath + '/unauthorized';
						} else if (jqXHR.status == "400") {
							// Generic Validation Error
							showValidationError();
						} else if (jqXHR.status == "406") {
							// Validation Error - specific rule reached its limit description
							showSpecificRuleMaxQuantityError();
						} else if (jqXHR.status == "417") {
							// Validation Error - rule reached the limit
							// permitted rulesMaxQuantityError
							showRulesMaxQuantityError();
						} else if (jqXHR.status == "418") {
							// Validation Error - Up&Go- At least one location must be stablished
							showNoLocationError();
						} else if (jqXHR.status == "422") {
							// Validation Error - specific missing days of week
							// description
							showMissingDaysOfWeekError();
						}  else if (jqXHR.status == "509") {
							// Validation Error - specific missing days of week
							// description
							showXssError();
						} else {
							showServerError();
						}
					}).always(function() {
				$.app.loader.hide();
			});

			return false;
		}
		;

		function resetRules(event) {
			rules.restoreOriginals();
			periods.restoreOriginals();

			return false;
		}
		;

		function checkPrevPeriodRules(rule, prevRule) {
			if (prevRule)
				checkPeriodRules(prevRule);
		}
		;

		function checkPeriodRules(rule) {
			if (rule.periodSystemId) {
				periods.find(rule.periodSystemId).then(function(period) {
					if (rules.byPeriodSystemId(period.systemId).length) {
						period._hasRule = true;
					} else {
						delete period._hasRule;
					}
				});
			}
		}
		;

		function addRule(event) {
			var rule = {
				systemId : uuid.v1(),
				ruleType : 'unsetted',
				enabled : true,
				editable : true
			};

			newRuleModal(rule);

			return false;
		}
		;

		function editRule(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			rules.find(systemId).then(editRuleModal);

			return false;
		}
		;

		function destroyRule(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			rules.destroy(systemId);
			
			// EIC-2161 -> Force refresh when delete 
			location.reload();

			return false;
		}
		;

		function toggleDeleteRule(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			rules.toggleDelete(systemId);

			return false;
		}
		;

		function toggleEnabledRule(event) {
			var $check = $(this);
			var systemId = $check.data('system-id');
			var enabled = event.target.checked;

			rules.toggleEnabled(systemId, enabled);

			return false;
		}
		;

		function changeForm(event) {
			var $form = $(this.form);
			var $modal = $form.parents('.modal');
			var rule = Form.mapForm($form);

			if (rule.groupDeviceId)
				rule.groupDeviceId = '';

			replaceRuleModal(rule, $modal);
		}
		;

		function createRule(event) {
			var $form = $(this);
			var $modal = $form.parents('.modal');
			var rule = Form.mapForm($form);
			
	
			if (rule.periodSystemId) {
				periods.find(rule.periodSystemId).then(function(period) {
					rule.period = period;
					period._hasRule = true;
					$modal.trigger('modal:hide', false);
					rules.create(rule);
				})
			} else {
				rules.create(rule);
				$modal.trigger('modal:hide', false);
			}

			return false;
		}
		;

		function updateRule(event) {
			var $form = $(this);
			var $modal = $form.parents('.modal');
			var rule = Form.mapForm($form);

			if (rule.periodSystemId) {
				periods.find(rule.periodSystemId).then(function(period) {
					rule.period = period;
					period._hasRule = true;
					$modal.trigger('modal:hide', false);
					rules.update(rule.systemId, rule);

				})
			} else {
				rules.update(rule.systemId, rule);
				$modal.trigger('modal:hide', false);
			}

			return false;
		}
		;

		function destroyModal(event, edit) {
			var $modal = $(this);
			var action = edit ? updateRule : createRule;

			$modal.off('modal:hide', destroyModal).off('change', '#ruleType',
					changeForm).off('change', '#isDeviceGroup', changeForm)
					.off('submit', '#rule-form', action).remove();
		}
		;

		function newPeriod(event) {
			newPeriodModal(periods.newPeriod());

			return false;
		}
		;

		function editPeriod(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			periods.find(systemId).then(editPeriodModal);

			return false;
		}
		;

		function createPeriod(event) {
			var $form = $(this);
			var $modal = $form.parents('.modal');
			var period = Form.mapForm($form);

			periods.create(period);
			$modal.trigger('modal:hide', false);

			return false;
		}
		;

		function updatePeriod(event) {
			var $form = $(this);
			var $modal = $form.parents('.modal');
			var period = Form.mapForm($form);

			periods.update(period.systemId, period);
			$modal.trigger('modal:hide', true);

			return false;
		}
		;

		function destroyPeriod(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			periods.destroy(systemId);

			return false;
		}
		;

		function toggleDeletePeriod(event) {
			var $link = $(this);
			var systemId = $link.data('system-id');

			periods.toggleDelete(systemId);

			return false;
		}
		;

		function changePeriodForm(event) {
			var $form = $(this.form);
			var $modal = $form.parents('.modal');

			replacePeriodModal(Form.mapForm($form), $modal);
		}
		;

		function disableEndTimes(event) {
			var value = this.value;

			if (value) {
				var $endTime = $('#endTime');

				// if ( $endTime.val() < value ) endTime.combobox('val', value);

				$endTime.find('option[value="' + value + '"]').attr('disabled',
						true);
				$endTime.find('option:not([value="' + value + '"])')
						.removeAttr('disabled');
			}
		}
		;

		function disableStartTimes(event) {
			var value = this.value;

			if (value) {
				var $startTime = $('#startTime');

				// if ( $startTime.val() > value ) $startTime.combobox('val',
				// value);

				$startTime.find('option[value="' + value + '"]').attr(
						'disabled', true);
				$startTime.find('option:not([value="' + value + '"])')
						.removeAttr('disabled');
			}
		}

		function destroyModalPeriod(event, edit) {
			var $modal = $(this);
			var action = edit ? updatePeriod : createPeriod;

			$modal.off('modal:hide', destroyModalPeriod).off('change',
					'#is24Hours', changePeriodForm).off('submit',
					'#period-form', action).remove();
		}
		;

		function removeModal() {
			$(this).remove();
		}
		;

		// VIEW
		function addRuleItem(rule) {
			var _html = tpls.ruleItem(rule);

			$list.append(_html);
			$.app.scroll.resize();
		}
		;

		function replaceRuleItem(rule) {
			var systemId = rule.systemId;
			var _html = tpls.ruleItem(rule);

			$list.find('#rule-' + systemId).replaceWith(_html);
		}
		;

		function removeRuleItem(rule) {
			$list.find('#rule-' + rule.systemId).remove();
			$.app.scroll.resize();
		}
		;

		function renderRulesList(rules) {
			var _html = rules.filter(isEditable).map(tpls.ruleItem).join('');

			$list.html(_html);
			$.app.scroll.resize();
		}
		;

		function addPeriodItem(period) {
			var _html = tpls.periodItem(period);

			$list.append(_html);
			$.app.scroll.resize();
		}
		;

		function replacePeriodItem(period) {
			var systemId = period.systemId;
			var _html = tpls.periodItem(period);

			$list.find('#period-' + systemId).replaceWith(_html);
		}
		;

		function removePeriodItem(period) {
			$list.find('#period-' + period.systemId).remove();
			$.app.scroll.resize();
		}
		;

		function renderPeriodsList(periods) {
			var _html = periods.filter(isEditable).map(tpls.periodItem)
					.join('');

			$list.html(_html);
			$.app.scroll.resize();
		}
		;

		function newRuleModal(rule) {
			periods
					.getList()
					.then(
							function(periods) {
								periods = periods.filter(isEditable).filter(
										notDeleted);
								devices
										.getList()
										.then(
												function(devices) {
													var ruleType = rule.ruleType;
													var isDeviceGroup = rule.isDeviceGroup === 'true'
															|| rule.isDeviceGroup === true;
													var _html = tpls
															.formRule(
																	rule,
																	Form
																			.fieldsForType(
																					policy,
																					ruleType,
																					periods,
																					devices,
																					isDeviceGroup),
																	false);

													createRuleModal(_html,
															false);
													$('#ruleType').trigger('change');
												});
							});
		}
		;

		function editRuleModal(rule) {
			if (rule.ruleType == "AtHomeForTooLong") {
				rule.homeTime = rule.delay;
			};
			if (rule.ruleType == "OutOfHome") {
				rule.homeTime = rule.delay;
			};

			periods.getList().then(function (periods) {
				periods = periods.filter(isEditable).filter(notDeleted);
				devices.getList().then(function (devices) {
					var ruleType = rule.ruleType;
					var isDeviceGroup = rule.isDeviceGroup === 'true' || rule.isDeviceGroup === true;
					var _html = tpls.formRule(rule, Form.fieldsForType(policy, ruleType, periods, devices, isDeviceGroup), true);
					
					createRuleModal(_html, true);
					
					if (ruleType === 'Presence') { 
						$('#firstLocationGroupDeviceId').trigger('change');
							$('#secondLocationGroupDeviceId').trigger('change');
							$('#thirdLocationGroupDeviceId').trigger('change');
						}
				});
			});
		};
		
		function createRuleModal(_html, edit) {
			var formAction = edit ? updateRule : createRule;
			
			var result = $.app.modals.$tpl(_html, 'edit-rule', true)
					.addClass('form container_16')
					.on('modal:hide', destroyModal)
					.appendTo('body')
					.trigger('modal:create').trigger('modal:update').trigger('modal:show')
					.on('change', '#ruleType', changeForm)
					.on('change', '#isDeviceGroup', changeForm)
					.on('submit', '#rule-form', formAction)
					.on('change', '#firstLocationGroupDeviceId', updateLocationValuesOnPresenceRule)
					.on('change', '#secondLocationGroupDeviceId', updateLocationValuesOnPresenceRule)
					.on('change', '#thirdLocationGroupDeviceId', updateLocationValuesOnPresenceRule);
			
			$(document).ready(function(){
				// set translation for time labels
				var labelD = $("label[for='" + $('#days-duration').attr('id') + "']");
				var labelH = $("label[for='" + $('#hours-duration').attr('id') + "']");
				var labelM = $("label[for='" + $('#minutes-duration').attr('id') + "']");
				var labelS = $("label[for='" + $('#seconds-duration').attr('id') + "']");
				var labelDelayD = $("label[for='" + $('#days-delay').attr('id') + "']");
				var labelDelayH = $("label[for='" + $('#hours-delay').attr('id') + "']");
				var labelDelayM = $("label[for='" + $('#minutes-delay').attr('id') + "']");
				var labelDelayS = $("label[for='" + $('#seconds-delay').attr('id') + "']");
				labelD.html(I18n.t('times.days'));
				labelH.html(I18n.t('times.hours'));
				labelM.html(I18n.t('times.minutes'));
				labelS.html(I18n.t('times.seconds'));
				labelDelayD.html(I18n.t('times.days'));
				labelDelayH.html(I18n.t('times.hours'));
				labelDelayM.html(I18n.t('times.minutes'));
				labelDelayS.html(I18n.t('times.seconds'));

				$("#groupAllDoors").closest("li").prepend("<div><label class='doorsLabel'></label></div>" )
				$(".doorsLabel").html(I18n.t('labels.doors'));
				$("#uniform-groupAllDoors").prop("style","margin-left:1em;margin-top:0.5em");
				$("#uniform-groupDeviceId").prop("style","margin-left:1em");
				$('label[for="groupDeviceId"]').prop("style","margin-left:1em");
				$('label[for="groupAllDoors"]').prop("style","margin-top:0.5em");
				$("#groupAllDoors").closest("li").prop("style","padding	-bottom:0.5em");
				
				// check on load
				if($('#groupDeviceId').val() instanceof Array){
					$("#groupDeviceId").prop("disabled", true);
					$("#groupDeviceId").closest(".selector").attr("disabled", true);
					$('label[for="groupDeviceId"]').attr("disabled", true);
        			$("#groupAllDoors").prop( "checked", true);
        			$("#groupAllDoors").parent().addClass('checked');
    			}
				// update groupDeviceId field in case we have allDevices field
				$('#groupAllDoors').change(function() {
					if ($(this).is(":checked")) {
						$("#groupDeviceId").prop("disabled", true);
						$("#groupDeviceId").closest(".selector").attr("disabled", true);
						$('label[for="groupDeviceId"]').attr("disabled", true);
					} else {
						$("#groupDeviceId").prop("disabled", false);
						$("#groupDeviceId").closest(".selector").attr("disabled", false);
						$('label[for="groupDeviceId"]').attr("disabled", false);
					}
				});
				// for enabling groupDeviceId field before submit
		  		$('form').bind('submit', function () {
		    		$(this).find(':input').prop('disabled', false);
		  		});
			});
			
			return result;	
		};
			
		function updateLocationValuesOnPresenceRule(event) {
			
			$('.locs').children().removeAttr('disabled');
	    	
			$('.locs').filter(function(index) {
	    		if($(this).val() === -1){ //0 index based
	    			return false;
	    		}
	    		return true;
	    	}).each(function(index) {
				$select = $(this);
				if ($select.val() != -1) {
					$('.locs').filter(function(index) {
			    		if($(this).attr('id') === $select.attr('id')){ //0 index based
			    			return false;
			    		}
			    		return true;
			    	}).children('option[value=' + $select.val() + ']')
				        .attr('disabled', true);
				}
				
	    	})
		}
		
		function replaceRuleModal(rule, $modal) {
			periods
					.getList()
					.then(
							function(periods) {
								periods = periods.filter(isEditable).filter(
										notDeleted);
								devices
										.getList()
										.then(
												function(devices) {
													var ruleType = rule.ruleType
															|| 'unsetted';

													// Force isDeviceGroup to
													// false if only devices
													// allowed by rule type
													// TODO: force in form
													// builder
													if (ruleType === 'DoorOpen'
															|| ruleType === 'ShortStayBedroom') {
														rule.isDeviceGroup = false;
													}

													var isDeviceGroup = rule.isDeviceGroup === 'true'
															|| rule.isDeviceGroup === true;
													var _html = tpls
															.formList(
																	rule,
																	Form
																			.fieldsForType(
																					policy,
																					ruleType,
																					periods,
																					devices,
																					isDeviceGroup),
																	'rules.form');

													$modal
															.find(
																	'#rule-form .form-list')
															.html(_html)
															.end()
															.trigger(
																	'modal:update',
																	[ {
																		preventChange : true
																	} ])
															.trigger(
																	'modal:show');
												});
							});
			
			$(document).ready(function(){
				// set translation for time labels
				var labelD = $("label[for='" + $('#days-duration').attr('id') + "']");
				var labelH = $("label[for='" + $('#hours-duration').attr('id') + "']");
				var labelM = $("label[for='" + $('#minutes-duration').attr('id') + "']");
				var labelS = $("label[for='" + $('#seconds-duration').attr('id') + "']");
				var labelDelayD = $("label[for='" + $('#days-delay').attr('id') + "']");
				var labelDelayH = $("label[for='" + $('#hours-delay').attr('id') + "']");
				var labelDelayM = $("label[for='" + $('#minutes-delay').attr('id') + "']");
				var labelDelayS = $("label[for='" + $('#seconds-delay').attr('id') + "']");
				labelD.html(I18n.t('times.days'));
				labelH.html(I18n.t('times.hours'));
				labelM.html(I18n.t('times.minutes'));
				labelS.html(I18n.t('times.seconds'));
				labelDelayD.html(I18n.t('times.days'));
				labelDelayH.html(I18n.t('times.hours'));
				labelDelayM.html(I18n.t('times.minutes'));
				labelDelayS.html(I18n.t('times.seconds'));
				
				$("#groupAllDoors").closest("li").prepend("<div><label class='doorsLabel'></label></div>" )
				$(".doorsLabel").html(I18n.t('labels.doors'));
				$("#uniform-groupAllDoors").prop("style","margin-left:1em;margin-top:0.5em");
				$("#uniform-groupDeviceId").prop("style","margin-left:1em");
				$('label[for="groupDeviceId"]').prop("style","margin-left:1em");
				$('label[for="groupAllDoors"]').prop("style","margin-top:0.5em");
				$("#groupAllDoors").closest("li").prop("style","padding	-bottom:0.5em");
				
				// update groupDeviceId field in case we have allDevices field
				$('#groupAllDoors').change(function() {
					if ($(this).is(":checked")) {
						$("#groupDeviceId").prop("disabled", true);
						$("#groupDeviceId").closest(".selector").attr("disabled", true);
						$('label[for="groupDeviceId"]').attr("disabled", true);
					} else {
						$("#groupDeviceId").prop("disabled", false);
						$("#groupDeviceId").closest(".selector").attr("disabled", false);
						$('label[for="groupDeviceId"]').attr("disabled", false);
					}
				});
				// for enabling groupDeviceId field before submit
		  		$('form').bind('submit', function () {
		    		$(this).find(':input').prop('disabled', false);
		  		});
			});
		}
		;

		function newPeriodModal(period) {
			var _html = tpls.formPeriod(period, Form.fieldsForPeriod(period),
					false);

			createPeriodModal(_html, false);
			setTimeout(function(){
				// EIC15-
				$('.ui-autocomplete.ui-front.ui-menu.ui-widget.ui-widget-content').hide();
			},75);	
		}
		;

		function editPeriodModal(period) {
			var _html = tpls.formPeriod(period, Form.fieldsForPeriod(period),
					true);

			createPeriodModal(_html, true);
			setTimeout(function(){
				$('.ui-autocomplete.ui-front.ui-menu.ui-widget.ui-widget-content').hide();
			},75);
		}
		;

		function createPeriodModal(_html, edit) {
			var formAction = edit ? updatePeriod : createPeriod;

			return $.app.modals.$tpl(_html, 'edit-period', true).addClass(
					'form container_16').on('modal:hide', destroyModalPeriod)
					.appendTo('body').trigger('modal:create').trigger(
							'modal:update').trigger('modal:show').on('change',
							'#is24Hours', changePeriodForm).on('submit',
							'#period-form', formAction).find('#startTime')
					.combobox({
						select : disableEndTimes
					}).end().find('#endTime').combobox({
						select : disableStartTimes
					});
		}
		;

		function replacePeriodModal(period, $modal) {
			if (/^\d{2}:\d{2}$/.test(period.startTime))
				period.startTime = '1980-01-01T' + period.startTime + ':00';
			if (/^\d{2}:\d{2}$/.test(period.endTime))
				period.endTime = '1980-01-01T' + period.endTime + ':00';

			var _html = tpls.formList(period, Form.fieldsForPeriod(period),
					'periods.form');

			$modal.find('#period-form .form-list').html(_html).end().trigger(
					'modal:update', [ {
						preventChange : true
					} ]).trigger('modal:show').find('#startTime').combobox({
				select : disableEndTimes
			}).end().find('#endTime').combobox({
				select : disableStartTimes
			}).end();
		}
		;

		function createConfirmExitModal(exitLink) {
			var _html = tpls.confirmExitModal(exitLink);

			return $.app.modals.$tpl(_html, 'exit-rules', true).addClass(
					'confirm').on('modal:hide', removeModal).appendTo('body')
					.trigger('modal:create').trigger('modal:update').trigger(
							'modal:show');
		}
		;

		function errorTemplate(_html) {
			return $.app.modals.$tpl(_html, 'system-error', true).addClass(
					'confirm').on('modal:hide', removeModal).appendTo('body')
					.trigger('modal:create').trigger('modal:update').trigger(
							'modal:show');
		};
		
		function showMissingDaysOfWeekError() { 
			return errorTemplate(tpls.showMissingDaysOfWeekError());
		};
		
		function showRulesMaxQuantityError() { 
			return errorTemplate(tpls.rulesMaxQuantityError());
		};
		
		function showNoLocationError() { 
			return errorTemplate(tpls.noLocationError());
		};
		
		
		function showRulesMaxQuantityError() { 
			return errorTemplate(tpls.lo());
		};
		
		function showSpecificRuleMaxQuantityError() { 
			return errorTemplate(tpls.specificRuleMaxQuantityError());
		};

		function showValidationError() {
			return errorTemplate(tpls.validationError());
		};
		
		function showXssError() {
			return errorTemplate(tpls.xssError());
		};

		function showServerError() {
			return errorTemplate(tpls.serverError());
		}
		;
		
	}
	;

	function dateFromTimeStr(time) {
		var split = time.split(':');
		return new Date(0, 0, 0, split[0], split[1]);
	}
	;

	function isEditable(item) {
		return item.editable;
	}
	;

	function notDeleted(item) {
		return !item._delete;
	}
	;

	$(initRules);
})
		(
				jQuery,
				(function($) {
					'use strict';

					var defaultModel = {
						rules : {
							originals : [],
							stored : []
						},
						periods : {
							originals : [],
							stored : []
						},
						policies : {}
					};

					function Session(panelId) {
						this.key = 'rules-' + panelId;
						this.model = this.retrieve() || defaultModel;
					}
					;

					Session.prototype = {
						getKey : function getKey() {
							return this.key;
						},
						store : function store(model) {
							this.model = model;
							sessionStorage.setItem(this.getKey(), JSON
									.stringify(model));
						},
						retrieve : function retrieve() {
							return JSON.parse(sessionStorage.getItem(this
									.getKey()));
						},
						getRules : function getRules() {
							var rules = this.model.rules;
							
							for (var i=0, iLen=rules.originals.length; i<iLen; i++) {
							    if (rules.originals[i].ruleType == "AtHomeForTooLong") {
							    	if (rules.originals[i].homeTime == null) {
							    		rules.originals[i].homeTime = rules.originals[i].delay;
							    	}
							    }
							}
							for (var i=0, iLen=rules.stored.length; i<iLen; i++) {
							    if (rules.stored[i].ruleType == "AtHomeForTooLong") {
							    	if (rules.stored[i].homeTime == null) {
							    		rules.stored[i].homeTime = rules.stored[i].delay;
							    	}
							    }
							}
							for (var i=0, iLen=rules.originals.length; i<iLen; i++) {
							    if (rules.originals[i].ruleType == "OutOfHome") {
							    	if (rules.originals[i].homeTime == null) {
							    		rules.originals[i].homeTime = rules.originals[i].delay;
							    	}
							    }
							}
							for (var i=0, iLen=rules.stored.length; i<iLen; i++) {
							    if (rules.stored[i].ruleType == "OutOfHome") {
							    	if (rules.stored[i].homeTime == null) {
							    		rules.stored[i].homeTime = rules.stored[i].delay;
							    	}
							    }
							}

							return rules;
						},
						getPeriods : function getPeriods() {
							return this.model.periods;
						},
						getPolicies : function getPolicies() {
							return this.model.policies;
						},
						clean : function() {
							sessionStorage.removeItem(this.getKey());
						}
					};

					return Session;
				})(jQuery),
				(function($) {
					'use strict';

					// Policy
					function Policy() {
						this.counters = {};

						this.callbacks = {
							request : $.Callbacks(),
							maximumRules : $.Callbacks(),
							maximumPeriods : $.Callbacks()
						};
					}
					;

					Policy.prototype = {
						on : function on(callback, fn) {
							if (this.callbacks[callback]) {
								if (typeof fn === 'function')
									this.callbacks[callback].add(fn);
								if (Array.isArray(fn))
									fn.forEach(this.on.bind(this, callback));
							}

							return this;
						},
						off : function(callback, fn) {
							if (this.callbacks[callback]
									&& typeof fn === 'function') {
								if (typeof fn === 'function')
									this.callbacks[callback].remove(fn);
								if (Array.isArray(fn))
									fn.forEach(this.off.bind(this, callback));
							}

							return this;
						},
						set : function set(counters) {
							this.counters = counters;
						},
						get : function get() {
							return this.request().then(
									onRequestSuccess.bind(this));
						},
						updateRules : function updateRules(rules) {
							this.counters.rules.count = rules.length;
							Object
									.keys(this.counters.ruleTypes)
									.forEach(
											function updateRuleType(ruleType) {
												this.counters.ruleTypes[ruleType].count = countRuleTypes(
														rules, ruleType);
											}.bind(this));
							setManualRuleCounter(rules.length);
							
							// Hide the button if global limitation reached or
							// manual reached
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumRules,
									this.counters.rules,
									this.counters.ruleTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						updateRule : function updateRule(rule, prevRule) {
							if (rule.ruleType !== prevRule.ruleType) {
								this.addRuleType(rule.ruleType);
								this.removeRuleType(prevRule.ruleType);
							}
						},
						addRule : function(rule) {
							this.counters.rules.count++;
							setManualRuleCounter(rules.length);
							
							this.addRuleType(rule.ruleType);

							// Hide the button if global limitation reached or
							// manual reached
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumRules,
									this.counters.rules,
									this.counters.ruleTypes["Manual"]);
							
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						removeRule : function(rule) {
							this.counters.rules.count--;
							setManualRuleCounter(rules.length);
							this.removeRuleType(rule.ruleType);

							// Hide the button if global limitation reached or
							// manual reached
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumRules,
									this.counters.rules,
									this.counters.ruleTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						addRuleType : function(ruleType) {
							if (this.counters.ruleTypes[ruleType]) {
								this.counters.ruleTypes[ruleType].count++;
								incManualRule();
								
							}
						},
						removeRuleType : function removeRuleType(ruleType) {
							if (this.counters.ruleTypes[ruleType]) {
								this.counters.ruleTypes[ruleType].count--;
								decManualRule();
							}
						},
						isMaxRuleType : function(ruleType) {
							var counter = this.counters.ruleTypes[ruleType];
							if (counter) {
								return counter.count >= counter.max;
							}
							return false;
						},
						updatePeriods : function(periods) {
							this.counters.periods.count = periods.length;
							Object
									.keys(this.counters.periodTypes)
									.forEach(
											function updatePeriodType(
													periodType) {
												this.counters.periodTypes[periodType].count = countPeriodTypes(
														periods, periodType);
											}.bind(this));
							// triggerMaximum(this.callbacks.maximumPeriods,
							// this.counters.periods);
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumPeriods,
									this.counters.periods,
									this.counters.periodTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						addPeriod : function(period) {

							this.counters.periods.count++;
							if (this.counters.periodTypes
									&& this.counters.periodTypes["Manual"]) {
								this.counters.periodTypes["Manual"].count++;
							}
							// triggerMaximum(this.callbacks.maximumPeriods,
							// this.counters.periods);
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumPeriods,
									this.counters.periods,
									this.counters.periodTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						removePeriod : function(period) {
							this.counters.periods.count--;
							if (this.counters.periodTypes
									&& this.counters.periodTypes["Manual"]) {
								this.counters.periodTypes["Manual"].count--;
							}

							// triggerMaximum(this.callbacks.maximumPeriods,
							// this.counters.periods);
							triggerMaximumIfGlobalOrManual(
									this.callbacks.maximumPeriods,
									this.counters.periods,
									this.counters.periodTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
						},
						request : function request() {
							this.callbacks.request.fire(true);
							return $.getJSON(
									$.app.rootPath + '/admin/rules/policy')

							.fail(
									function(jqXHR, textStatus, errorThrown) {

										if (jqXHR.status == "401") {
											window.location = $.app.rootPath
													+ '/unauthorized';
										} else {
											showServerError();
										}
									})

							.always(onAllwaysRequest.bind(this));
						}
					};

					// private
					
					// EIC15-2395: Access to Manual policy encapsulated to control scenarios
					//             without Manual Policies limitation (Ej: FAMILY resident group)
					
					function incManualRule()
					{
						try
						{
						this.counters.ruleTypes["Manual"].count++;
						}
						catch (err)
						{
							
						}
					}
					
					function decManualRule()
					{
						try
						{
						this.counters.ruleTypes["Manual"].count--;
						}
						catch (err)
						{
							
						}
					}
					
					function setManualRuleCounter(newLength)
					{
						try
						{
							this.counters.ruleTypes["Manual"].count  = newLength;
						}
						catch (err)
						{
							
						}
					}
					
					function triggerMaximum(callback, counter) {
						
						// EIC15-2395: If exception is launched App continues without
						// 			   hidden the buttons (Use Case: No manuel rule & PEriod
						//				policies defined)
						try
						{
							callback.fire(counter.count >= counter.max);
						}
						catch (err)
						{
							
						}
					}
					;
					

					function triggerMaximumIfGlobalOrManual(callback, counter1,
							counter2) {
						
						// EIC15-2395: If exception is launched App continues without
						// 			   hidden the buttons (Use Case: No manuel rule & PEriod
						//				policies defined)
						
						try
						{
							if (counter1 && counter2) {
								callback.fire(counter1.count >= counter1.max
										|| counter2.count >= counter2.max);
							} else if (counter1) {
								triggerMaximum(callback, counter1);
							} else if (counter2) {
								triggerMaximum(callback, counter2);
							}
						}
						catch (err)
						{
							
						}

					}
					;
					
					/**
					 * With triggerMaximumIfGlobalOrManual we can check global counters 
					 * but there is the case where global counters are big enough but there is a max limitation for every rule type 
					 * AddButton should be hidden, if global counters has not been achieved, when every rule type (for all 16 rules type) 
					 * has achieved the max quantiy.
					 */
					function triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(callback, ruleTypes) {
						
						var ruleTypesCount = Object.keys(ruleTypes).length;
						if (ruleTypesCount<17) { // 16 rule types plus "Manual"
							// Dont hide AddButton cause there is at least  one Rule with no quantity restriction
							return false; 
						}
						
						// We have counters or every ruleType, we can check particular rule contraints
						
						var b = false;
						for (var ruleType in ruleTypes) {
							if (ruleType === "Manual") {continue;}
							if (ruleTypes.hasOwnProperty(ruleType)) {
								  var counter = ruleTypes[ruleType];
								  if (counter) {
									  // This ruleType can accept another rule, so AddButton should be displayed
									  b = counter.count < counter.max;
									  if(b) {
										  break;
									  }
								}
							}
						}
						
						callback.fire(!b);
					};

					function onRequestSuccess(policies) {

						var counters = {
							rules : {
								max : policies.globalLimitations.maxNumberOfRules,
								count : 0
							},
							periods : {
								max : policies.globalLimitations.maxNumberOfPeriods,
								count : 0
							},
							ruleTypes : {},
							periodTypes : {}
						};

						if (policies.globalLimitations.rulesDefinitionTypeLimitations) {
							policies.globalLimitations.rulesDefinitionTypeLimitations
									.forEach(function(limitation) {
										counters.ruleTypes[limitation.ruleType] = {
											max : limitation.value,
											count : 0
										};
									});
						}
						if (policies.globalLimitations.periodsDefinitionTypeLimitations) {
							policies.globalLimitations.periodsDefinitionTypeLimitations
									.forEach(function(limitation) {
										counters.periodTypes[limitation.periodType] = {
											max : limitation.value,
											count : 0
										};
									});
						}
						if (policies.rulesLimitations) {
							policies.rulesLimitations.forEach(function(
									limitation) {
								counters.ruleTypes[limitation.ruleType] = {
									max : limitation.value,
									count : 0
								};
							});
						}

						this.set(counters);

						return this.counters;
					}
					;

					function onAllwaysRequest() {
						this.callbacks.request.fire(false);
					}
					;

					function countRuleTypes(rules, ruleType) {
						return rules.filter(byRuleTypeNotDeleted.bind(
								undefined, ruleType)).length;
					}
					;

					function countPeriodTypes(periods, periodType) {
						return periods.filter(byPeriodTypeNotDeleted.bind(
								undefined, periodType)).length;
					}
					;

					function byRuleTypeNotDeleted(ruleType, rule) {
						return !rule._delete && ruleType === rule.ruleType;
					}
					;

					function byPeriodTypeNotDeleted(periodType, period) {
						return !period._delete
								&& (!period.periodType || periodType === period.periodType);
					}
					;

					return Policy;
				})(jQuery),
				(function($) {
					'use strict';

					// Rules
					function Rules(panelId) {
						this.panelId = panelId;
						this.rules = [];
						this.original = [];

						this.callbacks = {
							request : $.Callbacks(),
							change : $.Callbacks(),
							list : $.Callbacks(),
							added : $.Callbacks(),
							updated : $.Callbacks(),
							removed : $.Callbacks(),
							toggleDeleted : $.Callbacks(),
							periods : $.Callbacks()
						};
					}
					;

					Rules.prototype = {
						on : function(callback, fn) {
							if (this.callbacks[callback]) {
								if (typeof fn === 'function')
									this.callbacks[callback].add(fn);
								if (Array.isArray(fn))
									fn.forEach(this.on.bind(this, callback));
							}

							return this;
						},
						off : function(callback, fn) {
							if (this.callbacks[callback]
									&& typeof fn === 'function') {
								if (typeof fn === 'function')
									this.callbacks[callback].remove(fn);
								if (Array.isArray(fn))
									fn.forEach(this.off.bind(this, callback));
							}

							return this;
						},
						setList : function setRules(rules, originals,
								forceChanged) {
							this.rules = rules;
							this.original = originals;

							this.fireChange(forceChanged);
							this.fireList();
						},
						getList : function getList(forceServer) {
							var dfd = $.Deferred();

							if (forceServer || !this.rules.length) {
								this.request()
										.then(onRequestSuccess.bind(this))
										.then(dfd.resolve);
							} else {
								dfd.resolve(this.rules);
							}

							return dfd.promise();
						},
						find : function find(systemId) {
							return this.getList().then(function(rules) {
								return rules.filter(function(rule) {
									return systemId === rule.systemId;
								})[0];
							});
						},
						byPeriodSystemId : function byPeriodSystemId(
								periodSystemId) {
							return this.rules.filter(ruleNotDeleted).filter(
									rulesByPeriodSystemId.bind(undefined,
											periodSystemId));
						},
						create : function create(attrs) {
							attrs._new = true;
							attrs._sort = this.rules.length;
							var rule = this.rules[this.rules.length] = attrs;

							this.fireChange(true);
							this.callbacks.added.fire(rule);
							
							 // EIC15-2161 fix
							setManualRuleCounter(rules.length); 
							triggerMaximumIfGlobalOrManual( 
							          this.callbacks.maximumRules, 
							          this.counters.rules, 
							          this.counters.ruleTypes["Manual"]);
							triggerMaximumIfEveryRuleTypeHasAchievedMaxQuantity(this.callbacks.maximumRules, this.counters.ruleTypes);
							 // --------------

							return rule;
						},
						update : function update(systemId, attrs) {
							return this
									.find(systemId)
									.then(
											function(prevRule) {

												var index = this.rules
														.indexOf(prevRule);
												var rule = attrs;
												var originalRule = this.original
														.filter(ruleBySystemId
																.bind(
																		undefined,
																		systemId))[0];

												rule._sort = prevRule._sort;

												delete rule._edited;

												if (originalRule
														&& !$.utils
																.compareObjects(
																		rule,
																		originalRule,
																		{
																			deep : false
																		})) {
													rule._edited = true;
												}

												if (!originalRule) {
													rule._new = true;
												}

												this.rules[index] = rule;
												this.fireChange();
												this.callbacks.updated.fire(
														rule, prevRule);

												return rule;
											}.bind(this));
						},
						destroy : function destroy(systemId) {

							return this.find(systemId).then(function(rule) {
								var index = this.rules.indexOf(rule);

								this.rules.splice(index, 1);
								this.fireChange();
								this.callbacks.removed.fire(rule);

								return rule;
							}.bind(this));
						},
						toggleEnabled : function toggleEnabled(systemId,
								enabled) {
							return this.find(systemId).then(function(rule) {
								rule.enabled = enabled;

								return this.update(systemId, rule);
							}.bind(this));
						},
						toggleDelete : function toggleDelete(systemId) {
							return this.find(systemId).then(
									function(rule) {

										(rule._delete) ? delete rule._delete
												: rule._delete = !rule._delete;
										
										 
										
										this.fireChange();
										this.callbacks.toggleDeleted.fire(rule,
												rule._delete);
										
										return rule;
										
									}.bind(this));
							
					
						},
						fireChange : function fireChange(forceChange) {
							var changed = (typeof forceChange === 'undefined' || forceChange === null) ? isChanged(
									this.rules, this.original)
									: forceChange;
							this.hasChange = changed;

							this.callbacks.change.fire(this.hasChange);
						},
						fireList : function fireList() {
							this.callbacks.list.fire(this.rules);
						},
						toServer : function getModified() {
							return this.rules.filter(ruleNotDeleted).map(
									normalize2Server);
						},
						getDeletedIds : function getDeletedIds() {
							return this.rules.filter(ruleDeleted).map(getId)
						},
						restoreOriginals : function() {
							this.setList($.extend(true, [], this.original),
									this.original, false);

							return this.rules;
						},
						request : function request() {
							this.callbacks.request.fire(true);
							return $.getJSON(
									$.app.rootPath + '/admin/rules/'
											+ this.panelId)

							.fail(
									function(jqXHR, textStatus, errorThrown) {

										if (jqXHR.status == "401") {
											window.location = $.app.rootPath
													+ '/unauthorized';
										} else {
											showServerError();
										}
									})

							.always(onAllwaysRequest.bind(this));
						},
						requestDummy : function requestDummy() {
							return $.getJSON($.app.rootPath
									+ '/js/dummy-json/rules.json');
						}
					};

					// private functions
					function onRequestSuccess(data) {
						var rules = data.rules ? data.rules.map(
								preparePresenceMultifield).map(addSort).sort(
								sort) : []; // .filter(isEditable);
						var periods = data.periods ? data.periods.map(addSort)
								.sort(sort) : [];

						periods.forEach(associatePeriod.bind(this, rules));
						this.callbacks.periods.fire(periods, $.extend(true, [],
								periods));
						this.setList(rules, $.extend(true, [], rules), false);

						return this.rules;
					}
					;

					function onAllwaysRequest() {
						this.callbacks.request.fire(false);
					}
					;

					function associatePeriod(rules, period) {
						var periodRules = rules.filter(ruleByPeriod.bind(
								undefined, period))

						if (periodRules.length) {
							period._hasRule = true;
							periodRules.forEach(setPeriod.bind(undefined,
									period));
						}
					}
					;

					function preparePresenceMultifield(rule) {

						var handledRule = $.extend(true, {}, rule);

						if (handledRule.ruleType == 'Presence'
								&& handledRule.groupDeviceIds
								&& handledRule.groupDeviceIds.length > 0) {
							handledRule.firstLocationGroupDeviceId = handledRule.groupDeviceIds[0];
							handledRule.secondLocationGroupDeviceId = handledRule.groupDeviceIds[1]
									|| -1;
							handledRule.thirdLocationGroupDeviceId = handledRule.groupDeviceIds[2]
									|| -1;
						}
						
						if (handledRule.ruleType == "NoActivityDetected" && handledRule.groupDeviceIds
								&& handledRule.groupDeviceIds.length > 0) {
							handledRule.firstLocationGroupDeviceId = handledRule.groupDeviceIds[0];
							handledRule.secondLocationGroupDeviceId = handledRule.groupDeviceIds[1]
							|| -1;
							handledRule.thirdLocationGroupDeviceId = handledRule.groupDeviceIds[2]
							|| -1;
						}

						return handledRule;
					}
					;

					function addSort(rule, i) {
						rule._sort = i;
						return rule;
					}
					;

					function sort(a, b) {
						return a._sort - b._sort;
					}
					;

					function ruleByPeriod(period, rule) {
						return period.systemId === rule.periodSystemId;
					}
					;

					function rulesByPeriodSystemId(periodSystemId, rule) {
						return periodSystemId === rule.periodSystemId;
					}
					;

					function ruleBySystemId(systemId, rule) {
						return systemId === rule.systemId;
					}
					;

					function ruleDeleted(rule) {
						return rule._delete;
					}
					;

					function ruleNotDeleted(rule) {
						return !rule._delete;
					}
					;

					function ruleModified(rule) {
						return !rule._delete && (rule._edited || rule._new);
					}
					;

					function normalize2Server(rule) {
						var normalized = $.extend(true, {}, rule);

						if (normalized._sort)
							delete normalized._sort;
						if (normalized._new) {
							normalized.systemId = '';
							delete normalized._new;
						}
						if (normalized._delete)
							delete normalized._delete;
						if (normalized._edited)
							delete normalized._edited;
						if (normalized.period)
							delete normalized.period;

						if (normalized.firstLocationGroupDeviceId) {
							// Build groupDeviceIds
							normalized.groupDeviceIds = [];
							if (normalized.firstLocationGroupDeviceId && normalized.firstLocationGroupDeviceId != -1) {
								normalized.groupDeviceIds
										.push(normalized.firstLocationGroupDeviceId);
							}
							
							if (!window.isExpress || normalized.ruleType == "NoActivityDetected" || normalized.ruleType == "Presence")
							{
								if (normalized.secondLocationGroupDeviceId && normalized.secondLocationGroupDeviceId != -1) {
									normalized.groupDeviceIds
									.push(normalized.secondLocationGroupDeviceId);
								}
							
								if (normalized.thirdLocationGroupDeviceId && normalized.thirdLocationGroupDeviceId != -1) {
									normalized.groupDeviceIds
									.push(normalized.thirdLocationGroupDeviceId);
								}
							}
							delete normalized.firstLocationGroupDeviceId;
							delete normalized.secondLocationGroupDeviceId;
							delete normalized.thirdLocationGroupDeviceId;
						}

						return normalized;
					}
					;

					function getId(rule) {
						return rule.systemId;
					}
					;

					function setPeriod(period, rule) {
						rule.period = period;
					}
					;

					function isEditable(item) {
						return item.editable;
					}
					;

					function isChanged(rules, originals) {
						var compareSettings = {
							sort : sort,
							deep : true,
							deepSettings : {
								deep : false,
								sort : true
							}
						};

						return !$.utils.compareArrays(rules, originals,
								compareSettings);
					}
					;

					return Rules;
				})(jQuery),
				(function($) {
					'use strict';

					// PERIODS
					function Periods() {
						this.periods = [];
						this.original = [];
						this.hasChange = false;

						this.callbacks = {
							change : $.Callbacks(),
							list : $.Callbacks(),
							added : $.Callbacks(),
							updated : $.Callbacks(),
							removed : $.Callbacks(),
							toggleDeleted : $.Callbacks()
						};
					}
					;

					Periods.prototype = {
						on : function(callback, fn) {
							if (this.callbacks[callback]) {
								if (typeof fn === 'function')
									this.callbacks[callback].add(fn);
								if (Array.isArray(fn))
									fn.forEach(this.on.bind(this, callback));
							}

							return this;
						},
						off : function(callback, fn) {
							if (this.callbacks[callback]
									&& typeof fn === 'function') {
								if (typeof fn === 'function')
									this.callbacks[callback].remove(fn);
								if (Array.isArray(fn))
									fn.forEach(this.off.bind(this, callback));
							}

							return this;
						},
						setList : function setList(periods, originals,
								forceChanged) {
							this.periods = periods;
							this.original = originals;

							this.fireChange(forceChanged);
							this.fireList();
						},
						getList : function getList(forceServer) {
							var dfd = $.Deferred();

							dfd.resolve(this.periods);

							return dfd.promise();
						},
						find : function find(systemId) {
							return this.getList().then(function(periods) {
								return periods.filter(function(period) {
									return systemId === period.systemId;
								})[0];
							});
						},
						newPeriod : function newPeriod() {
							return {
								systemId : uuid.v1(),
								is24Hours : false,
								editable : true
							};
						},
						create : function create(attrs) {
							attrs._new = true;
							attrs._sort = this.periods.length;

							if (/^\d{2}:\d{2}$/.test(attrs.startTime))
								attrs.startTime = '1980-01-01T'
										+ attrs.startTime + ':00';
							if (/^\d{2}:\d{2}$/.test(attrs.endTime))
								attrs.endTime = '1980-01-01T' + attrs.endTime
										+ ':00';

							var period = this.periods[this.periods.length] = attrs;

							this.fireChange(true);
							this.callbacks.added.fire(period);

							return period;
						},
						update : function update(systemId, attrs) {
							return this
									.find(systemId)
									.then(
											function(prevPeriod) {
												var index = this.periods
														.indexOf(prevPeriod);
												var period = attrs;
												var originalPeriod = this.original
														.filter(periodBySystemId
																.bind(
																		undefined,
																		systemId))[0];

												if (/^\d{2}:\d{2}$/
														.test(period.startTime))
													period.startTime = '1980-01-01T'
															+ period.startTime
															+ ':00';
												if (/^\d{2}:\d{2}$/
														.test(period.endTime))
													period.endTime = '1980-01-01T'
															+ period.endTime
															+ ':00';

												period._sort = prevPeriod._sort;
												delete period._edited;

												if (originalPeriod
														&& !$.utils
																.compareObjects(
																		period,
																		originalPeriod)) {
													period._edited = true;
												}

												if (!originalPeriod) {
													period._new = true;
												}

												this.periods[index] = period;
												this.fireChange();
												this.callbacks.updated.fire(
														period, prevPeriod);

												return period;
											}.bind(this));
						},
						destroy : function destroy(systemId) {

							return this.find(systemId).then(function(period) {
								var index = this.periods.indexOf(period);

								this.periods.splice(index, 1);
								this.fireChange();
								this.callbacks.removed.fire(period);

								return period;
							}.bind(this));
						},
						toggleDelete : function toggleDelete(systemId) {
							return this
									.find(systemId)
									.then(
											function(period) {

												(period._delete) ? delete period._delete
														: period._delete = !period._delete;
												this.fireChange();
												this.callbacks.toggleDeleted
														.fire(period,
																period._delete);

												return period;
											}.bind(this));
						},
						fireList : function fireList() {
							this.callbacks.list.fire(this.periods);
						},
						fireChange : function fireChange(forceChange) {
							var changed = (typeof forceChange === 'undefined' || forceChange === null) ? isChanged(
									this.periods, this.original)
									: forceChange;
							this.hasChange = changed;

							this.callbacks.change.fire(this.hasChange);
						},
						toServer : function getModified() {
							return this.periods.filter(notDeleted).map(
									normalize2Server);
						},
						getDeletedIds : function getDeletedIds() {
							return this.periods.filter(deleted).map(getId)
						},
						restoreOriginals : function() {
							this.setList($.extend(true, [], this.original),
									this.original, false);

							return this.periods;
						},
					};

					// private functions
					function getId(period) {
						return period.systemId;
					}
					;

					function periodBySystemId(systemId, period) {
						return systemId === period.systemId;
					}
					;

					function normalize2Server(period) {
						var normalized = $.extend(true, {}, period);

						if (normalized._sort)
							delete normalized._sort;
						if (normalized._new) {
							// normalized.systemId = '';
							delete normalized._new;
						}
						if (normalized._delete)
							delete normalized._delete;
						if (normalized._edited)
							delete normalized._edited;

						return normalized;
					}
					;

					function notDeleted(period) {
						return !period._delete;
					}
					;

					function deleted(period) {
						return period._delete;
					}
					;

					function sort(a, b) {
						return a._sort - b._sort;
					}
					;

					function isChanged(periods, originals) {
						var compareSettings = {
							sort : sort,
							deep : true,
							deepSettings : {
								deep : false,
								sort : true
							}
						};

						return !$.utils.compareArrays(periods, originals,
								compareSettings);
					}
					;

					function dateFromTimeStr(time) {
						var split = time.split(':');
						return new Date(0, 0, 0, split[0], split[1], 0);
					}
					;

					return Periods;
				})(jQuery),
				(function($) {
					'use strict';

					// DEVICES
					function Devices(panelId) {
						this.panelId = panelId;
						this.devices = [];
						this.fetched = false;

						this.callbacks = {
							request : $.Callbacks()
						};
					}
					;

					Devices.prototype = {
						on : function(callback, fn) {
							if (this.callbacks[callback]) {
								if (typeof fn === 'function')
									this.callbacks[callback].add(fn);
								if (Array.isArray(fn))
									fn.forEach(this.on.bind(this, callback));
							}

							return this;
						},
						off : function(callback, fn) {
							if (this.callbacks[callback]
									&& typeof fn === 'function') {
								if (typeof fn === 'function')
									this.callbacks[callback].remove(fn);
								if (Array.isArray(fn))
									fn.forEach(this.off.bind(this, callback));
							}

							return this;
						},
						getList : function(forceServer) {
							var dfd = $.Deferred();

							if (forceServer || !this.fetched) {
								this.request()
										.then(onRequestSuccess.bind(this))
										.then(dfd.resolve);
							} else {
								dfd.resolve(this.devices);
							}

							return dfd.promise();
						},
						request : function request() {
							this.callbacks.request.fire(true);
							return $.getJSON(
									$.app.rootPath + '/admin/rules/'
											+ this.panelId + '/devices')

							.fail(
									function(jqXHR, textStatus, errorThrown) {

										if (jqXHR.status == "401") {
											window.location = $.app.rootPath
													+ '/unauthorized';
										} else {
											showServerError();
										}
									})

							.always(onAllwaysRequest.bind(this));
						}
					};

					// private functions
					function onRequestSuccess(data) {
						this.fetched = true;
						this.callbacks.request.fire(false);
						this.devices = data.devices;

						return this.devices;
					}
					;

					function onAllwaysRequest() {
						this.callbacks.request.fire(false);
					}
					;

					function filterByTypes(types, device) {
						return types.indexOf(device.type) > -1;
					}
					;

					function filterByActivityTypes(activityTypes, device) {
						return activityTypes.indexOf(device.activityType) > -1;
					}
					;

					return Devices;
				})(jQuery),
				(function($) {
					var RULE_TYPES = [ 'OutOfHome', 'Presence', 'Inactivity',
							'Wandering', 'Absent', 'NoActivityDetected',
							'LowNumOfDetections', 'LowNumOfVisits',
							'ShortStay', 'ShortStayBedroom', 'LongStay',
							'ExcessiveNumOfDetections', 'HighNumOfVisits',
							'UnexpectedPresence', 'UnexpectedEntryExit',
							'AtHomeForTooLong',
							'DoorOpen' ];
					var WEEK_DAYS = [ 'Monday', 'Tuesday', 'Wednesday',
							'Thursday', 'Friday', 'Saturday', 'Sunday' ];
					var DEVICE_GROUPS = {
						OTHER_ROOM : {
							id : 0,
							activityType : 'OtherRoom'
						},
						FRIDGE_DOOR : {
							id : 1,
							activityType : 'FridgeDoor'
						},
						FRONT_DOOR : {
							id : 2,
							activityType : 'FrontDoor'
						},
						BEDROOM_SENSOR : {
							id : 3,
							activityType : 'BedroomSensor'
						},
						TOILET_ROOM_SENSOR : {
							id : 4,
							activityType : 'ToiletRoomSensor'
						},
						BATHROOM_SENSOR : {
							id : 5,
							activityType : 'BathroomSensor'
						},
						LIVING_ROOM : {
							id : 6,
							activityType : 'LivingRoom'
						},
						DINING_ROOM : {
							id : 7,
							activityType : 'DiningRoom'
						},
						SMOKE_DETECTOR : {
							id : 8,
							activityType : 'SmokeDetector'
						},
						WATER_LEAKAGE : {
							id : 9,
							activityType : 'WaterLeakage'
						},
						EP : {
							id : 10,
							activityType : 'EP'
						},
						SPBP : {
							id : 11,
							activityType : 'SPBP'
						},
						BATHROOM_COMBINED : {
							id : 20,
							activityType : 'BathroomCombined'
						}
					};

					var delays = [ 0, 1, 5, 10, 15, 20, 30, 45, 60, 90, 120,
							180, 240, 300, 360, 420, 480, 540, 600, 660, 720,
							900, 1080, 1440, 2160, 2880, 4320 ];

					var defaultDurations = [];
					for (var i = 5; i <= 720; i += 5) {
						defaultDurations[defaultDurations.length] = i;
					}
					;
					var defaultDurationsValues = defaultDurations
							.map(minuteValue);

					var unexpectedEntryExitDurations = [];
					for (var i = 5; i <= 1200; i += 5) {
						unexpectedEntryExitDurations[unexpectedEntryExitDurations.length] = i;
					}
					var unexpectedEntryExitDurationsValues = unexpectedEntryExitDurations
							.map(secondValue);

					var sustainedActivityDurations = [];
					for (var i = 1; i <= 240; i++) {
						sustainedActivityDurations[sustainedActivityDurations.length] = i;
					}
					var sustainedActivityDurationsValues = sustainedActivityDurations
							.map(minuteValue);

					var timeValues = [];
					for (var i = 0; i <= 23; i++) {
						for (var j = 0; j <= 45; j += 15) {
							timeValues[timeValues.length] = [ zeroValue(i)
									+ ':' + zeroValue(j) ];
						}
					}

					function zeroValue(value) {
						return (value < 10) ? '0' + value : value;
					}
					;

					var nameField = {
						name : 'name',
						required : true,
						type : 'text',
						validations : {
							maxlength : 25,
							xssPrevention : true,
						}
					};
					var descriptionField = {
						name : 'description',
						required : false,
						type : 'info',
						className : 'readonly textarea'
					};
					var typeField = {
						name : 'ruleType',
						required : true,
						type : 'select'
					};
					var enabledField = {
						name : 'enabled',
						required : false,
						type : 'boolean'
					};
					var delayField = {
						name : 'delay',
						required : true,
						type : 'text',
						className : 'duration',
						attrs : {
							min : 60,
							max : 4320,
							step : 60,
							format : 'd/hh'
						}
					};
					var periodField = {
						name : 'periodSystemId',
						required : true,
						type : 'select',
						className : 'combobox'
					};
					var weekDaysField = {
						name : 'daysOfWeek',
						required : true,
						type : 'checkboxGroup',
						values : WEEK_DAYS.map(weekDayValue)
					};
					var durationField = {
						name : 'duration',
						required : true,
						type : 'text',
						className : 'duration'
					};
					var durationAtHomeField = {
							name : 'delay',
							goal : 'AtHomeForTooLong',
							required : true,
							type : 'text',
							className : 'duration',
							attrs : {
								min : 720,
								max : 43200,
								step : 60,
								format : 'd/hh'
							}
						};
					var awayField = {
						name : 'duration',
						label : I18n.t('rules.form.delay'),
						required : true,
						type : 'text',
						className : 'duration'
					};
					var detectionsField = {
						name : 'maxNumOfDetections',
						required : true,
						type : 'number',
						validations : {
							range : [ 1, 255 ]
						},
						attrs : {
							min : 1
						}
					};
					var minDetectionsField = {
						name : 'maxNumOfDetections',
						label : I18n.t('rules.form.minNumOfDetections'),
						required : true,
						type : 'number',
						validations : {
							range : [ 1, 255 ]
						},
						attrs : {
							min : 1
						}
					};
					var visitsField = {
						name : 'maxNumOfDetections',
						label : I18n.t('rules.form.maxNumOfVisits'),
						required : true,
						type : 'number',
						validations : {
							range : [ 1, 255 ]
						},
						attrs : {
							min : 1
						}
					};
					var minVisitsField = {
						name : 'maxNumOfDetections',
						label : I18n.t('rules.form.minNumOfVisits'),
						required : true,
						type : 'number',
						validations : {
							range : [ 1, 255 ]
						},
						attrs : {
							min : 1
						}
					};
					var groupDeviceField = {
						name : 'isDeviceGroup',
						required : false,
						type : 'boolean'
					};
					var groupAllDoorsField = {
						name : 'groupAllDoors',
						required : false,
						type : 'boolean'
					};
					var groupDoorIdField = {
						name : 'groupDeviceId',
						label : I18n.t('rules.form.groupDoorId'),
						required : true,
						type : 'select'
					};
					var groupDevideIdField = {
						name : 'groupDeviceId',
						required : true,
						type : 'select'
					};
					var specificDevideIdField = {
						name : 'groupDeviceId',
						label : I18n.t('rules.form.specificDeviceId'),
						required : true,
						type : 'select'
					};

					var is24hField = {
						name : 'is24Hours',
						required : false,
						type : 'boolean'
					};

					var startTimeField = {
						name : 'startTime',
						required : true,
						type : 'time',
						className : 'combobox',
						values : $.extend(true, [], timeValues)
					};

					var endTimeField = {
						name : 'endTime',
						required : true,
						type : 'time',
						className : 'combobox',
						values : $.extend(true, [], timeValues)
					};

					var firstLocationField = {
						name : 'firstLocationGroupDeviceId',
						className: 'locs', 
						required : true,
						type : 'select'
					};
					var secondLocationField = {
						name : 'secondLocationGroupDeviceId',
						className: 'locs', 
						required : true,
						type : 'select'
					};
					var thirdLocationField = {
						name : 'thirdLocationGroupDeviceId',
						className: 'locs', 
						required : true,
						type : 'select'
					};

					var RULE_TYPES_SETTINGS = {
						unsetted : {
							fields : [ nameField, typeField, enabledField ]
						},
						OutOfHome : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, durationAtHomeField  ]
						},
						DoorOpen : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField, groupDeviceField,
									specificDevideIdField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							},
							devices : {
								activityTypes : [ 'FrontDoor','FridgeDoor' ],
								onlyDevice : true
							}
						},
						Wandering : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									weekDaysField, awayField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							}
						},
						Presence : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									groupDeviceField, firstLocationField, secondLocationField, thirdLocationField ],
							devices : {
								groups : [ 
										DEVICE_GROUPS.FRIDGE_DOOR,
										DEVICE_GROUPS.FRONT_DOOR,
										DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						ExcessiveNumOfDetections : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									detectionsField, groupDeviceField,
									groupDevideIdField ],
							devices : {
								groups : [
									    DEVICE_GROUPS.FRIDGE_DOOR,
										DEVICE_GROUPS.FRONT_DOOR,
										DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						UnexpectedPresence : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									detectionsField, groupDeviceField,
									groupDevideIdField ],
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						HighNumOfVisits : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField, visitsField,
									groupDeviceField, groupDevideIdField ],
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						LongStay : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField, groupDeviceField,
									groupDevideIdField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							},
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						ShortStay : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField, groupDeviceField,
									groupDevideIdField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							},
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								// DEVICE_GROUPS.BEDROOM_SENSOR,
								DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						ShortStayBedroom : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField, groupDeviceField,
									specificDevideIdField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							},
							devices : {
								activityTypes : [ 'BedroomSensor' ],
								onlyDevice : true
							}
						},
						LowNumOfVisits : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									minVisitsField, groupDeviceField,
									groupDevideIdField ],
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						LowNumOfDetections : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									minDetectionsField, groupDeviceField,
									groupDevideIdField ],
							devices : {
								groups : [ DEVICE_GROUPS.FRONT_DOOR,
										DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						NoActivityDetected : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									groupDeviceField, firstLocationField, secondLocationField, thirdLocationField],
							devices : {
								groups : [ DEVICE_GROUPS.FRONT_DOOR,
										DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						Absent : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									groupDeviceField, groupDevideIdField ],
							devices : {
								groups : [
								// DEVICE_GROUPS.FRONT_DOOR,
								DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						},
						Inactivity : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField ],
							attrs : {
								duration : {
									min : 5,
									max : 720,
									step : 5,
									format : 'hh/mm'
								}
							}
						},
						UnexpectedEntryExit : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									weekDaysField, durationField,
									groupDeviceField, groupAllDoorsField, groupDoorIdField ],
							attrs : {
								duration : {
									min : 60,
									max : 1200,
									step : 60,
									format : 'mm',
									'data-unit' : 'seconds'
								}
							},
							devices : {
								groups : [ DEVICE_GROUPS.FRONT_DOOR ],
								onlyGroup : true
							}
						},
						AtHomeForTooLong : {
							fields : [ nameField, typeField, enabledField,
									descriptionField,
									durationAtHomeField ]
						},
						
						SustainedActivity : {
							fields : [ nameField, typeField, enabledField,
									descriptionField, periodField,
									durationField, groupDeviceField,
									groupDevideIdField ],
							attrs : {
								duration : {
									min : 1,
									max : 240,
									step : 1,
									format : 'hh/mm'
								}
							},
							devices : {
								groups : [ DEVICE_GROUPS.FRONT_DOOR,
										DEVICE_GROUPS.BEDROOM_SENSOR,
										DEVICE_GROUPS.TOILET_ROOM_SENSOR,
										DEVICE_GROUPS.BATHROOM_SENSOR,
										DEVICE_GROUPS.LIVING_ROOM,
										DEVICE_GROUPS.DINING_ROOM,
										DEVICE_GROUPS.OTHER_ROOM ],
								onlyGroup : true
							}
						}
					};

					// PUBLIC FUNCTIONS

					function fieldsForType(policies, ruleType, periods,
							devices, groupDevices) {
						
						modifyRulesIfIsExpress();
						var settings = RULE_TYPES_SETTINGS[ruleType];

						var fields = settings.fields;
						var values = settings.values;
						var attrs = settings.attrs;
						var labels = settings.labels;

						fields
								.forEach(function(field) {
									if (field.name === 'delay') {
										field.name = 'homeTime';
									};
									if (field.name === 'ruleType')
										field.values = RULE_TYPES.filter(
												validRuleTypes).map(
												ruleTypeValue);
									if (field.name === 'description')
										field.defaultValue = I18n
												.t('rules.descriptions.'
														+ ruleType);
									if (field.name === 'periodSystemId')
										field.values = periods.map(periodValue);
									if (field.name === 'groupDeviceId'
											|| field.name === 'firstLocationGroupDeviceId'
											|| field.name === 'secondLocationGroupDeviceId'
											|| field.name === 'thirdLocationGroupDeviceId') {
										field.values = devicesValues(devices,
												settings, groupDevices);
										if (field.name === 'firstLocationGroupDeviceId'
												|| field.name === 'secondLocationGroupDeviceId'
												|| field.name === 'thirdLocationGroupDeviceId') {

											field.values
													.unshift([
															-1,
															I18n
																	.t('rules.form.none') ]); // -1:
																								// Is
																								// the
																								// value
																								// for
																								// none
																								// and
																								// should
																								// be
																								// excluded
																								// before
																								// sending
																								// to
																								// server
										}

										if (field.values
												&& field.values.length > 0
												&& settings.devices.onlyGroup
												&& settings.devices.groups.length === 1) {
											field.defaultValue = field.values[0][0];
										} else {
											delete field.defaultValue;
										}
									}
									if (field.name === 'isDeviceGroup') {
										if (settings.devices.onlyDevice
												|| settings.devices.onlyGroup) {
											field.type = 'hidden';
											field.defaultValue = settings.devices.onlyGroup || false;
										} else {
											field.type = 'boolean';
											delete field.defaultValue;
										}
									}

									if (values && values[field.name])
										field.values = values[field.name];
									if (attrs && attrs[field.name])
										field.attrs = attrs[field.name];
								});

						return fields;

						function validRuleTypes(type) {
							if (type === ruleType)
								return true;
							if (policies.isMaxRuleType(type))
								return false;
							
							var hasFrontDoorDevice = false;

							  for(var i=0; i<devices.length; i++){
							    var activityType = devices[i].activityType;
							    var deviceType = devices[i].deviceType;
							    if(activityType == 'FrontDoor' && deviceType == 'MagneticContact'){
							    	hasFrontDoorDevice = true;
							    	break;
							    }
							  }
							

							if (type == 'AtHomeForTooLong' && hasFrontDoorDevice == false) {
								return false;
							}
							 

							modifyRulesIfIsExpress();
							var typeDevices = RULE_TYPES_SETTINGS[type].devices;

							if (typeDevices) {
								if (typeDevices.onlyGroup) {
									var acceptActivityTypes = typeDevices.groups
											.map(function(d) {
												return d.activityType;
											});

									return devices.some(function(d) {
										return acceptActivityTypes
												.indexOf(d.activityType) >= 0;
									});
								}
								if (typeDevices.onlyDevice) {
									var acceptActivityTypes = typeDevices.activityTypes;

									return devices.some(function(d) {
										return acceptActivityTypes
												.indexOf(d.activityType) >= 0;
									});
								}
							}

							return true;
						}
						;
					}
					;

					function fieldsForPeriod(period) {
						var fields = [ nameField, is24hField ];

						if (!period.is24Hours) {
							fields.splice(fields.length, 0, startTimeField,
									endTimeField);

							var startTime = period.startTime ? period.startTime
									.split('T')[1].split(':').splice(0, 2)
									.join(':') : null;
							var endTimeValues = fields[3].values;

							endTimeValues.forEach(function(valArr, i) {
								var value = valArr[0];
								endTimeValues[i] = value === startTime ? [
										value, value, {
											disabled : true
										} ] : [ value ];
							});

							var endTime = (period.endTime) ? period.endTime
									.split('T')[1].split(':').splice(0, 2)
									.join(':') : null;
							var startTimeValues = fields[2].values;

							startTimeValues.forEach(function(valArr, i) {
								var value = valArr[0];
								startTimeValues[i] = value === endTime ? [
										value, value, {
											disabled : true
										} ] : [ value ];
							});
						}

						return fields;
					}
					;

					function mapForm($form) {
						var model = {};

						$form.serializeArray().forEach(function mapValue(item) {
							var name = item.name;
							var value = item.value;

							if (/^\d+$/.test(value))
								value = parseInt(value);
							if (/^true|false$/.test(value))
								value = value === 'true';

							if (/\[\]$/.test(name)) {
								var name = name.replace(/\[\]$/, '');
								if (!model[name])
									model[name] = [];
								if (!$.utils.isBlank(value))
									model[name].push(value);
							} else {
								model[name] = value;
							}
						});

						return model;
					}
					;

					function modifyRulesIfIsExpress()
					{
						if (window.isExpress == true)
						{
							RULE_TYPES_SETTINGS.Presence.fields = [ nameField, typeField, enabledField,
								descriptionField, periodField,
								groupDeviceField, firstLocationField, secondLocationField, thirdLocationField];
						}
					}
					// PRIVATE FUNCTIONS

					function devicesValues(devices, settings, groupDevices) {
						var conf = settings.devices;
						var groupDevices = groupDevices || conf.onlyGroup;

						if (groupDevices) {
							var activityTypes = devices.map(function(d) {
								return d.activityType;
							});
							return conf.groups.filter(
									devicesByActivityTypes.bind(undefined,
											activityTypes)).map(
									deviceGroupValue);
						} else {
							return devices.filter(
									devicesByActivityTypes.bind(undefined,
											conf.activityTypes)).map(
									deviceValue);
							;
						}

						function devicesByActivityTypes(activityTypes, device) {
							return activityTypes.indexOf(device.activityType) > -1;
						}
						;
					}
					;

					function deviceValue(device) {
						return [ device.deviceId, device.label ];
					}
					;

					function deviceGroupValue(group) {
						return [
								group.id,
								I18n.t('devices.activityTypes.'
										+ group.activityType) ];
					}

					function ruleTypeValue(ruleType) {
						return [ ruleType,
								I18n.t('rules.ruleTypes.' + ruleType) ];
					}
					;

					function periodValue(period) {
						var helpText = period.is24Hours ? I18n
								.t('times.all_day') : timeStr(period.startTime)
								+ ' - ' + timeStr(period.endTime);
						return [ period.systemId, period.name, {
							'data-helptext' : helpText
						} ];
					}
					;

					function weekDayValue(weekDay) {
						return [ weekDay,
								I18n.t('weekdays.' + weekDay.toLowerCase()) ];
					}

					function secondValue(seconds) {
						var text = legibleSeconds(seconds);
						var attrs = {
							'data-helptext' : seconds + ' '
									+ I18n.t('times.seconds')
						};

						return [ seconds, text, attrs ];
					}
					;

					function minuteValue(minutes) {
						var text = legibleMinutes(minutes);
						var attrs = {
							'data-helptext' : minutes + ' '
									+ I18n.t('times.minutes')
						};

						return [ minutes, text, attrs ];
					}
					;

					function timeStr(dateTime) {
						var time = dateTime.split('T')[1].split(':');
						var hours = time[0];
						var minutes = time[1];

						return hours + ':' + minutes;
					}
					;

					function legibleSeconds(seconds) {
						if (seconds < 60) {
							return seconds + ' ' + I18n.t('times.seconds');
						} else {
							return legibleMinutes(seconds / 60, seconds % 60);
						}
					}
					;

					function legibleMinutes(minutes, restSeconds) {
						if (minutes < 60) {
							return Math.floor(minutes)
									+ ' '
									+ I18n.t('times.minutes')
									+ (restSeconds > 0 ? ' ' + restSeconds
											+ ' ' + I18n.t('times.seconds')
											: '');
						} else {
							return legibleHours(minutes / 60, minutes % 60);
						}
					}
					;

					function legibleHours(hours, restMinutes) {
						if (hours < 24) {
							return Math.floor(hours)
									+ ' '
									+ I18n.t('times.hours')
									+ (restMinutes > 0 ? ' ' + restMinutes
											+ ' ' + I18n.t('times.minutes')
											: '');
						} else {
							return legibleDays(hours / 24, hours % 24);
						}
					}
					;

					function legibleDays(days, restHours) {
						return Math.floor(days)
								+ ' '
								+ I18n.t('times.days')
								+ (restHours > 0 ? ' ' + restHours + ' '
										+ I18n.t('times.hours') : '');
					}
					;

					return {
						 fieldsForType : fieldsForType ,
						fieldsForPeriod : fieldsForPeriod,
						mapForm : mapForm
					};

				})(jQuery),
				(function($) {
					'use strict';

					// tpls
					function ruleItemTpl(rule) {
						var isNew = rule._new || false;
						var rowClass = rule._delete ? 'class="marked-delete"'
								: isNew || rule._edited ? 'class="added-item"'
										: '';
						var periodName = rule.period ? rule.period.name : '';
						
						
						

						return [
								'<tr id="rule-' + rule.systemId + '" '
										+ rowClass + '>',
								'<td class="grid_3">' + rule.name + '</td>',
								'<td class="grid_3">'
										+ ruleTypeTpl(rule.ruleType) + '</td>',
								'<td class="grid_2">' + periodName + '</td>',
								'<td class="grid_2 center">'
										+ enabledCheckTpl(rule) + '</td>',
								'<td class="grid_2 center">'
										+ actionsTpl(rule, isNew) + '</td>',
								'</tr>' ].join('');
					}
					;

					function periodItemTpl(period) {
						var isNew = period._new || false;
						var rowClass = period._delete ? 'class="marked-delete"'
								: isNew || period._edited ? 'class="added-item"'
										: '';
					
						
						return [
								'<tr id="period-' + period.systemId + '"'
										+ rowClass + '>',
								'<td class="grid_5">' + period.name + '</td>',
								'<td class="grid_5 center">'
										+ periodTimeTpl(period) + '</td>',
								'<td class="grid_2 center">'
										+ periodsActionsTpl(period, isNew)
										+ '</td>', '</tr>' ].join('');
					}
					;

					function ruleTypeTpl(ruleType) {
						return [
								'<strong>',
								'<img class="icon rule-type-icon" src="'
										+ $.app.rootPath
										+ '/stylesheets/svg/rule-types-icons/'
										+ ruleType.toLowerCase() + '.svg"/>',
								I18n.t('rules.ruleTypes.' + ruleType),
								'</strong>' ].join('');
					}
					;

					function periodTimeTpl(period) {
						if (period.is24Hours) {
							return I18n.t('periods.form.is24Hours');
						} else {
							return timeTpl(period.startTime) + '/'
									+ timeTpl(period.endTime);
						}
					}
					;

					function enabledCheckTpl(rule) {
						var checked = rule.enabled && !rule._delete ? 'checked'
								: '';
						var id = 'toggle-enabled-' + rule.systemId;
						return [
								'<label class="switch" for="' + id + '">',
								'<input id='
										+ id
										+ ' type="checkbox" '
										+ checked
										+ ' data-toggle-enabled data-system-id="'
										+ rule.systemId + '" />',
								'<span class="switch-handle"></span>',
								'</button>' ].join('');
					}
					;

					function enabledIconTpl(enabled) {
						var iconClass = enabled ? 'enabled' : 'disabled';

						return '<span class="icon_' + iconClass + '"></span>';
					}
					;

					function actionsTpl(rule, isNew) {
						var systemId = rule.systemId;
						var deleted = rule._delete;

						if (deleted) {
							return '<button class="btn small grey" data-toggle-delete data-system-id="'
									+ systemId
									+ '"><span class="icon undo"></span></button>'
						} else {
							var dataAction = isNew ? 'data-destroy'
									: 'data-toggle-delete';
							return [
									'<button class="btn small grey_special" data-edit data-system-id="'
											+ systemId
											+ '"><span class="icon edit"></span></button>',
									'<button class="btn small grey_special left_separation" '
											+ dataAction
											+ ' data-system-id="'
											+ systemId
											+ '"><span class="icon delete"></span></button>' ]
									.join('');
						}
					}
					;

					function periodsActionsTpl(period, isNew) {
						var systemId = period.systemId;
						var deleted = period._delete;

						if (deleted) {
							return '<button class="btn small grey" data-toggle-delete data-system-id="'
									+ systemId
									+ '"><span class="icon undo"></span></button>'
						} else {
							var dataAction = isNew ? 'data-destroy'
									: 'data-toggle-delete';
							var disableDelete = period._hasRule ? 'disabled'
									: '';

							return [
									'<button class="btn small grey_special" data-edit data-system-id="'
											+ systemId
											+ '"><span class="icon edit"></span></button>',
									'<button class="btn small grey_special left_separation" '
											+ dataAction
											+ ' data-system-id="'
											+ systemId
											+ '" '
											+ disableDelete
											+ '><span class="icon delete"></span></button>' ]
									.join('');
						}
					}
					;

					function timeTpl(dateTime) {
						if (!dateTime)
							return '-';

						var time = dateTime.split('T')[1].split(':');
						var hours = time[0];
						var minutes = time[1];

						var originalTime = hours + ':' + minutes;
						var formattedTime = formatTime(originalTime,
								$.app.timeFormatter);

						return formattedTime;
					}
					;

					function formRuleTpl(rule, fields, edit) {
						var translationScope = 'rules.form';

						return [
								'<form id="rule-form" data-validate>',
								headerModal(edit, rule.name, translationScope),
								'<article class="cf">',
								'<ul class="form-list grid_16">',
								formListTpl(rule, fields, translationScope),
								'</ul>',
								'<input type="hidden" name="systemId" value="'
										+ rule.systemId + '" />',
								'<input type="hidden" name="editable" value="'
										+ rule.editable + '" />', '</article>',
								footerModal(translationScope), '</form>' ]
								.join('');
					}
					;

					function formPeriodTpl(period, fields, edit) {
						var translationScope = 'periods.form';

						return [
								'<form id="period-form" data-validate>',
								headerModal(edit, period.name, translationScope),
								'<article class="cf">',
								'<ul class="form-list grid_16">',
								formListTpl(period, fields, translationScope),
								'</ul>',
								'<input type="hidden" name="systemId" value="'
										+ period.systemId + '" />',
								'<input type="hidden" name="editable" value="'
										+ period.editable + '" />',
								'</article>', footerModal(translationScope),
								'</form>' ].join('');
					}
					;

					function headerModal(edit, name, translationScope) {
						return [
								'<header class="header_modal">',
								'<h1 class="section_title">',
								edit ? I18n.t(translationScope + '.edit_title')
										+ ' ' + name : I18n.t(translationScope
										+ '.new_title'), '</h1>', '</header>' ]
								.join('');
					}
					;

					function footerModal(translationScope) {
						return [
								'<footer class="footer_modal">',
								'<button type="submit" class="btn blue confirm_button">'
										+ I18n.t(translationScope + '.commit')
										+ '</button> ',
								'<button type="button" class="btn mixed" data-rel="close">'
										+ I18n.t(translationScope + '.cancel')
										+ '</button>', '</footer>' ].join('')
					}
					;

					function formListTpl(rule, fields, translationScope) {
						return fields.map(
								function renderGroup(field) {
									return fieldGroupTpl(field, rule,
											translationScope);
								}).join('');
					}
					;

					function fieldGroupTpl(field, item, translationScope) {

						if (field.type === 'hidden') {
							return '<li class="hidden">'
									+ fieldTpl(field, item[field.name],
											translationScope) + '</li>';
						}

						var content = field.type === 'checkbox'
								|| field.type === 'boolean' ? [
								fieldTpl(field, item[field.name],
										translationScope),
								labelTpl(field, translationScope) ].join(' ')
								: [
										labelTpl(field, translationScope),
										fieldTpl(field, item[field.name],
												translationScope) ].join('');
						return [ '<li>', content, '</li>' ].join('');
					}

					function labelTpl(field, translationScope) {
						var name = field.name;
						var label = field.label
								|| I18n.t(translationScope + '.' + name);

						return '<label for="' + name + '">' + label
								+ '</label>';
					}

					function fieldTpl(field, value, translationScope) {
						var value = valueOrBlank(value, field.defaultValue);

						switch (field.type) {
						case 'textarea':
							return '<textarea ' + fieldAttrs(field) + '>'
									+ value + '</textarea>';
						case 'info':
							return '<div ' + fieldAttrs(field) + '>' + value
									+ '</div>';
						case 'time':
							if (!$.utils.isBlank(value)) {
								var time = value.split('T')[1].split(':');
								value = time[0] + ':' + time[1];
							}
						case 'select':
							var arr = ['<select ' + fieldAttrs(field) + '>',optionsForField(field.values, value,
									field.type), '</select>' ];
							if (!(field.className && field.className === 'locs')) {
								arr.splice(1, 0, '<option disabled selected value>&nbsp;</option>');
							}
							return arr.join('');
						case 'boolean':
							var checked = value.toString() === 'true' ? 'checked'
									: '';

							return [
									'<input type="hidden" name="' + field.name
											+ '" value="false" />',
									'<input type="checkbox" '
											+ fieldAttrs(field)
											+ ' value="true" ' + checked
											+ ' />' ].join('');
						case 'checkboxGroup':
							if (field.values) {
								return [
										'<ul class="cf checkboxgroup">',
										'<input type="hidden" name="'
												+ field.name + '[]" />',
										checkboxForGroup(field, value), '</ul>' ]
										.join('');
							}
							return '';
						default:
							return '<input type="' + field.type + '"'
									+ fieldAttrs(field) + ' value="' + value
									+ '" />';
						}
					}
					;

					function optionsForField(values, fieldValue, fieldType) {
						if (values) {

							return values
									.map(
											function renderOption(option) {
												if (Array.isArray(option)) {
													var value = option[0];
													var text = option.length > 1 ? option[1]
															: value;
													var selected = fieldValue === value ? 'selected'
															: '';
													var attrs = '';

													if (fieldType == "time") {
														text = formatTime(
																text,
																$.app.timeFormatter);
													}

													if (option.length >= 3) {
														var optAttrs = option[2];
														attrs = Object
																.keys(optAttrs)
																.map(
																		function(
																				key) {
																			return key
																					+ '="'
																					+ optAttrs[key]
																					+ '"';
																		})
																.join(' ');
													}

													return '<option value="'
															+ value + '" '
															+ selected + ' '
															+ attrs + '>'
															+ text
															+ '</option>';
												} else {
													return [
															'<optgroup label="'
																	+ option.title
																	+ '">',
															optionsForField(
																	option.values,
																	fieldValue),
															'</optgroup>' ]
															.join('');
												}
											}).join('');
						}
						return '';
					}

					function checkboxForGroup(field, fieldValue) {
						if (field.values) {
							var name = field.name;
							var requiredAttr = field.required ? 'required' : '';

							return field.values
									.map(
											function renderCheckbox(checkbox) {
												var value = checkbox[0];
												var text = checkbox.length > 1 ? checkbox[1]
														: value;
												var id = name + value;
												var checked = (typeof fieldValue === 'string' && value === fieldValue)
														|| (Array
																.isArray(fieldValue) && fieldValue
																.indexOf(value) > -1);
												var checkedAttr = checked ? 'checked'
														: '';

												return [
														'<li class="grid_3">',
														'<input type="checkbox" id="'
																+ id
																+ '" name="'
																+ name
																+ '[]" value="'
																+ value + '" '
																+ checkedAttr
																+ ' '
																+ requiredAttr
																+ ' /> ',
														'<label for="' + id
																+ '">' + text
																+ '</label>',
														'</li>' ].join('');
											}).join('');
						}
						return '';
					}

					function fieldAttrs(field) {
						var className = field.className;
						var required = field.required;
						var validations = field.validations;
						var fieldAttrs = field.attrs;

						var attrs = [ 'id="' + field.name + '"',
								'name="' + field.name + '"' ];

						if (className) {
							attrs[attrs.length] = 'class="' + className + '"'
						}

						if (required) {
							attrs[attrs.length] = 'required';
						}

						if (validations) {
							attrs = attrs.concat(Object.keys(validations).map(
									function(key) {
										return key + '="' + validations[key]
												+ '"';
									}));
						}

						if (fieldAttrs) {
							attrs = attrs.concat(Object.keys(fieldAttrs).map(
									function(key) {
										return key + '="' + fieldAttrs[key]
												+ '"';
									}));
						}

						return attrs.join(' ');
					}

					function valueOrBlank(value, defaultValue) {
						var defaultValue = defaultValue || '';
						return typeof value !== 'undefined' && value !== null
								&& value !== '' ? value : defaultValue;
					}

					function confirmExitModalTpl(exitLink) {
						return [
								'<header class="header_modal">',
								'<h1 class="section_title">',
								I18n.t('rules.exit.title'),
								'</h1>',
								'</header>',
								'<article>',
								'<p>' + I18n.t('rules.exit.message') + '</p>',
								'</article>',
								'<footer class="footer_modal">',
								'<a href="' + exitLink + '" class="btn mixed">'
										+ I18n.t('buttons.accept') + '</a> ',
								'<button type="button" class="btn mixed" data-rel="close">'
										+ I18n.t('buttons.cancel')
										+ '</button>', '</footer>' ].join('');
					}
					;
					
					function errorTpl(title, message) {
						return [
								'<header class="header_modal">',
								'<h1 class="section_title">',
								title,
								'</h1>',
								'</header>',
								'<article>',
								'<p>' + message
										+ '</p>',
								'</article>',
								'<footer class="footer_modal">',
								'<button type="button" class="btn blue" data-rel="close">'
										+ I18n.t('buttons.ok') + '</button>',
								'</footer>' ].join('');
					}
					
					function showMissingDaysOfWeekErrorTpl() {
						return errorTpl(I18n.t('showMissingDaysOfWeekError.title'), I18n.t('showMissingDaysOfWeekError.message'));
					}
					
					function rulesMaxQuantityErrorTpl() {
						return errorTpl(I18n.t('rulesMaxQuantityError.title'), I18n.t('rulesMaxQuantityError.message'));
					}
					
					function noLocationErrorTpl() {
						return errorTpl(I18n.t('noLocationError.title'), I18n.t('noLocationError.message'));
					}
					
					function specificRuleMaxQuantityErrorTpl() {
						return errorTpl(I18n.t('specificRuleMaxQuantityError.title'), I18n.t('specificRuleMaxQuantityError.message'));
					}

					function validationErrorTpl() {
						return errorTpl(I18n.t('validationError.title'), I18n.t('validationError.message'));
					}
					;
					
					function xssErrorTpl() {
						return errorTpl(I18n.t('xssError.title'), I18n.t('xssError.message'));
					}
					;

					function serverErrorTpl() {
						return errorTpl(I18n.t('serverError.title'), I18n.t('serverError.message'));
					}
					;
					
				
					
					return {
						ruleItem : ruleItemTpl,
						periodItem : periodItemTpl,
						formRule : formRuleTpl,
						formPeriod : formPeriodTpl,
						formList : formListTpl,
						confirmExitModal : confirmExitModalTpl,
						serverError : serverErrorTpl,
						validationError : validationErrorTpl,
						specificRuleMaxQuantityError: specificRuleMaxQuantityErrorTpl,
						rulesMaxQuantityError: rulesMaxQuantityErrorTpl,
						showMissingDaysOfWeekError : showMissingDaysOfWeekErrorTpl,
						noLocationError: noLocationErrorTpl,
						xssError: xssErrorTpl
					};
				})(jQuery));

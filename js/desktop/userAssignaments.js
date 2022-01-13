$(function ($) {
	var $usersAssignaments = $('#users_assignaments'); 
	if ($usersAssignaments.length > 0) {		
		$(document).on('ajax:success', '.assign, .unassign', function (event, data, textStatus, jQXhr) {
			var $link = $(this),
				$row  = $(data);
			
			$link.qtip('destroy').parents('tr').replaceWith($row);
			$row.find('[data-hastip]').qtip({ position: { my: 'bottom center', at: 'top center', viewport: $(window) } });
		});
		
		$(document).on('ajax:error', '.assign, .unassign', function (event, data, textStatus, jQXhr) {
			var modal = $('#user-assignment-modal');
			if (!modal.length) {
				modal = $('#system-error-modal').clone().removeClass('keep-active').attr('id', 'user-assignment-modal').appendTo('body');
				modal.find('footer button').attr('data-rel', 'close');
			}
			modal.find('article h1').html(data.responseText);
			modal.addClass('active');
		});
		
		var $newCaregiverBtn = $usersAssignaments.find('#new-caregiver-btn');
		$newCaregiverBtn.click(function(event) {
			var href = $newCaregiverBtn.attr('href');
			if ($newCaregiverBtn.data('no-master')){
				href = href + '?nomaster';
			} else if ($newCaregiverBtn.data('no-standard')){
				href = href + '?nostandard';
			}
			$newCaregiverBtn.attr('href', href);
		});
		
		// DISABLE BUTTONS ON MAX OR MIN ASSIGNED CAREGIVER TYPE
		(function (){
			var $assignCaregiverBtns = $usersAssignaments.find('.assign_caregiver_btn a');
//			var MIN_CAREGIVER_MASTER   = 1; // hardcoded min caregiver master
			var MAX_CAREGIVER_MASTER   = $usersAssignaments.data('max-caregiver-master'); 
			var MAX_CAREGIVER_STANDARD = $usersAssignaments.data('max-caregiver-standard');
			var caregiversCount = {};
			
			if ( MAX_CAREGIVER_MASTER > -1 ) {
				caregiversCount.master = $usersAssignaments.find('.icon.assigned.master').length;
				$(document).on('ajax:success', '.assign[data-assign-master]', addMaster);
				$(document).on('ajax:success', '.unassign[data-unassign-master]', removeMaster);
			}
			if ( MAX_CAREGIVER_STANDARD > -1 ) {
				caregiversCount.standard= $usersAssignaments.find('.icon.assigned.standard').length;
				$(document).on('ajax:success', '.assign[data-assign-standard]', addStandard);
				$(document).on('ajax:success', '.unassign[data-unassign-standard]', removeStandard);
			}
			
			checkCaregiversCount();
			
			function addMaster() { add('master'); };
			function removeMaster() { remove('master'); };
			function addStandard() { add('standard'); };
			function removeStandard() { remove('standard') };
			function add(type) {
				caregiversCount[type]++;
				checkCaregiversCount();
			};
			function remove(type) {
				caregiversCount[type]--;
				checkCaregiversCount();
			};
			function checkCaregiversCount() {
				var maxMaster, maxStandard;
				if ( MAX_CAREGIVER_MASTER > -1 ) {
					maxMaster = caregiversCount.master >= MAX_CAREGIVER_MASTER;
					$usersAssignaments.find('.assign[data-assign-master]').attr('disabled', maxMaster);
//					$usersAssignaments.find('.unassign[data-unassign-master]').attr('disabled', caregiversCount.master <= MIN_CAREGIVER_MASTER);
					$newCaregiverBtn.data('no-master', maxMaster);
				}
				if ( MAX_CAREGIVER_STANDARD > -1 ) {
					maxStandard = caregiversCount.standard >= MAX_CAREGIVER_STANDARD;
					$usersAssignaments.find('.assign[data-assign-standard]').attr('disabled', maxStandard);
					$newCaregiverBtn.data('no-standard', maxStandard);
				}
				$assignCaregiverBtns.attr('disabled', maxMaster && maxStandard);
			};
			
			$(document).on('modal:show', '.assign-existing-caregiver', function(event, data){
				if ( MAX_CAREGIVER_MASTER > -1 && caregiversCount.master >= MAX_CAREGIVER_MASTER) {		
					$("#caregiver_type_MASTER").attr('disabled', true);
				}
				if ( MAX_CAREGIVER_STANDARD > -1 && caregiversCount.standard >= MAX_CAREGIVER_STANDARD) {
					$("#caregiver_type_STANDARD").attr('disabled', true);
				}
			});
		})();
	}
});

$(function ($) {
	if ($('#only_assigned').length > 0) {
		
		$(document).on('click', '#only_assigned', function (event) {checkAssigned();});			
		$('#tabMain').on('table:added_page',function(event){checkAssigned();});
	

		function checkAssigned() {
			var chk=$('#only_assigned');
			if (chk.length > 0) {
				if (chk.is(':checked')){					
					$('.not-assigned').closest("tr").hide();
				}else{
					$('.not-assigned').closest("tr").show();
				}
				if (("icon alerts").length<20){
					$('#pagination-list-spinner').hide();
				}else{
					$('#pagination-list-spinner').show();
				}
				$.app.scroll.resize();
			}
		}
	}
});
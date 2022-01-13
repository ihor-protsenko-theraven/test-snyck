/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : English - Great Britain
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['en'] = scope.translations['en'] || {};

          // Module              : Alerts
          scope.translations['en'].alerts= {
                    home : {
                              in_home : 'User At Home',
                              not_at_home : 'Not At Home',
                    },
                    popup : {
                              error401 : {
                                        message : 'Your session is no longer valid. Please login again',
                                        title : 'Invalid session'
                              },
                              detected : 'Detected:',
                              handle_it_later : 'HANDLE IT LATER',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Status:',
                              statuses : {
                                        CLOSED : 'CLOSED',
                                        IN_PROGRESS : 'IN PROGRESS',
                                        NEW : 'NEW',
                                        VIEWED : 'VIEWED',
                              },
                              titles : {
                                        alarm : 'ALARM',
                                        photos : 'NEW PHOTOS',
                              },
                              updated : 'Updated:',
                              view_event : 'VIEW EVENT',
                    },
          };

          // Module              : activity index
          scope.translations['en'].activity_index= {
                    first_activity : 'First act',
                    first_activity_tip : 'First activity of the day',
                    last_activity : 'Last act.',
                    last_activity_tip : 'Last activity of the day',
                    total_activities : 'Total activities',
          };

          // Module              : JsMobile Other Items
          scope.translations['en'].buttons= {
                    close : 'Close',
          };


          // Module              : monthly report
          scope.translations['en'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Bathroom',
                              BEDROOM_SENSOR : 'Bedroom',
                              DINING_ROOM : 'Dining room',
                              FRIDGE_DOOR : 'Meal',
                              FRONT_DOOR : 'Out of home',
                              LIVING_ROOM : 'Living room',
                              OTHER_ROOM : 'Other room',
                              TOILET_ROOM_SENSOR : 'Restroom',
                              unknown : 'Unknown',
                    },
                    activity : 'activity',
          };
          scope.translations['en'].forgot = {
            ok_message : 'We will email you a link to reset the password. Open the link and follow the instructions.<br>If you don’t receive the email within a few minutes, contact support.',
            error_message : 'Sorry, there was a problem on the system.Please try again later.',
        }

        scope.translations['en'].validations = {
          passwordstrength : 'The password is not enough secure. Please, check it and try again.',
          passwordhistory : 'This password has been used recently. Please, introduce a different one.',
          passparam_wrong : 'Some of the parameters are wrong. Please, check it and try again.',
          servererror : 'Sorry, there was a problem on the system.Please try again later.',
          passwordchanged: 'The password has been updated.',
          required : 'This field is required',
      }

})(this);

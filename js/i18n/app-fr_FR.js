/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : French
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['fr'] = scope.translations['fr'] || {};

          // Module              : Alerts
          scope.translations['fr'].alerts= {
                    home : {
                              in_home : 'alerts.home.in_home',
                              not_at_home : 'alerts.home.not_at_home',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'alerts.popup.detected',
                              handle_it_later : 'alerts.popup.handle_it_later',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'alerts.popup.status',
                              statuses : {
                                        CLOSED : 'alerts.popup.statuses.CLOSED',
                                        IN_PROGRESS : 'alerts.popup.statuses.IN_PROGRESS',
                                        NEW : 'alerts.popup.statuses.NEW',
                                        VIEWED : 'alerts.popup.statuses.VIEWED',
                              },
                              titles : {
                                        alarm : 'alerts.popup.titles.alarm',
                                        photos : 'alerts.popup.titles.photos',
                              },
                              updated : 'alerts.popup.updated',
                              view_event : 'alerts.popup.view_event',
                    },
          };

          // Module              : activity index
          scope.translations['fr'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'activity_index.total_activities',
          };

          // Module              : JsMobile Other Items
          scope.translations['fr'].buttons= {
                    close : 'buttons.close',
          };
          scope.translations['fr'].validations= {
                    required : 'validations.required',
          };

          // Module              : monthly report
          scope.translations['fr'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'monthly_report.activities.BATHROOM_SENSOR',
                              BEDROOM_SENSOR : 'monthly_report.activities.BEDROOM_SENSOR',
                              DINING_ROOM : 'monthly_report.activities.DINING_ROOM',
                              FRIDGE_DOOR : 'monthly_report.activities.FRIDGE_DOOR',
                              FRONT_DOOR : 'monthly_report.activities.FRONT_DOOR',
                              LIVING_ROOM : 'monthly_report.activities.LIVING_ROOM',
                              OTHER_ROOM : 'monthly_report.activities.OTHER_ROOM',
                              TOILET_ROOM_SENSOR : 'monthly_report.activities.TOILET_ROOM_SENSOR',
                              unknown : 'monthly_report.activities.unknown',
                    },
                    activity : 'monthly_report.activity',
          };
})(this);

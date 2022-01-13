/*##################################################
# Product             : Care @ Home
# Version             : 2.2.8
# Client              : Default
# Date                : 13/02/2017
# Translation Versión : Third version of translations for Care @ Home version 2.2.8
# Language            : English - United Kingdom
# Parse               : JavaScript
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['en'] = scope.translations['en'] || {};

          // Module              : activity index
          scope.translations['en'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'activity_index.total_activities',
          };

          // Module              : Alerts
          scope.translations['en'].alerts= {
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
                              page_load_failed: {
                                title: 'Failed Loading Page',
                                message: 'Page requested cannot be loaded now. Please, wait and try again later'
                              }
                    },
          };

          // Module              : JsMobile Other Items
          scope.translations['en'].buttons= {
                    close : 'buttons.close',
          };
          scope.translations['en'].validations= {
                    required : 'validations.required',
          };

          // Module              : monthly report
          scope.translations['en'].monthly_report= {
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

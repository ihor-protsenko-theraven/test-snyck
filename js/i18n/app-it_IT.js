/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Italian
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['it'] = scope.translations['it'] || {};

          // Module              : Alerts
          scope.translations['it'].alerts= {
                    home : {
                              in_home : 'Utente a Casa',
                              not_at_home : 'Non a casa',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Rilevato:',
                              handle_it_later : 'GESTISCI PIÙ TARDI',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Stato:',
                              statuses : {
                                        CLOSED : 'CHIUSO',
                                        IN_PROGRESS : 'IN CORSO',
                                        NEW : 'NUOVO',
                                        VIEWED : 'VISUALIZZATO',
                              },
                              titles : {
                                        alarm : 'ALLARME',
                                        photos : 'NUOVA FOTO',
                              },
                              updated : 'Aggiornato:',
                              view_event : 'VISUALIZZA EVENTO',
                    },
          };

          // Module              : activity index
          scope.translations['it'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'Total activities',
          };

          // Module              : JsMobile Other Items
          scope.translations['it'].buttons= {
                    close : 'Chiudi',
          };
          scope.translations['it'].validations= {
                    required : 'Questo campo è obbligatorio',
          };

          // Module              : monthly report
          scope.translations['it'].monthly_report= {
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

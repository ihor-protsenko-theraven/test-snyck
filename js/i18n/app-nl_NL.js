/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Dutch - Netherlands
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['nl'] = scope.translations['nl'] || {};

          // Module              : Alerts
          scope.translations['nl'].alerts= {
                    home : {
                              in_home : 'Cliënt is thuis',
                              not_at_home : 'Niet thuis',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Gedetecteerd:',
                              handle_it_later : 'LATER AFHANDELEN',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Status:',
                              statuses : {
                                        CLOSED : 'AFGESLOTEN',
                                        IN_PROGRESS : 'IN BEHANDELING',
                                        NEW : 'NIEUW',
                                        VIEWED : 'BEKEKEN',
                              },
                              titles : {
                                        alarm : 'ALARM',
                                        photos : 'NIEUWE FOTO\'S',
                              },
                              updated : 'Bijgewerkt:',
                              view_event : 'GEBEURTENIS BEKIJKEN',
                    },
          };

          // Module              : activity index
          scope.translations['nl'].activity_index= {
                    first_activity : 'Eerste actie',
                    first_activity_tip : 'Eerste activiteit van de dag',
                    last_activity : 'Laatste actie',
                    last_activity_tip : 'Laatste activiteit van de dag',
                    total_activities : 'Total activities',
          };

          // Module              : JsMobile Other Items
          scope.translations['nl'].buttons= {
                    close : 'Gesloten',
          };
          scope.translations['nl'].validations= {
                    required : 'Dit veld is verplicht',
          };

          // Module              : monthly report
          scope.translations['nl'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badkamer',
                              BEDROOM_SENSOR : 'Slaapkamer',
                              DINING_ROOM : 'Keuken',
                              FRIDGE_DOOR : 'Maaltijd',
                              FRONT_DOOR : 'Niet thuis',
                              LIVING_ROOM : 'Woonkamer',
                              OTHER_ROOM : 'Logeerkamer',
                              TOILET_ROOM_SENSOR : 'Toilet',
                              unknown : 'Onbekend',
                    },
                    activity : 'activiteit',
          };
})(this);

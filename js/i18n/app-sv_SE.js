/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Swedish
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['sv'] = scope.translations['sv'] || {};

          // Module              : Alerts
          scope.translations['sv'].alerts= {
                    home : {
                              in_home : 'bosätta är hemma',
                              not_at_home : 'Inte hemma',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Iakttagna',
                              handle_it_later : 'Hantera senare',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Status',
                              statuses : {
                                        CLOSED : 'Stängt',
                                        IN_PROGRESS : 'Pågår',
                                        NEW : 'Ny',
                                        VIEWED : 'Visad',
                              },
                              titles : {
                                        alarm : 'Alarm',
                                        photos : 'Ny bild',
                              },
                              updated : 'Uppdaterad',
                              view_event : 'Visa larmet',
                    },
          };

          // Module              : activity index
          scope.translations['sv'].activity_index= {
                    first_activity : 'Första aktivitet',
                    first_activity_tip : 'Dagens första aktivitet',
                    last_activity : 'Sista aktivitet',
                    last_activity_tip : 'Dagens sista aktivitet',
                    total_activities : 'Aktiviteter',
          };

          // Module              : JsMobile Other Items
          scope.translations['sv'].buttons= {
                    close : 'Stäng',
          };
          scope.translations['sv'].validations= {
                    required : 'Detta fält är obligatoriskt',
          };

          // Module              : monthly report
          scope.translations['sv'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badrum',
                              BEDROOM_SENSOR : 'Sovrum',
                              DINING_ROOM : 'Köket',
                              FRIDGE_DOOR : 'Måltid',
                              FRONT_DOOR : 'Inte hemma',
                              LIVING_ROOM : 'Vardagsrum',
                              OTHER_ROOM : 'Annat rum',
                              TOILET_ROOM_SENSOR : 'Toalett',
                              unknown : 'Okänd',
                    },
                    activity : 'aktivitet',
          };
})(this);

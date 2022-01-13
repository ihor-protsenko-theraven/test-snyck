/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Slovene
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['sl'] = scope.translations['sl'] || {};

          // Module              : Alerts
          scope.translations['sl'].alerts= {
                    home : {
                              in_home : 'Uporabnik je doma.',
                              not_at_home : 'Nikogar ni doma.',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Zaznano:',
                              handle_it_later : 'OBRAVNAJ KASNEJE.',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Status:',
                              statuses : {
                                        CLOSED : 'ZAKLJUČENO',
                                        IN_PROGRESS : 'POTEKA',
                                        NEW : 'NOVO',
                                        VIEWED : 'OGLEDANO',
                              },
                              titles : {
                                        alarm : 'ALARM',
                                        photos : 'NOVE FOTOGRAFIJE',
                              },
                              updated : 'Posodobljeno:',
                              view_event : 'POGLEJ DOGODEK.',
                    },
          };

          // Module              : activity index
          scope.translations['sl'].activity_index= {
                    first_activity : 'Prvo dejanje',
                    first_activity_tip : 'Prva dejavnost v dnevu',
                    last_activity : 'Zadnje dejanje',
                    last_activity_tip : 'Zadnja dejavnost v dnevu',
                    total_activities : 'Skupno število dejavnosti',
          };

          // Module              : JsMobile Other Items
          scope.translations['sl'].buttons= {
                    close : 'Zapri',
          };
          scope.translations['sl'].validations= {
                    required : 'Polje je obvezno.',
          };

          // Module              : monthly report
          scope.translations['sl'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Kopalnica',
                              BEDROOM_SENSOR : 'Spalnica',
                              DINING_ROOM : 'Jedilnica',
                              FRIDGE_DOOR : 'Obrok',
                              FRONT_DOOR : 'Izven doma',
                              LIVING_ROOM : 'Dnevna soba',
                              OTHER_ROOM : 'Druga soba',
                              TOILET_ROOM_SENSOR : 'Stranišče',
                              unknown : 'Neznano',
                    },
                    activity : 'Dejavnost',
          };
})(this);

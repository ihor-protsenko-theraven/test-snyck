/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Finnish
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['fi'] = scope.translations['fi'] || {};

          // Module              : Alerts
          scope.translations['fi'].alerts= {
                    home : {
                              in_home : 'Asukas kotona',
                              not_at_home : 'Ei kotona',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Havaittu:',
                              handle_it_later : 'Käsittele myöhemmin',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Tila:',
                              statuses : {
                                        CLOSED : 'SULJETTU',
                                        IN_PROGRESS : 'KÄYNNISSÄ',
                                        NEW : 'UUSI',
                                        VIEWED : 'KATSOTTU',
                              },
                              titles : {
                                        alarm : 'Hälytys',
                                        photos : 'Uusi kuva',
                              },
                              updated : 'Päivitetty:',
                              view_event : 'Näytä hälytys',
                    },
          };

          // Module              : activity index
          scope.translations['fi'].activity_index= {
                    first_activity : 'Ensimmäinen aktiviteetti',
                    first_activity_tip : 'Päivän ensimmäinen aktiviteetti',
                    last_activity : 'Viimeisin aktiviteetti',
                    last_activity_tip : 'Päivän viimeisin aktiviteetti',
                    total_activities : 'Aktiviteetit',
          };

          // Module              : JsMobile Other Items
          scope.translations['fi'].buttons= {
                    close : 'Sulje',
          };
          scope.translations['fi'].validations= {
                    required : 'Tämä on pakollinen  kenttä',
          };

          // Module              : monthly report
          scope.translations['fi'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Kylpyhuone',
                              BEDROOM_SENSOR : 'Makuuhuone',
                              DINING_ROOM : 'Keittiö',
                              FRIDGE_DOOR : 'Ateria',
                              FRONT_DOOR : 'Ei kotona',
                              LIVING_ROOM : 'Makuuhuone',
                              OTHER_ROOM : 'Muu huone',
                              TOILET_ROOM_SENSOR : 'WC',
                              unknown : 'Tuntematon',
                    },
                    activity : 'aktiviteetti',
          };
})(this);

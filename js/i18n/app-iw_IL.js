/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Hebrew
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['iw'] = scope.translations['iw'] || {};

          // Module              : Alerts
          scope.translations['iw'].alerts= {
                    home : {
                              in_home : 'משתמש בבית',
                              not_at_home : 'לא בבית',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'זוהה :',
                              handle_it_later : 'נסה מאוחר יותר',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'מצב&nbsp;:',
                              statuses : {
                                        CLOSED : 'סגור',
                                        IN_PROGRESS : 'בתהליך',
                                        NEW : 'חדש',
                                        VIEWED : 'נצפות',
                              },
                              titles : {
                                        alarm : 'אזעקה',
                                        photos : 'תמונות חדשות',
                              },
                              updated : 'עודכן :',
                              view_event : 'צפו אירוע',
                    },
          };

          // Module              : activity index
          scope.translations['iw'].activity_index= {
                    first_activity : 'פעילות ראשונה',
                    first_activity_tip : 'הפעילות הראשונה של היום',
                    last_activity : 'הפעילות האחרונה',
                    last_activity_tip : 'הפעילות האחרונה של היום',
                    total_activities : 'סה"כ פעילויות',
          };

          // Module              : JsMobile Other Items
          scope.translations['iw'].buttons= {
                    close : 'לסגור',
          };
          scope.translations['iw'].validations= {
                    required : 'שדה זה נחוץ',
          };

          // Module              : monthly report
          scope.translations['iw'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'חדר אמבטיה',
                              BEDROOM_SENSOR : 'חדר שינה',
                              DINING_ROOM : 'חדר אוכל',
                              FRIDGE_DOOR : 'ארוחה',
                              FRONT_DOOR : 'מחוץ לבית',
                              LIVING_ROOM : 'סלון',
                              OTHER_ROOM : 'חדר אחר',
                              TOILET_ROOM_SENSOR : 'שירותימ',
                              unknown : 'לא ידוע',
                    },
                    activity : 'פעילות',
          };
})(this);

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Russian
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['ru'] = scope.translations['ru'] || {};

          // Module              : Alerts
          scope.translations['ru'].alerts= {
                    home : {
                              in_home : 'Пользователь дома',
                              not_at_home : 'Вне дома',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Обнаружен:',
                              handle_it_later : 'Обратить внимание позже',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : 'Статус:',
                              statuses : {
                                        CLOSED : 'Закрыто',
                                        IN_PROGRESS : 'В процессе',
                                        NEW : 'Новое',
                                        VIEWED : 'Просмотренно',
                              },
                              titles : {
                                        alarm : 'Аварийный сигнал',
                                        photos : 'Новое фото',
                              },
                              updated : 'Обновлен:',
                              view_event : 'Просмотр события',
                    },
          };

          // Module              : activity index
          scope.translations['ru'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'Total activities',
          };

          // Module              : JsMobile Other Items
          scope.translations['ru'].buttons= {
                    close : 'Закрыть',
          };
          scope.translations['ru'].validations= {
                    required : 'Это поле обязательно для заполнения',
          };

          // Module              : monthly report
          scope.translations['ru'].monthly_report= {
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

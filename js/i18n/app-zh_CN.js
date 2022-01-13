/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Chinese z
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['zh'] = scope.translations['zh'] || {};

          // Module              : Alerts
          scope.translations['zh'].alerts= {
                    home : {
                              in_home : '用户在家',
                              not_at_home : '不在家',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : '已探测：',
                              handle_it_later : '稍后处理',
                              page_load_failed : {
                                        message : 'Page requested cannot be loaded now. Please, wait and try again later',
                                        title : 'Failed Loading Page',
                              },
                              status : '状态：',
                              statuses : {
                                        CLOSED : '已关闭',
                                        IN_PROGRESS : '进行中',
                                        NEW : '新',
                                        VIEWED : '已阅',
                              },
                              titles : {
                                        alarm : '警报',
                                        photos : '新照片',
                              },
                              updated : '已更新：',
                              view_event : '查看事项',
                    },
          };

          // Module              : activity index
          scope.translations['zh'].activity_index= {
                    first_activity : '第一项活动',
                    first_activity_tip : '该天第一项活动',
                    last_activity : '最后一项活动',
                    last_activity_tip : '该天最后一项活动',
                    total_activities : '总活动',
          };

          // Module              : JsMobile Other Items
          scope.translations['zh'].buttons= {
                    close : '关闭',
          };
          scope.translations['zh'].validations= {
                    required : '这是必填项',
          };

          // Module              : monthly report
          scope.translations['zh'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : '浴室',
                              BEDROOM_SENSOR : '卧室',
                              DINING_ROOM : '餐厅',
                              FRIDGE_DOOR : '进餐',
                              FRONT_DOOR : '不在家',
                              LIVING_ROOM : '客厅',
                              OTHER_ROOM : '其它房间',
                              TOILET_ROOM_SENSOR : '洗手间',
                              unknown : '未知',
                    },
                    activity : '活动',
          };
})(this);

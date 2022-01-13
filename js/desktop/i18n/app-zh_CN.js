/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Chinese z
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['zh_CN'] = scope.translations['zh_CN'] || {};

          // Module              : combobox
          scope.translations['zh_CN'].combobox= {
                    not_in_list : '请从名单中选择一项',
          };

          // Module              : activity index
          scope.translations['zh_CN'].activity_index= {
                    first_activity : '第一项活动',
                    first_activity_tip : '该天第一项活动',
                    last_activity : '最后一项活动',
                    last_activity_tip : '该天最后一项活动',
                    total_activities : '总活动',
          };

          // Module              : loader
          scope.translations['zh_CN'].loading = '加载中…';
          scope.translations['zh_CN'].serverError= {
                    message : '抱歉，系统有误，请再次尝试。',
                    title : '服务器出错',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['zh_CN'].buttons= {
                    accept : '接受',
                    cancel : '取消',
                    ok : '确认',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['zh_CN'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['zh_CN'].devices= {
                    activityTypes : {
                              BathroomCombined : '组合式浴室',
                              BathroomSensor : '浴室',
                              BedroomSensor : '卧室',
                              DiningRoom : '餐厅',
                              EP : '紧急挂件',
                              FridgeDoor : '冰箱门',
                              FrontDoor : '入口门',
                              LivingRoom : '客厅',
                              OtherRoom : '其它房间',
                              SmokeDetector : '烟雾探测器',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : '洗手间',
                              WaterLeakage : '漏水',
                    },
          };
          scope.translations['zh_CN'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : '浴室',
                              BEDROOM_SENSOR : '卧室',
                              DINING_ROOM : '餐厅',
                              FRIDGE_DOOR : '用餐',
                              FRONT_DOOR : '不在家',
                              LIVING_ROOM : '客厅',
                              OTHER_ROOM : '其它房间',
                              TOILET_ROOM_SENSOR : '洗手间',
                              unknown : '未知',
                    },
                    activity : '活动',
          };
          scope.translations['zh_CN'].periods= {
                    form : {
                              cancel : '取消',
                              commit : '确定',
                              edit_title : '编辑',
                              endTime : '结束时间',
                              is24Hours : '全天',
                              name : '姓名',
                              new_title : '新间期',
                              startTime : '开始时间',
                    },
          };
          scope.translations['zh_CN'].rules= {
                    descriptions : {
                              Absent : '在所选<b>房间类型</b>，所选<b>时间段</b>，未探测到活动. <br/><br/>如，被探测者本应该在</b>早上<b>餐厅进餐，但餐厅探测器并未在早上探测到<b>活动</b>',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : '在所选<b>时间段</b>，家中房门打开时间长于预期<b>时长</b>. <br/><br/>如，在<b>早上</b>，当像往常一样从公园散步回到家中，人口们打开时长超过了一个<b>小时</b>',
                              ExcessiveNumOfDetections : '在所选<b>间期</b>，被探测次数超过了在所选<b>房间类型</b>最大值. <br/><br/>如， 在<b>晚上</b>，<b>入口门</b>打开及关闭了<b>次数</b>',
                              HighNumOfVisits : '在所选<b>时间段</b>，所选<b>房间类型</b>，活动次数超过了预期最大的<b>活动次数</b>. <br/><br/>如，在<b>晚上</b>，被探测者进入<b>洗手间</b>五次，多于预期中的晚上<b>两次</b>',
                              Inactivity : '当被探测者在家，在所选<b> 时间段</b>，所选 <b>时长</b>, 未探测到活动. <br/><br/>如， 在<b>下午</b>，被探测者在客厅并且至少有<b>三个小时</b>未被探测到有活动. <br/><br/><b>重要：</b> 每一被探测者，只允许这一规则',
                              LongStay : '在所选<b>间期</b>，所选<b>房间类型</b>，总活动时长超出预期<b>时长</b>. <br/><br/>如，被探测者在<b>早上</b>进出<b>洗手间</b>多次，停留时间总时长为60分钟，长于早上常规的<b>20分钟</b>.',
                              LowNumOfDetections : '当被探测者在家，在所选<b> 时间段</b>，所选<b>房间类型</b>，<b>探测次数</b>低于预期. <br/><br/>如，被探测者使用<b>入口门</b>次数少于常规使用<b>四次</b> 一<b>天</b>',
                              LowNumOfVisits : '当被探测者在家，在所选<b> 时间段</b>，所选<b>房间类型</b>，<b>探测次数</b>低于预期. <br/><br/>如，被探测者使用<b>洗手间</b>次数少于常规使用<b>四次</b> 一<b>天</b>',
                              NoActivityDetected : '当被探测者在家，在所选<b>房间类型</b>, <b> 时间段</b> 未探测到活动. <br/><br/>如， 被探测者本应在<b>早上</b> 使用<b>洗手间</b>，但未探测到活动',
                              OutOfHome : '在入口门被开启后，被探测者离家时间超过所选<b>离家时间</b>. <br/><br/><b>注意: </b>推荐<b>离家时间</b>至少应为12小时.<br/><br/><b>重要：</b>每一被探测者只允许这一规则',
                              Presence : '在所选<b>时间段</b>，所选<b>房间类型</b>，被探测者有活动，表明被探测者醒了并开始活动.. <br/><br/>如， 在<b>早上</b>醒来后，被探测者在<b>洗手间</b>有活动.',
                              ShortStay : '在所选<b>时间段</b>，所选<b>房间类型</b>，总<b>活动</b>时长 低于预测<b>时长</b>. <br/><br/>如， 在<b>中午</b>，被探测者在<b>餐厅用餐时间为15分钟，而不是预测内的<b>45分钟</b>.',
                              ShortStayBedroom : '当被探测者在家，在所选<b>时间段</b>，如：<br/><br/>*卧室传感器未探测到活动<br/>，或<br/>*. 第一次与最后一次卧室传感器探测到活动的间隔要短于所选<b>时长</b>. <br/><br/>如，被探测者在客厅睡着了，未上床睡觉. <br/><br/><b>注意：如果传感器安装在卧室，</b>这一规则类型有效',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : '在所选<b>时间段</b>，所选<b>天数</b>，入口门打开. 对于这一<b>时长</b>，使用入口门不会触发新的警报. <br/><br/>如，在<b>星期三</b>的<b>晚上</b>，不速之客进入房屋内. 在接下来的<b>10分钟</b>，使用入口门将不会触发新的警报. <br/><br/><b>重要：</b>每一被探测者只允许这一规则',
                              UnexpectedPresence : '在所选<b>时间段，所选<b>房间类型</b>，至少探测到一次<b>活动</b>. <br><br/>如，被探测者在<b>晚上</b>中间时间段在<b>餐厅</b>好像进行了用餐',
                              Wandering : '在所选<b>间期</b>，一或多<b>天数</b>，被探测者本该在家，但探测到入口门开启后，离家时间过长<b>离开时间</b>.<br/><br/>如，被探测者通常睡觉时间为<b>晚上9:30 至 上午06:00</b>.在<b>星期二晚上</b>，早上2点，入口门打开， 被探测者离开家，在<b>一小时</b> 内未返回. <br/><br/><b>注意</b>: 推荐<b>离开时间</b>应最多为一小时. <br/><br/><b>重要：</b> 每一被探测者，只允许这一规则',
                    },
                    exit : {
                              message : '您确定要退出吗？',
                              title : '修改将不被保存',
                    },
                    form : {
                              cancel : '取消',
                              commit : '确定',
                              daysOfWeek : '白天',
                              delay : '离家时间',
                              description : '描述',
                              duration : '持续时长',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : '编辑',
                              enabled : '激活完成',
                              groupDeviceId : '房间类型',
                              maxNumOfDetections : '探测',
                              maxNumOfVisits : '进入',
                              minNumOfDetections : '探测',
                              minNumOfVisits : '进入',
                              name : '姓名',
                              new_title : '新规则',
                              periodSystemId : '间期',
                              ruleType : '规则类型',
                              specificDeviceId : '房间',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : '未出现',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : '大门开启时间过长',
                              ExcessiveNumOfDetections : '探测次数多',
                              HighNumOfVisits : '进入次数多',
                              Inactivity : '无活动',
                              LongStay : '长时间停留',
                              LowNumOfDetections : '探测次数少',
                              LowNumOfVisits : '进入次数少',
                              NoActivityDetected : '未监测到活动',
                              OutOfHome : '不在家',
                              Presence : '起床后正常走动',
                              ShortStay : '短时间停留',
                              ShortStayBedroom : '卧室短时间停留',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : '非正常时间进/出',
                              UnexpectedPresence : '非正常时段出现',
                              Wandering : '非正常时段离家',
                    },
          };
          scope.translations['zh_CN'].times= {
                    all_day : '全天',
                    days : '白天',
                    hours : '小时',
                    minutes : '分钟',
                    seconds : '秒',
          };
          scope.translations['zh_CN'].weekdays= {
                    friday : '星期五',
                    monday : '星期一',
                    saturday : '星期六',
                    sunday : '星期日',
                    thursday : '星期四',
                    tuesday : '星期二',
                    wednesday : '星期三',
          };
})(this);

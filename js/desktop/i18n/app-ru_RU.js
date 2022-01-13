/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Russian
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['ru_RU'] = scope.translations['ru_RU'] || {};

          // Module              : combobox
          scope.translations['ru_RU'].combobox= {
                    not_in_list : 'Пожалуйста, выберите пункт из списка',
          };

          // Module              : activity index
          scope.translations['ru_RU'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'Total activities',
          };

          // Module              : loader
          scope.translations['ru_RU'].loading = 'загрузка...';
          scope.translations['ru_RU'].serverError= {
                    message : 'serverError.message',
                    title : 'serverError.title',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['ru_RU'].buttons= {
                    accept : 'buttons.accept',
                    cancel : 'Отмена',
                    ok : 'ОК',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['ru_RU'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['ru_RU'].devices= {
                    activityTypes : {
                              BathroomCombined : 'devices.activityTypes.BathroomCombined',
                              BathroomSensor : 'devices.activityTypes.BathroomSensor',
                              BedroomSensor : 'devices.activityTypes.BedroomSensor',
                              DiningRoom : 'devices.activityTypes.DiningRoom',
                              EP : 'devices.activityTypes.EP',
                              FridgeDoor : 'devices.activityTypes.FridgeDoor',
                              FrontDoor : 'devices.activityTypes.FrontDoor',
                              LivingRoom : 'devices.activityTypes.LivingRoom',
                              OtherRoom : 'devices.activityTypes.OtherRoom',
                              SmokeDetector : 'devices.activityTypes.SmokeDetector',
                              SPBP : 'devices.activityTypes.SPBP',
                              ToiletRoomSensor : 'devices.activityTypes.ToiletRoomSensor',
                              WaterLeakage : 'devices.activityTypes.WaterLeakage',
                    },
          };
          scope.translations['ru_RU'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Bathroom',
                              BEDROOM_SENSOR : 'Bedroom',
                              DINING_ROOM : 'Dining room',
                              FRIDGE_DOOR : 'Meal',
                              FRONT_DOOR : 'Out of home',
                              LIVING_ROOM : 'Living room',
                              OTHER_ROOM : 'Other room',
                              TOILET_ROOM_SENSOR : 'Restroom',
                              unknown : 'Unknown',
                    },
                    activity : 'activity',
          };
          scope.translations['ru_RU'].periods= {
                    form : {
                              cancel : 'periods.form.cancel',
                              commit : 'periods.form.commit',
                              edit_title : 'periods.form.edit_title',
                              endTime : 'periods.form.endTime',
                              is24Hours : 'periods.form.is24Hours',
                              name : 'periods.form.name',
                              new_title : 'periods.form.new_title',
                              startTime : 'periods.form.startTime',
                    },
          };
          scope.translations['ru_RU'].rules= {
                    descriptions : {
                              Absent : 'rules.descriptions.Absent',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'rules.descriptions.DoorOpen',
                              ExcessiveNumOfDetections : 'rules.descriptions.ExcessiveNumOfDetections',
                              HighNumOfVisits : 'rules.descriptions.HighNumOfVisits',
                              Inactivity : 'rules.descriptions.Inactivity',
                              LongStay : 'rules.descriptions.LongStay',
                              LowNumOfDetections : 'rules.descriptions.LowNumOfDetections',
                              LowNumOfVisits : 'rules.descriptions.LowNumOfVisits',
                              NoActivityDetected : 'rules.descriptions.NoActivityDetected',
                              OutOfHome : 'rules.descriptions.OutOfHome',
                              Presence : 'rules.descriptions.Presence',
                              ShortStay : 'rules.descriptions.ShortStay',
                              ShortStayBedroom : 'rules.descriptions.ShortStayBedroom',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'rules.descriptions.UnexpectedEntryExit',
                              UnexpectedPresence : 'rules.descriptions.UnexpectedPresence',
                              Wandering : 'rules.descriptions.Wandering',
                    },
                    exit : {
                              message : 'rules.exit.message',
                              title : 'rules.exit.title',
                    },
                    form : {
                              cancel : 'rules.form.cancel',
                              commit : 'rules.form.commit',
                              daysOfWeek : 'rules.form.daysOfWeek',
                              delay : 'rules.form.delay',
                              description : 'rules.form.description',
                              duration : 'rules.form.duration',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'rules.form.edit_title',
                              enabled : 'rules.form.enabled',
                              groupDeviceId : 'rules.form.groupDeviceId',
                              maxNumOfDetections : 'rules.form.maxNumOfDetections',
                              maxNumOfVisits : 'rules.form.maxNumOfVisits',
                              minNumOfDetections : 'rules.form.minNumOfDetections',
                              minNumOfVisits : 'rules.form.minNumOfVisits',
                              name : 'rules.form.name',
                              new_title : 'rules.form.new_title',
                              periodSystemId : 'rules.form.periodSystemId',
                              ruleType : 'rules.form.ruleType',
                              specificDeviceId : 'rules.form.specificDeviceId',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'rules.ruleTypes.Absent',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'rules.ruleTypes.DoorOpen',
                              ExcessiveNumOfDetections : 'rules.ruleTypes.ExcessiveNumOfDetections',
                              HighNumOfVisits : 'rules.ruleTypes.HighNumOfVisits',
                              Inactivity : 'rules.ruleTypes.Inactivity',
                              LongStay : 'rules.ruleTypes.LongStay',
                              LowNumOfDetections : 'rules.ruleTypes.LowNumOfDetections',
                              LowNumOfVisits : 'rules.ruleTypes.LowNumOfVisits',
                              NoActivityDetected : 'rules.ruleTypes.NoActivityDetected',
                              OutOfHome : 'rules.ruleTypes.OutOfHome',
                              Presence : 'rules.ruleTypes.Presence',
                              ShortStay : 'rules.ruleTypes.ShortStay',
                              ShortStayBedroom : 'rules.ruleTypes.ShortStayBedroom',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : 'rules.ruleTypes.UnexpectedEntryExit',
                              UnexpectedPresence : 'rules.ruleTypes.UnexpectedPresence',
                              Wandering : 'rules.ruleTypes.Wandering',
                    },
          };
          scope.translations['ru_RU'].times= {
                    all_day : 'times.all_day',
                    days : 'times.days',
                    hours : 'times.hours',
                    minutes : 'times.minutes',
                    seconds : 'times.seconds',
          };
          scope.translations['ru_RU'].weekdays= {
                    friday : 'weekdays.friday',
                    monday : 'weekdays.monday',
                    saturday : 'weekdays.saturday',
                    sunday : 'weekdays.sunday',
                    thursday : 'weekdays.thursday',
                    tuesday : 'weekdays.tuesday',
                    wednesday : 'weekdays.wednesday',
          };
})(this);

/*##################################################
# Product             : Care @ Home
# Version             : 2.2.8
# Client              : Default
# Date                : 13/02/2017
# Translation Versión : Third version of translations for Care @ Home version 2.2.8
# Language            : English - United Kingdom
# Parse               : JavaScript
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['en_UK'] = scope.translations['en_UK'] || {};

          // Module              : activity index
          scope.translations['en_UK'].activity_index= {
                    first_activity : 'activity_index.first_activity',
                    first_activity_tip : 'activity_index.first_activity_tip',
                    last_activity : 'activity_index.last_activity',
                    last_activity_tip : 'activity_index.last_activity_tip',
                    total_activities : 'activity_index.total_activities',
          };

          // Module              : buttons
          scope.translations['en_UK'].buttons= {
                    accept : 'buttons.accept',
                    cancel : 'buttons.cancel',
                    ok : 'buttons.ok',
                    remove: 'Remove'
          };

          // Module              : combobox
          scope.translations['en_UK'].combobox= {
                    not_in_list : 'combobox.not_in_list',
          };

          // Module              : loader
          scope.translations['en_UK'].loading = 'loading';
          scope.translations['en_UK'].serverError= {
                    message : 'serverError.message',
                    title : 'serverError.title',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : monthly report
          scope.translations['en_UK'].devices= {
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
          scope.translations['en_UK'].monthly_report= {
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
          scope.translations['en_UK'].periods= {
                    form : {
                              cancel : 'periods.form.cancel',
                              commit : 'periods.form.commit',
                              edit_title : 'periods.form.edit_title',
                              endTime : 'periods.form.endTime',
                              is24Hours : 'periods.form.is24Hours',
                              name : 'periods.form.name',
                              new_title : 'periods.form.new_title',
                              startTime : 'periods.form.startTime'
                    },
          };
          scope.translations['en_UK'].rules= {
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
          scope.translations['en_UK'].times= {
                    all_day : 'times.all_day',
                    days : 'times.days',
                    hours : 'times.hours',
                    minutes : 'times.minutes',
                    seconds : 'times.seconds',
          };
          scope.translations['en_UK'].weekdays= {
                    friday : 'weekdays.friday',
                    monday : 'weekdays.monday',
                    saturday : 'weekdays.saturday',
                    sunday : 'weekdays.sunday',
                    thursday : 'weekdays.thursday',
                    tuesday : 'weekdays.tuesday',
                    wednesday : 'weekdays.wednesday',
          };
})(this);

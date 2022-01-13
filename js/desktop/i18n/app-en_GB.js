/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : English - Great Britain
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['en_GB'] = scope.translations['en_GB'] || {};

          // Module              : combobox
          scope.translations['en_GB'].combobox= {
                    not_in_list : 'Please select an item from the list.',
          };

          // Module              : activity index
          scope.translations['en_GB'].activity_index= {
                    first_activity : 'First act',
                    first_activity_tip : 'First activity of the day',
                    last_activity : 'Last act.',
                    last_activity_tip : 'Last activity of the day',
                    total_activities : 'Total activities',
          };

          // Module              : loader
          scope.translations['en_GB'].loading = 'loading...';
          scope.translations['en_GB'].serverError= {
                    message : 'Sorry, there was a problem on the system.Please try again later.',
                    title : 'Server error',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['en_GB'].buttons= {
                    accept : 'Accept',
                    cancel : 'Cancel',
                    ok : 'OK',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['en_GB'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['en_GB'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Combined bathroom',
                              BathroomSensor : 'Bathroom',
                              BedroomSensor : 'Bedroom',
                              DiningRoom : 'Dining room',
                              EP : 'EP',
                              FridgeDoor : 'Fridge door',
                              FrontDoor : 'Entrance door',
                              LivingRoom : 'Living room',
                              OtherRoom : 'Other room',
                              SmokeDetector : 'Smoke detector',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Restroom',
                              WaterLeakage : 'Water leakage',
                    },
          };
          scope.translations['en_GB'].monthly_report= {
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
          scope.translations['en_GB'].periods= {
                    form : {
                              cancel : 'Cancel',
                              commit : 'Apply',
                              edit_title : 'Edit',
                              endTime : 'End Time',
                              is24Hours : 'All day',
                              name : 'Name',
                              new_title : 'New Period',
                              startTime : 'Start Time'
                    },
          };
          scope.translations['en_GB'].rules= {
                    descriptions : {
                              Absent : 'When no <b>Visits</b> are identified in a chosen <b>Room Type</b>, during a chosen <b>Period</b>.<br/><br/>For example, the resident is expected to have breakfast in the <b>dining room</b> in the <b>morning</b> and the dining room sensor does not identify a <b>visit</b> in the morning.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'The door at home is open for longer than the expected <b>Duration</b>, during the chosen <b>Period</b>.<br/><br/>For example, in the <b>morning</b>, the resident leaves the entrance door open for over an <b>hour</b>, when returning from the daily walk to the park.',
                              ExcessiveNumOfDetections : 'During the chosen <b>Period</b>, the number of detections exceeds the chosen maximum number of <b>Detections</b> in the chosen <b>Room Type</b>.<br/><br/>For example, during the <b>night</b>, the <b>entrance door</b> is opened and closed a <b>number of times</b>.',
                              HighNumOfVisits : 'During the chosen <b>Period</b>, the number of visits to the chosen <b>Room Type</b>, exceeds the chosen maximum number of <b>Visits</b>.<br/><br/>For example, during the <b>night</b>, the resident visits the <b>restroom</b> five times, more than the expected <b>twice</b> a night.',
                              Inactivity : 'While the resident is at home, during the chosen <b>Period</b>, the resident has not been detected for the chosen <b>Duration</b>.<br/><br/>For example, in the <b>afternoon</b>, the resident is in the living room and is not detected moving about for at least <b>three hours</b>.<br/><br/><b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              LongStay : 'During the chosen <b>Period</b>, the total duration of the visits, identified in a chosen <b>Room Type</b>, is above the expected <b>Duration</b>.<br/><br/>For example, the resident goes to the <b>restroom</b> multiple times in the <b>morning</b> for a total duration of 60 minutes, longer than the morning routine of <b>20 minutes</b>.',
                              LowNumOfDetections : 'While the resident is at home, during the chosen <b>Period</b>, the <b>Detections</b> in the chosen <b>Room Type</b> are lower than expected.<br/><br/>For example, the resident uses the <b>entrance door</b> less than the routine usage of <b>four</b> times a <b>day</b>.',
                              LowNumOfVisits : 'While the resident is at home, during the chosen <b>Period</b>, the number of <b>Visits</b> identified in a chosen <b>Room Type</b> is lower than expected.<br/><br/>For example, the resident uses the <b>restroom</b> less than the routine of <b>four</b> times a <b>day</b>.',
                              NoActivityDetected : 'When the resident is at home, no detections are made in the chosen <b>Room Type</b>, during the chosen <b>Period</b>.<br/><br/>For example, the resident is expected to use the <b>restroom</b> in the <b>morning</b>, but is not detected.',
                              OutOfHome : 'The resident does not appear to be at home for longer than the chosen <b>Away Time</b> after the entrance door is used.<br/><br/><b>Note:</b> The recommended <b>Away Time</b> should be at least 12 hours.<br/><br/><b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              Presence : 'During the chosen <b>Period</b>, the resident is detected in the chosen <b>Room Type</b>, indicating that the resident is awake and moving about.<br/><br/>For example, the resident is detected in the <b>restroom</b> after waking up in the <b>morning</b>.',
                              ShortStay : 'During the chosen <b>Period</b>, the total duration of the <b>Visits</b>, identified in a chosen <b>Room Type</b>, is below the expected <b>Duration</b>.<br/><br/>For example, at <b>noon</b> time, the resident has a 15-minute lunch in the <b>dining room</b> rather than the expected <b>45-minute</b> lunch.',
                              ShortStayBedroom : 'While the resident is at home, during the chosen <b>Period</b>, either:<br/><br/>* No activity detections are made by the bedroom sensor<br/>-or-<br/>* The time between the first and last bedroom sensor detections is shorter than the chosen <b>Duration</b>.<br/><br/>For example, the resident fell asleep in the living room and never went to bed.<br/><br/><b>Note:</b> This rule type is only valid if a sensor is installed in the bedroom.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'During the chosen <b>Period</b>, on a chosen <b>Day(s)</b>, the entrance door is used. For the length of the <b>Duration</b>, using the entrance door will not cause new alarms<br/><br/>For example, an unexpected visitor enters the premises during the <b>night</b> on <b>Wednesday</b>. For the next <b>10 minutes</b>, using the entrance door will not cause new alarms.<br/><br/><b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              UnexpectedPresence : 'During the chosen <b>Period</b>, at least one <b>Visit</b> is identified in the chosen <b>Room Type</b>.<br><br/>For example, the resident appears to have a meal in the <b>dining room</b> in the middle of the <b>night</b>.',
                              Wandering : 'While the resident is expected to be at home, during a chosen <b>Period</b>, on one or more chosen <b>Days</b>, the resident is not detected for longer than the <b>Away Time</b> after the entrance door is used.<br/><br/>For example, the resident routinely sleeps from <b>9:30 PM to 6 AM</b>. On <b>Tuesday night</b>, at 2 AM, using the entrance door, the resident leaves home, not to return within <b>an hour</b>.<br/><br/><b>Note</b>: The recommended <b>Away Time</b> should be at most an hour.<br/><br/><b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                    },
                    exit : {
                              message : 'Are you sure you want to exit?',
                              title : 'Changes will not be saved',
                    },
                    form : {
                              cancel : 'Cancel',
                              commit : 'Apply',
                              daysOfWeek : 'Days',
                              delay : 'Away Time',
                              description : 'Description',
                              duration : 'Duration',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Edit',
                              enabled : 'Enabled',
                              groupDeviceId : 'Room Type',
                              maxNumOfDetections : 'Detections',
                              maxNumOfVisits : 'Visits',
                              minNumOfDetections : 'Detections',
                              minNumOfVisits : 'Visits',
                              name : 'Name',
                              new_title : 'New Rule',
                              periodSystemId : 'Period',
                              ruleType : 'Rule Type',
                              specificDeviceId : 'Room',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Absent',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Door Open',
                              ExcessiveNumOfDetections : 'High Number of Detections',
                              HighNumOfVisits : 'High Number of Visits',
                              Inactivity : 'Inactivity',
                              LongStay : 'Long Stay',
                              LowNumOfDetections : 'Low Number of Detections',
                              LowNumOfVisits : 'Low Number of Visits',
                              NoActivityDetected : 'No Activity Detected',
                              OutOfHome : 'Not at Home',
                              Presence : 'Up & About',
                              ShortStay : 'Short Stay',
                              ShortStayBedroom : 'Bedroom Short Stay',
                              SustainedActivity : 'Sustained Activity',
                              UnexpectedEntryExit : 'Unexpected Entry/Exit',
                              UnexpectedPresence : 'Unexpected Presence',
                              Wandering : 'Wandering',
                    },
          };
          scope.translations['en_GB'].times= {
                    all_day : 'All day',
                    days : 'Days',
                    hours : 'Hours',
                    minutes : 'Minutes',
                    seconds : 'Seconds',
          };
          scope.translations['en_GB'].weekdays= {
                    friday : 'Friday',
                    monday : 'Monday',
                    saturday : 'Saturday',
                    sunday : 'Sunday',
                    thursday : 'Thursday',
                    tuesday : 'Tuesday',
                    wednesday : 'Wednesday',
          };
})(this);

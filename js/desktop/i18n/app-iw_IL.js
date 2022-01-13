/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Hebrew
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['iw_IL'] = scope.translations['iw_IL'] || {};

          // Module              : combobox
          scope.translations['iw_IL'].combobox= {
                    not_in_list : 'אנא בחר מהרשימה',
          };

          // Module              : activity index
          scope.translations['iw_IL'].activity_index= {
                    first_activity : 'פעילות ראשונה',
                    first_activity_tip : 'פעילות ראשונה ביום',
                    last_activity : 'פעילות אחרונה',
                    last_activity_tip : 'פעילות אחרונה ביום',
                    total_activities : 'סה"כ פעילויות',
          };

          // Module              : loader
          scope.translations['iw_IL'].loading = '\u05D8\u05D5\u05E2\u05DF';
          scope.translations['iw_IL'].serverError= {
                    message : 'התרחשה תקלה במערכת.<br>אנא נסה שוב מאוחר יותר',
                    title : 'תקלה',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['iw_IL'].buttons= {
                    accept : 'אישור',
                    cancel : 'ביטול',
                    ok : 'אישור',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['iw_IL'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['iw_IL'].devices= {
                    activityTypes : {
                              BathroomCombined : 'חדר שירותים משולב',
                              BathroomSensor : 'חדר אמבטיה',
                              BedroomSensor : 'חדר שינה',
                              DiningRoom : 'חדר אוכל',
                              EP : 'לחצן מצוקה נייד',
                              FridgeDoor : 'חיישן מקרר',
                              FrontDoor : 'דלת כניסה',
                              LivingRoom : 'חדר מגורים',
                              OtherRoom : 'חדר נוסף',
                              SmokeDetector : 'גלאי עשן',
                              SPBP : 'כפתור מצוקה',
                              ToiletRoomSensor : 'שירותים',
                              WaterLeakage : 'חיישן הצפה',
                    },
          };
          scope.translations['iw_IL'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'חדר אמבטיה',
                              BEDROOM_SENSOR : 'חדר שינה',
                              DINING_ROOM : 'חדר אוכל',
                              FRIDGE_DOOR : 'ארוחה',
                              FRONT_DOOR : 'מחוץ לבית',
                              LIVING_ROOM : 'חדר מגורים',
                              OTHER_ROOM : 'חדר נוסף',
                              TOILET_ROOM_SENSOR : 'שירותים',
                              unknown : 'לא ידוע',
                    },
                    activity : 'פעילות',
          };
          scope.translations['iw_IL'].periods= {
                    form : {
                              cancel : 'ביטול',
                              commit : 'החל',
                              edit_title : 'עריכת',
                              endTime : 'זמן סיום',
                              is24Hours : 'כל היום',
                              name : 'שם',
                              new_title : 'טווח זמן חדש',
                              startTime : 'זמן התחלה',
                    },
          };
          scope.translations['iw_IL'].rules= {
                    descriptions : {
                              Absent : 'לא זוהו <b>\\\\\'ביקורים\\\\\'</b> ב<b>\\\\\'סוג חדר\\\\\'</b> במהלך <b>\\\\\'טווח זמן\\\\\'</b>.<br/><br/>לדוגמא, המנוי בדרך כלל אוכל ארוחת בוקר ב<b>חדר האוכל</b> בשעות ה<b>בוקר</b> וחיישן חדר האוכל לא זיהה <b>ביקור</b> של המנוי במהלך הבוקר.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'דלת הכניסה נשארת פתוחה מעבר ל<b>\\\\\'משך הזמן\\\\\'</b> המוגדר, במהלך <b>\\\\\'טווח זמן</b>\\\\\' מסוים.<br/><br/>לדוגמא, במהלך שעות ה<b>בוקר</b>, המנוי השאיר את דלת הכניסה פתוחה למשך יותר מ<b>שעה אחת</b>.',
                              ExcessiveNumOfDetections : 'מספר הגילויים במהלך <b>\\\\\'טווח זמן\\\\\'</b> ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים גדול יותר ממספר ה<b>\\\\\'גילויים\\\\\'</b> שהוגדר.<br/><br/>לדוגמא,  במהלך ה<b>לילה</b> נעשתה פתיחה וסגירה של <b>דלת הכניסה</b> <b>מספר פעמים</b>.',
                              HighNumOfVisits : 'מספר הביקורים במהלך <b>\\\\\'טווח זמן\\\\\'</b> ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים גדול יותר ממספר ה<b>\\\\\'ביקורים\\\\\'</b> שהוגדר.<br/><br/>לדוגמא,  במהלך ה<b>לילה</b> המנוי ביקר ב<b>חדר שירותים</b> חמש פעמים, כאשר בדרך כלל הוא לא קם משינה יותר מ<b>פעמיים</b>.',
                              Inactivity : 'המנוי נמצא בבית אך לא מזהים תנועה שלו במהלך התקופה המוגדרת ב<b>\\\\\'טווח זמן\\\\\'</b> ובמשך הזמן המוגדר ב<b>\\\\\'משך זמן\\\\\'</b>.<br/><br/>לדוגמא, במהלך <b>אחר הצהריים</b> המנוי זוהה בחדר המגורים אך לאחר מכן לא הייתה תזוזה למשך לפחות <b>שלוש שעות</b>.<br/><br/><b>לתשומת לבך:</b> ניתן להגדיר כלל אחד בלבד מסוג זה עבור המנוי.',
                              LongStay : 'זמן השהות של הביקורים ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים במהלך <b>\\\\\'טווח זמן\\\\\'</b> מוגדר הינה מעבר ל<b>\\\\\'משך זמן\\\\\'</b> מצופה.<br/><br/>לדוגמא, המנוי משתמש ב<b>שירותים</b> מספר פעמים רב במהלך ה<b>בוקר</b> ושוהה בחדר זה 60 דקות, כאשר בדרך כלל הוא שוהה בחדר שירותים כ <b>20 דקות</b> במהלך הבוקר.',
                              LowNumOfDetections : 'מספר הגילויים במהלך <b>\\\\\'טווח זמן\\\\\'</b> ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים נמוך ממספר ה<b>\\\\\'גילויים\\\\\'</b> הצפוי.<br/><br/>לדוגמא, המנוי משתמש ב<b>דלת הכניסה</b> פחות מ-<b> 4</b> פעמים ב<b>יום</b> כפי שנעשה בדרך כלל.',
                              LowNumOfVisits : 'במהלך <b>\\\\\'טווח זמן\\\\\'</b>, מספר ה<b>\\\\\'ביקורים\\\\\'</b> שזוהו ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים כאשר המנוי הינו בבית נמוך מהרגיל.<br/><br/>לדוגמא, המנוי משתמש ב<b>שירותים</b> פחות מ <b>4</b> ביקורים ב<b>יום</b> כפי שנעשה בדרך כלל.',
                              NoActivityDetected : 'כאשר המנוי בבית, אין גילויים ב<b>\\\\\'סוג חדר\\\\\'</b> במהלך <b>\\\\\'טווח זמן\\\\\'</b> מסויים.<br/><br/>לדוגמא, אין גילויים ב<b>שירותים</b> ב<b>בוקר</b> על אף שהמנוי בדרך כלל משתמש בשירותים בשעות הבוקר.',
                              OutOfHome : 'המנוי אינו בבית מעבר לזמן המוגדר כ<b>\\\\\'זמן מחוץ לבית\\\\\'</b>, לאחר שיצא מדלת הכניסה.<br><b><br>הערה:</b> הזמן המומלץ ל<b>\\\\\'זמן מחוץ לבית\\\\\'</b> הינו לפחות 12 שעות.<br/><b><br/>לתשומת לבך:</b> ניתן להגדיר כלל אחד בלבד מסוג זה עבור המנוי.',
                              Presence : 'המנוי מזוהה במיקום <b>\\\\\'סוג חדר\\\\\'</b> במהלך התקופה המוגדרת ב <b>\\\\\'טווח זמן\\\\\'</b>. המשמעות הינה שהמנוי קם ופעיל</b>.<br/><br/>לדוגמא, המנוי זוהה ב<b>שירותים</b> לאחר שהתעורר בשעות ה<b>בוקר</b>.',
                              ShortStay : 'משך ה<b>\\\\\'ביקורים\\\\\'</b> שמזוהים ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים במהלך <b>\\\\\'טווח זמן\\\\\'</b> מסוים, נמוך מ<b>\\\\\'משך זמן\\\\\'</b> מצופה.<br/><br/>לדוגמא, בשעות ה<b>צהריים</b> הלקוח אכל צהריים ב<b>חדר אוכל</b> במשך <b>15 דקות</b> כאשר בדרך כלל הוא אוכל צהריים <b>45 דקות</b>.',
                              ShortStayBedroom : 'כאשר הלקוח נמצא בבית ומתקיים אחד מהמקרים הבאים במהלך <b>\\\\\'טווח זמן\\\\\'</b> מסוים:<br/><br/>* אין גילויים של חיישן חדר שינה<br/>* הזמן בין גילוי ראשון וגילוי אחרון של חיישן חדר שינה קצר מ<b>\\\\\'משך זמן\\\\\'</b> מוגדר<br/><br/>לדוגמא, המנוי נרדם בחדר המגורים ולא הלך לישון בחדר השינה.<br/><br/><b>הערה:</b> הכלל תקף אך ורק אם יש חיישן מותקן בחדר שינה.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'דלת הכניסה נפתחת / נסגרת במהלך <b>\\\\\'טווח זמן\\\\\'</b> וב<b>\\\\\'יום\\\\\'</b> מסוים. במהלך <b>\\\\\'משך הזמן\\\\\'</b> שלאחר מכן, לא תיווצר התראה משימוש נוסף בדלת הכניסה.<br/><br/>לדוגמא, אורח לא צפוי נכנס לבית במהלך <b>שעות הלילה</b> של <b>יום רביעי</b>. <b>\\\\\'משך הזמן\\\\\'</b> שהוגדר הינו <b>10 דקות</b>, במהלכן לא תישלחנה התראות נוספות על שימוש בדלת הכניסה.<br/><br/><b>לתשומת לבך:</b> ניתן להגדיר כלל אחד בלבד מסוג זה עבור המנוי.',
                              UnexpectedPresence : 'מזוהה לפחות ביקור אחד ב<b>\\\\\'סוג חדר\\\\\'</b> מסוים במהלך <b>\\\\\'טווח זמן\\\\\'</b>.<br/><br/>לדוגמא, המנוי אכל ב<b>חדר האוכל</b> ב<b>אמצע הלילה</b>.',
                              Wandering : 'במהלך <b>\\\\\'טווח זמן\\\\\'</b> וב<b>\\\\\'יום\\\\\'</b> בהם מנוי בדרך כלל נמצא בבית, המנוי לא זוהה למשך זמן ארוך מ<b>\\\\\'זמן מחוץ לבית\\\\\'</b> לאחר שיצא מדלת הכניסה.<br/><br/>לדוגמא, המנוי ישן בדרך כלל בין <b>21:30 בערב ל 06:00 בבוקר</b>. במהלך <b>שלישי</b> ב<b>לילה</b>, בשעה 02:00 לפנות בוקר, המנוי עזב את הבית דרך דלת הכניסה ולא חזר תוך <b>שעה אחת</b>.<br/><br/><b>הערה:</b> הזמן המומלץ  לשדה \\\\\'זמן מחוץ לבית\\\\\' הינו שעה לכל היותר.<br/><b><br/>לתשומת לבך:</b>  ניתן להגדיר כלל אחד בלבד מסוג זה עבור מנוי.',
                    },
                    exit : {
                              message : 'האם אתה בטוח שאתה רוצה לצאת?',
                              title : 'השינויים שביצעת לא יישמרו',
                    },
                    form : {
                              cancel : 'ביטול',
                              commit : 'החל',
                              daysOfWeek : 'ימים',
                              delay : 'זמן מחוץ לבית',
                              description : 'תיאור',
                              duration : 'משך זמן',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'עריכת',
                              enabled : 'מופעל',
                              groupDeviceId : 'סוג חדר',
                              maxNumOfDetections : 'גילויים',
                              maxNumOfVisits : 'ביקורים',
                              minNumOfDetections : 'גילויים',
                              minNumOfVisits : 'ביקורים',
                              name : 'שם',
                              new_title : 'כלל חדש',
                              periodSystemId : 'טווח זמן',
                              ruleType : 'סוג כלל',
                              specificDeviceId : 'חדר',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'היעדרות מחדר',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'דלת פתוחה',
                              ExcessiveNumOfDetections : 'מספר גילויים רב בחדר',
                              HighNumOfVisits : 'מספר ביקורים רב בחדר',
                              Inactivity : 'חוסר פעילות',
                              LongStay : 'שהות ארוכה בחדר',
                              LowNumOfDetections : 'מספר גילויים נמוך בחדר',
                              LowNumOfVisits : 'מספר ביקורים נמוך בחדר',
                              NoActivityDetected : 'חוסר פעילות בחדר',
                              OutOfHome : 'מחוץ לבית',
                              Presence : 'קם ופעיל',
                              ShortStay : 'שהות קצרה בחדר',
                              ShortStayBedroom : 'שהות קצרה בחדר שינה',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : 'כניסה / יציאה לא שגרתית',
                              UnexpectedPresence : 'נוכחות בלתי צפוייה בחדר',
                              Wandering : 'שוטטות',
                    },
          };
          scope.translations['iw_IL'].times= {
                    all_day : 'כל היום',
                    days : 'ימים',
                    hours : 'שעות',
                    minutes : 'דקות',
                    seconds : 'שניות',
          };
          scope.translations['iw_IL'].weekdays= {
                    friday : 'שישי',
                    monday : 'שני',
                    saturday : 'שבת',
                    sunday : 'ראשון',
                    thursday : 'חמישי',
                    tuesday : 'שלישי',
                    wednesday : 'רביעי',
          };
})(this);

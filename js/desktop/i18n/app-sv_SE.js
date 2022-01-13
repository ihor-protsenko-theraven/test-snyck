/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Swedish
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['sv_SE'] = scope.translations['sv_SE'] || {};

          // Module              : combobox
          scope.translations['sv_SE'].combobox= {
                    not_in_list : 'Vänligen välj från listan',
          };

          // Module              : activity index
          scope.translations['sv_SE'].activity_index= {
                    first_activity : 'Första aktivitet',
                    first_activity_tip : 'Dagens första aktivitet',
                    last_activity : 'Sista aktivitet',
                    last_activity_tip : 'Dagens sista aktivitet',
                    total_activities : 'Aktiviteter',
          };

          // Module              : loader
          scope.translations['sv_SE'].loading = 'Laddar....';
          scope.translations['sv_SE'].serverError= {
                    message : 'Tyvärr system error. Försök senare igen.',
                    title : 'Server fel',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['sv_SE'].buttons= {
                    accept : 'Akseptera',
                    cancel : 'Avbryt',
                    ok : 'OK',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['sv_SE'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['sv_SE'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Badrum med WC',
                              BathroomSensor : 'Badrum',
                              BedroomSensor : 'Sovrum',
                              DiningRoom : 'Mat sal',
                              EP : 'Larm knapp',
                              FridgeDoor : 'Kylskåps dörr',
                              FrontDoor : 'Huvud dörr',
                              LivingRoom : 'Vardags rum',
                              OtherRoom : 'Annat rum',
                              SmokeDetector : 'Rökdetektor',
                              SPBP : 'Fast larm knapp',
                              ToiletRoomSensor : 'WC',
                              WaterLeakage : 'Vattenläcka detektor',
                    },
          };
          scope.translations['sv_SE'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badrum',
                              BEDROOM_SENSOR : 'Sovrum',
                              DINING_ROOM : 'Köket',
                              FRIDGE_DOOR : 'Måltid',
                              FRONT_DOOR : 'Inte hemma',
                              LIVING_ROOM : 'Vardagsrum',
                              OTHER_ROOM : 'Annat rum',
                              TOILET_ROOM_SENSOR : 'Toalett',
                              unknown : 'Okänd',
                    },
                    activity : 'aktivitet',
          };
          scope.translations['sv_SE'].periods= {
                    form : {
                              cancel : 'Cancel',
                              commit : 'Ändra',
                              edit_title : 'Ändra',
                              endTime : 'Tid att sluta',
                              is24Hours : 'Hela dagen',
                              name : 'Namn',
                              new_title : 'Ny period',
                              startTime : 'Tid att börja',
                    },
          };
          scope.translations['sv_SE'].rules= {
                    descriptions : {
                              Absent : 'bosätta har inte besökt det valda rummet under den valda perioden',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'Under den valda perioden dörren är öppet längre än den den valda tiden.',
                              ExcessiveNumOfDetections : 'Under den valda perioden de summerade detekterade rörelsemängdena i de valda rummet  är högre än vald.',
                              HighNumOfVisits : 'Under den valda perioden de summerade besäkmängdena i de valda rummet  är högre än vald.',
                              Inactivity : 'bosätta är hemma men har inte rört på sig tillräckligt lång tid under den valda perioden.  Bara en så här regel är möjligt.',
                              LongStay : 'Under den valda perioden de summerade besöktidena i de valda rummet  är högre än vald.',
                              LowNumOfDetections : 'bosätta är hemma men mängd av rörelsen är lägre än vald  i det valda rummet under den valda perioden.',
                              LowNumOfVisits : 'bosätta är hemma men mängd av rörelsen är lägre än vald  i det valda rummet under den valda perioden.',
                              NoActivityDetected : 'bosätta är hemma men rörelsen har inte  blivit detekterad i det valda rummet under den valda perioden.',
                              OutOfHome : 'bosätta är hemifrån längre än vald tid. Längre än 12 timmar rekomenderas. Bara en så här regel är möjligt.',
                              Presence : 'bosätta har blivit uptäckt under den valda perioden i de valda rummet. T.ex. bosätta har blivit uptäckt i toalett under morgon.',
                              ShortStay : 'Under den valda perioden de summerade besöktidena i de valda rummet  är lägre än vald.',
                              ShortStayBedroom : 'bosätta är hemma men antngen inga rörelsen är detekterad under den valda  perioden i sovrummet eller  tiden mellan rörelser i sovrummet är  kortare än vald. Kan betyda att bosätta är rastlös eller sover inte i sovrummet .',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'Under den valda perioden och de valda dagarna huvuddörren öppnades. Under den valda tiden den nästa dörröppningen orsakar ingen larm.',
                              UnexpectedPresence : 'Under den valda perioden åtminstone ett besök i de valda rummet.',
                              Wandering : 'bosätta borde vara hemma under den valda perioden (t.ex. natten) och under de valda dagarna (t.ex alla vardagar) men boede är borta hemifrån länge än hen borde.  Väl perioden, dagarna och hur länge bosätta  får  vara borta hemifrån.  Det är rekomenderat att inte välja kortare tid än en timme.  Bara en så här regel möjligt.',
                    },
                    exit : {
                              message : 'Är du säker du vill sluta?',
                              title : 'Ändring inte sparad',
                    },
                    form : {
                              cancel : 'Cancel',
                              commit : 'Ändra',
                              daysOfWeek : 'Dagar',
                              delay : 'Tiden man är borta',
                              description : 'Beskrivelse',
                              duration : 'Tiden',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Ändra',
                              enabled : 'Enabled',
                              groupDeviceId : 'Typ av rummet',
                              maxNumOfDetections : 'Detekterad rörelser',
                              maxNumOfVisits : 'Besöken',
                              minNumOfDetections : 'Detekterad rörelser',
                              minNumOfVisits : 'Besöken',
                              name : 'Namn',
                              new_title : 'Ny regel',
                              periodSystemId : 'Period',
                              ruleType : 'Regel typ',
                              specificDeviceId : 'Rummet',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Inte hemma',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Dörret är öppet',
                              ExcessiveNumOfDetections : 'Högt mängd av detekterad rörelser',
                              HighNumOfVisits : 'Hög mängd av besök',
                              Inactivity : 'Låg aktivitet',
                              LongStay : 'Lång besök',
                              LowNumOfDetections : 'Låg mängd av detekterad rörelser',
                              LowNumOfVisits : 'Få besök',
                              NoActivityDetected : 'Ingen aktivitet detekterad',
                              OutOfHome : 'Inte hemma',
                              Presence : 'Upp ock rör på sig',
                              ShortStay : 'Kort besök',
                              ShortStayBedroom : 'Kort besök i sovrummet',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : 'Oväntad inträde/utträde',
                              UnexpectedPresence : 'Oväntad vistelse',
                              Wandering : 'Vandrar',
                    },
          };
          scope.translations['sv_SE'].times= {
                    all_day : 'Hela dagen',
                    days : 'Dagar',
                    hours : 'Timmar',
                    minutes : 'Minuter',
                    seconds : 'Sekunder',
          };
          scope.translations['sv_SE'].weekdays= {
                    friday : 'Fredag',
                    monday : 'Måndag',
                    saturday : 'Lördag',
                    sunday : 'Sönndag',
                    thursday : 'Torsdag',
                    tuesday : 'Disdag',
                    wednesday : 'Onsdag',
          };
})(this);

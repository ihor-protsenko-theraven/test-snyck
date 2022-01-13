/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Slovene
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['sl_SI'] = scope.translations['sl_SI'] || {};

          // Module              : combobox
          scope.translations['sl_SI'].combobox= {
                    not_in_list : 'Prosimo, izberite možnost s seznama.',
          };

          // Module              : activity index
          scope.translations['sl_SI'].activity_index= {
                    first_activity : 'Prvo dejanje',
                    first_activity_tip : 'Prva dejavnost v dnevu',
                    last_activity : 'Zadnje dejanje',
                    last_activity_tip : 'Zadnja dejavnost v dnevu',
                    total_activities : 'Skupno število dejavnosti',
          };

          // Module              : loader
          scope.translations['sl_SI'].loading = 'Stran se nalaga …';
          scope.translations['sl_SI'].serverError= {
                    message : 'Oprostite, prišlo je do težave. Prosimo, poskusite znova.',
                    title : 'Napaka na strežniku',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['sl_SI'].buttons= {
                    accept : 'Sprejmi',
                    cancel : 'Prekliči',
                    ok : 'V redu',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['sl_SI'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['sl_SI'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Kopalnica s straniščem',
                              BathroomSensor : 'Kopalnica',
                              BedroomSensor : 'Spalnica',
                              DiningRoom : 'Jedilnica',
                              EP : 'EP',
                              FridgeDoor : 'Vrata hladilnika',
                              FrontDoor : 'Vhodna vrata',
                              LivingRoom : 'Dnevna soba',
                              OtherRoom : 'Druga soba',
                              SmokeDetector : 'Detektor dima',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Stranišče',
                              WaterLeakage : 'Razlitje vode',
                    },
          };
          scope.translations['sl_SI'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Kopalnica',
                              BEDROOM_SENSOR : 'Spalnica',
                              DINING_ROOM : 'Jedilnica',
                              FRIDGE_DOOR : 'Obrok',
                              FRONT_DOOR : 'Izven doma',
                              LIVING_ROOM : 'Dnevna soba',
                              OTHER_ROOM : 'Druga soba',
                              TOILET_ROOM_SENSOR : 'Stranišče',
                              unknown : 'Neznano',
                    },
                    activity : 'Dejavnost',
          };
          scope.translations['sl_SI'].periods= {
                    form : {
                              cancel : 'Prekliči',
                              commit : 'Uporabi',
                              edit_title : 'Uredi',
                              endTime : 'Končni čas',
                              is24Hours : 'Ves dan',
                              name : 'Ime',
                              new_title : 'Novo obdobje',
                              startTime : 'Začetni čas',
                    },
          };
          scope.translations['sl_SI'].rules= {
                    descriptions : {
                              Absent : 'Ko  Visits niso določeni za izbrani Room Type, v izbranem obdobju Period.Na primer, uporabnik naj bi imel zajtrk v dining room in sicer  morning  hkrati pa sensor v jedilnici zjutraj ni zaznal visit.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'Vrata v bivališču so odprta dlje kot pričakovano Duration, v izbranem Period.Na primer, morning, uporabnik pusti vhodna vrata odprta več kothour, ko se vrne z dnevnega sprehoda.',
                              ExcessiveNumOfDetections : 'V izbranem Period, je število zaznavanj preseglo določeno najvišje število Detections v določenem Room Type.Na primer, v night,  entrance door so bila vhodna vrata odprta in zaprta number of times.',
                              HighNumOfVisits : 'V izbranem Period, je število obiskov v določenem Room Type, preseglo določeno najvišje številoVisits.Na primer, v night, je uporabnik uporabil restroom petkrat, kar je več, kot pričakovanih twice na noč.',
                              Inactivity : 'Ko je uporabnik doma, v izbranemPeriod, uporabnik ni bil zaznan v določenemDuration.Na primer, afternoon, je uporabnik v dnevni sobi in njegovo gibanje ni bilo zaznano najmanjthree hours.Important: Samo eno pravilo te vrste pravil je dovoljeno na uporabnika.',
                              LongStay : 'V izbranem Period, je skupno trajanje obiskov, zaznanih v določenem Room Type, večje kot pričakovano Duration.Na primer, uporabnik uporabi restroom večkrat v času morning v skupnem trajanju 60 minute, kar je dlje kot običajno zjutraj - 20 minutes.',
                              LowNumOfDetections : 'Ko je uporabnik doma, v izbranem Period, je število Detections v določenem Room Type nižje, kot pričakovano.Na primer, uporabnik uporablja entrance door manj, kot je običajnih four -krat na  day.',
                              LowNumOfVisits : 'Ko je uporabnik doma, v izbranem Period, je število Visits zaznanih v določenem Room Type nižje, kot pričakovano.Na primer, uporabnik je uporabil restroom manjkrat,kot običajnih four na day.',
                              NoActivityDetected : 'Ko je uporabnik doma ni bilo zaznavanj v določenem Room Type, v določenem obdobju Period.Na primer, pričakuje se, da uporabnik uporabirestroom morning, vendar to ni bilo zaznano.',
                              OutOfHome : 'Kot kaže, uporabnika ni doma dlje kot določen Away Time po uporabi vhodnih vrat.Note: Priporočen Away Time naj bi bil vsaj 12 ur.Important: Samo eno pravilo te vrste pravil je dovoljeno na uporabnika.',
                              Presence : 'V izbranemPeriod, je bil uporabnik zaznan v določenem Room Type, kar pomeni, da je uporabnik buden in se giblje.Na primer, uporbnik je bil zaznam v restroom potem, ko se je prebudil morning.',
                              ShortStay : 'V izbranemPeriod, je skupno število Visits, zaznano za določen Room Type, nižje od pričakovanih  Duration.Na primer, ob noon, ima uporabnik 15 minutno kosilo v dining room namesto običajnega 45-minute kosila.',
                              ShortStayBedroom : 'Ko je uporabnik doma, v določenem obdobju Period, ali:* Senzor v spalnici ni zaznal premika-ali-* Čas med prvo in zadnjo zaznavo senzorja v spalnici je krajši kot izbrano trajanjeDuration.Na primer, uporabnik je zaspal v dnevni sobi in ni šel v posteljo.Opozorilo: Ta vrsta pravil je veljavna le, če je sensor nameščen v spalnici.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'V izbranem Period, na izbran Day(s), je nekdo uporabil vhodna vrata. V obdobju Duration, uporaba vhodnih vrat ne bo sprožila novih alarmovNa primer, nepričakovan obiskovalec je vstopil v času night v Wednesday. V naslednjih 10 minutes, uporaba vhodnih vrat ne bo povzročila novih alarmov.Pomembno: Samo eno pravilo na osnovi te vrste pravil je dovoljeno na uporabnika.',
                              UnexpectedPresence : 'V izbranem Period, je bil vsaj en Visit zaznan v določenem Room Type.Na primer, uporabnik naj bi imel obrok vdining room sredi night.',
                              Wandering : 'Ko je predvideno, da je uporabnik doma, v določenem Period, na en ali več izbranih Days, uporabnik ni bil zaznan dlje časa, kot Away Time po uporabi vhodnih vrat.Na primer, uporabnik običajno spi od 9:30 PM to 6 AM. V Tuesday night, ob 2:00 ponoči uporabnik odpre vhodna vrata in zapusti bivališče in se ne vrne v času an hour.Note: Priporočen Away Time naj ne bi presegel ene ure.Important: Samo eno pravilo te vrste pravil je dovoljeno na uporabnika.',
                    },
                    exit : {
                              message : 'Ali ste prepričani, da želite zaključiti?',
                              title : 'Spremembe ne bodo shranjene.',
                    },
                    form : {
                              cancel : 'Prekliči',
                              commit : 'Uporabi',
                              daysOfWeek : 'Dnevi',
                              delay : 'Čas odsotnosti',
                              description : 'Opis',
                              duration : 'Trajanje',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Uredi',
                              enabled : 'Omogočeno',
                              groupDeviceId : 'Vrsta sobe',
                              maxNumOfDetections : 'Zaznave',
                              maxNumOfVisits : 'Obiski',
                              minNumOfDetections : 'Zaznave',
                              minNumOfVisits : 'Obiski',
                              name : 'Ime',
                              new_title : 'Novo pravilo',
                              periodSystemId : 'Obdobje',
                              ruleType : 'Vrsta pravila',
                              specificDeviceId : 'Soba',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Odsoten',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Odprta vrata',
                              ExcessiveNumOfDetections : 'Veliko število zaznav',
                              HighNumOfVisits : 'Veliko število obiskov',
                              Inactivity : 'Nedejavnost',
                              LongStay : 'Daljši obisk',
                              LowNumOfDetections : 'Majhno število zaznav',
                              LowNumOfVisits : 'Majhno število obiskov',
                              NoActivityDetected : 'Nobena dejavnost ni zaznana',
                              OutOfHome : 'Nikogar ni doma',
                              Presence : 'Že pokonci',
                              ShortStay : 'Krajši obisk',
                              ShortStayBedroom : 'Krajši obisk spalnice',
                              SustainedActivity : 'Redna dejavnost',
                              UnexpectedEntryExit : 'Nepričakovan vstop/izstop',
                              UnexpectedPresence : 'Nepričakovana prisotnost',
                              Wandering : 'Pohajkovanje',
                    },
          };
          scope.translations['sl_SI'].times= {
                    all_day : 'Ves dan',
                    days : 'Dnevi',
                    hours : 'Ure',
                    minutes : 'Minute',
                    seconds : 'Sekunde',
          };
          scope.translations['sl_SI'].weekdays= {
                    friday : 'Petek',
                    monday : 'Ponedeljek',
                    saturday : 'Sobota',
                    sunday : 'Nedelja',
                    thursday : 'Četrtek',
                    tuesday : 'Torek',
                    wednesday : 'Sreda',
          };
})(this);

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Dutch - Netherlands
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['nl_NL'] = scope.translations['nl_NL'] || {};

          // Module              : combobox
          scope.translations['nl_NL'].combobox= {
                    not_in_list : 'Selecteer een onderdeel uit de lijst.',
          };

          // Module              : activity index
          scope.translations['nl_NL'].activity_index= {
                    first_activity : 'Eerste actie',
                    first_activity_tip : 'Eerste activiteit van de dag',
                    last_activity : 'Laatste actie',
                    last_activity_tip : 'Laatste activiteit van de dag',
                    total_activities : 'Total activities',
          };

          // Module              : loader
          scope.translations['nl_NL'].loading = 'laden...';
          scope.translations['nl_NL'].serverError= {
                    message : 'Er is een tijdelijke storing.<br/> Probeer het later opnieuw.',
                    title : 'Serverfout',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['nl_NL'].buttons= {
                    accept : 'Bevestigen',
                    cancel : 'Annuleren',
                    ok : 'Bevestigen',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['nl_NL'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['nl_NL'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Badkamer met toilet',
                              BathroomSensor : 'Badkamer',
                              BedroomSensor : 'Slaapkamer',
                              DiningRoom : 'Keuken',
                              EP : 'EP',
                              FridgeDoor : 'Deur van de koelkast',
                              FrontDoor : 'Voordeur',
                              LivingRoom : 'Woonkamer',
                              OtherRoom : 'Logeerkamer',
                              SmokeDetector : 'Rookmelder',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Toilet',
                              WaterLeakage : 'Waterlek',
                    },
          };
          scope.translations['nl_NL'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badkamer',
                              BEDROOM_SENSOR : 'Slaapkamer',
                              DINING_ROOM : 'Keuken',
                              FRIDGE_DOOR : 'Maaltijd',
                              FRONT_DOOR : 'Niet thuis',
                              LIVING_ROOM : 'Woonkamer',
                              OTHER_ROOM : 'Logeerkamer',
                              TOILET_ROOM_SENSOR : 'Toilet',
                              unknown : 'Onbekend',
                    },
                    activity : 'activiteit',
          };
          scope.translations['nl_NL'].periods= {
                    form : {
                              cancel : 'Annuleren',
                              commit : 'TOEPASSEN',
                              edit_title : 'Bewerk',
                              endTime : 'Eindtijd',
                              is24Hours : 'De hele dag',
                              name : 'Naam',
                              new_title : 'nieuwe periode',
                              startTime : 'Starttijd',
                    },
          };
          scope.translations['nl_NL'].rules= {
                    descriptions : {
                              Absent : 'Wanneer er geen <b>Bezoeken</b> zijn geïdentificeerd in een gekozen <b>Kamertype</b>, tijdens een gekozen <b>Periode</b>.<br/><br/>Bijvoorbeeld: de bewoner wordt verwacht voor het ontbijt in de <b>eetkamer</b> te zijn in de',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'De deur van het huis is langer open dan de verwachte <b>Duur</b>, tijden de gekozen <b>Periode</b>.<br/><br/>Bijvoorbeeld: de bewoner laat <b>\'s morgens</b>de voordeur open voor meer dan een',
                              ExcessiveNumOfDetections : 'Tijdens de gekozen <b>Periode</b>, overtreft het aantal detecties het gekozen maximale aantal detecties voor <b>Detecties</b> in het gekozen <b>Kamertype</b>.<br/><br/>Bijvoorbeeld: tijdens de <b>nacht</b>, is de',
                              HighNumOfVisits : 'Tijdens de gekozen <b> Periode </ b>, het aantal bezoeken aan de gekozen <b> Kamer Type </ b>, hoger is dan de gekozen maximum aantal <b> Bezoeken </ b>. <br/> <Br /> Als u bijvoorbeeld tijdens de <b> \'s nachts </ b>, de bewoner bezoekt de <b> toilet </ b> vijf keer meer dan de verwachte <b> twee keer </ b> per nacht.',
                              Inactivity : 'Tijdens de gekozen <b>Periode</b>, overtreft het aantal detecties het gekozen maximale aantal detecties voor <b>Detecties</b> in het gekozen <b>Kamertype</b>.<br/><br/>Bijvoorbeeld: tijdens de <b>nacht</b>, is de',
                              LongStay : 'Tijdens de gekozen <b>Periode</b> is de totale duur van de bezoeken, geïdentificeerd in een gekozen <b>Kamertype</b>, langer dan de verwachte <b>Duur</b>.<br/><br/>Bijvoorbeeld: de bewoner gaat meerdere keren <b>\'s morgens</b>  naar het <b>toilet</b> en de totale duur is 60 minuten, langer dan de gebruikelijke <b>20 minuten</b> \'s morgens.',
                              LowNumOfDetections : 'Terwijl de bewoner thuis is, tijdens de gekozen <b>Periode</b>, is het aantal <b>Detecties</b> in het gekozen <b>Kamertype</b> lager dan zou mogen worden verwacht.<br/><br/>Bijvoorbeeld: de bewoner gebruikt de <b>voordeur</b> minder vaak dan de gebruikelijke <b>vier</b> keer per <b>dag</b>.',
                              LowNumOfVisits : 'Terwijl de bewoner thuis is, tijdens de gekozen <b>Periode</b>, is het aantal <b>Bezoeken</b> in een gekozen <b>Kamertype</b> lager dan zou mogen worden verwacht.<br/><br/>Bijvoorbeeld: de bewoner gaat minder vaak naar het <b>toilet</b> dan de gebruikelijke <b>vier</b> keer per <b>dag</b>.',
                              NoActivityDetected : 'Terwijl de bewoner thuis is wordt hij of zij niet gedetecteerd in het gekozen <b>Kamertype</b> tijdens een gekozen <b>Periode</b>.<br/><br/>Bijvoorbeeld: er wordt verwacht dat de bewoner <b>\'s morgens</b> het <b>toilet</b> gebruikt, maar dit wordt niet gedetecteerd.',
                              OutOfHome : 'Het lijkt alsof de bewoner langer niet thuis is dan de gekozen <b>Tijd niet thuis</b> nadat de voordeur is gebruikt.<br/><br/><b>Opmerking:</b> Het wordt aanbevolen de <b>Tijd niet thuis</b> op minstens 12 uur in te stellen.<br/><br/><b>Belangrijk:</b> Per bewoner is er slechts één regel op basis van dit regeltype toegestaan.',
                              Presence : 'Tijdens de gekozen <b>Periode</b> is de bewoner gedetecteerd in het gekozen <b>Kamertype</b>, wat aangeeft dat de bewoner wakker is en zich door het huis verplaatst.<br/><br/>Bijvoorbeeld: de bewoner is gedetecteerd in het <b>toilet </b> nadat hij of zij <b>\'s morgens</b> is opgestaan.',
                              ShortStay : 'Tijdens de gekozen <b>Periode</b> is de totale duur van de bezoeken, geïdentificeerd in een gekozen <b>Kamertype</b>, korter dan de verwachte <b>Duur</b>.<br/><br/>Bijvoorbeeld: rond het <b>middaguur</b>, luncht de bewoner slechts 15 minuten in de <b>eetkamer</b> in plaats van de verwachte <b>45 minuten</b>.',
                              ShortStayBedroom : 'Terwijl de bewoner thuis is, zijn er tijdens de gekozen <b>Periode</b>, ofwel:<br/><br/>* geen activiteiten gedetecteerd door de slaapkamersensor<br/>, of<br/>* de tijd tussen de eerste en de tweede detectie door slaapkamersensor is korten dat de gekozen <b>Duur</b>.<br/><br/>Bijvoorbeeld: de bewoner is in slaap gevallen in de woonkamer en is helemaal niet naar bed gegaan.<br/><br/><b>Opmerking:</b> Dit regeltype is alleen geldig als er een sensor in de slaapkamer is geïnstalleerd.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'Tijdens de gekozen <b>Periode</b>, op een gekozen <b>Dag of Dagen</b>, wordt de voordeur gebruikt. Voor de totale lengte van de <b>Duur</b> zal het gebruik van de voordeur geen nieuwe alarmen af laten gaan<br/><br/>Bijvoorbeeld: een onverwachte bezoeker betreedt het huis tijdens de <b>woensdagnacht</b>. De volgende <b>10 minuten</b> zal het gebruik van de voordeur geen nieuwe alarmen af laten gaan.<br/><br/><b>Belangrijk:</b> Per bewoner is er slechts één regel op basis van dit regeltype toegestaan.',
                              UnexpectedPresence : 'Tijdens een gekozen <b>Periode</b>, op één of meerdere <b>dagen</b>, waarvan wordt verwacht dat de bewoner thuis is, wordt de bewoner voor langere tijd dan de <b>Tijd niet thuis</b> niet gedetecteerd nadat de voordeur is gebruikt.<br/><br/>Bijvoorbeeld: de bewoner slaapt normaal gesproken van <b>21.30 tot 06.00 uur</b>. <b>Dinsdagnacht</b>, om 02.00 uur, verlaat de bewoner het huis via de voordeur en keert niet terug binnen <b>een uur</b>.<br/><br/><b>Opmerking</b>: De aanbevolen <b>Tijd niet thuis</b> zou minstens één uur moeten bedragen. <br/><br/><b>Belangrijk:</b> Per bewoner is er slechts één regel op basis van dit regeltype toegestaan.',
                              Wandering : 'Tijdens een gekozen <b>Periode</b>, op één of meerdere <b>dagen</b>, waarvan wordt verwacht dat de bewoner thuis is, wordt de bewoner voor langere tijd dan de <b>Tijd niet thuis</b> niet gedetecteerd nadat de voordeur is gebruikt.<br/><br/>Bijvoorbeeld: de bewoner slaapt normaal gesproken van <b>21.30 tot 06.00 uur</b>. <b>Dinsdagnacht</b>, om 02.00 uur, verlaat de bewoner het huis via de voordeur en keert niet terug binnen <b>een uur</b>.<br/><br/><b>Opmerking</b>: De aanbevolen <b>Tijd niet thuis</b> zou minstens één uur moeten bedragen. <br/><br/><b>Belangrijk:</b> Per bewoner is er slechts één regel op basis van dit regeltype toegestaan.',
                    },
                    exit : {
                              message : 'Weet u zeker dat u wilt afsluiten?',
                              title : 'Wijzigingen zullen niet worden opgeslagen',
                    },
                    form : {
                              cancel : 'Annuleren',
                              commit : 'Toepassen',
                              daysOfWeek : 'Dagen',
                              delay : 'Dagen',
                              description : 'Beschrijving',
                              duration : 'Duur',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Bewerk',
                              enabled : 'Ingeschakeld',
                              groupDeviceId : 'Kamertype',
                              maxNumOfDetections : 'Waarnemingen',
                              maxNumOfVisits : 'bezoeken',
                              minNumOfDetections : 'Waarnemingen',
                              minNumOfVisits : 'bezoeken',
                              name : 'Naam',
                              new_title : 'Nieuwe regel',
                              periodSystemId : 'Periode',
                              ruleType : 'Regeltype',
                              specificDeviceId : 'Kamer',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Afwezig',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Deur open',
                              ExcessiveNumOfDetections : 'Groot aantal detecties',
                              HighNumOfVisits : 'Groot aantal bezoeken',
                              Inactivity : 'inactiviteit',
                              LongStay : 'Langdurig verblijf',
                              LowNumOfDetections : 'Gering aantal detecties',
                              LowNumOfVisits : 'Gering aantal bezoeken',
                              NoActivityDetected : 'Geen activiteit gedetecteerd',
                              OutOfHome : 'Niet thuis',
                              Presence : 'Opgestaan',
                              ShortStay : 'Kort verblijf',
                              ShortStayBedroom : 'Slaapkamer Short Stay',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : 'Onverwacht bezoek/vertrek',
                              UnexpectedPresence : 'Onverwachte aanwezigheid',
                              Wandering : 'Rondwandelend',
                    },
          };
          scope.translations['nl_NL'].times= {
                    all_day : 'Hele dag',
                    days : 'Dagen',
                    hours : 'Uren',
                    minutes : 'Minuten',
                    seconds : 'Seconden',
          };
          scope.translations['nl_NL'].weekdays= {
                    friday : 'Vrijdag',
                    monday : 'Maandag',
                    saturday : 'Zaterdag',
                    sunday : 'Zondag',
                    thursday : 'Donderdag',
                    tuesday : 'Dinsdag',
                    wednesday : 'Woensdag',
          };
})(this);

/*##################################################
# Product             : Care @ Home
# Version             : 2.6.5
# Client              : Default
# Date                : 27/9/2020
# Translation Versión : First version of translations for Care @ Home version 2.6.5
# Language            : German - Germany
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['de_DE'] = scope.translations['de_DE'] || {};

          // Module              : combobox
          scope.translations['de_DE'].combobox= {
                    not_in_list : 'Wählen Sie ein Element aus der Liste',
          };

          // Module              : activity index
          scope.translations['de_DE'].activity_index= {
                    first_activity : 'Erste Akt',
                    first_activity_tip : 'Erste Aktivität des Tages',
                    last_activity : 'Letzte Akt',
                    last_activity_tip : 'Letzte Aktivität des Tages',
                    total_activities : 'Aktivitäten gesamt',
          };

          // Module              : loader
          scope.translations['de_DE'].loading = 'Wird geladen...';
          scope.translations['de_DE'].serverError= {
                    message : 'Auf dem System ist ein Fehler aufgetreten. Versuchen Sie es später erneut.',
                    title : 'Serverfehler',
          };
          scope.translations['de_DE'].validationError= {
                    message : 'Fehler beim Validieren Ihrer Anfrage',
                    title : 'Validierungsfehler',
          };
          scope.translations['de_DE'].specificRuleMaxQuantityError= {
                    message : 'Eine Regel hat das Limit erreicht.',
                    title : 'Regellimit',
          };
          scope.translations['de_DE'].rulesMaxQuantityError= {
                    message : 'Maximale Anzahl an Regeln erreicht.',
                    title : 'Regellimit',
          };

          scope.translations['de_DE'].showMissingDaysOfWeekError= {
                    message : 'Mindestens Wochentag erforderlich.',
                    title : 'Wochentage fehlen',
          };

          scope.translations['de_DE'].noLocationError= {
                    message : 'Mindestens ein Standort erforderlich.',
                    title : 'Mindestens ein Standort fehlt',
          };

          scope.translations['de_DE'].xssError= {
            message : 'Name darf nicht &, <, >, ", \‘ oder / enthalten.',
            title : 'Validierungsfehler',
          };

          // Module              : buttons
          scope.translations['de_DE'].buttons= {
                    accept : ' Akzeptieren',
                    cancel : 'Abbrechencel',
                    ok : 'OK',
                    remove: 'entfernen'
          };

          // Module              : pop ups
          scope.translations['de_DE'].pop_ups= {
                    active_pop_up_message : 'Care@Home Active-Service wird vom Bedienteil dieser Firmware-Version nicht unterstützt. Möchten Sie den Bewohner wirklich speichern?',
                    active_pop_up_title : 'Care@Home Active',
          };

          // Module              : monthly report
          scope.translations['de_DE'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Badezimmer(Combined)',
                              BathroomSensor : 'Badezimmer',
                              BedroomSensor : 'Schlafzimmer',
                              DiningRoom : 'Esszimmer',
                              EP : 'EP',
                              FridgeDoor : 'Mahlzeit',
                              FrontDoor : 'Eingangstür',
                              LivingRoom : 'Wohnzimmer',
                              OtherRoom : 'Anderer Raum',
                              SmokeDetector : 'Rauchmelder',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'WC',
                              WaterLeakage : 'Wasseraustritt',
                    },
          };
          scope.translations['de_DE'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badezimmer',
                              BEDROOM_SENSOR : 'Schlafzimmer',
                              DINING_ROOM : 'Esszimmer',
                              FRIDGE_DOOR : 'Mahlzeit',
                              FRONT_DOOR : 'Nicht zuhause',
                              LIVING_ROOM : 'Wohnzimmer',
                              OTHER_ROOM : 'Anderer Raum',
                              TOILET_ROOM_SENSOR : 'WC',
                              unknown : 'Unbekannt',
                    },
                    activity : 'Aktivität',
          };
          scope.translations['de_DE'].periods= {
                    form : {
                              cancel : 'Abbrechencel',
                              commit : 'Übernehmen',
                              edit_title : 'Bearbeiten',
                              endTime : 'Endzeit',
                              is24Hours : 'Ganzer Tag',
                              name : 'Name',
                              new_title : 'Neuer Zeitraum',
                              startTime : ' Startzeit'
                    },
          };
          scope.translations['de_DE'].rules= {
                    descriptions : {
                              Absent : 'Wenn an einem ausgewählten <b>Standort</b> während eines ausgewählten <b>Zeitraums</b> keine <b>Besuche</b> identifiziert werden.<br/><br/>Wenn beispielsweise erwartet wird, dass der Bewohner das Frühstück am <b>Morgen</b> im <b>Esszimmer</b> einnimmt und der Raumsensor des Esszimmers am Morgen keinen <b>Besuch</b> aufzeichnet.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'Die Tür zuhause ist länger als die erwartete <b>Dauer</b> während des ausgewählten <b>Zeitraums</b> geöffnet.<br/><br/>Beispielsweise am <b>Morgen</b> lässt der Bewohner die Eingangstür mehr als eine <b>Stunde</b> offen, nachdem er vom täglichen Spaziergang zurückkommt.',
                              ExcessiveNumOfDetections : 'Während des ausgewählten <b>Zeitraums</b> übersteigt die Anzahl der Vorkommnisse die maximale Anzahl an <b>Vorkommnissen</b> am ausgewählten <b>Standort</b>.<br/><br/>Beispielsweise <b>nachts</b> wird die <b>Eingangstür</b> <b>ein paarmal</b> geöffnet und geschlossen.',
                              HighNumOfVisits : 'Während des ausgewählten <b>Zeitraums</b> übersteigt die Anzahl der Besuche am ausgewählten <b>Standort</b> die maximale Anzahl an <b>Besuchen</b>.<br/><br/>Beispielsweise <b>nachts</b> besucht der Bewohner die <b>Toilette</b> fünf Mal öfter als die erwarteten <b>zwei Mal</b>.',
                              Inactivity : 'Bei Anwesenheit des Bewohners zuhause während des ausgewählten <b>Zeitraums</b> gab es für die ausgewählte <b>Dauer</b> keine Aktivität.<br/><br/>Beispielsweise am <b>Nachmittag</b> ist der Bewohner im Wohnzimmer und es gab <b>drei Stunden</b> lang keine Aktivität.<br/><br/>Hinweis: Das System prüft die Inaktivität bereits vor Beginn des ausgewählten Zeitraums. Wenn der Bewohner beispielsweise vor dem ausgewählten Zeitraum bereits drei Stunden inaktiv war, wird die Regel sofort bei Beginn des Zeitraums ausgelöst.',
                              LongStay : 'Während des ausgewählten <b>Zeitraums</b> übersteigt die Gesamtdauer der Besuche an einem ausgewählten <b>Standort</b> die erwartete <b>Dauer</b>.<br/><br/>Beispielsweise der Bewohner besucht die <b>Toilette</b> mehrere Male am <b>Morgen</b> für insgesamt 60 Minuten länger, als die normale Morgenroutine von <b>20 Minuten</b>.',
                              LowNumOfDetections : 'Bei Anwesenheit des Bewohners während des ausgewählten <b>Zeitraums</b> sind die <b>Vorkommnisse</b> am ausgewählten <b>Standort</b> weniger als erwartet.<br/><br/>Beispielsweise der Bewohner verwendet die <b>Eingangstür</b> weniger oft als die üblichen <b>vier Mal</b> am <b>Tag</b>.',
                              LowNumOfVisits : 'Bei Anwesenheit des Bewohners während des ausgewählten <b>Zeitraums</b> sind die <b>Besuche</b> am ausgewählten <b>Standort</b> weniger als erwartet.<br/><br/>Beispielsweise der Bewohner besucht die <b>Toilette</b> weniger oft als die üblichen <b>vier Mal</b> am <b>Tag</b>.',
                              NoActivityDetected : 'Bei Anwesenheit des Bewohner werden am ausgewählten <b>Standort</b> während des ausgewählten <b>Zeitraums</b> keine Vorkommnisse erfasst.<br/><br/>Beispielsweise besucht der Bewohner normalerweise die <b>Toilette</b> am <b>Morgen</b>, aber es wird kein Besuch registriert.',
                              OutOfHome : 'Es scheint, der Bewohner ist nach Betätigen der Eingangstür länger nicht zuhause als die ausgewählte <b>Abwesenheitsdauer</b>.<br/><br/><b>Hinweis:</b> Die empfohlene <b>Abwesenheitsdauer</b> sollte mindestens 12 Stunden betragen.<br/><br/><b>Wichtig:</b> Für diesen Regeltyp ist nur eine Regel pro Bewohner zulässig.',
                              Presence : 'Während des ausgewählten <b>Zeitraums</b> wird der Bewohner am ausgewählten <b>Standort</b> erfasst und ist wach und in Bewegung.<br/><br/>Beispielsweise wird der Bewohner nach dem Aufstehen am <b>Morgen</b> in der <b>Toilette</b> erfasst.',
                              ShortStay : 'Während des ausgewählten <b>Zeitraums</b> ist die Gesamtdauer der <b>Besuche</b> an einem ausgewählten <b>Standort</b> niedriger als die erwartete <b>Dauer</b>.<br/><br/>Beispielsweise <b>mittags</b> nimmt der Bewohner 15 Minuten lang im <b>Esszimmer</b> das Mittagessen ein anstatt der erwarteten Dauer von <b>45 Minuten</b>.',
                              ShortStayBedroom : 'Bei Anwesenheit des Bewohners während des ausgewählten <b>Zeitraums</b>, entweder:<br/><br/>* Der Schlafzimmer-Sensor erkennt keine Aktivität<br/>-oder-<br/>* Die Dauer zwischen dem ersten und letzten Vorkommnis des Schlafzimmer-Sensors ist kürzer als die ausgewählte <b>Dauer</b>.<br/><br/>Beispielsweise ist der Bewohner im Wohnzimmer eingeschlafen und ist nie zu Bett gegangen.<br/><br/><b>Hinweis:</b> Dieser Regeltyp ist nur gültig, wenn im Schlafzimmer ein Sensor montiert wurde.',
                              SustainedActivity : 'Anhaltende Aktivität',
                              UnexpectedEntryExit : 'Während des ausgewählten <b>Zeitraums</b> an ausgewähltem(n) <b>Tag(en)</b> wurde die Eingangstür benutzt. Für die Dauer der definierten <b>Dauer</b> wird bei Verwendung der Eingangstür kein neuer Alarm ausgelöst<br/><br/>Beispielsweise <b>nachts</b> am <b>Mittwoch</b> betritt ein unerwarteter Besucher das Gebäude. Während der nächsten <b>10 Minuten</b> löst die Verwendung der Eingangstür keine neuen Alarme aus.<br/><br/><b>Wichtig:</b> Für diesen Regeltyp ist nur eine Regel pro Bewohner zulässig.',
                              UnexpectedPresence : 'Während des ausgewählten <b>Zeitraums</b> wird mindestens ein <b>Besuch</b> am ausgewählten <b>Standort</b> identifiziert.<br><br/>Beispielsweise es scheint, der Bewohner nimmt mitten in der <b>Nacht</b> eine Mahlzeit im <b>Esszimmer</b> ein. Umherirrend: Obwohl der Bewohner erwartungsgemäß während eines ausgewählten <b>Zeitraums</b> zuhause sein sollte - an einem oder mehreren <b>Tagen</b> - wird der Bewohner länger als die definierte <b>Abwesenheitsdauer</b> nach Verwenden der Eingangstür nicht erfasst.<br/><br/>Beispielsweise der Bewohner schläft in der Regel von <b>21:30 Uhr bis 06:00 Uhr</b>. <b>Dienstag nachts</b> um 02:00 Uhr verlässt der Bewohner über die Eingangstür das Gebäude und kommt nicht innerhalb <b>einer Stunde</b> zurück.<br/><br/><b>Hinweis</b>: Die empfohlene <b>Abwesenheitsdauer</b> sollte maximal eine Stunde betragen.<br/><br/><b>Wichtig:</b> Für diesen Regeltyp ist nur eine Regel pro Bewohner zulässig.',
                              Wandering : 'Obwohl der Bewohner erwartungsgemäß während eines ausgewählten <b>Zeitraums</b> zuhause sein sollte - an einem oder mehreren <b>Tagen</b> - wird der Bewohner länger als die definierte <b>Abwesenheitsdauer</b> nach Verwenden der Eingangstür nicht erfasst.<br/><br/>Beispielsweise der Bewohner schläft in der Regel von <b>21:30 Uhr bis 06:00 Uhr</b>. <b>Dienstag nachts</b> um 02:00 Uhr verlässt der Bewohner über die Eingangstür das Gebäude und kommt nicht innerhalb <b>einer Stunde</b> zurück.<br/><br/><b>Hinweis</b>: Die empfohlene <b>Abwesenheitsdauer</b> sollte maximal eine Stunde betragen.<br/><br/><b>Wichtig:</b> Für diesen Regeltyp ist nur eine Regel pro Bewohner zulässig.',
                    },
                    exit : {
                              message : 'Möchten Sie wirklich beenden?',
                              title : 'Änderungen werden nicht gespeichert',
                    },
                    form : {
                              cancel : 'Abbrechencel',
                              commit : 'Übernehmen',
                              daysOfWeek : 'Tage',
                              delay : 'Away Time',
                              description : ' Beschreibung',
                              duration : 'Duration',
                              immediatelyDuration : 'Sofort',
                              groupAllDoors : 'Alle Türen',
                              groupDoorId : 'Tür auswählen',
                              edit_title : 'Bearbeiten',
                              enabled : ' Aktiviert',
                              groupDeviceId : 'Raumtyp',
                              maxNumOfDetections : 'Vorkommnisse',
                              maxNumOfVisits : 'Besuche',
                              minNumOfDetections : ' Vorkommnisse',
                              minNumOfVisits : 'Besuche',
                              name : 'Name',
                              new_title : 'Neue Regel',
                              periodSystemId : 'Zeitraum',
                              ruleType : 'Regeltyp',
                              specificDeviceId : 'Raum',
                              none: 'Keiner',
                              firstLocationGroupDeviceId: '1. Standort',
                              secondLocationGroupDeviceId: '2. Standort',
                              thirdLocationGroupDeviceId: '3. Standort',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Abwesend',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Tür offen',
                              ExcessiveNumOfDetections : 'Hohe Anzahl an Vorkommnissen',
                              HighNumOfVisits : 'Hohe Anzahl an Besuchen',
                              Inactivity : 'Inaktivität',
                              LongStay : 'Langer Aufenthalt',
                              LowNumOfDetections : 'Niedrige Anzahl an Vorkommnissen',
                              LowNumOfVisits : 'Niedrige Anzahl an Besuchen',
                              NoActivityDetected : 'Keine Aktivität erfasst',
                              OutOfHome : 'Nicht zuhause',
                              Presence : 'Wach & Fit',
                              ShortStay : 'Short Stay',
                              ShortStayBedroom : 'Schlafzimmer Kurzaufenthalt',
                              SustainedActivity : 'Anhaltende Aktivität',
                              UnexpectedEntryExit : 'Unerwarteter Eintritt/Austritt',
                              UnexpectedPresence : 'Unerwartete Anwesenheit',
                              Wandering : 'Umherirrend',
                    },
          };
          scope.translations['de_DE'].times= {
                    all_day : 'Ganzer Tag',
                    days : 'Tage',
                    hours : 'Stunden',
                    minutes : 'Minuten',
                    seconds : 'Sekunden',
          };
          scope.translations['de_DE'].weekdays= {
                    friday : 'Freitag',
                    monday : 'Montag',
                    saturday : 'Samstag',
                    sunday : 'Sonntag',
                    thursday : 'Donnerstag',
                    tuesday : 'Dienstag',
                    wednesday : 'Mittwoch',
          };
})(this);

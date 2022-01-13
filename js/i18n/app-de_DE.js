/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : English - USA
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['de'] = scope.translations['de'] || {};
  scope.translations['de_DE'] = scope.translations['de_DE'] || {};

          // Module              : Alerts
          scope.translations['de'].alerts= {
                    home : {
                              in_home : 'Benutzer zuhause',
                              not_at_home : ' Nicht zuhause',
                    },
                    popup : {
                              error401 : {
                                message : 'Ihre Sesion ist nicht mehr gültig. Bitte,loggen Sie nochmal ein',
                                title : 'Sesion nicht mehr gültig'
                              },
                              detected : 'Erkannt:',
                              handle_it_later : 'Machen Sie das bitte später',
                              page_load_failed : {
                                        message : 'Die Webseite kann jetzt nicht geladen werden. Biite warten Sie einen Moment und versuchen Sie nochmal',
                                        title : 'Fehler beim Laden',
                              },
                              status : 'Zustand:',
                              statuses : {
                                        CLOSED : 'GESCHLOSSEN',
                                        IN_PROGRESS : ' WIRD AUSGEFÜHRT',
                                        NEW : 'NEU',
                                        VIEWED : 'GESEHEN',
                              },
                              titles : {
                                        alarm : 'ALARM',
                                        photos : 'NEUE PHOTOS',
                              },
                              updated : 'Aktualisiert:',
                              view_event : 'Ereignis sehen',
                    },
          };

          // Module              : activity index
          scope.translations['de'].activity_index= {
                    first_activity : 'Erste Akt',
                    first_activity_tip : 'Erste Aktivität des Tages',
                    last_activity : 'Letzte Akt.',  
                    last_activity_tip : 'Letzte Aktivität des Tages',
                    total_activities : 'Aktivitäten gesamt',
          };

          // Module              : JsMobile Other Items
          scope.translations['de'].buttons= {
                    close : 'Close',
          };


          // Module              : monthly report
          scope.translations['de'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Badezimmer',
                              BEDROOM_SENSOR : 'Schlafzimmer',
                              DINING_ROOM : ' Mahlzeit',
                              FRIDGE_DOOR : ' Kühlschranktür',
                              FRONT_DOOR : 'Nicht zuhause',
                              LIVING_ROOM : 'Wohnzimmer',
                              OTHER_ROOM : 'Other room',
                              TOILET_ROOM_SENSOR : 'Anderer Raum',
                              unknown : ' Unbekannt',
                    },
                    activity : 'Aktivität',
          };

          scope.translations['de'].forgot = {
            ok_message : 'Wir werden Ihnen eine Email mit einem Link senden. Öffnen Sir den Link und folgen Sie den Anweisungen.<br>Wenn Sie die Email in den nächsten Minuten nicht bekommen, Bitte kontaktieren Sie mit Support.',
            error_message : 'Entschuldigung, es gab ein Problem mit dem System. Bitte versuchen Sie später nochmal.',
        }
        scope.translations['de_DE'].forgot = {
            ok_message : 'Wir werden Ihnen eine Email mit einem Link senden. Öffnen Sir den Link und folgen Sie den Anweisungen.<br>Wenn Sie die Email in den nächsten Minuten nicht bekommen, Bitte kontaktieren Sie mit Support.',
            error_message : 'Entschuldigung, es gab ein Problem mit dem System. Bitte versuchen Sie später nochmal.',

        }

})(this);

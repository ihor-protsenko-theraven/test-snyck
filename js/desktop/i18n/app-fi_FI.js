/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Finnish
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['fi_FI'] = scope.translations['fi_FI'] || {};

          // Module              : combobox
          scope.translations['fi_FI'].combobox= {
                    not_in_list : 'Ole hyvä ja valitse listalta.',
          };

          // Module              : activity index
          scope.translations['fi_FI'].activity_index= {
                    first_activity : 'Ensimmäinen aktiviteetti',
                    first_activity_tip : 'Päivän ensimmäinen aktiviteetti',
                    last_activity : 'Viimeisin aktiviteetti',
                    last_activity_tip : 'Päivän viimeisin aktiviteetti',
                    total_activities : 'Aktiviteetit',
          };

          // Module              : loader
          scope.translations['fi_FI'].loading = 'Lataa…';
          scope.translations['fi_FI'].serverError= {
                    message : 'Pahoittelut, ongelma järjestelmässä. Yritä uudelleen myöhemmin.',
                    title : 'Palvelinvika',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['fi_FI'].buttons= {
                    accept : 'Hyväksy',
                    cancel : 'Peruuta',
                    ok : 'OK',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['fi_FI'].pop_ups= {
                    active_pop_up_message : 'pop_ups.active_pop_up_message',
                    active_pop_up_title : 'pop_ups.active_pop_up_title',
          };

          // Module              : monthly report
          scope.translations['fi_FI'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Yhdistetty kylpyhuone ja WC',
                              BathroomSensor : 'Kylpyhuone',
                              BedroomSensor : 'Makuuhuone',
                              DiningRoom : 'Keittiö',
                              EP : 'Hätänappi',
                              FridgeDoor : 'Jääkaapin ovi',
                              FrontDoor : 'Ulko-ovi',
                              LivingRoom : 'Olohuone',
                              OtherRoom : 'Muu huone',
                              SmokeDetector : 'Savuhälytin',
                              SPBP : 'Kiinteä hätänappi',
                              ToiletRoomSensor : 'WC',
                              WaterLeakage : 'Vuotohälytin',
                    },
          };
          scope.translations['fi_FI'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Kylpyhuone',
                              BEDROOM_SENSOR : 'Makuuhuone',
                              DINING_ROOM : 'Keittiö',
                              FRIDGE_DOOR : 'Ateria',
                              FRONT_DOOR : 'Ei kotona',
                              LIVING_ROOM : 'Olohuone',
                              OTHER_ROOM : 'Muu huone',
                              TOILET_ROOM_SENSOR : 'WC',
                              unknown : 'Tuntematon',
                    },
                    activity : 'aktiviteetti',
          };
          scope.translations['fi_FI'].periods= {
                    form : {
                              cancel : 'Peru',
                              commit : 'Suorita',
                              edit_title : 'Muuta',
                              endTime : 'Päättymisaika',
                              is24Hours : 'koko päivä',
                              name : 'Nimi',
                              new_title : 'Uusi ajanjakso',
                              startTime : 'Alkamissaika',
                    },
          };
          scope.translations['fi_FI'].rules= {
                    descriptions : {
                              Absent : 'Kun <b>käyntejä</b> ei ole havaittu  valitussa <b>huoneessa</b>, valittuna <b>ajanjaksona</b>.<br/><br/>Esimerkiksi asukkaan oletetaan syövän aamupalaa <b>keittiössä</b> <b>aamulla</b>, mutta asukasta ei <b>käynyt</b> keittiössä aamulla.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'Ovi on auki valittua <b>aikaa</b> pidempään vaittuna <b>ajanjaksona</b>. Esimerkiksi asukas jättää ulko-oven auki yli <b>60 minuutiksi</b> <b>aamulla</b>, palattuaan aamukävelyltään.',
                              ExcessiveNumOfDetections : 'Valittuna <b>ajanjaksona</b> yhteenlaskettu <b>liikehavaintojen määrä</b> valitussa <b>huoneessa</b> ylittää valitun määrän.<br/><br/>Esimerkiksi <b>ulko-ovi</b> suljetaan ja avataan yöllä yli valitun määrän.',
                              HighNumOfVisits : 'Valittuna <b>ajanjaksona</b> yhteenlaskettu <b>käyntien määrä</b> valitussa <b>huoneessa</b> ylittää valitun määrän.<br/><br/>Esimerkiksi asukas vierailee <b>WC:ssä yöllä viisi kertaa</b>, kun asukas oletetaan käyvän WC:ssä vain kahdesti.',
                              Inactivity : 'Asukkaan ollessa kotona valittuna <b>Ajanjaksona</b>, asunnosta ei ole havaittu aktiviteetteja valittuun <b>Aikaan</b>.<br/><br/>Esimerkiksi, asukas on ollut <b>iltapäivällä</b> olohuoneessa, mutta häntä ei ole havaittu liikkuvan <b>kolmeen tuntiin</b>.<br/><br/>Huomaa, että järjestelmä alkaa mittaamaan vähäistä aktiivisuutta ennen Ajanjakson alkua. Esimerkiksi, jos asukkaalla on ollut vähäinen aktiivisuus kolme tuntia ennen Ajanjaksoa, tulee sääntö tekemään hälytyksen heti Ajanjakson alussa.',
                              LongStay : 'Valittuna <b>ajanjaksona</b> yhteenlaskettu käyntien pituus valitussa <b>huoneessa</b> ylittää <b>valitun pituuden</b>.<br/><br/>Esimerkiksi asukas käy <b>WC:ssä aamulla</b> monta kertaa ja käyntien yhteenlaskettu kesto on yli <b>60 minuuttia</b>, normaalin 20 minuutin sijaan.',
                              LowNumOfDetections : 'Asukkaan ollessa kotona valittuna <b>ajanjaksona</b> <b>havaintojen määrä</b> valitussa <b>huoneessa</b> on vähemmän, kuin oletettaisiin.<br/><br/>Esimerkiksi asukas avaa <b>ulko-oven</b> <b>päivällä</b> vähemmän kuin <b>neljä</b> kertaa.',
                              LowNumOfVisits : 'Asukkaan ollessa kotona valittuna <b>ajanjaksona</b>, <b>käyntien määrä</b> valitussa <b>huoneessa</b> on vähemmän, kuin oletettaisiin.<br/><br/>Esimerkiksi asukas käy <b>WC:ssä</b> <b>päivän aikana</b> oletettua vähemmän.',
                              NoActivityDetected : 'Asukkaan ollessa kotona, asukasta ei havaita ollenkaan <b>valitussa huoneessa valittuna ajanjaksona</b>.<br/><br/>Esimerkiksi asukkaan oletetaan käyvän <b>WC:ssä</b> aamulla, mutta asukasta ei havaittu WC:ssä lainkaan.',
                              OutOfHome : 'Asukasta ei ole havaittu kotona yli valituun <b>poissaoloaikaan</b> ulko-oven avaamisesta lähtien.<br/><br/><b>HUOM.</b> Suositeltu aika on vähintään 12 tuntia.<br/> Vain yksi tämän tyypin sääntö sallitaan..',
                              Presence : 'Valittuna <b>ajanjaksona</b> asukas havaitaan valitussa <b>huoneessa<b>, merkiten sitä, että asukas on hereillä ja liikkeessä.<br/><br/>Esimerkiksi asukas havaitaan <b>WC:ssä</b> herättyään <b>aamulla</b>.',
                              ShortStay : 'Tiettynä <b>ajanjaksona</b> <b>käyntien</b> yhteenlaskettu pituus <b>tietyssä huoneessa</b> alittaa asetetun <b>keston</b>.<br/><br/>Esimerkiksi <b>iltapäivällä</b> asukas syö lounasta <b>keittiössä</b> vain 15 minuuttia oletetun 45 minuutin sijaan. ',
                              ShortStayBedroom : 'Asukkaan ollessa kotona valittuna <b>ajanjaksona</b>:<br/><br/>* Asukasta ei ole havaittu makuuhuoneessa ollenkaan<br/>- tai -<br/>* Asukkaan ensimmäisen ja viimeisen makuuhuonehavainnon välillä on vähemmän aikaa, kuin valittu <b>kesto</b>.<br/><br/>Esimerkiksi asukas nukahti olohuoneessa, eikä mennyt ollenkaan makuuhuoneeseen tai asukas nukkuu rauhattomasti.<br/><br/><b>HUOM.</b> Tämä sääntö vaatii sensorin makuuhuoneeseen.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'Kun valittuna <b>ajanjaksona</b> ja valittuina <b>päivinä</b> käytetään ulko-ovea lähetetään hälytys. Valitun <b>keston</b> aikana ei lähetetä uuttä hälytystä oven käytöstä.<br/><br/>Esimerkiksi <b>keskiviikkoiltana</b> hoitaja tulee käymään ja siitä lähetetään hälytys. Seuraavan <b>20 minuutin</b> aikana ulko-oven avaaminen ei lähetä hälytystä.<br/><br/><b>HUOM.</b> Vain yksi tämän tyypin sääntö sallitaan.',
                              UnexpectedPresence : 'Valittuna <b>ajanjaksona</b> ainakin yksi <b>käynti</b> havaitaan valitussa <b>huoneessa</b>.<br><br/>Esimerkiksi asukas menee <b>keittiöön</b> keskellä <b>yötä</b>.',
                              Wandering : 'Asukkaan ollessa kotona valittuna <b>ajanajksona</b> ja <b>päivänä</b> ei asukasta havaita asunnossa yli <b>poissaoloaikaan</b> ulko-oven käyttämisen jälkeen.<br/><br/>Esimerkiksi asukas poistuu asunnosta <b>yöllä</b> eikä palaa <b>10 minuuttiin</b>.<br/><br/><b>HUOM.</b> Suositeltu poissaoloaika on alle tunnin.<br/>Vain yksi tämän tyypin sääntö sallitaan.',
                    },
                    exit : {
                              message : 'Oletko varma että haluat lopettaa?',
                              title : 'Muutosta ei ole talletettu',
                    },
                    form : {
                              cancel : 'Peru',
                              commit : 'Suorita',
                              daysOfWeek : 'Päiviä',
                              delay : 'Poissaoloaika',
                              description : 'Seloste',
                              duration : 'Kesto',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Muuta',
                              enabled : 'Sallittu',
                              groupDeviceId : 'Huoneen nimi',
                              maxNumOfDetections : 'Liikehavainnot',
                              maxNumOfVisits : 'Vierailut',
                              minNumOfDetections : 'Liikehavainnot',
                              minNumOfVisits : 'Vierailut',
                              name : 'Nimi',
                              new_title : 'Uusi sääntö',
                              periodSystemId : 'Ajanjakso',
                              ruleType : 'Säännön tyyppi',
                              specificDeviceId : 'Huone',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Poissa',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Ovi auki',
                              ExcessiveNumOfDetections : 'Paljon liikehavaintoja',
                              HighNumOfVisits : 'Paljon käyntejä',
                              Inactivity : 'Vähäinen aktiivisuus',
                              LongStay : 'Pitkä oleskelu',
                              LowNumOfDetections : 'Vähän liikehavaintoja',
                              LowNumOfVisits : 'Vähän käyntejä',
                              NoActivityDetected : 'Ei aktiviteettia havaittu',
                              OutOfHome : 'Ei kotona',
                              Presence : 'Herännyt ja liikkeessä',
                              ShortStay : 'Lyhyt käynti',
                              ShortStayBedroom : 'Lyhyt käynti makuuhuoneessa',
                              SustainedActivity : 'rules.ruleTypes.SustainedActivity',
                              UnexpectedEntryExit : 'Odottamaton sisääntulo/lähtö',
                              UnexpectedPresence : 'Odottamaton läsnäolo',
                              Wandering : 'Vaeltelee',
                    },
          };
          scope.translations['fi_FI'].times= {
                    all_day : 'koko päivä',
                    days : 'Päivää',
                    hours : 'Tuntia',
                    minutes : 'Minuuttia',
                    seconds : 'Sekunttia',
          };
          scope.translations['fi_FI'].weekdays= {
                    friday : 'Perjantai',
                    monday : 'Maanantai',
                    saturday : 'Lauantai',
                    sunday : 'Sunnuntai',
                    thursday : 'Torstai',
                    tuesday : 'Tiistai',
                    wednesday : 'Keskiviikko',
          };
})(this);

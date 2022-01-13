/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Íslensku
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['is_IS'] = scope.translations['is_IS'] || {};

          // Module              : combobox
          scope.translations['is_IS'].combobox= {
                    not_in_list : 'Vinsamlegast veljið atriði úr lista.',
          };

          // Module              : activity index
          scope.translations['is_IS'].activity_index= {
                    first_activity : 'Fyrsta hreyfing',
                    first_activity_tip : 'Fyrsta hreyfing dagsins',
                    last_activity : 'Síðasta hreyfing.',
                    last_activity_tip : 'Síðasta hreyfing dagsins',
                    total_activities : 'Heildarfjöldi greininga',
          };

          // Module              : loader
          scope.translations['is_IS'].loading = 'hleð...';
          scope.translations['is_IS'].serverError= {
                    message : 'Afsakið, Það komu upp vandamál í kerfinu. Vinsamlegsat reynið aftur síðar.',
                    title : 'Villa í netþjón',
          };
          scope.translations['is_IS'].validationError= {
                    message : 'Afsakið, það komu upp vandamál með fyrirspurn þína.',
                    title : 'Villa',
          };
          scope.translations['is_IS'].specificRuleMaxQuantityError= {
                    message : 'Afsakið, en ein regla hefur náð hámarksgildum.',
                    title : 'Gildi reglu',
          };
          scope.translations['is_IS'].rulesMaxQuantityError= {
                    message : 'Afsakið, hámarksfjölda reglna hefur verið náð.',
                    title : 'Gildi reglu',
          };

          scope.translations['is_IS'].showMissingDaysOfWeekError= {
                    message : 'Afsakið, en vikudag vantar.',
                    title : 'vantar vikudaga',
          };

          scope.translations['is_IS'].noLocationError= {
                    message : 'Afsakið, en það vantar að lágmarki eina staðsetningu.',
                    title : 'A.m.k. ein staðsetning sem vantar',
          };

          scope.translations['en_US'].xssError= {
            message : 'Nöfn mega ekki hafa &, <, >, ", \', or /.',
            title : 'Villa',
          };

          // Module              : buttons
          scope.translations['is_IS'].buttons= {
                    accept : 'Samþykkja',
                    cancel : 'Hætta við',
                    ok : 'OK',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['is_IS'].pop_ups= {
                    active_pop_up_message : 'Þessi útgáfa af stjórnstöð styður ekki Active þjónustu Snjallhnapps. Ertu viss um að þú viljir vista íbúa?',
                    active_pop_up_title : 'Snjallhnappur Active',
          };

          // Module              : monthly report
          scope.translations['is_IS'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Baðherbergi samanlagt',
                              BathroomSensor : 'Baðherbergi',
                              BedroomSensor : 'Svefn',
                              DiningRoom : 'Eldhús',
                              EP : 'EP',
                              FridgeDoor : 'Hurð á ísskáp',
                              FrontDoor : 'Útidyr',
                              LivingRoom : 'Stofa',
                              OtherRoom : 'Annað herbergi',
                              SmokeDetector : 'Reykskynjari',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Salerni',
                              WaterLeakage : 'Vatnsleki',
                    },
          };
          scope.translations['is_IS'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Baðherbergi',
                              BEDROOM_SENSOR : 'Svefn',
                              DINING_ROOM : 'Eldhús',
                              FRIDGE_DOOR : 'máltíð',
                              FRONT_DOOR : 'Ekki heima',
                              LIVING_ROOM : 'Stofa',
                              OTHER_ROOM : 'Annað herbergi',
                              TOILET_ROOM_SENSOR : 'Salerni',
                              unknown : 'Óþekkt',
                    },
                    activity : 'virkni',
          };
          scope.translations['is_IS'].periods= {
                    form : {
                              cancel : 'Hætta við',
                              commit : 'Sækja',
                              edit_title : 'Breyta',
                              endTime : 'Lokatími',
                              is24Hours : 'Allan daginn',
                              name : 'Nafn',
                              new_title : 'Nýtt tímabil',
                              startTime : 'Upphafstími'
                    },
          };
          scope.translations['is_IS'].rules= {
                    descriptions : {
                              Absent : 'Þegar engin <b>heimsókn</b> er greind á valdri <b>staðsetningu</b>, á völdu <b>tímabili</b>.<br/><br/>Til dæmis, gert er ráð fyrir því að íbúi fái sér morgunmat í <b>eldhúsi</b> um <b>morgun</b> en skynjari i eldhúsi greindi ekki <b>heimsókn</b> í eldhúsið á því tímabili.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'Útidyr eru opnar lengur en skilgreind eðlileg <b>tímalengd</b>, á völdu <b>tímabili</b>.<br/><br/>Til dæmis, að <b>morgni</b>, skilur íbúi útidyr eftir opnar lengur en <b>klukkustund</b>, þegar hann kemur heim úr morgungöngu.',
                              ExcessiveNumOfDetections : 'Á völdu <b>tímabili</b>, fór fjöldi virknigreininga fram úr skilgreindum hámarksfjölda <b>greininga</b> á valdri <b>staðsetningu</b>.<br/><br/>Til dæmis, að<b>næturlagi</b>, voru <b>útidyr</b> opnaðar og lokaðar<b>í fjölda skipta</b>.',
                              HighNumOfVisits : 'Á völdu <b>tímabili</b>, hefur fjöldi heimsókna á valda <b>staðsetningu</b>, farið yfir skilgreindan hámarksfjölda <b>heimsókna</b>.<br/><br/>Til dæmis, að <b>næturlagi</b>, fer íbúi fimm sinnum á <b>baðherbergi</b> sem er oftar en skilgreint er í reglu sem er <b>tvisvar sinnum</b> á nótt.',
                              Inactivity : 'Á meðan íbúi er heima, á völdu <b>tímabili</b>, hefur engin virkni verið greind á völdu <b>tímabili</b>.<br/><br/>Til dæmis, <b>síðdegis</b>, er íbúi í stofunni en engin virkni greind síðustu <b>þrjár klukkustundir</b>.<br/><br/>Athugið að kerfið byrjar að skoða vanvirkni áður en skilgreint tímabil hefst. Til dæmis, ef íbúi ef vanvirkni hefur verið greind í þrjár klukkustundir áður en skilgreint tímabil hefst þá mun reglan senda frá sér viðvörun um leið og skilgreint tímabil hennar hefst.',
                              LongStay :  'Á völdu <b>tímabili</b>, hefur heildar tímalengd viðveru, sem greinist á valdri <b>staðsetningu</b>, verið meiri en vænt heildar tímalengd <b>viðveru</b>.<br/><br/>Til dæmis, íbúi fer á <b>baðherbergi</b> ínokkrum sinnum að <b>morgni</b> og heildar viðvera er 60 mínútur, sem er lengra en skilgreind heildar viðvera í reglu sem eru <b>20 mínútur</b>.',
                              LowNumOfDetections : 'Á meðan íbúi er heima, á völdu <b>tímabili</b>, eru <b>virkni greiningar</b> á valdri <b>staðsetningu</b> færri en skilgreint er í reglu.<br/><br/>Til dæmis, íbúi opnar <b>útidyr</b> sjaldnar en skilgreint er sem eðlileg notkun í reglu, sem er <b>fjórum</b> sinnum á <b>dag</b>.',
                              LowNumOfVisits : 'Á meðan íbúi er heima, á völdu <b>tímabili</b>, hefur fjöldi <b>heimsókna</b> sem greindar eru á valdri <b>staðsetningu</b> verið færri en gert er ráð fyrir.<br/><br/>Til dæmis, íbúi hefur farið á <b>baðherbergi</b> sjadlnar en vanalega sem er skilgreint í reglu <b>fjórum</b> sinnum á <b>dag</b>.',
                              NoActivityDetected : 'Þegar íbúi er heima, hafa engar virkni greiningar verið á valdri<b>staðsetningu</b>, á völdu <b>tímabili</b>.<br/><br/>Til dæmis, gert er ráð fyrir að íbúi noti <b>salerni</b> að <b>morgni</b>, en engin virkni er greind.',
                              OutOfHome : 'Íbúi virðist ekki vera heima í lengri tíma en skilgreind <b>fjarvera</b> er í reglu, eftir að útidyr eru notaðar.<br/><br/><b>Athugið:</b> Mælt er með að <b>tími fjarveru</b> sé a.m.k. 12 klst. <br/><br/><b>Mikilvægt:</b> Aðeins ein regla af þessari tegund getur verið í gildi fyrir hvern íbúa.',
                              Presence : 'Á völdu <b>tímabili</b>, greinist íbúi á valdri <b>staðsetningu</b>, sem gefur til kynna að íbúi sé vakandi og kominn á ról.<br/><br/>Til dæmis, viðvera íbúa hefur greinst á <b>baðherbergi</b> eftir að hann vaknar að <b>morgni</b>.',
                              ShortStay : 'Á völdu <b>tímabili</b>, hefur heildar tímalengd <b>heimsókna</b>, sem greinast á valdri <b>staðsetningu</b>, verið styttri en skilgreint er í reglu sem <b>heildar tímalengd</b>.<br/><br/>Til dæmis, um <b>hádegi</b> dvelur íbúi í 15 mínútur að fá sér hádegismat í <b>eldhúsi</b> en skilgreint er í reglu að eðlileg tímalengd séu <b>45 mínútna</b> hádegisverður.',
                              ShortStayBedroom : 'Á meðan íbúi er heima, á völdu <b>tímabili</b>, hefur annað hvort:<br/><br/>* Engin virkni greining frá skynjara í svefnherbergi<br/>-eða-<br/>* Tími milli fyrstu og síðustu virkni greiningar frá skynjara í svefnherbergi er styttri en valin <b>tímalengd</b>.<br/><br/>Til dæmis, íbúi sofnar í stofu og fer ekki inn í svefnherbergi.<br/><br/><b>Note:</b> Þessi regla virkar eingöngu ef hreyfiskynjari er settur upp í svefnherbergi.',
                              SustainedActivity : 'reglur.lýsingar.viðvarandihreyfing',
                              UnexpectedEntryExit : 'Á völdu <b>tímabili</b>, á völdum <b>degi(s)</b>, eftir að útidyr notaðar mun ný opnun á útdyrum í skilgreindan <b>tíma</b>, ekki framkalla nýja viðvörun frá kerfinu<br/><br/>Til dæmis, óvænt opnun á útidyrum greinist að <b>næturlagi</b> á <b>miðvikudegi</b>. Næstu <b>10 mínútur</b>, mun notkun á útidyrum ekki framkalla nýja viðvörun.<br/><br/><b>Mikilvægt:</b> Aðeins ein regla af þessari tegund getur verið í gildi fyrir hvern íbúa.',
                              UnexpectedPresence : 'Á völdu <b>tímabili</b>, hefur að lágmarki ein <b>heimsókn</b> verið greind á valdri <b>staðsetningu</b>.<br><br/>Til dæmis, íbúi virðist fá sér að borða í <b>eldhúsi</b> að <b>næturlagi</b>.',
                              Wandering : 'Þegar gert er ráð fyrir að íbúi sé heima, á völdu <b>tímabili</b>, á einum eða fleiri <b>dögum</b>, hefur engin virkni greinst hjá íbúa lengur en regla skilgreinir eðlilegan <b>tíma fjarveru</b> eftir að útidyr eru notaðar.<br/><br/>Til dæmis, íbúi sefur vanalega frá 22:30 til 7:00 að morgni. Á <b>þriðjudags nótt</b>, kl. 2:00, eru útidyr opnaðar og íbúi yfirgefur heimilið og kemur kemur ekki til baka innan <b>klukkustundar</b>.<br/><br/><b>Athugið</b>: Mælt er með að <b>tími fjarveru</b> sé í mesta lagi klukkustund.<br/><br/><b>Important:</b> Aðeins ein regla af þessari tegund getur verið í gildi fyrir hvern íbúa.',
                    },
                    exit : {
                              message : 'Ertu viss um að þú viljir hætta?',
                              title : 'Breytingar verða ekki vistaðar',
                    },
                    form : {
                              cancel : 'Hætta við',
                              commit : 'Sækja',
                              daysOfWeek : 'Dagar',
                              delay : 'Tími fjarveru',
                              description : 'Lýsing',
                              duration : 'Tímalengd',
                              immediatelyDuration : 'Strax',
                              groupAllDoors : 'Allar hurðir',
                              groupDoorId : 'Veldu hurð',
                              edit_title : 'Breyta',
                              enabled : 'Virkjað',
                              groupDeviceId : 'Tegund herbergis',
                              maxNumOfDetections : 'Greiningar',
                              maxNumOfVisits : 'Heimsóknir',
                              minNumOfDetections : 'Greiningar',
                              minNumOfVisits : 'Heimsóknir',
                              name : 'Nafn',
                              new_title : 'Ný regla',
                              periodSystemId : 'Tímabil',
                              ruleType : 'Tegund reglu',
                              specificDeviceId : 'Herbergi',
                              none: 'Engin',
                              firstLocationGroupDeviceId: '1. staðsetning',
                              secondLocationGroupDeviceId: '2. staðsetning',
                              thirdLocationGroupDeviceId: '3. staðsetning',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Fjarverandi',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Hurð opin',
                              ExcessiveNumOfDetections : 'Mikill fjöldi virknigreininga',
                              HighNumOfVisits : 'Mikill fjöldi heimsókna',
                              Inactivity : 'Vanvirkni',
                              LongStay : 'Löng dvöl',
                              LowNumOfDetections : 'Lítill fjöldi virknigreininga',
                              LowNumOfVisits : 'Lítill fjöldi heimsókna',
                              NoActivityDetected : 'Engin virkni greind',
                              OutOfHome : 'Ekki heima',
                              Presence : 'Komin á ról',
                              ShortStay : 'Stutt viðvera',
                              ShortStayBedroom : 'Stutt viðvera á baðherbergi',
                              SustainedActivity : 'Viðvarandi hreyfing greinist',
                              UnexpectedEntryExit : 'Óvænt inn-/útganga',
                              UnexpectedPresence : 'Óvænt viðvera',
                              Wandering : 'Ráp',
                    },
          };
          scope.translations['is_IS'].times= {
                    all_day : 'Allan daginn',
                    days : 'Dagar',
                    hours : 'Klukkustundir',
                    minutes : 'Mínútur',
                    seconds : 'Sekúndur',
          };
          scope.translations['is_IS'].weekdays= {
                    friday : 'Föstudagur',
                    monday : 'Mánudagur',
                    saturday : 'Laugardagur',
                    sunday : 'Sunnudagur+',
                    thursday : 'Fimmtudagur',
                    tuesday : 'Þriðjudagur',
                    wednesday : 'Miðvikudagur',
          };
})(this);

/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : Íslensku - IS
# Parse               : Javascript Mob C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['is'] = scope.translations['is'] || {};
  scope.translations['is_IS'] = scope.translations['is_IS'] || {};

          // Module              : Alerts
          scope.translations['is'].alerts= {
                    home : {
                              in_home : 'Íbúi er heima',
                              not_at_home : 'Ekki heima',
                    },
                    popup : {
                              error401 : {
                                message : 'Tímin þinn rennur. Reynið aftur síðar.',
                                title : 'Invalid session'
                              },
                              detected : 'Greint:',
                              handle_it_later : 'Afgreiða síðar',
                              page_load_failed : {
                                        message : 'Síðan sem þú baðst um hleðst ekki inn núna, reynið aftur síðar',
                                        title : 'Tókst ekki að hlaða inn síðu',
                              },
                              status : 'Staða:',
                              statuses : {
                                        CLOSED : 'Lokað',
                                        IN_PROGRESS : 'Í vinnslu',
                                        NEW : 'NÝTT',
                                        VIEWED : 'SKOÐAÐ',
                              },
                              titles : {
                                        alarm : 'ATVIK',
                                        photos : 'NEW PHOTOS',
                              },
                              updated : 'Uppfært.',
                              view_event : 'Skoða atvik',
                    },
          };

          // Module              : activity index
          scope.translations['is'].activity_index= {
                    first_activity : 'Fyrsta virkni',
                    first_activity_tip : 'Fyrsta virkni dagsins',
                    last_activity : 'Síðasta virkni',
                    last_activity_tip : 'Síðasta virkni dagsins',
                    total_activities : 'Samtals starfsemi',
          };

          // Module              : JsMobile Other Items
          scope.translations['is'].buttons= {
                    close : 'Loka',
          };


          // Module              : monthly report
          scope.translations['is'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Baðherbergi',
                              BEDROOM_SENSOR : 'Svefnherbergi',
                              DINING_ROOM : 'Borðstofa',
                              FRIDGE_DOOR : 'Máltíð',
                              FRONT_DOOR : 'Ekki heima',
                              LIVING_ROOM : 'Stofa',
                              OTHER_ROOM : 'Annað herbergi',
                              TOILET_ROOM_SENSOR : 'Salerni',
                              unknown : 'Óþekkt',
                    },
                    activity : 'virkni',
          };

          scope.translations['is'].forgot = {
               ok_message : 'Við sendum þér tölvupóst með slóð til þess að skipta um lykilorð. Opnaðu slóðina og fylgdu leiðbeiningunum, ef tölvupósturinn berst ekki, hafðu samband við þjónustuver.',
               error_message : 'Afsakið, upp hefur komið villa. Reynið aftur síðar.',
          }
          scope.translations['is_IS'].forgot = {
               ok_message : 'Við sendum þér tölvupóst með slóð til þess að skipta um lykilorð. Opnaðu slóðina og fylgdu leiðbeiningunum, ef tölvupósturinn berst ekki, hafðu samband við þjónustuver.',
               error_message : 'Afsakið, upp hefur komið villa. Reynið aftur síðar.',
          }
          scope.translations['is'].validations = {
               passwordstrength : 'Þetta lykilorð er ekki nógu öryggt, reyndu aftur.',
               passwordhistory : 'Þetta lykilorð hefur verið nýlega notað, reyndu annað.',
               passparam_wrong : 'Sumar breytur eru rangar, reyndu aftur.',
               servererror : 'Afsakið, upp hefur komið villa. Reynið aftur síðar.',
               passwordchanged: 'Lykilorð hefur verið uppfært.',
               required : 'Þennan reit þarf að fylla út',
          }
          
          scope.translations['is_IS'].validations = {
               passwordstrength : 'Þetta lykilorð er ekki nógu öryggt, reyndu aftur.',
               passwordhistory : 'Þetta lykilorð hefur verið nýlega notað, reyndu annað.',
               passparam_wrong : 'Sumar breytur eru rangar, reyndu aftur.',
               servererror : 'Afsakið, upp hefur komið villa. Reynið aftur síðar.',
               passwordchanged: 'Lykilorð hefur verið uppfært.',
               required : 'Þennan reit þarf að fylla út',
          }

})(this);

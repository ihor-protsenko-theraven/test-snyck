/*##################################################
# Product             : Care @ Home
# Version             : 2.2.8
# Client              : Default
# Date                : 13/02/2017
# Translation Versión : Third version of translations for Care @ Home version 2.2.8
# Language            : Spanish - Spain
# Parse               : JavaScript
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['es'] = scope.translations['es'] || {};

          // Module              : activity index
          scope.translations['es'].activity_index= {
                    first_activity : 'Primera actividad',
                    first_activity_tip : 'Primera actividad del día',
                    last_activity : 'Última actividad.',
                    last_activity_tip : 'Última actividad del día',
                    total_activities : 'Actividades totales',
          };

          // Module              : Alerts
          scope.translations['es'].alerts= {
                    home : {
                              in_home : 'El usuario está en casa',
                              not_at_home : 'No está en casa',
                    },
                    popup : {
                              error401 : {
                                message : 'Your session is no longer valid. Please login again',
                                title : 'Invalid session'
                              },
                              detected : 'Detectado:',
                              handle_it_later : 'Gestionar más tarde',
                              status : 'Estado:',
                              statuses : {
                                        CLOSED : 'CERRADO',
                                        IN_PROGRESS : 'EN PROGRESO',
                                        NEW : 'NUEVO',
                                        VIEWED : 'VISTO',
                              },
                              titles : {
                                        alarm : 'ALARMA',
                                        photos : 'FOTOS NUEVAS',
                              },
                              updated : 'Actualizado:',
                              view_event : 'Ver evento',
                              page_load_failed: {
                                title: 'Fallo Cargando Página',
                                message: 'La página solicitada no se puede cargar en este momento. Por favor, espere unos minutos e inténtelo de nuevo'
                              }
                    },
          };

          // Module              : JsMobile Other Items
          scope.translations['es'].buttons= {
                    close : 'Cerrar',
          };
          scope.translations['es'].validations= {
                    required : 'este campo es obligatorio',
          };

          // Module              : monthly report
          scope.translations['es'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Baño',
                              BEDROOM_SENSOR : 'Dormitorio',
                              DINING_ROOM : 'Comedor',
                              FRIDGE_DOOR : 'Comida',
                              FRONT_DOOR : 'Fuera de casa',
                              LIVING_ROOM : 'Sala de estar',
                              OTHER_ROOM : 'Otra habitación',
                              TOILET_ROOM_SENSOR : 'Aseo',
                              unknown : 'Desconocido',
                    },
                    activity : 'actividad',
          };
})(this);

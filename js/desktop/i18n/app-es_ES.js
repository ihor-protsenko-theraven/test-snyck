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
  scope.translations['es_ES'] = scope.translations['es_ES'] || {};

          // Module              : activity index
          scope.translations['es_ES'].activity_index= {
                    first_activity : 'Primera  actividad',
                    first_activity_tip : 'Primera actividad del día',
                    last_activity : 'Última actividad.',
                    last_activity_tip : 'Última actividad del día',
                    total_activities : 'Actividades totales',
          };

          // Module              : buttons
          scope.translations['es_ES'].buttons= {
                    accept : 'Aceptar',
                    cancel : 'Cancelar',
                    ok : 'OK',
                    remove: 'Remove'
          };

          // Module              : combobox
          scope.translations['es_ES'].combobox= {
                    not_in_list : 'Seleccione un elemento de la lista.',
          };

          // Module              : loader
          scope.translations['es_ES'].loading = 'cargando...';
          scope.translations['es_ES'].serverError= {
                    message : 'Lo sentimos, hubo un problema en el sistema. Inténtelo de nuevo más tarde.',
                    title : 'Error del Servidor',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Ha habido un error validando tu petición',
                    title : 'Error de validación',
          };

          // Module              : monthly report
          scope.translations['es_ES'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Baño combinado',
                              BathroomSensor : 'Baño',
                              BedroomSensor : 'Dormitorio',
                              DiningRoom : 'Cocina',
                              EP : 'EP',
                              FridgeDoor : 'Puerta del frigorífico',
                              FrontDoor : 'Puerta principal',
                              LivingRoom : 'Sala de estar',
                              OtherRoom : 'Otra habitación',
                              SmokeDetector : 'Detector de humo',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Aseo',
                              WaterLeakage : 'Fuga de agua',
                    },
          };
          scope.translations['es_ES'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Baño',
                              BEDROOM_SENSOR : 'Dormitorio',
                              DINING_ROOM : 'Cocina',
                              FRIDGE_DOOR : 'Comida',
                              FRONT_DOOR : 'Fuera de casa',
                              LIVING_ROOM : 'Sala de estar',
                              OTHER_ROOM : 'Otra habitación',
                              TOILET_ROOM_SENSOR : 'Aseo',
                              unknown : 'Desconocido',
                    },
                    activity : 'actividad',
          };
          scope.translations['es_ES'].periods= {
                    form : {
                              cancel : 'Cancelar',
                              commit : 'Aplicar',
                              edit_title : 'Editar',
                              endTime : 'Hora de finalización',
                              is24Hours : 'Todo el dia',
                              name : 'Nombre',
                              new_title : 'Nuevo período',
                              startTime : 'Hora de inicio',
                    },
          };
          scope.translations['es_ES'].rules= {
                    descriptions : {
                              Absent : 'Cuando no se identifican Visitas en un Tipo de Habitación elegido, durante un Período determinado. Por ejemplo, se espera que el residente desayune en la cocina por la mañana y el sensor de cocina no identifica una visita por la mañana.',
                              AtHomeForTooLong : 'The resident appears to be at home for longer than the chosen <b>Home Time.</b><br/><br/> <b>Important:</b> Only one rule based on this rule type is allowed per resident.',
                              DoorOpen : 'La puerta de casa está abierta durante más tiempo del esperado en el período elegido. Por ejemplo, por la mañana, el residente deja la puerta principal abierta durante más de una hora, al regresar de la caminata diaria al parque.',
                              ExcessiveNumOfDetections : 'Durante el Período determinado, el número de detecciones supera el máximo de Detecciones en el Tipo de Habitación elegido. Por ejemplo, durante la noche, la puerta principal se abre y se cierra varias veces.',
                              HighNumOfVisits : 'Durante el <b> Periodo </ b> elegido, el número de visitas al <b> tipo de habitación </ b> elegido excede el número máximo de <b> visitas </ b>. /> Por ejemplo, durante la <b> noche </ b>, el residente visita el <b> baño </ b> cinco veces,  <b> dos veces </ b>  superior a lo esperado por noche.',
                              Inactivity : 'Mientras está en su casa, durante el <b> Periodo </ b> elegido, el residente no ha sido detectado para la <b> Duración </ b> escogida. <br/> <br/> Por ejemplo, por la <B> tarde </ b>, el residente está en la sala de estar y no se detecta movimiento durante al menos <b> tres horas </ b>. <br/> <br/> <b> Importante: B> Sólo se permite una regla de este tipo por residente.',
                              LongStay : 'La duración total de las visitas identificadas en un tipo de habitación y en un periodo determinado está por encima de lo previsto. Por ejemplo, el residente va al baño varias veces por la mañana y suma un total de 60 minutos, por encima de la Rutina matutina de 20 minutos.',
                              LowNumOfDetections : 'Mientras que el residente está en casa, durante el período elegido, las detecciones en el tipo de habitación son inferiores a las esperadas. Por ejemplo, el residente usa la puerta principal menos de las cuatro veces al día que marca su rutina.',
                              LowNumOfVisits : 'Mientras que el residente está en su casa, durante el <b> Periodo </ b> elegido, el número de <b> Visitas </ b> identificadas en un <b> Tipo de Habitación </ b>  es menor de lo esperado. /> <br/> Por ejemplo, el residente usa <b> el baño </ b> menos de lo habitual que son  <b> cuatro </ b> veces al <b> día </ b>.',
                              NoActivityDetected : 'Cuando el residente está en casa, no se realizan detecciones en el <b> tipo de habitación </ b> escogido, durante el <b> período </ b> elegido. <br/> <br/> Por ejemplo, se espera que el residente use el <b> baño </ b> por la <b> mañana </ b>, pero no se detecta actividad.',
                              OutOfHome : 'El residente está ausente de su casa por un tiempo superior al establecido después de que se use la puerta principal. Nota: El tiempo de ausencia recomendado debe ser de al menos 12 horas. Importante: Sólo se permite una regla de este tipo por residente.',
                              Presence : 'Durante el <b> Periodo </ b> establecido, el residente es detectado en el <b> Tipo de Habitación </ b> elegido, indicando que el residente está despierto y moviéndose. <br/> <br/> Por ejemplo, el residente es detectado en el <b> baño </ b> después de despertar por la <b> mañana </ b>.',
                              ShortStay : 'Durante el período establecido, la duración total de las visitas identificadas en un tipo de habitación elegido, es inferior a la prevista. Por ejemplo, al mediodía, el residente tiene un almuerzo de 15 minutos en la cocina en lugar de los 45 minutos acostumbrados.',
                              ShortStayBedroom : 'Mientras el residente está en su casa, durante el <b> Período </ b> establecido se dan las siguientes circunstancias: <br/> <br/> * no hay detecciones de actividad desde el sensor de dormitorio <br/> - o - <br/> * el tiempo transcurrido entre la primera y la última detección del sensor del dormitorio es más corto que el <b> Duración </ b> elegido. <br/> <br/> Por ejemplo, el residente se quedó dormido en la sala de estar y nunca se acostó. <br/> <br/> <b> Nota: </ b> Este tipo de regla sólo es válida si se instala un sensor en el dormitorio.',
                              UnexpectedEntryExit : 'Durante el Período establecido, para un Día (s) concreto, la puerta principal es usada. Durante ese tiempo la puerta principal no provocará nuevas alarmas. Por ejemplo, un visitante inesperado entra en la casa la noche del miércoles. Durante los siguientes 10 minutos, el uso de la puerta principal no activará nuevas alarmas. Importante: Sólo se permite una regla de este tipo por residente.',
                              UnexpectedPresence : 'Durante el período establecido, se identifica al menos una visita al tipo de habitación elegido. Por ejemplo, el residente parece tener una comida en la cocina en medio de la noche.',
                              Wandering : 'A pesar de que se espera que el residente esté en su casa durante un período establecido, en uno o más Días escogidos, no se le detecta por un tiempo superior al rutinario para ausencia después de que la puerta principal haya sido usada. Por ejemplo, el residente habitualmente duerme de 9:30 PM a 6 AM. El martes por la noche, a las 2 de la madrugada, el residente sale de su casa por la puerta principal y transcurrida una hora no ha regresado. Nota: El tiempo de ausencia recomendado debe ser como máximo una hora. Importante: Sólo se permite una regla de este tipo por residente.',
                    },
                    exit : {
                              message : '¿Seguro que quieres salir?',
                              title : 'Los cambios no se guardarán',
                    },
                    form : {
                              cancel : 'Cancelar',
                              commit : 'Aceptar',
                              daysOfWeek : 'Días',
                              delay : 'Tiempo fuera de casa',
                              description : 'Descripción',
                              duration : 'Duración',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Editar',
                              enabled : 'Habilitado',
                              groupDeviceId : 'Tipo de habitación',
                              maxNumOfDetections : 'Detecciones',
                              maxNumOfVisits : 'Visitas',
                              minNumOfDetections : 'Detecciones',
                              minNumOfVisits : 'Visitas',
                              name : 'Nombre',
                              new_title : 'Nueva regla',
                              periodSystemId : 'Período',
                              ruleType : 'Tipo de regla',
                              specificDeviceId : 'Habitación',
                              none: 'None',
                              firstLocationGroupDeviceId: '1st Location',
                              secondLocationGroupDeviceId: '2nd Location',
                              thirdLocationGroupDeviceId: '3rd Location',
                              homeTime : 'Home Time'
                    },
                    ruleTypes : {
                              Absent : 'Ausente',
                              AtHomeForTooLong : 'At Home For Too Long',
                              DoorOpen : 'Puerta abierta',
                              ExcessiveNumOfDetections : 'Alto número de detecciones',
                              HighNumOfVisits : 'Alto número de visitas',
                              Inactivity : 'Inactividad',
                              LongStay : 'Larga estancia',
                              LowNumOfDetections : 'Bajo número de detecciones',
                              LowNumOfVisits : 'Bajo número de visitas',
                              NoActivityDetected : 'No se detecta actividad',
                              OutOfHome : 'No está en casa',
                              Presence : 'Despierto y activo',
                              ShortStay : 'Corta estancia',
                              ShortStayBedroom : 'Dormitorio de corta estancia',
                              SustainedActivity : 'Actividad Sostenida',
                              UnexpectedEntryExit : 'Entrada / salida inesperada',
                              UnexpectedPresence : 'Presencia inesperada',
                              Wandering : 'Errante',
                    },
          };
          scope.translations['es_ES'].times= {
                    all_day : 'Todo el dia',
                    days : 'Días',
                    hours : 'Horas de trabajo',
                    minutes : 'Minutos',
                    seconds : 'Segundos',
          };
          scope.translations['es_ES'].weekdays= {
                    friday : 'viernes',
                    monday : 'lunes',
                    saturday : 'sábado',
                    sunday : 'domingo',
                    thursday : 'jueves',
                    tuesday : 'martes',
                    wednesday : 'miércoles',
          };
})(this);

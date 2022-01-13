/*##################################################
# Product             : Care @ Home
# Version             : 2.4
# Client              : Default
# Date                : 06/11/2017
# Translation Versión : First version of translations for Care @ Home version 2.4
# Language            : French
# Parse               : JavaScript C@H
##################################################*/
(function (scope){
  scope.translations = scope.translations || {};
  scope.translations['fr_FR'] = scope.translations['fr_FR'] || {};

          // Module              : combobox
          scope.translations['fr_FR'].combobox= {
                    not_in_list : 'Veuillez sélectionner un élément dans la liste.',
          };

          // Module              : activity index
          scope.translations['fr_FR'].activity_index= {
                    first_activity : 'Premier acte',
                    first_activity_tip : 'Première activité de la journée',
                    last_activity : 'Dernier acte',
                    last_activity_tip : 'Dernière activité de la journée',
                    total_activities : 'Total des activités',
          };

          // Module              : loader
          scope.translations['fr_FR'].loading = 'chargement...';
          scope.translations['fr_FR'].serverError= {
                    message : 'Désolé, il y a eu un problème sur le système. Veuillez réessayer plus tard.',
                    title : 'Erreur du serveur',
          };
          scope.translations['en_US'].validationError= {
                    message : 'Sorry, there was a problem validating your request.',
                    title : 'Validation error',
          };

          // Module              : buttons
          scope.translations['fr_FR'].buttons= {
                    accept : 'Acceptez',
                    cancel : 'Annuler',
                    ok : 'D\'accord',
                    remove: 'Remove'
          };

          // Module              : pop ups
          scope.translations['fr_FR'].pop_ups= {
                    active_pop_up_message : 'La version du micrologiciel du transmetteur ne prend pas en charge le service Care@Home Active. Êtes-vous sûr de vouloir sauver le bÃ©nÃ©ficiaire?',
                    active_pop_up_title : 'Care @ Home Actif',
          };

          // Module              : monthly report
          scope.translations['fr_FR'].devices= {
                    activityTypes : {
                              BathroomCombined : 'Salle de bain combinée',
                              BathroomSensor : 'Salle de bains',
                              BedroomSensor : 'Chambre',
                              DiningRoom : 'Cuisine',
                              EP : 'EP',
                              FridgeDoor : 'Porte du réfrigérateur',
                              FrontDoor : 'Porte d\'entrée',
                              LivingRoom : 'Salon',
                              OtherRoom : 'Autre chambre',
                              SmokeDetector : 'Détecteur de fumée',
                              SPBP : 'SPBP',
                              ToiletRoomSensor : 'Toilettes',
                              WaterLeakage : 'Fuite d\'eau',
                    },
          };
          scope.translations['fr_FR'].monthly_report= {
                    activities : {
                              BATHROOM_SENSOR : 'Salle de bains',
                              BEDROOM_SENSOR : 'Chambre',
                              DINING_ROOM : 'Cuisine',
                              FRIDGE_DOOR : 'Repas',
                              FRONT_DOOR : 'Hors de la maison',
                              LIVING_ROOM : 'Salon',
                              OTHER_ROOM : 'Autre chambre',
                              TOILET_ROOM_SENSOR : 'Toilettes',
                              unknown : 'Inconnu',
                    },
                    activity : 'activité',
          };
          scope.translations['fr_FR'].periods= {
                    form : {
                              cancel : 'Annuler',
                              commit : 'Appliquer',
                              edit_title : 'editer',
                              endTime : 'Heure fin',
                              is24Hours : 'Toute la journée',
                              name : 'Nom de la période',
                              new_title : 'Nouvelle période',
                              startTime : 'Heure de début',
                    },
          };
          scope.translations['fr_FR'].rules= {
                    descriptions : {
                              Absent : 'Quand aucune <b>visite</b> n\'est identifiée dans un <b>type de pièce</b> choisi, pendant une <b>période</b> choisie. <br/> <br/> Par exemple, on s\'attend à ce que  le bÃ©nÃ©ficiaire prenne le petit déjeuner dans la <b>salle à manger</b> le <b>matin</b> et le capteur de la salle à manger n\'identifie pas de <b>visite</b> le matin.',
                              AtHomeForTooLong : 'Le bÃ©nÃ©ficiaire semble être à la maison depuis plus longtemps que l`heure définie pour la présence dans la maison <br/><br/> <b>Important:</b> Une seule règle basée sur ce type de règle est autorisée par bÃ©nÃ©ficiaire',
                              DoorOpen : 'La porte à la maison est ouverte plus longtemps que la durée <b>prévue</b>, pendant la <b>période</b> choisie. <br/> <br/> Par exemple, dans la <b>matinée</b>, le bÃ©nÃ©ficiaire laisse la porte d\'entrée ouverte plus d\'une <b> heure </ b>, en revenant de sa promenade quotidienne au parc.',
                              ExcessiveNumOfDetections : 'Pendant la <b>période</b> choisie, le nombre de détections dépasse le nombre maximum de <b>Détections</b> choisi dans le <b>Type de pièce</b> choisi. <br/> <br/> Par exemple, pendant la <b>nuit</b>, la <b>porte d\'entrée</b> est ouverte et fermée un bon <b>nombre de fois</b>.',
                              HighNumOfVisits : 'Pendant la <b>période</b> choisie, le nombre de visites au <b>Type de pièce</b> choisi dépasse le nombre maximum de <b>visites</b>. <br/> <br/> Par exemple, pendant la <b>nuit</b>, le bÃ©nÃ©ficiaire visite les <b>toilettes</b> cinq fois, plus que les <b>deux fois</b> attendus par nuit .',
                              Inactivity : 'Tant que le bÃ©nÃ©ficiaire est chez lui, pendant la <b>période</b> choisie, le bÃ©nÃ©ficiaire n\'a pas été détecté dans la <b>durée</b> choisie. <br/> <br/> Par exemple, dans l\'<b>après-midi</b>, le bÃ©nÃ©ficiaire est dans le salon et n\'est pas détecté en train de bouger depuis au moins <b>trois heures</b>. <br/> <br/> <b>Important:</b> Une seule règle basée sur ce type de règle est autorisée par bÃ©nÃ©ficiaire.',
                              LongStay : 'Pendant la période <b>Choisie</b>, la durée totale des visites, identifiée dans un <b>Type de pièce</b> choisi, est supérieure à la <b>Durée</ b> prévue. <br/> <br/> Par exemple, le bÃ©nÃ©ficiaire se rend <b>aux toilettes</b> plusieurs fois le <b> matin </b> pour une durée totale de 60 minutes, plus longue que la routine matinale de <b> 20 minutes </b>.',
                              LowNumOfDetections : 'Pendant que le bÃ©nÃ©ficiaire est chez lui, pendant la période <b>choisie</b>, les <b>Détections</b> dans le <b>type de pièce</b> choisi sont plus faibles que prévu. <br/> < br/> Par exemple, le bÃ©nÃ©ficiaire utilise la <b>porte d\'entrée</b> moins que l\'utilisation courante de <b>quatre</b> fois par <b>jour </b>.',
                              LowNumOfVisits : 'Pendant que le bÃ©nÃ©ficiaire est à la maison, durant la <b>période</b> choisie, le nombre de <b>visites</b> identifiées dans un <b>type de pièce</b> choisi est plus faible que prévu.<br/> <br/> Par exemple, le bÃ©nÃ©ficiaire utilise les <b>toilettes</b> moins que d\'habitude, <b>quatre</b> fois par <b>jour</b>.',
                              NoActivityDetected : 'Quand le bÃ©nÃ©ficiaire est à la maison et qu\'il n\'y a pas de détections pendant la période choisie. Un exemple de cette règle : le bÃ©nÃ©ficiaire est censé utiliser les toillettes le matin mais il n\'as pas été détecté',
                              OutOfHome : 'Le bÃ©nÃ©ficiaire ne semble pas être à la maison plus longtemps que le <b>temps à l\'extérieur</b> choisi après l\'utilisation de la porte d\'entrée. <br/> <br/> <b>Note:</b>Le < b>temps à l\'extérieur</b> recommendé devrait être d\'au moins 12 heures. <br/> <br/> <b>Important:</b> Une seule règle basée sur ce type de règle est autorisée par bÃ©nÃ©ficiaire.',
                              Presence : 'Pendant la période <b> choisie </b>, le bÃ©nÃ©ficiaire est détecté dans le <b>Type de pièce</b> choisi, indiquant que le bÃ©nÃ©ficiaire est éveillé et se déplace. <br/> <br/> Par exemple, le bÃ©nÃ©ficiaire est détecté dans les <b> toilettes </ b> après s\'être réveillé le <b> matin </ b>.',
                              ShortStay : 'Pendant la <b>Période</b> choisie, la durée totale des <b>Visites</b>, identifiées dans un <b>type de pièce</b> choisi, est inférieure à la <b>Durée prévue</b>. <br/> <br/> Par exemple, à <b>midi</b>, le bÃ©nÃ©ficiaire a un déjeuner de 15 minutes dans la <b>salle à manger</b> au lieu du déjeuner de <b>45 minutes</b> attendu.',
                              ShortStayBedroom : 'Pendant que le bÃ©nÃ©ficiaire est chez lui, durant la <b> Période </b> choisie, soit: <br/> <br/> * Aucune détection d\'activité n\'est effectuée par le capteur de la chambre <br/> -ou-- <br/>* Le temps entre la première et la dernière détections du  capteur de la chambre est plus court que la <b> durée </ b> choisie. <br/> <br/> Par exemple, le bÃ©nÃ©ficiaire s\'est endormi dans le salon et n\'est jamais allé se coucher. <br/> <br/> <b> Note: </ b> Ce type de règle n\'est valide que si un capteur est installé dans la chambre.',
                              SustainedActivity : 'rules.descriptions.SustainedActivity',
                              UnexpectedEntryExit : 'Pendant la période <b> choisie </b>, un jour <b>Choisi</ b>, la porte d\'entrée est utilisée. Pour une certaine <b>Durée</b>, l\'utilisation de la porte d\'entrée ne provoquera pas de nouvelles alarmes <br/> <br/> Par exemple, un visiteur inattendu entre dans les locaux pendant la <b> nuit </b> le <b> mercredi </b>. Pour les prochaines <b> 10 minutes</b>, l\'utilisation de la porte d\'entrée ne provoquera pas de nouvelles alarmes. <br/> <br/> <b> Important: </b> Une seule règle basée sur ce type de règle est autorisée par bÃ©nÃ©ficiaire.',
                              UnexpectedPresence : 'Pendant la <b>période</b> choisie, au moins une <b>visite</b> est identifiée dans le <b>type de pièce</b> choisi. <br> <br/> Par exemple,le bÃ©nÃ©ficiaire semble prendre un repas dans la <b>salle à manger</b> au milieu de la <b>nuit</b>.',
                              Wandering : 'Alors que le bÃ©nÃ©ficiaire est censé être à la maison, pendant une <b>période</b> choisie, un ou plusieurs <b>jours</b> choisis, le bÃ©nÃ©ficiaire n\'est pas détecté pendant plus longtemps que le <b>temps à l\'extérieur</b> après l\'utilisation de la porte d\'entrée. <br/> <br/> Par exemple, le bÃ©nÃ©ficiaire dort régulièrement de <b>21h30 à 6h00</b>. Le <b> mardi soir</b>, à 2 heures du matin, en utilisant la porte d\'entrée, le bÃ©nÃ©ficiaire quitte la maison, pour ne pas revenir dans <b>l\'heure</b>. <br/> <br/> <b> Note </b>: Le <b>temps passé à l\'extérieur</b> recommandé devrait être au plus une heure. <br/> <br/> <b>Important:</b> Une seule règle basée sur ce type de règle est autorisé par bÃ©nÃ©ficiaire.',
                    },
                    exit : {
                              message : 'Êtes-vous sûr de vouloir quitter?',
                              title : 'Les modifications ne seront pas sauvegardées',
                    },
                    form : {
                              cancel : 'Annuler',
                              commit : 'Appliquer',
                              daysOfWeek : 'Journées',
                              delay : 'Temps dehors',
                              description : 'Descriptions',
                              duration : 'Durée',
                              immediatelyDuration : 'Immediately',
                              groupAllDoors : 'All Doors',
                              groupDoorId : 'Select a Door',
                              edit_title : 'Editer',
                              enabled : 'Activée',
                              groupDeviceId : 'Type de chambre',
                              maxNumOfDetections : 'Détections',
                              maxNumOfVisits : 'Visites',
                              minNumOfDetections : 'Détections',
                              minNumOfVisits : 'Visites',
                              name : 'Prénom',
                              new_title : 'Nouvelle regle',
                              periodSystemId : 'Période',
                              ruleType : 'Type de règle',
                              specificDeviceId : 'Chambre',
                              none: 'None',
                              firstLocationGroupDeviceId: '1 er localisation',
                              secondLocationGroupDeviceId: '2 em localisation',
                              thirdLocationGroupDeviceId: '3 em localisation',
                              homeTime : 'Heure présence à la maison'
                    },
                    ruleTypes : {
                              Absent : 'Absence',
                              AtHomeForTooLong : 'À la maison trop longtemps',
                              DoorOpen : 'Porte ouverte',
                              ExcessiveNumOfDetections : 'Nombre élevé de détections',
                              HighNumOfVisits : 'Nombre important de visites',
                              Inactivity : 'Inactivité',
                              LongStay : 'Long séjour',
                              LowNumOfDetections : 'Faible nombre de détections',
                              LowNumOfVisits : 'Faible nombre de visites',
                              NoActivityDetected : 'Pas d\'activité detectée',
                              OutOfHome : 'Pas à la maison',
                              Presence : 'Debout et actif',
                              ShortStay : 'Court séjour',
                              ShortStayBedroom : 'Court séjour dans la chambre',
                              SustainedActivity : 'Activité soutenue',
                              UnexpectedEntryExit : 'Entrées / Sorties inattendues',
                              UnexpectedPresence : 'Présence inattendue',
                              Wandering : 'Errance',
                    },
          };
          scope.translations['fr_FR'].times= {
                    all_day : 'Toute la journée',
                    days : 'Journées',
                    hours : 'Heures',
                    minutes : 'Minutes',
                    seconds : 'Secondes',
          };
          scope.translations['fr_FR'].weekdays= {
                    friday : 'Vendredi',
                    monday : 'Lundi',
                    saturday : 'samedi',
                    sunday : 'dimanche',
                    thursday : 'Jeudi',
                    tuesday : 'Mardi',
                    wednesday : 'Mercredi',
          };
})(this);

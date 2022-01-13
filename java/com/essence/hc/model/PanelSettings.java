package com.essence.hc.model;

public class PanelSettings {

	public static enum ActiveRolesTypeAllowed{ADMINISTRATOR("Administrator"), CAREGIVER("CareGiver"), RESIDENT("Resident");
		
		private String activeRoleTypeAllowed;
		
		ActiveRolesTypeAllowed(String activeRoleTypeAllowed){
			this.activeRoleTypeAllowed = activeRoleTypeAllowed;
		}
		
		public String getActiveRoleTypeAllowed(){
			return this.activeRoleTypeAllowed;
		}
		
	}

	private String[] activeEmergencyRolesAllowed;

	public String[] getActiveEmergencyRolesAllowed() {
		return activeEmergencyRolesAllowed;
	}

	public void setActiveEmergencyRolesAllowed(String[] activeEmergencyRolesAllowed) {
		this.activeEmergencyRolesAllowed = activeEmergencyRolesAllowed;
	}
}

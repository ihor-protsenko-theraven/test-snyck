package com.essence.hc.model.comparators;

import java.util.Comparator;

import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.SystemUser;

public class PatientByEnableActiveServiceComparator implements
		Comparator<SystemUser> {

	
	@Override
	public int compare(SystemUser p1, SystemUser p2) {
		Panel panel1 = ((Patient) p1).getInstallation().getPanel();
		Panel panel2 = ((Patient) p2).getInstallation().getPanel();
		
		/*if (panel1 == null && panel2 == null) return 0;
		else if (panel1 != null && panel2 == null) return 1;
		else if (panel1 == null && panel2 != null) return -1;*/
		
		if ( panel1.isEnableActiveService() && !panel2.isEnableActiveService()) return 1;
		else if( !panel1.isEnableActiveService() && panel2.isEnableActiveService() ) return -1;
		else  return 0;
	}
}

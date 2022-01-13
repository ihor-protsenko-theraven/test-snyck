package com.essence.hc.model.comparators;

import java.util.Comparator;

import com.essence.hc.model.SystemUser;

public class SystemUserByServiceTypeComparator implements Comparator<SystemUser> {

	@Override
	public int compare(SystemUser u1, SystemUser u2) {
		if (u1 == null || u1.getServiceType() == null) {
			if (u2 == null || u2.getServiceType() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (u2 == null || u2.getServiceType() == null) {
			return 1;
		}
		return u1.getServiceType().compareTo(u2.getServiceType());
	}

}

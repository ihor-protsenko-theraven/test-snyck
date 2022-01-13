package com.essence.hc.model.comparators;

import java.util.Comparator;

import com.essence.hc.model.SystemUser;

public class SystemUserByGeneralIdComparator implements Comparator<SystemUser> {

	@Override
	public int compare(SystemUser u1, SystemUser u2) {
		if (u1 == null || u1.getGeneralId() == null) {
			if (u2 == null || u2.getGeneralId() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (u2 == null || u2.getGeneralId() == null) {
			return 1;
		}
		return u1.getGeneralId().compareTo(u2.getGeneralId());
	}

}

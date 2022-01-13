package com.essence.hc.model.comparators;

import java.util.Comparator;

import com.essence.hc.model.SystemUser;

public class SystemUserByNameComparator implements Comparator<SystemUser> {

	@Override
	public int compare(SystemUser u1, SystemUser u2) {
		if (u1 == null || u1.getFirstName() == null) {
			if (u2 == null || u2.getFirstName() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (u2 == null || u2.getFirstName() == null) {
			return 1;
		}
		return u1.getFirstName().compareTo(u2.getFirstName());
	}

}

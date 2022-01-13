package com.essence.hc.model.comparators;

import java.util.Comparator;

import com.essence.hc.model.Event;

public class EventStartTimeComparator implements Comparator<Event> {
	
	@Override
    public int compare(Event e1, Event e2) {
		if (e1.getStartDateTime() == null) {
			if (e2.getStartDateTime() == null) {
				return 0;
			} else {
				return -1;
			}
		}
		if (e2.getStartDateTime() == null) {
			return 1;
		}
        return e1.getStartDateTime().compareTo(e2.getStartDateTime());
    }
	
}

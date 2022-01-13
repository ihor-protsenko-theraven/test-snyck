package com.essence.hc.eil.parsers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Event;
import com.essence.hc.model.MissingInformation;
import com.essence.hc.model.MissingInformationActivityWrapper;

public class DayStoryPrsr implements IParser<List<Event>> {

	// private Logger logger = LoggerFactory.getLogger(getClass());

	private HashMap<ActivityType, String[]> actvtHash;
	private HashMap<ActivityType, Double> actvtDurHash;
	private MissingInformation[] missingInformation;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

	public DayStoryPrsr() {
	}

	@Override
	public List<Event> parse() {
		List<Event> events = new ArrayList<Event>();
		try {
			Iterator<Entry<ActivityType, String[]>> it = actvtHash.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ActivityType, String[]> e = it.next();
				loopParser((ActivityType) e.getKey(), (String[]) e.getValue(), events);
			}
			
			// Check MissingInformation For this type. Missing information
			// related to an Activity type needs to be returned as an
			// Event/activity so it can be rendered
			if (this.missingInformation != null) {
				for (MissingInformation missingInformation : this.missingInformation) {
					if (missingInformation.getPreviousActivity() != null) {
						
						// Might throw ParseException - MissingInformation data must be rendered as an activity in a activity type line
						events.add(new MissingInformationActivityWrapper(missingInformation));
					}
				}
			}
		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		
		return events;
	}

	/**
	 * This method gets the start and end of each activity detection
	 * 
	 * @param activityType
	 * @param type
	 * @param events
	 *
	 */
	private void loopParser(ActivityType activityType, String[] activityTimes, List<Event> events) {
		String hour = null;
		Date startTime = null;
		Date endTime = null;
		Date originalStartTime = null;
		Date originalEndTime = null;
		Activity lastActivity = null;

		try {
			// Runs through the array to get each activity detection
			for (int i = 0; i < activityTimes.length; i++) {
				if (activityTimes[i] != null) {
					if (!activityTimes[i].equalsIgnoreCase(hour)) {
						hour = activityTimes[i];
						Activity a = new Activity();
						a.setType(activityType);
						originalStartTime = dateFormat.parse(activityTimes[i].substring(0, 5));
						originalEndTime = dateFormat.parse(activityTimes[i].substring(8, 13));
						a.setOriginalStartTime(originalStartTime);
						a.setOriginalEndTime(originalEndTime);
						a.setFirstCell(i);
						a.setLastCell(i + 1);
						startTime = originalStartTime;
						endTime = originalEndTime;
						if (i == 0) {
							if (!activityTimes[0].substring(0, 2).equalsIgnoreCase("00")) {
								// so the activity started before the current
								// date
								startTime = dateFormat.parse("00:00");
							}
							if ((activityTimes[47] != null) && (activityTimes[0].substring(8, 13)
									.equalsIgnoreCase(activityTimes[47].substring(8, 13)))) {
								int j;
								for (j = 1; j < activityTimes.length && activityTimes[j] != null; j++)
									;
								if (j == activityTimes.length) {
									endTime = dateFormat.parse("23:59");
								}
							}
						} else {
							if ((activityTimes[47] != null) && (activityTimes[i].equalsIgnoreCase(activityTimes[47]))) {
								endTime = dateFormat.parse("23:59");
							}
						}
						a.setStartDateTime(startTime);
						a.setEndDateTime(endTime);

						lastActivity = a;
						events.add(a);
					} else {
						lastActivity.setLastCell(i + 1);
					}
				} else {
					hour = null;
					lastActivity = null;
				}
			}

		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}

	}

	public HashMap<ActivityType, String[]> getActvtHash() {
		return actvtHash;
	}

	public void setActvtHash(HashMap<ActivityType, String[]> actvtHash) {
		this.actvtHash = actvtHash;
	}

	public HashMap<ActivityType, Double> getActvtDurHash() {
		return actvtDurHash;
	}

	public void setActvtDurHash(HashMap<ActivityType, Double> actvtDurHash) {
		this.actvtDurHash = actvtDurHash;
	}

	public MissingInformation[] getMissingInformation() {
		return missingInformation;
	}

	public void setMissingInformation(MissingInformation[] missingInformation) {
		this.missingInformation = missingInformation;
	}
}

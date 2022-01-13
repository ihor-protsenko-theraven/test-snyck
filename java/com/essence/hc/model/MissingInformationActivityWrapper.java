package com.essence.hc.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import com.essence.hc.util.Util;

/**
 * MissingInformation is retrieved through a GetDayStory request and is
 * associated to an Activity Type meaning that it can be represented in that
 * activity type line.
 * 
 * Only Activity instances can be rendered in a PRO day story report, and are
 * the ones returned through getEvents() method in DayStoryPrsr.
 * 
 * This type is the activity wrapper for a missing information instance intended
 * to be rendered in a activity line and should contained the required info to a
 * proper missing information rendering.
 *
 */
public class MissingInformationActivityWrapper extends Activity {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

	private final MissingInformation missingInformation;

	public MissingInformationActivityWrapper(final MissingInformation missingInformation) {

		this.missingInformation = missingInformation;

		super.setType(missingInformation.getPreviousActivity().getActivityType());

	}

	/**
	 * The data contained in MissingInformation is not enough to check whether the
	 * original dates ocurre in the same day or not.
	 * 
	 * DayStory info is retrieved for a given date, so it is required to check if
	 * the original start and end dates are in the current day story date or not
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss string
	 */
	public void setActivityDatesBasedOnAGivenDate(String date) {

		try {
			String et;

			// Calc endtime
			if (StringUtils.hasText(missingInformation.getEndTime())) {

				// There is an original endtime
				super.setOriginalEndTime(dateFormat.parse(missingInformation.getEndTime().substring(11, 16)));

				if (this.isSameDay(missingInformation.getEndTime(), date)) {
					et = missingInformation.getEndTime().substring(11, 16);
				} else {
					// Not sameday -> Tomorrow
					et = "23:59";
				}
			} else {
				et = "23:59";
			}
			super.setEndDateTime(dateFormat.parse(et));

			// Calc startTime
			String st;

			if (StringUtils.hasText(missingInformation.getStartTime())) {

				// There is an original startTime
				super.setOriginalStartTime(dateFormat.parse(missingInformation.getStartTime().substring(11, 16)));

				if (this.isSameDay(missingInformation.getStartTime(), date)) {
					st = missingInformation.getStartTime().substring(11, 16);
				} else {
					// Not same day -> yesterday
					st = "00:00";
				}
			} else {
				st = "00:00";
			}
			super.setStartDateTime(dateFormat.parse(st));

			super.setLastCell(calculateCellForTime(et, true) + 1);
			super.setFirstCell(calculateCellForTime(st));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if two dates following this pattern happen in the same day
	 * 
	 * @param aDateStr
	 *            in 2019-01-28T22:12:38 format
	 * @param baseDateStr
	 *            in 2019-01-28 22:12:38 format
	 * @return
	 */
	public boolean isSameDay(String aDateStr, String baseDateStr) {

		DateTimeFormatter essenceFormatter = DateTimeFormat.forPattern(Util.DATEFORMAT_ESSENCE);
		DateTimeFormatter inputFormatter = DateTimeFormat.forPattern(Util.DATETIMEFORMAT_INPUT);

		DateTime aDate = inputFormatter.parseDateTime(aDateStr);
		DateTime baseDate = essenceFormatter.parseDateTime(baseDateStr);
		return aDate.toDateMidnight().isEqual(baseDate.toDateMidnight());
	}

	/**
	 * Convert to seconds the pattern HH:mm:ss
	 * 
	 * @param hhmmss
	 * @return
	 */
	private long toSeconds(String hhmm) {
		try {
			// reference is used to compensate for different timezones
			Date reference = this.dateFormat.parse("00:00");
			Date date = this.dateFormat.parse(hhmm);
			return (date.getTime() - reference.getTime()) / 1000L;
		} catch (java.text.ParseException e) {
			throw new RuntimeException(String.format("%s can not be parsed. Expected pattern: HH:mm", hhmm));
		}
	}

	private int calculateCellForTime(String hhmm, boolean isEnd) {
		long s = toSeconds(hhmm);
		int cell = (int) (s / 1800);
		if (isEnd && (s % 1800 == 0)) {
			cell = cell -1;
		}
		
		return cell; // CELL
	}

	private int calculateCellForTime(String hhmm) {
		return this.calculateCellForTime(hhmm, false);
	}

	public MissingInformation getMissingInformation() {
		return missingInformation;
	}

	public String getTitleCode() {
		switch (this.missingInformation.getReason()) {
		case lostTransmission:
			return "report.missing-info.reason.lost-transmision";
		case noCommunication:
			return "report.missing-info.reason.no-communication";
		case unknown:
			return "";
		case supervisionLoss:
			return "report.missing-info.reason.supervision-lost";
		default:
			throw new RuntimeException("The enum provided is not expected. Update your code!");

		}
	}

}

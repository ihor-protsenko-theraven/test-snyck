package com.essence.hc.model;

public class Report {

	public static enum ReportType {
		DAY_STORY_EXPRESS("Day Story Express"), DAY_STORY_ANALYTICS("Day Story Analytics"), WEEKLY_ACTIVITY("Weekly Activity"), MONTHLY_REPORT("Monthly Report"), MOVEMENT_LEVEL("Movement Level"),
		WEEKLY_REPORT("Weekly Report"), STEP_COUNT("Step Count") ;

		private String reportType;

		ReportType(String reportType) {
			this.reportType = reportType;
		}

		public String getReportType() {
			return reportType;
		}
	}
	
}

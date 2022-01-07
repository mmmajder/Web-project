package repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.json.simple.JSONObject;

public class DateManager {
	private static Month getMonth(String month) {
		switch (month) {
		case "JANUARY":
			return Month.JANUARY;
		case "FEBRUARY":
			return Month.FEBRUARY;
		case "MARCH":
			return Month.MARCH;
		case "APRIL":
			return Month.APRIL;
		case "MAY":
			return Month.MAY;
		case "JUNE":
			return Month.JUNE;
		case "JULY":
			return Month.JULY;
		case "AUGUST":
			return Month.AUGUST;
		case "SEPTEMBER":
			return Month.SEPTEMBER;
		case "OCTOBER":
			return Month.OCTOBER;
		case "NOVEMBER":
			return Month.NOVEMBER;
		case "DECEMBER":
			return Month.DECEMBER;
		default:
			return null;
		}
	}

	public static LocalDate getDate(JSONObject date) {
		int year = (int) (long) (date.get("year"));
        int day = (int) (long) (date.get("dayOfMonth"));
        return LocalDate.of(year, DateManager.getMonth((String)date.get("month")), day);
	}

	public static LocalDateTime getDateTime(JSONObject dateTime) {
		int year = (int) (long) (dateTime.get("year"));
        int day = (int) (long) (dateTime.get("dayOfMonth"));
        int hour = (int) (long) (dateTime.get("hour"));
        int minute = (int) (long) (dateTime.get("minute"));
		return LocalDateTime.of(year, DateManager.getMonth((String)dateTime.get("month")), day, hour, minute);
	}

}

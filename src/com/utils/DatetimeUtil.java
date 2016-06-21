package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatetimeUtil {

	private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
	private static final Calendar calendar = Calendar.getInstance();

	private static final String defaultFormat = "yyyy-MM-dd";
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	private static final String defaultTimeFormat = "HH:mm";

	private static final String[] weekArr = new String[] { "星期日", "星期一", "星期二",
			"星期三", "星期四", "星期五", "星期六" };

	public static String parseDateString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultFormat);
		try {
			return dateFormat.format(dateFormat.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateString;
	}

	public static Date parseDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultFormat);
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static String parseDateStr(String dateString,
			String fromDateFormatStr, String toDateFormatStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromDateFormatStr);
		SimpleDateFormat dateFormat1 = new SimpleDateFormat(toDateFormatStr);
		try {
			Date date = dateFormat.parse(dateString);
			if (59 == date.getSeconds())
				date.setMinutes(date.getMinutes() + 1);
			return dateFormat1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateString;
	}

	public static Date parseDate(String dateString, String dateFormatStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static String parseDate(long milliseconds, String dateFormatStr) {
		Date date = new Date(milliseconds);
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		return dateFormat.format(date);
	}

	public static String format(int year, int month, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultFormat);
		calendar.set(year, month, day);
		return dateFormat.format(calendar.getTimeInMillis());
	}

	public static int getAge(String birthday) {
		Date birthDate = parseDate(birthday);
		return (new Date().getYear() - birthDate.getYear());
	}

	public static String now() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultDateFormat);

		return dateFormat.format(new Date());
	}

	public static String nextHour() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultDateFormat);

		return dateFormat.format(System.currentTimeMillis() + 1000*60*60);
	}

	public static String displayDate(String dateString) {
		if (StringUtil.isEmpty(dateString))
			return "";
		Date date = parseDate(dateString, defaultFormat);
		calendar.set(date.getYear(), date.getMonth(), date.getDay());
		if (isToday(date)) {
			return dateString.split(" ")[1];
		}
		// if (isYestoday(calendar)) {
		// return "昨天";
		// }
		return dateString.split(" ")[0];
	}

	private static int compareTaday(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultFormat);
		Date d = parseDate(dateFormat.format(new Date()));
		return date.compareTo(d);
	}

	private static boolean isToday(Date date) {
		return compareTaday(date) == 0 ? true : false;
	}

	private static int compareTaday(Calendar calendar) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.set(date.getYear(), date.getMonth(), date.getDay());
		c.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.compareTo(c);
	}

	private static boolean isToday(Calendar calendar) {
		return compareTaday(calendar) == 0 ? true : false;
	}

	private static boolean isYestoday(Calendar calendar) {
		return compareTaday(calendar) == 1 ? true : false;
	}

	private static boolean isThisWeek() {
		return compareTaday(calendar) == 1 ? true : false;
	}

	public static boolean isEarly(String begin, String end) {
		Date beginDate = parseDate(begin, defaultTimeFormat);
		Date endDate = parseDate(end, defaultTimeFormat);
		return beginDate.before(endDate);
	}

	public static boolean isEarly(String begin, String end, String format) {
		Date beginDate = parseDate(begin, format);
		Date endDate = parseDate(end, format);
		return beginDate.before(endDate);
	}

	public static String parseStringTime(int minutes) {
		int hour = minutes / 60;
		int minute = minutes % 60;
		SimpleDateFormat dateFormat = new SimpleDateFormat(defaultTimeFormat);

		return dateFormat.format(parseDate(hour + ":" + minute,
				defaultTimeFormat));
	}

	public static int parseIntTime(String time) {
		if (StringUtil.isEmpty(time))
			return 0;
		String[] times = time.split(":");
		return Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
	}

	public static String getDateString(Date date) {
		if (date == null)
			date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return format(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static int getCurrenIntTime() {
		Calendar calendar = Calendar.getInstance();
		return parseIntTime(calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE));
	}

	/**
	 * from 2014-07-18 12:10:10 to 2014-07-18 周* 12:10
	 * 
	 * @param date
	 * @return
	 */
	public static String parseDateWithWeekDay(String date) {
		return parseDateStr(date, "yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd EEEE HH:mm").replaceAll("星期", "周");
	}

	/**
	 * from 2014-07-18 周* 12:10 to 2014-07-18 12:10:10
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateWithWeekDay(String date) {
		return parseDateStr(date.replaceAll("周", "星期"),
				"yyyy-MM-dd EEEE HH:mm", "yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentDateWithWeekDay() {
		return parseDateWithWeekDay(now());
	}
	
	public static String getNextHourWithWeekDay() {
		return parseDateWithWeekDay(nextHour());
	}
	
	public static String format(long d) {
    	Date date = new Date(d);
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
    
    /*
     * 2015-06-01 11:26
     */
    public static String getTimeLocal(long time){
    	Date d = new Date(time);
		SimpleDateFormat sdt=new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		String s = sdt.format(d);
		return s;
    }
    
    /*
     * 2015-06-01 11:26
     */
    public static String getYMDTimeLocal1(long time){
    	Date d = new Date(time);
		SimpleDateFormat sdt=new SimpleDateFormat("yyyy-MM-dd"); 
		String s = sdt.format(d);
		return s;
    }
    
    /*
     * 年月日
     */
    public static String getYMDTimeLocal2(long time){
    	Date d = new Date(time);
		SimpleDateFormat sdt=new SimpleDateFormat("yyyy年MM月dd日");
		String s = sdt.format(d);
		return s;
    }
    
    public static String getMinuteTime(int seconds) {
		int m = seconds / 60;
		int seco = seconds % 60;
		String time = m +"";
		if (m < 9) {
			time = "0" + m;
		}
		if (seco < 9) {
			time = time + ":" + "0" + seco;
		}else{
			time = time + ":" + seco;
		}
		return time;
	}

    public static String getYearMonth(int month) {
		int y = month / 12;
		int m = month % 12;
		String time =m+"个月";
		if(y>0){
			time= y +"年"+time;
			if(m==0)
				time= y +"年";
		}
		return time;
	}
}

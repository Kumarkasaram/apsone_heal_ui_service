package com.heal.dashboard.service.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {
	  public static String getTimeInGMT(long time) {

	        DateFormat simpleDateFormat = null;
	        if (simpleDateFormat == null) {
	            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	        }
	        return simpleDateFormat.format(time);
	    }
}

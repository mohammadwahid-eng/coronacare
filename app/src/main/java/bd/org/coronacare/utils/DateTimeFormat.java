package bd.org.coronacare.utils;

import android.icu.util.Calendar;
import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class DateTimeFormat extends DateUtils {

    public static String getTimeAgo(long timestamp) {
        if (timestamp < 1000000000000L) {
            timestamp *= 1000;
        }

        long now = System.currentTimeMillis();
        if (timestamp > now || timestamp <= 0) {
            return null;
        }


        final long diff = now - timestamp;
        if (diff < MINUTE_IN_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_IN_MILLIS) {
            return "A minute ago";
        } else if (diff < 50 * MINUTE_IN_MILLIS) {
            return diff / MINUTE_IN_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_IN_MILLIS) {
            return "An hour ago";
        } else if (diff < 24 * HOUR_IN_MILLIS) {
            return "Active " + diff / HOUR_IN_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_IN_MILLIS) {
            return "Last active yesterday";
        } else {
            return "Last active " + diff / DAY_IN_MILLIS + " days ago";
        }
    }

    public static String getLastActive(long timestamp) {
        if (timestamp < 1000000000000L) {
            timestamp *= 1000;
        }

        long now = System.currentTimeMillis();
        if (timestamp > now || timestamp <= 0) {
            return null;
        }

        final long diff = now - timestamp;
        if (diff < 24 * HOUR_IN_MILLIS) {
            return "Today";
        } else if (diff < 48 * HOUR_IN_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_IN_MILLIS + " days ago";
        }
    }

    public static CharSequence getChatTime(long timestamp) {
        if (isToday(timestamp)) {
            return formatSameDayTime(timestamp, System.currentTimeMillis(), DateFormat.SHORT, DateFormat.SHORT);
        } else {
            return new SimpleDateFormat("MMM dd").format(new Date(timestamp));
        }
    }

    public static Long milliseconds(String Date) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date mDate = sdf.parse(Date);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  timeInMilliseconds;
    }

    public static String getReadableTime(int hour, int minute) {
        String format = "AM";
        if (hour == 0) {
            hour += 12;
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        }
        return new StringBuilder().append(hour).append(":").append((minute==0) ? "00" : minute).append(" ").append(format).toString();
    }

    public static String feedbackTime(long timestamp) {
        return new SimpleDateFormat("MMM dd, yyyy 'at' h:mm a").format(timestamp);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calculateAge(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyyy");
        Date date = null;
        try {
            date = sdf.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        LocalDate dateBirth = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        LocalDate dateNow = LocalDate.now();
        return String.valueOf(Period.between(dateBirth, dateNow).getYears());
    }
}

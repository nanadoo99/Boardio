package com.nki.t1.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtils {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 타임스탬프 문자열을 포맷된 날짜 문자열로 변환합니다.
     *
     * @param timestampString 변환할 타임스탬프 문자열
     * @return 포맷된 날짜 문자열
     */
    public static String timestampStringToFormattedString(String timestampString) {
        if (timestampString == null || timestampString.trim().isEmpty()) {
            return "";
        }
        long timestamp = Long.parseLong(timestampString);
        // 타임스탬프를 Instant로 변환
        Instant instant = Instant.ofEpochMilli(timestamp);

        // Instant를 LocalDate로 변환 (기본 시간대 사용)
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(dateFormatter);
    }

    /**
     * java.sql.Date를 포맷된 문자열로 변환합니다.
     *
     * @param date 변환할 java.sql.Date
     * @return 포맷된 문자열
     */
    public static String dateTimeToFormattedString(Date date) {
        if (date == null) {
            return "";
        }
        return sdfTime.format(date);
    }

    /**
     * java.sql.Date를 포맷된 문자열로 변환합니다.
     *
     * @param date 변환할 java.sql.Date
     * @return 포맷된 문자열
     */
    public static String dateToFormattedString(Date date) {
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }


}

package com.nki.t1.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class SizeParser {

    private static final Map<String, Long> SIZE_UNIT = new LinkedHashMap<>(); // HashMap >> 무작위 검사, LinkedHashMap로 순서보장

    // 정적 초기화 블록. 클래스가 로드될 때 최초 1회 시행됨을 보장.
    static {
        SIZE_UNIT.put("GB", 1024*1024*1024L);
        SIZE_UNIT.put("MB", 1024*1024L);
        SIZE_UNIT.put("KB", 1024L);
        SIZE_UNIT.put("B", 1L);
    }

    // 크기 변환
    public static String longToString(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String unit = "KMGTPE".charAt(exp - 1) + "B";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), unit);
    }

    public static Long stringToLong(String size) {
        size = size.trim().toUpperCase();

        System.out.println("size = " + size);
        for( Map.Entry<String, Long>  entry : SIZE_UNIT.entrySet()) {
            if(size.endsWith(entry.getKey())) {
                String numericPart = size.replace(entry.getKey(), "").trim(); // 🔍 숫자 부분 확인
                System.out.println("Parsed numeric part: '" + numericPart + "'"); // ✅ 추가 디버깅
                return (long) Integer.parseInt(numericPart) * entry.getValue();
            }
        }

        return Long.parseLong(size);
    }
}

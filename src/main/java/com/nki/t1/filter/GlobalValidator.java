package com.nki.t1.filter;

import com.nki.t1.dto.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class GlobalValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchCondition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) { // Errors : BindingResult의 부모
        log.info("===== GlobalValidator called");
        SearchCondition searchCondition = (SearchCondition) o;

        String keyword = searchCondition.getKeyword();

        // 검색어 길이 제한
        int keywordMax = 2048;
        if(keyword.length() > keywordMax) {
            errors.rejectValue("keyword", "maxLength", new Object[] {keywordMax}, null);
        }

        // 날짜 검증
        String startDt = searchCondition.getStartDt();
        String endDt = searchCondition.getEndDt();

        // 날짜 형식 확인
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

        try {
            LocalDate startDate = LocalDate.parse(startDt, dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(endDt, dateTimeFormatter);

            // 시작일이 종료일 이후면 오류
            if(startDate.isAfter(endDate)) {
                errors.rejectValue("startDt", "date.startDate", new Object[] {startDt}, null);
            }

        } catch (DateTimeParseException e) {
            // 형식이 틀리면 오류
            if(!startDt.isEmpty()) {
                errors.rejectValue("startDt", "date.format", new Object[] {"시작일", dateFormat}, null);
            }

            if(!endDt.isEmpty()) {
                errors.rejectValue("endDt", "date.format", new Object[] {"종료일", dateFormat}, null);
            }

        }
    }
}

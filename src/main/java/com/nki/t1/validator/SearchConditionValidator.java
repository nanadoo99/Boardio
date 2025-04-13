package com.nki.t1.validator;

import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class SearchConditionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchCondition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) { // Errors : BindingResult의 부모
        log.info("===== SearchConditionValidator called");
        SearchCondition searchCondition = (SearchCondition) o;

        String keyword = searchCondition.getKeyword();

        // 검색어 길이 제한
        int keywordMax = 2048;
        if(keyword.length() > keywordMax) {
            errors.rejectValue("keyword", "maxLength", new Object[] {keywordMax}, null);
        }
    }
}

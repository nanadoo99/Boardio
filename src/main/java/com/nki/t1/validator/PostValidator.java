/* 삭제 가능
package com.nki.t1.validator;

import com.nki.t1.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
public class PostValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) { // Errors : BindingResult의 부모
        log.info("===== PostValidator called");

        PostDto postDto = (PostDto) o;

        // 제목 필수
        // 내용 필수
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title",  "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content",  "required");

        String title = postDto.getTitle();
        String content = postDto.getContent();
        postDto.extractContentTxtFromContent();
        String contentTxt = postDto.getContentTxt();

        // 제목 길이 제한
        int titleMax = 50;
        if(title.length() > titleMax) {
            errors.rejectValue("title", "maxLength", new Object[] {titleMax}, null);
        }

        // 내용 길이 제한 - 태그 미포함
        int contentMax = 5000;
        if (contentTxt.length() > 5000) {
            errors.rejectValue("contentTxt", "maxLength",  new Object[] {contentMax}, null);
        }

        // 내용 길이 제한 - 태그 포함
        if (postDto.getContent().length() > 10000) {
            errors.rejectValue("content", "limitedMax");
        }
    }
}
*/

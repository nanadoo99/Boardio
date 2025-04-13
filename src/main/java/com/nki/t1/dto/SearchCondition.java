package com.nki.t1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class SearchCondition implements ObjDto {
    private Integer page = 1;
    private int pageSize = 10;

    private String option1 = "";
    private String option2 = "";
    private String option3 = "";
    private Integer code1 = 0;
    private Integer code2 = 0;
    @Size(max = 50, message = "검색어는 최대 50자 입력 가능합니다.")
    private String keyword = "";
    private List<String> keywordList = new ArrayList<String>();
    private int uno;
    private int idx1;

    private String startDt = "";
    private String endDt = "";

    public SearchCondition() {}

    public SearchCondition(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (page-1)*pageSize;
    }

    // 검색 키워드 리스트 추출 - 쉼표 및 공백 단위
    public List<String> getKeywordList() {
        String trimedKeyword = keyword.trim().replaceAll("^[,\\s]+|[,\\s]+$", "");
        return Arrays.stream(trimedKeyword.split("[,\\s]+")).filter(keyword -> !keyword.isEmpty()).map(String::trim).collect(Collectors.toList());
    }

    public String getQueryString() { // 뷰에서 목록으로 돌아가기 중 검색어 저장에 쓰임
        return getQueryString(page);
    }
    public void setPage(Integer page) {
        this.page = (page != null && page > 0) ? page : 1;
    }

    public void setPage(String page) {
        if (page == null || page.trim().isEmpty()) {
            setPage(1); // 기본값 설정
        } else {
            try {
                setPage(Integer.parseInt(page)); // String을 int로 변환 후 설정
            } catch (NumberFormatException e) {
                setPage(1); // 변환 실패 시 기본값 설정
            }
        }
    }

    public String getQueryString(Integer page) {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .queryParam("option1", option1)
                .queryParam("option2", option2)
                .queryParam("option3", option3)
                .queryParam("code1", code1)
                .queryParam("code2", code2)
                .queryParam("keyword", keyword)
                .queryParam("uno", uno)
                .queryParam("idx1", idx1)
                .queryParam("startDt", startDt)
                .queryParam("endDt", endDt)
                .build().toString();
    }

    public static class Builder{
        private int pageSize;
        private int uno;
        private String option3;

        public Builder pageSize(int pageSize){
            this.pageSize = pageSize;
            return this;
        }
        public Builder uno(int uno){
            this.uno = uno;
            return this;
        }
        public Builder option3(String option3){
            this.option3 = option3;
            return this;
        }

        public SearchCondition build() {
            return new SearchCondition(this);
        }
    }

    public SearchCondition(Builder builder) {
        this.pageSize = builder.pageSize;
        this.uno = builder.uno;
        this.option3 = builder.option3;
    }
}

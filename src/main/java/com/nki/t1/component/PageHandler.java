package com.nki.t1.component;

import com.nki.t1.dto.SearchCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class PageHandler {
    private SearchCondition sc;

    private int totalCnt = 0;
    private int totalPage;
    private int navSize = 10;
    private int beginPage;
    private int endPage;
    private boolean showPrev;
    private boolean showNext;

    public PageHandler() {}

    public PageHandler(int totalCnt, SearchCondition sc) {
        this.sc = sc;
        this.totalCnt = totalCnt;

        paging();
    }

    private void paging() {

        totalPage = (int) Math.ceil(totalCnt*1.0 / sc.getPageSize());
        beginPage = (sc.getPage()-1) / navSize * navSize + 1;
        endPage = Math.min(beginPage + navSize - 1, totalPage);
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;

    }


}

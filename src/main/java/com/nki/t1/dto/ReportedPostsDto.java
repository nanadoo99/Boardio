package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReportedPostsDto extends BlockReasonsDto {
    private Integer rpno;
    private Integer pno;
    private Integer idx;
    private Integer reportUno;
    private String reportUserId;
    private Date reportedAt;

    private Integer deleteUno;
    private Date deletedAt;

    public ReportedPostsDto() {}

    public ReportedPostsDto(int idx, int reportUno, int deleteUno) {
        this.idx = idx;
        this.reportUno = reportUno;
        this.deleteUno = deleteUno;
    }
}

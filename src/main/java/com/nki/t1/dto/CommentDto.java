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
public class CommentDto {
    private Integer cno;
    private Integer pno;
    private Integer pcno;
    private Integer uno;
    private String userId;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
    private boolean del;

    public CommentDto() {}

    public CommentDto(Integer pno, Integer uno, String comment) {
        this.pno = pno;
        this.uno = uno;
        this.comment = comment;
    }
}

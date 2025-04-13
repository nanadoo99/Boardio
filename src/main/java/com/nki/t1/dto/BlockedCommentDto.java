package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BlockedCommentDto extends BlockReasonsDto {
    private Integer bcno;
    private Integer cno;
    private Integer uno;
    private String memo;
}

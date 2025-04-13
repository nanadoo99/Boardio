package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BlockedPostDto extends BlockReasonsDto {
    private Integer bpno;
    private Integer pno;
    private Integer uno;
    private String memo;
}

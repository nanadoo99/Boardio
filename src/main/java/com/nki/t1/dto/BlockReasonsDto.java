package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BlockReasonsDto implements ObjDto {
    private Integer brno;
    private List<Integer> brnoList;
    private String brText;
    private List<String> brTextList;
    private Integer brnoCnt;
}

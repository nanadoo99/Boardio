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
public class PostDto {
    private Integer pno;
    private Integer uno;
    private String userId;
    private String title;
    private String content;
    private Integer views;
    private Date createdAt;
    private Date updatedAt;
    private boolean del;

    public PostDto() {
        this(1, "", "");
    }

    public PostDto(Integer uno, String title, String content) {
        this.uno = uno;
        this.title = title;
        this.content = content;
    }
}

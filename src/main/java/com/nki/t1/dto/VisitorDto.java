package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class VisitorDto{

    private String lastestUpdate;
    private long count;
    private Integer id;
    private Integer uno;
    private LocalDate date;

    public VisitorDto() {}

    private VisitorDto(Builder builder) {
        this.uno = builder.uno;
        this.date = builder.date;
    }

    public static class Builder{
        private Integer uno;
        private LocalDate date;

        public Builder uno(Integer uno){
            this.uno = uno;
            return this;
        }

        public Builder date(LocalDate date){
            this.date = date;
            return this;
        }

        public VisitorDto build(){
            return new VisitorDto(this);
        }
    }

}

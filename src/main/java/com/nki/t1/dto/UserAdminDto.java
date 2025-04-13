package com.nki.t1.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserAdminDto extends UserDto implements ObjDto {
    private Integer countPost;
    private Integer countComment;
    private Integer countVisit;
    private Date lastVisitDate;

    public String getFormattedLastVisitDate() {
        return getFormattedDate(this.lastVisitDate);
    }

    public String getFormattedLastVisitDateTime() {
        return getFormattedDateTime(this.lastVisitDate);
    }
}

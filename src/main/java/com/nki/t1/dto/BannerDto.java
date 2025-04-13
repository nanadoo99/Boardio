package com.nki.t1.dto;

import com.nki.t1.domain.UserIdentifiable;
import com.nki.t1.utils.DateTimeFormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BannerDto implements UserIdentifiable, ObjDto {
    private Integer id;
    private Integer uno;
    private String userId;
    private Integer displayOrder;
    private Date postedAt;  // 공개 게시일
    private Date unpostedAt;
    private Date createdAt; // 작성일
    private Date updatedAt;
    private MultipartFile image;
    private FileDto fileDto;
    private String memo;

    private String uploadPath;

    private AnnounceDto announceDto = new AnnounceDto();

    public void setAno(Integer ano) {
        if (this.announceDto != null) {
            this.announceDto.setAno(ano);
        } else {
            throw new IllegalStateException("announceDto is not initialized.");
        }
    }

    public Integer getAno() {
        if (this.announceDto != null) {
            return this.announceDto.getAno();
        }
        throw new IllegalStateException("announceDto is not initialized.");
    }

    public String getAnnounceTitle() {
        if (this.announceDto != null) {
            return this.announceDto.getTitle();
        }
        throw new IllegalStateException("announceDto is not initialized.");
    }

    // Date 포맷팅 - 날짜
    private String getFormattedDate(java.util.Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateToFormattedString(date);
    }

    public String getFormattedCreatedAt() {
        return getFormattedDate(this.createdAt);
    }

    // Date 포맷팅 - 날짜 및 시간
    private String getFormattedDateTime(java.util.Date date) {
        if (date == null) {return "";}
        return DateTimeFormatUtils.dateTimeToFormattedString(date);
    }

    public String getFormattedCreatedAtTime() {
        return getFormattedDateTime(this.createdAt);
    }

    public String getFormattedAnnouncePostedAtTime(){
        return this.announceDto.getFormattedPostedAtTime();
    }
}

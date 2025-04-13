package com.nki.t1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@ToString
public class FileDto {
    private int fno;
    private int postIdx;
    private MultipartFile file;
    private String filePath;
    private String webPath;
    private String uploadPath;
    private String fileOrgNm;
    private String fileUidNm;
    private long size;
    private Date createdAt;
    private Date deletedAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FileDto fileDto = (FileDto) obj;
        return fileUidNm != null && fileUidNm.equals(fileDto.getFileUidNm());
    }

    @Override
    public int hashCode() {
        return fileUidNm != null ? fileUidNm.hashCode() : 0;
    }

    public String getUploadFullPath() {
        return uploadPath + fileUidNm;
    }
}
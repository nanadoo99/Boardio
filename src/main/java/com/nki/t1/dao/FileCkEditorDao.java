package com.nki.t1.dao;

import com.nki.t1.dto.FileDto;

import java.util.Set;

public interface FileCkEditorDao {
    int insertFile(FileDto fileDto);
    Set<FileDto> selectFileDto(int idx);
    Set<String> selectAllUids(long expirationPeriod);
    Set<String> selectUid(int idx);

    int deleteFileByPostIdx(int idx);
    int deletePartiallyByFno(int idx);
    void deleteFileByFileUuid(String fileUuid);
}

package com.nki.t1.dao;

import com.nki.t1.dto.FileDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAnnounceDao {
    int insertFile(FileDto fileDto);
    FileDto selectFileByFnoUser(int fano);
    FileDto selectFileByFnoAdmin(int fano);
    List<FileDto> selectFileListByFnoListAdmin(List<Integer> fnoList);
    List<FileDto> selectFileListByAnoAdmin(int fano);
    int deleteFileListByAnoAdmin(int ano);
    int deleteFileListByFanoAdmin(int fano);
    int deleteFileListByFnoListAdmin(List<Integer> fnoList);
}

package com.nki.t1.service;

import com.nki.t1.dao.FileAnnounceDao;
import com.nki.t1.domain.ErrorType;
import com.nki.t1.dto.FileDto;
import com.nki.t1.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileAnnounceServiceImpl implements FileAnnounceService{
    private FileAnnounceDao fileAnnounceDao;

    public FileAnnounceServiceImpl(FileAnnounceDao fileAnnounceDao) {
        this.fileAnnounceDao = fileAnnounceDao;
    }

    @Override
    public FileDto selectFileByFnoUser(int fano) throws InvalidFileException {
        FileDto fileDto = fileAnnounceDao.selectFileByFnoUser(fano);
        log.debug("----- FileAnnounceService fileDto={}", fileDto);
        if(fileDto == null) {
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND);
        }
        return fileDto;
    }

    @Override
    public FileDto selectFileByFnoAdmin(int fano) throws InvalidFileException {
        FileDto fileDto = fileAnnounceDao.selectFileByFnoAdmin(fano);
        log.debug("----- FileAnnounceService fileDto={}", fileDto);
        if(fileDto == null) {
            throw new InvalidFileException(ErrorType.FILE_NOT_FOUND);
        }
        return fileDto;
    }

    @Override
    public int deleteFileListByFanoAdmin(int fano) {
        return fileAnnounceDao.deleteFileListByFanoAdmin(fano);
    }
}

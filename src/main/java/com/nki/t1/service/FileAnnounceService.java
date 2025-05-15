package com.nki.t1.service;

import com.nki.t1.dto.FileDto;

public interface FileAnnounceService {
    FileDto selectFileByFnoUser(int fano);
    FileDto selectFileByFnoAdmin(int fano);
    int deleteFileListByFanoAdmin(int fano);
}

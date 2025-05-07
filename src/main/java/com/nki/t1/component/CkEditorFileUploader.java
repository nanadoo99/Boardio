package com.nki.t1.component;


import com.nki.t1.dto.FileDto;

import java.io.IOException;
import java.util.Set;

public interface CkEditorFileUploader {

    void deleteFileSetFromServer(Set<FileDto> fileSet) throws IOException;
    void deleteExpiredFileSetFromServer(Set<String> dbFileNameSet, long expirationPeriodSec);

}

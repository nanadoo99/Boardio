package com.nki.t1.component;


import com.nki.t1.dto.FileDto;
import com.nki.t1.utils.FileUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public interface CkEditorFileUploader extends FileUploader {

    void deleteFileSetFromServer(Set<FileDto> fileSet) throws IOException;
    void deleteExpiredFileSetFromServer(Set<String> dbFileNameSet, long expirationPeriodSec);

}

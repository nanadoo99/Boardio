package com.nki.t1.controller;

import com.nki.t1.service.CkEditorFileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

// CkEditor 가비지 이미지 처리
@RestController
@RequestMapping("/admin/test")
public class CleanTController {
    private final CkEditorFileService ckEditorFileServicePostImplCleanTest;
    private final CkEditorFileService ckEditorFileServiceAnnounceImplCleanTest;

    public CleanTController(@Qualifier("ckEditorFileServicePostImplCleanTest")
                            CkEditorFileService ckEditorFileServicePostImplCleanTest,
                            @Qualifier("ckEditorFileServiceAnnounceImplCleanTest")
                            CkEditorFileService ckEditorFileServiceAnnounceImplCleanTest) {
        this.ckEditorFileServicePostImplCleanTest = ckEditorFileServicePostImplCleanTest;
        this.ckEditorFileServiceAnnounceImplCleanTest = ckEditorFileServiceAnnounceImplCleanTest;
    }

    @GetMapping("/ckEditor-garbage")
    public ResponseEntity<String> ckEditorGarbage() throws IOException {
        ckEditorFileServicePostImplCleanTest.cleanUnusedImages();
        ckEditorFileServiceAnnounceImplCleanTest.cleanUnusedImages();

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

}

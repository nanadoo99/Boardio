/*
package com.nki.t1.utils;

import com.nki.t1.domain.CkEditorPost;
import com.nki.t1.dao.FileCkEditorDao;
import com.nki.t1.dto.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

// 절대경로 사용예제.
// FileUtilsCkEditor
// UserPostController2
// PostServiceImpl2

@Slf4j
public abstract class FileUtilsCkEditor {

    protected final FileCkEditorDao fileCkEditorDao;
    protected final String uploadDir22;
    protected final String webDir22;
    protected final long expirationPeriodSec;

    public FileUtilsCkEditor(FileCkEditorDao fileCkEditorDao, String uploadDir22, String webDir22, long expirationPeriodSec) {
        this.fileCkEditorDao = fileCkEditorDao;
        this.uploadDir22 = uploadDir22;
        this.webDir22 = webDir22;
        this.expirationPeriodSec = expirationPeriodSec;
    }

    // CkEditor의 에러 처리에 맞게 기본 IOException 사용
    public void fileUpload(FileDto fileDto) throws IOException {
        log.info("FileUtils fileUploadCkEditor");
        MultipartFile fileOrg = fileDto.getFile();

        String fileOrgNm = fileOrg.getOriginalFilename();
        String fileUidNm = createStoreFileName(fileOrgNm);
//            log.info("FileUtils fileUploadCkEditor uploadDirPost = " + uploadDirPost);
        log.info("FileUtils fileUploadCkEditor uploadDirPost = " + uploadDir22);
        checkUploadPath(); // 업로드 디렉토리 - 없을 경우 생성

        fileDto.setFileOrgNm(fileOrgNm); // 검토 : 필요한가
        fileDto.setFileUidNm(fileUidNm);
        fileDto.setWebPath(webDir22);

        File file1 = new File(uploadDir22 + fileUidNm);
        fileOrg.transferTo(file1);

    }

    private static String createStoreFileName(String fileOrgNm) {
        String ext = extractExt(fileOrgNm);
        String fileUidNm = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        return fileUidNm;
    }

    private static String extractExt(String fileOrgNm) {
        return fileOrgNm.substring(fileOrgNm.lastIndexOf(".") + 1);
    }


    // db 저장
    public void saveImage(Object obj) {
        log.info("FileUtils saveImage");
        Set<FileDto> imageSet = new HashSet<>();
        int idx = 0;

        // content로부터 이미지 정보 추출
        // 게시글 저장 모드 - 첫번째 변수가 CkEditorPost 경우만.
        if (obj instanceof CkEditorPost) {
            CkEditorPost identifiablePost = (CkEditorPost) obj;
            imageSet = extractFileDto(identifiablePost); // idx uuid uploadPath
            idx = identifiablePost.getPostId();
        }

        // 게시글 수정 모드 - 첫번째 변수가 Set<FileDto> 인 경우
        // - idx 추출
        if (obj instanceof Set<?>) {
            Set<?> set = (Set<?>) obj;
            Iterator<?> iterator = set.iterator();
            if (iterator.hasNext()) {
                Object element = iterator.next();
                if (element instanceof FileDto) {
                    FileDto randomFile = (FileDto) element;
                    idx = randomFile.getPostIdx();
                    imageSet = (Set<FileDto>) obj;
                }
            }
        }


        // for 루프로 저장
        if (imageSet != null && imageSet.size() > 0) {
            for (FileDto fileDto : imageSet) {
                fileDto.setPostIdx(idx);
                // db 저장
                fileCkEditorDao.insertFile(fileDto);
            }
        }
    }

    // ckEditor content html로부터 이미지 정보 추출
    public Set<FileDto> extractFileDto(CkEditorPost ckEditorPost) {
        log.info("FileUtils extractFileDto");
        Set<FileDto> fileSet = new HashSet<>();
        Document document = Jsoup.parse(ckEditorPost.getContent());
        Elements imgTags = document.select("img");
        int idx = ckEditorPost.getPostId();

        for (Element imgTag : imgTags) {
            String webPathNm = imgTag.attr("src");
            String fileUidNm = extractFileName(webPathNm);
            String webPath = webPathNm.substring(0, webPathNm.length() - fileUidNm.length());

            FileDto fileDto = new FileDto();

//            fileDto.setUploadPath(convertPathWebToUpload(webPath, request));
            fileDto.setUploadPath(webPath);
            fileDto.setFileUidNm(fileUidNm);
            fileDto.setWebPath(webPath);
            fileDto.setPostIdx(idx);

            fileSet.add(fileDto);
        }

        return fileSet;
    }

    public void fileSyncForPostUpdate(CkEditorPost newPost) throws IOException {
        // db 에서 해당 게시물 이미지 set 가져오기
        Set<FileDto> dbUuids = fileCkEditorDao.selectFileDto(newPost.getPostId());

        // 새로운 게시글에서 uuid 추출
        Set<FileDto> contentUuids = extractFileDto(newPost);

        // 1) db - 삭제된 기존 게시물
        Set<FileDto> removedSet = new HashSet<>(dbUuids);
        removedSet.removeAll(contentUuids);
        // - db 삭제처리
        // - 서버 삭제처리
        if (removedSet != null && removedSet.size() > 0) {
            deleteFromServerNDb(removedSet, true);
        }

        // 2) 게시글 - db : 새로추가
        Set<FileDto> addedSet = new HashSet<>(contentUuids);
        addedSet.removeAll(dbUuids);
        // - db 저장
        saveImage(addedSet);
    }

    private String extractFileName(String filePath) {
        log.info("FileUtils extractFileName");
        String[] parts = filePath.split("[/\\\\]");
        return parts[parts.length - 1];
    }

    // 이미지 - 서버 및 db
    @Transactional
    public void deleteFromServerNDb(Set<FileDto> fileSet, boolean partialRemove) throws IOException {
        log.info("FileUtils deleteFileSetFromServerNDb");
        for (FileDto fileDto : fileSet) {
//            String uploadPath = convertPathRelToUp(uploadDirPost) + fileDto.getFileUidNm();
            String uploadPath = uploadDir22 + fileDto.getFileUidNm();
            File file = new File(uploadPath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Failed to deleteFileSetFromServer image: " + uploadPath);
                }
                if (partialRemove) {
                    fileCkEditorDao.deletePartiallyByFno(fileDto.getFno());
                }
            }
        }
        if (!partialRemove) { // 게시물 삭제에 따른 이미지 전체 삭제
            Iterator<FileDto> iterator = fileSet.iterator();
            if (iterator.hasNext()) {
                FileDto randomFile = iterator.next();
                fileCkEditorDao.deleteFileByPostIdx(randomFile.getPostIdx());
            }
        }

    }


// ==== 가비지 이미지 처리 ====

    // 이미지 삭제
    @Scheduled(cron = "0 0 03 * * ?") // 0초 0분 3시 *매일 *매월 ?모든요일
    public void cleanUnusedImages() throws IOException {
        log.info("FileUtils cleanUnusedImages");
//        String uploadPath = convertPathRelToUp(uploadDirPost);
//        String uploadPath = uploadDirPost; // 수정예정 0902 - 불필요한 변수사용

        // db 정보
        Set<String> dbFileSet = fileCkEditorDao.selectAllUids(expirationPeriodSec);
        log.info("FileUtils cleanUnusedImages dbFileSet: " + dbFileSet);
        log.info("----- cleanUnusedImages uploadDirPost: " + uploadDir22);
        Set<String> serverFileSet = getServerFileSet();

        // 서버이미지가 db set에 없으면 삭제
        for (String serverFile : serverFileSet) {
            if (!dbFileSet.contains(serverFile)) {
//                File file = new File(uploadDirPost + serverFile);
                File file = new File(uploadDir22 + serverFile);
                if (file.exists()) {
                    if (!file.delete()) {
//                        throw new IOException("Failed to deleteFileSetFromServer image: " + uploadDirPost);
                        throw new IOException("Failed to deleteFileSetFromServer image: " + uploadDir22);
                    }
                }
            }
        }
    }

    // 서버 파일 리스트 반환
    public Set<String> getServerFileSet() throws IOException {
        log.info("FileUtils getServerFileSet");
        Set<String> serverFileSet = new HashSet<>();

        System.out.println("----- uploadDir22: " + uploadDir22);
        File uploadDirReal = new File(uploadDir22);
        if (uploadDirReal.exists() && uploadDirReal.isDirectory()) {
            File[] files = uploadDirReal.listFiles();
            if (files != null) {
                long currentTime = System.currentTimeMillis();
                for (File file : files) {
                    // 파일의 마지막 수정 날짜가 7일 이후인 경우에만 추가
                    Date lastModifiedDate = new Date(file.lastModified());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(lastModifiedDate);
                    boolean isExpired = currentTime - file.lastModified() > (expirationPeriodSec * 1000L);
                    System.out.println("");
                    System.out.print("----- file name: " + file.getName());
                    System.out.print(" L_____ lastModified: " + formattedDate + " / timeDiff: " + (currentTime - file.lastModified()) + " / isExpired: " + isExpired);
                    if (isExpired) {
                        serverFileSet.add(file.getName());
                        System.out.print(" / added !!!!! ");
                    }

                }
            }
        }
        log.info("FileUtils getServerFileSet serverFileSet: " + serverFileSet);
        return serverFileSet;
    }


// ==== 경로 ====

    // 경로 유무 체크 및 생성
    // ✅
    private void checkUploadPath() {
        // 실제 파일 저장 경로
        Path directoryPath = Paths.get(uploadDir22);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
    }

}
*/

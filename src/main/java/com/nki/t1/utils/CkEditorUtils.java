package com.nki.t1.utils;

import com.nki.t1.domain.CkEditorPost;
import com.nki.t1.dto.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class CkEditorUtils {

    //  게시글 저장 혹은 수정 후 html로부터 db에 저장될 이미지 정보 추출 & postIdx 부여
    public static Set<FileDto> setPostIdx(Object obj) {
        log.info("FileUtils saveImage");
        Set<FileDto> fileDtoSet = new HashSet<>();
        int idx = 0;

        // content로부터 이미지 정보 추출
        // 게시글 저장 모드 - 첫번째 변수가 CkEditorPost 경우만.
        if (obj instanceof CkEditorPost) {
            CkEditorPost identifiablePost = (CkEditorPost) obj;
            fileDtoSet = extractFileDto(identifiablePost); // idx uuid uploadPath
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
                    fileDtoSet = (Set<FileDto>) obj;
                }
            }
        }


        // 파일에 postIdx 연결
        if (fileDtoSet != null && fileDtoSet.size() > 0) {
            for (FileDto fileDto : fileDtoSet) {
                fileDto.setPostIdx(idx);
            }
        }

        return fileDtoSet;
    }

    // ckEditor content html로부터 이미지 정보 추출
    public static Set<FileDto> extractFileDto(CkEditorPost ckEditorPost) {
        log.info("FileUtils extractFileDto");
        Set<FileDto> fileSet = new HashSet<>();
        Document document = Jsoup.parse(ckEditorPost.getContent());
        Elements imgTags = document.select("img");
        int idx = ckEditorPost.getPostId();

        for (Element imgTag : imgTags) {
            String webPathNm = imgTag.attr("src");
            String fileUidNm = FileUtils.extractFileName(webPathNm);
            // web 접근 path
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

}

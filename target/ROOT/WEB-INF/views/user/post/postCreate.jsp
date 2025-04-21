<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="_csrf" content="${_csrf.token}"/>

<script src="https://cdn.ckeditor.com/ckeditor5/41.4.2/classic/ckeditor.js"></script>
<script src="https://cdn.ckeditor.com/ckeditor5/41.4.2/classic/translations/ko.js"></script>

<div class="container mt-5">
    <h2 class="mb-4" id="pageTitle">게시글 작성</h2>
    <form:form id="postForm" modelAttribute="postDto" onsubmit="return validateForm();">
        <div class="form-group mb-3">
            <label for="title">제목</label>
            <form:errors path="title" cssClass="text-danger" />
            <form:input path="title" cssClass="form-control" id="title" required="true" />
            <input type="hidden" name="pno" id="pno" value="0"/>
        </div>

        <div class="form-group mb-3">
            <label for="editor">내용</label>
            <form:errors path="content" cssClass="text-danger" />
            <form:textarea path="content" cssClass="form-control" id="editor"/>
        </div>

        <button type="submit" class="btn btn-primary">등록</button>
    </form:form>
</div>

<script>
    var mode = "${mode}";
    var orgPage = "${orgPage}";
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : null;
    const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : null;

    $(function () {
        if (mode === "update") {
            var pno = "${postDto.pno}";
            $('#pageTitle').text("게시글 수정");
            $('#pno').val(pno);

            $('#postForm').attr('action', contextPath + '/user/post/postEdit?orgPage=' + orgPage);
            $('#postForm').attr('method', 'POST');
        } else {
            $('#pageTitle').text("게시글 등록");
            $('#postForm').attr('action', contextPath + '/user/post/post');
            $('#postForm').attr('method', 'POST');
        }
    });

    let editorInstance;

    ClassicEditor
        .create(document.querySelector('#editor'), {
            language: 'ko',
            ckfinder: {
                uploadUrl: contextPath + '/user/post/ckUpload',
                withCredentials: true,
                headers: csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {}
            }
        })
        .catch(error => {
            console.error(error);
        });
/*
    ClassicEditor
        .create(document.querySelector('#editor'), {
            language: 'ko',
            ckfinder: {
                uploadUrl: contextPath + '/user/post/ckUpload',
                withCredentials: true,
                headers: csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {}
            }
        })
        .then(editor => {
            editorInstance = editor;
            setupCustomUploadAdapter(editor);
        })
        .catch(error => {
            console.error(error); // 오류 로깅
        });

    function setupCustomUploadAdapter(editor) {
        editor.plugins.get('FileRepository').createUploadAdapter = loader => {
            return {
                upload: function () {
                    return loader.file
                        .then(file => {
                            let formData = new FormData();
                            formData.append("upload", file);

                            let headers = {};
                            if (csrfHeader && csrfToken) {
                                headers[csrfHeader] = csrfToken;
                            }

                            return fetch(contextPath + '/user/post/ckUpload', {
                                method: "POST",
                                body: formData,
                                headers: headers
                            })
                                .then(response => {
                                    console.log("서버 응답 상태 코드:", response.status);
                                    return response.text();  // JSON이 아닌 경우에도 응답 확인
                                })
                                .then(text => {
                                    console.log("서버 응답 본문:", text);  // 서버 응답을 출력
                                    let result;
                                    try {
                                        result = JSON.parse(text);  // JSON 변환 시도
                                    } catch (error) {
                                        throw new Error("서버 응답이 JSON 형식이 아닙니다.");
                                    }

                                    if (result.uploaded === 0) {
                                        throw new Error(result.error.message);
                                    }
                                    return result;
                                })
                                .catch(error => {
                                    alert("파일 업로드 실패: " + error);
                                });
                        });
                }
            };
        };
    }*/


    // 유효성 검사 함수
    function validateForm() {
        const contentData = editorInstance.getData().trim();

        if (contentData === "") {
            alert("내용을 입력해 주세요."); // 경고 메시지 표시
            return false; // 폼 제출 중지
        }
        return true; // 폼 제출 허용
    }
</script>

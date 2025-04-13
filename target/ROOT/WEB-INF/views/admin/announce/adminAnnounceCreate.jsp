<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:set var="isEditMode" value="${announceDto != null}"/>
<c:url var="actionUrl" value="${isEditMode ? '/admin/announces/put' : '/admin/announces'}" />

<script src="https://cdn.ckeditor.com/ckeditor5/41.4.2/classic/ckeditor.js"></script>
<script src="https://cdn.ckeditor.com/ckeditor5/41.4.2/classic/translations/ko.js"></script>

<div class="container mt-5">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${isEditMode}">공지 수정</c:when>
            <c:otherwise>공지 등록</c:otherwise>
        </c:choose>
    </h2>
    <form:form modelAttribute="announceDto" method="post" action="${actionUrl}" enctype="multipart/form-data">
    <!-- Hidden 필드: ano -->
    <form:hidden path="ano" />

    <div class="form-group mb-2" style="width: 400px">
        <div class="input-group">
            <span class="input-group-text">유저 페이지 노출일</span>
            <c:choose>
                <c:when test="${isEditMode}">
                    <!-- 수정 시: postedAt의 최소값을 createdAt으로 설정 -->
                    <form:input path="postedAt" cssClass="form-control postedAtCalendar"
                                id="postedAtUpdate"
                                min="${announceDto.createdAt}" max="9999-12-31" />
                </c:when>
                <c:when test="${postedAt != null and postedAt != ''}">
                    <!-- 조건에 따라 postedAt이 별도로 존재할 경우 -->
                    <form:input path="postedAt" cssClass="form-control postedAtCalendar"
                                id="postedAtCreate"
                                min="" max="9999-12-31" />
                </c:when>
                <c:otherwise>
                    <!-- 등록 시: postedAt 입력값이 필수 -->
                    <form:input path="postedAt" cssClass="form-control postedAtCalendar"
                                id="postedAtCreate"
                                min="" max="9999-12-31" required="true" />
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="form-group mb-1">
        <label for="title">제목</label>
        <form:errors path="title" cssClass="text-danger"/>
        <form:input path="title" cssClass="form-control" id="title" required="true"/>

    </div>

    <div class="form-group mb-1">
        <label for="editor">내용</label>
        <form:errors path="content" cssClass="text-danger"/>
        <form:textarea path="content" cssClass="form-control" id="editor"/>
    </div>

    <div class="form-group mb-3">
        <label for="attachedFiles">첨부파일</label>
        <!-- 파일 업로드는 form 태그 내 일반 input 태그를 사용 -->
        <input type="file" class="form-control" id="attachedFiles" name="attachedFiles" multiple/>
        <c:if test="${isEditMode}">
            <c:forEach var="fileDto" items="${announceDto.fileDtoList}">
                <div class="file-item">
                    <input type="checkbox" class="delete-file-checkbox" data-fno="${fileDto.fno}"
                           value="${fileDto.fno}"> ${fileDto.fileOrgNm}
                </div>
            </c:forEach>
            <input type="hidden" id="deleteFiles" name="fnoList">
        </c:if>
    </div>

    <div class="btn-group">
        <button type="submit" class="btn btn-primary">등록</button>
        <c:if test="${isEditMode}">
            <button type="button" class="btn btn-secondary"
                    onclick="location.href='<c:url value='/admin/announces/${announceDto.ano}?page=1'/>'">
                취소
            </button>
        </c:if>
    </div>
    </form:form>
</div>

<script>
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    ClassicEditor
        .create(document.querySelector('#editor'), {
            language: 'ko',
            ckfinder: {
                uploadUrl: contextPath + '/admin/announces/images',
                withCredentials: true,
                headers: csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {}
            }
        })
        .catch(error => {
            console.error(error); // 오류 로깅
        });

    $(document).ready(function () {
        var isEditMode =
        ${isEditMode ? 'true' : 'false'} ===
        'true';
        var isPostedAtPresent = '${postedAt}' !== 'null' && '${postedAt}' !== '';
        var postedAtMin = '';
        var postedAtValue = '';

        if (isEditMode) {
            // 수정시 - 게시일 최소: 작성일
            postedAtMin = '${announceDto.createdAt}';
            postedAtValue = '${announceDto.postedAt}';
        } else if (isPostedAtPresent) {
            // "게시 일정" 메뉴에서 날짜를 미리 선택해서 들어온 경우 - 게시일 최소: 오늘 & 선택: 선택해 들어온 일자
            postedAtMin = moment().format('YYYY-MM-DD');
            postedAtValue = '${postedAt}';
        } else {
            // "새글쓰기" 버튼으로 들어온 경우 - 게시일 최소: 오늘
            postedAtMin = moment().format('YYYY-MM-DD');
        }

        $('.postedAtCalendar').daterangepicker({
            singleDatePicker: true,       // 하루만 선택 가능하도록 설정
            showDropdowns: true,
            autoUpdateInput: true,
            autoclose: true,
            locale: {
                format: 'YYYY-MM-DD'
            },
            minDate: postedAtMin !== '' ? postedAtMin : null, // 최소 날짜 설정
            startDate: postedAtValue !== '' ? postedAtValue : moment().format('YYYY-MM-DD') // 기본 선택 날짜 설정
        });

        setToday();

        // 폼 제출 시 유효성 검사
        $('form').on('submit', function (e) {
            var title = $('#title').val().trim();
            var content = $('#content').val().trim();
            var postedAt = $('#postedAtCreate').val() || $('#postedAtUpdate').val();
            var errorMsg = ''; // 경고 메시지 저장 변수

            if (!title) {
                errorMsg += '제목을 입력해 주세요.\n';
            }

            if (!content) {
                errorMsg += '내용을 입력해 주세요.\n';
            }

            if (!postedAt) {
                errorMsg += '게시일을 선택해 주세요.\n';
            }

            // 경고 메시지가 있는 경우 alert 띄우고 폼 제출 막기
            if (errorMsg) {
                alert(errorMsg);
                e.preventDefault(); // 폼 제출 막기
            }
        });

        // 파일 선택 시 확장자 체크
        $('#attachedFiles').on('change', function () {
            validateFileExtensions(this);
        });


        // 삭제 체크박스 클릭 시
        $('.deleteFileSetFromServer-file-checkbox').on('change', function () {
            var fno = $(this).data('fno');

            if (this.checked) {
                // 체크된 경우, 해당 항목을 숨기고 삭제 리스트에 추가
                $(this).closest('.file-item').hide();
                addFileToDeleteList(fno);
            }
        });

    });

    // 파일 확장자 유효성 검사
    function validateFileExtensions(input) {
        var validExtensions = ['pdf', 'xls', 'xlsx', 'txt', 'doc', 'docx', 'jpg', 'jpeg', 'png', 'gif'];
        var files = input.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var fileName = file.name;
            var fileExtension = fileName.split('.').pop().toLowerCase();
            if (!validExtensions.includes(fileExtension)) {
                alert('허용된 파일 형식이 아닙니다. (PDF, 엑셀, 텍스트, 워드, 이미지 파일만 업로드 가능합니다)');
                input.value = '';  // 파일 입력 초기화
                return false;
            }

            attachedFileSizeCheck(file, input);
        }
        return true;
    }

    // 파일 크기 검사 함수
    function attachedFileSizeCheck(file, input) {
        alert("attachedFileSizeCheck");

        var formData = new FormData();
        formData.append("file", file);

        return $.ajax({
            url: '/admin/announces/file-size-check',
            method: 'POST',
            data: formData,
            processData: false,
            dataType: 'json',
            success: function (response) {
                alert(response);
            }, error: function (response) {
                input.value = '';
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function setToday() {
        // 현재 날짜 구하기
        var today = new Date();
        var year = today.getFullYear();
        var month = ('0' + (today.getMonth() + 1)).slice(-2);  // 월은 0부터 시작하므로 +1
        var day = ('0' + today.getDate()).slice(-2);  // 날짜가 한 자리일 경우 앞에 0 추가

        var formattedDate = year + '-' + month + '-' + day;

        $('#postedAtCreate').attr('min', formattedDate);

    }


    // 파일 번호를 삭제 리스트에 추가
    function addFileToDeleteList(fno) {
        var deleteFiles = $('#deleteFiles').val();
        if (deleteFiles) {
            deleteFiles += ',' + fno;
        } else {
            deleteFiles = fno;
        }
        $('#deleteFiles').val(deleteFiles);
    }
</script>

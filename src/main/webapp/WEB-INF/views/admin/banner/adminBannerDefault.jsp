<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<section class="container my-4">
    <ul class="text-body-secondary">
        <li>등록된 배너가 없을 경우 노출됩니다.</li>
        <li>연결된 공지는 없으며, 1개만 등록 가능합니다.</li>
        <li>기존 배너는 삭제 불가합니다. 변경을 위해서는 새로운 배너를 등록하세요.</li>
    </ul>

    <!-- 현재 기본 배너 -->
    <div class="card my-4">
        <h5 class="card-header mb-0 fw-bold">현재 기본 배너</h5>
        <div class="card-body">
            <div class="mb-3">
                <span class="card-text text-muted">등록일 : ${bannerDto.formattedCreatedAt}</span>
                <span>&nbsp|&nbsp</span>
                <span class="card-text text-muted">등록자 : ${bannerDto.userId} (회원번호: ${bannerDto.uno})</span>
            </div>

            <div class="mb-3">
                <img src="/public/banners/${bannerDto.fileDto.fileUidNm}" class="d-block w-100 img-fluid"
                     alt="기본 배너 이미지">
            </div>
        </div>
    </div>

    <!-- 기본 배너 변경 -->
    <div class="card my-4">
        <h5 class="card-header mb-0 fw-bold">기본 배너 변경</h5>
        <div class="card-body">
            <form id="banner-form" enctype="multipart/form-data">
                <div class="mb-3">
                    <input type="file" name="image" id="banner-image-input" class="form-control"
                           accept=".jpg, .jpeg, .png" required>
                </div>
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-primary me-2" onclick="updateBanner()">변경</button>
                    <button type="button" class="btn btn-secondary" onclick="cancelBanner()">취소</button>
                </div>
            </form>
        </div>
    </div>
</section>

<script>
    function updateBanner() {
        var bannerImage = $('#banner-image-input')[0].files[0];
        // var memo = $('#banner-memo').val();

        if (typeof bannerImage === "undefined") {
            alert("배너이미지를 선택해주세요.");
            return false;
        }

        var formData = new FormData();
        formData.append("ano", 0);
        if (bannerImage) {
            formData.append("image", bannerImage); // 파일 추가
        }
        // formData.append("memo", memo);

        $.ajax({
            url: contextPath + "/admin/banners/default",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (response) {
                alert("기본 배너가 변경되었습니다.");
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function cancelBanner() {
        $('#banner-image-input').val("");
    }

</script>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section id="banners" class="container mb-3">
    <div id="carouselExampleIndicators" class="carousel slide">
        <div class="carousel-indicators">
            <c:forEach var="bannerDto" items="${bannerDtoList}" varStatus="status">
                <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="${status.index}"
                        class="<c:if test='${status.index == 0}'>active</c:if>" aria-label="Slide ${status.index + 1}"></button>
            </c:forEach>
        </div>
        <div class="carousel-inner">
            <c:forEach var="bannerDto" items="${bannerDtoList}" varStatus="status">
                <div style="height: 600px;" class="carousel-item d-flex justify-content-center align-items-center<c:if test='${status.index == 0}'>active</c:if>">
                    <c:choose>
                        <c:when test="${bannerDto.id != 0}"> <!-- 디폴트 배너가 아닐 경우 -->
                            <img src='<c:url value="${bannerDto.fileDto.uploadPath}"/>' class="d-block banner-img" alt="배너 이미지" onclick="location.href= contextPath + '/user/announces/' + ${bannerDto.ano} + '?page=1'">
                        </c:when>
                        <c:otherwise> <!-- 디폴트 배너일 경우 - 링크가 없다. -->
                            <img src="${bannerDto.fileDto.uploadPath}   " class="d-block banner-img" alt="배너 이미지">
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</section>

<hr class="mb-3">

<section id="announce" class="container mb-3">
    <div class="d-flex  justify-content-between">
        <h3 class="mb-3">공지사항</h3>
        <c:if test="${announceDtoList.size() > 0 }">
            <div align="right" class="mb-3">
                <button type="button" onclick="location.href=contextPath + '/user/announces?page=1'" class="btn btn-outline-secondary btn-sm">더보기 ></button>
            </div>
        </c:if>
    </div>
    <div>
        <table class="table table-hover">
            <colgroup>
                <col style="">
                <col style="width: 12rem;">
            </colgroup>
            <thead>
                <tr>
                    <th>제목</th><th>게시일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="announceDto" items="${announceDtoList}" >
                    <tr style="cursor: pointer;" onclick="location.href=contextPath + '/user/announces/' + ${announceDto.ano} + '?page=1'">
                        <td>${announceDto.title} <c:if test="${announceDto.fileCnt > 0}"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-floppy2-fill" viewBox="0 0 16 16">
                            <path d="M12 2h-2v3h2z"/>
                            <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0zM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1zM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1"/>
                        </svg></c:if></td>
<%--                        <td>${announceDto.title} <c:if test="${announceDto.fileCnt > 0}"><span class="badge text-bg-secondary">${announceDto.fileCnt}</span></c:if></td>--%>
                        <td>${announceDto.postedAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</section>

<%--<img id="banner-upload-thumbnail" style="object-fit: contain; width: 230px; height: 230px;"src=""/>
배너 이미지 가져오기 $('#banner-upload-thumbnail').attr('src', "/t1/upload/banner/" + bannerDto.fileDto.fileUidNm);--%>
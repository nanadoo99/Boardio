<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<button id="btnGarbage">CkEditor 가비지 처리</button>

<script>
    $(document).ready(function(){
        $('#btnGarbage').click(function(){
            $.ajax({
                url: '/admin/test/ckEditor-garbage',
                method: 'GET',
                success: function(response) {
                    alert(response);
                },
                error: function() {
                    alert('오류가 발생했습니다.');
                }
            });
        });
    });
</script>

<div> ${authUser.id} 님, 어서오세요</div>
<nav class="navbar navbar-expand-lg py-2">
    <div class="container">
        <a class="navbar-brand" href="<c:url value='/admin'/>">관리자 페이지</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <li class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 col-md-8">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/user-management?page=1'/>">회원 관리</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        공지사항 관리
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="<c:url value='/admin/announces/schedule'/>">게시 일정</a></li>
                        <li><a class="dropdown-item" href="<c:url value='/admin/announces?page=1'/>">전체 목록</a></li>
                        <li><a class="dropdown-item" href="<c:url value='/admin/announces/new'/>">새 공지 작성</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        배너 관리
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="<c:url value='/admin/banners'/>">게시 일정 및 등록</a></li>
                        <li><a class="dropdown-item" href="<c:url value='/admin/banners/default'/>">기본 배너</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/admin/posts?page=1'/>">게시글 관리</a>
                </li>
                <li class="nav-item me-2">
                    <a class="nav-link" href="<c:url value='/admin/comments?page=1'/>">댓글 관리</a>
                </li>
                <li class="nav-item">
                    <button class="btn btn-outline-success" type="button"
                            onclick="location.href='<c:url value='/'/>'">유저페이지
                    </button>
                </li>
            </ul>

            <div class="text-end">
                <button class="btn btn-outline-primary me-2" type="button"
                        onclick="location.href='<c:url value='/auth/logout'/>'">logOut</button>
            </div>
    </div>
    </div>
</nav>



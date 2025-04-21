<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <meta name="_csrf" content="${_csrf.token}">

    <title>Boardio</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/comm/comm.css"/>

    <script src="/static/js/jquery-3.7.1.min.js"></script>
    <script src="/static/js/comm/comm.js"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css">
    <script src="https://cdn.jsdelivr.net/npm/moment/min/moment.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>

    <!-- CSS for full calender -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/3.9.0/fullcalendar.min.css" rel="stylesheet"/>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap JavaScript (Popper 포함) -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>

    <!-- Swiper CSS -->
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
    <!-- Swiper JS -->
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>

    <script>
        <%--var contextPath = '${pageContext.request.contextPath}';--%>
        var contextPath = '';
        var authUserUno = '${sessionScope.authUser.uno}';
        var authUserId = '${sessionScope.authUser.id}';
        var authUserEmail = '${sessionScope.authUser.email}';
        var authUserRole = '${sessionScope.authUser.userRole}';
        var authSocial = '${sessionScope.authUser.social}';
        var defaultErrorMsg = "알 수 없는 오류가 발생했습니다.";
        var defaultStartDt = moment('2024-01-01').format('YYYY-MM-DD');
        var defaultEndDt = moment().startOf('day').format('YYYY-MM-DD');


        document.addEventListener('DOMContentLoaded', function () {
            /*            var error = ''; //Attribute("error << 로 검색
                        if (error !== "" && error) {
                            alert(error?.message||defaultErrorMsg);
                        }*/

            var errorMsg = '${errorResponse.message}';
            var error = '${errorResponse}';
            if (error !== "" && error) {
                alert(errorMsg || error);
            }

            var message = '${message}'; //Attribute("error << 로 검색
            if (message !== "" && message) {
                alert(message);
            }

        });

    </script>
    <style>
        #spinner-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .spinner-background {
            position: absolute;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
        }

        .post-title {
            width: 75%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .ck-editor__editable_inline {
            min-height: 400px;
        }

        table {
            table-layout: fixed;
        }

        th {
            word-break: keep-all;
        }

        td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .btn{
            white-space:nowrap;
        }


    </style>

</head>

<body>
<div id="spinner-overlay" class="d-none">
    <div class="spinner-background"></div>
    <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
    </div>
</div>

<header>
    <tiles:insertAttribute name="header"/>
</header>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <tiles:insertAttribute name="nav"/>
</nav>
<main class="container my-4">
    <tiles:insertAttribute name="main"/>
</main>
<footer class="bg-light text-center py-3">
    <tiles:insertAttribute name="footer"/>
</footer>
</body>
</html>

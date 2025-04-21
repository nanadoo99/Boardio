<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container">
    <nav style="--bs-breadcrumb-divider: '>';" aria-label="breadcrumb">
        <ol class="breadcrumb">
            <c:forEach var="crumb" items="${pagePath}">
                <li class="breadcrumb-item">
                    <c:choose>
                        <c:when test="${not empty crumb.getUrl()}">
                            <a href="<c:url value='${crumb.getUrl()}'/>">${crumb.getNameKor()}</a>
                        </c:when>
                        <c:otherwise>
                            ${crumb.getNameKor()}
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ol>
    </nav>
</div>

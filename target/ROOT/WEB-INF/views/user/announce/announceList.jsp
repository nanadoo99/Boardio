<%--
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section id="searchSection">
    <form id="searchForm" onsubmit="return false;">
        <input type="hidden" id="page" name="page" value="1"/>
        <select name="option1">
            <option value="" ${sc.option1 == '' ? 'selected' : ''}>전체</option>
            <option value="title" ${sc.option1 == 'title' ? 'selected' : ''}>제목</option>
            <option value="content" ${sc.option1 == 'content' ? 'selected' : ''}>내용</option>
        </select>
        <input type="text" name="keyword"/>
        <button type="submit" onclick="announceSearch();">검색</button>
    </form>
</section>

<section id="listSection">
    <table class="table">
        <thead>
            <tr>
                <th>제목</th>
                <th>댓글</th>
                <th>조회수</th>
                <th>날짜</th>
            </tr>
        </thead>
        <tbody id="listTbody">
        </tbody>
    </table>
</section>

<section id="paginationSection">
    <div id="pagination"></div>
</section>

<script>
    $(function () {
        announceList(${param.page});
        const mode = "${mode}";
        if (mode === "delete") {
            alert("삭제 완료");
        }
    });

    function announceSearch() {
        const formData = $('#searchForm').serialize();
        changeURL(formData);
        announceList(1);
    }

    function announceList(page) {
        applyQueryParamsToForm();
        $('#page').val(page);

        $.ajax({
            url: contextPath + '/user/announces/list',
            type: 'POST',
            data: $('#searchForm').serialize(),
            dataType: 'json',
            success: function (response) {
                const list = response.list;
                const ph = response.ph;
                listCont(list);
                pagination(ph);
                changeURLOnlyPage(page);
            }
        });
    }

    function announceSel(ano) {
        const param = window.location.href.split('?')[1];
        location.href = contextPath + '/user/announces/' + ano + '?' + param;
    }

    function listCont(list) {
        if (list.length) {
            list.forEach(function (announce) {
                cont += `<tr onclick="announceSel(${announce.ano});" class="announceTr" id="announceTr${announce.ano}">`;
                cont += `<td>${announce.title}</td>`;
                cont += `<td>${announce.cmtCnt}</td>`;
                cont += `<td>${announce.views}</td>`;
                cont += `<td class="createdAt">${announce.createdAt}</td>`;
                cont += '</tr>';
            });
        } else {
            cont += "<tr><td colspan='4'>No data</td></tr>";
        }
        $('#listTbody').html(cont);
    }

    function pagination(ph) {
        const totalCnt = ph.totalCnt;
        const beginPage = ph.beginPage;
        const endPage = ph.endPage;
        let cont = '';

        if (totalCnt) {
            if (ph.showPrev) {
                cont += `<a class="page" onclick="announceList(${beginPage - 1}, true)">&lt;</a>`;
            }
            for (let i = beginPage; i <= endPage; i++) {
                cont += `<a class="page" onclick="announceList(${i}, true)" ${$('#page').val() == i ? 'style="color: aquamarine"' : ''}>${i}</a>`;
            }
            if (ph.showNext) {
                cont += `<a class="page" onclick="announceList(${endPage + 1}, true)">&gt;</a>`;
            }
        }
        $('#pagination').html(cont);
    }
</script>--%>

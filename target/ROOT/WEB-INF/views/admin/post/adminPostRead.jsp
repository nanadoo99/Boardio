<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<input type="hidden" id="searchCondition" value="${sc.queryString}"/>
<section id="post-section" class="container mt-4">
    <div id="post">
        <input type="hidden" id="postPno" value=""/>
        <input type="hidden" id="postState" value=""/>
        <div style="display: none" id="rpList"></div>

        <div class="" id="postStateTxt1"></div>

        <h2 id="postTitle" class="mb-3 post-title"></h2>
        <hr>

        <div class="text-end mb-3">
            <span class="text-muted" id="createdAt">}</span>
            <span>&nbsp|&nbsp</span>
            <span class="text-muted" id="updatedAt"></span>
        </div>

        <div id="postContent" class="mb-4"></div>

        <div class="" id="postStateTxt2"></div>

        <div class="d-flex justify-content-between mb-3">
            <div id="btnBox">
                <button class="btn btn-outline-secondary" onclick="backToList()">목록</button>
            </div>
            <div id="btnBox1"></div>
        </div>
    </div>
</section>

<section id="report-section" class="container mt-4 card">
    <div class="card-body">
        <div class="mb-4">
            <h4>신고 통계 (총 <span id="reportCount"></span>건)</h4>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>신고 사유</th>
                    <th>누적 신고 수</th>
                </tr>
                </thead>
                <tbody id="report-summary-tbody"></tbody>
            </table>
        </div>
        <hr>
        <div>
            <h4 class="mt-4">신고 내역</h4>
            <div id="report-history" class="table-responsive" style="max-height: 500px;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>신고 사유</th>
                        <th>신고자</th>
                        <th>신고일</th>
                    </tr>
                    </thead>
                    <tbody id="report-history-tbody"></tbody>
                </table>
            </div>
        </div>
    </div>
</section>

<section id="comment-section" class="container mt-4">
    <h4 class="mb-3">댓글 (총 <span id="commentCount"></span>개)</h4>
    <div id="comments" class="mb-3"></div>
</section>


<section id="commentReportHistoryPop" class="modal" tabindex="-1" data-bs-backdrop="true" style="display: none">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">댓글신고 내역</h5>
                <button type="button" class="btn-close" onclick="commentReportHistoryPopClose();"
                        data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <table id="commentReportHistorySummaryTable" class="table">
                    <thead>
                    <tr>
                        <th>사유</th>
                        <th>횟수</th>
                    </tr>
                    </thead>
                    <tbody id="commentReportHistorySummaryTbody"></tbody>
                </table>
                <div  style="max-height: 500px; overflow:auto">
                    <table id="commentReportHistoryDetailTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>날짜</th>
                            <th>사유</th>
                            <th>신고자</th>
                        </tr>
                        </thead>
                        <tbody id="commentReportHistoryDetailTbody"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="blockPop" class="modal" tabindex="-1" data-bs-backdrop="true" style="display: none">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="blockPopTitle" class="modal-title"></h5>
                <button type="button" class="btn-close" onclick="blockPopClose();" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="blockPopForm" onsubmit="return false;">
                    <input type="hidden" name="" id="blockPopIdx"/>
                    <input type="hidden" name="uno" id="blockPopUno"/>
                    <input type="hidden" name="memo" id="blockPopMemo"/>
                    <p>차단 사유</p>
                    <div class="w-100">
                        <c:forEach var="brDto" items="${brList}">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="brno${brDto.brno}" name="brnoList"
                                       value="${brDto.brno}">
                                <label class="form-check-label" for="brno${brDto.brno}">
                                        ${brDto.brText}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="blockPostBtn" class="btn btn-warning" onclick="">차단</button>
            </div>
        </div>
    </div>
</section>


<section id="commentBlockPop" class="modal" tabindex="-1" data-bs-backdrop="true" style="display: none">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="commentBlockPopTitle" class="modal-title"></h5>
                <button type="button" class="btn-close" onclick="commentBlockPopClose();" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="commentBlockPopForm" onsubmit="return false;">
                    <input type="hidden" name="" id="commentBlockPopIdx"/>
                    <input type="hidden" name="uno" id="commentBlockPopUno"/>
                    <p>차단 사유</p>
                    <div class="w-100">
                        <c:forEach var="brDto" items="${brList}">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="commentBrno${brDto.brno}"
                                       name="brnoList"
                                       value="${brDto.brno}">
                                <label class="form-check-label" for="commentBrno${brDto.brno}">
                                        ${brDto.brText}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="blockCommentBtn" class="btn btn-warning" onclick="">차단</button>
            </div>
        </div>
    </div>
</section>

<%--<div id="blockPop" class="popSection" style="display: none">
    <div class="popContent">
        <span class="popClose" onclick="blockPopClose();">&times;</span>
        <h3 id="blockPopTitle"></h3>
        <form id="blockPopForm" onsubmit="return false;">
            <input type="text" name="" id="blockPopIdx"/>
            <input type="text" name="uno" id="blockPopUno"/>
            <input type="text" name="memo" id="blockPopMemo"/>
            차단 사유:
            <div id="brno">
                <c:forEach var="brDto" items="${brList}">
                    <label>
                        <input type="checkbox" name="brnoList" value="${brDto.brno}">
                            ${brDto.brText}
                    </label><br>
                </c:forEach>
            </div>
            <button type="button" id="blockPostBtn" onclick="">차단</button>
        </form>
    </div>
</div>--%>

<script>
    $(function () {
        readPost(${pno});
    });

    function backToList() {
        // org: post 혹은 comment
        location.href = contextPath + "/admin/${org}" + $('#searchCondition').val();
    }

    function readPost(pno) {
        $('#postPno').val(pno);
        $.ajax({
            type: "GET",
            url: contextPath + "/admin/posts/" + pno + "/data",
            dataType: "json",
            success: function (response) {
                completePost(response);
                var tags = "";
                var tags1 = "";

                if (response.postDto.contentState != "BLOCKED") {
                    tags += "<button type='button' class='btn btn-secondary me-2' onclick='userPost();'>유저페이지에서 보기</button>";
                    tags1 += "<button type='button' class='btn btn-danger' data-bs-toggle='modal' data-bs-target='#blockPop' onclick='postBlockPopOpen();'>게시글 차단</button>";
                } else {
                    tags1 += "<button type='button' class='btn btn-primary me-2' data-bs-toggle='modal' data-bs-target='#blockPop' onclick='postBlockPopOpen();' class='btn'>차단 사유 변경</button>";
                    tags1 += "<button type='button' class='btn btn-danger' onclick='postUnblock();' class='btn'>게시글 차단 해제</button>";
                }
                tags += "<button class='btn btn-outline-secondary' onclick='backToList()'>목록</button>";

                $('#btnBox').html(tags);
                $('#btnBox1').html(tags1);
                readComments();
                readReports();
                // if (response.postDto.rpnoCnt != 0) readReports();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
                backToList();
            }
        })
    }

    function completePost(response) {
        var postDto = response.postDto;
        var bpList = response.bpList;

        postState(postDto, bpList);

        $('#postTitle').text(postDto.pno + ")  " + postDto.title);
        $('#postState').val(postDto.contentState);
        $('#postContent').html(postDto.content);
        $('#createdAt').text("최초 작성: " + postDto.formattedCreatedAtTime);
        $('#updatedAt').text("마직막 수정: " + (postDto.formattedUpdateAtTime ? postDto.formattedUpdateAtTime : "-"));
    }

    function postState(postDto, bpList) {
        var target1 = $('#postStateTxt1');
        var target2 = $('#postStateTxt2');
        var contentState = postDto.contentState;
        var className = "alert alert-";
        var postStateTxt = "상태: " + postDto.contentStateKorNm;

        target1.removeClass();
        target2.removeClass();

        switch (contentState) {
            case 'UNREPORTED_UNBLOCKED':
                className += "secondary";
                break;
            case 'REPORTED':
                className += "warning";
                break;
            case 'BLOCKED':
                className += "danger";
                if (bpList.length > 0) {
                    postStateTxt += " (";

                    var bpTexts = bpList.map(function (bpDto) {
                        $('#blockPopForm input[name="brnoList"][value="' + bpDto.brno + '"]').prop("checked", true);
                        return bpDto.brText;
                    });

                    postStateTxt += bpTexts.join(", ") + ")";
                }
                break;
        }

        target1.addClass(className);
        target1.text(postStateTxt);
        target2.addClass(className);
        target2.text(postStateTxt);
    }

    function readReports() {
        var pno = $('#postPno').val();
        $.ajax({
            type: "GET",
            url: contextPath + "/admin/posts/" + pno + "/reports",
            dataType: "json",
            success: function (response) {
                $('#reportCount').text(response.reportCount);
                completeReportHistory(response.reportList);
                completeReportSummary(response.reportSummary);

            }
        });
    }

    function completeReportHistory(reportList) {
        var content = '';
        $.each(reportList, function (index, report) {
            var reportedAtDate = new Date(report.reportedAt);
            var reportedAtStr = reportedAtDate.toLocaleDateString() + ' ' + reportedAtDate.toLocaleTimeString();

            content += `<tr>
                    <td>` + (report.brText || '') + `</td>
                    <td>` + (report.reportUserId || '') + `</td>
                    <td>` + (reportedAtStr || '') + `</td>
                </tr>`;
        });

        if (!reportList.length) {
            content = `<tr><td colspan="3" class="text-center">no data</td></tr>`;
        }

        $('#report-history-tbody').html(content);
    }

    function completeReportSummary(reportSummary) {
        var content = '';
        $.each(reportSummary, function (index, summary) {

            content += `<tr>
                    <td>` + (summary.brText || '') + `</td>
                    <td>` + (summary.brnoCnt || '') + `</td>
                </tr>`;
        });
        $('#report-summary-tbody').html(content);
    }

    function userPost() {
        var sc = $('#postSearch').serialize();
        location.href = contextPath + "/user/post/read/" + $('#postPno').val() + '?' + sc;
    }

    // ===== 게시글 차단 ===== //
    function postBlockPopOpen() {
        if ($('#postState').val() == 'BLOCKED') {
            $('#blockPopTitle').text("게시글 차단 사유 변경");
            $('#blockPostBtn').text("변경");
        } else {
            $('#blockPopTitle').text("게시글 차단");
            $('#blockPostBtn').text("차단");
        }
        $('#blockPostBtn').attr("onclick", "blockPost();");
        $('#blockPop').show();

    }

    function blockPost() {
        if (!confirm("게시글을 차단하시겠습니까?")) return;
        if ($('input[name="brnoList"]:checked').length === 0) {
            alert("차단 사유를 선택하세요.");
            return;
        }
        var pno = $('#postPno').val();
        $('#blockPopIdx').attr("name", "pno");
        $('#blockPopIdx').val(pno);
        $('#blockPopUno').val(authUserUno);

        $.ajax({
            type: "POST",
            url: contextPath + "/admin/posts/" + pno + "/block",
            data: $('#blockPopForm').serialize(),
            success: function (response) {
                alert("차단이 접수되었습니다.");
                blockPopClose();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }
        });
    }

    function blockPopClose() {
        $('#blockPop').modal('hide');

        $('#blockPopTitle').text("");
        $('#blockPopIdx').attr("name", "");
        $('#blockPopIdx').val("");
        $('#blockPopUno').val("");
        $('#blockPopMemo').val("");
        $('#blockPostBtn').attr("onclick", "");
        $("input[name='brnoList']").prop("checked", false);

        readPost($('#postPno').val());
    }

    function postUnblock() {
        var pno = $('#postPno').val();
        if (!confirm("게시글 차단을 해제하시겠습니까?")) return;
        $.ajax({
            type: 'DELETE',
            url: contextPath + "/admin/posts/" + pno + "/block",
            contentType: 'application/json',
            data: JSON.stringify({pno: pno, uno: authUserUno}),
            dataType: 'json',
            success: function (response) {
                alert("차단이 해제되었습니다.");
                readPost(pno);
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }

        })
    }

</script>
<script src="/static/js/commentAdmin.js"></script>
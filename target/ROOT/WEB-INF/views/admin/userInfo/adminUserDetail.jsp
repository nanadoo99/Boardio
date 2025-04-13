<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="userRole" value="${userAdminDto.userRole}"/>


<!-- 사용자 정보 -->
<section class="mb-4">
    <button class="btn btn-outline-secondary" onclick="backToList()">목록</button>
</section>

<section class="mb-4">
    <div class="card">
        <h5 class="card-header bg-dark text-white fw-bold">
            회원 정보
        </h5>
        <div class="card-body">
            <input type="hidden" id="uno" value="${uno}"/>
            <input type="hidden" id="searchCondition" value="${sc.queryString}"/>
            <table class="table table-bordered">
                <colgroup>
                    <col style="width: 10rem;">
                    <col style="width: 12rem;">
                    <col style="width: 10rem;">
                    <col style="width: 12rem;">
                    <col style="width: 10rem;">
                    <col style="width: 12rem;">
                </colgroup>
                <tbody>
                <tr>
                    <th class="bg-body-tertiary">사용자 번호</th>
                    <td>${uno}</td>
                    <th class="bg-body-tertiary">사용자 이메일</th>
                    <td colspan="3">${userAdminDto.email}</td>
                </tr>
                <tr>
                    <th class="bg-body-tertiary">권한</th>
                    <td>
                        <c:choose>
                            <c:when test="${sessionScope.authUser.userRole == 'SUPER_ADMIN' && userAdminDto.userRole != 'SUPER_ADMIN'}">
                                <div class="d-flex align-items-center">
                                    <select class="form-select form-select-sm me-2" id="user_role">
                                        <c:forEach var="role" items="${roleList}">
                                            <option value="${role}" ${userAdminDto.userRole == role? 'selected' : ''}>${role.name()}</option>
                                        </c:forEach>
                                    </select>
                                    <button class="btn btn-sm btn-outline-secondary" type="button"
                                            style=" white-space: nowrap;" onclick="updateRole(${uno})">변경
                                    </button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                ${userAdminDto.userRole}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <th class="bg-body-tertiary">아이디</th>
                    <td>${userAdminDto.id}</td>
                    <th class="bg-body-tertiary">소셜 로그인</th>
                    <td>${userAdminDto.social? 'Y':'N'}</td>
                </tr>
                <tr>
                    <th class="bg-body-tertiary">계정 잠김</th>
                    <td colspan="3">
                        <c:choose>
                            <c:when test="${userAdminDto.nonlocked}">
                                <span>N</span>
                            </c:when>
                            <c:otherwise>
                                <span class="me-2">Y</span>
                                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="unlock()">잠금
                                    풀기
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <th class="bg-body-tertiary">로그인 실패 횟수</th>
                    <td>${userAdminDto.failures}</td>
                </tr>
                <tr>
                    <th class="bg-body-tertiary">가입일</th>
                    <td>${userAdminDto.formattedCreatedAt}</td>
                    <th class="bg-body-tertiary">방문 횟수</th>
                    <td>${userAdminDto.countVisit}</td>
                    <th class="bg-body-tertiary">최근 방문일</th>
                    <td>${userAdminDto.formattedLastVisitDate}</td>
                </tr>
                <tr>
                    <th class="bg-body-tertiary">게시글 수</th>
                    <td>${userAdminDto.countPost}</td>
                    <th class="bg-body-tertiary">댓글 수</th>
                    <td>${userAdminDto.countComment}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</section>

<!-- 작성 게시글 -->
<section class="mb-4">
    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center mb-2">
            <div class="d-flex align-items-center"><h5 class="mb-0 me-2 fw-bold">작성 게시글 </h5> <span
                    class="badge bg-secondary">총 ${userAdminDto.countPost}건</span></div>
            <c:if test="${userAdminDto.countPost > postPageSize}">
                <button class="btn btn-sm btn-outline-secondary" type="button"
                        onclick="window.open(contextPath + '/admin/posts?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                    더보기 &gt;
                </button>
                </button>
            </c:if>
        </div>
        <div class="card-body table-responsive">
            <table class="table table-hover table-sm">
                <colgroup>
                    <col style="width: 6%;"> <!-- 번호 -->
                    <col style=""> <!-- 제목 -->
                    <col style="width: 4%;"> <!-- 댓글수 -->
                    <col style="width: 6%;"> <!-- 조회수 -->
                    <col style="width: 14%;"> <!-- 작성일 -->
                    <col style="width: 12%;"> <!-- 상태 -->
                    <col style="width: 8%;"> <!-- 최근신고일 -->
                    <col style="width: 8%;"> <!-- 차단일 -->
                </colgroup>
                <thead class="thead-light">
                <tr>
                    <th>글번호</th>
                    <th>제목</th>
                    <th>댓글</th>
                    <th>조회수</th>
                    <th>작성일</th>
                    <th>상태</th>
                    <th>최근신고일</th>
                    <th>차단일</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${postDtoList == null or fn:length(postDtoList) == 0}">
                        <tr>
                            <td class="text-center" colspan="8">no data</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="postDto" items="${postDtoList}">
                            <tr style="cursor: pointer;"
                                onclick="window.open(contextPath + '/admin/posts/${postDto.pno}?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                                <td>${postDto.pno}</td>
                                <td>${postDto.title}</td>
                                <td>${postDto.cmtCnt}</td>
                                <td>${postDto.views}</td>
                                <td>${postDto.formattedCreatedAtTime}</td>
                                <td><c:choose>
                                    <c:when test="${postDto.contentState == 'REPORTED'}">
                                        ${postDto.contentStateKorNm} (${postDto.rpnoCnt}건)
                                    </c:when>
                                    <c:when test="${postDto.contentState == 'BLOCKED'}">
                                        ${postDto.contentStateKorNm}
                                    </c:when>
                                    <c:when test="${postDto.contentState == 'UNREPORTED_UNBLOCKED'}">
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose></td>
                                <td>${postDto.reportedAt == null ? '' : postDto.formattedReportedAt}</td>
                                <td>${postDto.blockedAt == null ? '' : postDto.formattedBlockedAt}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

                </tbody>
            </table>
        </div>
    </div>
</section>

<!-- 작성 댓글 -->
<section class="mb-4">
    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center mb-2">
            <div class="d-flex align-items-center"><h5 class="mb-0 me-2 fw-bold">작성 댓글</h5> <span
                    class="badge bg-secondary">총 ${userAdminDto.countComment}건</span></div>
            <c:if test="${userAdminDto.countComment > commentPageSize}">
                <button type="button" class="btn btn-sm btn-outline-secondary"
                        onclick="window.open(contextPath + '/admin/comments?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                    더보기 &gt;
                </button>
            </c:if>
        </div>
        <div class="card-body table-responsive">
            <table class="table table-hover table-sm">
                <colgroup>
                    <col style="width: 6%;"> <!-- 번호 -->
                    <col style="width: 22%;"> <!-- 내용 -->
                    <col style="width: 22%;"> <!-- 제목 -->
                    <col style="width: 6%;"> <!-- 번호 -->
                    <col style="width: 14%;"> <!-- 작성일 -->
                    <col style="width: 12%;"> <!-- 상태 -->
                    <col style="width: 8%;"> <!-- 최근신고일 -->
                    <col style="width: 8%;"> <!-- 차단일 -->
                </colgroup>
                <thead class="thead-light">
                <tr>
                    <th>댓글번호</th>
                    <th>내용</th>
                    <th>게시글 제목</th>
                    <th>글번호</th>
                    <th>작성일</th>
                    <th>상태</th>
                    <th>최근신고일</th>
                    <th>차단일</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${commentDtoList == null or fn:length(commentDtoList) == 0}">
                        <tr>
                            <td class="text-center" colspan="8">no data</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="commentDto" items="${commentDtoList}">
                            <tr style="cursor: pointer;"
                                onclick="window.open(contextPath + '/admin/posts/${commentDto.pno}?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                                <td>${commentDto.cno}</td>
                                <td>${commentDto.comment}</td>
                                <td>${commentDto.postTitle}</td>
                                <td>${commentDto.pno}</td>
                                <td>${commentDto.formattedCreatedAtTime}</td>
                                <td><c:choose>
                                    <c:when test="${commentDto.contentState == 'REPORTED'}">
                                        ${commentDto.contentStateKorNm} (${commentDto.rcnoCnt}건)
                                    </c:when>
                                    <c:when test="${commentDto.contentState == 'BLOCKED'}">
                                        ${commentDto.contentStateKorNm} (${commentDto.brText})
                                    </c:when>
                                    <c:when test="${commentDto.contentState == 'UNREPORTED_UNBLOCKED'}">
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose></td>
                                <td>${commentDto.formattedReportedAt}</td>
                                <td>${commentDto.formattedBlockedAt}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise></c:choose>
                </tbody>
            </table>
        </div>
    </div>
</section>

<%-- ----------- 구분선 --------------- --%>


<%--
<section class="mb-4">
    <input type="hidden" id="uno" value="${uno}"/>
    <input type="hidden" id="searchCondition" value="${sc.queryString}"/>

    &lt;%&ndash;  사용자 정보  &ndash;%&gt;
    <table>
        <tr>
            <th>사용자 번호</th>
            <td>${uno}</td>
            <th>권한</th>
            <td>
                <c:choose>
                    <c:when test="${sessionScope.authUser.userRole == 'SUPER_ADMIN' && userAdminDto.userRole != 'SUPER_ADMIN'}">
                        <select id="user_role" class="form-select">
                            <c:forEach var="role" items="${roleList}">
                                <option value="${role}" ${userAdminDto.userRole == role? 'selected' : ''}>${role.name()}</option>
                            </c:forEach>
                        </select>
                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="updateRole(${uno})">변경
                        </button>
                    </c:when>
                    <c:otherwise>
                        ${userAdminDto.userRole}
                    </c:otherwise>
                </c:choose>

            </td>
            <th>사용자 아이디</th>
            <td>${userAdminDto.id}</td>
        </tr>
        <tr>
            <th>사용자 이메일</th>
            <td colspan="3">${userAdminDto.email}</td>
            <th>소셜 로그인</th>
            <td>${userAdminDto.social? 'Y':'N'}</td>
        </tr>
        <tr>
            <th>계정 잠김</th>
            <td colspan="3">
                <c:choose>
                    <c:when test="${userAdminDto.nonlocked}">
                        <span>N</span>
                    </c:when>
                    <c:otherwise>
                        <span>Y</span>
                        <button type="button" onclick="unlock()">잠금 풀기</button>
                    </c:otherwise>
                </c:choose>
            </td>
            <th>로그인 실패 횟수</th>
            <td>${userAdminDto.failures}</td>
        </tr>
        <tr>
            <th>가입일</th>
            <td>${userAdminDto.formattedCreatedAt}</td>
            <th>방문 횟수</th>
            <td>${userAdminDto.countVisit}</td>
            <th>최근 방문일</th>
            <td>${userAdminDto.formattedLastVisitDate}</td>
        </tr>
        <tr>
            <th>게시글 수</th>
            <td>${userAdminDto.countPost}</td>
            <th>댓글 수</th>
            <td>${userAdminDto.countComment}</td>
        </tr>
    </table>
</section>

<section>
    <div>
        <span>작성 게시글</span>
        <span>총 ${userAdminDto.countPost}건</span>
        <c:if test="${userAdminDto.countPost > postPageSize}">
            <button type="button"
                    onclick="window.open(contextPath + '/admin/post?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                더보기 >
            </button>
        </c:if>
    </div>
    <table>
        <thead>
        <tr>
            <th>글번호</th>
            <th>제목</th>
            <th>댓글</th>
            <th>조회수</th>
            <th>작성일</th>
            <th>상태</th>
            <th>최근신고일</th>
            <th>차단일</th>
        </tr>
        </thead>
        <c:forEach var="postDto" items="${postDtoList}">
            <tr style="cursor: pointer;"
                onclick="window.open(contextPath + '/admin/post/read/${postDto.pno}?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                <td>${postDto.pno}</td>
                <td>${postDto.title}</td>
                <td>${postDto.cmtCnt}</td>
                <td>${postDto.views}</td>
                <td>${postDto.formattedCreatedAt}</td>
                <td><c:choose>
                    <c:when test="${postDto.contentState == 'REPORTED'}">
                        ${postDto.contentStateKorNm} (${postDto.rpnoCnt}건)
                    </c:when>
                    <c:when test="${postDto.contentState == 'BLOCKED'}">
                        ${postDto.contentStateKorNm} (${postDto.brText})
                    </c:when>
                    <c:when test="${postDto.contentState == 'UNREPORTED_UNBLOCKED'}">
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose></td>
                <td>${postDto.reportedAt == null ? '' : postDto.formattedReportedAt}</td>
                <td>${postDto.blockedAt == null ? '' : postDto.formattedBlockedAt}</td>
            </tr>
        </c:forEach>
    </table>
</section>

<section>
    <div>
        <span>작성 댓글</span>
        <span>총 ${userAdminDto.countComment}건</span>
        <c:if test="${userAdminDto.countComment > postCommentSize}">
            <button type="button"
                    onclick="window.open(contextPath + '/admin/comment?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                더보기 >
            </button>
        </c:if>
    </div>
    <table>
        <thead>
        <tr>
            <th>댓글번호</th>
            <th>내용</th>
            <th>게시글 제목</th>
            <th>게시글 번호</th>
            <th>작성일</th>
            <th>상태</th>
            <th>최근신고일</th>
            <th>차단일</th>
        </tr>
        </thead>
        <c:forEach var="commentDto" items="${commentDtoList}">
            <tr style="cursor: pointer;"
                onclick="window.open(contextPath + '/admin/post/read/${commentDto.pno}?page=1&option1=writerUno&keyword=${uno}', '_blank')">
                <td>${commentDto.cno}</td>
                <td>${commentDto.comment}</td>
                <td>${commentDto.postTitle}</td>
                <td>${commentDto.pno}</td>
                <td>${commentDto.formattedCreatedAt}</td>
                <td><c:choose>
                    <c:when test="${commentDto.contentState == 'REPORTED'}">
                        ${commentDto.contentStateKorNm} (${commentDto.rcnoCnt}건)
                    </c:when>
                    <c:when test="${commentDto.contentState == 'BLOCKED'}">
                        ${commentDto.contentStateKorNm} (${commentDto.brText})
                    </c:when>
                    <c:when test="${commentDto.contentState == 'UNREPORTED_UNBLOCKED'}">
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose></td>
                <td>${commentDto.formattedReportedAt}</td>
                <td>${commentDto.formattedBlockedAt}</td>
            </tr>
        </c:forEach>
    </table>
</section>--%>

<script>

    function updateRole(uno) {
        if ($('#user_role').val() == "${userRole}") {
            alert("동일한 권한입니다.");
            return;
        }

        if (!confirm("권한을 변경하시겠습니까?")) {
            return;
        }

        var data = JSON.stringify({
            uno: uno,
            userRole: $('#user_role').val()
        });

        $.ajax({
            url: contextPath + '/superAdmin/user-management/updateRole',
            method: 'POST',
            data: data,
            contentType: 'application/json',
            success: function (response) {
                alert("권한이 변경되었습니다.");
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
                location.reload();
            }

        });
    }

    function unlock() {
        if (!confirm("잠금을 해제하시겠습니까?")) return;

        $.ajax({
            url: contextPath + '/admin/user-management/' + $('#uno').val() + '/lock',
            method: 'DELETE',
            success: function (response) {
                alert("잠금이 해제되었습니다.");
                location.reload();
            }, error: function (response) {
                var errorMsg = response.responseJSON?.errorResponse?.message || defaultErrorMsg;
                alert(errorMsg);
            }

        });
    }

    function backToList() {
        var sc = $('#searchCondition').val();
        location.href = contextPath + "/admin/user-management" + sc;
    }
</script>

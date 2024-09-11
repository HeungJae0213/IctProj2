<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/template/Config.jsp" />
<title>게시글 상세</title>
<style>
	th.bg-dark.text-white {
		text-align: center;
	}
</style>
</head>
<body>
	<div class="container">
    <div class="container-fluid">
        <jsp:include page="/template/Header.jsp" />
        <!-- 컨텐츠 시작 -->
        <div class="p-5 gradient-bg text-black">
            <h1>
                게시글 상세 <small>페이지</small>
            </h1>
        </div>
		<!-- 내용 시작 -->
	    <div class="container my-3">
	    <c:if test="${record != null}">
	        <div class="card">
	            <div class="card-header">
	                <h3 class="card-title">${record.title}</h3>
	                <div class="d-flex justify-content-between align-items-center mt-2">
	                    <div>
	                        <span class="fw-bold">${record.username}</span>
	                        <span class="text-muted ms-2">${record.postDate}</span>
	                    </div>
	                </div>
	            </div>
	            <div class="card-body">
	                <p class="card-text">${record.content}</p>
	                <c:if test="${record.files != null}">
	                <div class="mt-4 d-flex align-items-center">
	                    <i class="fa-solid fa-file"></i>
	                    <c:forEach var="file" items="${fn:split(record.files,',') }">
	                    <a href="<c:url value="/Bbs/download.ict?filename=${file}&id=${record.id }"/>">
	                    <p class="card-text ms-2">${file}</p>
	                    </a>
	                    </c:forEach>
	                </div>
	                </c:if>
	            </div>
	            <div class="card-footer d-flex justify-content-between">
	                <div>
	                    <i class="fa-solid fa-eye me-1"></i>
	                    <span>조회수</span>
	                    <span class="ms-1">${record.hitcount}</span>
	                </div>
	            </div>
	        </div>
	         </c:if>
	    </div>
	    <hr>
		<!-- 내용 끝 -->
		<!-- 이전글/다음글 -->
        <div class="mt-3">
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <td class="text-white bg-dark w-25 text-center">다음글</td>
                        <td>
                            <c:choose>
                                <c:when test="${nextPost != null}">
                                    <a href="<c:url value='/Bbs/view.ict?id=${nextPost.id}' />">${nextPost.title}</a>
                                </c:when>
                                <c:otherwise>
                                    다음글이 없습니다
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-white bg-dark w-25 text-center">이전글</td>
                        <td>
                            <c:choose>
                                <c:when test="${prevPost != null}">
                                    <a href="<c:url value='/Bbs/view.ict?id=${prevPost.id}' />">${prevPost.title}</a>
                                </c:when>
                                <c:otherwise>
                                    이전글이 없습니다
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- 수정/삭제/목록 컨트롤 버튼 -->
        <div class="text-center">
            <!-- 토큰기반 -->
            <c:if test="${not empty sessionScope.user && sessionScope.user.name == record.username}">
			<a href="<c:url value='/Bbs/edit.ict?id=${record.id}'/>" class="btn btn-success">수정</a>
            <a href="javascript:isDelete()" class="btn btn-success" id="delete">삭제</a>
			</c:if>
            <a href="list.ict" class="btn btn-success">목록</a>
        </div>

        <!-- 컨텐츠 끝 -->
        <jsp:include page="/template/Footer.jsp" />
    </div>
    <!-- container-fluid -->
</div>
<!-- container -->
</body>
<script>
	var urlParams = new URLSearchParams(window.location.search);
	var recordId = urlParams.get('id');
	document.querySelector('#delete').onclick = function() {
		if(confirm("정말로 삭제하시겠습니까?")){
			location.replace("/ChoHeungJaeProj2/Bbs/deleteBbs.ict?id="+recordId);
		}
	}
</script>
</html>

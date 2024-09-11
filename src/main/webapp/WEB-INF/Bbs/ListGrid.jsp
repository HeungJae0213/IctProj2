<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/template/Config.jsp" />
<title>게시판</title>
</head>
<style>
    .thumbnail-img {
        width: 100%;
        height: 200px;
        object-fit: cover;
    }
</style>
<body>
	<div class="container">
		<div class="container-fluid">
			<jsp:include page="/template/Header.jsp" />

			<!-- 컨텐츠 시작 -->
			<div class="p-5 gradient-bg text-black">
				<h1>
					게시판 <small>목록</small>
				</h1>
			</div>
			<div class="d-flex justify-content-end">
				<div class="btn-group btn-group-lg my-2">
					<a href="listGrid.ict" class="btn btn-dark btn-sm">
			        	<i class="fa-solid fa-border-all"></i>
			        </a>
			        <a href="list.ict" class="btn btn-light btn-sm">
			        	<i class="fa-solid fa-rectangle-list"></i>
			        </a>
				</div>
			</div>
			<div class="row">
				<c:if test="${empty records}" var="isEmpty">
				<table class="table table-hover text-center">
					<tbody class="down-file-body">
						<tr>
							<td colspan="5">등록된 자료가 없습니다.</td>
						</tr>
					</tbody>
				</table>
				</c:if>
				<c:if test="${!isEmpty}">
			    	<c:forEach var="record" items="${records}" varStatus="loop">
				        <div class="col-md-3 mb-3">
				            <div class="card" style="max-width: 18rem; position: relative;">
				                <a href="<c:url value="/Bbs/view.ict?place=grid&id=${record.id }"/>" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; z-index: 1;"></a>
				                <c:choose>
			                        <c:when test="${record.thumbnail == null || record.thumbnail.isEmpty()}">
			                            <img class="card-img-top thumbnail-img" style="" src="<c:url value='/images/defaultthumbnail.png'/>" alt="Default image">
			                        </c:when>
			                        <c:otherwise>
			                            <img class="card-img-top thumbnail-img" src="<c:url value='/uploadthumb/${record.thumbnail}'/>" alt="Card image">
			                        </c:otherwise>
			                    </c:choose>
				                <div class="card-body" style="position: relative; z-index: 2;">
				                    <h5 class="card-title">${record.title}&nbsp;
				                    <c:if test="${not empty record.files}">
									<i class="fa-solid fa-link"></i>
									</c:if>
									</h5>
				                    <p class="card-text">작성자: ${record.username}</p>
				                    <p class="card-text">글쓴 날짜: ${record.postDate}</p>
				                    <div class="d-flex justify-content-between">
						                <p class="card-text text-end">조회수: ${record.hitcount}</p>
						            </div>
				                </div>
				            </div>
				        </div>
			    	</c:forEach>
			    </c:if>
			</div>
			<c:if test="${not empty sessionScope.user}">
				<div class="text-end">
					<a href="<c:url value='/Bbs/write.ict?id=${sessionScope.user.id}'/>" class="btn btn-secondary"><i class="fa-solid fa-pencil"></i>&nbsp;글쓰기</a>
				</div>
			</c:if>
			<!-- 페이징 출력 -->
			<div class="text-center">
			${paging}
			</div>
			<hr>
			<!-- 컨텐츠 끝 -->
			<jsp:include page="/template/Footer.jsp" />
		</div>
		<!-- container-fluid -->
	</div>
	<!--container  -->
</body>
</html>

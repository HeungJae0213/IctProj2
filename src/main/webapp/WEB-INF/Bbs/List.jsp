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
			<div class="btn-group btn-group-lg my-2 float-end">
				<a href="listGrid.ict" class="btn btn-light btn-sm">
			        <i class="fa-solid fa-border-all"></i>
			    </a>
			    <a href="list.ict" class="btn btn-dark btn-sm">
			        <i class="fa-solid fa-rectangle-list"></i>
			    </a>
			</div>
			<table class="table table-hover text-center">
				<thead class="table-dark">
					<tr>
						<th class="col-1">번호</th>
						<th class="col-auto">제목</th>
						<th class="col-2">작성자</th>
						<th class="col-2">작성일</th>
						<th class="col-1">조회수</th>
					</tr>
				</thead>
				<tbody class="down-file-body">
					<c:if test="${empty records}" var="isEmpty">
						<tr>
							<td colspan="5">등록된 자료가 없습니다.</td>
						</tr>
					</c:if>
					<c:if test="${!isEmpty}">
						<c:forEach var="record" items="${records}" varStatus="loop">
							<tr>
								<td>${record.id }</td>
								<td class="text-start">
								<a href="<c:url value="/Bbs/view.ict?place=list&id=${record.id }"/>">${record.title}</a>
								<c:if test="${not empty record.files}">
									<i class="fa-solid fa-link"></i>
								</c:if>
								</td>
								<td>${record.username}</td>
								<td>${record.postDate}</td>
								<td>${record.hitcount}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
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

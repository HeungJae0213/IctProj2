<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/template/Config.jsp" />
<title>회원목록</title>

</head>
<body>
	<div class="container">
		<div class="container-fluid">
			<jsp:include page="/template/Header.jsp" />

			<!-- 컨텐츠 시작 -->
			<div class="p-5 gradient-bg text-black">
				<h1>
					회원목록 <small>페이지</small>
				</h1>
			</div>
			<table class="table table-hover text-center">
				<thead class="table-dark">
					<tr>
						<th class="col-1">번호</th>
						<th class="col-1">닉네임</th>
						<th class="col-1">성별</th>
						<th class="col-auto">관심사항</th>
						<th class="col-1">학력</th>
						<th class="col-2">핸드폰번호</th>
						<th class="col-auto">자기소개</th>
						<th class="col-2">가입일</th>
					</tr>
				</thead>
				<tbody class="down-file-body">
					<c:if test="${empty records }" var="isEmpty">
						<tr>
							<td colspan="6">등록된 자료가 없습니다.</td>
						</tr>
					</c:if>
					<c:if test="${! isEmpty }">
						<c:forEach var="record" items="${records }">
							<tr>
								<td>${record.id }</td>
								<td>${record.username}</td>
								<td>${record.gender}</td>
								<td>${record.interest}</td>
								<td>${record.grade}</td>
								<td>${record.phone}</td>
								<td>${record.intro}</td>
								<td>${record.regDate}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
			<!-- 페이징 출력 -->
			${paging }		
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

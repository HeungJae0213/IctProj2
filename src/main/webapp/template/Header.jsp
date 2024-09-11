<%@page import="model.JWTokens"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<!-- 상단 네비게이션 바 -->
	<style>
	    .gradient-bg {
	        background: linear-gradient(270deg, yellow, skyblue);
	        background-size: 400% 400%;
	        animation: gradientAnimation 10s ease infinite;
	    }
	
	    @keyframes gradientAnimation {
	        0% { background-position: 0% 50%; }
	        50% { background-position: 100% 50%; }
	        100% { background-position: 0% 50%; }
	    }
	    
	   
	</style>
	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
	<script src="https://kit.fontawesome.com/9657bc028f.js" crossorigin="anonymous"></script>
		<div class="container-fluid">
			<a class="navbar-brand" href="list.ict"><i class="fa-solid fa-house-chimney"></i></a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse justify-content-end" id="collapsibleNavbar">
				<ul class="navbar-nav ml-auto">
				<c:choose>
	                <c:when test="${not empty sessionScope.user}">
	                    <li class="nav-item"><a class="nav-link" href="<c:url value='/Bbs/logout.ict'/>"><i class="fas fa-sign-out-alt"></i> 로그아웃</a></li>
	                    <li class="nav-item"><a class="nav-link" href="<c:url value='/Bbs/userinfo.ict?id=${sessionScope.user.id}'/>"><i class="fa-solid fa-user"></i> 회원정보</a></li>
	                </c:when>
	                <c:otherwise>
	                    <li class="nav-item"><a class="nav-link" href="<c:url value='/Bbs/login.ict'/>"><i class="fas fa-sign-in-alt"></i> 로그인</a></li>
	                    <li class="nav-item"><a class="nav-link" href="<c:url value='/Bbs/register.ict'/>"><i class="fa-solid fa-address-card"></i> 회원가입</a></li>
	                </c:otherwise>
	            </c:choose>
					<li class="nav-item"><a class="nav-link" href="userlist.ict"><i class="fa-solid fa-user"></i> 회원목록</a></li>
					<li class="nav-item"><a class="nav-link" href="list.ict"><i class="fa-solid fa-list"></i> 게시판</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<!-- 상단 네비게이션바 끝 -->
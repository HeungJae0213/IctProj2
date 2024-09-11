<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/template/Config.jsp"%>
<title>로그인</title>

</head>
<body>
	<div class="container">
        <div class="container-fluid">
            <%@ include file="/template/Header.jsp"%>
            <!-- 컨텐츠 시작 -->
            <div class="p-5 gradient-bg text-black">
                <h1>
                    로그인 <small>페이지</small>
                </h1>
            </div>
            <fieldset class="border rounded-3 p-3">
                <legend class="float-none w-auto px-3">로그인</legend>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible my-2 col-11" >
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    <strong>로그인 실패!</strong>&nbsp;&nbsp;${errorMsg}
                    </div>
                </c:if>
                <form method="post" action="<c:url value='/Bbs/login.ict'/>">
                    <div class="row">
                        <div class="col-5">
                            <input type="text" class="form-control" placeholder="아이디를 입력하세요."
                                name="username" value="">
                        </div>
                        <div class="col-5">
                            <input type="password" class="form-control"
                                placeholder="비밀번호를 입력하세요." name="password" value="">
                        </div>
                        <div class="col-1">
                            <button type="submit" class="btn btn-success">확인</button>
                        </div>
                    </div>
                </form>
            </fieldset>
            <!-- 컨텐츠 끝 -->
            <%@ include file="/template/Footer.jsp"%>
        </div>
        <!-- container-fluid -->
    </div>
    <!--container  -->
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/template/Config.jsp" />
    <title>회원가입</title>
    <style>
    .star {
            color: rgb(255, 0, 0);
            font-size: 20px;
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
					회원가입 <small>페이지</small>
				</h1>
				<div class="information"><span class="star">*</span>표시는 무조건 입력하거나 선택해야합니다.</div>
			</div>
			<c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible my-2 col-11" >
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    <strong>회원가입 실패!</strong>&nbsp;&nbsp;${errorMsg}
                    </div>
                </c:if>
	        <form id="registerForm" action="<c:url value='/Bbs/register.ict'/>" method="post">
	            <div class="mb-3 mt-3">
	                <label for="username" class="form-label"><i class="fa-solid fa-id-card"></i>&nbsp;<span class="star">*</span>아이디</label>
	                <input 
	                	type="text" class="form-control" id="username"
	                	placeholder="아이디를 입력하세요" name="username">
	            </div>
	            <div class="mb-3">
					<label for="password" class="form-label"><i class="fa-solid fa-lock"></i>&nbsp;<span class="star">*</span>비밀번호</label>
					<div class="input-group"> 
						<input type="password" class="form-control" id="password" placeholder="비밀번호를 입력하세요" name="password">
						<span class="input-group-text toggle-password"><i class="fa-regular fa-eye-slash"></i></span>
					</div>
				</div>
				<div class="mb-3">
					<label for="name" class="form-label"><i class="fa-solid fa-signature"></i>&nbsp;<span class="star">*</span>성명</label> 
					<input
						type="text" class="form-control" id="name"
						placeholder="이름을 입력하세요" name="name" >
				</div>
				<div class="mb-3">
				    <label for="phone" class="form-label"><i class="fa-solid fa-phone"></i>&nbsp;<span class="star">*</span>핸드폰번호</label>
				    <div class="d-flex">
				        <input
				            type="text" class="form-control me-2" id="phone-part1"
				            name="phone-part1" maxlength="3">
				        <span class="me-2">-</span>
				        <input
				            type="text" class="form-control me-2" id="phone-part2"
				            name="phone-part2" maxlength="4">
				        <span class="me-2">-</span>
				        <input
				            type="text" class="form-control" id="phone-part3"
				            name="phone-part3" maxlength="4">
				    </div>
				</div>
				<div class="mb-3">
					<label class="form-label"><i class="fa-solid fa-restroom"></i>&nbsp;<span class="star">*</span>성별</label>
					<div class="d-flex">
						<div class="form-check">
							<input type="radio" class="form-check-input" id="radio1"
								name="gender" value="남자"> <label
								class="form-check-label" for="radio1">남자</label>
						</div>
						<div class="form-check mx-3">
							<input type="radio" class="form-check-input" id="radio2"
								name="gender" value="여자"> <label
								class="form-check-label" for="radio2" >여자</label>
						</div>
						<div class="form-check mx-3">
							<input type="radio" class="form-check-input" id="radio3"
								name="gender" value="트랜스젠더"> <label
								class="form-check-label" for="radio3" >트랜스젠더</label>
						</div>
					</div>
				</div>
				<div class="my-3">
					<label class="form-label"><i class="fa-regular fa-circle-question"></i>&nbsp;<span class="star">*</span>관심사항</label>
					<div class="d-flex">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" id="check1"
								name="inter" value="정치"> <label
								class="form-check-label" for="check1">정치</label>
						</div>
						<div class="form-check mx-3">
							<input type="checkbox" class="form-check-input" id="check2"
								name="inter" value="경제"> <label
								class="form-check-label" for="check2">경제</label>
						</div>
						<div class="form-check">
							<input type="checkbox" class="form-check-input" id="check3"
								name="inter" value="스포츠"> <label
								class="form-check-label" for="check3">스포츠</label>
						</div>
						<div class="form-check mx-3">
							<input type="checkbox" class="form-check-input" id="check4"
								name="inter" value="연예"> <label
								class="form-check-label" for="check4">연예</label>
						</div>
					</div>
				</div>
				<div class="mb-3">
					<label for="sel" class="form-label"><i class="fa-solid fa-graduation-cap"></i>&nbsp;<span class="star">*</span>학력사항</label> 
					<select
						class="form-select" id="sel" name="grade">
						<option value="초졸">초등학교 졸업</option>
						<option value="중졸">중학교 졸업</option>
						<option value="고졸">고등학교 졸업</option>
						<option value="대졸">대학교 졸업</option>
					</select>
				</div>
				<div class="mb-3">
				<label for="comment"><i class="fa-solid fa-user-tie"></i>&nbsp;<span class="star">*</span>자기소개</label>
					<textarea class="form-control" rows="5" id="self" name="self"></textarea>
				</div>
	            <button type="submit" class="btn btn-primary">가입</button>
	        </form>
        </div>
    </div>
    <script>
        document.querySelectorAll('.toggle-password').forEach(item => {
            item.addEventListener('click', function () {
                const icon = this.querySelector('i');
                const passwordField = this.previousElementSibling;
                const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordField.setAttribute('type', type);
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            });
        });
        
        document.getElementById('registerForm').addEventListener('submit', function(event) {
            let valid = true;
            let allEmpty = true;

            const fields = ['username', 'password', 'name', 'phone-part1', 'phone-part2', 'phone-part3', 'gender', 'inter', 'grade', 'self'];
            fields.forEach(function(field) {
                const elements = document.getElementsByName(field);
                elements.forEach(function(element) {
                    if ((element.type === 'checkbox' || element.type === 'radio') && element.checked) {
                        allEmpty = false;
                    } else if (element.type !== 'checkbox' && element.type !== 'radio' && element.value.trim() !== '') {
                        allEmpty = false;
                    }
                });
            });

            if (allEmpty) {
                alert('모든 필드를 입력하세요.');
                valid = false;
            } else {
                const username = document.getElementById('username').value;
                if (username === '') {
                    alert('아이디를 입력하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const password = document.getElementById('password').value;
                if (password.length < 6) {
                    alert('비밀번호는 6자리 이상이어야 합니다.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const name = document.getElementById('name').value;
                if (name === '') {
                    alert('성명을 입력하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const phonePart1 = document.getElementById('phone-part1').value;
                const phonePart2 = document.getElementById('phone-part2').value;
                const phonePart3 = document.getElementById('phone-part3').value;
                const phoneRegex = /^[0-9]{3,4}$/;
                if (!phoneRegex.test(phonePart1) || !phoneRegex.test(phonePart2) || !phoneRegex.test(phonePart3)) {
                    alert('유효한 전화번호를 입력하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const gender = document.querySelector('input[name="gender"]:checked');
                if (!gender) {
                    alert('성별을 선택하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const interest = document.querySelectorAll('input[name="inter"]:checked');
                if (interest.length === 0) {
                    alert('관심사항을 선택하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const grade = document.getElementById('sel').value;
                if (grade === '') {
                    alert('학력사항을 선택하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }

                const self = document.getElementById('self').value;
                if (self === '') {
                    alert('자기소개를 입력하세요.');
                    valid = false;
                    event.preventDefault();
                    return;
                }
            }

            if (!valid) {
                event.preventDefault();
            }
        });
    </script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/template/Config.jsp" />
<title>게시글 쓰기</title>
<script>
    function validateForm() {
        // 제목과 내용 유효성 검사
        var title = document.getElementById("title").value.trim();
        var content = document.getElementById("content").value.trim();
        
        if (title === "") {
            alert("제목을 입력하세요.");
            document.getElementById("title").focus();
            return false;
        }

        if (content === "") {
            alert("내용을 입력하세요.");
            document.getElementById("content").focus();
            return false;
        }

        // 썸네일 파일 유효성 검사
        var thumbnail = document.getElementById("thumbnail").files[0];
        var validFileExtensions = [".jpg", ".jpeg", ".png", ".gif"];
        var maxThumbnailSize = 1024 * 1024 * 10;

        if (thumbnail) {
            if (!hasValidExtension(thumbnail.name, validFileExtensions)) {
                alert("유효하지 않은 썸네일 파일 형식입니다. 허용된 파일 형식: " + validFileExtensions.join(", "));
                document.getElementById("thumbnail").focus();
                return false;
            }
            if (thumbnail.size > maxThumbnailSize) {
                alert("썸네일 파일의 크기는 10MB를 초과할 수 없습니다.");
                document.getElementById("thumbnail").focus();
                return false;
            }
        }
        
     	// 자료파일 크기 유효성 검사
        var files = document.getElementById("files").files;
        var maxFileSize = 1024 * 1024 * 10;

        if (files.length > 0) {
            for (var i = 0; i < files.length; i++) {
                if (files[i].size > maxFileSize) {
                    alert("자료파일의 크기는 각각 10MB를 초과할 수 없습니다.");
                    document.getElementById("files").focus();
                    return false;
                }
            }
        }

        return true;
    }

    function hasValidExtension(fileName, validExtensions) {
        var fileExtension = fileName.substr(fileName.lastIndexOf('.')).toLowerCase();
        return validExtensions.indexOf(fileExtension) > -1;
    }
</script>
</head>
<body>
	<div class="container">
		<div class="container-fluid">
			<jsp:include page="/template/Header.jsp" />

			<!-- 컨텐츠 시작 -->
			<div class="p-5 gradient-bg text-black">
				<h1>게시글 등록 <small>페이지</small></h1>
			</div>
		    <c:if test="${not empty errorMsg}">
                   <div class="alert alert-danger alert-dismissible my-2 col-11" >
                   <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                   <strong>등록 실패!</strong>&nbsp;&nbsp;${errorMsg}
                   </div>
            </c:if>
		    <form action="<c:url value='/Bbs/write.ict'/>" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
		    	<input type="hidden" name="id" value="${id}"/>
		        <div class="mb-3 mt-3">
		            <label for="title" class="form-label">제목</label>
		            <input type="text" class="form-control" id="title" placeholder="제목을 입력하세요" name="title">
		        </div>
		        <div class="mb-3">
		            <label for="content" class="form-label">내용</label>
		            <textarea placeholder="내용을 입력하세요" class="form-control" rows="5" id="content" name="content"></textarea>
		        </div>
		        <div class="mb-3">
		            <label for="files" class="form-label">자료파일</label>
		            <input type="file" class="form-control" id="files" multiple name="files">
		        </div>
		        <div class="mb-3">
		            <label for="thumbnail" class="form-label">썸네일 사진</label>
		            <input type="file" class="form-control" id="thumbnail" name="thumbnail" accept=".jpg, .jpeg, .png, .gif">
		        </div>
		        <button type="submit" class="btn btn-primary mt-2">등록</button>
		    </form>
 			
			<!-- 컨텐츠 끝 -->
			<jsp:include page="/template/Footer.jsp" />
		</div>
		<!-- container-fluid -->
	</div>
	<!--container  -->
</body>
</html>

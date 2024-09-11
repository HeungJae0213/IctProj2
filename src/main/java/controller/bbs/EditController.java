package controller.bbs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.FileUtils;
import model.JWTokens;
import model.bbs.BbsDao;
import model.bbs.BbsDto;


@WebServlet(urlPatterns = "/Bbs/edit.ict", initParams = {
		@WebInitParam(name = "MAX_FILE_SIZE", value = "10485760"), // 10MB
		@WebInitParam(name = "MAX_REQUEST_SIZE", value = "104857600") // 100MB
})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 100) // 100MB
public class EditController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		BbsDao dao = new BbsDao(getServletContext());
		BbsDto dto = dao.findById(id);
		dao.close();
		req.setAttribute("record", dto);
		req.getRequestDispatcher("/WEB-INF/Bbs/Edit.jsp?id=" + id).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		int updateFlag;
		try {
			// 필수 입력 값 처리
			String title = req.getParameter("title");
			String content = req.getParameter("content");
			String name = req.getParameter("name");
			// 제목과 내용이 비어있는지 체크
			if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
				throw new IllegalArgumentException("제목과 내용은 필수 입력 항목입니다.");
			}

			// 파일을 저장할 서버의 물리적 경로 설정
			String saveDirectory = req.getServletContext().getRealPath("/upload");
			String thumbnailDirectory = req.getServletContext().getRealPath("/uploadthumb");
			File uploadDirFile = new File(saveDirectory);
			File uploadThumbDirFile = new File(thumbnailDirectory);
			if (!uploadDirFile.exists()) {
				uploadDirFile.mkdirs(); // 디렉토리가 존재하지 않으면 생성
			}
			if (!uploadThumbDirFile.exists()) {
				uploadThumbDirFile.mkdirs(); // 디렉토리가 존재하지 않으면 생성
			}

			// 기존 게시물 가져오기
			BbsDao dao = new BbsDao(getServletContext());
			BbsDto existingDto = dao.findById(id);

			// 썸네일 파일 처리
			String thumbnailFileName = "";
			Part thumbnailPart = req.getPart("thumbnail");
			if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
				if (existingDto != null && existingDto.getThumbnail() != null) {
					// 기존 썸네일 파일 삭제
					File existingThumbnail = new File(thumbnailDirectory + File.separator + existingDto.getThumbnail());
					if (existingThumbnail.exists()) {
						existingThumbnail.delete();
					}
				}
				thumbnailFileName = FileUtils.uploadThumbnail(thumbnailPart, thumbnailDirectory);
			} else {
				// 기존 썸네일 파일 유지
				thumbnailFileName = existingDto != null ? existingDto.getThumbnail() : "";
			}

			// 첨부 파일 처리
			StringBuffer buffer = FileUtils.upload(req.getParts(), saveDirectory);
			if (buffer == null || buffer.length() == 0) {
				// 새로운 첨부 파일이 없을 경우 기존 파일 유지
				buffer = new StringBuffer(existingDto != null ? existingDto.getFiles() : "");
			} else {
				// 새로운 첨부 파일이 있을 경우 기존 파일 삭제
				if (existingDto != null && existingDto.getFiles() != null) {
					FileUtils.delete(new StringBuffer(existingDto.getFiles()), saveDirectory, ",");
				}
			}

			// BbsDto에 데이터 설정
			BbsDto dto = new BbsDto();
			dto.setId(Integer.parseInt(id));
			dto.setUsername(name);
			dto.setTitle(title);
			dto.setContent(content);
			dto.setFiles(buffer.length() > 0 ? buffer.toString() : null);
			dto.setThumbnail(thumbnailFileName);

			// 게시글 DB에 업데이트
			updateFlag = dao.update(dto);

			// DB와 파일 처리 완료 후
			dao.close();

			// 실패 시 파일 삭제
			if (updateFlag == 0) {
				if (buffer.length() > 0) {
					FileUtils.delete(buffer, saveDirectory, ",");
				}
			}

			// 처리 완료 후 페이지 이동 또는 메시지 표시
			req.setAttribute("message", updateFlag);
			req.getRequestDispatcher("/Bbs/view.ict?id=" + id).forward(req, resp);
		} catch (Exception e) {
			updateFlag = -1;
			e.printStackTrace();
			req.setAttribute("errorMsg", "게시글 수정 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/Bbs/edit.ict").forward(req, resp);
		}
	}
}

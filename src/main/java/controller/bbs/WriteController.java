package controller.bbs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.bbs.BbsDao;
import model.bbs.BbsDto;
import model.users.UsersDao;
import model.users.UsersDto;
import model.FileUtils;
import model.JWTokens;

@WebServlet("/Bbs/write.ict")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,
		maxFileSize = 1024 * 1024 * 100,      
		maxRequestSize = 1024 * 1024 * 500   
		)
public class WriteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (id == null || id.isEmpty()) {
			System.out.println("아이디가 비었음");
			req.getRequestDispatcher("/Bbs/list.ict").forward(req, resp);
			return;
		}else {
			req.setAttribute("id", id);
			req.getRequestDispatcher("/WEB-INF/Bbs/Write.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		int insertFlag;
		try {
			// 필수 입력 값 처리
			String title = req.getParameter("title");
			String content = req.getParameter("content");

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

			// 썸네일 파일 처리
			String thumbnailFileName = "";
			Part thumbnailPart = req.getPart("thumbnail");
			thumbnailFileName = FileUtils.uploadThumbnail(thumbnailPart, thumbnailDirectory);

			//첨부 파일 처리
			//파일 업로드 로직 호출
			StringBuffer buffer= FileUtils.upload(req.getParts(), saveDirectory);
			if(buffer==null) {//파일 미 첨부시
				// BbsDto에 데이터 설정
				BbsDto dto = new BbsDto();
				UsersDao userDao = new UsersDao(getServletContext());
				UsersDto user = userDao.findByUsername(id);
				dto.setUsername(user.getName());
				dto.setTitle(title);
				dto.setContent(content);
				dto.setThumbnail(thumbnailFileName);

				// 게시글 DB에 입력
				BbsDao dao = new BbsDao(getServletContext());
				insertFlag = dao.insert(dto);

				// DB와 파일 처리 완료 후
				dao.close();
				userDao.close();
			}else {
				// BbsDto에 데이터 설정
				BbsDto dto = new BbsDto();
				UsersDao userDao = new UsersDao(getServletContext());
				UsersDto user = userDao.findByUsername(id);
				dto.setUsername(user.getName());
				dto.setTitle(title);
				dto.setContent(content);
				dto.setFiles(buffer.length() > 0 ? buffer.toString() : null);
				dto.setThumbnail(thumbnailFileName);

				// 게시글 DB에 입력
				BbsDao dao = new BbsDao(getServletContext());
				insertFlag = dao.insert(dto);

				// DB와 파일 처리 완료 후
				dao.close();
				userDao.close();

				// 실패 시 파일 삭제
				if (insertFlag == 0) {
					if (buffer.length() > 0) {
						FileUtils.delete(buffer, saveDirectory, ",");
					}
				}
			}

			// 처리 완료 후 페이지 이동 또는 메시지 표시
			req.getRequestDispatcher("/Bbs/list.ict").forward(req, resp);
		} catch (Exception e) {
			insertFlag = -1;
			e.printStackTrace();
			req.setAttribute("errorMsg", "게시글 등록 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/WEB-INF/Bbs/Write.jsp").forward(req, resp);
		}
	}



}

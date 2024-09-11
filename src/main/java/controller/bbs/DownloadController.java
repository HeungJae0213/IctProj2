package controller.bbs;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FileUtils;
import model.bbs.BbsDao;

@WebServlet("/Bbs/download.ict")
public class DownloadController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//파라미터 받기
		String id = req.getParameter("id");
		String filename = req.getParameter("filename");
		//다운로드와 관련된 모델 호출]
		//1.파일 다운로드 로직 호출
		FileUtils.download(filename, "/upload", req, resp);
		BbsDao dao = new BbsDao(getServletContext());
		dao.close();
	}///////////////
}

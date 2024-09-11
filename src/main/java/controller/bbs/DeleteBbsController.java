package controller.bbs;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bbs.BbsDao;
import model.bbs.BbsDto;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet("/Bbs/deleteBbs.ict")
public class DeleteBbsController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 // 파라미터 받기
        String id = req.getParameter("id");

        // 모델 호출 및 결과값 받기
        BbsDao dao = new BbsDao(getServletContext());
        BbsDto dto = dao.findById(id);
        if (dto == null) {
            System.out.println("게시글이 없습니다.");
            req.getRequestDispatcher("/Bbs/list.ict").forward(req, resp);
            return;
        }

        System.out.println(dto.getId());

        if (id.equals(String.valueOf(dto.getId()))) {
            int deleteFlag = dao.delete(dto);
            System.out.println("삭제 성공");
            dao.close();
            req.getRequestDispatcher("/Bbs/list.ict").forward(req, resp);
        } else {
            System.out.println("삭제 실패");
            req.getRequestDispatcher("/Bbs/list.ict").forward(req, resp);
        }
	}
}/////////////////


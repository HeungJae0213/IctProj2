package controller.bbs;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet("/Bbs/delete.ict")
public class DeleteController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 // 파라미터 받기
        String id = req.getParameter("id");

        // 모델 호출 및 결과값 받기
        UsersDao dao = new UsersDao(getServletContext());
        UsersDto dto = dao.findById(id);
        if (dto == null) {
            System.out.println("User not found");
            req.getRequestDispatcher("/WEB-INF/Bbs/List.jsp").forward(req, resp);
            return;
        }

        System.out.println(dto.getId());

        // [비밀번호가 일치하는 경우] - 비밀번호 확인이 필요하면 이 부분을 구현
        if (id.equals(String.valueOf(dto.getId()))) {
            int deleteFlag = dao.delete(dto);
            System.out.println("삭제 성공");
            dao.close();
            req.getRequestDispatcher("/Bbs/logout.ict").forward(req, resp);
        } else {
            System.out.println("삭제 실패");
            req.getRequestDispatcher("/WEB-INF/Bbs/List.jsp").forward(req, resp);
        }
	}
}/////////////////


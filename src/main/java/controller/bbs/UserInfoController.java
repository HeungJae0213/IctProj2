package controller.bbs;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet(urlPatterns = "/Bbs/userinfo.ict")
public class UserInfoController extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//3]요청분석
		String id = req.getParameter("id");
		//4]모델 호출 및 결과값 받기
		UsersDao dao= new UsersDao(getServletContext());
		UsersDto dto= dao.findById(id);
		dao.close();
		//5]필요한 값 리퀘스트 영역에 저장
		req.setAttribute("record", dto);
		//마]결과값을 뿌려줄 뷰(JSP페이지) 선택후 포워딩 
		//뷰선택 & 포워딩]
		req.getRequestDispatcher("/WEB-INF/Bbs/UserInfo.jsp").forward(req, resp);
	}
}

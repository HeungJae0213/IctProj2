package controller.bbs;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet("/Bbs/registeredit.ict")
public class RegisterEditController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("POST")) {
        	String id = req.getParameter("id");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String phone = req.getParameter("phone-part1") + "-" + req.getParameter("phone-part2") + "-" + req.getParameter("phone-part3");
            String gender = req.getParameter("gender");
            String[] interests = req.getParameterValues("inter");
            String interest = String.join(", ", interests);
            String grade = req.getParameter("grade");
            String intro = req.getParameter("self");
            String date = req.getParameter("date");
            UsersDao dao = new UsersDao(getServletContext());
            UsersDto item = new UsersDto();
            item.setId(Long.parseLong(id));
            item.setUsername(username);
            item.setPassword(password);
            item.setName(name);
            item.setPhone(phone);
            item.setGender(gender);
            item.setInterest(interest);
            item.setGrade(grade);
            item.setIntro(intro);
            item.setRegDate(Date.valueOf(date));

            int affected = dao.update(item);
            System.out.println("수정 성공");
            dao.close();

            if (affected > 0) {
                // 수정 완료 시 게시판 페이지로 이동
            	resp.sendRedirect(req.getContextPath() + "/Bbs/list.ict");
            } else {
                // 수정 실패 시 현재 페이지로 다시 요청
                req.setAttribute("errorMsg", "수정에 실패했습니다.");
                req.getRequestDispatcher("/WEB-INF/Bbs/Register.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/WEB-INF/Bbs/Register.jsp").forward(req, resp);
        }
    }
}


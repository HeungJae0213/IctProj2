package controller.bbs;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet("/Bbs/register.ict")
public class RegisterController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("POST")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String phone = req.getParameter("phone-part1") + "-" + req.getParameter("phone-part2") + "-" + req.getParameter("phone-part3");
            String gender = req.getParameter("gender");
            String[] interests = req.getParameterValues("inter");
            String interest = String.join(", ", interests);
            String grade = req.getParameter("grade");
            String intro = req.getParameter("self");

            UsersDto item = new UsersDto();
            item.setUsername(username);
            item.setGender(gender);
            item.setGrade(grade);
            item.setInterest(interest);
            item.setIntro(intro);
            item.setName(name);
            item.setPassword(password);
            item.setPhone(phone);
            
            UsersDao dao = new UsersDao(getServletContext());
            int affected = dao.insert(item);
            System.out.println("서버에 입력되었습니다.");
            dao.close();

            if (affected > 0) {
                resp.sendRedirect(req.getContextPath() + "/Bbs/login.ict");
            } else {
                req.setAttribute("errorMsg", "회원가입에 실패했습니다.");
                req.getRequestDispatcher("/WEB-INF/Bbs/Register.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/WEB-INF/Bbs/Register.jsp").forward(req, resp);
        }
    }
}

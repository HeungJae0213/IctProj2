package controller.bbs;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Bbs/logout.ict")
public class LogoutController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 세션 무효화
        req.getSession().invalidate();

        // 토큰 쿠키 삭제
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    cookie.setValue("");
                    cookie.setPath("/"); // 루트 경로로 설정
                    cookie.setMaxAge(0); // 유효 기간을 0으로 설정하여 쿠키 삭제
                    resp.addCookie(cookie);
                    break;
                }
            }
        }

        // 로그아웃 후 로그인 페이지로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/Bbs/login.ict");
    }
}

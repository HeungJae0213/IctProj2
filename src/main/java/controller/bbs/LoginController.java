package controller.bbs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTokens;
import model.users.UsersDao;
import model.users.UsersDto;

@WebServlet("/Bbs/login.ict")
public class LoginController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // POST 요청에서만 로그인 처리
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            UsersDao dao = new UsersDao(getServletContext());
            UsersDto dto = dao.findByUsernameAndPassword(username, password);
            dao.close();

            if (dto != null) {
                // 유효한 사용자라면 JWT 토큰 생성
                Map<String, Object> payloads = new HashMap<>();
                payloads.put("username", dto.getUsername());
                String token = JWTokens.createToken(dto.getUsername(), payloads, 3600000); // 1시간 만료

                // 토큰을 쿠키에 저장
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(3600); // 1시간
                cookie.setPath("/"); // 루트 경로로 설정
                resp.addCookie(cookie);

                // 세션에 사용자 정보 저장
                req.getSession().setAttribute("user", dto);
                
                // 로그인 성공 시 메인 페이지로 리다이렉트
                resp.sendRedirect(req.getContextPath() + "/Bbs/list.ict");
                return;
            } else {
                // 로그인 실패 시 에러 메시지를 요청 영역에 저장
                req.setAttribute("errorMsg", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            }
        }
        // 에러 메시지가 있거나 GET 요청 시 로그인 페이지 표시
        req.getRequestDispatcher("/WEB-INF/Bbs/Login.jsp").forward(req, resp);
    }
}

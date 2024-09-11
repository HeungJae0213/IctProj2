package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTokens;

@WebFilter("/Bbs/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 필터를 거치지 않는 경로 설정
        if (path.equals("/Bbs/register.ict") || path.equals("/Bbs/login.ict") || path.equals("/Bbs/list.ict") || path.equals("/Bbs/listGrid.ict") || path.equals("/Bbs/logout.ict")) {
            chain.doFilter(request, response);
            return;
        }

        String token = JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"));
        boolean isAuthenticated = JWTokens.verifyToken(token);

        if (isAuthenticated) {
            chain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/Bbs/login.ict").forward(req, resp);
        }
    }

}

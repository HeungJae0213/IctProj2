package controller.bbs;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bbs.BbsDao;
import model.bbs.BbsDto;

@WebServlet("/Bbs/view.ict")
public class ViewController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 3] 요청 분석
        String id = req.getParameter("id");

        // Referer 헤더를 통해 이전 페이지 URL 확인
        String referer = req.getHeader("Referer");

        BbsDao dao = new BbsDao(getServletContext());

        // 이전 페이지가 특정 URL인 경우 hitCount 증가
        if (referer != null && (referer.contains("listGrid.ict") || referer.contains("list.ict"))) {
            dao.hitCount(id);
        }

        // 4] 모델 호출 및 결과값 받기
        BbsDto dto = dao.findById(id);
        
        // 이전글/다음글 조회
        Map<String, BbsDto> prevNextMap = dao.prevNext(id);
        dao.close();

        // 줄바꿈 처리
        dto.setContent(dto.getContent().replace("\r\n", "<br/>"));

        // 5] 필요한 값 리퀘스트 영역에 저장
        req.setAttribute("record", dto);
        req.setAttribute("prevPost", prevNextMap.get("PREV"));
        req.setAttribute("nextPost", prevNextMap.get("NEXT"));

        // 6] 뷰 선택후 포워딩
        req.getRequestDispatcher("/WEB-INF/Bbs/View.jsp").forward(req, resp);
    }
}



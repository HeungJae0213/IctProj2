package controller.bbs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PagingUtil;
import model.bbs.BbsDao;
import model.bbs.BbsDto;


@WebServlet(urlPatterns = "/Bbs/listGrid.ict", initParams = {@WebInitParam(name = "PAGE-SIZE", value = "4"), @WebInitParam(name = "BLOCK-PAGE", value = "3")})
public class ListGridController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 사용자 요청을 받는다
        BbsDao dao = new BbsDao(getServletContext());
        Map<String, String> map = new HashMap<>();
        
        // PAGE_SIZE 및 BLOCK_PAGE는 맵에 설정
        String pageSize = getInitParameter("PAGE-SIZE");
        String blockPage = getInitParameter("BLOCK-PAGE");
        map.put(PagingUtil.PAGE_SIZE, pageSize);
        map.put(PagingUtil.BLOCK_PAGE, blockPage);
        
        // 페이징 설정
        PagingUtil.setMapForPaging(map, dao, req);
        
        // 목록 관련 로직 호출
        List<BbsDto> records = dao.findAll(map);
        dao.close();
        
        // 페이징 관련 로직 호출
        int totalRecordCount = Integer.parseInt(map.get(PagingUtil.TOTAL_RECORD_COUNT));
        int nowPage = Integer.parseInt(map.get(PagingUtil.NOWPAGE));
        String paging = PagingUtil.pagingBootStrapStyle(
                totalRecordCount,
                Integer.parseInt(pageSize),
                Integer.parseInt(blockPage),
                nowPage,
                req.getContextPath() + "/Bbs/listGrid.ict?"
        );
        
        // 결과값이 있으면 리퀘스트 영역에 저장
        req.setAttribute("records", records);
        req.setAttribute("paging", paging);
        
        // 결과값을 뿌려줄 뷰(JSP 페이지) 선택 후 포워딩
        req.getRequestDispatcher("/WEB-INF/Bbs/ListGrid.jsp").forward(req, resp);
    }
}



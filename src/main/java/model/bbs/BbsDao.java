package model.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.*;
import javax.sql.*;
import jakarta.servlet.ServletContext;
import model.PagingUtil;
import model.users.UsersDto;
import service.DaoService;

public class BbsDao implements DaoService<BbsDto> {
    private Connection conn;
    private ResultSet rs;
    private PreparedStatement psmt;

    public BbsDao(ServletContext context) {
        try {
            Class.forName(context.getInitParameter("ORACLE-DRIVER"));
            InitialContext initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource source = (DataSource)envContext.lookup("ICTUSER");
            conn = source.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {}
    }

    @Override
    public List<BbsDto> findAll(Map<String, String> map) {
		List<BbsDto> items = new Vector<>();
		//페이징 적용 쿼리
		String sql ="SELECT * FROM (SELECT d.*,RANK() OVER (ORDER BY id DESC) AS idRank FROM bbs d ";
		sql+=")  WHERE idRank BETWEEN ? AND ? ";
		try {
			psmt = conn.prepareStatement(sql);
			
			//페이징을 위한 시작 및 종료 Rank설정
			psmt.setString(1, map.get(PagingUtil.START));
			psmt.setString(2, map.get(PagingUtil.END));			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				BbsDto item = new BbsDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setTitle(rs.getString(3));
				item.setContent(rs.getString(4));
				item.setHitcount(rs.getInt(5));
				item.setPostDate(rs.getDate(6));
				item.setThumbnail(rs.getString(7));
				item.setFiles(rs.getString(8));
				items.add(item);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		System.out.println("size:"+items.size());
		return items;
    }

	@Override
	public BbsDto findById(String... params) {
		BbsDto item=null;
		try {
			psmt=conn.prepareStatement("SELECT * FROM bbs WHERE id=?");
			psmt.setString(1, params[0]);
			rs= psmt.executeQuery();
			if(rs.next()) {
				item = new BbsDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setTitle(rs.getString(3));
				item.setContent(rs.getString(4));
				item.setHitcount(rs.getLong(5));
				item.setPostDate(rs.getDate(6));
				item.setThumbnail(rs.getString(7));
				item.setFiles(rs.getString(8));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return item;
	}

	@Override
	public int getTotalRecordCount(Map<String, String> map) {
		int totalRecordCount=0;
		String sql="SELECT COUNT(*) FROM bbs ";
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			rs.next();
			totalRecordCount= rs.getInt(1);
		}
		catch(SQLException e) {e.printStackTrace();}
		return totalRecordCount;
	}
	
    // 이전글/다음글 조회
    public Map<String, BbsDto> prevNext(String currentId) {
        Map<String, BbsDto> map = new HashMap<>();
        try {
            // 이전글 얻기
            psmt = conn.prepareStatement("SELECT id, title FROM bbs WHERE id = (SELECT MAX(id) FROM bbs WHERE id < ?)");
            psmt.setString(1, currentId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                map.put("PREV", new BbsDto(rs.getLong(1), null, rs.getString(2), null, null, 0, null, null));
            }

            // 다음글 얻기
            psmt = conn.prepareStatement("SELECT id, title FROM bbs WHERE id = (SELECT MIN(id) FROM bbs WHERE id > ?)");
            psmt.setString(1, currentId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                map.put("NEXT", new BbsDto(rs.getLong(1), null, rs.getString(2), null, null, 0, null, null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
    
	@Override
	public int insert(BbsDto dto) {
		int affected=0;
		try {
			psmt = conn.prepareStatement("INSERT INTO bbs VALUES(SEQ_BBS.NEXTVAL,?,?,?,DEFAULT,SYSDATE,?,?)");
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getThumbnail());
			psmt.setString(5, dto.getFiles());
			affected=psmt.executeUpdate();
			System.out.println("입력성공");
		}
		catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

    @Override
    public int update(BbsDto dto) {
		int affected=0;
		try {
			psmt = conn.prepareStatement("UPDATE bbs SET title=?,content=?,thumbnail=?,files=?  WHERE id=?");
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getThumbnail());
			psmt.setString(4, dto.getFiles());
			psmt.setLong(5, dto.getId());
			affected=psmt.executeUpdate();
			System.out.println("수정성공");
		}
		catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

    @Override
    public int delete(BbsDto dto) {
		int affected=0;
		try {
			psmt = conn.prepareStatement("DELETE FROM bbs WHERE id=?");
			psmt.setLong(1, dto.getId());
			affected = psmt.executeUpdate();
		}
		catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
    
    public void hitCount(String id) {
        try {
            // DB 연결 로직
            psmt = conn.prepareStatement("UPDATE BBS SET hitCount = hitCount + 1 WHERE id = ?");
            psmt.setString(1, id);
            psmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

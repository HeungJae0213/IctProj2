package model.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.*;
import javax.sql.*;
import jakarta.servlet.ServletContext;
import model.PagingUtil;
import model.bbs.BbsDto;
import service.DaoService;

public class UsersDao implements DaoService<UsersDto> {
    private Connection conn;
    private ResultSet rs;
    private PreparedStatement psmt;

    public UsersDao(ServletContext context) {
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
    public List<UsersDto> findAll(Map<String, String> map) {
		List<UsersDto> items = new Vector<>();
		//페이징 적용 쿼리
		String sql ="SELECT * FROM (SELECT u.*,RANK() OVER (ORDER BY id DESC) AS idRank FROM users u ";
		sql+=")  WHERE idRank BETWEEN ? AND ? ";
		try {
			psmt = conn.prepareStatement(sql);
			//페이징을 위한 시작 및 종료 Rank설정
			psmt.setString(1, map.get(PagingUtil.START));
			psmt.setString(2, map.get(PagingUtil.END));			
			rs = psmt.executeQuery();
			while(rs.next()) {
				UsersDto item = new UsersDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setPassword(rs.getString(3));
				item.setName(rs.getString(4));
				item.setGender(rs.getString(5));
				item.setInterest(rs.getString(6));
				item.setGrade(rs.getString(7));
				item.setIntro(rs.getString(8));
				item.setPhone(rs.getString(9));
				item.setRegDate(rs.getDate(10));
				items.add(item);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return items;
    }

    @Override
    public UsersDto findById(String... params) {
    	UsersDto item=null;
		try {
			//레코드 하나 조회
			psmt = conn.prepareStatement("SELECT u.*, name FROM users u WHERE id=?");
			psmt.setString(1, params[0]);
			rs = psmt.executeQuery();
			if(rs.next()) {
				item = new UsersDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setPassword(rs.getString(3));
				item.setName(rs.getString(4));
				item.setGender(rs.getString(5));
				item.setInterest(rs.getString(6));
				item.setGrade(rs.getString(7));
				item.setIntro(rs.getString(8));
				item.setPhone(rs.getString(9));
				item.setRegDate(rs.getDate(10));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return item;
    }
    
    public UsersDto findByUsernameAndPassword(String username, String password) {
    	UsersDto item=null;
		try {
			//레코드 하나 조회
			psmt = conn.prepareStatement("SELECT u.*, name FROM users u WHERE username=?");
			psmt.setString(1, username);
			rs = psmt.executeQuery();
			if(rs.next()) {
				item = new UsersDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setPassword(rs.getString(3));
				item.setName(rs.getString(4));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
        if (item != null && item.getPassword().equals(password)) {
            return item;
        }
        return null;
    }
    
    @Override
    public int getTotalRecordCount(Map<String, String> map) {
		int totalRecordCount=0;
		String sql="SELECT COUNT(*) FROM users ";
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			rs.next();
			totalRecordCount= rs.getInt(1);
		}
		catch(SQLException e) {e.printStackTrace();}
		return totalRecordCount;
    }

    @Override
    public int insert(UsersDto dto) {
        int affected = 0;
        if (conn == null) {
            System.out.println("Connection is null. Cannot insert data.");
            return affected;
        }
        try {
            psmt = conn.prepareStatement("INSERT INTO USERS VALUES (SEQ_USER.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)");
            psmt.setString(1, dto.getUsername());
            psmt.setString(2, dto.getPassword());
            psmt.setString(3, dto.getName());
            psmt.setString(4, dto.getGender());
            psmt.setString(5, dto.getInterest());
            psmt.setString(6, dto.getGrade());
            psmt.setString(7, dto.getIntro());
            psmt.setString(8, dto.getPhone());
            affected = psmt.executeUpdate();
            rs = psmt.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affected;
    }

    @Override
    public int update(UsersDto dto) {
        int affected = 0;
        if (conn == null) {
            System.out.println("Connection is null. Cannot update data.");
            return affected;
        }
        try {
            // ID 값을 기준으로 사용자의 정보를 업데이트
            psmt = conn.prepareStatement("UPDATE USERS SET USERNAME=?, PASSWORD=?, NAME=?, GENDER=?, INTEREST=?, GRADE=?, INTRO=?, PHONE=? WHERE ID=?");
            psmt.setString(1, dto.getUsername());
            psmt.setString(2, dto.getPassword());
            psmt.setString(3, dto.getName());
            psmt.setString(4, dto.getGender());
            psmt.setString(5, dto.getInterest());
            psmt.setString(6, dto.getGrade());
            psmt.setString(7, dto.getIntro());
            psmt.setString(8, dto.getPhone());
            psmt.setLong(9, dto.getId());
            affected = psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affected;
    }

	@Override
	public int delete(UsersDto dto) {
		int affected=0;
		try {
			psmt = conn.prepareStatement("DELETE FROM USERS WHERE id=?");
			psmt.setLong(1, dto.getId());
			affected = psmt.executeUpdate();
		}
		catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

	public UsersDto findByUsername(String id) {
    	UsersDto item=null;
		try {
			//레코드 하나 조회
			psmt = conn.prepareStatement("SELECT u.*, name FROM users u WHERE id=?");
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if(rs.next()) {
				item = new UsersDto();
				item.setId(rs.getLong(1));
				item.setUsername(rs.getString(2));
				item.setPassword(rs.getString(3));
				item.setName(rs.getString(4));
				item.setGender(rs.getString(5));
				item.setInterest(rs.getString(6));
				item.setGrade(rs.getString(7));
				item.setIntro(rs.getString(8));
				item.setPhone(rs.getString(9));
				item.setRegDate(rs.getDate(10));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return item;
    }


}

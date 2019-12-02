package com.spring.springboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class BoardDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
//글의 갯수를 구하는 함수.
	public int getArticleCount()   {
		conn = JDBCUtil.getConnection();

		int count = 0;                           
		try {
			pstmt = conn.prepareStatement("select count (*) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1); //1은 첫번째 인덱스를 의미한다 [count(*) 결과]
			}                                            
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return count;
	}
//리스트 구하기
	public ArrayList<BoardVO> getArticles(int start, int end)   {
		// 1페이지라면 start는 1번글 end는 10번글, 2페이지라면 start는 11번글 end는 20번글
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardVO> articleList = null;
		String sql = null;
		try {
			conn = JDBCUtil.getConnection();
			sql = "select * from "
					+ "(select rownum rnum, num, writer, passwd, subject, email, content, reg_date, readcount, ref, re_step, re_level "
					+ "from (select * from board order by ref desc, re_step asc))" 
					+ "where rnum>=? and rnum<=?";
			/*
			 * 새롭게 만든 테이블(from절)에서 select 한 새로운 테이블에는 번호(rownum)가 붙고 그것에 대한 별칭으로 rnum을 지정한다.
			 */
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				
				articleList = new ArrayList<BoardVO>();
				
				do {
					BoardVO article = new BoardVO();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setPasswd(rs.getString("passwd"));
					article.setSubject(rs.getString("subject"));
					article.setEmail(rs.getString("email"));
					article.setContent(rs.getString("content"));
					article.setReg_date(rs.getTimestamp("reg_date"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setRe_step(rs.getInt("re_step"));
					article.setRe_level(rs.getInt("re_level"));

					articleList.add(article);
				} while (rs.next());
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return articleList;
	}
//글쓰기	
	public int insertArticle(BoardVO article)  {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result = 0;
		
		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
		int number = 0;
		
		String sql = "";
		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement("select max(num)from board");
			rs = pstmt.executeQuery();
			
			if(rs.next()) { 
				number = rs.getInt(1)+1;
			}else { //next가 없으면 최초 글쓰기
				number = 1; //=해당 테이블의 최초글
			}
			
			if(num!=0) { //원글에 대한 혹은 답글에 대한 답글쓰기
				sql = "update board set re_step = re_step+1 where ref = ? and re_step > ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,ref);
				pstmt.setInt(2,re_step);
				pstmt.executeUpdate();
				re_step = re_step + 1;
				re_level = re_level + 1;
			}
			else { //원글쓰기
				ref = number;
				re_step = 0;
				re_level = 0;
			}
			sql = "insert into board(num, writer, passwd, subject, email, content, reg_date, readcount,";
			sql += "ref, re_step, re_level) values (board_seq.nextval,?,?,?,?,?,sysdate,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getPasswd());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getEmail());
			pstmt.setString(5, article.getContent());
			//pstmt.setTimestamp(6, article.getReg_date());
			pstmt.setInt(6, article.getReadcount());
			pstmt.setInt(7, ref);
			pstmt.setInt(8, re_step);
			pstmt.setInt(9, re_level);
			
			pstmt.executeUpdate();
			
			result = 1;
	
		}catch(Exception ex) {
			System.out.println("글쓰기 오류 : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		
		return result;
		
	}
//상세보기
	public BoardVO getArticle(int num)  {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;
		
		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement("select * from board where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				article = new BoardVO();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setPasswd(rs.getString("passwd"));
				article.setSubject(rs.getString("subject"));
				article.setEmail(rs.getString("email"));
				article.setContent(rs.getString("content"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("re_step"));
				article.setRe_level(rs.getInt("re_level"));
				
				pstmt = conn.prepareStatement("update board set readcount=? where num = ?");
				pstmt.setInt(1, rs.getInt("readcount")+1);
				pstmt.setInt(2, num);
				pstmt.executeUpdate();
				
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		
		return article;
	}
		
//업데이트시 기존정보 불러오기
	public BoardVO updateGetArticle(int num)  {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;
		
		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement("select * from board where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				article = new BoardVO();
				article.setNum(num);
				
				article.setWriter(rs.getString("writer"));
				article.setSubject(rs.getString("subject"));
				article.setEmail(rs.getString("email"));
				article.setContent(rs.getString("content"));
				article.setPasswd(rs.getString("passwd"));
				
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		
		return article;
	}
	
	
//실제 업데이트
	public int updateArticle(BoardVO article) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String dbpasswd = "";
		String sql = "";
		int x = -1;
		
		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement("select passwd from board where num = ?");
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				dbpasswd = rs.getString("passwd");
				
				System.out.println("dbpasswd" + dbpasswd );
				System.out.println("article.getPasswd" + article.getPasswd());
				System.out.println(dbpasswd.equals(article.getPasswd()));
				
				if(dbpasswd.equals(article.getPasswd())) {
					sql = "update board set writer = ?, passwd = ?, subject = ?, email = ?, content = ? where num = ?";
					

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getPasswd());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getEmail());
					pstmt.setString(5, article.getContent());
					pstmt.setInt(6, article.getNum());
					pstmt.executeUpdate();
					x=1;
					System.out.println("실행되었니??");
					
				}else {
					x=0;
				}
				
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		System.out.println("x=" + x );
		return x;
	}	
//글 삭제
	public int deleteArticle(int num, String passwd)  {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String dbpasswd = "";
		int x = -1;
		
		try {
				
			conn = JDBCUtil.getConnection();
			
			pstmt = conn.prepareStatement("select passwd from board where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				if(dbpasswd.equals(passwd)) {
					pstmt = conn.prepareStatement("delete from board where num = ?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();
					System.out.println("삭제되었니??");
					x=1;
				}else {
					x=0;
				}
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return x;
	}
	
	
	
	
	
	
	
	
	
	
}

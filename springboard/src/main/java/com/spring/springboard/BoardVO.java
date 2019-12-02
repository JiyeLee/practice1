package com.spring.springboard;
/*
create table board (
    num number primary key, 
    writer varchar2(15), 
    passwd varchar2(15), 
    subject varchar2(50),       
    email varchar2(50), 
    content varchar2(2000), 
    reg_date timestamp, 
    readcount number, 
    ref number, 
    re_step number,
    re_level number
);

create sequence board_seq
    start with 1
    increment by 1
    maxvalue 100000;

*/
import java.sql.Timestamp;

public class BoardVO {

	private int num;			//시퀀스 넘버
	private String writer;		//작성자
	private String passwd;		//비밀번호
	private String subject;		//제목
	private String email;		//이메일
	private String content;		//내용
	private Timestamp reg_date;	//작성일
	private int readcount;		//조회수
	private int ref;			//원글 지표
	private int re_step;		//가시목록
	private int re_level;		//원/댓/대댓
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public int getRe_step() {
		return re_step;
	}
	public void setRe_step(int re_step) {
		this.re_step = re_step;
	}
	public int getRe_level() {
		return re_level;
	}
	public void setRe_level(int re_level) {
		this.re_level = re_level;
	}
	
}

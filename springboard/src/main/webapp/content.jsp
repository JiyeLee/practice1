<%--content --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.spring.springboard.BoardVO"%>

<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="color.jsp"%>

<html>
<head>
<meta charset="UTF-8">
<title>게 시 판</title>
<style>
body {
	text-align: center;
}
</style>
</head>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	int num = (int)request.getAttribute("num");	
	String pageNum = (String)request.getAttribute("pageNum");	
	int number = (int)request.getAttribute("number");	
	BoardVO article = (BoardVO)request.getAttribute("article");	
	int ref = (int)request.getAttribute("ref");	
	int re_step = (int)request.getAttribute("re_step");	
	int re_level = (int)request.getAttribute("re_level");	


	
%>
<body bgcolor="<%=bodyback_c%>">
	<b> 글 내용 보기 </b>
	<br>
	<form>
		<table width="555" border="1" cellspacing="0" cellpadding="0"
			bgcolor="<%=bodyback_c%>" align="center">
			<tr height="30" align="center" width="125">
				<td bgcolor="<%=value_c%>">글 번호</td>
				<td><%=number%></td>
				<td bgcolor="<%=value_c%>">조회수</td>
				<td><%=article.getReadcount()%></td>
			</tr>
			<tr height="30" align="center" width="125">
				<td bgcolor="<%=value_c%>">작성자</td>
				<td><%=article.getWriter()%></td>
				<td bgcolor="<%=value_c%>">작성일</td>
				<td><%=sdf.format(article.getReg_date())%></td>
			</tr>
			<tr height="30">
				<td align="center" width="125" bgcolor="<%=value_c%>">글제목</td>
				<td align="left" colspan="3">&nbsp;<%=article.getSubject()%>
				</td>
			</tr>
			<tr>
				<td align="center" width="125" bgcolor="<%=value_c%>">글내용</td>
				<td align="left" colspan="3"><pre><%=article.getContent()%></pre>
				</td>
			</tr>
			<tr height="30">
				<td colspan="4" bgcolor="<%=value_c%>" align="center"><input
					type="button" value="글수정"
					Onclick="document.location.href=
                           'updateForm.bo?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
					&nbsp; &nbsp; <input type="button" value="글삭제"
					Onclick="document.location.href=
                           'deleteForm.bo?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
					&nbsp; &nbsp; <input type="button" value="답글쓰기"
					Onclick="document.location.href=
                           'writeForm.bo?num=<%=num%>&ref=<%=ref%>&re_step=<%=re_step%>&re_level=<%=re_level%>'">
					&nbsp; &nbsp; <input type="button" value="글목록"
					onclick="document.location.href=
                           'list.bo?pageNum=<%=pageNum%>'">
					<!--  ref: 같은 글 그룹 번호 --></td>
			</tr>
		</table>
	</form>
</body>


</html>
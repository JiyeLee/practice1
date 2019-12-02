<%-- writeForm --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "color.jsp" %>

<html>
   <head>
   <title> 게시판 </title>
   </head>
<%
 
	int num = (int)request.getAttribute("num");
	int ref = (int)request.getAttribute("num");
	int re_step = (int)request.getAttribute("re_step");
	int re_level = (int)request.getAttribute("re_level");
	
	
%>      
   <body bgcolor="<%=bodyback_c %>">
      <center><b> 글쓰기 </b> <br>
      
      <form method = "post" name = "writeForm" action = "writePro.bo">
         <input type = "hidden" name = "num" value = "<%=num %>">
         <input type = "hidden" name = "ref" value = "<%=ref %>">
         <input type = "hidden" name = "re_step" value = "<%=re_step %>">
         <input type = "hidden" name = "re_level" value = "<%=re_level %>">
         
         <table width = "400" border = "1" cellspacing = "0" cellpadding = "0" bgcolor = "<%=bodyback_c %>" align = "center">
            <tr>
               <td align = "right" colspan = "2" bgcolor = "<%=value_c %>">
                  <a href = "list.bo">글목록</a>
               </td>
            </tr>
            <tr>
               <td width = "70" bgcolor = "<%=value_c %>" align = "center"> 이 름 </td>
               <td width = "330">
                  <input type = "text" size = "10" maxlength = "10" name = "writer"></td>
            </tr>
            <tr>
               <td width = "70" bgcolor = "<%=value_c %>" align = "center"> 제 목 </td>
               <td width = "330">
<%
                  if(request.getParameter("num") == null)  //원글쓰기일때
                  {
%>
                     <input type = "text" size = "40" maxlength = "50" name = "subject">
<%
                  }
                  else  //답변쓰기일때
                  {
%>
                     <input type = "text" size = "40" maxlength = "50" name = "subject" value = "[답변]">   
<%
                  }
%>
               </td>
            </tr>
            <tr>
               <td width = "70" bgcolor = "<%=value_c %>" align = "center"> Email </td>
               <td width = "330">
                  <input type = "text" size = "40" maxlength = "30" name = "email">
               </td>
            </tr>
            <tr>
               <td width = "70" bgcolor = "<%=value_c %>" align = "center"> 내 용 </td>
               <td width = "330">
                  <textarea name = "content" rows = "13" cols = "40"></textarea>
               </td>
            </tr>
            <tr>
               <td width = "70" bgcolor = "<%=value_c %>" align = "center"> 비밀번호 </td>
               <td width = "330">
                  <input type = "password" size = "8" maxlength = "12" name = "passwd">
               </td>
            </tr>
            <tr>
               <td colspan = "2" bgcolor = "<%=value_c %>" align = "center">
                  <input type = "submit" value = "글쓰기">
                  <input type = "reset" value = "다시 작성">
                  <input type = "button" value = "목록 보기" Onclick = "window.location='list.bo'" >
               </td>
            </tr>
         </table>
      </form>
      </center>
   </body>      
      
<%       
 
%>   
   
</html>
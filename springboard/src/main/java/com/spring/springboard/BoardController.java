package com.spring.springboard;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	

	//@RequestMapping("/")	//
	@RequestMapping("/list.bo")
	public String getArticles(Model model, HttpServletRequest request) throws Exception {
		
		int pageSize = 10;
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage -1) * pageSize +1; //1~10 , 11~20 을 구하는 식.
		int endRow = startRow + pageSize -1;
		int count = 0;
		int number = 0;
		
		List articleList = null;
		count =  boardService.getArticleCount();
			//count는 전체 데이터 갯수
		
		
		//맨 마지막 페이지의 마지막 글을 삭제하여 마지막 페이지가 없어졌을때 필요
		if(count<startRow){
			currentPage = currentPage-1;
			startRow = (currentPage -1)*pageSize +1;
			endRow = startRow + pageSize -1;	
		}
		if(count>0){
			articleList =  boardService.getArticles(startRow,endRow);
			number = count-(currentPage -1)*pageSize;
		}
			
		model.addAttribute("articleList",articleList);
		model.addAttribute("currentPage",currentPage);
		model.addAttribute("count",count);
		model.addAttribute("number",number);
		model.addAttribute("pageSize",pageSize);
		
		return "list";
	}
	@RequestMapping("/writeForm.bo")
	public String writeForm(Model model, HttpServletRequest request) throws Exception{
		
		int num=0, ref=1, re_step=0, re_level=0;
		//num 글번호: 보드테이블의 num 칼럼 // ref 글그룹 : 원글 답글 어저고 묶은것 //re_step : 댓글순서 //re_level : 들여쓰기
		/*원글에 대한 답글들은 원글과 동일한 ref를 가진다. 
			re_level 른 같은 원글인지 답글인지 답답글인지
			re_step은 눈에 보이는 순서가 바뀌는것으로 윗쪽에 글이 늘어나면 아래에 있는 글들의 re_step도 다 바뀐다.*/
	  
	      if(request.getParameter("num") != null) //답글쓰기 
	      {
	         num = Integer.parseInt(request.getParameter("num"));
	         ref = Integer.parseInt(request.getParameter("ref"));
	         re_step = Integer.parseInt(request.getParameter("re_step"));
	         re_level = Integer.parseInt(request.getParameter("re_level"));
	      }
		
	     model.addAttribute("num",num);
	     model.addAttribute("ref",ref);
	     model.addAttribute("re_step",re_step);
	     model.addAttribute("re_level",re_level);
	      
		return "writeForm";
	}
	
	@RequestMapping("/writePro.bo")
	public String writePro(BoardVO boardVO, Model model) throws Exception {
		int res = boardService.insertArticle(boardVO);
		if(res!=0) {
			System.out.println("글쓰기 성공");
			return "redirect:/list.bo";
		}
		else {
			System.out.println("글쓰기 실패");
			return"redirect:/writeForm.bo";
		}
		
	
	}
		
	
	@RequestMapping("/content.bo")
	public String content(Model model, HttpServletRequest request) throws Exception{
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		int number = Integer.parseInt(request.getParameter("number"));

		BoardVO article = boardService.getArticle(num);

		/* 원글에 대한 ref, re_step, re_level*/
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
	
		model.addAttribute("num",num);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("number",number);
		model.addAttribute("article",article);
		model.addAttribute("ref",ref);
		model.addAttribute("re_step",re_step);
		model.addAttribute("re_level",re_level);

	
	
		return "content";
	
	}
	
	
	@RequestMapping("/updateForm.bo")
	public String updateForm(Model model, HttpServletRequest request) throws Exception {
	
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		BoardVO article = boardService.updateGetArticle(num);
		
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("article",article);
		
		return "updateForm";
		
	}
	
	
	@RequestMapping("/updatePro.bo")
	public String updatePro(BoardVO boardVO, Model model, HttpServletRequest request)throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		
		int res = boardService.updateArticle(boardVO);
		
		model.addAttribute(pageNum);
		if(res == 1){
			System.out.println("업데이트 성공ㄴ");
			return "redirect:/list.bo";
		}
		else {
			System.out.println("업데이트 실패");
			return "redirect/list.bo";
		}
	}
	
	@RequestMapping("/deleteForm.bo")
	public String deleteForm(Model model, HttpServletRequest request) throws Exception {
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		model.addAttribute("num",num);
		model.addAttribute("pageNum",pageNum);
		
		return "deleteForm";
		
		
	}
	@RequestMapping("/deletePro")
	public String deletePro(Model model, HttpServletRequest request,HttpServletResponse response)throws Exception {
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String passwd = request.getParameter("passwd");
		
		int res= boardService.deleteArticle(num, passwd);//비밀번호가 맞았을때 1(업데이트 동작), 틀렸을때 0
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		PrintWriter writer = response.getWriter();
		
		model.addAttribute("pageNum",pageNum);
		
		if(res==1) {
			writer.write("<script>alert('삭제 성공!!');" + "location.href='./list.bo';</script>");
			
		}else {
			writer.write("<script>alert('삭제 실패!!');" + "location.href='./list.bo';</script>");
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

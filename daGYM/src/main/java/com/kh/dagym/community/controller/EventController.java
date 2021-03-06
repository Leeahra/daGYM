package com.kh.dagym.community.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.dagym.common.Attachment;
import com.kh.dagym.common.Board;
import com.kh.dagym.common.PageInfo;
import com.kh.dagym.community.model.service.EventService;
import com.kh.dagym.member.model.vo.Member;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("/event/*")
public class EventController {

	@Autowired
	private EventService eventService;

	private final int BOARD_TYPE = 1;
	private final int LIST = 0;
	private final int END_LIST = -1;


	@GetMapping("list")
	public String eventList(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		PageInfo pInfo = eventService.pagenation(BOARD_TYPE, cp, LIST);


		List<Board> eventList = eventService.selectList(pInfo, LIST);

		if (!eventList.isEmpty()) {
			List<Attachment> thList = eventService.selectThumbnailList(eventList);

			model.addAttribute("thList",thList);
		}

		model.addAttribute("eventList",eventList);
		model.addAttribute("pInfo",pInfo);


		return "community/eventList";
	}

	@GetMapping("insert")
	public String boardInsertForm(@RequestParam int boardType, Model model) {
		model.addAttribute("boadType", boardType);
		Member loginMember = (Member)model.getAttribute("loginMember");
		return "community/eventInsert";
	}

	@PostMapping("insert")
	public String boardInsert(@RequestParam int boardType,Board board, Model model, RedirectAttributes rdAttr,
			@RequestParam(value = "images", required = false) List<MultipartFile> images, HttpServletRequest request) {
		System.out.println(board);
		Member loginMember = (Member)model.getAttribute("loginMember");
		board.setBoardType(boardType);
		board.setBoardWriter(loginMember.getMemberNo() + "");

		String savePath = request.getSession().getServletContext().getRealPath("resources/uploadImages");
		int result = eventService.insertBoard(board, images, savePath);
		String status;
		String msg;
		String path;
		if (result > 0) {
			status = "success";
			msg = "게시글 삽입 성공";
			path = "redirect:list?cp=1";
		} else {
			status = "error";
			msg = "게시글 삽입 실패";
			path = "insert";
		}
		rdAttr.addFlashAttribute("status",status);
		rdAttr.addFlashAttribute("msg",msg);

		return path;
	}

	@GetMapping("{boardNo}")
	public String boardView(@PathVariable int boardNo, Model model,RedirectAttributes rdAttr) {

		Board board = eventService.selectBoard(boardNo);
		String url = "";

		if(board != null) {
			List<Attachment> files = eventService.selectFiles(boardNo);

			if(!files.isEmpty()) {
				model.addAttribute("files", files);
				files.stream().forEach(System.out::println);
			}
			model.addAttribute("board", board);
			url = "community/eventView";
		} else {
			// 존재하지 않은 게시글입니다 출력 후 이전 주소로 리다이렉트
			rdAttr.addFlashAttribute("status","error");
			rdAttr.addFlashAttribute("msg","해당 게시글 존재 X");
			rdAttr.addFlashAttribute("status","error");
			url = "redirect:/event/list";
		}
		return url;
	}

	@GetMapping("{boardNo}/delete")
	public String deleteEvent(@PathVariable int boardNo, RedirectAttributes rdAttr,HttpServletRequest request) {
		String savePath = request.getSession().getServletContext().getRealPath("resources/uploadImages");
		int result = eventService.deleteEvent(boardNo, savePath);
		String status;
		String msg;
		String path;
		if (result > 0) {
			status = "success";
			msg = "게시글 삭제 성공";
			path = "/event/list";
		} else {
			status = "error";
			msg = "게시글 삭제 실패";
			path = request.getHeader("referer");
		}

		rdAttr.addFlashAttribute("status", status);
		rdAttr.addFlashAttribute("msg",msg);
		return "redirect:"+path;
	}

	@GetMapping("{boardNo}/update")
	public ModelAndView eventUdateView(@PathVariable int boardNo, ModelAndView mv) {
		Board board = eventService.selectBoard(boardNo);

		if (board != null) {
			List<Attachment> files = eventService.selectFiles(boardNo);
			mv.addObject("files", files);
		} 
		mv.addObject("board", board);
		mv.setViewName("community/eventUpdate");

		return mv;
	}

	@PostMapping("{boardNo}/update")
	public ModelAndView eventUpdate(@PathVariable int boardNo, ModelAndView mv, 
			Board upBoard, boolean[] deleteImages, RedirectAttributes rdAttr, HttpServletRequest request,
			@RequestParam(value="thumbnail", required = false) MultipartFile thumbnail,
			@RequestParam(value="images", required = false) List<MultipartFile> images) {

		upBoard.setBoardNo(boardNo);



		String savePath = request.getSession().getServletContext().getRealPath("resources/uploadImages");


		int result = eventService.updateEvent(upBoard, savePath, images, deleteImages);

		String status;
		String msg;
		String url;
		if (result > 0) { 
			status = "success";
			msg = "게시글 수정 성공";
			//url = "/board/"+type+"/"+boardNo+"?cp="+cp;
			url = "../"+boardNo;
		} else {
			status = "error";
			msg = "게시글 수정 실패";
			url = request.getHeader("referer");
		}
		rdAttr.addFlashAttribute("status", status);
		rdAttr.addFlashAttribute("msg",msg);

		//mv.addObject("board", upBoard);
		mv.setViewName("redirect:"+url);

		return mv;
	}

	// ==================== 종료된 이벤트 ================================


	@GetMapping("end-list")
	public String endList(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		PageInfo pInfo = eventService.pagenation(BOARD_TYPE, cp, END_LIST);

		List<Board> eventList = eventService.selectList(pInfo, END_LIST);

		if (!eventList.isEmpty()) {
			List<Attachment> thList = eventService.selectThumbnailList(eventList);
			thList.stream().forEach(System.out::println);

			model.addAttribute("thList",thList);
		}

		model.addAttribute("eventList",eventList);
		model.addAttribute("pInfo",pInfo);

		return "community/endList";
	}

}

package com.sbs.dan.at.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.dan.at.dto.Member;
import com.sbs.dan.at.service.MemberService;
import com.sbs.dan.at.util.Util;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;

	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "/member/join";
	}

	@RequestMapping("/usr/member/doJoin")
	public String doJoin(@RequestParam Map<String, Object> param, Model model) {
		memberService.doJoin(param);
		
		model.addAttribute("alertMsg","회원가입을 축하 합니다.");
		return "common/redirect";
	}

	@RequestMapping("/usr/member/login")
	public String login() {
		return "/member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	public String doLogin(String loginId, String loginPwReal, String redirectUri, Model model, HttpSession session) {
		String loginPw = loginPwReal;
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","존재하지 않는 회원입니다.");
			return "common/redirect";
		}
		
		if (member.getDelStatus() == 1) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","존재하지 않는 회원입니다.");
			return "common/redirect";
		}
		
		if (member.getLoginPw().equals(loginPw) == false) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","비밀번호가 일치하지 않습니다.");
			return "common/redirect";
		}
		
		session.setAttribute("loginedMemberId",member.getId());
		
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}
		
		model.addAttribute("redirectUri",redirectUri);
		model.addAttribute("alertMsg",String.format("%s님 반갑습니다.", member.getNickname()));
		
		return "common/redirect";
	}
	
	@RequestMapping("/usr/member/doLogout")
	public String doLogout(HttpSession session, Model model, String redirectUri) {
		session.removeAttribute("loginedMemberId");
		
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}
		model.addAttribute("redirectUri",redirectUri);
		model.addAttribute("alertMsg","안녕히 가세요");
		
		return "common/redirect";
	}
	
	@RequestMapping("/usr/member/modify")
	public String modify(HttpSession session, Model model, String redirectUri) {
		Member member = memberService.getMemberById((int)session.getAttribute("loginedMemberId"));
		model.addAttribute("member", member);
		
		return "/member/modify";
	}
	
	@RequestMapping("/usr/member/doModify")
	public String doModify(@RequestParam Map<String, Object> param, HttpSession session, Model model, String redirectUri) {
		Member member = memberService.getMemberById((int)session.getAttribute("loginedMemberId"));
		int id = (int)session.getAttribute("loginedMemberId");
		
		Map<String, Object> newParam = Util.getNewMapOf(param, "loginPwReal", "nickname", "email", "cellphoneNo");
		newParam.put("id", id);
		
		memberService.modify(newParam);
		
		return "redirect:"+redirectUri;
	}
	
	@RequestMapping("/usr/member/deleteAccount")
	public String deleteAccount (@RequestParam Map<String, Object> param, HttpSession session, Model model, String redirectUri) {
		int id = (int)session.getAttribute("loginedMemberId");
		Member member = memberService.getMemberById(id);
		
		memberService.deleteAccount(member);
		doLogout(session, model, redirectUri);
		
		return "redirect:/usr/home/main";
	}
	
	@RequestMapping("/usr/member/accountInfo")
	public String accountInfo(HttpSession session,@RequestParam Map<String,Object> param, Model model, String redirectUri) {
		String loginId = (String)param.get("id");
		
		Member member = memberService.getMemberById((int)session.getAttribute("loginedMemberId"));
		model.addAttribute("member",member);
		
		return "/member/accountInfo";
	}
	
	@RequestMapping("/usr/member/findLoginPw")
	public String findLoginPw() {
		return "/member/findPw";
	}
	
	@RequestMapping("/usr/member/doFindLoginPw")
	public String doFindLoginPw(@RequestParam Map<String,Object> param, Model model, String redirectUri) {
		Map<String,Object> newParam = Util.getNewMapOf(param,"name","loginId","email");
		
		String loginPw = (String)memberService.findPw(newParam);
		Member member = memberService.getMemberByNameLoginId(newParam);
		
		if (member == null) {
			model.addAttribute("historyBack", true);
			model.addAttribute("alertMsg","존재하지 않는 회원입니다.");
			return "common/redirect";
		}
		
		if (member.getDelStatus() == 1) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","탈퇴한 회원입니다.");
			return "common/redirect";
		}
		
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}

			model.addAttribute("redirectUri",redirectUri);
			model.addAttribute("alertMsg","가입하신 이메일로 임시 비밀번호를 발송했습니다.");
		
		return "common/redirect";
	}
	
	@RequestMapping("/usr/member/findLoginId")
	public String findLoginId() {
		return "/member/findId";
	}
	
	@RequestMapping("/usr/member/doFindLoginId")
	public String doFindLoginId(@RequestParam Map<String, Object> param, Model model,String redirectUri) {
		Map<String, Object> newParam = Util.getNewMapOf(param, "name","email");
		
		String loginId = (String)memberService.findId(newParam);
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","존재하지 않는 회원입니다.");
			return "common/redirect";
		}
		
		if (member.getDelStatus() == 1) {
			model.addAttribute("historyBack",true);
			model.addAttribute("alertMsg","탈퇴한 회원입니다.");
			return "common/redirect";
		}
		
		if (redirectUri == null || redirectUri.length() == 0) {
			redirectUri = "/usr/home/main";
		}
		
		model.addAttribute("redirectUri",redirectUri);
		model.addAttribute("alertMsg",String.format("찾으시는 아이디는 %s 입니다.", member.getNickname()));
		
		return "common/redirect";
	}
	
	@RequestMapping("/usr/member/manage")
	public String manage(@RequestParam Map<String,Object> param, Model model, String redirectUri,HttpServletRequest req) {
		List<Member> members = memberService.getMembersToManage();
		model.addAttribute("members",members);
		
		return "/member/manage";
	}
}
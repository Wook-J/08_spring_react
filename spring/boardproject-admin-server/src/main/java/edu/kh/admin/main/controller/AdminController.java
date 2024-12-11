package edu.kh.admin.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.admin.main.model.dto.Member;
import edu.kh.admin.main.model.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 서버 : 8080포트(Spring), 클라이언트 : 3000포트(리액트)
 * -> 브라우저가 주체인데 기본적으로 다른 출처의 요청과 응답, 리소스들의 공유를 막아둠
 * -> 현재 포트번호가 달라서 브라우저가 다른 출처로 인식함
 * 
 * @CrossOrigin(origins = "http://localhost:3000")
 * -> CORS Error 방지를 위해 다른 포트간 자원을 공유할 수 있도록 함
 * (서버단에서 해줘야 함!)
 * */

@RestController		// 비동기요청(@ResponseBody 생략 가능)
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("admin")
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})		// 로그인 멤버를 세션에 올리기
@Slf4j
public class AdminController {
	
	private final AdminService service;
	
	/* ********** 메서드 ********** */
	/** 관리자 로그인
	 * @param inputMember
	 * @param model
	 * @return
	 */
	@PostMapping("login")
	public Member login(@RequestBody Member inputMember,
						Model model) {
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) return null;
		
		model.addAttribute(loginMember);	// key-value 아니고 value 만 작성하면 동일한 name 이 key 임
		return loginMember;
	}
	
//	@GetMapping("logout")	// 내 버전 로그아웃
//	public String logout(SessionStatus status) {
//		status.setComplete();
//		return null;
//	}
	
	@GetMapping("logout")	// 선생님 버전 로그아웃
	public ResponseEntity<String> logout(HttpSession session) {
		
		// ResponseEntity (자주 사용함...?)
		// Spring 에서 제공하는 Http 응답 데이터를 커스터마이징 할 수 있도록 지원하는 클래스
		// HTTP 상태코드, 헤더, 응답 본문(body)을 모두 설정 가능
		/*
		 * HttpStatus.OK: 200 OK
		 * HttpStatus.CREATED: 201 Created
		 * HttpStatus.NO_CONTENT: 204 No Content
		 * HttpStatus.BAD_REQUEST: 400 Bad Request
		 * HttpStatus.UNAUTHORIZED: 401 Unauthorized
		 * HttpStatus.FORBIDDEN: 403 Forbidden
		 * HttpStatus.NOT_FOUND: 404 Not Found
		 * HttpStatus.INTERNAL_SERVER_ERROR: 500 Internal Server Error 
		 * 
		 * */
		
		try {
			session.invalidate();			// 세선 무효화 처리
			return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 완료되었습니다");
			
		} catch (Exception e) {
			// 세션 무효화 중 예외발생한 경우 (서버에서 발생한 오류 500번)
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("로그아웃 처리 중 문제 발생 : " + e.getMessage());
		}
	}
}


package edu.kh.admin.main.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.admin.main.model.dto.Board;
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
	
	/* ********** 복구 관련 ********** */
	// 탈퇴한 회원 목록 조회
	@GetMapping("withdrawnMemberList")
	public ResponseEntity<Object> selectWithdrawnMemberList() {
		// 성공 시 List<Member>, 실패 시 String 반환 -> Generic 으로 Object 사용
		// (참고) ResponseEntity<?> : 반환값을 특정할 수 없을 때 사용...(불명학하여 지양하는 편)
		
		try {
			List<Member> withdrawnMemberList = service.selectWithdrawnMemberList();
			return ResponseEntity.status(HttpStatus.OK).body(withdrawnMemberList);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴한 회원 목록 조회중 문제 발생 : " + e.getMessage());
		}
	}
	
	// 탈퇴 회원 복구
	@PutMapping("restoreMember")
	public ResponseEntity<String> restoreMember(@RequestBody Member member){
		
		try {
			int result = service.restoreMember(member.getMemberNo());
			
			if(result > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(member.getMemberNo() + "번 회원 복구 완료");
				
			} else {	// 업데이트된 행이 없음 -> 400(요청구문이 잘못되었거나 유효하지 않음)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("유효하지 않은 memberNo : " + member.getMemberNo());
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("탈퇴 회원 복구 중 문제 발생 : " + e.getMessage());
		}
	}
	
	// 삭제한 게시글 목록 조회
	@GetMapping("deleteBoardList")
	public ResponseEntity<Object> selectDeleteBoardList () {
		try {
			List<Board> deleteBoardList = service.deleteBoardList();
			return ResponseEntity.status(HttpStatus.OK).body(deleteBoardList);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("삭제한 게시글 목록 조회 중 문제 발생 : " + e.getMessage());
		}
	}
	
	// 삭제한 게시글 복구
	@PutMapping("restoreBoard")
	public ResponseEntity<String> restoreBoard (@RequestBody Board board) {
		try {
			int result = service.restoreBoard(board.getBoardNo());
			
			if(result > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(board.getBoardNo() + "번 게시글 복구 완료");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("유효하지 않은 boardNo : " + board.getBoardNo());
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("삭제한 게시글 복구 중 문제 발생 : " + e.getMessage());
		}
	}
	
}


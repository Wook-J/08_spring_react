package edu.kh.admin.main.model.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.admin.main.model.dto.Board;
import edu.kh.admin.main.model.dto.Member;
import edu.kh.admin.main.model.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService{

	private final AdminMapper mapper;
	private final BCryptPasswordEncoder bcrypt;
	
	/* ********** 메서드 ********** */
	@Override	// 관리자 로그인
	public Member login(Member inputMember) {
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		if(loginMember == null) return null;
		
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
		
		loginMember.setMemberPw(null);		// 클라이언트 쪽으로 비밀번호 들고가는거 아님
		return loginMember;
	}
	
	@Override	// 탈퇴한 회원 목록 조회
	public List<Member> selectWithdrawnMemberList() {
		return mapper.selectWithdrawnMemberList();
	}
	
	@Override	// 탈퇴 회원 복구
	public int restoreMember(int memberNo) {
		return mapper.restoreMember(memberNo);
	}
	
	@Override	// 삭제한 게시글 목록 조회
	public List<Board> deleteBoardList() {
		return mapper.deleteBoardList();
	}
	
	@Override	// 삭제한 게시글 복구
	public int restoreBoard(int boardNo) {
		return mapper.restoreBoard(boardNo);
	}
}

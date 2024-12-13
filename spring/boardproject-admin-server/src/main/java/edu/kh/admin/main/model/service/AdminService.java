package edu.kh.admin.main.model.service;

import java.util.List;

import edu.kh.admin.main.model.dto.Board;
import edu.kh.admin.main.model.dto.Member;

public interface AdminService {

	/** 관리자 로그인
	 * @param inputMember (memberEmail, memberPw)
	 * @return
	 */
	Member login(Member inputMember);

	/** 탈퇴한 회원 목록 조회
	 * @return
	 */
	List<Member> selectWithdrawnMemberList();

	/** 탈퇴 회원 복구
	 * @param memberNo
	 * @return
	 */
	int restoreMember(int memberNo);

	/** 삭제한 게시글 목록 조회
	 * @return
	 */
	List<Board> deleteBoardList();

	/** 삭제한 게시글 복구
	 * @param boardNo
	 * @return
	 */
	int restoreBoard(int boardNo);

}

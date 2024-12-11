package edu.kh.admin.main.model.service;

import edu.kh.admin.main.model.dto.Member;

public interface AdminService {

	/** 관리자 로그인
	 * @param inputMember (memberEmail, memberPw)
	 * @return
	 */
	Member login(Member inputMember);

}

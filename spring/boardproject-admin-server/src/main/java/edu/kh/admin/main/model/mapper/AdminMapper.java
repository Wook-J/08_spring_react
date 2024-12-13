package edu.kh.admin.main.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.admin.main.model.dto.Board;
import edu.kh.admin.main.model.dto.Member;

@Mapper
public interface AdminMapper {

	Member login(String memberEmail);

	List<Member> selectWithdrawnMemberList();

	int restoreMember(int memberNo);

	List<Board> deleteBoardList();

	int restoreBoard(int boardNo);

}

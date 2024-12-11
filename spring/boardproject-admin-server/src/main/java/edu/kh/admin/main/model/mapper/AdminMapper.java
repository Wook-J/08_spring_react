package edu.kh.admin.main.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.admin.main.model.dto.Member;

@Mapper
public interface AdminMapper {

	Member login(String memberEmail);

}

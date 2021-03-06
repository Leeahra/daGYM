package com.kh.dagym.main.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.dagym.admin.model.vo.Board;
import com.kh.dagym.common.Attachment;
import com.kh.dagym.trainer.model.vo.TrainerAttachment;
@Repository
public class HomeDAO {
	@Autowired
	private SqlSession sqlSession;

	public List<Attachment> eventViews() {
		return sqlSession.selectList("homeMapper.eventViews",null);
	}

	public List<Board> eventTitle() {
		return sqlSession.selectList("homeMapper.eventTitle",null);
	}

}

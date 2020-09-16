package com.kh.dagym.member.model.service;

import java.util.List;

import com.kh.dagym.common.PageInfo;
import com.kh.dagym.member.model.vo.Member;
import com.kh.dagym.member.model.vo.MyBoard;
import com.kh.dagym.member.model.vo.MyPass;
import com.kh.dagym.member.model.vo.MyReply;
import com.kh.dagym.member.model.vo.MyStudents;
import com.kh.dagym.trainer.model.vo.Review;

public interface MemberService {

	/** 회원가입 Service
	 * @param signUpMember
	 * @return result
	 */
	int signUp(Member signUpMember);

	/** 로그인 Service
	 * @param member
	 * @return loginMember
	 */
	Member login(Member member);

	
	/** 아이디 중복체크 Service
	 * @param memberId
	 * @return result
	 */
	int idDupCheck(String memberId);

	/** 회원 비밀번호 확인용 Service
	 * @param loginMember
	 * @return result
	 */
	int checkPwd(String memberPwd, int memberNo);
	
	
	/** 회원 정보 수정용 Service
	 * @param upMember
	 * @return result
	 */
	int updateMember(Member upMember);

	/** 회원 탈퇴용 Service
	 * @param memberPwd
	 * @param memberNo
	 * @return result
	 */
	int removeMember(String memberPwd, int memberNo);

	/** 내 댓글 Service
	 * @param pInfo 
	 * @param memberNo
	 * @return myReplyList
	 */
	List<MyReply> MyReplyList(int rerlyMemberNo, PageInfo pInfo);

	/** 내 댓글 페이징 처리 Service
	 * @param type
	 * @param cp
	 * @param rerlyMemberNo
	 * @return pInfo
	 */
	PageInfo replyPagination(int type, int cp, int rerlyMemberNo);

	/** 내 게시판 페이징 처리 Service
	 * @param type
	 * @param cp
	 * @param rerlyMemberNo
	 * @return pInfo
	 */
	PageInfo boardPagination(int type, int cp, int rerlyMemberNo);

	/** 내 게시글 Service
	 * @param rerlyMemberNo
	 * @param pInfo
	 * @return myBoardList
	 */
	List<MyBoard> myBoardList(int rerlyMemberNo, PageInfo pInfo);

	/** 이용권 및 결제정보 페이징 처리 Service
	 * @param type
	 * @param cp
	 * @param memberNo
	 * @return pInfo
	 */
	PageInfo myPassPagination(int type, int cp, int memberNo);
	
	/** 이용권 및 결제정보 Service
	 * @param rerlyMemberNo
	 * @param pInfo
	 * @return myBoardList
	 */
	List<MyPass> MyPassList(int memberNo, PageInfo pInfo);

	/** 아이디찾기 Service
	 * @param email
	 * @return id
	 */
	String findId(String email);

	/** 내 수강생 조회  페이징 처리Service
	 * @param type
	 * @param cp
	 * @param memberNo
	 * @return pInfo
	 */
	PageInfo myStudentsPagination(int type, int cp, int memberNo);

	/** 내 수강생 조회 Service
	 * @param memberNo
	 * @param pInfo
	 * @return myStudentsList
	 */
	List<MyStudents> myStudentsList(int memberNo, PageInfo pInfo);


	/** 비밀번호찾기 - 아이디확인 Service
	 * @param id
	 * @return checkId
	 */
	int checkId(Member member);


	/** 비밀번호 찾기 - 임시 비밀번호 저장 Service
	 * @param pw
	 * @return result
	 */
	int updatePw(Member member);

	/** 리뷰 등록
	 * @param review
	 * @return result
	 */
	int insertReview(Review review);

	/** 다음 리뷰번호 받아오기
	 * @return reviewNo
	 */
	int selectReviewNo();

	
	/** 리뷰 작성 여부확인
	 * @param memberNo
	 * @return result
	 */
	int checkReview(int memberNo);
}

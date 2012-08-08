package au.gov.nsw.records.digitalarchive.service;

import au.gov.nsw.records.digitalarchive.ORM.*;


import java.util.*;

public interface MemberService {
	
	public boolean addMember(Member member) throws Exception;
	
	public Member memberLogin(String login,String password) throws Exception;
		
	public boolean updateMember (Member member) throws Exception;
	
	public List<Member> browseMember() throws Exception;
	
	public boolean delMember(Integer id) throws Exception;
	
	public Member loadMember(Integer id) throws Exception;	

	public List<String> accountStatus (String login) throws Exception;	
	
	public boolean chkLoginName(String loginName) throws Exception;	
	
	public List<String> retrievePassword (String email) throws Exception;


}


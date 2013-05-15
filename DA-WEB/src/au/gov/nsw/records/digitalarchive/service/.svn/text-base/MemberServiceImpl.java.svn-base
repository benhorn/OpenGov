package au.gov.nsw.records.digitalarchive.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseLog;


public class MemberServiceImpl extends BaseLog implements MemberService
{

	@Override
	public boolean addMember(Member member) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(member);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class MemberServiceImpl:addMember()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}
	
	@Override
	public Member memberLogin(String login, String password) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Member member = null;
		try 
		{
			String hql = "SELECT a FROM Member AS a WHERE a.login=:login AND a.password=:password";
			Query query = session.createQuery(hql);
			query.setString("login", login.trim());
			query.setString("password", password.trim());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			member = (Member)query.uniqueResult();
			if (member != null && ("y").equals(member.getActivated().toLowerCase()))
			{
				member.setLoginTimes(Integer.valueOf(member.getLoginTimes() + 1));
				member.setLastLogin(new Date());
				session.update(member);
			}
			tx.commit();	
		} catch (Exception ex) {
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:memberLogin()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return member;
	}
	
	@Override
	public boolean updateMember(Member member) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(member);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:updateMember()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Member> browseMember() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Member> list = null;
		try{
			Query query = session.createQuery("FROM Member AS a ORDER BY a.id");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:browseMember()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	public boolean delMember(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Member member = (Member)session.load(Member.class, id);
			session.delete(member);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	public Member loadMember(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Member member = null;
		try{
			tx = session.beginTransaction();
			member = (Member)session.get(Member.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:loadMember()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return member;
	}
	
	@Override
	public Member loadMemberViaEmail(String email) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Member member = null;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Member AS m WHERE m.login=:email").setParameter("email", email);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				member = null;
			else
				member = (Member)query.uniqueResult();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:loadMemberViaEmail()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return member;
	}
	
	
	public List<String> accountStatus(String login) throws Exception
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<String> list = null;
		try 
		{
			String hql = "SELECT a.activated FROM Member AS a WHERE a.login=:login";
			Query query = session.createQuery(hql);
			query.setString("login", login);
			query.setMaxResults(1);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:accountStatus()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}

	@Override
	public boolean chkLoginName(String loginName) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = true;
		try{
			String hql = "SELECT count(*) FROM Member AS a WHERE a.login=:loginName";
			Query query = session.createQuery(hql);
			query.setString("loginName", loginName.trim());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			if (((Integer)query.uniqueResult()).intValue() > 0)
				result = false;
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:chkLoginName()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;
	}

	@Override
	public List<String> retrievePassword(String email) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<String> list = null;
		try{
			String hql = "SELECT password FROM Member AS a WHERE a.login=:email";
			Query query = session.createQuery(hql);
			query.setString("email", email.trim());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class MemberServiceImpl:retrievePassword()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

}
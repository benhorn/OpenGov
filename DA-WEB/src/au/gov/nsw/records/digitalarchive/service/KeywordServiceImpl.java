package au.gov.nsw.records.digitalarchive.service;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Keyword;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class KeywordServiceImpl extends BaseLog implements KeywordService{

	@Override
	public boolean addKeyword(Keyword keyword) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(keyword);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class KeywordServiceImpl:addKeyword()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public List<Keyword> browseKeywords() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Keyword> list = null;
		try{
			Query query = session.createQuery("FROM Keyword AS a ORDER BY a.keywordId ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordServiceImpl:browseKeywords()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public Keyword loadKeyword(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Keyword keyword = null;
		try{
			tx = session.beginTransaction();
			keyword = (Keyword)session.get(Keyword.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordServiceImpl:loadKeyword()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return keyword;
	}

	@Override
	public List<Keyword> loadKeywordViaPublication(String pubID) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Keyword> list = null;
		try 
		{
			String hql = "SELECT k FROM Keyword as k LEFT JOIN k.keywordPublications " +
						 "as kp WHERE kp.publication.publicationId=:pubID";
			Query query = session.createQuery(hql);
			query.setString("pubID", pubID.trim());
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordServiceImpl:loadKeywordViaPublication()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}
	
	@Override
	public List<Integer> browseKeywordID(String keyword) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Integer> list = null;
		try 
		{
			String hql = "SELECT a.keywordId FROM Keyword AS a WHERE a.keyword=:keyword";
			Query query = session.createQuery(hql);
			query.setString("keyword", keyword.trim());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordServiceImpl:browseKeywordID()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}
	
	@Override
	public boolean chkKeyword(String keyword) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = true;
		try{
			String hql = "SELECT count(*) FROM Keyword AS a WHERE a.keyword=:keyword";
			Query query = session.createQuery(hql);
			query.setString("keyword", keyword.toLowerCase());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			if (((Integer)query.uniqueResult()).intValue() > 0)
				result = false;
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordServiceImpl:chkKeyword()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;
	}

}

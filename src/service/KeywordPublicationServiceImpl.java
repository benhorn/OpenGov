package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.KeywordPublication;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class KeywordPublicationServiceImpl extends BaseLog implements KeywordPublicationService{

	@Override
	public boolean addKeywordPublication(KeywordPublication keyPub) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(keyPub);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class KeywordPublicationServiceImpl:addKeywordPublication()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public List<KeywordPublication> browseKeywordPublications() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<KeywordPublication> list = null;
		try{
			Query query = session.createQuery("FROM KeywordPublication ORDER BY keyPubId ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordPublicationServiceImpl:browseKeywordPublications()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public KeywordPublication loadKeywordPublication(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		KeywordPublication keywordPublication = null;
		try{
			tx = session.beginTransaction();
			keywordPublication = (KeywordPublication)session.get(KeywordPublication.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordPublicationServiceImpl:loadKeywordPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return keywordPublication;
	}

	@Override
	public boolean deleteKeyPubViaPublication(Publication publication)
											throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			Query query = session.createQuery("DELETE KeywordPublication WHERE publication=:publication").setParameter("publication", publication);
			int update = query.executeUpdate();
			if (update == 0)
				{status = false;}
			else
				{status = true;}
			
				tx.commit();
			
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class KeywordPublicationServiceImpl:deleteKeyPubViaPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}
}

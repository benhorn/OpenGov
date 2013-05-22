package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.Agency;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class PublicationServiceImpl extends BaseLog implements PublicationService {

	@Override
	public boolean addPublication(Publication publication) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(publication);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class PublicationServiceImpl: addPublication()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public boolean updatePublication(Publication publication) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(publication);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:updatePublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Publication> browsePublication() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		try{
			Query query = session.createQuery("FROM Publication AS a WHERE a.status='submitted' ORDER BY a.lastUpdated DESC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:browsePublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}
	
	@Override
	public List<Publication> allPublication(int begin, int end) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		try{
			Query query = session.createQuery("FROM Publication");
			query.setFirstResult(begin);
			query.setMaxResults(end);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:allPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Publication> browsePublication(Member member) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		try{
			Query query = session.createQuery("FROM Publication AS a WHERE a.member=:member ORDER BY a.lastUpdated DESC").setParameter("member", member);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:browsePublication(Member member)\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public List<Publication> browsePublication(Member member, Agency agency)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delPublication(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Publication publication = (Publication)session.load(Publication.class, id);
			session.delete(publication);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:delPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public Publication loadPublication(Integer id) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Publication publication = null;
		try{
			tx = session.beginTransaction();
			publication = (Publication)session.get(Publication.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:loadPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return publication;
	}

	public static void main(String args[]) throws Exception
	{
		PublicationService ps = new PublicationServiceImpl();
		ps.allPublication(0, 100);
	}
	
}

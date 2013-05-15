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
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class PublicationServiceImpl extends BaseLog implements PublicationService {

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
			logger.info("In class PublicationServiceImpl:addPublication()\n");
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
			Query query = session.createQuery("FROM Publication AS a ORDER BY a.lastUpdated DESC");
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Publication> lastestPublication() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		try{
			Query query = session.createQuery("FROM Publication AS p WHERE p.status='published' ORDER BY p.dateApproved DESC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:lastestPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Publication> mostPopularPublication() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		try{
			Query query = session.createQuery("FROM Publication AS p WHERE p.status='published' ORDER BY p.popularity DESC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:mostPopularPublication()\n");
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
	public int countTotalPages(Publication publication) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int pages = 0;
		try{
			tx = session.beginTransaction();
			String hql = "SELECT SUM(f.pages) FROM UploadedFile AS f WHERE f.publication=:publication";
			Query query = session.createQuery(hql).setParameter("publication", publication);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				pages = 0;
			else
				pages = ((Integer)query.uniqueResult()).intValue();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:countTotalPages()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return pages;
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
	public List<UploadedFile> listFilesViaPublication(Publication pub) throws Exception {
		Session session = HibernateUtil.getSession();
		List<UploadedFile> uList = null;
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String hql = "FROM UploadedFile AS f WHERE f.publication=:pub";
			Query query = session.createQuery(hql).setParameter("pub", pub);
			uList = query.list();
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublicationServiceImpl:listFilesViaPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return uList;
	}
	
}

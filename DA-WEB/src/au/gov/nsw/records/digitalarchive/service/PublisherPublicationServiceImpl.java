package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.PublisherPublication;
import au.gov.nsw.records.digitalarchive.base.BaseLog;


public class PublisherPublicationServiceImpl extends BaseLog implements PublisherPublicationService
{

	@Override
	public boolean addPublisherPublication(
			PublisherPublication publisherPublication) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(publisherPublication);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class PublisherPublicationServiceImpl:addPublisherPublication()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public List<PublisherPublication> browsePublisherPublication()
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<PublisherPublication> list = null;
		try{
			Query query = session.createQuery("FROM PublisherPublication AS a ORDER BY a.publisherPublicationId ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublisherPublicationServiceImpl:browsePublisherPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public List<FullAgencyList> loadPublisherViaPublication(String pubID)
														 throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<FullAgencyList> list = null;
		try 
		{
			String hql = "SELECT f FROM FullAgencyList AS f INNER JOIN " +
						 "f.publisherPublications AS fp WHERE fp.publication.publicationId=:pubID";
			Query query = session.createQuery(hql);
			query.setString("pubID", pubID.trim());
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class PublisherPublicationServiceImpl:loadPublisherViaPublication()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}
	
	@Override
	public boolean deletePublisherViaPublication(Publication publication) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery("DELETE PublisherPublication WHERE publication=:publication").setParameter("publication", publication);
			int update = query.executeUpdate();
			if (update == 0)
				{status = false;}
			else
				{status = true;}
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class PublisherPublication:deletePublisherViaPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public PublisherPublication loadPublisherPublication(Integer id)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		PublisherPublication publisherPublication = null;
		try
		{
			tx = session.beginTransaction();
			publisherPublication = (PublisherPublication)session.get(PublisherPublication.class, id);
			tx.commit();
		}catch(Exception ex)
		{
			if(tx!=null)tx.rollback();
			logger.info("In class PublisherPublicationServiceImpl:loadPublisherPublication()\n");
			ex.printStackTrace();
		}finally
		{
			HibernateUtil.closeSession();
		}	
		return publisherPublication;
	}

}

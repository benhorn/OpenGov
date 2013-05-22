package au.gov.nsw.records.digitalarchive.service;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class AgencyPublicationServiceImpl extends BaseLog implements AgencyPublicationService{

	@Override
	public boolean addAgencyPublication(AgencyPublication agencyPublication)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(agencyPublication);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class AgencyPublicationServiceImpl:addAgencyPublication()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgencyPublication> browseAgencyPublications() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<AgencyPublication> list = null;
		try{
			Query query = session.createQuery("FROM AgencyPublication AS a ORDER BY a.agencyPublicationId ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyPublicationServiceImpl:browseAgencyPublications()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}
	
	@Override
	public List<Publication> loadPublicationViaFullAgency(FullAgencyList fList) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Publication> list = null;
		
		try{
			Query query = session.createQuery("SELECT a.publication FROM AgencyPublication AS a " +
											  "WHERE a.fullAgencyList=:fList ORDER BY a.agencyPublicationId DESC").setParameter("fList", fList);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyPublicationServiceImpl:loadPublicationViaFullAgency()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public boolean deleteAgencyPubViaPublication(Publication publication)
			throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery("DELETE AgencyPublication WHERE publication=:publication").setParameter("publication", publication);
			int update = query.executeUpdate();
			if (update == 0)
				{status = false;}
			else
				{status = true;}
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyPublicationServiceImpl:deleteAgencyPubViaPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public AgencyPublication loadAgencyPublication(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		AgencyPublication agencyPublication = null;
		try
		{
			tx = session.beginTransaction();
			agencyPublication = (AgencyPublication)session.get(AgencyPublication.class, id);
			tx.commit();
		}catch(Exception ex)
		{
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyPublicationServiceImpl:loadAgencyPublication()\n");
			ex.printStackTrace();
		}finally
		{
			HibernateUtil.closeSession();
		}	
		return agencyPublication;
	}
}

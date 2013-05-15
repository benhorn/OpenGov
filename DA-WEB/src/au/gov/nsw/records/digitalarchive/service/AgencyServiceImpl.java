package au.gov.nsw.records.digitalarchive.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.Agency;
import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class AgencyServiceImpl extends BaseLog implements AgencyService{

	@Override
	public boolean addAgency(Agency agency) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(agency);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class AgencyServiceImpl:addAgency()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public boolean updateAgency(Agency agency) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(agency);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyServiceImpl:updateAgency()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@Override
	public boolean delAgency(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Agency agency = (Agency)session.load(Agency.class, id);
			session.delete(agency);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyServiceImpl:delAgency()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public Agency loadAgency(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Agency agency = null;
		try{
			tx = session.beginTransaction();
			agency = (Agency)session.get(Agency.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class AgencyServiceImpl:loadAgency()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return agency;
	}

}

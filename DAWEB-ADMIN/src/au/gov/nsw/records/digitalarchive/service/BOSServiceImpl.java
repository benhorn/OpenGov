package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.*;

import au.gov.nsw.records.digitalarchive.ORM.*;
import au.gov.nsw.records.digitalarchive.base.BaseLog;
import au.gov.nsw.records.digitalarchive.service.BOSService;

public class BOSServiceImpl extends BaseLog implements BOSService{

	@Override
	public List<Agencies> browseAgency() throws Exception {
		Session session = FilbertHibernateUtil.getSession();
		Transaction tx = null;
		List<Agencies> list = null;
		try{
			Query query = session.createQuery("FROM Agencies ORDER BY agencyTitle ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class BOSServiceImpl:browseAgency()\n");
			ex.printStackTrace();
		}finally{
			FilbertHibernateUtil.closeSession();
		}	
		return list;
		
	}	

}

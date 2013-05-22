package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.*;

import au.gov.nsw.records.digitalarchive.ORM.*;
import au.gov.nsw.records.digitalarchive.service.FilbertService;

public class FilbertServiceImpl implements FilbertService{

	@Override
	public List browseAgency() throws Exception {
		Session session = FilbertHibernateUtil.getSession();
		Transaction tx = null;
		List list = null;
		try{
			Query query = session.createQuery("FROM Agencies WHERE endDate is NULL ORDER BY agencyTitle ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			ex.printStackTrace();
		}finally{
			FilbertHibernateUtil.closeSession();
		}	
		return list;
		
	}	

}

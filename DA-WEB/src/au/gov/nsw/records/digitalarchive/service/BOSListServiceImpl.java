package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class BOSListServiceImpl extends BaseLog implements BOSListService {

	@Override
	public boolean addNewBosList(BosList bosList) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(bosList);
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx !=  null)tx.rollback();
			logger.info("In class BOSListServiceImpl:addNewBosList()\n");
			e.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public List<BosList> browseBosList() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<BosList> list = null;
		try{
			Query query = session.createQuery("FROM BosList ORDER BY agencyNumber ASC");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class BOSListServiceImpl:browseBosList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public boolean updateBosList(BosList bosList) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(bosList);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class BOSListServiceImpl:updateBosList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@Override
	public BosList loadBosList(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		BosList bosList = null;
		try{
			tx = session.beginTransaction();
			bosList = (BosList)session.get(BosList.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class BOSListServiceImpl:loadBosList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return bosList;
	}

}

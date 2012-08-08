package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.TempList;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class TempListServiceImpl extends BaseLog implements TempListService {

	@Override
	public boolean addTempList(TempList tempList) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(tempList);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class TempListServiceImpl:addTempList()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public boolean updateTempList(TempList tempList) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(tempList);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class TempListServiceImpl:updateTempList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TempList> browseTempList() throws Exception {
		Session session = HibernateUtil.getSession();
		List<TempList> tempList = session.createCriteria(TempList.class).add(Restrictions.ne("name","")).list();
		return tempList;				
	}

	@Override
	public boolean delTempList(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			TempList tempList = (TempList)session.load(TempList.class, id);
			session.delete(tempList);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class TempListServiceImpl:delTempList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public TempList loadTempList(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		TempList tempList = null;
		try{
			tx = session.beginTransaction();
			tempList = (TempList)session.get(TempList.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class TempListServiceImpl:loadTempList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return tempList;
	}

}

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
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseLog;

public class FullAgencyListServiceImpl extends BaseLog implements FullAgencyListService{

	@Override
	public boolean addFullAgencyList(FullAgencyList fullAgencyList)
									throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(fullAgencyList);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:addFullAgencyList()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public boolean updateFullAgencyList(FullAgencyList fullAgencyList)
										throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(fullAgencyList);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:updateFullAgencyList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FullAgencyList> browseFullAgencyList() throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<FullAgencyList> list = null;
		try{
			Query query = session.createQuery("FROM FullAgencyList AS a ORDER BY a.fullAgencyListId");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:browseFullAgencyList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public boolean deleteFullAgencyList(Integer id) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			FullAgencyList fullList = (FullAgencyList)session.load(FullAgencyList.class, id);
			session.delete(fullList);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:deleteFullAgencyList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}
	
	@Override
	public List<AgencyPublication> loadAgencyWithPublication() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<AgencyPublication> list = null;
		try 
		{
			String hql = "SELECT ap FROM AgencyPublication AS ap INNER JOIN " +
						 "ap.publication WHERE ap.publication.status='published'";
			Query query = session.createQuery(hql);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:loadAgencyWithPublicaion()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;			
	}
	
//	@Override
//	public List<FullAgencyList> loadAgencyViaPublication(String pubID)
//			throws Exception {
//		Session session = HibernateUtil.getSession();
//		Transaction tx = null;
//		List<FullAgencyList> list = null;
//		try 
//		{
//			String hql = "SELECT f FROM FullAgencyList AS f INNER JOIN " +
//						 "f.agencyPublications AS ap WHERE ap.publication.publicationId=:pubID";
//			Query query = session.createQuery(hql);
//			query.setString("pubID", pubID.trim());
//			tx = session.beginTransaction();
//			list = query.list();
//			tx.commit();	
//		} catch (Exception e) {
//			if(tx!=null)tx.rollback();
//			logger.info("In class FullAgencyListServiceImpl:loadAgencyViaPublication()\n");
//			e.printStackTrace();
//		}finally{
//			HibernateUtil.closeSession();
//		}
//		return list;		
//	}
	
	@Override
	public List<FullAgencyList> loadAgencyViaPublication(String pubID)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<FullAgencyList> list = null;
		try 
		{
			String hql = "SELECT f FROM FullAgencyList AS f INNER JOIN " +
						 "f.agencyPublications AS ap WHERE ap.publication.publicationId=:pubID";
			Query query = session.createQuery(hql);
			query.setString("pubID", pubID.trim());
			//query.setMaxResults(50);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:loadAgencyViaPublication()\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}
	
	
	@Override
	public FullAgencyList loadFullAgencyList(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		FullAgencyList fullList = null;
		try{
			tx = session.beginTransaction();
			fullList = (FullAgencyList)session.get(FullAgencyList.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:loadFullAgencyList()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return fullList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> browseAgencyID(String agencyName) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Integer> list = null;
		try 
		{
			String hql = "SELECT a.fullAgencyListId FROM FullAgencyList AS a WHERE a.agencyName=:agencyName";
			Query query = session.createQuery(hql);
			query.setString("agencyName", agencyName.trim());
			query.setMaxResults(1);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();	
		} catch (Exception e) {
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:browseAgencyID(String agencyName)\n");
			e.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return list;		
	}
	
	@Override
	public boolean chkAgencyName(String agencyName) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = true;
		try{
			String hql = "SELECT count(*) FROM FullAgencyList AS a WHERE a.agencyName=:agencyName";
			Query query = session.createQuery(hql);
			query.setString("agencyName", agencyName);
			query.setMaxResults(1);
			tx = session.beginTransaction();
			if (((Integer)query.uniqueResult()).intValue() > 0)
				result = false;
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:chkAgencyName()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FullAgencyList> list4AutoComplete(String agencyName) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<FullAgencyList> list = null;
		try{
			Query query = session.createQuery("FROM FullAgencyList AS a WHERE a.agencyName LIKE '%" + agencyName + "%'");
			query.setMaxResults(15);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:list4AutoComplete()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public String loadAgencyNumberViaMember(String memberID) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		String agencyNumber = "";
		Integer temp = null;
		try{
			Query query = session.createQuery("SELECT a.fullAgencyListId FROM FullAgencyList AS a WHERE a.members.memberId=:memberID").setString("memberID", memberID);
			tx = session.beginTransaction();
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				agencyNumber="";
			else
				temp = ((Integer)query.uniqueResult()).intValue();
			agencyNumber = temp.toString();
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FullAgencyListServiceImpl:loadAgencyNumberViaMember()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return agencyNumber;
	}
	
	
}

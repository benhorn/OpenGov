package au.gov.nsw.records.digitalarchive.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.Archivist;
import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseLog;


public class ArchivistServiceImpl extends BaseLog implements ArchivistService
{

	@Override
	public boolean addArchivist(Archivist archivist) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(archivist);
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx !=  null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:addArchivist()\n");
			e.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
	}

	@Override
	public Archivist archivistLogin(String login, String password)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Archivist archivist = null;
		try 
		{
			String hql = "SELECT a FROM Archivist AS a WHERE a.loginName=:login AND a.loginPassword=:password";
			Query query = session.createQuery(hql);
			query.setString("login", login);
			query.setString("password", password);
			query.setMaxResults(1);
			tx = session.beginTransaction();
			archivist = (Archivist)query.uniqueResult();
			if (archivist != null)
			{
				archivist.setLoginTimes(Integer.valueOf(archivist.getLoginTimes()) + 1);
				archivist.setLastLogin(new Date());
				session.update(archivist);
			}
			tx.commit();	
		} catch (Exception ex) {
			if(tx!=null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:archivistLogin()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}
		return archivist;
	}

	@Override
	public boolean updateArchivist(Archivist archivist) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(archivist);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:updateArchivist()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Archivist> browseArchivist() throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<Archivist> list = null;
		try{
			Query query = session.createQuery("from Archivist as a order by a.archivistId");
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:browseArchivist()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public boolean delArchivist(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			Archivist archivist = (Archivist)session.load(Archivist.class, id);
			session.delete(archivist);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:delArchivist()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public Archivist loadArchivist(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Archivist archivist = null;
		try{
			tx = session.beginTransaction();
			archivist = (Archivist)session.get(Archivist.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class ArchivistServiceImpl:loadArchivist()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return archivist;
	}
}
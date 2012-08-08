package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseLog;


public class FileServiceImpl extends BaseLog implements FileService
{

	@Override
	public boolean addFile(UploadedFile uploadedFile) throws Exception 
	{
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try {
			tx = session.beginTransaction();
			session.save(uploadedFile);
			tx.commit();
			result = true;
		} catch (Exception ex) {
			if (tx !=  null)tx.rollback();
			logger.info("In class FileServiceImpl:addFile()\n");
			ex.printStackTrace();
		} finally
		{
			HibernateUtil.closeSession();
		}
		return result;
		
	}

	@Override
	public boolean updateFile(UploadedFile uploadedFile) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			session.update(uploadedFile);
			tx.commit();
			result=true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:updateFile()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return result;	
	}
	
	@Override
	public List<UploadedFile> browseFiles(Publication publication) throws Exception {
		
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		List<UploadedFile> list = null;
		try{
			Query query = session.createQuery("FROM UploadedFile AS a WHERE a.publication=:publication ORDER BY a.dateUploaded DESC").setParameter("publication", publication);
			tx = session.beginTransaction();
			list = query.list();
			tx.commit();
			if (!Hibernate.isInitialized(list))Hibernate.initialize(list);
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:browseFiles()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return list;
	}

	@Override
	public boolean deleteFileViaPublication(Publication publication) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			Query query = session.createQuery("DELETE UploadedFile WHERE publication=:publication").setParameter("publication", publication);
			int update = query.executeUpdate();
			if (update == 0)
				status = false;
			else
				status = true;
			
				tx.commit();
			
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:deleteFilesViaPublication()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}

	@Override
	public boolean deleteFile(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		boolean status = false;
		try{
			tx = session.beginTransaction();
			UploadedFile uploadedFile = (UploadedFile)session.load(UploadedFile.class, id);
			session.delete(uploadedFile);
			tx.commit();
			status = true;
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:deleteFile()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return status;
	}	
	
	@Override
	public int countFiles(String hql) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int count = 0;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setMaxResults(1);
			count = ((Integer)query.uniqueResult()).intValue();
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:countFiles()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return count;
	}

	@Override
	public boolean deletePhysicalFiles(Publication publication) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UploadedFile loadFile(Integer id) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		UploadedFile uploadedFile = null;
		try{
			tx = session.beginTransaction();
			uploadedFile = (UploadedFile)session.get(UploadedFile.class, id);
			tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:loadFile()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return uploadedFile;
	}

	
}
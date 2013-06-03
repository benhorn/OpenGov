package au.gov.nsw.records.digitalarchive.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.gov.nsw.records.digitalarchive.ORM.HibernateUtil;
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
			Query query = session.createQuery("FROM UploadedFile AS a WHERE a.publication=:publication ORDER BY a.fileOrder ASC").setParameter("publication", publication);
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
		boolean status = false;
		try{
			Query query = session.createQuery("DELETE UploadedFile WHERE publication=:publication").setParameter("publication", publication);
			int update = query.executeUpdate();
			if (update == 0)
				status = false;
			else
				status = true;			
		}catch(Exception ex){
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
	public boolean deletePhysicalFiles(UploadedFile inputFile) throws Exception {
		String fileLocation = inputFile.getInboxUrl().trim();
		boolean result = false;
		File targetFile = new File(fileLocation);
		
		if (!targetFile.exists())
		      logger.info("In class FileServiceImpl:deletePhysicalFiles() - no such file or directory: " + inputFile.getFileName().trim());

		if (!targetFile.canWrite())
		      logger.info("In class FileServiceImpl:deletePhysicalFiles() - write protected: " + inputFile.getFileName().trim());

		    // If it is a directory, make sure it is empty
		if (targetFile.isDirectory()) {
		      String[] files = targetFile.list();
		      if (files.length > 0)
		    	  logger.info("In class FileServiceImpl:deletePhysicalFiles() - directory not empty: " + inputFile.getFileName().trim());
		}
		targetFile.setWritable(true);
		result = targetFile.delete();		
		logger.info("In class FileServiceImpl:deletePhysicalFiles() - deleting file " + inputFile.getFileName().trim() + " " + result);
		return result;
	}
	
	@Override
	public void cleanUpInbox(UploadedFile inputFile)
	{
		File inboxFile = new File(inputFile.getInboxUrl());
		File inboxDIR =  new File(inboxFile.getAbsolutePath().substring(0, inboxFile.getAbsolutePath().length() - inputFile.getFileName().length()));
		inboxDIR.setWritable(true);
		try {	
			FileUtils.forceDelete(inboxDIR);
		}
		catch (IOException e) {
			logger.info("In class FileServiceImpl:cleanUpInbox()\n");
			try {
				FileChannel channel = new RandomAccessFile(inboxFile, "rw").getChannel();
				FileLock lock = channel.tryLock();
				 if(lock == null)
		            {
		                // File is lock by other application
		                channel.close();
		                throw new RuntimeException("\n\n\nTwo instance cant run at a time.\n\n");
		            }
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("File not found");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				System.out.println("IOException");
			}
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean deletePhysicalFilesViaPub(Publication publication) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Publication findPubViaFile(Integer id)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Publication publication = null;
		try{
			tx = session.beginTransaction();
			String hql = "SELECT f.publication FROM UploadedFile AS f WHERE f.fileId=:id";
			Query query = session.createQuery(hql).setParameter("id", id);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				publication = null;
			else
				publication = (Publication)query.uniqueResult();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:findPubViaFile()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return publication;
	
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

	@Override
	public UploadedFile loadFileViaOrder(Integer fileOrder, Integer pubID)
										 throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		UploadedFile uploadedFile = null;
		try{
			tx = session.beginTransaction();
			String hql = "FROM UploadedFile AS f WHERE f.fileOrder=:fileOrder AND f.publication.publicationId=:pubID";
			Query query = session.createQuery(hql).setParameter("fileOrder", fileOrder).setParameter("pubID", pubID);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				uploadedFile = null;
			else
				uploadedFile = (UploadedFile)query.uniqueResult();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:loadFileViaOrder()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return uploadedFile;
	}
	
	@Override
	public UploadedFile loadFileViaOrder(Integer fileOrder, Integer pubID, Integer fileID)
			throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		UploadedFile uploadedFile = null;
		try{
			tx = session.beginTransaction();
			String hql = "FROM UploadedFile AS f WHERE f.fileOrder=:fileOrder AND f.publication.publicationId=:pubID AND f.fileId<>:fileID";
			Query query = session.createQuery(hql).setParameter("fileOrder", fileOrder).setParameter("pubID", pubID).setParameter("fileID", fileID);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				uploadedFile = null;
			else
				uploadedFile = (UploadedFile)query.uniqueResult();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:loadFileViaOrder()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return uploadedFile;
	}
	
	@Override
	public int countFiles(Integer pubID) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int count = 0;
		try{
			tx = session.beginTransaction();
			String hql = "SELECT COUNT(*) FROM UploadedFile AS f WHERE f.publication.publicationId=:pubID";
			Query query = session.createQuery(hql).setParameter("pubID", pubID);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				count=0;
			else
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
	public int maxFileOrder(String pubID) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int order = 0;
		try{
			tx = session.beginTransaction();
			String hql = "SELECT MAX(f.fileOrder) FROM UploadedFile AS f WHERE f.publication.publicationId=:pubID";
			Query query = session.createQuery(hql).setParameter("pubID", pubID);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				order=0;
			else
				order = ((Integer)query.uniqueResult()).intValue();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:maxFileOrder()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return order;
	}

	@Override
	public int minFileOrder(String pubID) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		int order = 0;
		try{
			tx = session.beginTransaction();
			String hql = "SELECT MIN(f.fileOrder) FROM UploadedFile AS f WHERE f.publication.publicationId=:pubID";
			Query query = session.createQuery(hql).setParameter("pubID", pubID);
			query.setMaxResults(1);
			if (query.uniqueResult() == null)
				order=0;
			else
				order = ((Integer)query.uniqueResult()).intValue();
				tx.commit();
		}catch(Exception ex){
			if(tx!=null)tx.rollback();
			logger.info("In class FileServiceImpl:minFileOrder()\n");
			ex.printStackTrace();
		}finally{
			HibernateUtil.closeSession();
		}	
		return order;
	}

	
}
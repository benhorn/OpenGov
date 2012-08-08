package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.FileUploadForm;


public class FileUploadAction extends Action {

    private final static String SUCCESS = "success";
    private final static String FAILED = "failed";
    private static final double BASE = 1024, KB = BASE, MB = KB * BASE, GB = MB * BASE;
    private static final DecimalFormat df = new DecimalFormat("#.##");


    public ActionForward execute(ActionMapping mapping, 
    							 ActionForm form,
            					 HttpServletRequest request, 
            					 HttpServletResponse response)
            					 throws Exception 
    {
    	FileUploadForm uploadForm = (FileUploadForm) form;
    	FileService fs = new FileServiceImpl();
        PublicationService ps = new PublicationServiceImpl();
    	FileOutputStream outputStream = null;
        FormFile formFile = null;
		ActionForward forward = null;
		String fullPath = "";
		Integer id = null;
		if (request.getParameter("id") != null)
		{
			id = new Integer(request.getParameter("id"));
		}
        try 
        {
        	Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");
			}else
			{	
        	
				int totalFiles;
				String fileDownloadLink = "";
				formFile = uploadForm.getFile();
				Publication publication = ps.loadPublication(id);
				fullPath = getServlet().getServletContext().getRealPath("") + "/" + publication.getPublicationId() + "/" + formFile.getFileName();
				boolean status = false;
				UploadedFile uploadedFile = new UploadedFile();
				uploadedFile.setFileName(formFile.getFileName().trim());
				uploadedFile.setUploadedTo(fullPath);
				uploadedFile.setContentType(formFile.getContentType());
				uploadedFile.setSize(formatSize(Double.parseDouble((Integer.toString(formFile.getFileSize())))));
				uploadedFile.setDateUploaded(new Date());
				uploadedFile.setUploadedBy(member.getFirstname() + " " + member.getLastname());
				uploadedFile.setIpAddress(InetAddress.getLocalHost().getHostAddress().toString());
				uploadedFile.setPublication(publication);
				status = fs.addFile(uploadedFile);
				if (status)
				{	
					fileDownloadLink = "<a href=\"/DA-WEB/pub.do?method=downloadFile&id=" + uploadedFile.getFileId() +"\">" + uploadedFile.getFileName() + "</a>";
					uploadedFile = fs.loadFile(uploadedFile.getFileId());
					uploadedFile.setDownloadLink(fileDownloadLink);
					fs.updateFile(uploadedFile);
					/*System.out.println("File name: " + uploadedFile.getFileName());
					System.out.println("File ID: " + uploadedFile.getFileId());*/
					totalFiles = fs.countFiles("select count(*) from UploadedFile as f where f.publication.publicationId="+id);
					publication.setTotalFiles(Integer.toString(totalFiles));
					publication.setLastUpdated(new Date());
					ps.updatePublication(publication);
					new File(getServlet().getServletContext().getRealPath("") + "/" + publication.getPublicationId()).mkdirs();
					outputStream = new FileOutputStream(new File(fullPath));
					outputStream.write(formFile.getFileData());
					forward = mapping.findForward(SUCCESS);
				}else
				{
					forward = mapping.findForward(FAILED);
				}
			}
        }catch (Exception e) 
        {
			e.printStackTrace();
        }finally 
        {
            if (outputStream != null) 
            {
                outputStream.close();
            }
        }
        return forward;
    }


    private static String formatSize(double size) {
        if(size >= GB) {
            return df.format(size/GB) + " GB";
        }
        if(size >= MB) {
            return df.format(size/MB) + " MB";
        }
        if(size >= KB) {
            return df.format(size/KB) + " KB";
        }
        return "" + (int)size + " bytes";
    }

}

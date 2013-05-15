package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

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
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.base.NYTimesJSONFormat;
import au.gov.nsw.records.digitalarchive.search.TikaWrapper;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.FileUploadForm;
import au.gov.nsw.records.digitalarchive.system.HostNameFinder;
import au.gov.nsw.records.digitalarchive.system.URLGenerator;

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
		String InboxURL = "";
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
				String cmsDownloadLink = "";
				String UID = UUID.randomUUID().toString().trim().replaceAll("-", "");
				formFile = uploadForm.getFile();
				InboxURL = Constants.INBOX + UID + File.separator + formFile.getFileName();
								
				Publication publication = ps.loadPublication(id);
				
				boolean status = false;
				UploadedFile uploadedFile = new UploadedFile();
				uploadedFile.setFileName(formFile.getFileName().trim());
				uploadedFile.setInboxUrl(InboxURL);
				uploadedFile.setUid(UID.trim());
				uploadedFile.setContentType(formFile.getContentType().trim());
				uploadedFile.setSize(formatSize(Double.parseDouble((Integer.toString(formFile.getFileSize())))));
				uploadedFile.setFileOrder(fs.maxFileOrder(publication.getPublicationId().toString()) + 1);
				uploadedFile.setDateUploaded(new Date());
				uploadedFile.setUploadedBy(member.getFirstname() + " " + member.getLastname());
				uploadedFile.setIpAddress(request.getRemoteAddr());
				uploadedFile.setPublication(publication);
				status = fs.addFile(uploadedFile);
				if (status)
				{	
					fileDownloadLink = "<a href=\"/download/" + uploadedFile.getFileId() +"\">" + uploadedFile.getFileName() + "</a>";
					cmsDownloadLink = "<a href=\"/download/" + uploadedFile.getFileId() +"\">" + uploadedFile.getFileName() + "</a>";
					uploadedFile = fs.loadFile(uploadedFile.getFileId());
					uploadedFile.setDownloadLink(fileDownloadLink);
					uploadedFile.setCmsDownloadLink(cmsDownloadLink);
					
					totalFiles = fs.countFiles(id);
					publication.setTotalFiles(Integer.toString(totalFiles));
					publication.setLastUpdated(new Date());
					ps.updatePublication(publication);
					
					new File(Constants.INBOX + UID).mkdirs();
					outputStream = new FileOutputStream(new File(InboxURL));
					outputStream.write(formFile.getFileData());
					outputStream.close();
					
					TikaWrapper ta = new TikaWrapper();
					Integer pageNumber = Integer.parseInt(ta.getPageNumber(uploadedFile.getInboxUrl()));
					uploadedFile.setPages(pageNumber);
					
					String hostURL = new URLGenerator(new HostNameFinder().getMyCanonicalHostName()).getHostURL();
					
					NYTimesJSONFormat NYTimesJSON = new NYTimesJSONFormat(uploadedFile.getFileName().substring(0, uploadedFile.getFileName().length()-4),
																		  uploadedFile.getFileName().substring(0, uploadedFile.getFileName().length()-4),
							  										      pageNumber.toString(),
							  										      "",
							  										      hostURL.concat("doc_opener?uid=").concat(uploadedFile.getUid()),
							  										      hostURL.concat("publication/").concat(uploadedFile.getFileName()),
							  										      hostURL.concat("txt_opener?uid=").concat(uploadedFile.getUid()),
							  										      hostURL.concat("thumb?uid=").concat(uploadedFile.getUid()),
							  										      hostURL.concat("txt_opener?uid=").concat(uploadedFile.getUid())+"&search.json?q={query}",
							  										      hostURL.concat("txt_opener?uid=").concat(uploadedFile.getUid())+"&counter={page}",
							  										      hostURL.concat("pdf_image_opener?uid=").concat(uploadedFile.getUid())+"&counter={page}");

					
					uploadedFile.setReaderJson(NYTimesJSON.toJSONString());
					fs.updateFile(uploadedFile);

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

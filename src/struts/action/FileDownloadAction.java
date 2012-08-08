package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;


public class FileDownloadAction extends DownloadAction{

	@Override
	protected StreamInfo getStreamInfo(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request, 
									   HttpServletResponse response) throws Exception 
	{
		String id = request.getParameter("id");
		
		UploadedFile uploadedFile = new UploadedFile();
		FileService fs = new FileServiceImpl();
		uploadedFile = fs.loadFile(Integer.parseInt(id));
		
		String filename= "c:\\tomcat\\webapps\\DA-WEB\\" + uploadedFile.getPublication().getPublicationId() + "\\" + uploadedFile.getFileName();
		System.out.println("File name:" + filename);
		response.setHeader("Content-disposition","attachment; filename=" + uploadedFile.getFileName().trim().replace(" ", "_"));
		String contentType = uploadedFile.getContentType();
		
		return new FileStreamInfo(contentType, new File(filename));
	}

}

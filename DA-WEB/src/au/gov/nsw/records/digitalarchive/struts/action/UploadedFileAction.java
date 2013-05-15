package au.gov.nsw.records.digitalarchive.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;

public class UploadedFileAction extends BaseAction {
	
	public ActionForward moveDown(ActionMapping mapping,
			 					  ActionForm form,
			 					  HttpServletRequest request,
			 					  HttpServletResponse response)
	{
			ActionForward forward = null;
			if (!("").equalsIgnoreCase(request.getParameter("id")))
			{
				Integer fileID = new Integer(request.getParameter("id"));
				String pageType = request.getParameter("pageType");
				try
				{
					FileService fs = new FileServiceImpl();
					UploadedFile topFile = fs.loadFile(fileID);
					topFile.setFileOrder(topFile.getFileOrder()+1);
					fs.updateFile(topFile);
					Publication pub = fs.findPubViaFile(fileID);

					UploadedFile downFile = fs.loadFileViaOrder(topFile.getFileOrder(), pub.getPublicationId(), fileID);
					downFile.setFileOrder(downFile.getFileOrder() - 1);
					fs.updateFile(downFile);
					if (("edit").equals(pageType))
						forward = new ActionForward("/agency_pages/publicationEdit.jsp?id=" + pub.getPublicationId());
					else
						forward = new ActionForward("/agency_pages/newPublication.jsp?id=" + pub.getPublicationId());
					
				}catch(Exception ex)
				{
					logger.info("In class UploadedFileAction:moveDown()\n");
					ex.printStackTrace();
				}
			}else
			{
				forward = mapping.findForward("home");
			}
			return forward;
	}

	public ActionForward moveUp(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer fileID = new Integer(request.getParameter("id"));
			String pageType = request.getParameter("pageType");
			try
			{
				FileService fs = new FileServiceImpl();
				UploadedFile topFile = fs.loadFile(fileID);
				topFile.setFileOrder(topFile.getFileOrder() - 1);
				fs.updateFile(topFile);
				Publication pub = fs.findPubViaFile(fileID);

				UploadedFile downFile = fs.loadFileViaOrder(topFile.getFileOrder(), pub.getPublicationId(), fileID);
				downFile.setFileOrder(downFile.getFileOrder() + 1);
				fs.updateFile(downFile);
				if (("edit").equals(pageType))
					forward = new ActionForward("/agency_pages/publicationEdit.jsp?id=" + pub.getPublicationId());
				else
					forward = new ActionForward("/agency_pages/newPublication.jsp?id=" + pub.getPublicationId());

			}catch(Exception ex)
			{
				logger.info("In class UploadedFileAction:moveUp()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
		return forward;
	}
}

package au.gov.nsw.records.digitalarchive.struts.form;


import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;


public class FileUploadForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FormFile file;

    private String metaData;
    private String message;

    public FileUploadForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    /**
     * @return the file
     */
    public FormFile getFile() {
        return this.file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	/**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    
}

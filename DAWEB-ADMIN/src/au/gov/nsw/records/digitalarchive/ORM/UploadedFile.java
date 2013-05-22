package au.gov.nsw.records.digitalarchive.ORM;
// default package
// Generated 12/09/2012 1:28:58 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * UploadedFile generated by hbm2java
 */
public class UploadedFile implements java.io.Serializable {

	private Integer fileId;
	private Publication publication;
	private String fileName;
	private String downloadLink;
	private String cmsDownloadLink;
	private String readerJson;
	private String inboxUrl;
	private String pairtreeUrl;
	private String contentType;
	private String size;
	private Integer pages;
	private String uid;
	private Integer fileOrder;
	private Date dateUploaded;
	private String uploadedBy;
	private String ipAddress;

	public UploadedFile() {
	}

	public UploadedFile(Publication publication, String fileName,
			String downloadLink, String cmsDownloadLink, String readerJson,
			String inboxUrl, String pairtreeUrl, String contentType,
			String size, Integer pages, String uid, Integer fileOrder,
			Date dateUploaded, String uploadedBy, String ipAddress) {
		this.publication = publication;
		this.fileName = fileName;
		this.downloadLink = downloadLink;
		this.cmsDownloadLink = cmsDownloadLink;
		this.readerJson = readerJson;
		this.inboxUrl = inboxUrl;
		this.pairtreeUrl = pairtreeUrl;
		this.contentType = contentType;
		this.size = size;
		this.pages = pages;
		this.uid = uid;
		this.fileOrder = fileOrder;
		this.dateUploaded = dateUploaded;
		this.uploadedBy = uploadedBy;
		this.ipAddress = ipAddress;
	}

	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Publication getPublication() {
		return this.publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadLink() {
		return this.downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public String getCmsDownloadLink() {
		return this.cmsDownloadLink;
	}

	public void setCmsDownloadLink(String cmsDownloadLink) {
		this.cmsDownloadLink = cmsDownloadLink;
	}

	public String getReaderJson() {
		return this.readerJson;
	}

	public void setReaderJson(String readerJson) {
		this.readerJson = readerJson;
	}

	public String getInboxUrl() {
		return this.inboxUrl;
	}

	public void setInboxUrl(String inboxUrl) {
		this.inboxUrl = inboxUrl;
	}

	public String getPairtreeUrl() {
		return this.pairtreeUrl;
	}

	public void setPairtreeUrl(String pairtreeUrl) {
		this.pairtreeUrl = pairtreeUrl;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getPages() {
		return this.pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getFileOrder() {
		return this.fileOrder;
	}

	public void setFileOrder(Integer fileOrder) {
		this.fileOrder = fileOrder;
	}

	public Date getDateUploaded() {
		return this.dateUploaded;
	}

	public void setDateUploaded(Date dateUploaded) {
		this.dateUploaded = dateUploaded;
	}

	public String getUploadedBy() {
		return this.uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}

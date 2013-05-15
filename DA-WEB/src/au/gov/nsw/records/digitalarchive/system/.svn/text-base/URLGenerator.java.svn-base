package au.gov.nsw.records.digitalarchive.system;

public class URLGenerator {
	
	private String hostURL;
	private static final String prefix = "http://";
	private static final String port_number = ":8080";
	private static final String app_name = "/";
	private StringBuilder sc;
	
	public URLGenerator(String hostURL) {
		super();
		this.hostURL = prefix.concat(hostURL).concat(port_number).concat(app_name);
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}
	
	

}

package au.gov.nsw.records.digitalarchive.system;

import org.apache.commons.lang.StringUtils;

public class LuceneDateFormatter {

	public LuceneDateFormatter(){}
	
	public String formatDate(String publicationDate)
	{
		String year = "";
		String month = "01";
		String day = "01";
		String formattedDate = "";
		
		if (StringUtils.isBlank(publicationDate))
		{	formattedDate = "18000101"; }
		else
		{
			publicationDate = publicationDate.replaceAll("/", "");
			year = publicationDate.substring(publicationDate.length() - 4);
			try {
				month = publicationDate.substring(publicationDate.length() - 6, publicationDate.length() - 4);
			} catch (Exception e) {
				month = "01";
			}
			try {
				day = publicationDate.substring(publicationDate.length() - 8, publicationDate.length() - 6);
			} catch (Exception e) {
				day = "01";
			}
			
			formattedDate = year + month + day;
//			System.out.println(year);
//			System.out.println(month);
//			System.out.println(day);
		}
		return formattedDate; 
	}

}

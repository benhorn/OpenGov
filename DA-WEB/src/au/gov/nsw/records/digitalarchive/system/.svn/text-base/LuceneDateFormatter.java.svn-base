package au.gov.nsw.records.digitalarchive.system;

import gov.loc.repository.pairtree.Pairtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.base.NYTimesJSONFormat;
import au.gov.nsw.records.digitalarchive.search.TikaWrapper;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

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

	public static void main (String[] args)
	{
		LuceneDateFormatter ldf = new LuceneDateFormatter();
		System.out.println(ldf.formatDate("01/01/2013"));
	}
	
}

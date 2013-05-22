package au.gov.nsw.records.digitalarchive.base;

import java.io.File;

public class Constants {
	
	public static String ADMIN_LOSTNAME_KEY = "admin.lostName";
	public static String ADMIN_LOSTPWD_KEY = "admin.lostPwd";
	public static String ADMIN_LOGINERROR_KEY = "admin.loginerror";
	
	public static String PUBLICATION_DRAFT = "draft";
	public static String PUBLICATION_SUBMITTED = "submitted";
	public static String PUBLICATION_REJECTED = "rejected";
	public static String PUBLICATION_PUBLISHED = "published";
	
	public static String MOUNT_POINT = File.separator + "mnt" + File.separator + "opengovdata" + File.separator;
		
	public static String PAIRTREE_ROOT = MOUNT_POINT.concat("pairtree_root" + File.separator);
	public static String INBOX = MOUNT_POINT.concat("inbox" + File.separator);	
	public static String LUCENE_INDEX = MOUNT_POINT.concat("lucene_index" + File.separator);
	public static String FACET_INDEX = MOUNT_POINT.concat("facet_index" + File.separator);
	public static String TAXONOMY = MOUNT_POINT.concat("taxonomy" + File.separator);
	
	public static String quotes = "\"";

}
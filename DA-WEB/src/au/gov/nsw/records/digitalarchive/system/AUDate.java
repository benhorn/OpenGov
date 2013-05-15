package au.gov.nsw.records.digitalarchive.system;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AUDate{

	/**
	 * 
	 */
	public AUDate()
	{
		this(doFormat());
	}
	
	public AUDate(String date)
	{
	
	}
	
	private static String doFormat()
	{
		 Date date = new Date();
		 Format formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	     String today = formatter.format(date);  
	     return today;
	}

	public long daysBetween(Calendar startDate, Calendar endDate) 
	{  
			Calendar date = (Calendar) startDate.clone();  
			long daysBetween = 0;  
			while (date.before(endDate)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
		daysBetween++;  
		}  
			return daysBetween;  
	}  
	
	public static void main(String[] args)
	{
		  try {
		  String str_date="2010";
		  DateFormat formatter ; 
		  Date date ; 
		  formatter = new SimpleDateFormat("yy");
		  date = (Date)formatter.parse(str_date); 
		  long longDate=date.getTime();
		  System.out.println("Today is " +longDate );
		  }
		  catch (ParseException e){
		  System.out.println("Exception :"+e); 
		  }
		  

	}
}

package au.gov.nsw.records.digitalarchive.system;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormat {
	
  public String formatDate(Date inputDate)
  {
	  String s;
	  Format formatter = new SimpleDateFormat("ddMMyyyy");
	  s = formatter.format(inputDate);
	  return s;
  }

  public String rightNow()
  {
	  return formatDate(new Date());
  }
  
  public String getLastDay(String year, String month) {
	  
	    // get a calendar object
	    GregorianCalendar calendar = new GregorianCalendar();
	    
	    // convert the year and month to integers
	    int yearInt = Integer.parseInt(year);
	    int monthInt = Integer.parseInt(month);
	    
	    // adjust the month for a zero based index
	    monthInt = monthInt - 1;
	    
	    // set the date of the calendar to the date provided
	    calendar.set(yearInt, monthInt, 1);
	    
	    int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	    
	    return Integer.toString(dayInt);
	  } // end getLastDay m
  
  
  public static void main(String args[]) 
  {
	  DateFormat df = new DateFormat();
	  
	  System.out.println("True of false: " + df.rightNow().equals(df.getLastDay("2013", "3")));
	  System.out.println("Right now: " + df.rightNow());
	  System.out.println("Last day: " + df.getLastDay("2013", "2"));
	   
	  //System.out.println("Last Day : " + lastDay);    
  }
}
package au.gov.nsw.records.digitalarchive.service;

import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import au.gov.nsw.records.digitalarchive.ORM.*;
import au.gov.nsw.records.digitalarchive.service.*;


public class copyBOSStoLOCAL {
	
	public static void main(String args[])
	{
		BOSService fs = new BOSServiceImpl();
		BOSListService as = new BOSListServiceImpl();
	
	try{
			List<Agencies> list = fs.browseAgency();
			Iterator<Agencies> it = list.iterator();
			
			for (int i = 0; i < list.size(); i++) 
			{
				BosList BosList = new BosList();
				BosList.setAgencyNumber(list.get(i).getAgencyNumber());
				BosList.setAgencyName(list.get(i).getAgencyTitle());
					  as.addNewBosList(BosList);
			}
			/*BosList BosList2 = new BosList();
			BosList2.setAgencyName("");
			as.addNewAgency(BosList2);*/
			System.out.println(list.size());
			
		}catch(Exception ex){
		
		} 
	}

}

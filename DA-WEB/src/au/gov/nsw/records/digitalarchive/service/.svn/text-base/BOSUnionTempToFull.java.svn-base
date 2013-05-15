package au.gov.nsw.records.digitalarchive.service;

import java.util.Iterator;
import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Agencies;
import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.TempList;

public class BOSUnionTempToFull {

	public static void main(String args[])
	{
		BOSService bs = new BOSServiceImpl();
		TempListService tls = new TempListServiceImpl();
		BOSListService bls = new BOSListServiceImpl();
		FullAgencyListService fls = new FullAgencyListServiceImpl();
	
	 try{
			List<Agencies> list = bs.browseAgency();
			List<TempList> list1 = tls.browseTempList();
			Iterator<Agencies> it = list.iterator();
			Iterator<TempList> it1 = list1.iterator();
			FullAgencyList BOSList = new FullAgencyList();
			FullAgencyList TempList = new FullAgencyList();
			
			for (int i = 0; i < list.size(); i++) 
			{
				BOSList.setBosId(list.get(i).getAgencyNumber());
				BOSList.setAgencyName(list.get(i).getAgencyTitle());
				fls.addFullAgencyList(BOSList);
			}
			
			for (int i = 0; i < list1.size(); i++) 
			{
				TempList.setTempId(list1.get(i).getTempListId());
				TempList.setAgencyName(list1.get(i).getName());
				fls.addFullAgencyList(TempList);
			}
			System.out.println(tls.browseTempList().size());
		}catch(Exception ex){
		
		} 
	}
}

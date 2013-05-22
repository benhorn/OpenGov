package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Agencies;

public interface BOSService {

	public List<Agencies> browseAgency() throws Exception;
}

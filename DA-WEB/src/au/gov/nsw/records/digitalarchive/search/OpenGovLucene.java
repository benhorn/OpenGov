package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.index.params.DefaultFacetIndexingParams;
import org.apache.lucene.facet.index.params.FacetIndexingParams;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.search.params.CountFacetRequest;
import org.apache.lucene.facet.search.params.FacetSearchParams;
import org.apache.lucene.facet.search.results.FacetResult;
import org.apache.lucene.facet.search.results.FacetResultNode;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.core.io.ClassPathResource;

import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;

public class OpenGovLucene{
 
	private static ClassPathResource cpr = new ClassPathResource("lucene.properties");
	
	private static final String INDEX_LOC_PROPERTY = "indexlocation";
	private static final String TEXO_LOC_PROPERTY = "taxolocation";
	
	Directory indexDirectory;
	Directory taxonomyDirectory;
	private static final File INDEX_PATH = new File(Constants.LUCENE_INDEX);
	private static final File TAXONOMY_PATH = new File(Constants.TAXONOMY);
	
	private int numTotalHits;
	private static Logger log = Logger.getLogger(OpenGovLucene.class);
	
	public OpenGovLucene() throws IOException {
	    this.indexDirectory = FSDirectory.open(INDEX_PATH);
	    this.taxonomyDirectory = FSDirectory.open(TAXONOMY_PATH);
	  }
	
	public OpenGovSearchResult searchTextOrderViaScore(String searchText, FacetSearchParams facets, Integer pageNumber, Integer pageSize) 
	{
		Map<String, String> resultEntity = new HashMap<String, String>();
		List<FacetResultItem> facetResults = new ArrayList<FacetResultItem>();

		File INDEX_PATH = new File(Constants.LUCENE_INDEX);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		
		try 
		{
			IndexReader reader = IndexReader.open(FSDirectory.open(INDEX_PATH)); 
			IndexSearcher searcher = new IndexSearcher(reader);
			
			String[] fields = {"title", "keyword", "agencyname", "filename", "content", "type", "coverage"};
			
			QueryParser query = new MultiFieldQueryParser(Version.LUCENE_36, fields, analyzer);
			TopScoreDocCollector collector = TopScoreDocCollector.create(pageNumber.intValue() * pageSize.intValue(), false);
			TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxonomyDirectory);

			FacetsCollector facetsCollector = new FacetsCollector(facets, reader, taxoReader);
			
			Query searchQuery = query.parse(searchText);
            
//			System.out.println("Searching " + searcher.maxDoc() + " documents.");
//			System.out.println("Query " + searchQuery.toString());
            
			searcher.search(searchQuery, MultiCollector.wrap(collector, facetsCollector));
            
			TopDocs docs = collector.topDocs((pageNumber.intValue()-1)*pageSize.intValue(), pageSize.intValue());
			this.numTotalHits = collector.getTotalHits();
	    //    System.out.println("Found " + numTotalHits);
	        Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlighter\">","</span>");
	        Scorer fragmentScorer = new QueryScorer(searchQuery);
	        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
	        Fragmenter fragmenter = new SimpleFragmenter(150);
	        highlighter.setTextFragmenter(fragmenter);
	       
	        for (ScoreDoc hit:docs.scoreDocs)
	        {
		    	Document doc = searcher.doc(hit.doc);
	              
	            String filename = doc.get("filename");
	            String agencyname = doc.get("agencyname");
	            String keyword = doc.get("keyword");
	            String title = doc.get("title");
	            String description = doc.get("description");
	            String content = doc.get("content");
	            String fileID = doc.get("fileID");
		              
	            String hl_filename = highlighter.getBestFragment(analyzer, "filename", filename);
	            //String hl_agencyname = highlighter.getBestFragment(analyzer, "agencyname", agencyname);
	            String hl_agencyname = "";
	            String hl_keyword = highlighter.getBestFragment(analyzer, "keyword", keyword);
	            String hl_title = highlighter.getBestFragment(analyzer, "title", title);
	            String hl_description = highlighter.getBestFragment(analyzer, "description", description);
	            String hl_content = highlighter.getBestFragment(analyzer, "content", content);
	
	            if(StringUtils.isEmpty(hl_filename))
	            {hl_filename = filename.substring(0, Math.min(150, filename.length()));}
	              
//	            if(StringUtils.isEmpty(hl_agencyname))
//	            {hl_agencyname = agencyname.substring(0, Math.min(150, agencyname.length()));}
	
	            if(StringUtils.isEmpty(hl_keyword))
	            {hl_keyword = keyword.substring(0, Math.min(150, keyword.length()));}
	
	            if(StringUtils.isEmpty(hl_title))
	            {hl_title = title.substring(0, Math.min(150, title.length()));}
	              
	            if(StringUtils.isEmpty(hl_description))
	            {hl_description = description.substring(0, Math.min(150, description.length()));}
	              
	            if(StringUtils.isEmpty(hl_content))
	            {hl_content = content.substring(0, Math.min(150, content.length()));}
	                  
	            FileService fs = new FileServiceImpl();
	            UploadedFile uFile = fs.loadFile(Integer.parseInt(fileID));
	            String fileName = uFile.getFileName();
	            String fileSize = uFile.getSize();
	            final StringBuilder builder = new StringBuilder();
	              
	            builder.append("...... " + hl_content + "......").append("<br/><br/><strong><span class=\"lightblue\"><a href=\"download/" + fileID + "\" title=\"" + fileName + "\">" + hl_filename + "</a>&nbsp;&nbsp;|&nbsp;&nbsp;Size:&nbsp;" + fileSize +"</span></strong>");      
	            resultEntity.put(fileID, builder.toString());
	        }

	      for (FacetResult fr:facetsCollector.getFacetResults()){
				List<FacetResultItem> subItems = new ArrayList<FacetResultItem>();
				facetResults.add(new FacetResultItem(fr.getFacetResultNode().getLabel().lastComponent().toString(), fr.getFacetResultNode().getLabel().lastComponent().toString(), 0, subItems));
				for (FacetResultNode rn: fr.getFacetResultNode().getSubResults()){
					subItems.add(new FacetResultItem(rn.getLabel().lastComponent().toString().replace(fr.getFacetResultNode().getLabel().lastComponent().toString() + "/", ""), fr.getFacetResultNode().getLabel().lastComponent().toString(), new Double(rn.getValue()).intValue(), null));
				}
			}
            searcher.close();
            reader.close();
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();		
		}
		return new OpenGovSearchResult(resultEntity, facetResults, this.numTotalHits);
	}
	
	public OpenGovSearchResult searchTextOrderViaDate(String searchText, FacetSearchParams facets, Integer pageNumber, Integer pageSize) 
	{
		Map<String, String> resultEntity = new HashMap<String, String>();
		List<FacetResultItem> facetResults = new ArrayList<FacetResultItem>();

		File INDEX_PATH = new File(Constants.LUCENE_INDEX);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	    
	    String filename = "";
        String agencyname = "";
        String keyword = "";
        String title = "";
        String description = "";
        String content = "";
        String fileID = "";
	    
	    String hl_filename = "";
        String hl_agencyname = "";
        String hl_keyword = "";
        String hl_title = "";
        String hl_description = "";
        String hl_content = "";
        
        String hl_datePublished="";
        SortedMap<String, String> yearToFileID = new TreeMap<String, String>();
        
    	SortedMap<String, String> yearToExtractedParagraph = new TreeMap<String, String>();
    	
    	String extractedParagraph = "";
		try 
		{
			IndexReader reader = IndexReader.open(FSDirectory.open(INDEX_PATH)); 
			IndexSearcher searcher = new IndexSearcher(reader);
			
			String[] fields = {"title", "keyword", "agencyname", "filename", "content", "type", "coverage"};
			
			QueryParser query = new MultiFieldQueryParser(Version.LUCENE_36, fields, analyzer);
			Sort sort = new Sort();
			SortField sortField = new SortField("date_published", SortField.STRING, true);
			sort.setSort(sortField);
			TopFieldCollector collector = TopFieldCollector.create(sort, pageNumber.intValue() * pageSize.intValue(), true, false, false, false);
			TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxonomyDirectory);

			FacetsCollector facetsCollector = new FacetsCollector(facets, reader, taxoReader);
			
			Query searchQuery = query.parse(searchText);
//            
//			System.out.println("Searching " + searcher.maxDoc() + " documents.");
//			System.out.println("Query " + searchQuery.toString());
            
			searcher.search(searchQuery, MultiCollector.wrap(collector, facetsCollector));
            
			TopDocs docs = collector.topDocs((pageNumber.intValue()-1)*pageSize.intValue(), pageSize.intValue());
			this.numTotalHits = collector.getTotalHits();
	        System.out.println("Found " + numTotalHits);
	        Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlighter\">","</span>");
	        Scorer fragmentScorer = new QueryScorer(searchQuery);
	        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
	        Fragmenter fragmenter = new SimpleFragmenter(150);
	        highlighter.setTextFragmenter(fragmenter);
	              
	        for (ScoreDoc hit:docs.scoreDocs)
	        {
		    	Document doc = searcher.doc(hit.doc);
	              
	            filename = doc.get("filename");
	            agencyname = doc.get("agencyname");
	            keyword = doc.get("keyword");
	            title = doc.get("title");
	            description = doc.get("description");
	            content = doc.get("content");
	            fileID = doc.get("fileID");
	            
	            hl_datePublished = doc.get("date_published");
		              
	            hl_filename = highlighter.getBestFragment(analyzer, "filename", filename);
	            hl_agencyname = highlighter.getBestFragment(analyzer, "agencyname", agencyname);
	            hl_keyword = highlighter.getBestFragment(analyzer, "keyword", keyword);
	            hl_title = highlighter.getBestFragment(analyzer, "title", title);
	            hl_description = highlighter.getBestFragment(analyzer, "description", description);
	            hl_content = highlighter.getBestFragment(analyzer, "content", content);
	
	            if(StringUtils.isEmpty(hl_filename))
	            {hl_filename = filename.substring(0, Math.min(150, filename.length()));}
	              
	            if(StringUtils.isEmpty(hl_agencyname))
	            {hl_agencyname = agencyname.substring(0, Math.min(150, agencyname.length()));}
	
	            if(StringUtils.isEmpty(hl_keyword))
	            {hl_keyword = keyword.substring(0, Math.min(150, keyword.length()));}
	
	            if(StringUtils.isEmpty(hl_title))
	            {hl_title = title.substring(0, Math.min(150, title.length()));}
	              
	            if(StringUtils.isEmpty(hl_description))
	            {hl_description = description.substring(0, Math.min(150, description.length()));}
	              
	            if(StringUtils.isEmpty(hl_content))
	            {hl_content = content.substring(0, Math.min(150, content.length()));}
	                  
	            FileService fs = new FileServiceImpl();
	            UploadedFile uFile = fs.loadFile(Integer.parseInt(fileID));
	            String fileName = uFile.getFileName();
	            String fileSize = uFile.getSize();
	              
	            extractedParagraph = "...... " + hl_content + "......<br/><br/><strong><span class=\"lightblue\"><a href=\"download/" + fileID + "\" title=\"" + fileName + "\">" + hl_filename + "</a>&nbsp;&nbsp;|&nbsp;&nbsp;Size:&nbsp;" + fileSize +"</span></strong>";
	            resultEntity.put(fileID, extractedParagraph);
	            yearToFileID.put(hl_datePublished, fileID);
	            yearToExtractedParagraph.put(hl_datePublished, extractedParagraph);
	        }

	      for (FacetResult fr:facetsCollector.getFacetResults()){
				List<FacetResultItem> subItems = new ArrayList<FacetResultItem>();
				facetResults.add(new FacetResultItem(fr.getFacetResultNode().getLabel().lastComponent().toString(), fr.getFacetResultNode().getLabel().lastComponent().toString(), 0, subItems));
				for (FacetResultNode rn: fr.getFacetResultNode().getSubResults()){
					subItems.add(new FacetResultItem(rn.getLabel().lastComponent().toString().replace(fr.getFacetResultNode().getLabel().lastComponent().toString() + "/", ""), fr.getFacetResultNode().getLabel().lastComponent().toString(), new Double(rn.getValue()).intValue(), null));
				}
			}
	        searcher.close();
            reader.close();
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
		return new OpenGovSearchResult(hl_datePublished, fileID,hl_filename, 
									   hl_agencyname, hl_title, 
									   hl_keyword, hl_description, 
									   hl_content, facetResults, 
									   this.numTotalHits, resultEntity, yearToFileID, yearToExtractedParagraph);

	}
	
	public void LuceneRangeQuery(String lowerTerm, String upperTerm)
	{
		 try{
			IndexReader reader = IndexReader.open(FSDirectory.open(INDEX_PATH)); 
			IndexSearcher searcher = new IndexSearcher(reader);
		    TermRangeQuery query = new TermRangeQuery("date_published", lowerTerm, upperTerm, true, true);
		    System.out.print(query.toString());
		    ScoreDoc[] hits = searcher.search(query, null, 1000).scoreDocs;
		    System.out.println(hits.length);
		    System.out.println("Search result:");
		    for (int i = 0; i < hits.length; i++) {
		     Document hitDoc = searcher.doc(hits[i].doc);
		     	System.out.println(hitDoc.get("filename"));
		     	System.out.println(hitDoc.get("agencyname"));
		     	System.out.println(hitDoc.get("keyword"));
		     	System.out.println(hitDoc.get("title"));
		     	System.out.println(hitDoc.get("description"));
		     	System.out.println(hitDoc.get("content"));
		     	System.out.println(hitDoc.get("fileID"));
		     	}
		   }catch(IOException e){
		    e.printStackTrace();
		   }
		   System.out.println("Search Success");
	}	
//	private SearchResult search(Query query, FacetSearchParams facets, Integer page, Integer size){
//		List<SearchResultItem> searchResults = new ArrayList<SearchResultItem>(); 
//		List<FacetResultItem> facetResults = new ArrayList<FacetResultItem>();
//		int numTotalHits = 0;
//		try {
//			Properties prop = new Properties();
//			prop.load(new FileInputStream(cpr.getFile()));
//			
//			IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(prop.getProperty(INDEX_LOC_PROPERTY))));
//			IndexSearcher searcher = new IndexSearcher(indexReader);
//			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
//
//			Directory taxoDir = FSDirectory.open(new File(prop.getProperty(TEXO_LOC_PROPERTY)));
//			TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
//			FacetsCollector facetsCollector = new FacetsCollector(facets, indexReader, taxoReader);
//			
//			TopScoreDocCollector docCollector = TopScoreDocCollector.create(page.intValue()*size.intValue(), true);
//			
//			searcher.search(query, MultiCollector.wrap(docCollector, facetsCollector));
//			
//			TopDocs docs = docCollector.topDocs((page.intValue()-1)*size.intValue(), size.intValue());
//			numTotalHits = docCollector.getTotalHits();
//			log.info(numTotalHits + " total matching documents");
//			
//			for (ScoreDoc hit:docs.scoreDocs){
//				Document doc = searcher.doc(hit.doc);
//				Formatter formatter = new SimpleHTMLFormatter("<b>","</b>");
//				Scorer fragmentScorer = new QueryScorer(query);
//				Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
//				Fragmenter fragmenter = new SimpleFragmenter(150);
//				highlighter.setTextFragmenter(fragmenter);
//				searchResults.add(new SearchResultItem(doc.get("type"), doc.get("title"), highlighter.getBestFragment(analyzer, "content", doc.get("content")), doc.get("id"), doc.get("url")));
//			}
//			for (FacetResult fr:facetsCollector.getFacetResults()){
//				List<FacetResultItem> subItems = new ArrayList<FacetResultItem>();
//				facetResults.add(new FacetResultItem(fr.getFacetResultNode().getLabel().toString(), fr.getFacetResultNode().getLabel().toString(),0, subItems));
//				for (FacetResultNode rn: fr.getFacetResultNode().getSubResults()){
//					subItems.add(new FacetResultItem(rn.getLabel().toString().replace(fr.getFacetResultNode().getLabel().toString() + "/", ""), fr.getFacetResultNode().getLabel().toString(), new Double(rn.getValue()).intValue(), null));
//				}
//			}
//			searcher.close();
//			indexReader.close();
//		} catch (CorruptIndexException e) {
//			e.printStackTrace();
//		} catch (InvalidTokenOffsetsException e){
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return new SearchResult(searchResults, facetResults, numTotalHits);
//	}

	public OpenGovSearchResult search(LuceneSearchParams params, String order)
	{
		int i = 0;
		// multi-entity search
		String facetCondition = "";
		if (!StringUtils.isBlank(params.getAgency())){
			facetCondition += String.format(" AND agencyname:\"%s\"", params.getAgency());
		}
		if (!StringUtils.isBlank(params.getType())){
			facetCondition += String.format(" AND type:\"%s\"", params.getType());
		}
		if (!StringUtils.isBlank(params.getPublicationYearStart()) && 
		    !StringUtils.isBlank(params.getPublicationYearEnd())){
			facetCondition += String.format(" AND date_published:[%s TO %s]", params.getPublicationYearStart(), params.getPublicationYearEnd());
		}
		try {
			String queryText = params.getQuery().trim() + facetCondition; 
			//System.out.println("Facet Condition:" + queryText);
			if (("year").equals(order))
				return searchTextOrderViaDate(queryText, params.getFacetParams(), params.getPageNo(), params.getPageSize());
			if (("relevance").equals(order))
				return searchTextOrderViaScore(queryText, params.getFacetParams(), params.getPageNo(), params.getPageSize());
		} catch (Exception e){
			e.printStackTrace();
		}
		return new OpenGovSearchResult(new HashMap<String, String>(), new ArrayList<FacetResultItem>(), 0);
	}
	
	public int getNumTotalHits() {
		return this.numTotalHits;
	}

	public void setNumTotalHits(int numTotalHits) {
		this.numTotalHits = numTotalHits;
	}
	
//	public static void main(String[] args) throws Exception
//	{
//		OpenGovLucene ogl = new OpenGovLucene();
//		FacetIndexingParams indexingParams = new DefaultFacetIndexingParams();
//		FacetSearchParams facetSearchParams = new FacetSearchParams(indexingParams);
//		
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("agencyname"), 10));
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("rights"), 10));
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("type"), 20));
//		LuceneSearchParams params = new LuceneSearchParams("\"1813\"", facetSearchParams, "", "", "", "", 1, 25);
//		OpenGovSearchResult results = ogl.search(params);
//
//	}
	
//	public static void main(String[] args) throws Exception 
//	{
//		OpenGovLucene ogl = new OpenGovLucene();
//		FacetIndexingParams indexingParams = new DefaultFacetIndexingParams();
//		FacetSearchParams facetSearchParams = new FacetSearchParams(indexingParams);
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("agencyname"), 10));
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("rights"), 10));
//		facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("type"), 20));
//		FileService fs = new FileServiceImpl();
//
//		OpenGovSearchResult resultList = ogl.searchTextOrderViaDate("\"police\"", facetSearchParams, 1, 25);
//		Set<Map.Entry<String, String>> resultEntities = resultList.getResults().entrySet();
//		for(Map.Entry<String, String> resultEntity : resultEntities ) 
//		{
//	        String key = resultEntity.getKey();
//	        String value = resultEntity.getValue();
//	        if (key != null)
//	        {
//	    		UploadedFile uFile = fs.loadFile(Integer.parseInt(key));
//				Publication pub = fs.findPubViaFile(Integer.parseInt(key));		
//				System.out.println(pub.getDatePublishedRaw());
//	        }
//		}
//		String[] keys = new String[resultList.getExtractedParagraph().size()];
//		System.out.println(keys.length);
//		resultList.getExtractedParagraph().keySet().toArray(keys);
//		resultList.getExtractedParagraph().keySet().toArray(keys);
//		Arrays.sort(keys, Collections.reverseOrder());
//
//		for (String key:keys) {  
//            System.out.println(resultList.getExtractedParagraph().get(key));  
//            //System.out.println(key + " " + resultList.getExtractedParagraph().get(key));
//        }  
//	}
//	
	
}
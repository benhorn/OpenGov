package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.facet.index.CategoryDocumentBuilder;
import org.apache.lucene.facet.index.params.DefaultFacetIndexingParams;
import org.apache.lucene.facet.index.params.FacetIndexingParams;
import org.apache.lucene.facet.search.DrillDown;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.search.params.CountFacetRequest;
import org.apache.lucene.facet.search.params.FacetRequest;
import org.apache.lucene.facet.search.params.FacetSearchParams;
import org.apache.lucene.facet.search.results.FacetResult;
import org.apache.lucene.facet.search.results.FacetResultNode;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import au.gov.nsw.records.digitalarchive.base.Constants;


public class FacetPrinter {

  Directory indexDirectory;
  Directory taxonomyDirectory;
  private static final File INDEX_PATH = new File(Constants.LUCENE_INDEX);
  private static final File TAXONOMY_PATH = new File(Constants.TAXONOMY);

  public FacetPrinter() throws IOException {
    this.indexDirectory = FSDirectory.open(INDEX_PATH);
    this.taxonomyDirectory = FSDirectory.open(TAXONOMY_PATH);
  }

  public static void main(String[] args) throws Exception {
		FacetPrinter example = new FacetPrinter();
	    example.run();
	  }

  public List<FacetResult> getFacetResult() throws IOException
  {
	  IndexReader indexReader = IndexReader.open(indexDirectory);
	  IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	  Query matchAllDocsQuery1 = new MatchAllDocsQuery();
	  FacetsCollector facetsCollector = getFacetsCollector();
	  indexSearcher.search(matchAllDocsQuery1, new QueryWrapperFilter(matchAllDocsQuery1), facetsCollector);
	  
	  return facetsCollector.getFacetResults();
  }
  
  public void run() throws Exception {
    	  
    IndexReader indexReader = IndexReader.open(indexDirectory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    
    // Search for all documents
    Query matchAllDocsQuery1 = new MatchAllDocsQuery();
    FacetsCollector facetsCollector1 = getFacetsCollector();
    indexSearcher.search(matchAllDocsQuery1, new QueryWrapperFilter(matchAllDocsQuery1), facetsCollector1);

    //
    String[] fields = {"title", "description", "keyword", "agencyname", "filename", "content", "type", "coverage"};
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	QueryParser query = new MultiFieldQueryParser(Version.LUCENE_36, fields, analyzer);
	
	TopScoreDocCollector collector = TopScoreDocCollector.create(100, false);

    Query searchQuery = query.parse("Police");
    
    indexSearcher.search(searchQuery, MultiCollector.wrap(collector, facetsCollector1));

    // Output:
    printFacetResult(facetsCollector1.getFacetResults());

    // Drill down query for the Mission
  /*  CategoryPath disclosure_log = new CategoryPath("type", "Disclosure Log");

    Query matchAllDocsQuery2 = new MatchAllDocsQuery();
    Query missionQuery = DrillDown.query(matchAllDocsQuery2, disclosure_log);
    FacetsCollector facetsCollector2 = getFacetsCollector();
    indexSearcher.search(missionQuery, new QueryWrapperFilter(matchAllDocsQuery2), facetsCollector2);
	*/
    // Output:
    //Mission: 1
    //But I want the same facet counts like before.
    //Is this possible without running two queries: one with the drill down query to retrieve the documents,
    //and another one without the drill down query to count the facets?
    //System.out.println("Drill down query");
    //printFacetResult(facetsCollector2.getFacetResults());
  }

  
  public void createFacetIndex(String agencyName, String type, String rights, String coverage, String cType, String publication_year) throws IOException
  {
	  IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
	    IndexWriter indexWriter = new IndexWriter(indexDirectory, config);

	    TaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter (taxonomyDirectory);
	    CategoryDocumentBuilder categoryDocumentBuilder = new CategoryDocumentBuilder(taxonomyWriter);

	    CategoryPath agency_category = new CategoryPath("agency", agencyName.trim());
	    CategoryPath type_category = new CategoryPath("type", type.trim());
	    CategoryPath rights_category = new CategoryPath("rights", rights.trim());
	    CategoryPath coverage_category = new CategoryPath("coverage", coverage.trim());
	    CategoryPath cType_category = new CategoryPath("content type", cType.trim());
	    
	    if (publication_year.length() >= 4)
	    	publication_year = publication_year.substring(publication_year.length() - 4, publication_year.length());
	    
	    CategoryPath year_category = new CategoryPath("publication_year", publication_year);
	    
	    Document doc1 = new Document();
	    List<CategoryPath> doc1Categories = new ArrayList<CategoryPath>();
	    
	    doc1Categories.add(agency_category);
	    doc1Categories.add(type_category);
	    doc1Categories.add(rights_category);
	    doc1Categories.add(coverage_category);
	    doc1Categories.add(cType_category);
	    categoryDocumentBuilder.setCategoryPaths(doc1Categories).build(doc1);
	    indexWriter.addDocument(doc1);

	   
	    Document doc2 = new Document();
	    List<CategoryPath> doc2Categories = new ArrayList<CategoryPath>();
	    doc2Categories.add(year_category);
	    categoryDocumentBuilder.setCategoryPaths(doc2Categories).build(doc2);
	    indexWriter.addDocument(doc2);

	    indexWriter.commit();
	    taxonomyWriter.commit();
	    indexWriter.close();
	    taxonomyWriter.close();
  }
  
  
  public void printFacetResult(List<FacetResult> facetResults) {
	  
	 int totalCount = 0 ;
	 for (FacetResult facetResult : facetResults) {
      FacetResultNode resultNode = facetResult.getFacetResultNode();

      if (resultNode.getNumSubResults() > 0) {
        int numSubResults = resultNode.getNumSubResults();
        String facetName = resultNode.getLabel().lastComponent();

        for (FacetResultNode node : resultNode.getSubResults()) {
          String label = node.getLabel().lastComponent();
          Integer count = (int) node.getValue();
          totalCount = count + totalCount;
          System.out.println(label + ": " + "[" + count + "]");
        }
      }
    }
	 System.out.println(totalCount);
  }

  public FacetsCollector getFacetsCollector() throws IOException {
    IndexReader indexReader = IndexReader.open(indexDirectory);
    TaxonomyReader taxonomyReader = new DirectoryTaxonomyReader(taxonomyDirectory);
    
    CategoryPath agencyName = new CategoryPath("agency");
    CategoryPath type = new CategoryPath("type");
    /*CategoryPath rights = new CategoryPath("rights");
    CategoryPath coverage = new CategoryPath("coverage");
    CategoryPath cType_category = new CategoryPath("content type");*/
    //CategoryPath publicationType = new CategoryPath("publicationType");
    
    FacetIndexingParams indexingParams = new DefaultFacetIndexingParams();
    FacetSearchParams facetSearchParams = new FacetSearchParams(indexingParams);

    FacetRequest AgencyFacetRequest = new CountFacetRequest(agencyName, 10);
    FacetRequest RightsFacetRequest = new CountFacetRequest(type, 10);
    /*FacetRequest CoverageFacetRequest = new CountFacetRequest(coverage, 10);
    FacetRequest ContentTypeFacetRequest = new CountFacetRequest(cType_category, 10);
    FacetRequest PublicationYearFacetRequest = new CountFacetRequest(publication_year, 10);*/

    
    facetSearchParams.addFacetRequest(AgencyFacetRequest);
    facetSearchParams.addFacetRequest(new CountFacetRequest(type, 10));
    facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("publication_year"), 10));
    /*facetSearchParams.addFacetRequest(CoverageFacetRequest);
    facetSearchParams.addFacetRequest(ContentTypeFacetRequest);
    facetSearchParams.addFacetRequest(PublicationYearFacetRequest);*/
    
    return new FacetsCollector(facetSearchParams, indexReader, taxonomyReader);
  }
}

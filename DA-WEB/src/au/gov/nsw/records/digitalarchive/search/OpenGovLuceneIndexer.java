package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.facet.index.CategoryDocumentBuilder;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import au.gov.nsw.records.digitalarchive.base.Constants;

public class OpenGovLuceneIndexer {

	private static final File INDEX_PATH = new File(Constants.LUCENE_INDEX);
	private static final File TAXONOMY_PATH = new File(Constants.TAXONOMY);
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

    private String title = "";
	private String description = "";
	private String keyword = "";
	private List<String> agencyNameList;
	
	private Document doc;
	private String date_published = "";
	private String type = "";
	private String coverage = "";
	private String rights = "";
	
	private Directory indexDirectory;
	private Directory taxonomyDirectory;
    
	private TaxonomyWriter taxonomyWriter;
	private IndexWriter indexWriter;
    
	public OpenGovLuceneIndexer() throws IOException{
	    this.indexDirectory = FSDirectory.open(INDEX_PATH);
	    this.taxonomyDirectory = FSDirectory.open(TAXONOMY_PATH);
	    this.taxonomyWriter = new DirectoryTaxonomyWriter (taxonomyDirectory);
	}
	
	public OpenGovLuceneIndexer(String title, String description, 
						 		String keyword, List<String> agencyNameList, 
						 		String datePublished, String type, 
						 		String coverage, String rights) throws IOException
	{
		this.indexDirectory = FSDirectory.open(INDEX_PATH);
		this.taxonomyDirectory = FSDirectory.open(TAXONOMY_PATH);
		this.taxonomyWriter = new DirectoryTaxonomyWriter (taxonomyDirectory);
		this.title = new String(title.trim());
		this.description = new String(description.trim());
		this.keyword = new String(keyword.trim());
		this.agencyNameList = agencyNameList;
		this.date_published = new String(datePublished.trim());
		this.type = new String(type.trim());
		this.coverage = new String(coverage.trim());
		this.rights = new String(rights.trim());
	}
	
	public void createIndex(String fileID, String fileName, String inputFile, String cType) throws Exception
	{
		//Description is no longer indexed and read
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        iwc.setRAMBufferSizeMB(256.0);
        indexWriter = new IndexWriter(indexDirectory, iwc);
        
		TikaWrapper tw = new TikaWrapper();
		
		Field fileIDField = new Field("fileID", fileID.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    Field titleField = new Field("title", this.title.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    Field descriptionField = new Field("description", this.description.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    Field keywordField = new Field("keyword", this.keyword.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    Field agencyNameField = null;
	    
	    List<Field> agencyNameFieldList = new ArrayList<Field>();
	    
	    for(int i=0; i < agencyNameList.size(); i++)
	    {	
	    	agencyNameFieldList.add(agencyNameField = new Field("agencyname", this.agencyNameList.get(i).toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
		    agencyNameField.setBoost(11.0f);
	    }
	    
	    Field fileNameField = new Field("filename", fileName.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    Field contentField = new Field("content", tw.getFileContent(inputFile).toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
	    
	    //These are facets
	    Field date_published = new Field("date_published", this.date_published.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED); //Used for Sort
	    Field type = new Field("type", this.type.toString(), Field.Store.YES, Field.Index.ANALYZED);
	    Field coverage = new Field("coverage", this.coverage.toString(), Field.Store.YES, Field.Index.ANALYZED);
	    Field rights = new Field("rights", this.rights.toString(), Field.Store.YES, Field.Index.ANALYZED);
	    Field contentType = new Field("content_type", cType.toString(), Field.Store.YES, Field.Index.ANALYZED);
	    
	    titleField.setBoost(11.0f);
	    keywordField.setBoost(2.0f);
	    descriptionField.setBoost(1.5f);
	    contentField.setBoost(0.2f);
	    
	    doc = new Document();
	    doc.add(fileIDField);
	    doc.add(titleField);
	    doc.add(descriptionField);
	    doc.add(keywordField);
	    
	    for(int j=0; j < agencyNameFieldList.size(); j++)
	    {	
	    	doc.add(agencyNameFieldList.get(j));
	    }
	    
	    doc.add(fileNameField);
	    doc.add(contentField);
	    
	    doc.add(date_published);
	    doc.add(type);
	    doc.add(coverage);
	    doc.add(rights);
	    doc.add(contentType);
	    
	    if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE_OR_APPEND) 
	    {
	      System.out.println("writing index of " + fileName);
	      System.out.println("writing facets of " + fileName + "\n");
	      createFacets(this.agencyNameList, this.type, this.rights, this.coverage, this.type, this.date_published);
	      System.out.println("committing...................");
	      commit();
	      finishWriting();
	      
	    } else {
	      System.out.println("updating " + fileName);
	      indexWriter.updateDocument(new Term("path", Constants.LUCENE_INDEX), doc);
	      
	    }        

	    
	}
	
	public void createFacets(List<String> agencyNameList, String type, String rights, String coverage, String cType, String yearPublished) throws IOException
	  {
		  	IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);

		    CategoryDocumentBuilder categoryDocumentBuilder = new CategoryDocumentBuilder(taxonomyWriter);

		    List<CategoryPath> agencyNameCategoryPathList = new ArrayList<CategoryPath>();
		    CategoryPath agency_category;

		    for(int i=0; i < agencyNameList.size(); i++)
		    {	
		    	agencyNameCategoryPathList.add(agency_category = new CategoryPath("agency", agencyNameList.get(i)));
		    }
		    	
		    if (yearPublished.length() >= 4)
		    	yearPublished = yearPublished.substring(yearPublished.length() - 4, yearPublished.length()); //only take the year bit
		    		    
		    List<CategoryPath> doc1Categories = new ArrayList<CategoryPath>();
		    
		    for(int j=0; j < agencyNameCategoryPathList.size(); j++)
		    {	
		    	doc1Categories.add(agencyNameCategoryPathList.get(j));
		    }
		    doc1Categories.add(new CategoryPath("type", type.trim()));
		    doc1Categories.add(new CategoryPath("rights", rights.trim()));
		    doc1Categories.add(new CategoryPath("coverage", coverage.trim()));
		    doc1Categories.add(new CategoryPath("content type", cType.trim()));
		    doc1Categories.add(new CategoryPath("publication_year", yearPublished));
		    categoryDocumentBuilder.setCategoryPaths(doc1Categories).build(doc);
		    indexWriter.addDocument(doc);
		    
	  }
	
	
	/**
	 * Determine index created or not
	 */
	public boolean noIndex() {
			File[] indexs = INDEX_PATH.listFiles();
			if (indexs.length == 0) {
				return true;
			} else {
				return false;
			}
	}
	
	public void commit() throws CorruptIndexException, IOException
	{		
		taxonomyWriter.commit();	
		indexWriter.commit();	
	}	
	
	public void finishWriting() throws CorruptIndexException, IOException
	{		
		taxonomyWriter.close();		
		indexWriter.close();	
	}
	
	public static void main(String[] args) throws Exception
	{
		List<String> agencyNameList = new ArrayList<String>();
		agencyNameList.add("Republic of Ken Zhai");
		
		OpenGovLuceneIndexer ogl = new OpenGovLuceneIndexer("Ken Zhai's publication", 
				"A good publication", 
				"Description", 
				agencyNameList,
				"10/10/2010",
				"Annual Report",
				"",
				"");			
		String pairTreeURL = new String("\\mnt\\opengovdata\\pairtree_root\\85\\e7\\1a\\1e\\0b\\30\\ee\\54\\9e\\44\\ec\\cc\\f7\\b7\\bd\\81\\obj\\document.pdf");
		ogl.createIndex("123", "Ken Zhai's unique file", pairTreeURL, "Annual Report");
	}
	

}
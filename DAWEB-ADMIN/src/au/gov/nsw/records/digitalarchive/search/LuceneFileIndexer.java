package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import au.gov.nsw.records.digitalarchive.base.Constants;


public class LuceneFileIndexer {

    static Version matchVersion = Version.LUCENE_36;
    static String indexPath = Constants.LUCENE_INDEX;
    static String filePath = "PublicReport.pdf";
    static Analyzer analyzer = new StandardAnalyzer(matchVersion);
    static Analyzer a3 = new CJKAnalyzer(matchVersion);
    
    TikaWrapper tk = new TikaWrapper();
    
    public static void main(String[] args) throws Exception
    {
    	LuceneFileIndexer lfi = new LuceneFileIndexer();
    	//lfi.createIndex();
    	lfi.testSearch();
    	//lfi.testSearch();
    
    }
     
    public void createIndex() throws Exception
    {
         Directory dir = FSDirectory.open(new File(indexPath)) ;
         Document doc = new Document();
         Field titleField = new Field("title", new File(filePath).getName(), Store.YES, Index.ANALYZED);
         String content = new String(tk.getFileContent(filePath));
         Field contentField = new Field("content", content, Store.YES, Index.ANALYZED);
         doc.add(titleField);
         doc.add(contentField);
         IndexWriterConfig iwc = new IndexWriterConfig(matchVersion, analyzer);

         iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
         IndexWriter iw= new IndexWriter(dir, iwc) ;
         iw.addDocument(doc);
         iw.close();
     
     }

    public void testSearch() throws Exception
    {
         Directory dir =FSDirectory.open(new File(indexPath),null) ;
         IndexSearcher is = new IndexSearcher(dir);
         System.out.println(is.maxDoc());
         
         String[] fields = {"title","content"};
         QueryParser qp = new MultiFieldQueryParser(matchVersion, fields, analyzer);
         //QueryParser qp=new QueryParser(matchVersion, "content", analyzer);
         Query query = qp.parse("public");
         //System.out.println(query.toString("content"));
         TopDocs tDocs=is.search(query,10000);
         
         Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlighter\">","</span>");
         Scorer fragmentScorer = new QueryScorer(query);
         Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
         Fragmenter fragmenter = new SimpleFragmenter(100);
         highlighter.setTextFragmenter(fragmenter);
         
         int numTotalHits = tDocs.totalHits;
         System.out.println("Total hits: ["+numTotalHits+"]");
         System.out.println(tDocs.scoreDocs.length);
         
         // int  k = tDocs.scoreDocs[0].doc ;
         //Document doc = is.doc(k) ;
         Document doc = is.doc(0);
         //doc.getField("content");
         String content = doc.get("content");
         
         String hc=highlighter.getBestFragment(a3, "content", content);
         System.out.println("hc:" + hc);
         if(hc==null)
         {
             hc=content.substring(0, Math.min (50, content.length()));
         //    Field contentField=doc.getFieldable("content");
         }
         Field contentField=(Field) doc.getFieldable("content");
         contentField.setValue(hc);
 //        doc.getField("content").setValue(hc);
         System.out.println(doc.get("content"));
         
         TokenStream ts = a3.tokenStream("content", new StringReader(content));
 //         System.out.println("token: "+ts.getAttribute(String.class).toString());
         OffsetAttribute offsetAttribute = ts.getAttribute(OffsetAttribute.class);
         TermAttribute termAttribute = ts.getAttribute(TermAttribute.class);
         while (ts.incrementToken()) {
             int startOffset = offsetAttribute.startOffset();
             int endOffset = offsetAttribute.endOffset();
             String term = termAttribute.term();
             //System.out.println(term);
         }
     }

     public void testCreateRAMandFS() throws Exception{
         Directory fsDir =FSDirectory.open(new File(indexPath)) ;        
         Directory ramDir =new RAMDirectory(fsDir);
         Document doc=new Document();
         Field titleField=new Field("title",new File(filePath).getName(),Store.YES,Index.ANALYZED);
         String content=new String(tk.getFileContent(filePath));
         Field contentField=new Field("content",content,Store.YES,Index.ANALYZED);
         doc.add(titleField);
         doc.add(contentField);
         IndexWriterConfig ramiwc=new IndexWriterConfig(matchVersion, analyzer);
        
         IndexWriter ramiw=new IndexWriter(ramDir, ramiwc) ;
         ramiw.addDocument(doc);
         ramiw.close();
        
         IndexWriterConfig fsiwc=new IndexWriterConfig(matchVersion, analyzer);
         
         fsiwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
         IndexWriter fsiw=new IndexWriter(fsDir, fsiwc) ;
         
         fsiw.addIndexes(ramDir);
         fsiw.commit();
         fsiw.close();
         System.out.println("===Finished");
     }
 }

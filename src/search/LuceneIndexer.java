package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

public class LuceneIndexer {

       /**
       * @param args
       * @throws IOException 
        */
       public static void main(String[] args) throws IOException {
              // TODO Auto-generated method stub

              Directory dir = FSDirectory.open(new File("c:\\LuceneIndex"));
              Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
              IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
              
              iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
              
              File docdir = new File("PublicReport.pdf");
              IndexWriter writer = new IndexWriter(dir, iwc);
              indexDocs(writer, docdir);
       }

       static void indexDocs(IndexWriter writer, File file)
          throws IOException {
            // do not try to index files that cannot be read
            if (file.canRead()) {
              if (file.isDirectory()) {
                String[] files = file.list();
                // an IO error could occur
                if (files != null) {
                  for (int i = 0; i < files.length; i++) {
                    indexDocs(writer, new File(file, files[i]));
                  }
                }
              } else {
       
                FileInputStream fis;
                try {
                  fis = new FileInputStream(file);
                } catch (FileNotFoundException fnfe) {
                  // at least on windows, some temporary files raise this exception with an "access denied" message
                  // checking if the file can be read doesn't help
                  return;
                }
       
                try {
       
                  // make a new, empty document
                  Document doc = new Document();
                  PublicationService ps = new PublicationServiceImpl();
                 // Publication publication = ps.loadPublication(id);

                  doc.add(new Field("path", file.getPath(), Store.YES, Index.NOT_ANALYZED));
                  doc.add(new Field("content", "content from tika", Store.YES, Index.NOT_ANALYZED));
                  //doc.add(new Field("title", publication.getTitle(), Store.YES, Index.NOT_ANALYZED));
                  
                  
                  Field f = new Field("title", "doc title", Store.YES, Index.NOT_ANALYZED);
                  f.setBoost(1.0f);
                  doc.add(f);
                  
                  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    // New index, so we just add the document (no old document can be there):
                    System.out.println("adding " + file);
                    writer.addDocument(doc);
                  } else {
                    // Existing index (an old copy of this document may have been indexed) so 
                    // we use updateDocument instead to replace the old one matching the exact 
                    // path, if present:
                    System.out.println("updating " + file);
                    writer.updateDocument(new Term("path", file.getPath()), doc);
                  }
                  
                } finally {
                  fis.close();
                }
              }
            }
       }
}

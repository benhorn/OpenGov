package au.gov.nsw.records.digitalarchive.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The simplification usage of Apache Tika library
 * @author wisanup
 * @see <a href="http://tika.apache.org/">Apache Tika website</a>
 */
public class TikaWrapper {
	
	private static final Log log = LogFactory.getLog(TikaWrapper.class);
	
	private Tika tika;
	private ParseContext context;
	private DefaultDetector detector;
	private AutoDetectParser parser;
	private ContentHandler handler; 
	
	/**
	 * Create an instance of this class
	 */
	public TikaWrapper(){
		
		tika = new Tika();
        
		context = new ParseContext();
		detector = new DefaultDetector();
		parser = new AutoDetectParser(detector);
		handler =  new DefaultHandler();
		
        context.set(Parser.class, parser);
	}
	
	/**
	 * Get meta data from the specified file
	 * @param filePath the path and file name of the desired file
	 * @return the map of the metadata key and metadata value
	 * @throws IOException
	 */
	public Map<String, String> getMetaData(String filePath) throws Exception{
		            
        URL url;
        File file = new File(filePath);
        url = file.toURI().toURL();
        Metadata metadata = new Metadata();
        InputStream input = TikaInputStream.get(url, metadata);
		Map<String, String> metadataMap = new HashMap<String, String>();
		
		parser.parse(input, handler, metadata, context);
		
		for (String name:metadata.names()){
			String vals = "";
			for (String val:metadata.getValues(name)){
				if (vals.isEmpty()){
					vals = val.trim();
				}else{
					vals = vals + "," + val.trim();
				}
			}
			metadataMap.put(name, vals);
			log.info(( String.format("MetaData: [%s][%s]", name, vals)));
		}
		
		return metadataMap;
	}

	/**
	 * Get file format identification of the specified file
	 * @param filePath the path and file name of the desired file
	 * @return the mime type of the input file
	 * @throws IOException
	 */
	public String getFileType(String filePath) throws IOException{

        File file = new File(filePath);
        // Mark as read only -- Found that Tika somehow modified the file after file identification operation. This caused 
        // md5 sum checking fail
        file.setReadOnly();
        FileInputStream fis = new FileInputStream(file);
        try{
        	return tika.detect(file);
        } finally {
        	fis.close();
        }
	}
	
	public String getFileContent(String filePath) throws Exception
	{
		InputStream input = new FileInputStream(new File(filePath));
		ContentHandler textHandler = new BodyContentHandler(10*1024*1024);
		Metadata metadata = new Metadata();
		Parser parser = new PDFParser();
		parser.parse(input, textHandler, metadata, context);
		input.close();
		return textHandler.toString();
		
	}
	
}

package au.gov.nsw.records.digitalarchive.pdf;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import au.gov.nsw.records.digitalarchive.base.BaseLog;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PDFImageGenerator extends BaseLog
{
	public PDFImageGenerator()
	{}
	
	public void generateThumbnailImage(String fileName, String location) throws IOException
	{
        File file = new File(fileName);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        
        // show the first page
        PDFPage page = pdffile.getPage(1);
        System.out.println("Generating thumbnail....");
        
        Rectangle rect = new Rectangle((int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());

        Image img = page.getImage(rect.width, 
            					  rect.height, 
             				      rect, null, 
             					  true, true);
             
             BufferedImage bimage = null;
             img = new ImageIcon(img).getImage();
             boolean hasAlpha = hasAlpha(img);
             
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             try 
             {
             	int transparency = Transparency.OPAQUE;
             	
                 if (hasAlpha) {
                     transparency = Transparency.BITMASK;
                 }
                 // Create the buffered image
                 GraphicsDevice gs = ge.getDefaultScreenDevice();
                 GraphicsConfiguration gc = gs.getDefaultConfiguration();
                 bimage = gc.createCompatibleImage(82, 108, transparency);
             } catch (HeadlessException e) 
             {
             	logger.info("In class PDFImageGenerator:generateThumbnailImage()");
             }
             
             if (bimage == null) 
             {
                 int type = BufferedImage.TYPE_INT_RGB;
                 if (hasAlpha) 
                 {
                     type = BufferedImage.TYPE_INT_ARGB;
                 }
                 bimage = new BufferedImage(82, 108, type);
             }
                     
             Graphics g = bimage.createGraphics();
             g.drawImage(img, 0, 0, 82, 108, null);
             g.dispose();
             
             File imageFile = new File(location + File.separator + "thumbnail.gif");
             imageFile.createNewFile();
             
             ImageIO.write(bimage, "gif", imageFile);
             raf.close();
    }
	
	public void generateNormalImage(String fileName, String location) throws IOException 
	{
		
		System.out.println("Generating NormalImage........");
        File file = new File(fileName);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
           
        //System.out.println(pdffile.getRoot().);
        
        for (int i = 1; i <= pdffile.getNumPages(); i++)
        {
	        Rectangle rect = new Rectangle(0, 0, (int)pdffile.getPage(i).getBBox().getWidth(), (int)pdffile.getPage(i).getBBox().getHeight());
	        
	        double ratio = 1000 / pdffile.getPage(i).getBBox().getWidth();
	
	        int new_height = (int)(pdffile.getPage(i).getBBox().getHeight() * ( 1000 / pdffile.getPage(i).getBBox().getWidth()));
	        
	        Image img = pdffile.getPage(i).getImage(rect.width, 
	        						  			    rect.height, 
	        						  			    rect, null, 
	        						  			    true, true);
	        
	        BufferedImage bimage = null;
	        img = new ImageIcon(img).getImage();
	        boolean hasAlpha = hasAlpha(img);
	        
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        try 
	        {
	        	int transparency = Transparency.OPAQUE;
	            if (hasAlpha) {
	                transparency = Transparency.BITMASK;
	            }
	            // Create the buffered image
	            GraphicsDevice gs = ge.getDefaultScreenDevice();
	            GraphicsConfiguration gc = gs.getDefaultConfiguration();
	            bimage = gc.createCompatibleImage(1000, new_height, transparency);
	        } catch (HeadlessException e) 
	        {
	        	logger.info("In class PDFImageGenerator:generateNormalImage()");
	        }
	        
	        if (bimage == null) 
	        {
	            int type = BufferedImage.TYPE_INT_RGB;
	            if (hasAlpha) 
	            {
	                type = BufferedImage.TYPE_INT_ARGB;
	            }
	            bimage = new BufferedImage(1000, new_height, type);
	        }
	                
	        Graphics g = bimage.createGraphics();
	        g.drawImage(img, 0, 0, 1000, new_height, null);
	        g.dispose();
	        System.out.println("ratio: " + ratio + " original_height: " + pdffile.getPage(i).getBBox().getHeight() + " original_width: " + pdffile.getPage(i).getBBox().getWidth() + " new height: " + new_height);
	        File imageFile = new File(location  + File.separator + "image" + i + ".png");
	        imageFile.createNewFile();
	        
	        ImageIO.write(bimage, "png", imageFile);
        }
        raf.close();
    }
    	
    private static boolean hasAlpha(Image image) 
    {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        	logger.info("In class ThumnailGenerator:hasAlpha()");
        }

        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
     
   public static void main (String args[])
   {
		ConvertPagesToHiResImages CRT= new ConvertPagesToHiResImages(); 
		String imageLocation = "C:\\file\\images";

		try {
			CRT.ConvertImages("png", "document.pdf", imageLocation, "800", "997");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
}
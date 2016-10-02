package comp9321.assignment2.bookstore.helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;


public class UploadImageHandler {
	public static String saveImageInFolder(Part filePart, String filePath) throws IOException{
		String fullPath = null;
		if (filePart != null && filePart.getSize() != 0){
			File file = new File(filePath);
			
			if (!file.exists()) {
                file.mkdirs();
            }
			
			OutputStream out = null;
			InputStream filecontent = null;
			
			try {
				Date date= new java.util.Date();
				//String fileName = ""+new Timestamp(date.getTime());
				String fileName = System.currentTimeMillis()+".png";

				fullPath = filePath + File.separator + fileName;
                out = new FileOutputStream(new File(fullPath));
                
                filecontent = filePart.getInputStream();
                StringBuilder sb = new StringBuilder();
                int read = 0;
                final byte[] bytes = new byte[1024];
 
                while ((read = filecontent.read(bytes)) != -1) {
                    sb.append(bytes);
                    out.write(bytes, 0, read);
                }
                
			} catch (FileNotFoundException fne) {

            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }
		}
		return "file://"+fullPath;
	}
	
	public static BufferedImage getImageFromPath(String filePath) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filePath));
		} catch (IOException e) {
		}
		
//		JFrame editorFrame = new JFrame("Image Demo");
//        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        
//		ImageIcon imageIcon = new ImageIcon(img);
//        JLabel jLabel = new JLabel();
//        jLabel.setIcon(imageIcon);
//        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);
//
//        editorFrame.pack();
//        editorFrame.setLocationRelativeTo(null);
//        editorFrame.setVisible(true);
		
		
		
		
		return img;
	}

}

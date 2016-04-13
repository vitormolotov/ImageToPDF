/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author vitor
 */
public class Bean {
    private int totalTranformed=0;
    
    private File[] readDirectory(File directory) {

        File[] files = new File[0];
        if (directory.isDirectory()) {
            files = directory.listFiles();
        }

        return files;
    }
    public void analyseDirectories(File directory) {
        analyseRecursive(directory);
        JOptionPane.showMessageDialog(null, "Total de: "+totalTranformed+" arquivos transformados");
    }
    private void analyseRecursive(File directory) {
        String[] files = new String[0];
        if (directory.isDirectory()) {
            files = directory.list();
            try {
                if (files.length > 0) {

                    for (int i = 0; i < files.length; i++) {

                        File subFile = new File(directory.getAbsolutePath()+File.separator+files[i]);
                        if(subFile.isDirectory()){
                            analyseRecursive(subFile);
                        }
                        else if (isValidFile(subFile)){
                            generatePDFFile(subFile);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            
            try{
                if(isValidFile(directory)){
                generatePDFFile(directory);
            }
            }catch(Exception e){
                System.out.println(directory.getName()+" não é um arquivo válido.");
            }
            
            
        }
       // JOptionPane.showMessageDialog(null, "Total de "+totalTranformed+" transformados em pdfs");

    }
    private boolean isValidFile(File file){
        
        if(!file.getName().contains(".")){
            return false;
        }
        
        System.out.println(file.getName() +": nome do arquivo");
        String filename = file.getName();
        int lastDot = filename.lastIndexOf(".");
        
        
        String extension = filename.substring(lastDot);
        
       // System.out.println(filename + ":extensao");
        
       // System.out.println("Analisando arquivo: "+file.getAbsolutePath());
        if(extension.toLowerCase().contains("png") ||
                                 extension.toLowerCase().contains("jpg") ||
                                 extension.toLowerCase().contains("bmp") ||
                                 extension.toLowerCase().contains("jpeg")||
                                 extension.toLowerCase().contains("tiff")){
            return true;   
            
        }
        return false;
     
    }
    private void generatePDFFile(File file) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(replaceExtention(file.getAbsolutePath()) + ".pdf"));
            document.setMargins(0, 0, 0, 0);

            try {

                Image image = Image.getInstance(file.getAbsolutePath());

                //jpeg.setRotatioimagemnDegrees(90);
                float width = image.getWidth();
                float height = image.getHeight();
                Rectangle retangulo = new Rectangle(width, height);
                document.setPageSize(retangulo);
               
                document.open();
                document.add(image);

//                document.add(new Paragraph("A Hello World PDF document."));
                document.close(); // no need to close PDFwriter?
                
                totalTranformed++;
                
            } catch (BadElementException ex) {
                Logger.getLogger(Bean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Bean.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
        String replaceExtention(String file){
        return file.replace(".png", "").replace(".jpg", "").replace(".JPG","").replace(".bmp", "").replace(".BMP", "").replace(".TIFF", "").replace(".tiff", "");
    }

}

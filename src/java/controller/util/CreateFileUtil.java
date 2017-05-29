/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.faces.context.FacesContext;

/**
 *
 * @author kamal
 */
public class CreateFileUtil {
//    
//    private static String fileBackUpOrigine = "backup";
//    private static String ssFileBackUp = "Dropbox";

    public static void createBackUp(String path) throws Exception {
        //vérifier si le dossier backup existe
        System.out.println("hahwaa*********");
        File file = new File(path);
        if (file.exists()) { // C:/...
            System.out.println("le dossier origine existe");
        } else {
            file.mkdirs();
            System.out.println("le dossier n'existe pas");
//            createFolder(path);
            System.out.println("le dossier " + path + " est crée");
        }
    }
    
    public static String getPath(String relativePath){
//        ServletContext servletContext = (ServletContext) ExternalContext.getContext();
//        String absolutePath = servletContext.getRealPath(relativePath);
//        return absolutePath;
//        String relativePath = "/resources/temp/";
        String absolutePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath);
//        File file = new File(absolutePath);
        return absolutePath;
    }

//    private static void createFolder(String path) throws Exception {
//        Runtime runtime = Runtime.getRuntime();
//        //String[] cmdArray = {"move", source, destinataire}; //etc
//        //System.out.println("move "+source+" "+destinataire);
//        String commande = "mkdir " + path;
//        System.out.println("ha l commande " + commande);
//        Process process = runtime.exec("cmd /c " + commande);
//        //Wait until the end of the command execution :
//        process.waitFor();
//    }
    public static void concat2Pdf(String cover, String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReader reader2 = new PdfReader(cover);
// Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
// Create an imported page to be inserted
        PdfImportedPage page = stamper.getImportedPage(reader2, 1);
        stamper.insertPage(1, reader2.getPageSize(1));
        stamper.getUnderContent(1).addTemplate(page, 0, 0);
// Close the stamper and the readers
        stamper.close();
        reader.close();
        reader2.close();
    }
    
    public static String getPathByOsName(){
        String path = "";
        String os = System.getProperty("os.name");
        String home = System.getProperty("user.home");
        String langue = System.getProperty("user.language");
        if(os.startsWith("Wind")){
            if(langue.equals("en")){
                path = home+"\\Downloads";
            }else if(langue.equals("fr")){
                path = home+"\\Téléchargement";
            }
        }else if(os.startsWith("linux")){
//            path = getPath("root");
        }
        return path;
    }
    
}

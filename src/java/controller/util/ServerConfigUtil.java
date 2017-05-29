///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package controller.util;
//
//import bean.CourrierArrivee;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.context.FacesContext;
//import org.primefaces.model.UploadedFile;
//
///**
// *
// * @author Chaimaa-abd
// */
//public class ServerConfigUtil {
//
//    private static String vmParam = "commune.files.path";//chemin dont laquelle on va creer le dosqsier globale qui aura pour bute de contenir la totalitees des dossier d un abonnee
//    private static List<Item> taxePaths = new ArrayList();
//    private static String pathPieceJoint = "resources";
//
//    static {
//        filesPath(taxePaths, "libelles_dossiers");
//    }
//
//    private static String getContextParameter(String paramInWeb) {
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        String myConstantValue = ctx.getExternalContext().getInitParameter(paramInWeb);
//        return myConstantValue;
//    }
//
//    private static void filesPath(List<Item> items, String nameVariable) {
//        String filesName = getContextParameter(nameVariable);
//        String paths[] = filesName.split(",");
//        for (int i = 0; i < paths.length; i++) {
//            String path = paths[i];
//            String[] oneFileConfig = path.split("=");
//            items.add(new Item(oneFileConfig[0], oneFileConfig[1]));
//        }
//    }
//
////    private static String findFileByPath(List<Item> items, String path) {
////        for (int i = 0; i < items.size(); i++) {
////            Item sessionItem = items.get(i);
////            if (sessionItem.getKey().equalsIgnoreCase(path)) {
////                return sessionItem.getObject().toString();
////            }
////        }
////        return null;
////    }
//
//    private static int createFile(File file) {
//        if (!file.exists()) {
//            if (file.mkdir()) {
//                return 1; //file.getName() + " Directory is created!";
//            }
//            return -1;//"Failed to create " + file.getName() + " directory!";
//        }
//        return -2; //file.getName() + " Directory already existe!";
//    }
//
////    private static String getCommuneFilePath(Commune commune, boolean write) {
////        String rootPath = "";
////        if (write) {
////            rootPath = System.getProperty(vmParam);
////            System.out.println("ha pzth:::." + rootPath);
////
////        } else {
////            rootPath = "/e-taxe";
////        }
////        System.out.println("path :::: "+rootPath + "/commune-" + commune.getId());
////        return rootPath + "/commune-" + commune.getId();
////
////    }
//
//    private static void createCommuneFiles(Commune commune) {
//        for (Item taxePath : taxePaths) {
//            createFile(new File(getCommuneFilePath(commune, true) + "\\" + taxePath.getObject().toString()));
//        }
//
//    }
//
//    private static void createRepoIfNotExist(String repo) throws IOException {
//
//        Path path = Paths.get(repo);
//        if (!Files.exists(path)) {
//
//            Files.createDirectories(path);
//        }
//
//    }
//
//    public static String getChangementFilePath(Commune commune, boolean write) {
//        return getCommuneFilePath(commune, write) + "/Changement";
//    }
//
//    public static String getChaumagePath(Commune commune, boolean write) {
//        return getCommuneFilePath(commune, write) + "/Chaumage";
//    }
//    
//    public static String getChangementEtAlienationPath(Commune commune, boolean write) throws IOException {
//            createRepoIfNotExist(getCommuneFilePath(commune, write)+ "/ChangementEtAlienation");
//        return getCommuneFilePath(commune, write) + "/ChangementEtAlienation";
//    }
//    
//    public static String getDeclarationExistencePath(Commune commune, boolean write) throws IOException {
//        createRepoIfNotExist(getCommuneFilePath(commune, write) + "/DeclarationExistence");
//        return getCommuneFilePath(commune, write) + "/DeclarationExistence";
//    }
//
//    public static String getEntrePath(Commune commune, boolean write) {
//        return getCommuneFilePath(commune, write) + "/Entree";
//    }
//
//    public static String getSortiePath(Commune commune, boolean write) {
//        return getCommuneFilePath(commune, write) + "/Sortie";
//    }
//
//    public static String getInfoPath(Commune commune, boolean write) {
//        return getCommuneFilePath(commune, write) + "/info";
//    }
//
//    public static void createCommuneFile(Commune commune) {
//        createFile(new File(getCommuneFilePath(commune, true)));
//        createCommuneFiles(commune);
//    }
//
//    public static void upload(UploadedFile uploadedFile, String destinationPath, String nameOfUploadeFile) {
//        try {
//            CreateFileUtil.createBackUp(destinationPath);
//            String fileSavePath = destinationPath + "\\" + nameOfUploadeFile;
//            System.out.println("ha file save path :::" + fileSavePath);
//            InputStream fileContent = null;
//            fileContent = uploadedFile.getInputstream();
//            System.out.println("ha fileContent "+fileContent.toString());
//            System.out.println("ha faile save path "+new File(fileSavePath).toPath().toString());
//            Files.copy(fileContent, new File(fileSavePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            JsfUtil.addErrorMessage(e, "Erreur Upload image");
//            System.out.println("No update !!!!");
//            e.printStackTrace();
//        } catch (Exception ex) {
//            Logger.getLogger(ServerConfigUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//}

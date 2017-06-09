/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Departement;
import bean.Ecole;
import bean.Employee;
import bean.Stagiaire;
import controller.util.DateUtil;
import controller.util.PdfUtil;
import controller.util.SearchUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;

/**
 *
 * @author PC
 */
@Stateless
public class StagiaireFacade extends AbstractFacade<Stagiaire> {

    
    private static String FILE = "c:/";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    
    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StagiaireFacade() {
        super(Stagiaire.class);
    }

    public List<Stagiaire> findStagiaire(int typeStage, Ecole ecole, Date dateDebut, Date dateFin, Departement departement, Employee encadrant,int genre) {
        System.out.println("recherche Facade");
        String query = "Select s FROM Stagiaire s WHERE 1=1";
        if (typeStage != 0) {
            query += SearchUtil.addConstraint("s", "stage.typeStage", "=", typeStage);
        }
        if (ecole != null) {
            query += SearchUtil.addConstraint("s", "ecoleStagiaire.id", "=", ecole.getId());
        }

        if (dateDebut != null) {
            query += " AND s.dateDebut >= '" + DateUtil.convertUtilToSql(dateDebut) + "'";
        }

        if (dateFin != null) {
            query += " AND s.dateFin <= '" + DateUtil.convertUtilToSql(dateFin) + "'";
        }
        if (departement != null) {
            query += SearchUtil.addConstraint("s", "departement.id", "=", departement.getId());
        }

        if (encadrant != null) {
            query += SearchUtil.addConstraint("s", "encadrant.id", "=", encadrant.getId());
        }
        if(genre != 0){
             query += SearchUtil.addConstraint("s", "genre","=",genre);
        }
        return em.createQuery(query).getResultList();

    }

    public void printPdf(Stagiaire stagiaire) throws JRException, IOException {
        List myList = new ArrayList();
        myList.add(stagiaire);
        Map<String, Object> params = prepareParams(stagiaire);
        PdfUtil.generatePdf(myList, params, "AttestationStage.pdf", "jasper/AttestationS.jasper");
    }
    private Map<String, Object> prepareParams(Stagiaire stagiaire) {
        String genre = null;
        String etudiant = null;
        if (stagiaire.getGenre()==2) {
            genre = "Mrs.";
            etudiant = "etudiant";
        } else if(stagiaire.getGenre()==1) {
            genre = "Mme.";
            etudiant = "etudiante";
        }
        
        String date= DateUtil.convrtStringDate(stagiaire.getDateDebut(), "dd/MM/YY") +" au "+ DateUtil.convrtStringDate(stagiaire.getDateFin(), "dd/MM/YY")+"";
        String division = stagiaire.getDepartement().getNom()+" du secretariat general de la Prefecture de Marrakech.";
        String name = stagiaire.getNom() + "  " + stagiaire.getPrenom() + "  de CIN  " + stagiaire.getCin();
        String school = stagiaire.getEcoleStagiaire().getNom() + " option " + stagiaire.getDomaine().getNom();
      String kolchi="    Le Wali de la Region Marrakech-Safi Gouverneur de la prefecture de Marrakech atteste par la presente que"
              + " : "+ genre +" " +name+"  "+ etudiant+" a " + school + " a effectuer un stage du " +date+ " a la division du  " +division+"";
        Map<String, Object> params = new HashMap();
       // params.put("date", date);
       // params.put("school", school);
       // params.put("Division", division);
       // params.put("genre", genre);
      //  params.put("etudiant", etudiant);
       // params.put("name", name);
       params.put("kolchi", kolchi);
        return params;
    }

    public void clone(Stagiaire stagiaireSource, Stagiaire stagiaireDestination) {
        stagiaireDestination.setCin(stagiaireSource.getCin());
        stagiaireDestination.setDateDebut(stagiaireSource.getDateDebut());
        stagiaireDestination.setDateFin(stagiaireSource.getDateFin());
        stagiaireDestination.setDepartement(stagiaireSource.getDepartement());
        stagiaireDestination.setDomaine(stagiaireSource.getDomaine());
        stagiaireDestination.setEcoleStagiaire(stagiaireSource.getEcoleStagiaire());
        stagiaireDestination.setEncadrant(stagiaireSource.getEncadrant());
        stagiaireDestination.setMail(stagiaireSource.getMail());
        stagiaireDestination.setNom(stagiaireSource.getNom());
        stagiaireDestination.setNumTel(stagiaireSource.getNumTel());
        stagiaireDestination.setStage(stagiaireSource.getStage());
        stagiaireDestination.setPrenom(stagiaireSource.getPrenom());
    }

    public Stagiaire clone(Stagiaire stagiaire) {
        Stagiaire cloned = new Stagiaire();
        clone(stagiaire, cloned);
        return cloned;
    }

    
    
             

    public static void addTitlePage(Document document, List<Stagiaire> items,boolean nomB  ,boolean  prenomB ,boolean cinB ,boolean mailB ,boolean numTelB ,boolean genreB ,boolean typeB ,boolean dateDebutB ,boolean dateFinB ,boolean encadrantB ,boolean depB ,boolean ecoleB ,boolean filiereB)
            throws DocumentException, IOException {
        int rowCount = countRowChek(nomB  ,prenomB , cinB , mailB , numTelB , genreB , typeB , dateDebutB , dateFinB , encadrantB , depB , ecoleB ,filiereB);
        Paragraph preface = new Paragraph();
        Paragraph preface1 = new Paragraph();
        // We add one empty line 
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("             Royaume du Maroc\n         Ministère de l'Intérieur\nWilaya de la Région de Marrakech-Safi\n         Préfecture de Marrakech\n             Secrétariat Général\n   Division des Ressources Humaines\n         Et des Moyens Généraux\n   Service de la Formation Continue", smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "                                     Liste des Stagiaire ",
                catFont));
        addEmptyLine(preface, 1);
        document.add(preface);
        PdfPTable table = new PdfPTable(rowCount);
        System.out.println(rowCount);
        PdfPCell c1;
//        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(c1);
        if (nomB  == true) {
            c1 = new PdfPCell(new Phrase("Nom"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (prenomB  == true) {
            c1 = new PdfPCell(new Phrase("Prénom"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (cinB  == true) {
            c1 = new PdfPCell(new Phrase("CIN"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (mailB  == true) {
            c1 = new PdfPCell(new Phrase("Email "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (numTelB  == true) {
            c1 = new PdfPCell(new Phrase("Tél"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (genreB  == true) {
            c1 = new PdfPCell(new Phrase("Genre"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (typeB  == true) {
            c1 = new PdfPCell(new Phrase("Type"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateDebutB  == true) {
            c1 = new PdfPCell(new Phrase("Date Début"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateFinB  == true) {
            c1 = new PdfPCell(new Phrase("Date Fin"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

        }
        if (encadrantB  == true) {
            c1 = new PdfPCell(new Phrase("Encadrant"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (depB  == true) {
            c1 = new PdfPCell(new Phrase("Département"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (ecoleB   == true) {
            c1 = new PdfPCell(new Phrase("Ecole"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (filiereB  == true) {
            c1 = new PdfPCell(new Phrase("Filière"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        table.setHeaderRows(1);
        for (Stagiaire item : items) {

            
            if (nomB  == true) {
                table.addCell(" " +item.getNom());
            }
            if (prenomB   == true) {
                table.addCell(" "+item.getPrenom());
            }
            if (cinB   == true) {
                table.addCell( ""+item.getCin());
            }
            if (mailB   == true) {
                table.addCell(" "+item.getMail());
            }
            if (numTelB   == true) {
                table.addCell(" "+item.getNumTel());
            }
            if (genreB   == true) {
                table.addCell(" "+item.getGenre());
            }
            if (typeB    == true) {
                table.addCell(" "+item.getStage().getTypeStage());
            }
            if (dateDebutB   == true) {
                table.addCell(" "+item.getDateDebut());
            }
            if (dateFinB   == true) {
                table.addCell(" "+item.getDateFin());
            }
            if (encadrantB   == true) {
                table.addCell(" "+item.getEncadrant());
            }
            
            if (depB   == true) {
                table.addCell(" "+item.getDepartement());
            }
            if (ecoleB    == true) {
                table.addCell(" "+item.getEcoleStagiaire());
            }
            if (filiereB   == true) {
                table.addCell(" "+item.getDomaine());
            }
        }
        document.add(table);

        addEmptyLine(preface1, 3);
        document.add(preface1);
        document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n____________________________________________________________________________________", FontFactory.getFont(FontFactory.TIMES_BOLD)));
        document.add(new Paragraph("           \t\t\t\t                 Avenu 11 Janvier Daoudiate, Wilaya de la Région de Marrakech-Safi, Maroc \n", FontFactory.getFont(FontFactory.TIMES_ITALIC)));
        document.add(new Paragraph("                                                      Tél : 05 24 33 27 40  - fax : 05 24 30 88 13 \n", FontFactory.getFont(FontFactory.TIMES_ITALIC)));
        document.add(new Paragraph("       E-mail :wilaya.marrakech.asfi@gmail.com - Site web : http://www.ville-marrakech.ma/", FontFactory.getFont(FontFactory.TIMES_ITALIC)));

    }

    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static int countRowChek(boolean  nomB  ,boolean  prenomB ,boolean cinB ,boolean mailB ,boolean numTelB ,boolean genreB ,boolean typeB ,boolean dateDebutB ,boolean dateFinB ,boolean encadrantB ,boolean depB ,boolean ecoleB ,boolean filiereB) {
        int rowCount = 0;
        if (nomB  == true) {
            rowCount += 1;
        }
        if (prenomB == true) {
            rowCount += 1;
        }
        if (cinB == true) {
            rowCount += 1;
        }
        if (mailB  == true) {
            rowCount += 1;
        }
        if (numTelB  == true) {
            rowCount += 1;
        }
        if (genreB  == true) {
            rowCount += 1;
        }
        if (typeB  == true) {
            rowCount += 1;
        }
        if (dateDebutB  == true) {
            rowCount += 1;
        }
        if (dateFinB ==true) {
            rowCount += 1;
        }
        if (encadrantB ==true) {
            rowCount += 1;
        }
        if (depB ==true) {
            rowCount += 1;
        }
        if (ecoleB ==true) {
            rowCount += 1;
        }
        if (filiereB ==true) {
            rowCount += 1;
        }
        return rowCount;
    }
             
}

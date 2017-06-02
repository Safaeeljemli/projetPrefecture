/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierArrivee;
import bean.DestinataireExpediteur;
import bean.ModeTraitement;
import bean.SousClasse;
import controller.util.DateUtil;
import controller.util.SearchUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author safa
 */
@Stateless
public class CourrierArriveeFacade extends AbstractFacade<CourrierArrivee> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    private static String FILE = "c:/";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourrierArriveeFacade() {
        super(CourrierArrivee.class);
    }

    public void clone(CourrierArrivee courrierSource, CourrierArrivee courrierDestination) {
        courrierDestination.setObjet(courrierSource.getObjet());
        courrierDestination.setCodeA_V(courrierSource.getCodeA_V());
        courrierDestination.setDateEnregistrement(courrierSource.getDateEnregistrement());
        courrierDestination.setDateEnregistrementBOW_TRANS_RLAN(courrierSource.getDateEnregistrementBOW_TRANS_RLAN());
        courrierDestination.setDateEnregistrementDRHMG(courrierSource.getDateEnregistrementDRHMG());
        courrierDestination.setDestinataireExpediteur(courrierSource.getDestinataireExpediteur());
        courrierDestination.setModeTraitement(courrierSource.getModeTraitement());
        courrierDestination.setMotsCle(courrierSource.getMotsCle());
        courrierDestination.setN_enregistrement(courrierSource.getN_enregistrement());
        courrierDestination.setN_enregistrementBOW_TRANS_RLAN(courrierSource.getN_enregistrementBOW_TRANS_RLAN());
        courrierDestination.setN_enregistrementDRHMG(courrierSource.getN_enregistrementDRHMG());
        courrierDestination.setSousClasse(courrierSource.getSousClasse());
    }

    public CourrierArrivee clone(CourrierArrivee courrierArrivee) {
        CourrierArrivee cloned = new CourrierArrivee();
        clone(courrierArrivee, cloned);
        return cloned;
    }

    public List<CourrierArrivee> findCourrier(ModeTraitement modeTR) {
        System.out.println("tttetetd");
        String query = "SELECT s FROM CourrierArrivee s WHERE 1=1";
        if (modeTR != null) {
            System.out.println("zzzzzzzzzzze");
            query += SearchUtil.addConstraint("s", "modeTraitement", "=", modeTR);
        }
        return em.createQuery(query).getResultList();
    }

    public List<CourrierArrivee> findCourrierArrivee(Date dateC, Date dateDRHMG, Date dateBTR, SousClasse sousClasse, DestinataireExpediteur expediteur, ModeTraitement modeTraitement, String codeA) {
        System.out.println("facaaade");
        String query = "Select c FROM CourrierArrivee c WHERE 1=1";
        if (dateC != null) {
//            query +=SearchUtil.addConstraintDate("c", "dateEnregistrement", "=",  DateUtil.convertUtilToSql(dateC) );
            query += " AND c.dateEnregistrement <= '" + DateUtil.convertUtilToSql(dateC) + "'";
            System.out.println("" + DateUtil.convertUtilToSql(dateC));
        }
        if (dateDRHMG != null) {
            query += " AND c.dateEnregistrementDRHMG = '" + DateUtil.convertUtilToSql(dateDRHMG) + "'";
        }
        if (dateBTR != null) {
            query += " AND c.dateEnregistrementBOW_TRANS_RLAN = '" + DateUtil.convertUtilToSql(dateBTR) + "'";
        }
        if (sousClasse != null) {
            query += " AND c.sousClasse.id='" + sousClasse.getId() + "'";
        }
        if (expediteur != null) {
            query += SearchUtil.addConstraint("c", "destinataireExpediteur.id", "=", expediteur.getId());
        }

        if (modeTraitement != null) {
            query += " AND c.modeTraitement.id='" + modeTraitement.getId() + "'";
        }
        if (codeA != null) {
            query += SearchUtil.addConstraint("c", "codeA_V", "=", codeA);
        }
        return em.createQuery(query).getResultList();

    }

    public String generateCodeA(String abrv, Date dateCreation, int sousClasse) {
        String code;
        code = sousClasse + abrv + DateUtil.convrtStringDate(dateCreation, "yy") + "A";
        return code;
    }

    public static void addTitlePage(Document document, List<CourrierArrivee> items, boolean expediteurCheck, boolean motsCleCheck, boolean objetCheck, boolean modeTraitementCheck, boolean n_DRHMGCheck, boolean n_enCheck, boolean n_BOW_TRANS_RLANcheck, boolean codeA_Vcheck, boolean dateEnregcheck, boolean dateBOW_TRANS_RLANcheck, boolean sousClasseCheck)
            throws DocumentException, IOException {
        int rowCount = countRowChek(expediteurCheck, motsCleCheck, objetCheck, modeTraitementCheck, n_DRHMGCheck, n_enCheck, n_BOW_TRANS_RLANcheck, codeA_Vcheck, dateEnregcheck, dateBOW_TRANS_RLANcheck, sousClasseCheck);
        Paragraph preface = new Paragraph();
        Paragraph preface1 = new Paragraph();
        // We add one empty line 
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("             Royaume du Maroc\n         Ministère de l'Intérieur\nWilaya de la Région de Marrakech-Safi\n         Préfecture de Marrakech\n             Secrétariat Général\n   Division des Ressources Humaines\n         Et des Moyens Généraux\n   Service de la Formation Continue", smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "                                     Liste des Courrier Arrivées ",
                catFont));
        addEmptyLine(preface, 1);
        document.add(preface);
        PdfPTable table = new PdfPTable(rowCount);
        System.out.println(rowCount);
        PdfPCell c1;
//        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(c1);
        if (expediteurCheck == true) {
            c1 = new PdfPCell(new Phrase("Expediteur"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (motsCleCheck == true) {
            c1 = new PdfPCell(new Phrase("motsCleCheck"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (objetCheck == true) {
            c1 = new PdfPCell(new Phrase("objetCheck "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (modeTraitementCheck == true) {
            c1 = new PdfPCell(new Phrase("modeTraitementCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (n_DRHMGCheck == true) {
            c1 = new PdfPCell(new Phrase("n_DRHMGCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (n_enCheck == true) {
            c1 = new PdfPCell(new Phrase("n_enCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (n_BOW_TRANS_RLANcheck == true) {
            c1 = new PdfPCell(new Phrase("n_BOW_TRANS_RLANcheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (codeA_Vcheck == true) {
            c1 = new PdfPCell(new Phrase("codeA_Vcheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateEnregcheck == true) {
            c1 = new PdfPCell(new Phrase("dateEnregcheck "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

        }
        if (dateBOW_TRANS_RLANcheck == true) {
            c1 = new PdfPCell(new Phrase("dateBOW_TRANS_RLANcheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (sousClasseCheck == true) {
            c1 = new PdfPCell(new Phrase("sousClasseCheck   "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        table.setHeaderRows(1);
        for (CourrierArrivee item : items) {

            
            if (expediteurCheck == true) {
                table.addCell(item.getDestinataireExpediteur().getNom());
            }
            if (motsCleCheck  == true) {
                table.addCell(item.getMotsCle());
            }
            if (objetCheck  == true) {
                table.addCell(item.getObjet());
            }
            if (modeTraitementCheck  == true) {
                table.addCell(item.getModeTraitement().getNom());
            }
            if (n_DRHMGCheck  == true) {
                table.addCell(" "+item.getN_enregistrementDRHMG());
            }
            if (n_enCheck  == true) {
                table.addCell(" "+item.getN_enregistrement());
            }
            if (n_BOW_TRANS_RLANcheck   == true) {
                table.addCell(" "+item.getN_enregistrementBOW_TRANS_RLAN());
            }
            if (codeA_Vcheck  == true) {
                table.addCell(item.getCodeA_V());
            }
            if (dateEnregcheck  == true) {
                table.addCell(" "+item.getDateEnregistrement());
            }
            if (dateBOW_TRANS_RLANcheck  == true) {
                table.addCell(" "+item.getDateEnregistrementBOW_TRANS_RLAN());
            }
            
            if (sousClasseCheck  == true) {
                table.addCell(" "+item.getSousClasse().getNom());
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

    public static int countRowChek(boolean expediteurCheck, boolean motsCleCheck, boolean objetCheck,
            boolean modeTraitementCheck, boolean n_DRHMGCheck, boolean n_enCheck, boolean n_BOW_TRANS_RLANcheck,
            boolean codeA_Vcheck, boolean dateEnregcheck, boolean dateBOW_TRANS_RLANcheck, boolean sousClasseCheck) {
        int rowCount = 0;
        if (expediteurCheck == true) {
            rowCount += 1;
        }
        if (motsCleCheck == true) {
            rowCount += 1;
        }
        if (objetCheck == true) {
            rowCount += 1;
        }
        if (modeTraitementCheck == true) {
            rowCount += 1;
        }
        if (n_DRHMGCheck == true) {
            rowCount += 1;
        }
        if (n_enCheck == true) {
            rowCount += 1;
        }
        if (n_BOW_TRANS_RLANcheck == true) {
            rowCount += 1;
        }
        if (codeA_Vcheck==true) {
            rowCount += 1;
        }
        if (dateEnregcheck==true) {
            rowCount += 1;
        }
        if (dateBOW_TRANS_RLANcheck==true) {
            rowCount += 1;
        }
        if (sousClasseCheck==true) {
            rowCount += 1;
        }
        return rowCount;
    }

//    public String initFile(){
//        String file = "C:\\Users\\safa\\Desktop\\test\\firstTest.pdf";
//        
//        
//    }
}

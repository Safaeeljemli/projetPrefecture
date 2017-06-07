/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.Finalite;
import bean.SousClasse;
import controller.util.DateUtil;
import controller.util.SearchUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
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

/**
 *
 * @author safa
 */
@Stateless
public class CourrierProduitFacade extends AbstractFacade<CourrierProduit> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public CourrierProduitFacade() {
        super(CourrierProduit.class);
    }

    public void clone(CourrierProduit courrierSource, CourrierProduit courrierDestination) {
        courrierDestination.setCodeP_V(courrierSource.getCodeP_V());
        courrierDestination.setCourrierArrivee(courrierSource.getCourrierArrivee());
        courrierDestination.setDateCreation(courrierSource.getDateCreation());
        courrierDestination.setDateEnvoiAuBOW_TRANS(courrierSource.getDateEnvoiAuBOW_TRANS());
        courrierDestination.setDateEnvoiParBOW_TRANS(courrierSource.getDateEnvoiParBOW_TRANS());
        courrierDestination.setDateEnvoiePourValidation(courrierSource.getDateEnvoiePourValidation());
        courrierDestination.setDateRetourDeLaMinuteDuBOW_TRANS(courrierSource.getDateRetourDeLaMinuteDuBOW_TRANS());
        courrierDestination.setDateRetourDocument(courrierSource.getDateRetourDocument());
        courrierDestination.setEtat(courrierSource.getEtat());
        courrierDestination.setFinalite(courrierSource.getFinalite());
        courrierDestination.setN_EnvoiParBOW_TRANS(courrierSource.getN_EnvoiParBOW_TRANS());
        courrierDestination.setN_dordre(courrierSource.getN_dordre());
        courrierDestination.setObjet(courrierSource.getObjet());
        courrierDestination.setRaisonSignature(courrierSource.getRaisonSignature());
        courrierDestination.setSousClasse(courrierSource.getSousClasse());
    }

    public CourrierProduit clone(CourrierProduit courrierProduit) {
        CourrierProduit cloned = new CourrierProduit();
        clone(courrierProduit, cloned);
        return cloned;
    }

    public List<CourrierProduit> findCourrierProduit(Date dateMin, Date dateMax,  Finalite finalite, DestinataireExpediteur destinataire,int etat,SousClasse sousClasse) {
        String query = "Select cp FROM CourrierProduit cp WHERE 1=1";
        if (dateMin != null) {
            query += " AND cp.dateCreation >= '" + DateUtil.convertUtilToSql(dateMin) + "'";
        }
        if (dateMax != null) {
            query += " AND cp.dateCreation <= '" + DateUtil.convertUtilToSql(dateMax) + "'";
        }
        if (finalite != null) {
            query += SearchUtil.addConstraint("cp", "finalite.id", "=", finalite.getId());
        }

        if (destinataire != null) {
            query += SearchUtil.addConstraint("cp", "destinataireExpediteur.id", "=", destinataire.getId());
        }
        if (etat>0) {
            query += SearchUtil.addConstraint("cp", "etat", "=", etat);
        }
        if (sousClasse != null) {
            query += SearchUtil.addConstraint("cp", "sousClasse.id", "=", sousClasse.getId());
        }
        System.out.println("eeeeeeeee");
        return em.createQuery(query).getResultList();

    }

    public String generateCodeP(String abrv, Date dateCreation, int sousClasse) {
        String code;
        code = sousClasse + abrv + DateUtil.convrtStringDate(dateCreation, "yy") + "P";
        return code;
    }

    public static void addTitlePage(Document document, List<CourrierProduit> items, boolean n_ordreCheck, boolean dateCrtCheck, boolean objetCheck, boolean dateEnvValiCheck, boolean dateRetrCheck, boolean etatCheck, boolean raisonCheck, boolean codePCheck, boolean dateEnvoiAuBTCheck, boolean dateEnvoiParBTCheck, boolean dateRetMinutBTCheck, boolean n_EnvoiParBTCheck, boolean sousClasseCheck, boolean finaliteCheck, boolean destinataireCheck, boolean courrierArriveeCodeCheck)
            throws DocumentException, IOException {
        int rowCount = countRowChek(n_ordreCheck, dateCrtCheck, objetCheck, dateEnvValiCheck, dateRetrCheck, etatCheck, raisonCheck, codePCheck, dateEnvoiAuBTCheck, dateEnvoiParBTCheck, dateRetMinutBTCheck, n_EnvoiParBTCheck, sousClasseCheck, finaliteCheck, destinataireCheck, courrierArriveeCodeCheck);

        Paragraph preface = new Paragraph();
        Paragraph preface1 = new Paragraph();
        // We add one empty line 
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("             Royaume du Maroc\n         Ministère de l'Intérieur\nWilaya de la Région de Marrakech-Safi\n         Préfecture de Marrakech\n             Secrétariat Général\n   Division des Ressources Humaines\n         Et des Moyens Généraux\n   Service de la Formation Continue", smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "                                     Liste des Courrier Produits ",
                catFont));
        addEmptyLine(preface, 1);
        document.add(preface);
        PdfPTable table = new PdfPTable(rowCount);
        System.out.println(rowCount);
        PdfPCell c1;
//        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(c1);
        if (n_ordreCheck == true) {
            c1 = new PdfPCell(new Phrase("n_ordreCheck;"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateCrtCheck == true) {
            c1 = new PdfPCell(new Phrase("dateCrtCheck "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (objetCheck == true) {
            c1 = new PdfPCell(new Phrase("objetCheck "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateEnvValiCheck == true) {
            c1 = new PdfPCell(new Phrase("dateEnvValiCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateRetrCheck == true) {
            c1 = new PdfPCell(new Phrase("dateRetrCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (codePCheck == true) {
            c1 = new PdfPCell(new Phrase("codePCheck   "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (etatCheck == true) {
            c1 = new PdfPCell(new Phrase("etatCheck   "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (raisonCheck == true) {
            c1 = new PdfPCell(new Phrase("raisonCheck   "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateEnvoiAuBTCheck == true) {
            c1 = new PdfPCell(new Phrase("dateEnvoiAuBTCheck "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

        }
        if (dateEnvoiParBTCheck == true) {
            c1 = new PdfPCell(new Phrase("dateEnvoi  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (dateRetMinutBTCheck == true) {
            c1 = new PdfPCell(new Phrase("dateRetour  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (n_EnvoiParBTCheck == true) {
            c1 = new PdfPCell(new Phrase("n_EnvoiParBTCheck  "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (sousClasseCheck == true) {
            c1 = new PdfPCell(new Phrase("sousClasseCheck   "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (finaliteCheck == true) {
            c1 = new PdfPCell(new Phrase("finaliteCheck    "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (destinataireCheck == true) {
            c1 = new PdfPCell(new Phrase("destinataireCheck    "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        if (courrierArriveeCodeCheck == true) {
            c1 = new PdfPCell(new Phrase("courrierArriveeCodeCheck    "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        table.setHeaderRows(1);
        for (CourrierProduit item : items) {

            if (n_ordreCheck == true) {
                table.addCell(" "+item.getN_dordre());
            }
            if (dateCrtCheck == true) {
                table.addCell(" "+item.getDateCreation());
            }
            if (objetCheck == true) {
                table.addCell(item.getObjet());
            }
            if (dateEnvValiCheck == true) {
                table.addCell(" " +item.getDateEnvoiePourValidation());
            }
            if (dateRetrCheck == true) {
                table.addCell(" " + item.getDateRetourDocument());
            }
            if (codePCheck  == true) {
                table.addCell(" " + item.getCodeP_V());
            }
            if (etatCheck  == true) {
                table.addCell(" " + item.getEtat());
            }
            if (raisonCheck  == true) {
                table.addCell(item.getRaisonSignature());
            }
            if (dateEnvoiAuBTCheck == true) {
                table.addCell(" " + item.getDateEnvoiAuBOW_TRANS());
            }
            if (dateEnvoiParBTCheck == true) {
                table.addCell(" " + item.getDateEnvoiParBOW_TRANS());
            }
            if (dateRetMinutBTCheck == true) {
                table.addCell(" " + item.getDateRetourDeLaMinuteDuBOW_TRANS());
            }
            if (n_EnvoiParBTCheck == true) {
                table.addCell(" " + item.getN_EnvoiParBOW_TRANS());
            }
            if (sousClasseCheck == true) {
                table.addCell(" " + item.getSousClasse().getNom());
            }
            if (finaliteCheck  == true) {
                table.addCell(" " + item.getFinalite().getNom());
            }
            if (destinataireCheck  == true) {
                table.addCell(" " + item.getDestinataireExpediteur());
            }
            if (courrierArriveeCodeCheck == true) {
                table.addCell(" " + item.getCourrierArrivee().getCodeA_V());
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

    public static int countRowChek(boolean n_ordreCheck, boolean dateCrtCheck, boolean objetCheck, boolean dateEnvValiCheck, boolean dateRetrCheck, boolean etatCheck, boolean raisonCheck, boolean codePCheck, boolean dateEnvoiAuBTCheck, boolean dateEnvoiParBTCheck, boolean dateRetMinutBTCheck, boolean n_EnvoiParBTCheck, boolean sousClasseCheck, boolean finaliteCheck, boolean destinataireCheck, boolean courrierArriveeCodeCheck) {
        int rowCount = 0;
        if (n_ordreCheck == true) {
            rowCount += 1;
        }
        if (dateCrtCheck == true) {
            rowCount += 1;
        }
        if (objetCheck == true) {
            rowCount += 1;
        }
        if (dateEnvValiCheck == true) {
            rowCount += 1;
        }
        if (dateRetrCheck == true) {
            rowCount += 1;
        }
        if (etatCheck == true) {
            rowCount += 1;
        }
        if (codePCheck == true) {
            rowCount += 1;
        }
        if (raisonCheck == true) {
            rowCount += 1;
        }
        if (dateEnvoiAuBTCheck == true) {
            rowCount += 1;
        }
        if (dateEnvoiParBTCheck == true) {
            rowCount += 1;
        }
        if (dateRetMinutBTCheck == true) {
            rowCount += 1;
        }
        if (n_EnvoiParBTCheck == true) {
            rowCount += 1;
        }
        if (sousClasseCheck == true) {
            rowCount += 1;
        }
        if (finaliteCheck == true) {
            rowCount += 1;
        }
        if (destinataireCheck == true) {
            rowCount += 1;
        }
        if (courrierArriveeCodeCheck == true) {
            rowCount += 1;
        }
        return rowCount;
    }

}

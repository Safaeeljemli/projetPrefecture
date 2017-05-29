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
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import controller.util.DateUtil;
import controller.util.SearchUtil;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

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
    
//     public CourrierArrivee imprimer() {
//        Document document= new Document();
//        ByteArrayOutputStream bas= new ByteArrayOutputStream();
//        
//            PdfWriter.getInstance(document, bas);
//            document.add
//            
//        
//    }
//    

}

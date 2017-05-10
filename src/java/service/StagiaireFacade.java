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
import controller.util.SearchUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PC
 */
@Stateless
public class StagiaireFacade extends AbstractFacade<Stagiaire> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StagiaireFacade() {
        super(Stagiaire.class);
    }
     public List<Stagiaire> findStagiaire(int typeStage, Ecole ecole, Date dateDebut, Date dateFin, Departement departement, Employee encadrant) {
        System.out.println("recherche Facade");
        String query = "Select s FROM Stagiaire s WHERE 1=1";
        if (typeStage != 0) {
            query += SearchUtil.addConstraint("s", "stage.typeStage", "=", typeStage);
        }
        if (ecole != null) {
            query += SearchUtil.addConstraint("s", "ecoleStagiaire.id", "=", ecole.getId());
        }
        
       if (dateDebut != null) {
           query += " AND s.dateDebut >" + dateDebut;
           // query += SearchUtil.addConstraintDate("s", "s.dateDebut", ">",  dateDebut);
       }
        if (dateFin != null) {
             query += " AND s.dateFin <" + dateFin;
        }
            //query += SearchUtil.addConstraintDate("s", "s.dateFin", "<",  dateFin);
     

        if (departement != null) {
            query += SearchUtil.addConstraint("s", "departement.id", "=", departement.getId());
        }

        if (encadrant != null) {
            query += SearchUtil.addConstraint("s", "encadrant.id", "=", encadrant.getId());
        }
        return em.createQuery(query).getResultList();

    

     }
//      public void printPdf(Stagiaire stagiaire,User user) throws JRException, IOException {
//            Map<String, Object> params = prepareParams(stagiaire,user);
//            PdfUtil.generatePdf(myList, params, "bordereauAnnuel" + stagiaire.getId() + ".pdf", "jasper/taxeAnnuelleRapport.jasper");
//        
//    }
//       private Map<String, Object> prepareParams(Stagiaire stagiaire,User user) {
//        String nature;
//        String status = "NoN";
//        String cinOuRcRedevable;
//        String adresse = taxeAnnuelBoisson.getLocale().getRue().getQuartier().getSecteur().getName() + " ";
//        if (taxeAnnuelBoisson.getLocale().getRue().getQuartier().getSecteur().getName().equals(taxeAnnuelBoisson.getLocale().getRue().getQuartier().getName())) {
//            adresse += "Quartier " + taxeAnnuelBoisson.getLocale().getRue().getQuartier().getName() + " ";
//        }
//        if (taxeAnnuelBoisson.getLocale().getRue().getQuartier().getName().equals(taxeAnnuelBoisson.getLocale().getRue().getName())) {
//            adresse += "Rue " + taxeAnnuelBoisson.getLocale().getRue().getName() + " ";
//        }
//        adresse += taxeAnnuelBoisson.getLocale().getComplementAdress();
//        if (taxeAnnuelBoisson.getRedevable().getNature() == 1) {
//            nature = "Gerant";
//        } else {
//            nature = "proprietaire";
//        }
//        if (taxeAnnuelBoisson.getRedevable().getCin() != null || !taxeAnnuelBoisson.getRedevable().getCin().equals("")) {
//            cinOuRcRedevable = taxeAnnuelBoisson.getRedevable().getCin();
//        } else {
//            cinOuRcRedevable = taxeAnnuelBoisson.getRedevable().getRc();
//        }
//
//        if (taxeAnnuelBoisson.getFinished() == 1) {
//            status = "Terminer";
//        } else if (taxeAnnuelBoisson.getFinished() == -1 || taxeAnnuelBoisson.getFinished() == 0) {
//            status = "Non Termine";
//        }
//
//        Map<String, Object> params = new HashMap();
//        params.put("redevableName", taxeAnnuelBoisson.getRedevable().getNom());
//        params.put("activite", taxeAnnuelBoisson.getLocale().getTypeLocal().getNom());
//        params.put("taxYear", taxeAnnuelBoisson.getAnnee());
//        params.put("montantAnnuel", taxeAnnuelBoisson.getMontantTaxeannuel());
//        params.put("idRedevable", taxeAnnuelBoisson.getRedevable().getId());
//        params.put("cinOrRcRedevable", cinOuRcRedevable);
//        params.put("nomLocale", taxeAnnuelBoisson.getLocale().getNom());
//        params.put("natureRedevable", nature);
//        params.put("adresseLocale", adresse);
//        params.put("idTaxeAnn", taxeAnnuelBoisson.getId());
//        params.put("totalEnLettre", FrenchNumberToWords.convert(Math.round(taxeAnnuelBoisson.getMontantTaxeannuel())));
//        params.put("status", status);
//        params.put("userName", user.getNom());
//        return params;
 //   }

}

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;

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
        return em.createQuery(query).getResultList();

    }

    public void printPdf(Stagiaire stagiaire) throws JRException, IOException {
        List myList = new ArrayList();
        myList.add(stagiaire);
        Map<String, Object> params = prepareParams(stagiaire);
        PdfUtil.generatePdf(myList, params, "AttestationStage.pdf", "jasper/AttestationS.jasper");

    }

    private Map<String, Object> prepareParams(Stagiaire stagiaire) {
        String genre;
        String etudiant;
        if (stagiaire.getGenre().equals("Homme")) {
            genre = "Mrs.";
            etudiant = "etudiant";
        } else {
            genre = "Mme.";
            etudiant = "etudiante";
        }
        String date= stagiaire.getDateDebut().toString()+" au "+stagiaire.getDateFin().toString();
        String division = stagiaire.getDepartement().getNom()+" du secretariat general de la Prefecture de Marrakech.";
        String name = stagiaire.getNom() + " " + stagiaire.getPrenom() + "  de CIN " + stagiaire.getCin();
        String school = stagiaire.getEcoleStagiaire().getNom() + " " + stagiaire.getDomaine().getNom();
         String kolchi="Le Wali de la Region Marrakech-Safi Gouverneur de la prefecture de Marrakech atteste par la presente que :"+ genre +" " +name+" "+ etudiant+" a "+school+" a effectuer un stage du: "+date+" a la "+division;
        Map<String, Object> params = new HashMap();
        params.put("date", date);
        params.put("school", school);
        params.put("Division", division);
        params.put("genre", genre);
        params.put("etudiant", etudiant);
        params.put("name", name);
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

}

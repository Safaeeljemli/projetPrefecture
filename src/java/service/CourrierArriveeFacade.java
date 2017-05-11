/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierArrivee;
import bean.DestinataireExpediteur;
import bean.ModeTraitement;
import controller.util.SearchUtil;
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

    public List<CourrierArrivee> findCourrierArrivee(Date dateMinC, Date dateMaxC, Date dateMinDRHMG, Date dateMaxDRHMG, Date dateMinBTR, Date dateMaxBTR, String codeA_V, DestinataireExpediteur expediteur, ModeTraitement modeTraitement) {
        System.out.println("facaaade");
        String query = "Select ca FROM CourrierArrivee ca WHERE 1=1";
        if (dateMinC != null) {
            System.out.println("date test " + dateMinC);
            query += " AND ca.dateCreation >= " + dateMinC;
        }
        if (dateMaxC != null) {
            System.out.println("date test " + dateMaxC);
            query += " AND ca.dateCreation <= " + dateMaxC;
        }
        if (dateMinDRHMG != null) {
            query += " AND ca.dateEnregistrementDRHMG >= " + dateMinDRHMG;
        }
        if (dateMaxDRHMG != null) {
            query += " AND ca.dateEnregistrementDRHMG <= " + dateMaxDRHMG;
        }
        if (dateMinBTR != null) {
            query += " AND ca.dateEnregistrementBOW_TRANS_RLAN >= " + dateMinBTR;
        }
        if (dateMaxBTR != null) {
            query += " AND ca.dateEnregistrementBOW_TRANS_RLAN <= " + dateMaxBTR;
        }
        if (codeA_V != null) {
            query += SearchUtil.addConstraint("ca", "codeA_V", "=", codeA_V);
        }
        if (expediteur != null) {
            query += SearchUtil.addConstraint("ca", "destinataireExpediteur.id", "=", expediteur.getId());
        }

        if (modeTraitement != null) {
            query += SearchUtil.addConstraint("ca", "modeTraitement.id", "=", modeTraitement.getId());
        }
        return em.createQuery(query).getResultList();

    }

}

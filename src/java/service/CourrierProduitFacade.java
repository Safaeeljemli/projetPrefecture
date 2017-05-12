/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.Finalite;
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
public class CourrierProduitFacade extends AbstractFacade<CourrierProduit> {
    
    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
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
    
    public List<CourrierProduit> findCourrierProduit(Date dateMinC, Date dateMaxC, Date dateMinDRHMG, Date dateMaxDRHMG, Date dateMinBTR, Date dateMaxBTR, String codeP_V, Finalite finalite, DestinataireExpediteur destinataire) {
        String query = "Select cp FROM CourrierProduit cp WHERE 1=1";
        if (dateMinC != null) {
            query += " AND cp.dateCreation >=" + dateMinC;
        }
        if (dateMaxC != null) {
            query += " AND cp.dateCreation <=" + dateMaxC;
        }
        if (dateMinDRHMG != null) {
            query += " AND cp.dateEnregistrementDRHMG >= " + dateMinDRHMG;
        }
        if (dateMaxDRHMG != null) {
            query += " AND cp.dateEnregistrementDRHMG <= " + dateMaxDRHMG;
        }
        if (dateMinBTR != null) {
            query += " AND cp.dateEnregistrementBOW_TRANS_RLAN >= " + dateMinBTR;
        }
        if (dateMaxBTR != null) {
            query += " AND cp.dateEnregistrementBOW_TRANS_RLAN <= " + dateMaxBTR;
        }
        if (codeP_V != null) {
            query += SearchUtil.addConstraint("cp", "codeP_V", "=", codeP_V);
        }
        if (finalite != null) {
            query += SearchUtil.addConstraint("cp", "finalite.id", "=", finalite.getId());
        }
        
        if (destinataire != null) {
            query += SearchUtil.addConstraint("cp", "destinataire.id", "=", destinataire.getId());
        }
        return em.createQuery(query).getResultList();
        
    }
    
}

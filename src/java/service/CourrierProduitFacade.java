/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.Finalite;
import controller.util.DateUtil;
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

    public List<CourrierProduit> findCourrierProduit(Date dateEnValidation, Date dateRetourDoc, Date dateEnBOW_TRANS, Date dateReMinuteBOW_TRANS, Finalite finalite, DestinataireExpediteur destinataire) {
        String query = "Select cp FROM CourrierProduit cp WHERE 1=1";
        if (dateEnValidation != null) {
            query += " AND cp.dateEnvoiePourValidation = '" + dateEnValidation+ "'";
        }
        if (dateRetourDoc != null) {
            query += " AND cp.dateRetourDocument = '" + dateRetourDoc + "'";
        }
        if (dateEnBOW_TRANS != null) {
            query += " AND cp.dateEnvoiAuBOW_TRANS = '" + dateEnBOW_TRANS+ "'";
        }
        if (dateReMinuteBOW_TRANS != null) {
            query += " AND cp.dateRetourDeLaMinuteDuBOW_TRANS = '" + dateReMinuteBOW_TRANS+ "'";
        }
//        if (codeP_V != null) {
//            query += SearchUtil.addConstraint("cp", "codeP_V", "=", codeP_V);
//        }
        if (finalite != null) {
            query += SearchUtil.addConstraint("cp", "finalite.id", "=", finalite.getId());
        }

        if (destinataire != null) {
            query += SearchUtil.addConstraint("cp", "destinataireExpediteur.id", "=", destinataire.getId());
        }
        return em.createQuery(query).getResultList();

    }

    public String generateCodeP(String abrv, Date dateCreation, int sousClasse) {
        String code;
        code = sousClasse + abrv + DateUtil.convrtStringDate(dateCreation, "yy") + "P";
        return code;
    }

}

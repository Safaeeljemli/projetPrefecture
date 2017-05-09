/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierProduit;
import bean.Destinataire;
import bean.Finalite;
import controller.util.SearchUtil;
import java.sql.Date;
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

    public List<CourrierProduit> findCourrierProduit(Date dateMin, Date dateMax, String codeP_V, Finalite finalite, Destinataire destinataire) {
        String query = "Select cp FROM CourrierProduit cp WHERE 1=1";
        if (dateMin != null) {
            query += " AND cp.dateCreation >= :dateMin";
        }
        if (dateMax != null) {
            query += " AND cp.dateCreation <= :dateMax";
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

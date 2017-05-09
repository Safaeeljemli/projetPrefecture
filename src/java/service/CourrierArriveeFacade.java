/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CourrierArrivee;
import static bean.CourrierProduit_.finalite;
import bean.Expediteur;
import bean.ModeTraitement;
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

    public List<CourrierArrivee> findCourrierArrivee(Date dateMin, Date dateMax, String codeP_V, Expediteur expediteur, ModeTraitement modeTraitement) {
        String query = "Select ca FROM CourrierArrivee ca WHERE 1=1";
        if (dateMin != null) {
            query += " AND ca.dateCreation >= :dateMin";
        }
        if (dateMax != null) {
            query += " AND ca.dateCreation <= :dateMax";
        }
        if (codeP_V != null) {
            query += SearchUtil.addConstraint("cp", "codeP_V", "=", codeP_V);
        }
        if (expediteur != null) {
            query += SearchUtil.addConstraint("cp", "Expediteur.id", "=", expediteur.getId());
        }

        if (modeTraitement != null) {
            query += SearchUtil.addConstraint("cp", "modeTraitement.id", "=", modeTraitement.getId());
        }
        return em.createQuery(query).getResultList();

    }
}

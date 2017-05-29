/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Formateur;
import bean.Formation;
import bean.LieuFormation;
import bean.OrganismeFormation;
import controller.util.DateUtil;
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
public class FormationFacade extends AbstractFacade<Formation> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormationFacade() {
        super(Formation.class);
    }
    
    public List<Formation> findFormation(Date dateDebut, Date dateFin,LieuFormation lieuF ,OrganismeFormation organisme,Formateur formateur){
        String query="Select f FROM Formation f where 1=1 ";
         if (dateDebut != null) {
            query += " AND f.dateDebut >= '" + DateUtil.convertUtilToSql(dateDebut) + "'";
        }

        if (dateFin != null) {
            query += " AND f.dateFin <= '" + DateUtil.convertUtilToSql(dateFin) + "'";
        }
        if(lieuF != null){
            query += SearchUtil.addConstraint("f", "lieuFormation", "=", lieuF);
        }
        if(organisme != null){
            query += SearchUtil.addConstraint("f", "organismeFormation", "=", organisme);
        }
        if(formateur != null){
            query += SearchUtil.addConstraint("f", "formateur", "=", formateur);
        }
        return em.createQuery(query).getResultList();
    }
     public void clone(Formation formationSource, Formation formationDestination) {
         formationDestination.setDateDebut(formationSource.getDateDebut());
         formationDestination.setDateFin(formationSource.getDateFin());
         formationDestination.setId(formationSource.getId());
         formationDestination.setFormateur(formationSource.getFormateur());
         formationDestination.setLieuFormation(formationSource.getLieuFormation());
         formationDestination.setNom(formationSource.getNom());
         formationDestination.setOrganismeFormation(formationSource.getOrganismeFormation());
         formationDestination.setParticipants(formationSource.getParticipants());
                 
       }

    public Formation clone(Formation formation) {
        Formation cloned = new Formation();
        clone(formation, cloned);
        return cloned;
    }

}

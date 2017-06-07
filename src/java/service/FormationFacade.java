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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.DefaultScheduleEvent;

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
                 
       }

    public Formation clone(Formation formation) {
        Formation cloned = new Formation();
        clone(formation, cloned);
        return cloned;
    }
///
     public List<DefaultScheduleEvent> convertir() {
        List<Formation> formations = findAll();
        List<DefaultScheduleEvent> l1 = new ArrayList<>();
        for (Formation frt : formations) {
            /* date de fin du event = date de debut + pas */
            DefaultScheduleEvent event = new DefaultScheduleEvent(frt.getNom(), frt.getDateDebut(), frt.getDateFin());
            event.setId("frt" + frt.getId());
            event.setDescription("" + frt.getId());
//            event.setDescription("rddvvv");
            l1.add(event);
        }
       
        return l1;
    }
    
}

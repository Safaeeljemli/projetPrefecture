/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Classe;
import bean.SousClasse;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author safa
 */
@Stateless
public class SousClasseFacade extends AbstractFacade<SousClasse> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SousClasseFacade() {
        super(SousClasse.class);
    }

    public List<SousClasse> findSousClasseByClasse(Classe classe) {
        
        return em.createQuery("SELECT sc FROM SousClasse sc WHERE sc.classe.id='" + classe.getId() + "'").getResultList();
    }

     private void clone(SousClasse sousClasseSource, SousClasse sousClasseDestination) {
        sousClasseDestination.setId(generateId("Tache", "id"));
        sousClasseDestination.setNom(sousClasseSource.getNom());
        sousClasseDestination.setClasse(sousClasseSource.getClasse());
        sousClasseDestination.setId(sousClasseSource.getId());
    }

    public SousClasse clone(SousClasse sousClasse) {
        SousClasse cloned = new SousClasse();
        clone(sousClasse, cloned);
        System.out.println("sousClasse :: clone :: " + cloned);
        return cloned;
    }
    
}

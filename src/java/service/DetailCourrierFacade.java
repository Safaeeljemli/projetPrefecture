/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.DetailCourrier;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author safa
 */
@Stateless
public class DetailCourrierFacade extends AbstractFacade<DetailCourrier> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetailCourrierFacade() {
        super(DetailCourrier.class);
    }
    
}

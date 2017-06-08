/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Contact;
import bean.Formation;
import bean.FormationItem;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PC
 */
@Stateless
public class FormationItemFacade extends AbstractFacade<FormationItem> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;
   
    @EJB
    private service.FormationFacade formationFacade;
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormationItemFacade() {
        super(FormationItem.class);
    }
    public int saveFItem(List<Contact> contacts,Formation formation){
       if(formation== null )  return -1;
           if(contacts!=null) return -2;
           else{
             formationFacade.create(formation);
               for (Contact contact : contacts) {
             
               FormationItem formationItem = new FormationItem();
               formationItem.setContact(contact);
               formationItem.setFormation(formation);
             
           }
               return 1;
    }}
public  List<FormationItem> findFItems(Formation formation){
    return em.createQuery("SELECT f FROM FormationItem f WHERE f.formation.id='"+formation.getId()+"'").getResultList();
}
public  List<FormationItem> findFItemsContact(Contact contact ){
    return  em.createQuery("SELECT f FROM FormationItem f WHERE f.contact.id='"+contact.getId()+"'").getResultList();
}
    

}
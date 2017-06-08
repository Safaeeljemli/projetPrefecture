/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Contact;
import bean.Formation;
import bean.FormationItem;
import controller.util.SearchUtil;
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
public class ContactFacade extends AbstractFacade<Contact> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;
 @EJB
    private service.FormationItemFacade formationItemFacade;
 @EJB
    private service.ContactFacade contactFacade;
   
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContactFacade() {
        super(Contact.class);
    }
    public List<Contact> findParticipant(Formation formation){
        System.out.println("findParticipant--->facada");
        List<Contact> participant=null;
        List<Contact> contacts= contactFacade.findAll();
        List<FormationItem> fItems =formationItemFacade.findFItems(formation);
            for (FormationItem fItem : fItems) {
                for (Contact iContact : contacts) {
                    if(fItem.getContact().getId() == iContact.getId())
                        participant.add(iContact);
                    System.out.println("iC"+iContact.getId());
                }
        }
            return participant;
        }
    }
    


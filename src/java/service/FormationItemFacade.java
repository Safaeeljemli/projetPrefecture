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

    public int saveFItem(List<Contact> contacts, Formation formation) {
        if (formation == null) {
            return -1;
        }
        if (contacts == null) {
            return -2;
        } else {
            System.out.println("saveFItem1");
            formation.setId(generateId("Formation", "id"));
            formationFacade.create(formation);
            System.out.println("formation created"+formation.getId());
            for (Contact contact : contacts) {
               
                System.out.println("saveFItem contact id-->"+contact.getId());
                FormationItem formationItem = new FormationItem();
                formationItem.setId(generateId("FormationItem", "id"));
                formationItem.setContact(contact);
                formationItem.setFormation(formation);
                create(formationItem);
                System.out.println("hahowa"+formationItem.getContact().getId());
            }
            return 1;
        }
    }

    public List<FormationItem> findFItems(Formation formation) {
//        System.out.println("facade item formation id"+formation.getId());
//        System.out.println("findFItems facade");
//        String query="SELECT f FROM FormationItem f WHERE 1=1 ";
//        SearchUtil.addConstraint("f", "formation.id", "=", formation.getId());
    return em.createQuery("SELECT f FROM FormationItem f WHERE f.formation.id =" + formation.getId()).getResultList();
//return em.createQuery(query).getResultList();
    }

    public List<FormationItem> findFItemsContact(Contact contact) {
        return em.createQuery("SELECT f FROM FormationItem f WHERE f.contact.id=" + contact.getId()).getResultList();
    }

}

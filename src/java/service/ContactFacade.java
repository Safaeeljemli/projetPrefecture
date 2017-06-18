/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Contact;
import bean.Formation;
import bean.FormationItem;
import controller.util.DateUtil;
import controller.util.EmailUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
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
        List<Contact> participant= new ArrayList<>();
        List<Contact> contacts= contactFacade.findAll();
        if(contacts==null){ System.out.println("facadeCon null");}
        else{System.out.println("kayn les contacts");
        List<FormationItem> fItems = new ArrayList<>();
             fItems=formationItemFacade.findFItems(formation);
            System.out.println("size items facade"+fItems.size());
            for (FormationItem fItem : fItems) {
                System.out.println("item id " +fItem.getId());
                for (Contact iContact : contacts) {
                    if(fItem.getContact().getId() == iContact.getId())
                        participant.add(iContact);
                    System.out.println("iC"+iContact.getNom());
                }
        }
           
        } return participant;
    }
     //////send a mail
     public int sendMail(Contact contact,Formation formation) {
         
     String email=contact.getMail();
//     if(msg==null || msg==""){
//         msg="";
           String msg = "Bonjour " + contact.getNom() + ",<br/>"
                    +  "on vous invite a participer a une formation sous le theme de "+formation.getNom()
                  +"  du "+DateUtil.convrtStringDate(formation.getDateDebut(), "dd/MM/YY") +" au "+DateUtil.convrtStringDate(formation.getDateFin(), "dd/MM/YY")
                 +"pour plus d'information veulliez contacter le bureau ..."
                 +"en attente de votre reponse ";
     
//     if(emailWIlaya==null || emailWIlaya==""){
//         emailWIlaya="wilaya.marrakech.asfi@gmail.com";
//         mdp="wilayaAsfi";
//     }
//            
            try {
                EmailUtil.sendMail("wilaya.marrakech.asfi@gmail.com", "wilayaAsfi", msg, email, "invitation a une formation");
//                wilayamarrakechsafi
            } catch (MessagingException ex) {
                System.out.println("-2");
                //  Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
return 1;
        
     }
    }
    




















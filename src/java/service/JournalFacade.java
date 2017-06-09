/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Contact;
import bean.CourrierArrivee;
import bean.CourrierProduit;
import bean.Employee;
import bean.Formation;
import bean.Journal;
import bean.Stagiaire;
import bean.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.util.SessionUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

/**
 *
 * @author safa
 */
@Stateless
public class JournalFacade extends AbstractFacade<Journal> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @EJB
    private UserFacade userFacade;
    @EJB
    private CourrierArriveeFacade courrierArriveeFacade;
    @EJB
    private CourrierProduitFacade courrierProduitFacade;
    @EJB
    private StagiaireFacade stagiaireFacade;
    @EJB
    private FormationFacade formationFacade;
    @EJB
    private ContactFacade contactFacade;
    @EJB
    private EmployeeFacade employeeFacade;
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JournalFacade() {
        super(Journal.class);
    }

    public void createJournal(Object entity, int action) {
        Journal journal = initJournal(action, entity);
        if (action == 1) {
            journal.setOldeValue(entity.toString());
            journal.setMessage("Non");
        }
        if (action == 2) {
            Object oldEntity = findOldObject(entity);
            journal.setMessage(GenerateMessage(entity, oldEntity));
            journal.setNewValue(entity.toString());
            journal.setOldeValue(oldEntity.toString());
        }
        create(journal);
    }

    public boolean recreate(Journal item) {
        boolean res = recreatTheItem(item);
        if (res) {
            remove(item);
        }
        return res;
    }

    //doesn't work when the chaged attribute of a class is a bean
    //to make it work u can use :
    //String diffMessage=javers.compare(entity, oldEntity).prettyPrint();
    //instead of 
    //Diff d = javers.compare(entity, oldEntity);
    private String GenerateMessage(Object entity, Object oldEntity) {
        String chngesMessage = "";
        Javers javers = JaversBuilder.javers().build();
        Diff d = javers.compare(entity, oldEntity);
        List<ValueChange> change = d.getChangesByType(ValueChange.class);
        for (ValueChange valueChange : change) {
            chngesMessage += valueChange.getPropertyName() + " avant :" + valueChange.getRight() + " apres :" + valueChange.getLeft() + ", ";
        }
        return chngesMessage;
    }

    private Journal initJournal(int action, Object entity) {
        Journal journal = new Journal();
        journal.setTypeDaction(action);
        journal.setUserLogin(SessionUtil.getConnectedUser().getLogin());
        journal.setClassName(entity.getClass().getSimpleName());
        journal.setDateDeModification(LocalDateTime.now());
        return journal;
    }

    private boolean recreatTheItem(Journal item) {
        System.out.println(" recreatTheItem :: ");
        try {
            Class deletedObjectClass = Class.forName("bean." + item.getClassName());
            Object recreated = deletedObjectClass.newInstance();
            Gson gson = new Gson();
            if (recreated instanceof User) {
                return recreatUser(gson, item);
            } else if (recreated instanceof CourrierArrivee) {
                return recreateCourrierArrivee(gson, item);
            } else if (recreated instanceof CourrierProduit) {
                return recreateCourrierProduit(gson, item);
            } else if (recreated instanceof Stagiaire) {
                return recreateStagiaire(gson, item);
            } else if (recreated instanceof Contact) {
                return recreateContact(gson, item);
            } else if (recreated instanceof Employee) {
                return recreateEmployee(gson, item);
            } else if (recreated instanceof Formation) {
                return recreateFormation(gson, item);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
        return true;
    }

    public Object findOldObject(Object entity) {
        System.out.println("findId");
        if (entity instanceof User) {
            User user = (User) entity;
            return userFacade.find(user.getLogin());
        } else if (entity instanceof CourrierArrivee) {
            CourrierArrivee courrierArrivee = (CourrierArrivee) entity;
            return courrierArriveeFacade.find(courrierArrivee.getN_enregistrement());
        } else if (entity instanceof CourrierProduit) {
            CourrierProduit courrierProduit = (CourrierProduit) entity;
            return courrierArriveeFacade.find(courrierProduit.getN_dordre());
        } else if (entity instanceof Stagiaire) {
            Stagiaire stagiaire = (Stagiaire) entity;
            return stagiaireFacade.find(stagiaire.getId());
        } else if (entity instanceof Formation) {
            Formation formation = (Formation) entity;
            return formationFacade.find(formation.getId());
        } else if (entity instanceof Employee) {
            Employee employee = (Employee) entity;
            return employeeFacade.find(employee.getId());
        } else if (entity instanceof Contact) {
            Contact contact = (Contact) entity;
            return contactFacade.find(contact.getId());
        } else {
            return null;
        }
    }

    //Should use TypedQuery unstead of normal ones with localDate and localDateTime
    public List<Journal> findByConditions(String userName, LocalDateTime dateMin, LocalDateTime dateMax, int action) {
        String sqlQuery = "SELECT j FROM Journal j WHERE 1=1 ";
        if (userName != null && !userName.isEmpty()) {
            sqlQuery += " AND j.userLogin = :userName";
        }
        if (action != -1) {
            sqlQuery += " AND j.typeDaction = :actionType";
        }
        if (dateMin != null) {
            sqlQuery += " AND j.dateDeModification >= :dateMin";
        }
        if (dateMax != null) {
            sqlQuery += " AND j.dateDeModification <= :dateMax";
        }
        TypedQuery<Journal> query = getEntityManager().createQuery(sqlQuery, Journal.class);
        if (sqlQuery.contains("userName")) {
            query.setParameter("userName", userName);
        }

        if (sqlQuery.contains("actionType")) {
            query.setParameter("actionType", action);
        }

        if (sqlQuery.contains("dateMin")) {
            query.setParameter("dateMin", dateMin);
        }

        if (sqlQuery.contains("dateMax")) {
            query.setParameter("dateMax", dateMax);
        }
        return query.getResultList();
    }

    private boolean recreateCourrierArrivee(Gson gson, Journal item) throws JsonSyntaxException {
        CourrierArrivee courrierArrivee = gson.fromJson(item.getOldeValue(), CourrierArrivee.class);
        if (item.getTypeDaction() == 1) {
            courrierArriveeFacade.create(courrierArrivee);
        } else {
            courrierArriveeFacade.edit(courrierArrivee);
        }
        return true;
    }
    private boolean recreateCourrierProduit(Gson gson, Journal item) throws JsonSyntaxException {
        CourrierProduit courrierProduit = gson.fromJson(item.getOldeValue(), CourrierProduit.class);
        if (item.getTypeDaction() == 1) {
            courrierProduitFacade.create(courrierProduit);
        } else {
            courrierProduitFacade.edit(courrierProduit);
        }
        return true;
    }


   

    private boolean recreateStagiaire(Gson gson, Journal item) throws JsonSyntaxException {
        Stagiaire stagiaire = gson.fromJson(item.getOldeValue(), Stagiaire.class);
        if (item.getTypeDaction() == 1) {
            stagiaireFacade.create(stagiaire);
        } else {
            stagiaireFacade.edit(stagiaire);
        }
        return true;
    }

    private boolean recreateContact(Gson gson, Journal item) throws JsonSyntaxException {
        Contact contact = gson.fromJson(item.getOldeValue(), Contact.class);
        if (item.getTypeDaction() == 1) {
            contactFacade.create(contact);
        } else {
            contactFacade.edit(contact);
        }
        return true;
    }
    private boolean recreateEmployee(Gson gson, Journal item) throws JsonSyntaxException {
        Employee emlpoyee = gson.fromJson(item.getOldeValue(), Employee.class);
        if (item.getTypeDaction() == 1) {
            employeeFacade.create(emlpoyee);
        } else {
            employeeFacade.edit(emlpoyee);
        }
        return true;
    }

    private boolean recreateFormation(Gson gson, Journal item) throws JsonSyntaxException {
        Formation formation = gson.fromJson(item.getOldeValue(), Formation.class);
        if (item.getTypeDaction() == 1) {
            formationFacade.create(formation);
        } else {
            formationFacade.edit(formation);
        }
        return true;
    }

   
    private boolean recreatUser(Gson gson, Journal item) throws JsonSyntaxException {
        User user = gson.fromJson(item.getOldeValue(), User.class);
        if (item.getTypeDaction() == 1) {
            userFacade.create(user);
        } else {
            userFacade.edit(user);
        }
        return true;
    }
}

    


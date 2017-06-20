package controller;

import bean.Contact;
import bean.Departement;
import bean.Employee;
import bean.Tache;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.EmployeeFacade;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.ContactFacade;
import service.TacheFacade;

@Named("employeeController")
@SessionScoped
public class EmployeeController implements Serializable {

    private controller.TacheController tacheController;
    @EJB
    private service.EmployeeFacade ejbFacade;
    @EJB
    private service.DepartementFacade departementFacade;
    @EJB
    private TacheFacade tacheFacade;
    @EJB
    private ContactFacade contactFacade;
    private List<Employee> items = null;
    private Employee selected;
    private Departement departement;
    private Contact contact;
    private String cin;
    private Date dateRetard= new Date();
    private Date dateEnCour= new Date();

    private Employee thisEmlpoyee;
    private List<Tache> taches;
    private List<Tache> tachesFinis;
    private List<Tache> tachesRetards;
    private Tache tacheToCreate;

    public EmployeeController() {
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Date getDateRetard() {
        return dateRetard;
    }

    public void setDateRetard(Date dateRetard) {
        this.dateRetard = dateRetard;
    }

    public Date getDateEnCour() {
        return dateEnCour;
    }

    public void setDateEnCour(Date dateEnCour) {
        this.dateEnCour = dateEnCour;
    }

    public List<Tache> getTachesRetards() {
        return tachesRetards;
    }

    public void setTachesRetards(List<Tache> tachesRetards) {
        this.tachesRetards = tachesRetards;
    }

    
    public List<Tache> getTachesFinis() {
        
        return tachesFinis;
    }

    public void setTachesFinis(List<Tache> tachesFinis) {
        this.tachesFinis = tachesFinis;
    }

    
    public Tache getTacheToCreate() {
        if (tacheToCreate == null) {
            tacheToCreate = new Tache();
        }
        return tacheToCreate;
    }

    public void setTacheToCreate(Tache tacheToCreate) {
        this.tacheToCreate = tacheToCreate;
    }

    public Contact getContact() {
        if (contact == null) {
            contact = new Contact();
        }
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Departement getDepartement() {
        if (departement == null) {
            departement = new Departement();
        }
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Employee getSelected() {
        if (selected == null) {
            selected = new Employee();
        }
        return selected;
    }

    public void setSelected(Employee selected) {
        this.selected = selected;
    }

    public Employee getThisEmlpoyee() {
        if (thisEmlpoyee == null) {
            thisEmlpoyee = new Employee();
        }
        return thisEmlpoyee;
    }

    public void setThisEmlpoyee(Employee thisEmlpoyee) {
        this.thisEmlpoyee = thisEmlpoyee;
    }

    public List<Tache> getTaches() {
        if (taches == null) {
            taches = new ArrayList<>();
        }
        return taches;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EmployeeFacade getFacade() {
        return ejbFacade;
    }

//    public Employee prepareCreate() {
//        selected = new Employee();
//        initializeEmbeddableKey();
//        return selected;
//    }
    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
        setDepartement(null);
    }

    public List<Departement> getDepartementAvailableSelectOne() {
        return departementFacade.findAll();
    }

    public void findTacheByEmployee(Employee employee) {
        System.out.println("hanaaa");
        thisEmlpoyee = employee;
        try {
            System.out.println("ssclass");
            setTaches(tacheFacade.findTacheByEmployee(employee));
            employee.setTaches(tacheFacade.findTacheByEmployee(employee));
            JsfUtil.addSuccessMessage("trouvé hjyfgt");
            if(taches!=null){
                JsfUtil.addSuccessMessage("trouvé");
            }else
                JsfUtil.addErrorMessage("liste vide");

            System.out.println("jjjjjj");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire un employé");
        }
    }
    public void findTachEmp(Employee employee) {
        System.out.println("hanaaa");
        thisEmlpoyee = employee;
        setTaches(tacheFacade.findTacheByEmployee(employee));
        for (Tache tache : taches) {
            System.out.println(""+tache.getNom());
        }
    }

    public void findEmployee() {
        System.out.println("controller find");
        items = null;
        items = getFacade().findEncadrentByDepartement(departement);
        if (items == null) {
            System.out.println("no found");
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            System.out.println("succeee");
            JsfUtil.addSuccessMessage("successe");
        }
    }
    public void findEmployeeByCin() {
        System.out.println("controller find");
        selected = getFacade().findEmployeeByCin(cin);
        System.out.println(""+selected.getCin());
        setTaches(tacheFacade.findTacheByEmployee(selected));
        for(Tache tache: taches){
            System.out.println(""+tache.getEmployee().getNom());
        }
        
        if (selected == null) {
            System.out.println("no found");
            JsfUtil.addSuccessMessage("Employé non trouvé");
        } else {
            System.out.println("succeee");
            JsfUtil.addSuccessMessage("success");
        }
    }

    
    public void findTacheEnCour(){
        setTachesFinis(tacheFacade.findTacheRetard(dateEnCour));
         for(Tache tache: tachesFinis){
            System.out.println(""+tache.getNom());
        }
    }
    public void findTacheRetard(){
//        dateRetard=DateUtil.;
        setTachesFinis(tacheFacade.findTacheEnCour(dateRetard));
         for(Tache tache: tachesFinis){
            System.out.println(""+tache.getNom());
        }
    }
    public void prepareCreate() {
        selected = new Employee();
    }

    public void prepareCreateTache() {
        tacheToCreate = new Tache();
        tacheToCreate.setEmployee(selected);
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeCreated"));
        int res = contactFacade.employeeContact(selected);
        if (res > 1) {
            JsfUtil.addSuccessMessage("Nouveau contact creer");
        } else {
            JsfUtil.addErrorMessage("Information non acomplie pour creer contact");
        }
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void createTache() {
                System.out.println(""+tacheToCreate.getNom()+" "+tacheToCreate.getEmployee().getNom());
//        tacheController.createTache(tacheToCreate);
        tacheFacade.create(tacheToCreate);
        System.out.println("tache"+tacheToCreate.getNom());
        taches.add(tacheFacade.clone(tacheToCreate));
        JsfUtil.addSuccessMessage("Tache creé");
        tacheToCreate = new Tache();
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeUpdated"));
    }

    public void destroy(Employee employee) {
        System.out.println("User Controller");
        int res = ejbFacade.deleteEmloyeee(employee);
        if (res > 0) {
            JsfUtil.addSuccessMessage("User Deleted");
            items = null;
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Employee> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().savedEdite(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Employee getEmployee(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Employee> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Employee> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Employee.class)
    public static class EmployeeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmployeeController controller = (EmployeeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "employeeController");
            return controller.getEmployee(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Employee) {
                Employee o = (Employee) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Employee.class.getName()});
                return null;
            }
        }

    }

}

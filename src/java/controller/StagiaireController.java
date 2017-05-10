package controller;

import bean.Departement;
import bean.Ecole;
import bean.Employee;
import bean.Stagiaire;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import java.io.IOException;
import service.StagiaireFacade;

import java.io.Serializable;
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

@Named("stagiaireController")
@SessionScoped
public class StagiaireController implements Serializable {

   @EJB
    private service.StagiaireFacade ejbFacade;
    @EJB
    private service.EcoleFacade ecoleFacade;
    @EJB
    private service.DepartementFacade depatementFacade;
    @EJB
    private service.EmployeeFacade employeeFacade;

    private List<Stagiaire> items;
    private Stagiaire selected;
    //recherche Stagiaire
    private int typeStage;
    private Ecole ecole;
    private Date dateDebut;
    private Date dateFin;
    private Employee encadrant;
    private Departement thisDepartement;

    public StagiaireController() {
    }

    public Stagiaire getSelected() {
        return selected;
    }

    public void setSelected(Stagiaire selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StagiaireFacade getFacade() {
        return ejbFacade;
    }

    public Stagiaire prepareCreate() {
        selected = new Stagiaire();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("StagiaireCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("StagiaireUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("StagiaireDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Stagiaire> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

   
 public void refresh() {
        selected = null;
        items = null;
    }
    public int getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(int typeStage) {
        this.typeStage = typeStage;
    }

    public Ecole getEcole() {
        return ecole;
    }

    public void setEcole(Ecole ecole) {
        this.ecole = ecole;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

   
    public Employee getEncadrant() {
        return encadrant;
    }

    public void setEncadrant(Employee encadrant) {
        this.encadrant = encadrant;
    }

    public Departement getThisDepartement() {
        return thisDepartement;
    }

    public void setThisDepartement(Departement thisDepartement) {
        this.thisDepartement = thisDepartement;
    }
    

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
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

    public Stagiaire getStagiaire(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Stagiaire> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ecole> getEcolesAvailableSelectOne() {
        return ecoleFacade.findAll();
    }
    

    public void findEncadrentByDepartement() {
        try {
            getThisDepartement().setEmployees(employeeFacade.findEncadrentByDepartement(thisDepartement));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une departement");
        }
    }
    public List<Departement> findDepartement() {
        return depatementFacade.findAll();
    }


    //recherche dial Stagiaire

   public void findStagiaire() {
        System.out.println(":: search :: ");
items =getFacade().findStagiaire(typeStage, ecole, dateDebut, dateFin, thisDepartement, encadrant);
//items =ejbFacade.findStagiaire(typeStage, ecole, dateDebut, dateFin, thisDepartement, encadrant);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
            System.out.println("items null");
        } else {
            JsfUtil.addSuccessMessage("successe");
            System.out.println("success");
        }
    }
   ///pdf
//    public void generatPdf(Stagiaire stagiaire) throws JRException, IOException {
//        System.out.println("print pdf controller");
//        ejbFacade.printPdf(stagiaire,SessionUtil.getConnectedUser());
//        FacesContext.getCurrentInstance().responseComplete();
//    }


    @FacesConverter(forClass = Stagiaire.class)
    public static class StagiaireControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StagiaireController controller = (StagiaireController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "stagiaireController");
            return controller.getStagiaire(getKey(value));
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
            if (object instanceof Stagiaire) {
                Stagiaire o = (Stagiaire) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Stagiaire.class.getName()});
                return null;
            }
        }

    }

}

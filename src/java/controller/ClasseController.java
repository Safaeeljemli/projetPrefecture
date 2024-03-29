package controller;

import bean.Classe;
import bean.SousClasse;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.ClasseFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import service.SousClasseFacade;

@Named("classeController")
@SessionScoped
public class ClasseController implements Serializable {

    @EJB
    private service.ClasseFacade ejbFacade;
    private List<Classe> items = null;
    private Classe selected;
    

    private Classe thisClasse;
    private List<SousClasse> sousClasses;
    private SousClasse sousClasseToCreate;

    @EJB
    private SousClasseFacade sousClasseFacade;

    public ClasseController() {
    }

    public Classe getThisClasse() {
        if(thisClasse==null)
            thisClasse= new Classe();
        return thisClasse;
    }

    public void setThisClasse(Classe thisClasse) {
        this.thisClasse = thisClasse;
    }

    
    public List<SousClasse> getSousClasses() {
        if (sousClasses == null) {
            sousClasses = new ArrayList<>();
        }
        return sousClasses;
    }

    public SousClasse getSousClasseToCreate() {
        if(sousClasseToCreate==null)
            sousClasseToCreate = new SousClasse();
        return sousClasseToCreate;
    }

    public void setSousClasseToCreate(SousClasse sousClasseToCreate) {
        this.sousClasseToCreate = sousClasseToCreate;
    }

    public void setSousClasses(List<SousClasse> sousClasses) {
        this.sousClasses = sousClasses;
    }

    public Classe getSelected() {
        if (selected == null) {
            selected = new Classe();
        }
        return selected;
    }

    public void setSelected(Classe selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClasseFacade getFacade() {
        return ejbFacade;
    }

    public List<Classe> getClassesAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void findSousClasseByClasse(Classe classe) {
        System.out.println("hanaaa");
        thisClasse=classe;
        try {
            System.out.println("ssclass");
            setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
            if(getSousClasses()!=null){
                System.out.println(" sous class :!!!null");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une classe");
        }
    }

    public void prepareCreate() {
        selected = new Classe();
        initializeEmbeddableKey();
//        return selected;
    }
    public void prepareCreateSousClasse() {
       sousClasseToCreate = new SousClasse();
        sousClasseToCreate.setClasse(thisClasse);
    }
    

//    public Classe prepareCreate() {
//        selected = new Classe();
//        initializeEmbeddableKey();
//        return selected;
//    }
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClasseCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
     public void createSousClasse() {
                System.out.println(""+sousClasseToCreate.getNom());
        sousClasseFacade.create(sousClasseToCreate);
        System.out.println("tache"+sousClasseToCreate.getNom());
        sousClasses.add(sousClasseFacade.clone(sousClasseToCreate));
        JsfUtil.addSuccessMessage("Tache creé");
        sousClasseToCreate = new SousClasse();
    }
    

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClasseUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClasseDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Classe> getItems() {
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
                    getFacade().savedEdite(getSelected());
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

    public Classe getClasse(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Classe> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Classe> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Classe.class)
    public static class ClasseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClasseController controller = (ClasseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "classeController");
            return controller.getClasse(getKey(value));
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
            if (object instanceof Classe) {
                Classe o = (Classe) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Classe.class.getName()});
                return null;
            }
        }

    }

}

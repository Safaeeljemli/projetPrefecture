package controller;

import bean.DestinataireExpediteur;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.DestinataireExpediteurFacade;

import java.io.Serializable;
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

@Named("destinataireExpediteurController")
@SessionScoped
public class DestinataireExpediteurController implements Serializable {

    @EJB
    private service.DestinataireExpediteurFacade ejbFacade;
    private List<DestinataireExpediteur> items = null;
    private DestinataireExpediteur selected;

    public DestinataireExpediteurController() {
    }

    public DestinataireExpediteur getSelected() {
        return selected;
    }

    public void setSelected(DestinataireExpediteur selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DestinataireExpediteurFacade getFacade() {
        return ejbFacade;
    }

    public DestinataireExpediteur prepareCreate() {
        selected = new DestinataireExpediteur();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DestinataireExpediteurCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DestinataireExpediteurUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DestinataireExpediteurDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DestinataireExpediteur> getItems() {
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

    public DestinataireExpediteur getDestinataireExpediteur(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<DestinataireExpediteur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DestinataireExpediteur> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DestinataireExpediteur.class)
    public static class DestinataireExpediteurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DestinataireExpediteurController controller = (DestinataireExpediteurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "destinataireExpediteurController");
            return controller.getDestinataireExpediteur(getKey(value));
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
            if (object instanceof DestinataireExpediteur) {
                DestinataireExpediteur o = (DestinataireExpediteur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DestinataireExpediteur.class.getName()});
                return null;
            }
        }

    }

}

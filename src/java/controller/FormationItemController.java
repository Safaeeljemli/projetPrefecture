package controller;

import bean.Contact;
import bean.Formation;
import bean.FormationItem;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.FormationItemFacade;

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

@Named("formationItemController")
@SessionScoped
public class FormationItemController implements Serializable {

    @EJB
    private service.FormationItemFacade ejbFacade;
    private List<FormationItem> items = null;
    private FormationItem selected;
    private List<Contact> contacts;
    private Formation formation;
    

    public FormationItemController() {
    }
///creation Formation
    public void saveFItem(){
   int res=ejbFacade.saveFItem(contacts, formation);
   if(res!=1){
       JsfUtil.addErrorMessage("add formation");
   }else{
       
   }
}
    //getter setter
    public FormationItem getSelected() {
        return selected;
    }

    public void setSelected(FormationItem selected) {
        this.selected = selected;
    }
    

    protected void setEmbeddableKeys() {
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    protected void initializeEmbeddableKey() {
    }

    private FormationItemFacade getFacade() {
        return ejbFacade;
    }

    public FormationItem prepareCreate() {
        selected = new FormationItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FormationItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FormationItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<FormationItem> getItems() {
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

    public FormationItem getFormationItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<FormationItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<FormationItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = FormationItem.class)
    public static class FormationItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FormationItemController controller = (FormationItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "formationItemController");
            return controller.getFormationItem(getKey(value));
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
            if (object instanceof FormationItem) {
                FormationItem o = (FormationItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FormationItem.class.getName()});
                return null;
            }
        }

    }

}

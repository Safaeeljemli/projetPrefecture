package controller;

import bean.CourrierProduit;
import bean.Destinataire;
import bean.Finalite;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.CourrierProduitFacade;
import service.FinaliteFacade;
import service.DestinataireFacade;

import java.io.Serializable;
import java.sql.Date;
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

@Named("courrierProduitController")
@SessionScoped
public class CourrierProduitController implements Serializable {

    @EJB
    private service.CourrierProduitFacade ejbFacade;
    private List<CourrierProduit> items = null;
    private CourrierProduit selected;
    private Date dateMin;
    private Date dateMax;
    private String codeP_V;
    private Finalite finalite;
    private Destinataire destinataire;
    
    @EJB
    private FinaliteFacade finaliteFacade;
    @EJB
    private DestinataireFacade destinataireFacade;

    public CourrierProduitController() {
    }
      // getter & setter
    
    
    public Destinataire getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Destinataire destinataire) {
        this.destinataire = destinataire;
    }

    public Date getDateMin() {
        return dateMin;
    }

    public void setDateMin(Date dateMin) {
        this.dateMin = dateMin;
    }

    public Date getDateMax() {
        return dateMax;
    }

    public void setDateMax(Date dateMax) {
        this.dateMax = dateMax;
    }

    public String getCodeP_V() {
        return codeP_V;
    }

    public void setCodeP_V(String codeP_V) {
        this.codeP_V = codeP_V;
    }

    public Finalite getFinalite() {
        return finalite;
    }

    public void setFinalite(Finalite finalite) {
        this.finalite = finalite;
    }

    
    public CourrierProduit getSelected() {
        return selected;
    }

    public void setSelected(CourrierProduit selected) {
        this.selected = selected;
    }
//  end getter & setter
    
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }
    //methods 
    
    public List<Finalite> getFinalitesAvailableSelectOne() {
        return finaliteFacade.findAll();
    }
    public List<Destinataire> getDestinatairesAvailableSelectOne() {
        return destinataireFacade.findAll();

    }
    
    private void findCourrierProduit(){
        items = ejbFacade.findCourrierProduit(dateMin, dateMax, codeP_V,  finalite, destinataire);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            JsfUtil.addSuccessMessage("successe");
        }
    
    }
    
//     public void prepareView() {
//        selected = null;
//        dateCreation = null;
//        finalite = null;
//        codeP_V = false;
//        tauRetardFinished = false;
//        tauxTaxeFinished = false;
//        initializeEmbeddableKey();
//    }
//
//    
    private CourrierProduitFacade getFacade() {
        return ejbFacade;
    }

    public CourrierProduit prepareCreate() {
        selected = new CourrierProduit();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CourrierProduit> getItems() {
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

    public CourrierProduit getCourrierProduit(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CourrierProduit> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CourrierProduit> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CourrierProduit.class)
    public static class CourrierProduitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CourrierProduitController controller = (CourrierProduitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "courrierProduitController");
            return controller.getCourrierProduit(getKey(value));
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
            if (object instanceof CourrierProduit) {
                CourrierProduit o = (CourrierProduit) object;
                return getStringKey(o.getN_dordre());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CourrierProduit.class.getName()});
                return null;
            }
        }

    }

}

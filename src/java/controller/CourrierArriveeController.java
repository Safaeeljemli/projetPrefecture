package controller;

import bean.Classe;
import bean.CourrierArrivee;
import bean.CourrierProduit;
//import static bean.CourrierProduit_.courrierArrivee;
import bean.DestinataireExpediteur;
import bean.ModeTraitement;
import bean.SousClasse;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.CourrierArriveeFacade;

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
import service.ClasseFacade;
import service.DestinataireExpediteurFacade;
import service.ModeTraitementFacade;
import service.SousClasseFacade;

@Named("courrierArriveeController")
@SessionScoped
public class CourrierArriveeController implements Serializable {

    @EJB
    private service.CourrierArriveeFacade ejbFacade;
    private List<CourrierArrivee> items = null;
    private CourrierArrivee selected;

    private Date dateMinC;
    private Date dateMaxC;
    private Date dateMinDRHMG;
    private Date dateMaxDRHMG;
    private Date dateMinBTR;
    private Date dateMaxBTR;
    private String codeA_V;
    private DestinataireExpediteur expediteur;
    private ModeTraitement modeTraitement;
    private CourrierProduit courrierProduit;
    private Classe classe;
    private SousClasse sousClasse;

    @EJB
    private DestinataireExpediteurFacade destinataireExpediteurFacade;
    @EJB
    private ModeTraitementFacade modeTraitementFacade;
    @EJB
    private ClasseFacade classeFacade;
    @EJB
    private SousClasseFacade sousClasseFacade;

    public CourrierArriveeController() {
    }

    // getter & setter
    public SousClasse getSousClasse() {
        return sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public CourrierProduit getCourrierProduit() {
        return courrierProduit;
    }

    public void setCourrierProduit(CourrierProduit courrierProduit) {
        this.courrierProduit = courrierProduit;
    }

    public Date getDateMinC() {
        return dateMinC;
    }

    public void setDateMinC(Date dateMinC) {
        this.dateMinC = dateMinC;
    }

    public Date getDateMaxC() {
        return dateMaxC;
    }

    public void setDateMaxC(Date dateMaxC) {
        this.dateMaxC = dateMaxC;
    }

    public Date getDateMinDRHMG() {
        return dateMinDRHMG;
    }

    public void setDateMinDRHMG(Date dateMinDRHMG) {
        this.dateMinDRHMG = dateMinDRHMG;
    }

    public Date getDateMaxDRHMG() {
        return dateMaxDRHMG;
    }

    public void setDateMaxDRHMG(Date dateMaxDRHMG) {
        this.dateMaxDRHMG = dateMaxDRHMG;
    }

    public Date getDateMinBTR() {
        return dateMinBTR;
    }

    public void setDateMinBTR(Date dateMinBTR) {
        this.dateMinBTR = dateMinBTR;
    }

    public Date getDateMaxBTR() {
        return dateMaxBTR;
    }

    public void setDateMaxBTR(Date dateMaxBTR) {
        this.dateMaxBTR = dateMaxBTR;
    }

    public String getCodeA_V() {
        return codeA_V;
    }

    public void setCodeA_V(String codeA_V) {
        this.codeA_V = codeA_V;
    }

    public DestinataireExpediteur getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(DestinataireExpediteur expediteur) {
        this.expediteur = expediteur;
    }

    public ModeTraitement getModeTraitement() {
        return modeTraitement;
    }

    public void setModeTraitement(ModeTraitement modeTraitement) {
        this.modeTraitement = modeTraitement;
    }

    public CourrierArrivee getSelected() {
        return selected;
    }

    public void setSelected(CourrierArrivee selected) {
        this.selected = selected;
    }

    //methods 
    public void refresh() {
        selected = null;
        items = null;
    }

    public List<DestinataireExpediteur> getExpediteursAvailableSelectOne() {
        return destinataireExpediteurFacade.findAll();
    }

    public List<ModeTraitement> getModeTraitementsAvailableSelectOne() {
        return modeTraitementFacade.findAll();
    }

    public List<Classe> getClassesAvailableSelectOne() {
        return classeFacade.findAll();
    }

    public void findSousClasseByClasse() {
        try {
            getClasse().setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
        } catch (Exception sc) {
            JsfUtil.addErrorMessage("veiller choisire une departement");
        }
    }

    public void findCourrierArrivee() {
        System.out.println("haaaa");
        items = ejbFacade.findCourrierArrivee(dateMinC, dateMaxC, dateMinDRHMG, dateMinDRHMG, dateMaxBTR, dateMaxBTR, codeA_V, expediteur, modeTraitement);
        if (items == null) {
            System.out.println(" item ! null ");
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            JsfUtil.addSuccessMessage("successe");
        }
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CourrierArriveeFacade getFacade() {
        return ejbFacade;
    }

    public CourrierArrivee prepareCreate() {
        selected = new CourrierArrivee();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CourrierArrivee> getItems() {
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

    public CourrierArrivee getCourrierArrivee(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CourrierArrivee> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CourrierArrivee> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CourrierArrivee.class)
    public static class CourrierArriveeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CourrierArriveeController controller = (CourrierArriveeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "courrierArriveeController");
            return controller.getCourrierArrivee(getKey(value));
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
            if (object instanceof CourrierArrivee) {
                CourrierArrivee o = (CourrierArrivee) object;
                return getStringKey(o.getN_enregistrement());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CourrierArrivee.class.getName()});
                return null;
            }
        }

    }

}

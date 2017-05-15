package controller;

import bean.Classe;
import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.Finalite;
import bean.SousClasse;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.CourrierProduitFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
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
import service.FinaliteFacade;
import service.SousClasseFacade;

@Named("courrierProduitController")
@SessionScoped
public class CourrierProduitController implements Serializable {

    @EJB
    private service.CourrierProduitFacade ejbFacade;
    private List<CourrierProduit> items = null;
    private CourrierProduit selected;
    private Date dateMinC;
    private Date dateMaxC;
    private Date dateMinDRHMG;
    private Date dateMaxDRHMG;
    private Date dateMinBTR;
    private Date dateMaxBTR;
    private int etat;
    private Finalite finalite;
    private String codeP_V;
    private DestinataireExpediteur destinataire = null;
    private Classe classe = null;
    private SousClasse sousClasse = null;

    @EJB
    private FinaliteFacade finaliteFacade;
    @EJB
    private DestinataireExpediteurFacade destinataireFacade;
    @EJB
    private ClasseFacade classeFacade;
    @EJB
    private SousClasseFacade sousClasseFacade;

    public CourrierProduitController() {
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public SousClasse getSousClasse() {
        return sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
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

    public CourrierProduit getSelected() {
        return selected;
    }

    public void setSelected(CourrierProduit selected) {
        this.selected = selected;
    }

    public Finalite getFinalite() {
        return finalite;
    }

    public void setFinalite(Finalite finalite) {
        this.finalite = finalite;
    }

    public String getCodeP_V() {
        return codeP_V;
    }

    public void setCodeP_V(String codeP_V) {
        this.codeP_V = codeP_V;
    }

    public DestinataireExpediteur getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(DestinataireExpediteur destinataire) {
        this.destinataire = destinataire;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public List<DestinataireExpediteur> getDestinatairesAvailableSelectOne() {
        return destinataireFacade.findAll();
    }

    public List<Classe> getClassesAvailableSelectOne() {
        return classeFacade.findAll();
    }

    public List<Finalite> getFinalitesAvailableSelectOne() {
        return finaliteFacade.findAll();
    }

    public void findSousClasseByClasse() {
        try {
            getClasse().setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une classe");
        }
    }

    public void findCourrierProduit() {
        items = ejbFacade.findCourrierProduit(dateMinC, dateMaxC, dateMinDRHMG, dateMinDRHMG, dateMaxBTR, dateMaxBTR, codeP_V, finalite, destinataire);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            JsfUtil.addSuccessMessage("successe");
        }

    }

    public void refresh() {
        selected = null;
        items = null;
        setCodeP_V(null);
        setEtat(0);
        setFinalite(null);
        setDateMaxBTR(null);
        setDateMaxC(null);
        setDateMaxDRHMG(null);
        setDateMinBTR(null);
        setDateMinC(null);
        setDateMinDRHMG(null);
        setDestinataire(null);
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
            getItems().add(ejbFacade.clone(selected));
            //ystem.out.println("***" + selected.getModeTraitement().getNom());
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitUpdated"));
    }

//    public void destroy() {
//        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitDeleted"));
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
//    }
    public void destroy(CourrierProduit item) {
        getFacade().remove(item);
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CourrierProduitDeleted"));
        items = null;
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

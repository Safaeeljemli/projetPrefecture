package controller;

import bean.Classe;
import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.Finalite;
import bean.SousClasse;
import com.itextpdf.io.IOException;
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
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import controller.util.SessionUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
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
    private List<CourrierProduit> filteredCourrierP = null;
    private CourrierProduit selected;
    private Date dateEnValidation;
    private Date dateRetourDoc;
    private Date dateEnBOW_TRANS;
    private Date dateReMinuteBOW_TRANS;
    private int etat;
    private Finalite finalite;
    private String codeP_V;
    private String abrev;
    private DestinataireExpediteur destinataire = null;
    private Classe classe = null;
    private SousClasse sousClasse = null;

    private boolean n_ordreCheck;
    private boolean dateCrtCheck = true;
    private boolean objetCheck;
    private boolean dateEnvValiCheck;
    private boolean dateRetrCheck;
    private boolean etatCheck = true;
    private boolean codePCheck = true;
    private boolean raisonCheck;
    private boolean dateEnvoiAuBTCheck;
    private boolean dateEnvoiParBTCheck;
    private boolean dateRetMinutBTCheck;
    private boolean n_EnvoiParBTCheck;
    private boolean sousClasseCheck = true;
    private boolean finaliteCheck;
    private boolean destinataireCheck = true;
    private boolean courrierArriveeCodeCheck;
    private boolean optionCheck;

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

    public List<CourrierProduit> getFilteredCourrierP() {
        return filteredCourrierP;
    }

    public void setFilteredCourrierP(List<CourrierProduit> filteredCourrierP) {
        this.filteredCourrierP = filteredCourrierP;
    }

    public boolean isOptionCheck() {
        return optionCheck;
    }

    public boolean isDateEnvoiParBTCheck() {
        return dateEnvoiParBTCheck;
    }

    public void setDateEnvoiParBTCheck(boolean dateEnvoiParBTCheck) {
        this.dateEnvoiParBTCheck = dateEnvoiParBTCheck;
    }

    public void setOptionCheck(boolean optionCheck) {
        this.optionCheck = optionCheck;
    }

    public boolean isN_ordreCheck() {
        return n_ordreCheck;
    }

    public void setN_ordreCheck(boolean n_ordreCheck) {
        this.n_ordreCheck = n_ordreCheck;
    }

    public boolean isDateCrtCheck() {
        return dateCrtCheck;
    }

    public void setDateCrtCheck(boolean dateCrtCheck) {
        this.dateCrtCheck = dateCrtCheck;
    }

    public boolean isObjetCheck() {
        return objetCheck;
    }

    public void setObjetCheck(boolean objetCheck) {
        this.objetCheck = objetCheck;
    }

    public boolean isDateEnvValiCheck() {
        return dateEnvValiCheck;
    }

    public void setDateEnvValiCheck(boolean dateEnvValiCheck) {
        this.dateEnvValiCheck = dateEnvValiCheck;
    }

    public boolean isDateRetrCheck() {
        return dateRetrCheck;
    }

    public void setDateRetrCheck(boolean dateRetrCheck) {
        this.dateRetrCheck = dateRetrCheck;
    }

    public boolean isEtatCheck() {
        return etatCheck;
    }

    public void setEtatCheck(boolean etatCheck) {
        this.etatCheck = etatCheck;
    }

    public boolean isCodePCheck() {
        return codePCheck;
    }

    public void setCodePCheck(boolean codePCheck) {
        this.codePCheck = codePCheck;
    }

    public boolean isRaisonCheck() {
        return raisonCheck;
    }

    public void setRaisonCheck(boolean raisonCheck) {
        this.raisonCheck = raisonCheck;
    }

    public boolean isDateEnvoiAuBTCheck() {
        return dateEnvoiAuBTCheck;
    }

    public void setDateEnvoiAuBTCheck(boolean dateEnvoiAuBTCheck) {
        this.dateEnvoiAuBTCheck = dateEnvoiAuBTCheck;
    }

    public boolean isDateRetMinutBTCheck() {
        return dateRetMinutBTCheck;
    }

    public void setDateRetMinutBTCheck(boolean dateRetMinutBTCheck) {
        this.dateRetMinutBTCheck = dateRetMinutBTCheck;
    }

    public boolean isFinaliteCheck() {
        return finaliteCheck;
    }

    public void setFinaliteCheck(boolean finaliteCheck) {
        this.finaliteCheck = finaliteCheck;
    }

    public boolean isDestinataireCheck() {
        return destinataireCheck;
    }

    public void setDestinataireCheck(boolean destinataireCheck) {
        this.destinataireCheck = destinataireCheck;
    }

    public boolean isN_EnvoiParBTCheck() {
        return n_EnvoiParBTCheck;
    }

    public void setN_EnvoiParBTCheck(boolean n_EnvoiParBTCheck) {
        this.n_EnvoiParBTCheck = n_EnvoiParBTCheck;
    }

    public boolean isSousClasseCheck() {
        return sousClasseCheck;
    }

    public void setSousClasseCheck(boolean sousClasseCheck) {
        this.sousClasseCheck = sousClasseCheck;
    }

    public boolean isCourrierArriveeCodeCheck() {
        return courrierArriveeCodeCheck;
    }

    public void setCourrierArriveeCodeCheck(boolean courrierArriveeCodeCheck) {
        this.courrierArriveeCodeCheck = courrierArriveeCodeCheck;
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

    public Date getDateEnValidation() {
        return dateEnValidation;
    }

    public void setDateEnValidation(Date dateEnValidation) {
        this.dateEnValidation = dateEnValidation;
    }

    public Date getDateRetourDoc() {
        return dateRetourDoc;
    }

    public void setDateRetourDoc(Date dateRetourDoc) {
        this.dateRetourDoc = dateRetourDoc;
    }

    public Date getDateEnBOW_TRANS() {
        return dateEnBOW_TRANS;
    }

    public void setDateEnBOW_TRANS(Date dateEnBOW_TRANS) {
        this.dateEnBOW_TRANS = dateEnBOW_TRANS;
    }

    public Date getDateReMinuteBOW_TRANS() {
        return dateReMinuteBOW_TRANS;
    }

    public void setDateReMinuteBOW_TRANS(Date dateReMinuteBOW_TRANS) {
        this.dateReMinuteBOW_TRANS = dateReMinuteBOW_TRANS;
    }

    public CourrierProduit getSelected() {
        if(selected==null){
            selected=new CourrierProduit();
        }
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

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
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

     public void redirectToCreate() throws IOException, java.io.IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/courrierProduit/CreateCourrierProduit.xhtml");
    }
    
    public void findSousClasseByClasse() {
        try {
            getClasse().setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une classe");
        }
    }

    public void findCourrierProduit() {
        items = null;
        items = ejbFacade.findCourrierProduit(dateEnValidation, dateRetourDoc, dateEnBOW_TRANS, dateReMinuteBOW_TRANS, finalite, destinataire);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            JsfUtil.addSuccessMessage("successe");
        }

    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());

        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(cell.getStringCellValue().toUpperCase());
                cell.setCellStyle(style);
            }
        }
    }

    public void preProcessPDF(Object document) {

        Document doc = (Document) document;
        doc.setPageSize(PageSize.LEGAL.rotate());
        doc.addTitle("Informe"); // i tried to add a title with this , but it did not work.
        doc.addHeader("azerr", "aeaze");
        doc.addAuthor("autt");
        doc.addSubject("sujet");
        doc.addCreationDate(); // this did not work either.

    }

    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
//        setCodeP_V(null);
//        setEtat(0);
//        setFinalite(null);
//        setDateRetourDoc(null);
//        setDateEnBOW_TRANS(null);
//        setDateReMinuteBOW_TRANS(null);
//        setDateEnValidation(null);
//        setDestinataire(null);
    }
    
     public void refreshCreate(){
        setSelected(null);
        JsfUtil.addSuccessMessage("Enregisrement annulé");
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
        selected.setCodeP_V(ejbFacade.generateCodeP(abrev, selected.getDateCreation(), 12));
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CourrierProduitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));//ystem.out.println("***" + selected.getModeTraitement().getNom());
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

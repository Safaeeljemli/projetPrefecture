package controller;

import bean.Classe;
import bean.CourrierArrivee;
import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.ModeTraitement;
import bean.SousClasse;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
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
    private CourrierArrivee item;

    private String errorMessage = "";
    private Date dateMinC;
    private Date dateMaxC;
    private Date dateMinDRHMG;
    private Date dateMaxDRHMG;
    private Date dateMinBTR;
    private Date dateMaxBTR;
    private String codeA_V;
    private DestinataireExpediteur expediteur = null;
    private ModeTraitement modeTraitement = null;
    private CourrierProduit courrierProduit = null;
    private Classe classe = null;
    private SousClasse sousClasse = null;
    private String annee = "17";
    private String type;
    private String abrev;

    private boolean dateDRHMGcheck=false;
    private boolean expediteurCheck=true;
    private boolean motsCleCheck=true;
    private boolean objetCheck=true;
    private boolean modeTraitementCheck=true;
    private boolean n_DRHMGCheck=true;
    private boolean n_enCheck=true;
    private boolean n_BOW_TRANS_RLANcheck=true;
    private boolean codeA_Vcheck=true;
    private boolean dateEnregcheck=true;
    private boolean dateBOW_TRANS_RLANcheck=true;
    private boolean sousClasseCheck=true;
    private boolean optionCheck=true;

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
    public boolean isOptionCheck() {
        return optionCheck;
    }

    public void setOptionCheck(boolean optionCheck) {
        this.optionCheck = optionCheck;
    }

    public boolean isSousClasseCheck() {
        return sousClasseCheck;
    }

    public void setSousClasseCheck(boolean sousClasseCheck) {
        this.sousClasseCheck = sousClasseCheck;
    }

    public boolean isDateBOW_TRANS_RLANcheck() {
        return dateBOW_TRANS_RLANcheck;
    }

    public void setDateBOW_TRANS_RLANcheck(boolean dateBOW_TRANS_RLANcheck) {
        this.dateBOW_TRANS_RLANcheck = dateBOW_TRANS_RLANcheck;
    }

    public boolean isDateEnregcheck() {
        return dateEnregcheck;
    }

    public void setDateEnregcheck(boolean dateEnregcheck) {
        this.dateEnregcheck = dateEnregcheck;
    }

    public boolean isCodeA_Vcheck() {
        return codeA_Vcheck;
    }

    public void setCodeA_Vcheck(boolean codeA_Vcheck) {
        this.codeA_Vcheck = codeA_Vcheck;
    }

    public boolean isN_DRHMGCheck() {
        return n_DRHMGCheck;
    }

    public void setN_DRHMGCheck(boolean n_DRHMGCheck) {
        this.n_DRHMGCheck = n_DRHMGCheck;
    }

    public boolean isN_enCheck() {
        return n_enCheck;
    }

    public void setN_enCheck(boolean n_enCheck) {
        this.n_enCheck = n_enCheck;
    }

    public boolean isN_BOW_TRANS_RLANcheck() {
        return n_BOW_TRANS_RLANcheck;
    }

    public void setN_BOW_TRANS_RLANcheck(boolean n_BOW_TRANS_RLANcheck) {
        this.n_BOW_TRANS_RLANcheck = n_BOW_TRANS_RLANcheck;
    }

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public boolean isDateDRHMGcheck() {
        return dateDRHMGcheck;
    }

    public void setDateDRHMGcheck(boolean dateDRHMGcheck) {
        this.dateDRHMGcheck = dateDRHMGcheck;
    }

    public boolean isExpediteurCheck() {
        return expediteurCheck;
    }

    public void setExpediteurCheck(boolean expediteurCheck) {
        this.expediteurCheck = expediteurCheck;
    }

    public boolean isMotsCleCheck() {
        return motsCleCheck;
    }

    public void setMotsCleCheck(boolean motsCleCheck) {
        this.motsCleCheck = motsCleCheck;
    }

    public boolean isObjetCheck() {
        return objetCheck;
    }

    public void setObjetCheck(boolean objetCheck) {
        this.objetCheck = objetCheck;
    }

    public boolean isModeTraitementCheck() {
        return modeTraitementCheck;
    }

    public void setModeTraitementCheck(boolean modeTraitementCheck) {
        this.modeTraitementCheck = modeTraitementCheck;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public CourrierArrivee getItem() {
        return item;
    }

    public void setItem(CourrierArrivee item) {
        this.item = item;
    }

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
        this.codeA_V = "'" + sousClasse.getNom() + abrev + annee + "'";
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

    public List<DestinataireExpediteur> getExpediteursAvailableSelectOne() {
        return destinataireExpediteurFacade.findAll();
    }

    public List<ModeTraitement> getModeTraitementsAvailableSelectOne() {
        return modeTraitementFacade.findAll();
    }

    public List<Classe> getClassesAvailableSelectOne() {
        return classeFacade.findAll();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CourrierArriveeFacade getFacade() {
        return ejbFacade;
    }

    //methods 
    public void testError() {
        String s = "" + selected.getN_enregistrementDRHMG();

        System.out.println("s = " + s);
        System.out.println("selected = " + selected.getN_enregistrementDRHMG());
        if (!s.matches("\\d+")) {
            System.out.println("ssss test");
//            setErrorMessage("error");
            FacesContext.getCurrentInstance().addMessage("CourrierArriveeCreateForm", new FacesMessage("the input must be a Number"));

        }
        setErrorMessage("");
    }

    public void findSousClasseByClasse() {
        try {
            getClasse().setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une classe");
        }
    }

    public void findCourrierArrivee() {
        System.out.println("haaaa");
        refresh();
        items = ejbFacade.findCourrierArrivee(dateMinC, dateMaxC, dateMinDRHMG, dateMinDRHMG, dateMaxBTR, dateMaxBTR, codeA_V, sousClasse, expediteur, modeTraitement);
        if (items == null) {
            items = ejbFacade.findAll();

            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            System.out.println(" items ! null ");
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

    //    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
    //        Document pdf = (Document) document;
    //        pdf.open();
    //        pdf.setPageSize(PageSize.A4);
    //
    //        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    //        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
    //
    //        pdf.add(Image.getInstance(logo));
    //    }
    public CourrierArrivee prepareCreate() {
        selected = new CourrierArrivee();
        initializeEmbeddableKey();
        dateMinC = null;
        dateMaxC = null;
        dateMinDRHMG = null;
        dateMinDRHMG = null;
        dateMaxBTR = null;
        dateMaxBTR = null;
        codeA_V = null;
        expediteur = null;
        modeTraitement = null;
        return selected;
    }

//    public void prepareView() {
//        selected = null;
//        dateMinC = null;
//        dateMaxC = null;
//        dateMinDRHMG = null;
//        dateMinDRHMG = null;
//        dateMaxBTR = null;
//        dateMaxBTR = null;
//        codeA_V = null;
//        expediteur = null;
//        modeTraitement = null;
//        initializeEmbeddableKey();
//    }
    public void refresh() {
        selected = null;
        items = null;
        setClasse(null);
        setCodeA_V(null);
        setCourrierProduit(null);
        setDateMaxBTR(null);
        setDateMaxC(null);
        setDateMaxDRHMG(null);
        setDateMinBTR(null);
        setDateMinC(null);
        setDateMinDRHMG(null);
        setSousClasse(null);
    }

    public void initialiseCode() {
        selected.setCodeA_V("'" + sousClasse.getNom() + abrev + annee + "'");
    }

    public void create() {
        selected.setCodeA_V(ejbFacade.generateCodeA(abrev,selected.getDateEnregistrement(), 12));
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeUpdated"));
    }

    public void delete() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeDeleted"));
    }

//    public void destroy() {
//        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeDeleted"));
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
//    }
    public void destroy(CourrierArrivee item) {
        getFacade().remove(item);
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeDeleted"));
        items = null;
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

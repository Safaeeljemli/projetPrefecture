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

    private String errorMessage = "";
    private Date dateC;
    private Date dateDRHMG;
    private Date dateBTR;
    private String codeA=null;
    private DestinataireExpediteur thisExpediteur = null;
    private ModeTraitement modeTraitement = null;
    private CourrierProduit courrierProduit = null;
    private Classe classe = null;
    private SousClasse sousClasse = null;
    private String abrev;
    private Long n_DRHMG;

    private boolean dateDRHMGcheck = false;
    private boolean expediteurCheck = true;
    private boolean motsCleCheck = true;
    private boolean objetCheck = true;
    private boolean modeTraitementCheck = true;
    private boolean n_DRHMGCheck = true;
    private boolean n_enCheck = true;
    private boolean n_BOW_TRANS_RLANcheck = true;
    private boolean codeA_Vcheck = true;
    private boolean dateEnregcheck = true;
    private boolean dateBOW_TRANS_RLANcheck = true;
    private boolean sousClasseCheck = true;
    private boolean optionCheck = true;

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

    public Long getN_DRHMG() {
        return n_DRHMG;
    }

    // getter & setter
    public void setN_DRHMG(Long n_DRHMG) {
        this.n_DRHMG = n_DRHMG;
    }

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

    

    public SousClasse getSousClasse() {
        if(sousClasse==null){
            sousClasse= new SousClasse();
        }
        return sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public Classe getClasse() {
        if(classe== null){
            classe= new Classe();
        }
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

    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {
        this.dateC = dateC;
    }

    public Date getDateDRHMG() {
        return dateDRHMG;
    }

    public void setDateDRHMG(Date dateDRHMG) {
        this.dateDRHMG = dateDRHMG;
    }

    public Date getDateBTR() {
        return dateBTR;
    }

    public void setDateBTR(Date dateBTR) {
        this.dateBTR = dateBTR;
    }

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public DestinataireExpediteur getThisExpediteur() {
        if(thisExpediteur==null){
            thisExpediteur = new DestinataireExpediteur();
        }
        return thisExpediteur;
    }

    public void setThisExpediteur(DestinataireExpediteur thisExpediteur) {
        this.thisExpediteur = thisExpediteur;
    }

    public ModeTraitement getModeTraitement() {
        if (modeTraitement==null) {
            modeTraitement = new ModeTraitement();
        }
        return modeTraitement;
    }

    public void setModeTraitement(ModeTraitement modeTraitement) {
        this.modeTraitement = modeTraitement;
    }

    public CourrierArrivee getSelected() {
        if(selected==null){
            selected = new CourrierArrivee();
        }
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

    public void findSousClasseByClasse() {
        try {
            getClasse().setSousClasses(sousClasseFacade.findSousClasseByClasse(classe));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisire une classe");
        }
    }

    public void findCourrierArrivee() {
        System.out.println("controller find");
        items = null;
//        items = getFacade().findCourrier(modeTraitement);
        items = getFacade().findCourrierArrivee(dateC, dateDRHMG, dateBTR, sousClasse, thisExpediteur, modeTraitement,codeA);
        if (items == null) {
            System.out.println("no found");
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            System.out.println("succeee");
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

    public CourrierArrivee prepareCreate() {
        selected = new CourrierArrivee();
        initializeEmbeddableKey();
        dateC = null;
        dateDRHMG = null;
        dateBTR = null;
        codeA = null;
        thisExpediteur = null;
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
//        codeA = null;
//        thisExpediteur = null;
//        modeTraitement = null;
//        initializeEmbeddableKey();
//    }
    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
        setSousClasse(null);
        setClasse(null);
        setCodeA(null);
//        setCourrierProduit(null);
        setDateDRHMG(null);
        setDateBTR(null);
        setDateC(null);
        setModeTraitement(null);
        setThisExpediteur(null);
    }

//    public void initialiseCode() {
//        selected.setCodeA_V("'" + sousClasse.getNom() + abrev + annee + "'");
//    }

    public void create() {
        selected.setCodeA_V(ejbFacade.generateCodeA(abrev, selected.getDateEnregistrement(), 12));
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

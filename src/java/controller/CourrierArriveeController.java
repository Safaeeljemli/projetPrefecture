package controller;

import bean.Classe;
import bean.CourrierArrivee;
import bean.CourrierProduit;
import bean.DestinataireExpediteur;
import bean.ModeTraitement;
import bean.SousClasse;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import controller.util.ServerConfigUtil;
import java.io.FileOutputStream;
import java.io.IOException;
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

import org.primefaces.event.FileUploadEvent;
import service.ClasseFacade;
import service.DestinataireExpediteurFacade;
import service.ModeTraitementFacade;
import service.SousClasseFacade;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.primefaces.model.UploadedFile;

@Named("courrierArriveeController")
@SessionScoped
public class CourrierArriveeController implements Serializable {

    @EJB
    private service.CourrierArriveeFacade ejbFacade;
    private List<CourrierArrivee> items = null;
    private CourrierArrivee selected;

    private String errorMessage = "";
    private Date dateMin;
    private Date dateMax;
    private String codeA = null;
    private DestinataireExpediteur thisExpediteur = null;
    private ModeTraitement modeTraitement = null;
    private CourrierProduit courrierProduit = null;
    private Classe classe = null;
    private SousClasse sousClasse = null;
    private String abrev;
    private Long n_DRHMG;
    private UploadedFile uploadedFile ;
    
    private boolean dateDRHMGcheck = false;
    private boolean expediteurCheck = true;
    private boolean motsCleCheck = true;
    private boolean objetCheck = true;
    private boolean modeTraitementCheck = true;
    private boolean n_DRHMGCheck = false;
    private boolean n_enCheck = false;
    private boolean n_BOW_TRANS_RLANcheck = false;
    private boolean codeA_Vcheck = true;
    private boolean dateEnregcheck = true;
    private boolean dateBOW_TRANS_RLANcheck = false;
    private boolean sousClasseCheck = false;
    private boolean optionCheck = true;
//    private boolean testCheck = true;

    
    private int i = 0;
    private List myDataList = new ArrayList<>();

    @EJB
    private DestinataireExpediteurFacade destinataireExpediteurFacade;
    @EJB
    private ModeTraitementFacade modeTraitementFacade;
    @EJB
    private ClasseFacade classeFacade;
    @EJB
    private SousClasseFacade sousClasseFacade;

    private static String file;

    private List<FileUploadEvent> fileUploadEvents = new ArrayList<>();

    public CourrierArriveeController() {
    }

    public List<FileUploadEvent> getFileUploadEvents() {
        return fileUploadEvents;
    }

    public void setFileUploadEvents(List<FileUploadEvent> fileUploadEvents) {
        this.fileUploadEvents = fileUploadEvents;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    
    public List getMyDataList() {
        return myDataList = items;
    }

    public void setMyDataList(List myDataList) {
        this.myDataList = myDataList;
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
        if (sousClasse == null) {
            sousClasse = new SousClasse();
        }
        return sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }

    public Classe getClasse() {
        if (classe == null) {
            classe = new Classe();
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

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public DestinataireExpediteur getThisExpediteur() {
        if (thisExpediteur == null) {
            thisExpediteur = new DestinataireExpediteur();
        }
        return thisExpediteur;
    }

    public void setThisExpediteur(DestinataireExpediteur thisExpediteur) {
        this.thisExpediteur = thisExpediteur;
    }

    public ModeTraitement getModeTraitement() {
        if (modeTraitement == null) {
            modeTraitement = new ModeTraitement();
        }
        return modeTraitement;
    }

    public void setModeTraitement(ModeTraitement modeTraitement) {
        this.modeTraitement = modeTraitement;
    }

    public CourrierArrivee getSelected() {
        if (selected == null) {
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
    
    
    public boolean validate(CourrierArrivee selected){
        boolean res=false;
        System.out.println("befor false");
        
            if(selected.getSousClasse()!=null){
                System.out.println("sous Class ");
            }
            if(selected.getDateEnregistrement()!=null){
                res=true;
                System.out.println("trrue");
            }
        
        return res;
    }
            
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
        items = getFacade().findCourrierArrivee(dateMin, dateMax, sousClasse, thisExpediteur, modeTraitement, codeA);
        if (items == null) {
            System.out.println("no found");
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            System.out.println("succeee");
            JsfUtil.addSuccessMessage("successe");
        }
    }

    public void createPDF() {
        try {
            Document document = new Document();
            file = "C:\\Users\\PC\\Desktop\\Liste des Courriers Arrivées" + Integer.toString(i) + ".pdf";
            i++;
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            ejbFacade.addTitlePage(document, items, expediteurCheck, motsCleCheck, objetCheck, modeTraitementCheck, n_DRHMGCheck, n_enCheck, n_BOW_TRANS_RLANcheck, codeA_Vcheck, dateEnregcheck, dateBOW_TRANS_RLANcheck, sousClasseCheck);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(FileUploadEvent event) throws IOException {

        System.out.println("locale upload ::: " + event.getFile().getFileName());
        selected.setPhoto(event.getFile().getFileName());
        System.out.println("path--->" + selected.getPhoto());
        fileUploadEvents.add(event);

        System.out.println("list event :: " + fileUploadEvents);

    }

    public CourrierArrivee prepareCreate() {
        selected = new CourrierArrivee();
        initializeEmbeddableKey();
        abrev = null;
        dateMin = null;
        dateMax = null;
        codeA = null;
        thisExpediteur = null;
        modeTraitement = null;
        return selected;
    }

    public void redirectToCreate() throws IOException, java.io.IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/courrierArrivee/CreateCourrierArrivee.xhtml");
    }

    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
        setSousClasse(null);
        setClasse(null);
        setCodeA(null);
        setDateMax(null);
        setDateMin(null);
        setModeTraitement(null);
        setThisExpediteur(null);
    }

    public void refreshCreate() {
        setSelected(null);
        JsfUtil.addSuccessMessage("Enregisrement annulé");
    }
     public void upoadFiles() {
        System.out.println("IN");
        if (uploadedFile != null) {
            System.out.println("ulpoad null");

            String nameOfUp = uploadedFile.getFileName();
            System.out.println(nameOfUp);
            ServerConfigUtil.upload(uploadedFile,"C:\\Users\\PC\\Desktop", nameOfUp);
            System.out.println("controller");
            
        }
    }
    public void create() throws java.io.IOException {
        upoadFiles();
        selected.setCodeA_V(ejbFacade.generateCodeA(abrev, selected.getDateEnregistrement(), 12));
//        selected.setPhoto(fileUploadEvents.get(0).getFile().getFileName() + "");
//        selected.setPhoto(uploadedFile.getFileName() + "");        
//ServerConfigUtil.upload(fileUploadEvents.get(0).getFile(), ServerConfigUtil.getCheminePhoto(selected, true), selected.getPhoto());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CourrierArriveeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
        fileUploadEvents = null;
        fileUploadEvents = new ArrayList<>();
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

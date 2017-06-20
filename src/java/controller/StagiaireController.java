package controller;

import bean.Departement;
import bean.Domaine;
import bean.Ecole;
import bean.Employee;
import bean.Stagiaire;
import com.itextpdf.text.Document;
//import com.lowagie.text.Document;import java.io.FileOutputStream;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import service.StagiaireFacade;
import java.io.FileOutputStream;

import java.io.Serializable;
import java.util.ArrayList;
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
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.FileUploadEvent;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;


@Named("stagiaireController")
@SessionScoped
public class StagiaireController implements Serializable {

    @EJB
    private service.StagiaireFacade ejbFacade;
    @EJB
    private service.EcoleFacade ecoleFacade;
    @EJB
    private service.DepartementFacade depatementFacade;
    @EJB
    private service.EmployeeFacade employeeFacade;
    private Ecole selectedE;
    private Domaine selectedD;
    private List<Stagiaire> items;
    private List<Stagiaire> filteredStagiaire;
    private Stagiaire selected;
    //recherche Stagiaire
    private int typeStage;
    private Ecole ecole;
    private Date dateDebut;
    private Date dateFin;
    private Employee encadrant;
    private Departement thisDepartement;
    private int genre;
    //boolean
    private boolean nomB = true;
    private boolean prenomB = true;
    private boolean cinB = true;
    private boolean mailB = true;
    private boolean numTelB = true;
    private boolean genreB = true;
    private boolean typeB = true;
    private boolean dateDebutB = true;
    private boolean dateFinB = true;
    private boolean encadrantB = true;
    private boolean depB = true;
    private boolean ecoleB = true;
    private boolean filiereB;

    private int i = 0;

    private DomaineController controllerD;
    private EcoleController controllerE;

    public StagiaireController() {
    }

    private static String file;

    private List<FileUploadEvent> fileUploadEvents = new ArrayList<>();

    public Stagiaire getSelected() {
        if (selected == null) {
            selected = new Stagiaire();
        }
        return selected;
    }

    public Domaine getSelectedD() {
        if (selectedD == null) {
            selectedD = new Domaine();
        }
        return selectedD;
    }

    public void setSelectedD(Domaine selectedD) {
        this.selectedD = selectedD;
    }

    public Ecole getSelectedE() {
        return selectedE;
    }

    public void setSelectedE(Ecole selectedE) {
        this.selectedE = selectedE;
    }

    public List<Stagiaire> getFilteredStagiaire() {
        return filteredStagiaire;
    }

    public void setFilteredStagiaire(List<Stagiaire> filteredStagiaire) {
        this.filteredStagiaire = filteredStagiaire;
    }

    public boolean isFiliereB() {
        return filiereB;
    }

    public DomaineController getControllerD() {
        return controllerD;
    }

    public void setControllerD(DomaineController controllerD) {
        this.controllerD = controllerD;
    }

    public void setFiliereB(boolean filiereB) {
        this.filiereB = filiereB;
    }

    public void setSelected(Stagiaire selected) {
        this.selected = selected;
    }

    public EcoleController getControllerE() {
        return controllerE;
    }

    public void setControllerE(EcoleController controllerE) {
        this.controllerE = controllerE;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StagiaireFacade getFacade() {
        return ejbFacade;
    }

    public Stagiaire prepareCreate() {
        selected = new Stagiaire();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("StagiaireCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));
            getFacade().create(selected);
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("StagiaireUpdated"));
    }

//    public void destroy() {
//        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("StagiaireDeleted"));
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
//    }
    public void destroy(Stagiaire item) {
        getFacade().remove(item);
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StagiaireDeleted"));
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Stagiaire> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
        setTypeStage(0);
        setEcole(null);
        setDateDebut(null);
        setDateFin(null);
        setEncadrant(null);
        setThisDepartement(null);
    }

    public int getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(int typeStage) {
        this.typeStage = typeStage;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public boolean isEcoleB() {
        return ecoleB;
    }

    public void setEcoleB(boolean ecoleB) {
        this.ecoleB = ecoleB;
    }

    public Ecole getEcole() {
        if (ecole == null) {
            ecole = new Ecole();
        }
        return ecole;
    }

    public void setEcole(Ecole ecole) {
        this.ecole = ecole;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Employee getEncadrant() {
        return encadrant;
    }

    public void setEncadrant(Employee encadrant) {
        this.encadrant = encadrant;
    }

    public Departement getThisDepartement() {
        return thisDepartement;
    }

    public void setThisDepartement(Departement thisDepartement) {
        this.thisDepartement = thisDepartement;
    }

    public boolean isNomB() {
        return nomB;
    }

    public void setNomB(boolean nomB) {
        this.nomB = nomB;
    }

    public boolean isPrenomB() {
        return prenomB;
    }

    public void setPrenomB(boolean prenomB) {
        this.prenomB = prenomB;
    }

    public boolean isCinB() {
        return cinB;
    }

    public void setCinB(boolean cinB) {
        this.cinB = cinB;
    }

    public boolean isMailB() {
        return mailB;
    }

    public void setMailB(boolean mailB) {
        this.mailB = mailB;
    }

    public boolean isNumTelB() {
        return numTelB;
    }

    public void setNumTelB(boolean numTelB) {
        this.numTelB = numTelB;
    }

    public boolean isTypeB() {
        return typeB;
    }

    public void setTypeB(boolean typeB) {
        this.typeB = typeB;
    }

    public boolean isDateDebutB() {
        return dateDebutB;
    }

    public void setDateDebutB(boolean dateDebutB) {
        this.dateDebutB = dateDebutB;
    }

    public boolean isDateFinB() {
        return dateFinB;
    }

    public void setDateFinB(boolean dateFinB) {
        this.dateFinB = dateFinB;
    }

    public boolean isEncadrantB() {
        return encadrantB;
    }

    public void setEncadrantB(boolean encadrantB) {
        this.encadrantB = encadrantB;
    }

    public boolean isDepB() {
        return depB;
    }

    public void setDepB(boolean depB) {
        this.depB = depB;
    }

    public boolean isGenreB() {
        return genreB;
    }

    public void setGenreB(boolean genreB) {
        this.genreB = genreB;
    }

    ////redirect
    public void redirectToCreate() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/stagiaire/Create.xhtml");
    }

    ////pdf
    public void createPDF() {
        try {
            Document document = new Document();
            file = "C:\\Users\\PC\\Desktop\\Liste des Stagiaires" + Integer.toString(i) + ".pdf";
            i++;
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            ejbFacade.addTitlePage(document, items,nomB , prenomB , cinB , mailB , numTelB , genreB , typeB , dateDebutB , dateFinB , encadrantB , depB , ecoleB ,filiereB);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().savedEdite(selected);
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

    public Stagiaire getStagiaire(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Stagiaire> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ecole> getEcolesAvailableSelectOne() {
        return ecoleFacade.findAll();
    }

    public void findEncadrentByDepartement() {
        try {
            getThisDepartement().setEmployees(employeeFacade.findEncadrentByDepartement(thisDepartement));
        } catch (Exception e) {
            JsfUtil.addErrorMessage("veiller choisir un departement");
        }
    }

    public List<Departement> findDepartement() {
        return depatementFacade.findAll();
    }

    //recherche dial Stagiaire
    public void findStagiaire() {
        System.out.println(":: search :: ");
        items = getFacade().findStagiaire(typeStage, ecole, dateDebut, dateFin, thisDepartement, encadrant, genre);
//items =ejbFacade.findStagiaire(typeStage, ecole, dateDebut, dateFin, thisDepartement, encadrant);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
            System.out.println("items null");
        } else {
            JsfUtil.addSuccessMessage("successe");
            System.out.println("success");
        }
    }

    public void generatPdf(Stagiaire stagiaire) throws JRException, IOException {
        System.out.println("print pdf controller");
        ejbFacade.printPdf(stagiaire);
        FacesContext.getCurrentInstance().responseComplete();
    }

    @FacesConverter(forClass = Stagiaire.class)
    public static class StagiaireControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StagiaireController controller = (StagiaireController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "stagiaireController");
            return controller.getStagiaire(getKey(value));
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
            if (object instanceof Stagiaire) {
                Stagiaire o = (Stagiaire) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Stagiaire.class.getName()});
                return null;
            }
        }

    }

}

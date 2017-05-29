package controller;

import bean.Contact;
import bean.Formateur;
import bean.Formation;
import bean.LieuFormation;
import bean.OrganismeFormation;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import java.io.IOException;
import service.FormationFacade;

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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

@Named("formationController")
@SessionScoped
public class FormationController implements Serializable {

    @EJB
    private service.FormationFacade ejbFacade;
    @EJB
    private service.FormateurFacade formateurFacade;
    @EJB
    private service.LieuFormationFacade lff;
    @EJB
    private service.OrganismeFormationFacade orgFacade;
    @EJB
    private service.ContactFacade contactFacade;
    private List<Contact> contacts;
    private List<Formation> items = null;
    private Formation selected;
    private Date dateDebut;
    private Date dateFin;
    private LieuFormation lieuF;
    private OrganismeFormation organisme;
    private Formateur formateur;
    private boolean nomB;
    private boolean dateB;
    private boolean lieuFB;
    private boolean organismeB;
    private boolean formateurB;

    public FormationController() {
    }

    //recherche dial Formation
    public void findFormation() {
        System.out.println(":: search :: ");
        items = getFacade().findFormation(dateDebut, dateFin, lieuF, organisme, formateur);
        if (items == null) {
            JsfUtil.addSuccessMessage("No Data Found");
            System.out.println("items null");
        } else {
            JsfUtil.addSuccessMessage("successe");
            System.out.println("success");
        }
    }
    //////contact f une formation
    public void findParticipant(Formation formation){
        contacts=contactFacade.findParticipant(formation);
         JsfUtil.addSuccessMessage("Success");
    }
     public void redirectToContact() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/contact/List.xhtml");
    }
      public void redirectToCreate() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/formation/CreateFormation.xhtml");
    }
/////imprimer pdf
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
        doc.setPageSize(PageSize.A4.rotate());
        doc.addCreationDate();
        doc.addSubject("Liste Des Stagiaires");
        doc.addTitle("Informe"); // i tried to add a title with this , but it did not work.
        doc.addHeader("azerr", "aeaze");
        doc.addAuthor("autt");
        doc.addCreationDate(); // this did not work either.

    }

    ///avaibale ones
    public List<Formateur> getFormateursAvailableSelectMany() {
        return formateurFacade.findAll();
    }

    public List<LieuFormation> getLieusAvailableSelectMany() {
        return lff.findAll();
    }

    public List<OrganismeFormation> getOrganismesAvailableSelectMany() {
        return orgFacade.findAll();
    }
    ////getter and setter

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public boolean isNomB() {
        return nomB;
    }

    public void setNomB(boolean nomB) {
        this.nomB = nomB;
    }

    public boolean isDateB() {
        return dateB;
    }

    public void setDateB(boolean dateB) {
        this.dateB = dateB;
    }

    public boolean isLieuFB() {
        return lieuFB;
    }

    public void setLieuFB(boolean lieuFB) {
        this.lieuFB = lieuFB;
    }

    public boolean isOrganismeB() {
        return organismeB;
    }

    public void setOrganismeB(boolean organismeB) {
        this.organismeB = organismeB;
    }

    public boolean isFormateurB() {
        return formateurB;
    }

    public void setFormateurB(boolean formateurB) {
        this.formateurB = formateurB;
    }

    public FormationFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FormationFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
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

    public LieuFormation getLieuF() {
        return lieuF;
    }

    public void setLieuF(LieuFormation lieuF) {
        this.lieuF = lieuF;
    }

    public OrganismeFormation getOrganisme() {
        return organisme;
    }

    public void setOrganisme(OrganismeFormation organisme) {
        this.organisme = organisme;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public Formation getSelected() {
        if(selected==null){
            selected= new Formation();
        }
        return selected;
    }

    public void setSelected(Formation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FormationFacade getFacade() {
        return ejbFacade;
    }

    public Formation prepareCreate() {
        selected = new Formation();
        initializeEmbeddableKey();
        return selected;
    }

//    public void create() {
//        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));
//        if (!JsfUtil.isValidationFailed()) {
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
//    }
     public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            getItems().add(ejbFacade.clone(selected));
            getFacade().create(selected);
            prepareCreate();    // Invalidate list of items to trigger re-query.
        }
     }
   

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FormationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FormationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Formation> getItems() {
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

    public Formation getFormation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Formation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Formation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Formation.class)
    public static class FormationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FormationController controller = (FormationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "formationController");
            return controller.getFormation(getKey(value));
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
            if (object instanceof Formation) {
                Formation o = (Formation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Formation.class.getName()});
                return null;
            }
        }

    }

}

package controller;

import bean.Contact;
import bean.Formateur;
import bean.Formation;
import bean.LieuFormation;
import bean.OrganismeFormation;
import bean.Position;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import java.io.IOException;
import service.FormationFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import org.primefaces.model.map.MapModel;

@Named("formationController")
@SessionScoped
public class FormationController implements Serializable {

    @EJB
    private service.FormationFacade ejbFacade;
    @EJB
    private service.FormateurFacade formateurFacade;
    @EJB
    private service.FormationItemFacade formationItemFacade;
    @EJB
    private service.LieuFormationFacade lff;
    @EJB
    private service.OrganismeFormationFacade orgFacade;
    @EJB
    private service.ContactFacade contactFacade;
    @EJB
    private service.PositionFacade positionFacade;
    private List<Contact> contacts;
    private List<Contact> selectedContacts;
    private List<Formation> items = null;
    private Formation selected;
    private Formation selectedF;
    private Date dateDebut;
    private Date dateFin;
    private LieuFormation lieuF;
    private OrganismeFormation organisme;
    private Formateur formateur;
    private Position position;
    private boolean nomB;
    private boolean dateB;
    private boolean lieuFB;
    private boolean organismeB;
    private boolean formateurB;
    private ScheduleModel eventModel;
    private String text = "";
    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String desabeledPosition = "false";
    private boolean activateMarkeMethode = true;
    private boolean isModification = false;
    private MapModel emptyModel;
    private Double lat = 0D;
    private Double lng = 0D;
    private String  mailWilaya;
    private String mdp;
    private String msg;

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
        contacts = null;
    }

    //////contact f une formation
    public void findParticipant(Formation formation) {
        setSelectedF(formation);
        contacts = new ArrayList<>();
        System.out.println("findParticipant" + formation.getId());
        setContacts(contactFacade.findParticipant(formation));
//        contacts = contactFacade.findParticipant(formation);
        System.out.println("taille contacts" + contacts.size());
        JsfUtil.addSuccessMessage("Success");
        if (contacts.isEmpty() || contacts == null) {
            JsfUtil.addErrorMessage("failed findParticipant");
            System.out.println("fail");

        } else {
            JsfUtil.addSuccessMessage("Success");
            System.out.println("reussi");
        }
    }

    public void redirectToContact() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/contact/List.xhtml");
    }

    public void redirectToCreate() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/formation/CreateFormation.xhtml");
    }

    public void redirectToCalendrier() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/formation/Schedule.xhtml");
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
//envoyer un email
    public void sendMail(Contact contact){
        
            int res = contactFacade.sendMail(contact, selectedF);
            if (res < 0) {
                System.out.println("ee error");
                JsfUtil.addErrorMessage("there is a problem");
            } else {

                JsfUtil.addSuccessMessage("Mail succefuly sended");
            }
        }

    //dstroy
    public void destroy(Formation item) {
        getFacade().remove(item);
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FormationDeleted"));
        items = null;    // Invalidate list of items to trigger re-query.
    }
////Schedule

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        items = ejbFacade.findAll();
        List<DefaultScheduleEvent> events = ejbFacade.convertir();
        for (DefaultScheduleEvent event1 : events) {
            eventModel.addEvent(event1);
        }
    }
     public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
      public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
       private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null)
            eventModel.addEvent(event);
        else
            eventModel.updateEvent(event);
         
        event = new DefaultScheduleEvent();
    }
//Map

    public void toAddPosition() throws IOException {
        setDesabeledPosition("false");
        setIsModification(false);
        SessionUtil.redirectNoXhtml("Map.xhtml");
    }

    public void cancelPositionAddition(boolean modification) throws IOException {
        cancelCreation();

        if (modification) {
            setActivateMarkeMethode(false);
            isModification = false;
            SessionUtil.redirectNoXhtml("/Project/faces/secured/formation/CreateFormation.xhtml");
        } else {
            setActivateMarkeMethode(false);
            redirect();
        }
    }

    public void marke(boolean modification) throws IOException {
        System.out.println("markk test");
        if (activateMarkeMethode) {
            System.out.println("activateMarkeMethode" + activateMarkeMethode);
            setActivateMarkeMethode(false);
            getPosition().setLat(getLat());
            getPosition().setLng(getLng());

            if (modification) {
                System.out.println("modification " + modification);
                positionFacade.remove(getSelected().getPosition());
                getSelected().setPosition(getPosition());
                positionFacade.create(getPosition());
                getFacade().edit(getSelected());
                cancelCreation();
                isModification = false;
                System.out.println("map test");
                SessionUtil.redirectNoXhtml("/Project/faces/secured/formation/CreateFormation.xhtml");
            } else {
                getSelected().setPosition(getPosition());
                positionFacade.create(getPosition());
                getFacade().edit(getSelected());
                cancelCreation();
                redirect();
            }
        }
    }

    public void redirect() throws IOException {
        SessionUtil.redirectNoXhtml("/Project/faces/secured/formation/CreateFormation.xhtml");
    }

    public void cancelCreation() {
        setSelected(null);
        emptyModel = null;
    }

//    public void saveLocal() throws IOException {
//        create();
//        JsfUtil.addSuccessMessage("add location by Clicking on the local position in the Map");
//        setDesabeledPosition("false");
//    }
    ///avaibale ones
    public List<Formateur> getFormateursAvailableSelectMany() {
        return formateurFacade.findAll();
    }

    public List<LieuFormation> getLieusAvailableSelectMany() {
        System.out.println("lieufor");
        return lff.findAll();
    }

    public List<OrganismeFormation> getOrganismesAvailableSelectMany() {
        return orgFacade.findAll();
    }
    ////getter and setter

    public String getDesabeledPosition() {
        return desabeledPosition;
    }

    public List<Contact> getSelectedContacts() {
        if (selectedContacts == null) {
            System.out.println("contacts null");

            selectedContacts = new ArrayList();
        }
        System.out.println("contacts");
        return selectedContacts;
    }

    public void setSelectedContacts(List<Contact> selectedContacts) {
        this.selectedContacts = selectedContacts;
    }

    public void setDesabeledPosition(String desabeledPosition) {
        this.desabeledPosition = desabeledPosition;
    }

    public Position getPosition() {
        if (position == null) {
            position = new Position();
        }
        position.setId(positionFacade.generateId("Position", "id"));
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Contact> getContacts() {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public ScheduleModel getEventModel() {
        if(eventModel==null){
            eventModel=new ScheduleModel() {
                @Override
                public void addEvent(ScheduleEvent se) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean deleteEvent(ScheduleEvent se) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<ScheduleEvent> getEvents() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public ScheduleEvent getEvent(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void updateEvent(ScheduleEvent se) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public int getEventCount() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void clear() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean isEventLimit() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
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

    public String getMailWilaya() {
        return mailWilaya;
    }

    public void setMailWilaya(String mailWilaya) {
        this.mailWilaya = mailWilaya;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public Formation getSelectedF() {
        return selectedF;
    }

    public void setSelectedF(Formation selectedF) {
        this.selectedF = selectedF;
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
        if (selected == null) {
            selected = new Formation();
        }
        return selected;
    }

    public void setSelected(Formation selected) {
        this.selected = selected;
    }

    public boolean isActivateMarkeMethode() {
        return activateMarkeMethode;
    }

    public void setActivateMarkeMethode(boolean activateMarkeMethode) {
        this.activateMarkeMethode = activateMarkeMethode;
    }

    public boolean isIsModification() {
        return isModification;
    }

    public void setIsModification(boolean isModification) {
        this.isModification = isModification;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FormationFacade getFacade() {
        return ejbFacade;
    }

    public void prepareCreate() {
        selected = new Formation();
        selectedContacts = new ArrayList<>();
        initializeEmbeddableKey();
        contacts = null;
        items = ejbFacade.findAll();
        dateDebut = null;
        dateFin = null;
        lieuF = null;
        organisme = null;
        formateur = null;
        position = null;

    }

    public void create() {
//        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));

//       if (!JsfUtil.isValidationFailed()) {
//              // Invalidate list of items to trigger re-query.
        int res = formationItemFacade.saveFItem(selectedContacts, selected);
        if (res != 1) {
            System.out.println("res " + res);
        } else {
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));
            getItems().add(ejbFacade.clone(selected));
            // formationItemFacade.saveFItem(selectedContacts, selected);
            prepareCreate();

            // }
        }
    }
    //     public void create() {
    //        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));
    //        if (!JsfUtil.isValidationFailed()) {
    //            getItems().add(ejbFacade.clone(selected));
    //            getFacade().create(selected);
    //            prepareCreate();    // Invalidate list of items to trigger re-query.
    //        }
    //    }
    //    public void create() {
    //        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FormationCreated"));
    //        if (!JsfUtil.isValidationFailed()) {
    //            getItems().add(ejbFacade.clone(selected));
    //            formationItemFacade.saveFItem(selectedContacts, selected);
    //            prepareCreate();    // Invalidate list of items to trigger re-query.
    //        }
    //    }

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
            items = new ArrayList<>();
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

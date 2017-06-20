package controller;

import bean.Stage;
import bean.Tache;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.TacheFacade;

import java.io.Serializable;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartModel;

@Named("tacheController")
@SessionScoped
public class TacheController implements Serializable {

    @EJB
    private service.TacheFacade ejbFacade;
    private List<Tache> items = null;
    private Tache selected;
     private int typeGraphe = 1;
    private BarChartModel barModel;
    private LineChartModel lineModel;
    private DonutChartModel donutModel;
    private Stage stage;

    public TacheController() {
    }
//chart
       public void createModel() {
        // items = ejbFacade.findStat(anneeDeb, anneeFin, rue, thisQuartier, thisCommune, thisSecteur);
//        switch (typeGraphe) {
//
//            case 1: {
        items = ejbFacade.findAll();
        barModel = ejbFacade.initBarModel(items, stage);
        barModel.setTitle("Statistique");
        barModel.setLegendPosition("ne");
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Taches");
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Avancement");
        yAxis.setMin(0);
        yAxis.setMax(ejbFacade.maxChart(items, stage) * 1.1);
//        break;
    }
//            case 2: {
//                lineModel = ejbFacade.initLineModel(items, anneeDeb, anneeFin);
//                lineModel.setTitle("Statistique");
//                lineModel.setLegendPosition("ne");
//                lineModel.setAnimate(true);
//                Axis xAxis = lineModel.getAxis(AxisType.X);
//                lineModel.getAxes().put(AxisType.X, new CategoryAxis(""));
//                xAxis.setLabel("Les trimestres");
//                Axis yAxis = lineModel.getAxis(AxisType.Y);
//                yAxis.setLabel("Montant");
//                yAxis.setMin(0);
//                yAxis.setMax(ejbFacade.maxChart(items, anneeDeb, anneeFin) * 1.1);
//                break;
//            }
//            case 3:
//                donutModel = ejbFacade.initDonuModel(items, anneeDeb, anneeFin);
//                donutModel.setTitle("Statistique");
//                donutModel.setLegendPosition("ne");
//                donutModel.setSliceMargin(5);
//                donutModel.setShowDataLabels(true);
//                donutModel.setDataFormat("value");
//                donutModel.setShadow(true);
//                break;
//            default:
//                break;
//        }

    public int getTypeGraphe() {
        return typeGraphe;
    }

    public void setTypeGraphe(int typeGraphe) {
        this.typeGraphe = typeGraphe;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Tache getSelected() {
        return selected;
    }

    public void setSelected(Tache selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TacheFacade getFacade() {
        return ejbFacade;
    }

    public Tache prepareCreate() {
        selected = new Tache();
        initializeEmbeddableKey();
        return selected;
    }

    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TacheCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void createTache(Tache tache) {
        selected=tache;
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TacheCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TacheUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TacheDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Tache> getItems() {
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

    public Tache getTache(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Tache> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Tache> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Tache.class)
    public static class TacheControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TacheController controller = (TacheController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tacheController");
            return controller.getTache(getKey(value));
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
            if (object instanceof Tache) {
                Tache o = (Tache) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Tache.class.getName()});
                return null;
            }
        }

    }

}

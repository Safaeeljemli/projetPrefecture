package controller;

import bean.Departement;
import bean.Employee;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.EmployeeFacade;

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

@Named("employeeController")
@SessionScoped
public class EmployeeController implements Serializable {

    @EJB
    private service.EmployeeFacade ejbFacade;
    private List<Employee> items = null;
    private Employee selected;
    private Departement departement;
    @EJB
    private service.DepartementFacade departementFacade;

    public EmployeeController() {
    }

    public Departement getDepartement() {
        if (departement == null) {
            departement = new Departement();
        }
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Employee getSelected() {
        if (selected == null) {
            selected = new Employee();
        }
        return selected;
    }

    public void setSelected(Employee selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EmployeeFacade getFacade() {
        return ejbFacade;
    }

//    public Employee prepareCreate() {
//        selected = new Employee();
//        initializeEmbeddableKey();
//        return selected;
//    }
    public void refresh() {
        selected = null;
        items = ejbFacade.findAll();
        setDepartement(null);
    }

    public List<Departement> getDepartementAvailableSelectOne() {
        return departementFacade.findAll();
    }

    public void findEmployee() {
        System.out.println("controller find");
        items = null;
        items = getFacade().findEncadrentByDepartement(departement);
        if (items == null) {
            System.out.println("no found");
            JsfUtil.addSuccessMessage("No Data Found");
        } else {
            System.out.println("succeee");
            JsfUtil.addSuccessMessage("successe");
        }
    }

    public void prepareCreate() {
        selected = new Employee();
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeUpdated"));
    }
    
    public void destroy(Employee employee) {
        System.out.println("User Controller");
        int res = ejbFacade.deleteEmloyeee(employee);
        if (res > 0) {
            JsfUtil.addSuccessMessage("User Deleted");
            items = null;
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Employee> getItems() {
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

    public Employee getEmployee(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Employee> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Employee> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Employee.class)
    public static class EmployeeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmployeeController controller = (EmployeeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "employeeController");
            return controller.getEmployee(getKey(value));
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
            if (object instanceof Employee) {
                Employee o = (Employee) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Employee.class.getName()});
                return null;
            }
        }

    }

}

package controller;

import bean.User;
import controller.util.DeviceUtil;
import controller.util.HashageUtil;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import java.io.IOException;
import service.UserFacade;

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

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private service.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;
    private User connectedUser;
    private String objet;
    private String msgEmail;

    private String oldPassword;
    private String changePassword;
    private String changeRepetePassword;
    private boolean afficheProfile = true;
    private boolean changerPasswrd = false;
    private boolean changerAutreInfos = false;

    public UserController() {
    }

    public String genaratePasswrd() {
        if (!selected.getEmail().equals("")) {
            System.out.println("email" + selected.getEmail());
            int res = ejbFacade.sendPW(selected.getEmail());
            if (res < 0) {
                System.out.println("ee error");
                JsfUtil.addErrorMessage("there is a problem");
            } else {

                JsfUtil.addSuccessMessage("loook your email");
                return "/login.xhtml";
            }
        }
        return null;
    }

    //CONNEXION
    public void connecte() throws IOException {
        int res = ejbFacade.seConnnecter(getSelected());
        switch (res) {
            case (-5):
                JsfUtil.addErrorMessage("Veuilliez saisir votre login");
                break;
            case (-4):
                JsfUtil.addErrorMessage("Login n'existe pas");
                break;
            case (-1):
                SessionUtil.redirectNoXhtml("/Project/faces/secured/home/accueil.xhtml");
                JsfUtil.addErrorMessage("User deja connecter veuiller vous deconnecter des autre device ou notifier votre admin ");
                break;
            case (-2):
                JsfUtil.addErrorMessage("Utilisateur est bloqué");
                break;
            case (-3):
                JsfUtil.addErrorMessage("Mot de passe incorrect");
                break;
            default:
                try {
                    SessionUtil.redirectNoXhtml("/Project/faces/secured/home/accueil.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        setSelected(null);

    }

    public void contactUs() {
        System.out.println("test user");
        int res = ejbFacade.contactUs(msgEmail, objet, getConnectedUser());
        if (res > 0) {
            JsfUtil.addSuccessMessage("Votre message a été envoyeravec succes ");
        } else {
            JsfUtil.addErrorMessage("Erreur de manipulation");
        }
    }

    //Profil
    public void aficherProfil(boolean profile, boolean password, boolean autreInfos) {
        afficheProfile = profile;
        changerPasswrd = password;
        changerAutreInfos = autreInfos;
        oldPassword = "";
        changePassword = "";
        changeRepetePassword = "";
    }

    public void changePass() {
        int res = ejbFacade.changePassword(getConnectedUser().getLogin(), oldPassword, changePassword, changeRepetePassword);
        showMessage(res);
    }

    public void changeInformation() {
        ejbFacade.changeData(connectedUser);
        JsfUtil.addSuccessMessage("Modification avec succes");
    }
    private void showMessage(int res) {
        if (res == -1) {
            JsfUtil.addErrorMessage("la confirmation de votre mot de passe n'est pas correct");
        } else if (res == -2) {
            JsfUtil.addErrorMessage("l'ancient mot de passe ne correspond pas au mot de passe de la base de données");
        } else if (res == -3) {
            JsfUtil.addErrorMessage("le nouveau mot de passe ne doit pas etre l'ancient");
        } else {
            JsfUtil.addSuccessMessage("Modification avec succes ");
        }
    }
    
    // se deconnecter
    public void seDeConnnecter() throws IOException {
        ejbFacade.seDeConnnecter();
        SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
    }

    public String canAccesseAdmin() throws IOException {
        try {
            if (SessionUtil.getConnectedUser().isAdminn()) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
            return null;
        }
    }

    public String canAccesseCourrier() throws IOException {
        try {
            if (SessionUtil.getConnectedUser().isCourrier()) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
            return null;
        }
    }

    public String canAccesseStagiaire() throws IOException {
        try {
            if (SessionUtil.getConnectedUser().isStagiaire()) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
            return null;
        }
    }

    public String canAccesseEmployee() throws IOException {
        try {
            if (SessionUtil.getConnectedUser().isEmployee()) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
            return null;
        }
    }

    public String canAccesseFormation() throws IOException {
        try {
            if (SessionUtil.getConnectedUser().isFormation()) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            SessionUtil.redirectNoXhtml("/Project/faces/login.xhtml");
            return null;
        }
    }

    public User getSelected() {
        if (selected == null) {
            selected = new User();
        }
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

//    public User prepareCreate() {
//        selected = new User();
//        initializeEmbeddableKey();
//        return selected;
//    }
    public void prepareCreate() {
        selected = null;
    }

    public void prepareEmail() {
        objet = null;
        msgEmail = null;
    }

    public void create() {
        if (selected != null) {
            getSelected().setPasswrd(HashageUtil.sha256(getSelected().getPasswrd()));
            getFacade().create(getSelected());
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
            items.add(getFacade().clone(selected));
            selected = null;
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy(User user) {
        System.out.println("User Controller");
        int res = ejbFacade.deleteUser(user);
        if (res > 0) {
            JsfUtil.addSuccessMessage("User Deleted");
            items = null;
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        items.remove(SessionUtil.getConnectedUser());
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public String getChangeRepetePassword() {
        return changeRepetePassword;
    }

    public void setChangeRepetePassword(String changeRepetePassword) {
        this.changeRepetePassword = changeRepetePassword;
    }

    public boolean isAfficheProfile() {
        return afficheProfile;
    }

    public void setAfficheProfile(boolean afficheProfile) {
        this.afficheProfile = afficheProfile;
    }

    public boolean isChangerPasswrd() {
        return changerPasswrd;
    }

    public void setChangerPasswrd(boolean changerPasswrd) {
        this.changerPasswrd = changerPasswrd;
    }

    public boolean isChangerAutreInfos() {
        return changerAutreInfos;
    }

    public void setChangerAutreInfos(boolean changerAutreInfos) {
        this.changerAutreInfos = changerAutreInfos;
    }

    
    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }

    public User getConnectedUser() {
        if (connectedUser == null) {
            connectedUser = ejbFacade.find(SessionUtil.getConnectedUser().getLogin());
        }
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public User getUser(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getLogin());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}

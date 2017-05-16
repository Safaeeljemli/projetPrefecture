/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Device;
import bean.User;
import controller.util.HashageUtil;
import controller.util.JsfUtil;
import controller.util.SessionUtil;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author safa
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @EJB
    private HistoryFacade historyFacade;
    
    
    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
     public void cloneData(User userSource, User userDestination) {
        userDestination.setNom(userSource.getNom());
        userDestination.setPrenom(userSource.getPrenom());
        userDestination.setTel(userSource.getTel());
        userDestination.setEmail(userSource.getEmail());
    }
     public User clone(User user) {
        User clone = new User();
        clone.setLogin(user.getLogin());
        clone.setBlocked(user.getBlocked());
        clone.setNbrCnx(user.getNbrCnx());
        clone.setNom(user.getNom());
        clone.setPasswrd(user.getPasswrd());
        clone.setPrenom(user.getPrenom());
        clone.setTel(user.getTel());
        clone.setAdminn(user.isAdminn());
        return clone;
     }
    public Object[] seConnecter(User user, Device device) {
        if (user == null || user.getLogin() == null) {
            JsfUtil.addErrorMessage("Veuilliez saisir votre login");
            return new Object[]{-5, null};
        } else {
            User loadedUser = find(user.getLogin());
            if (loadedUser == null) {
                return new Object[]{-4, null};
            } else if (!loadedUser.getPasswrd().equals(HashageUtil.sha256(user.getPasswrd()))) {
                if (loadedUser.getNbrCnx() < 3) {
                    System.out.println("hana loadedUser.getNbrCnx() < 3 ::: " + loadedUser.getNbrCnx());
                    loadedUser.setNbrCnx(loadedUser.getNbrCnx() + 1);
                    edit(loadedUser);
                    return new Object[]{-7, null};
                } else {//(loadedUser.getNbrCnx() >= 3)
                    System.out.println("hana loadedUser.getNbrCnx() >= 3::: " + loadedUser.getNbrCnx());
                    loadedUser.setBlocked(1);
                    edit(loadedUser);
                    return new Object[]{-3, null};
                }
            } else if (loadedUser.getBlocked() == 1) {
                JsfUtil.addErrorMessage("Cet utilisateur est bloqué");
                return new Object[]{-2, null};
            } 
//            else {
//                loadedUser.setNbrCnx(0);
//                edit(loadedUser);
//                user = clone(loadedUser);
//                user.setPasswrd(null);
//                int resDevice = deviceFacade.checkDevice(loadedUser, device);
//                device.setDateCreation(new Date());
//                switch (resDevice) {
//                    case 3:
//                        deviceFacade.save(device, loadedUser);
//                        return new Object[]{3, loadedUser};
//                    case 1:
//                        deviceFacade.save(device, loadedUser);
//                        return new Object[]{1, loadedUser};
//                    default:
//                        return new Object[]{2, loadedUser};
//                }
          return new Object[]{2, loadedUser};
        } }
    
// se deconnecter
     public void seDeConnnecter() {
        User connectedUser = SessionUtil.getConnectedUser();
        historyFacade.createHistoryElement(connectedUser, 2);
        SessionUtil.unRegisterUser(connectedUser);

    }
    
    
}

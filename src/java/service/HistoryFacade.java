/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.History;
import bean.User;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author safa
 */
@Stateless
public class HistoryFacade extends AbstractFacade<History> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoryFacade() {
        super(History.class);
    }
    
    public void createHistoryElement(User loadedUser, int type) {
        History connexionHistory = new History();
        connexionHistory.setUserLogin(loadedUser.getLogin());
        if (type == 1) {
            connexionHistory.setType(1);
            connexionHistory.setInOutTimeStamp(new Date());
        }
        if (type == 2) {
            connexionHistory.setType(2);
            connexionHistory.setInOutTimeStamp(new Date());
        }
        System.out.println("createHistoryElement :: "+connexionHistory);
        create(connexionHistory);
    }

    
    
}

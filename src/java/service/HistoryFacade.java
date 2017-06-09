/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.History;
import bean.User;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

    public void deleteHistoryForUser(User user) {
        System.out.println("HistoryFacede :: ");
        List<History> historysToDelete = findByConditions(user.getLogin(), null, null, 0);
        System.out.println("1");
        if (historysToDelete != null && !historysToDelete.isEmpty()) {
            historysToDelete.stream().forEach((history) -> {
                System.out.println("2");
                remove(history);
            });
        }
    }

    public List<History> findByConditions(String userName, LocalDateTime dateMin, LocalDateTime dateMax, int action) {
        String sqlQuery = "SELECT h FROM History h WHERE 1=1 ";
        if (userName != null && !userName.isEmpty()) {
            sqlQuery += " AND h.userLogin = :userName";
        }
        if (action == 1 || action == 2) {
            sqlQuery += " AND h.type = :actionType";
        }
        if (dateMin != null) {
            sqlQuery += " AND h.inOutTimeStamp >= :dateMin";
        }
        if (dateMax != null) {
            sqlQuery += " AND h.inOutTimeStamp <= :dateMax";
        }
        TypedQuery<History> query = getEntityManager().createQuery(sqlQuery, History.class);
        if (sqlQuery.contains("userName")) {
            query.setParameter("userName", userName);
        }

        if (sqlQuery.contains("actionType")) {
            query.setParameter("actionType", action);
        }

        if (sqlQuery.contains("dateMin")) {
            query.setParameter("dateMin", dateMin);
        }

        if (sqlQuery.contains("dateMax")) {
            query.setParameter("dateMax", dateMax);
        }
        System.out.println("find History Query :: " + query.toString());
        return query.getResultList();
    }

    public HistoryFacade() {
        super(History.class);
    }

   public void createHistoryElement(User loadedUser, int type) {
        History connexionHistory = new History();
        connexionHistory.setUserLogin(loadedUser.getLogin());
        if (type == 1) {
            connexionHistory.setType(1);
            connexionHistory.setInOutTimeStamp(LocalDateTime.now());
        }
        if (type == 2) {
            connexionHistory.setType(2);
            connexionHistory.setInOutTimeStamp(LocalDateTime.now());
        }
        System.out.println("createHistoryElement :: "+connexionHistory);
        create(connexionHistory);
    }

}

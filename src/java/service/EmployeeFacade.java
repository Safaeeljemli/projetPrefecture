/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Departement;
import bean.Employee;
import controller.util.SearchUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PC
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }

    public List<Employee> findEncadrentByDepartement(Departement departement) {
        return em.createQuery("SELECT e FROM Employee e WHERE e.departement.id='" + departement.getId() + "'").getResultList();
    }

    public int deleteEmloyeee(Employee employee) {
        System.out.println("User facade ");
        remove(employee);
        return 1;

    }
    public Employee findEmployeeByCin(String cin) {
       String query="SELECT e FROM Employee e WHERE 1=1 ";
       if(cin!=null){
           query+=SearchUtil.addConstraint("e", "cin", "=", cin);
       }
       
        return (Employee) em.createQuery(query).getSingleResult();
        }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SousTache;
import bean.Stage;
import bean.Tache;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author PC
 */
@Stateless
public class TacheFacade extends AbstractFacade<Tache> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TacheFacade() {
        super(Tache.class);
    }
     //chart
     //init barChart //pour construire la sereis des coordonnees pour les bar
    public BarChartModel initBarModel(List<Tache> taches,Stage stage) {
        ChartSeries sousTcheSerie = new ChartSeries();
        BarChartModel model1 = new BarChartModel();
        sousTcheSerie.setLabel("" + stage);
        int x;
        for (x = 1; x < 5; x++) {
            Double a = 0.0;
            for (Tache tache : taches) {
                    a += tache.getAvancement();
                }
               
            
            sousTcheSerie.set("Tache " + x, a);
        }
        model1.addSeries(sousTcheSerie);
        return model1;
    }

       public Double maxChart(List<Tache> taches,Stage stage){
            Double a = 0.0;
            for (Tache tache : taches) {
                
            
                    a += tache.getAvancement();
                }
               
        
        return a;
                }
    

}

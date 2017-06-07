/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Stage;
import bean.SousTache;
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
public class SousTacheFacade extends AbstractFacade<SousTache> {

    @PersistenceContext(unitName = "ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SousTacheFacade() {
        super(SousTache.class);
    }
    //chart
     //init barChart //pour construire la sereis des coordonnees pour les bar
    public BarChartModel initBarModel(List<SousTache> staches,Stage stage) {
        ChartSeries sousTcheSerie = new ChartSeries();
        BarChartModel model1 = new BarChartModel();
        sousTcheSerie.setLabel("" + stage);
        int x;
        for (x = 1; x < 5; x++) {
            Double a = 0.0;
            for (SousTache sousTache : staches) {
                    a += sousTache.getAvancement();
                }
               
            
            sousTcheSerie.set("sousTache " + x, a);
        }
        model1.addSeries(sousTcheSerie);
        return model1;
    }

       public Double maxChart(List<SousTache> staches,Stage stage){
            Double a = 0.0;
            for (SousTache sousTache : staches) {
                
            
                    a += sousTache.getAvancement();
                }
               
        
        return a;
                }
    }

//    // Chart-Donut
//    public DonutChartModel initDonuModel(List<TaxeTrimBoisson> taxes, int firstYear, int secondYear) {
//        DonutChartModel donutMoel = new DonutChartModel();
//        Map<String, Number> firstCircle = new LinkedHashMap<>();
//        Map<String, Number> secondCircle = new LinkedHashMap<>();
//        int x;
//        for (x = 1; x < 5; x++) {
//            Double a = 0.0;
//            Double b = 0.0;
//            for (TaxeTrimBoisson taxeTrim : taxes) {
//                if (taxeTrim.getTaxeAnnuelBoisson().getAnnee() == firstYear && taxeTrim.getNumeroTrim() == x) {
//                    a += taxeTrim.getMontantTotalTaxe();
//                }
//                if (taxeTrim.getTaxeAnnuelBoisson().getAnnee() == secondYear && taxeTrim.getNumeroTrim() == x) {
//                    b += taxeTrim.getMontantTotalTaxe();
//                }
//            }
//            firstCircle.put("Trimestre " + x + "-" + firstYear, a);
//            secondCircle.put("Trimestre " + x + "-" + secondYear, b);
//        }
//
//        donutMoel.addCircle(firstCircle);
//        donutMoel.addCircle(secondCircle);
//        return donutMoel;
//    }
//
//    //initialiser  lineChart
//    public LineChartModel initLineModel(List<TaxeTrimBoisson> taxes, int anneeDeb, int anneeFin) {
//        ChartSeries firstSerie = new ChartSeries();
//        ChartSeries secondSerie = new ChartSeries();
//        int x;
//        for (x = 1; x < 5; x++) {
//            Double a = 0.0;
//            Double b = 0.0;
//            for (TaxeTrimBoisson taxeTrim : taxes) {
//                if (taxeTrim.getTaxeAnnuelBoisson().getAnnee() == anneeDeb && taxeTrim.getNumeroTrim() == x) {
//                    a += taxeTrim.getMontantTotalTaxe();
//                }
//                if (taxeTrim.getTaxeAnnuelBoisson().getAnnee() == anneeFin && taxeTrim.getNumeroTrim() == x) {
//                    b += taxeTrim.getMontantTotalTaxe();
//                }
//            }
//            firstSerie.set("Trimestre " + x, a);
//            secondSerie.set("Trimestre " + x, b);
//        }
//        firstSerie.setLabel("" + anneeDeb);
//        secondSerie.setLabel("" + anneeFin);
//        LineChartModel modelLine = new LineChartModel();
//        modelLine.addSeries(firstSerie);
//        modelLine.addSeries(secondSerie);
//        modelLine.setShowPointLabels(true);
//        return modelLine;
//    }



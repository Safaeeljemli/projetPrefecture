/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author safa
 */
@Entity
public class CourrierArrivee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long n_enregistrement;
    private Date dateEnregistrement;
    private String motsCle;
    private String codeA_V;
    private String objet;
    private List<String> expediteurs;
    private List<String> modeTraitement;
    @OneToOne
    private DetailCourrier detailCourrier;
    @ManyToOne
    private SousClasse sousClasse;
    
    public Long getN_enregistrement() {
        return n_enregistrement;
    }

    public void setN_enregistrement(Long n_enregistrement) {
        this.n_enregistrement = n_enregistrement;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public String getMotsCle() {
        return motsCle;
    }

    public void setMotsCle(String motsCle) {
        this.motsCle = motsCle;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }


    public List<String> getExpediteurs() {
        return expediteurs;
    }

    public void setExpediteurs(List<String> expediteurs) {
        this.expediteurs = expediteurs;
    }

    public List<String> getModeTraitement() {
        return modeTraitement;
    }

    public void setModeTraitement(List<String> modeTraitement) {
        this.modeTraitement = modeTraitement;
    }

    public String getCodeA_V() {
        return codeA_V;
    }

    public void setCodeA_V(String codeA_V) {
        this.codeA_V = codeA_V;
    }

    public DetailCourrier getDetailCourrier() {
        return detailCourrier;
    }

    public void setDetailCourrier(DetailCourrier detailCourrier) {
        this.detailCourrier = detailCourrier;
    }

    public SousClasse getSousClasse() {
        return sousClasse;
    }

    public void setSousClasse(SousClasse sousClasse) {
        this.sousClasse = sousClasse;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (n_enregistrement != null ? n_enregistrement.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the n_enregistrement fields are not set
        if (!(object instanceof CourrierArrivee)) {
            return false;
        }
        CourrierArrivee other = (CourrierArrivee) object;
        if ((this.n_enregistrement == null && other.n_enregistrement != null) || (this.n_enregistrement != null && !this.n_enregistrement.equals(other.n_enregistrement))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.CourrierArrivee[ id=" + n_enregistrement + " ]";
    }

}

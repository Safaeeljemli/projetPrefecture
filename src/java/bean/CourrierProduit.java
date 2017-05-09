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
public class CourrierProduit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long n_dordre;
    private Date dateCreation;
    private String codeP_V;
    private String objet;
    @ManyToOne
    private Finalite finalite;
    @ManyToOne
    private Destinataire destinataiere;
    @OneToOne
    private DetailCourrier detailCourrier;

    public Long getN_dordre() {
        return n_dordre;
    }

    public void setN_dordre(Long n_dordre) {
        this.n_dordre = n_dordre;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCodeP_V() {
        return codeP_V;
    }

    public void setCodeP_V(String codeP_V) {
        this.codeP_V = codeP_V;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Finalite getFinalite() {
        return finalite;
    }

    public void setFinalite(Finalite finalite) {
        this.finalite = finalite;
    }

    public Destinataire getDestinataiere() {
        return destinataiere;
    }

    public void setDestinataiere(Destinataire destinataiere) {
        this.destinataiere = destinataiere;
    }

  

    public DetailCourrier getDetailCourrier() {
        return detailCourrier;
    }

    public void setDetailCourrier(DetailCourrier detailCourrier) {
        this.detailCourrier = detailCourrier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (n_dordre != null ? n_dordre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the n_dordre fields are not set
        if (!(object instanceof CourrierProduit)) {
            return false;
        }
        CourrierProduit other = (CourrierProduit) object;
        if ((this.n_dordre == null && other.n_dordre != null) || (this.n_dordre != null && !this.n_dordre.equals(other.n_dordre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.CourrierProduit[ id=" + n_dordre + " ]";
    }

}

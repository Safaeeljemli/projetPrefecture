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
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author safa
 */
@Entity
public class DetailCourrier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    //parametre courrier Arrivee:
    private int aN_enregistrementDRHMG;
    private int aN_enregistrementBOW_TRANS_RLAN;
    private Date pDateEnregistrementDRHMG;
    private Date pDateEnregistrementBOW_TRANS_RLAN;

    //parametre courrier Produit:
    private Date pDateEnvoiePourValidation;
    private Date pDateRetourPourValidation;
    private Date pDateRetourAuBOW_TRANS;
    private Date pDateRetourDeLaMinuteDuOW_TRANS;
    private Date pDateEnvoiParBOW_TRANS;
    private int pN_EnvoiParBOW_TRANS;
    private String raisonSignature;

    private List<String> pEtatValidation;
    private List<String> pDestinataire;

    @OneToOne(mappedBy = "detailCourrier")
    private CourrierProduit courrierProduit;

    @OneToOne(mappedBy = "detailCourrier")
    private CourrierArrivee courrierArrivee;

    public DetailCourrier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CourrierProduit getCourrierProduit() {
        return courrierProduit;
    }

    public void setCourrierProduit(CourrierProduit courrierProduit) {
        this.courrierProduit = courrierProduit;
    }

    public int getaN_enregistrementDRHMG() {
        return aN_enregistrementDRHMG;
    }

    public void setaN_enregistrementDRHMG(int aN_enregistrementDRHMG) {
        this.aN_enregistrementDRHMG = aN_enregistrementDRHMG;
    }

    public int getaN_enregistrementBOW_TRANS_RLAN() {
        return aN_enregistrementBOW_TRANS_RLAN;
    }

    public void setaN_enregistrementBOW_TRANS_RLAN(int aN_enregistrementBOW_TRANS_RLAN) {
        this.aN_enregistrementBOW_TRANS_RLAN = aN_enregistrementBOW_TRANS_RLAN;
    }

    public Date getpDateEnregistrementDRHMG() {
        return pDateEnregistrementDRHMG;
    }

    public void setpDateEnregistrementDRHMG(Date pDateEnregistrementDRHMG) {
        this.pDateEnregistrementDRHMG = pDateEnregistrementDRHMG;
    }

    public Date getpDateEnregistrementBOW_TRANS_RLAN() {
        return pDateEnregistrementBOW_TRANS_RLAN;
    }

    public void setpDateEnregistrementBOW_TRANS_RLAN(Date pDateEnregistrementBOW_TRANS_RLAN) {
        this.pDateEnregistrementBOW_TRANS_RLAN = pDateEnregistrementBOW_TRANS_RLAN;
    }

    public Date getpDateEnvoiePourValidation() {
        return pDateEnvoiePourValidation;
    }

    public void setpDateEnvoiePourValidation(Date pDateEnvoiePourValidation) {
        this.pDateEnvoiePourValidation = pDateEnvoiePourValidation;
    }

    public Date getpDateRetourPourValidation() {
        return pDateRetourPourValidation;
    }

    public void setpDateRetourPourValidation(Date pDateRetourPourValidation) {
        this.pDateRetourPourValidation = pDateRetourPourValidation;
    }

    public Date getpDateRetourAuBOW_TRANS() {
        return pDateRetourAuBOW_TRANS;
    }

    public void setpDateRetourAuBOW_TRANS(Date pDateRetourAuBOW_TRANS) {
        this.pDateRetourAuBOW_TRANS = pDateRetourAuBOW_TRANS;
    }

    public Date getpDateRetourDeLaMinuteDuOW_TRANS() {
        return pDateRetourDeLaMinuteDuOW_TRANS;
    }

    public void setpDateRetourDeLaMinuteDuOW_TRANS(Date pDateRetourDeLaMinuteDuOW_TRANS) {
        this.pDateRetourDeLaMinuteDuOW_TRANS = pDateRetourDeLaMinuteDuOW_TRANS;
    }

    public Date getpDateEnvoiParBOW_TRANS() {
        return pDateEnvoiParBOW_TRANS;
    }

    public void setpDateEnvoiParBOW_TRANS(Date pDateEnvoiParBOW_TRANS) {
        this.pDateEnvoiParBOW_TRANS = pDateEnvoiParBOW_TRANS;
    }

    public int getpN_EnvoiParBOW_TRANS() {
        return pN_EnvoiParBOW_TRANS;
    }

    public void setpN_EnvoiParBOW_TRANS(int pN_EnvoiParBOW_TRANS) {
        this.pN_EnvoiParBOW_TRANS = pN_EnvoiParBOW_TRANS;
    }

    public String getRaisonSignature() {
        return raisonSignature;
    }

    public void setRaisonSignature(String raisonSignature) {
        this.raisonSignature = raisonSignature;
    }

    public List<String> getpEtatValidation() {
        return pEtatValidation;
    }

    public void setpEtatValidation(List<String> pEtatValidation) {
        this.pEtatValidation = pEtatValidation;
    }

    public List<String> getpDestinataire() {
        return pDestinataire;
    }

    public void setpDestinataire(List<String> pDestinataire) {
        this.pDestinataire = pDestinataire;
    }

    public CourrierArrivee getCourrierArrivee() {
        return courrierArrivee;
    }

    public void setCourrierArrivee(CourrierArrivee courrierArrivee) {
        this.courrierArrivee = courrierArrivee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailCourrier)) {
            return false;
        }
        DetailCourrier other = (DetailCourrier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.DetailCourrier[ id=" + id + " ]";
    }

}

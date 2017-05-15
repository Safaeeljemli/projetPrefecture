/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

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
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnregistrement;
    private String motsCle;
    private String codeA_V;
    private String objet;
    private int n_enregistrementDRHMG;
    private int n_enregistrementBOW_TRANS_RLAN;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnregistrementDRHMG;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnregistrementBOW_TRANS_RLAN;
    @ManyToOne
    private DestinataireExpediteur destinataireExpediteur;
    @ManyToOne
    private ModeTraitement modeTraitement;
    @ManyToOne
    private SousClasse sousClasse;
    @OneToMany(mappedBy = "courrierArrivee")
    private List<CourrierProduit> courrierProduits;

    public List<CourrierProduit> getCourrierProduits() {
        return courrierProduits;
    }

    public void setCourrierProduits(List<CourrierProduit> courrierProduits) {
        this.courrierProduits = courrierProduits;
    }

    public int getN_enregistrementDRHMG() {
        return n_enregistrementDRHMG;
    }

    public void setN_enregistrementDRHMG(int n_enregistrementDRHMG) {
        this.n_enregistrementDRHMG = n_enregistrementDRHMG;
    }

    public int getN_enregistrementBOW_TRANS_RLAN() {
        return n_enregistrementBOW_TRANS_RLAN;
    }

    public void setN_enregistrementBOW_TRANS_RLAN(int n_enregistrementBOW_TRANS_RLAN) {
        this.n_enregistrementBOW_TRANS_RLAN = n_enregistrementBOW_TRANS_RLAN;
    }

    public Date getDateEnregistrementDRHMG() {
        return dateEnregistrementDRHMG;
    }

    public void setDateEnregistrementDRHMG(Date dateEnregistrementDRHMG) {
        this.dateEnregistrementDRHMG = dateEnregistrementDRHMG;
    }

    public Date getDateEnregistrementBOW_TRANS_RLAN() {
        return dateEnregistrementBOW_TRANS_RLAN;
    }

    public void setDateEnregistrementBOW_TRANS_RLAN(Date dateEnregistrementBOW_TRANS_RLAN) {
        this.dateEnregistrementBOW_TRANS_RLAN = dateEnregistrementBOW_TRANS_RLAN;
    }

    public DestinataireExpediteur getDestinataireExpediteur() {
        return destinataireExpediteur;
    }

    public void setDestinataireExpediteur(DestinataireExpediteur destinataireExpediteur) {
        this.destinataireExpediteur = destinataireExpediteur;
    }

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

    public ModeTraitement getModeTraitement() {
        return modeTraitement;
    }

    public void setModeTraitement(ModeTraitement modeTraitement) {
        this.modeTraitement = modeTraitement;
    }

    public String getCodeA_V() {
        return codeA_V;
    }

    public void setCodeA_V(String codeA_V) {
        this.codeA_V = codeA_V;
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
        return codeA_V;
    }

}

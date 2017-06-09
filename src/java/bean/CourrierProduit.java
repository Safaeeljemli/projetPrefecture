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
import javax.persistence.Temporal;

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

    private String codeP_V;
    private String objet;
    private String photo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnvoiePourValidation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateRetourDocument;
    private int etat; //1-signee 2-pas_signee
    private String raisonSignature;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnvoiAuBOW_TRANS;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnvoiParBOW_TRANS;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateRetourDeLaMinuteDuBOW_TRANS;
    private Long n_EnvoiParBOW_TRANS;
    @ManyToOne
    private SousClasse sousClasse;
    @ManyToOne
    private Finalite finalite;
    @ManyToOne
    private CourrierArrivee courrierArrivee;
    @ManyToOne
    private DestinataireExpediteur destinataireExpediteur;

    public Long getN_dordre() {
        return n_dordre;
    }

    public void setN_dordre(Long n_dordre) {
        this.n_dordre = n_dordre;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public DestinataireExpediteur getDestinataireExpediteur() {
        return destinataireExpediteur;
    }

    public void setDestinataireExpediteur(DestinataireExpediteur destinataireExpediteur) {
        this.destinataireExpediteur = destinataireExpediteur;
    }

    public CourrierArrivee getCourrierArrivee() {
        return courrierArrivee;
    }

    public void setCourrierArrivee(CourrierArrivee courrierArrivee) {
        this.courrierArrivee = courrierArrivee;
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

    public Date getDateEnvoiePourValidation() {
        return dateEnvoiePourValidation;
    }

    public void setDateEnvoiePourValidation(Date dateEnvoiePourValidation) {
        this.dateEnvoiePourValidation = dateEnvoiePourValidation;
    }

    public Date getDateRetourDocument() {
        return dateRetourDocument;
    }

    public void setDateRetourDocument(Date dateRetourDocument) {
        this.dateRetourDocument = dateRetourDocument;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getRaisonSignature() {
        return raisonSignature;
    }

    public void setRaisonSignature(String raisonSignature) {
        this.raisonSignature = raisonSignature;
    }

    public Date getDateEnvoiAuBOW_TRANS() {
        return dateEnvoiAuBOW_TRANS;
    }

    public void setDateEnvoiAuBOW_TRANS(Date dateEnvoiAuBOW_TRANS) {
        this.dateEnvoiAuBOW_TRANS = dateEnvoiAuBOW_TRANS;
    }

    public Date getDateEnvoiParBOW_TRANS() {
        return dateEnvoiParBOW_TRANS;
    }

    public void setDateEnvoiParBOW_TRANS(Date dateEnvoiParBOW_TRANS) {
        this.dateEnvoiParBOW_TRANS = dateEnvoiParBOW_TRANS;
    }

    public Date getDateRetourDeLaMinuteDuBOW_TRANS() {
        return dateRetourDeLaMinuteDuBOW_TRANS;
    }

    public void setDateRetourDeLaMinuteDuBOW_TRANS(Date dateRetourDeLaMinuteDuBOW_TRANS) {
        this.dateRetourDeLaMinuteDuBOW_TRANS = dateRetourDeLaMinuteDuBOW_TRANS;
    }

    public Long getN_EnvoiParBOW_TRANS() {
        return n_EnvoiParBOW_TRANS;
    }

    public void setN_EnvoiParBOW_TRANS(Long n_EnvoiParBOW_TRANS) {
        this.n_EnvoiParBOW_TRANS = n_EnvoiParBOW_TRANS;
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

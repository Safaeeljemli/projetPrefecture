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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author PC
 */
@Entity
public class Formation implements Serializable {

    @OneToMany(mappedBy = "formation")
    private List<FormationItem> formationItems;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    @ManyToOne
    private LieuFormation lieuFormation;
    @ManyToOne
    private OrganismeFormation organismeFormation;
    @ManyToOne
    private Formateur formateur;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDebut;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFin;
    @ManyToMany
    private List<Contact> participants;
    @OneToOne
    private Position position;

    ///getter and setter et fct predefinis
    public Long getId() {
        return id;
    }

    public List<FormationItem> getFormationItems() {
        return formationItems;
    }

    public void setFormationItems(List<FormationItem> formationItems) {
        this.formationItems = formationItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Contact> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Contact> participants) {
        this.participants = participants;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        if (!(object instanceof Formation)) {
            return false;
        }
        Formation other = (Formation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LieuFormation getLieuFormation() {
        return lieuFormation;
    }

    public void setLieuFormation(LieuFormation lieuFormation) {
        this.lieuFormation = lieuFormation;
    }

    public OrganismeFormation getOrganismeFormation() {
        return organismeFormation;
    }

    public void setOrganismeFormation(OrganismeFormation organismeFormation) {
        this.organismeFormation = organismeFormation;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "bean.Formation[ id=" + id + " ]";
    }

}

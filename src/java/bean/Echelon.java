/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author PC
 */
@Entity
public class Echelon implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date debutEchellon;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finEchellon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDebutEchellon() {
        return debutEchellon;
    }

    public void setDebutEchellon(Date debutEchellon) {
        this.debutEchellon = debutEchellon;
    }

    public Date getFinEchellon() {
        return finEchellon;
    }

    public void setFinEchellon(Date finEchellon) {
        this.finEchellon = finEchellon;
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
        if (!(object instanceof Echelon)) {
            return false;
        }
        Echelon other = (Echelon) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Echelon[ id=" + id + " ]";
    }

}

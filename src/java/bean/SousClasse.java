/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author safa
 */
@Entity
public class SousClasse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    @ManyToOne
    private Classe classe;
    @OneToMany(mappedBy = "sousClasse")
    private List<CourrierArrivee> courrierArrivees;
    @OneToMany(mappedBy = "sousClasse")
    private List<CourrierProduit> courrierProduits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CourrierProduit> getCourrierProduits() {
        return courrierProduits;
    }

    public void setCourrierProduits(List<CourrierProduit> courrierProduits) {
        this.courrierProduits = courrierProduits;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public List<CourrierArrivee> getCourrierArrivees() {
        return courrierArrivees;
    }

    public void setCourrierArrivees(List<CourrierArrivee> courrierArrivees) {
        this.courrierArrivees = courrierArrivees;
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
        if (!(object instanceof SousClasse)) {
            return false;
        }
        SousClasse other = (SousClasse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.SousClasse[ id=" + id + " ]";
    }

}

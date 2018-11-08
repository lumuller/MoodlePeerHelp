/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luana
 */
@Entity
@Table(name = "t_recursos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TRecursos.findAll", query = "SELECT t FROM TRecursos t"),
    @NamedQuery(name = "TRecursos.findByIdRecursos", query = "SELECT t FROM TRecursos t WHERE t.idRecursos = :idRecursos"),
    @NamedQuery(name = "TRecursos.findByNome", query = "SELECT t FROM TRecursos t WHERE t.nome = :nome")})
public class TRecursos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_recursos")
    private Integer idRecursos;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @OneToMany(mappedBy = "idRecurso")
    private Collection<TDuvidas> tDuvidasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tRecursos")
    private Collection<TRecursosUsuarios> tRecursosUsuariosCollection;

    public TRecursos() {
    }

    public TRecursos(Integer idRecursos) {
        this.idRecursos = idRecursos;
    }

    public TRecursos(Integer idRecursos, String nome) {
        this.idRecursos = idRecursos;
        this.nome = nome;
    }

    public Integer getIdRecursos() {
        return idRecursos;
    }

    public void setIdRecursos(Integer idRecursos) {
        this.idRecursos = idRecursos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public Collection<TDuvidas> getTDuvidasCollection() {
        return tDuvidasCollection;
    }

    public void setTDuvidasCollection(Collection<TDuvidas> tDuvidasCollection) {
        this.tDuvidasCollection = tDuvidasCollection;
    }

    @XmlTransient
    public Collection<TRecursosUsuarios> getTRecursosUsuariosCollection() {
        return tRecursosUsuariosCollection;
    }

    public void setTRecursosUsuariosCollection(Collection<TRecursosUsuarios> tRecursosUsuariosCollection) {
        this.tRecursosUsuariosCollection = tRecursosUsuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecursos != null ? idRecursos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TRecursos)) {
            return false;
        }
        TRecursos other = (TRecursos) object;
        if ((this.idRecursos == null && other.idRecursos != null) || (this.idRecursos != null && !this.idRecursos.equals(other.idRecursos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TRecursos[ idRecursos=" + idRecursos + " ]";
    }
    
}

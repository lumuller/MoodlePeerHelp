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

/**
 *
 * @author Luana
 */
@Entity
@Table(name = "t_usuarios")
@NamedQueries({
    @NamedQuery(name = "TUsuarios.findAll", query = "SELECT t FROM TUsuarios t")})
public class TUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_usuarios")
    private String idUsuarios;
    @Basic(optional = false)
    @Column(name = "senha")
    private String senha;
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "tempo_uso_moodle")
    private int tempoUsoMoodle;
    @Basic(optional = false)
    @Column(name = "nivel")
    private int nivel;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuariosR")
    private Collection<TDuvidas> tDuvidasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuariosP")
    private Collection<TDuvidas> tDuvidasCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tUsuarios")
    private Collection<TRecursosUsuarios> tRecursosUsuariosCollection;

    public TUsuarios() {
    }

    public TUsuarios(String idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public TUsuarios(String idUsuarios, String senha, int tempoUsoMoodle, int nivel, String email) {
        this.idUsuarios = idUsuarios;
        this.senha = senha;
        this.tempoUsoMoodle = tempoUsoMoodle;
        this.nivel = nivel;
        this.email = email;
    }

    public String getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(String idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempoUsoMoodle() {
        return tempoUsoMoodle;
    }

    public void setTempoUsoMoodle(int tempoUsoMoodle) {
        this.tempoUsoMoodle = tempoUsoMoodle;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<TDuvidas> getTDuvidasCollection() {
        return tDuvidasCollection;
    }

    public void setTDuvidasCollection(Collection<TDuvidas> tDuvidasCollection) {
        this.tDuvidasCollection = tDuvidasCollection;
    }

    public Collection<TDuvidas> getTDuvidasCollection1() {
        return tDuvidasCollection1;
    }

    public void setTDuvidasCollection1(Collection<TDuvidas> tDuvidasCollection1) {
        this.tDuvidasCollection1 = tDuvidasCollection1;
    }

    public Collection<TRecursosUsuarios> getTRecursosUsuariosCollection() {
        return tRecursosUsuariosCollection;
    }

    public void setTRecursosUsuariosCollection(Collection<TRecursosUsuarios> tRecursosUsuariosCollection) {
        this.tRecursosUsuariosCollection = tRecursosUsuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarios != null ? idUsuarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TUsuarios)) {
            return false;
        }
        TUsuarios other = (TUsuarios) object;
        if ((this.idUsuarios == null && other.idUsuarios != null) || (this.idUsuarios != null && !this.idUsuarios.equals(other.idUsuarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.TUsuarios[ idUsuarios=" + idUsuarios + " ]";
    }
    
}

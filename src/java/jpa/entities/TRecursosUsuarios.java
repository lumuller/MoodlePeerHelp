/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Luana
 */
@Entity
@Table(name = "t_recursos_usuarios")
@NamedQueries({
    @NamedQuery(name = "TRecursosUsuarios.findAll", query = "SELECT t FROM TRecursosUsuarios t")})
public class TRecursosUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TRecursosUsuariosPK tRecursosUsuariosPK;
    @Basic(optional = false)
    @Column(name = "aptidao")
    private int aptidao;
    @JoinColumn(name = "id_usuarios", referencedColumnName = "id_usuarios", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TUsuarios tUsuarios;
    @JoinColumn(name = "id_recursos", referencedColumnName = "id_recursos", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TRecursos tRecursos;

    public TRecursosUsuarios() {
    }

    public TRecursosUsuarios(TRecursosUsuariosPK tRecursosUsuariosPK) {
        this.tRecursosUsuariosPK = tRecursosUsuariosPK;
    }

    public TRecursosUsuarios(TRecursosUsuariosPK tRecursosUsuariosPK, int aptidao) {
        this.tRecursosUsuariosPK = tRecursosUsuariosPK;
        this.aptidao = aptidao;
    }

    public TRecursosUsuarios(int idRecursos, String idUsuarios) {
        this.tRecursosUsuariosPK = new TRecursosUsuariosPK(idRecursos, idUsuarios);
    }

    public TRecursosUsuariosPK getTRecursosUsuariosPK() {
        return tRecursosUsuariosPK;
    }

    public void setTRecursosUsuariosPK(TRecursosUsuariosPK tRecursosUsuariosPK) {
        this.tRecursosUsuariosPK = tRecursosUsuariosPK;
    }

    public int getAptidao() {
        return aptidao;
    }

    public void setAptidao(int aptidao) {
        this.aptidao = aptidao;
    }

    public TUsuarios getTUsuarios() {
        return tUsuarios;
    }

    public void setTUsuarios(TUsuarios tUsuarios) {
        this.tUsuarios = tUsuarios;
    }

    public TRecursos getTRecursos() {
        return tRecursos;
    }

    public void setTRecursos(TRecursos tRecursos) {
        this.tRecursos = tRecursos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tRecursosUsuariosPK != null ? tRecursosUsuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TRecursosUsuarios)) {
            return false;
        }
        TRecursosUsuarios other = (TRecursosUsuarios) object;
        if ((this.tRecursosUsuariosPK == null && other.tRecursosUsuariosPK != null) || (this.tRecursosUsuariosPK != null && !this.tRecursosUsuariosPK.equals(other.tRecursosUsuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.TRecursosUsuarios[ tRecursosUsuariosPK=" + tRecursosUsuariosPK + " ]";
    }
    
}

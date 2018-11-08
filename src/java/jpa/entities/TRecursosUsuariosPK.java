/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Luana
 */
@Embeddable
public class TRecursosUsuariosPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_recursos")
    private int idRecursos;
    @Basic(optional = false)
    @Column(name = "id_usuarios")
    private String idUsuarios;

    public TRecursosUsuariosPK() {
    }

    public TRecursosUsuariosPK(int idRecursos, String idUsuarios) {
        this.idRecursos = idRecursos;
        this.idUsuarios = idUsuarios;
    }

    public int getIdRecursos() {
        return idRecursos;
    }

    public void setIdRecursos(int idRecursos) {
        this.idRecursos = idRecursos;
    }

    public String getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(String idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRecursos;
        hash += (idUsuarios != null ? idUsuarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TRecursosUsuariosPK)) {
            return false;
        }
        TRecursosUsuariosPK other = (TRecursosUsuariosPK) object;
        if (this.idRecursos != other.idRecursos) {
            return false;
        }
        if ((this.idUsuarios == null && other.idUsuarios != null) || (this.idUsuarios != null && !this.idUsuarios.equals(other.idUsuarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.TRecursosUsuariosPK[ idRecursos=" + idRecursos + ", idUsuarios=" + idUsuarios + " ]";
    }
    
}

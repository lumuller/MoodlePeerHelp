/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luana
 */
@Entity
@Table(name = "t_auditoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TAuditoria.findAll", query = "SELECT t FROM TAuditoria t"),
    @NamedQuery(name = "TAuditoria.findByIdAuditoria", query = "SELECT t FROM TAuditoria t WHERE t.idAuditoria = :idAuditoria"),
    @NamedQuery(name = "TAuditoria.findByIdDuvidas", query = "SELECT t FROM TAuditoria t WHERE t.idDuvidas = :idDuvidas"),
    @NamedQuery(name = "TAuditoria.findByNivelEscolha", query = "SELECT t FROM TAuditoria t WHERE t.nivelEscolha = :nivelEscolha"),
    @NamedQuery(name = "TAuditoria.findByFindOther", query = "SELECT t FROM TAuditoria t WHERE t.findOther = :findOther")})
public class TAuditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;
    @Column(name = "id_duvidas")
    private Integer idDuvidas;
    @Column(name = "nivel_escolha")
    private String nivelEscolha;
    @Column(name = "find_other")
    private String findOther;

    public TAuditoria() {
    }

    public TAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdDuvidas() {
        return idDuvidas;
    }

    public void setIdDuvidas(Integer idDuvidas) {
        this.idDuvidas = idDuvidas;
    }

    public String getNivelEscolha() {
        return nivelEscolha;
    }

    public void setNivelEscolha(String nivelEscolha) {
        this.nivelEscolha = nivelEscolha;
    }

    public String getFindOther() {
        return findOther;
    }

    public void setFindOther(String findOther) {
        this.findOther = findOther;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuditoria != null ? idAuditoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TAuditoria)) {
            return false;
        }
        TAuditoria other = (TAuditoria) object;
        if ((this.idAuditoria == null && other.idAuditoria != null) || (this.idAuditoria != null && !this.idAuditoria.equals(other.idAuditoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.TAuditoria[ idAuditoria=" + idAuditoria + " ]";
    }
    
}

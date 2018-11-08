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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luana
 */
@Entity
@Table(name = "t_duvidas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TDuvidas.findAll", query = "SELECT t FROM TDuvidas t"),
    @NamedQuery(name = "TDuvidas.findByIdDuvidas", query = "SELECT t FROM TDuvidas t WHERE t.idDuvidas = :idDuvidas"),
    @NamedQuery(name = "TDuvidas.findByDuvida", query = "SELECT t FROM TDuvidas t WHERE t.duvida = :duvida"),
    @NamedQuery(name = "TDuvidas.findByResposta", query = "SELECT t FROM TDuvidas t WHERE t.resposta = :resposta"),
    @NamedQuery(name = "TDuvidas.findByIsQualificada", query = "SELECT t FROM TDuvidas t WHERE t.isQualificada = :isQualificada")})
public class TDuvidas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_duvidas")
    private Integer idDuvidas;
    @Basic(optional = false)
    @Column(name = "duvida")
    private String duvida;
    @Column(name = "resposta")
    private String resposta;
    @Column(name = "is_qualificada")
    private String isQualificada;
    @JoinColumn(name = "id_usuarios_r", referencedColumnName = "id_usuarios")
    @ManyToOne(optional = false)
    private TUsuarios idUsuariosR;
    @JoinColumn(name = "id_usuarios_p", referencedColumnName = "id_usuarios")
    @ManyToOne(optional = false)
    private TUsuarios idUsuariosP;
    @JoinColumn(name = "id_recurso", referencedColumnName = "id_recursos")
    @ManyToOne
    private TRecursos idRecurso;

    public TDuvidas() {
    }

    public TDuvidas(Integer idDuvidas) {
        this.idDuvidas = idDuvidas;
    }

    public TDuvidas(Integer idDuvidas, String duvida) {
        this.idDuvidas = idDuvidas;
        this.duvida = duvida;
    }

    public Integer getIdDuvidas() {
        return idDuvidas;
    }

    public void setIdDuvidas(Integer idDuvidas) {
        this.idDuvidas = idDuvidas;
    }

    public String getDuvida() {
        return duvida;
    }

    public void setDuvida(String duvida) {
        this.duvida = duvida;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getIsQualificada() {
        return isQualificada;
    }

    public void setIsQualificada(String isQualificada) {
        this.isQualificada = isQualificada;
    }

    public TUsuarios getIdUsuariosR() {
        return idUsuariosR;
    }

    public void setIdUsuariosR(TUsuarios idUsuariosR) {
        this.idUsuariosR = idUsuariosR;
    }

    public TUsuarios getIdUsuariosP() {
        return idUsuariosP;
    }

    public void setIdUsuariosP(TUsuarios idUsuariosP) {
        this.idUsuariosP = idUsuariosP;
    }

    public TRecursos getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(TRecursos idRecurso) {
        this.idRecurso = idRecurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDuvidas != null ? idDuvidas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TDuvidas)) {
            return false;
        }
        TDuvidas other = (TDuvidas) object;
        if ((this.idDuvidas == null && other.idDuvidas != null) || (this.idDuvidas != null && !this.idDuvidas.equals(other.idDuvidas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TDuvidas[ idDuvidas=" + idDuvidas + " ]";
    }
    
}

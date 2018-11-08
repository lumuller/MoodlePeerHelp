/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.TUsuarios;
import jpa.entities.TRecursos;
import jpa.entities.TRecursosUsuarios;
import jpa.entities.TRecursosUsuariosPK;
import jpa.entities.TRecursosUsuariosPK_;
import jpa.entities.TUsuarios_;

/**
 *
 * @author Luana
 */
public class TRecursosUsuariosJpaController implements Serializable {

    public TRecursosUsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TRecursosUsuarios TRecursosUsuarios) throws PreexistingEntityException, Exception {
        if (TRecursosUsuarios.getTRecursosUsuariosPK() == null) {
            TRecursosUsuarios.setTRecursosUsuariosPK(new TRecursosUsuariosPK());
        }
        TRecursosUsuarios.getTRecursosUsuariosPK().setIdUsuarios(TRecursosUsuarios.getTUsuarios().getIdUsuarios());
        TRecursosUsuarios.getTRecursosUsuariosPK().setIdRecursos(TRecursosUsuarios.getTRecursos().getIdRecursos());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TUsuarios TUsuarios = TRecursosUsuarios.getTUsuarios();
            if (TUsuarios != null) {
                TUsuarios = em.getReference(TUsuarios.getClass(), TUsuarios.getIdUsuarios());
                TRecursosUsuarios.setTUsuarios(TUsuarios);
            }
            TRecursos TRecursos = TRecursosUsuarios.getTRecursos();
            if (TRecursos != null) {
                TRecursos = em.getReference(TRecursos.getClass(), TRecursos.getIdRecursos());
                TRecursosUsuarios.setTRecursos(TRecursos);
            }
            em.persist(TRecursosUsuarios);
            if (TUsuarios != null) {
                TUsuarios.getTRecursosUsuariosCollection().add(TRecursosUsuarios);
                TUsuarios = em.merge(TUsuarios);
            }
            if (TRecursos != null) {
                TRecursos.getTRecursosUsuariosCollection().add(TRecursosUsuarios);
                TRecursos = em.merge(TRecursos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTRecursosUsuarios(TRecursosUsuarios.getTRecursosUsuariosPK()) != null) {
                throw new PreexistingEntityException("TRecursosUsuarios " + TRecursosUsuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TRecursosUsuarios TRecursosUsuarios) throws NonexistentEntityException, Exception {
        TRecursosUsuarios.getTRecursosUsuariosPK().setIdUsuarios(TRecursosUsuarios.getTUsuarios().getIdUsuarios());
        TRecursosUsuarios.getTRecursosUsuariosPK().setIdRecursos(TRecursosUsuarios.getTRecursos().getIdRecursos());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TRecursosUsuarios persistentTRecursosUsuarios = em.find(TRecursosUsuarios.class, TRecursosUsuarios.getTRecursosUsuariosPK());
            TUsuarios TUsuariosOld = persistentTRecursosUsuarios.getTUsuarios();
            TUsuarios TUsuariosNew = TRecursosUsuarios.getTUsuarios();
            TRecursos TRecursosOld = persistentTRecursosUsuarios.getTRecursos();
            TRecursos TRecursosNew = TRecursosUsuarios.getTRecursos();
            if (TUsuariosNew != null) {
                TUsuariosNew = em.getReference(TUsuariosNew.getClass(), TUsuariosNew.getIdUsuarios());
                TRecursosUsuarios.setTUsuarios(TUsuariosNew);
            }
            if (TRecursosNew != null) {
                TRecursosNew = em.getReference(TRecursosNew.getClass(), TRecursosNew.getIdRecursos());
                TRecursosUsuarios.setTRecursos(TRecursosNew);
            }
            TRecursosUsuarios = em.merge(TRecursosUsuarios);
            if (TUsuariosOld != null && !TUsuariosOld.equals(TUsuariosNew)) {
                TUsuariosOld.getTRecursosUsuariosCollection().remove(TRecursosUsuarios);
                TUsuariosOld = em.merge(TUsuariosOld);
            }
            if (TUsuariosNew != null && !TUsuariosNew.equals(TUsuariosOld)) {
                TUsuariosNew.getTRecursosUsuariosCollection().add(TRecursosUsuarios);
                TUsuariosNew = em.merge(TUsuariosNew);
            }
            if (TRecursosOld != null && !TRecursosOld.equals(TRecursosNew)) {
                TRecursosOld.getTRecursosUsuariosCollection().remove(TRecursosUsuarios);
                TRecursosOld = em.merge(TRecursosOld);
            }
            if (TRecursosNew != null && !TRecursosNew.equals(TRecursosOld)) {
                TRecursosNew.getTRecursosUsuariosCollection().add(TRecursosUsuarios);
                TRecursosNew = em.merge(TRecursosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TRecursosUsuariosPK id = TRecursosUsuarios.getTRecursosUsuariosPK();
                if (findTRecursosUsuarios(id) == null) {
                    throw new NonexistentEntityException("The tRecursosUsuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TRecursosUsuariosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TRecursosUsuarios TRecursosUsuarios;
            try {
                TRecursosUsuarios = em.getReference(TRecursosUsuarios.class, id);
                TRecursosUsuarios.getTRecursosUsuariosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TRecursosUsuarios with id " + id + " no longer exists.", enfe);
            }
            TUsuarios TUsuarios = TRecursosUsuarios.getTUsuarios();
            if (TUsuarios != null) {
                TUsuarios.getTRecursosUsuariosCollection().remove(TRecursosUsuarios);
                TUsuarios = em.merge(TUsuarios);
            }
            TRecursos TRecursos = TRecursosUsuarios.getTRecursos();
            if (TRecursos != null) {
                TRecursos.getTRecursosUsuariosCollection().remove(TRecursosUsuarios);
                TRecursos = em.merge(TRecursos);
            }
            em.remove(TRecursosUsuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TRecursosUsuarios> findTRecursosUsuariosEntities() {
        return findTRecursosUsuariosEntities(true, -1, -1);
    }

    public List<TRecursosUsuarios> findTRecursosUsuariosEntities(int maxResults, int firstResult) {
        return findTRecursosUsuariosEntities(false, maxResults, firstResult);
    }

    private List<TRecursosUsuarios> findTRecursosUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TRecursosUsuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public TRecursosUsuarios findTRecursosUsuarios(TRecursosUsuariosPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TRecursosUsuarios.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<TRecursosUsuarios> findTRecursosUsuariosByUsuarios(String idUsuarios){        
        String query = "SELECT tru"
                    + " FROM TRecursosUsuarios tru"
                    + " WHERE tru.tUsuarios.idUsuarios like \"" + idUsuarios + "\""
                    + " ORDER BY tru.tRecursos.idRecursos";
        
        Query q = getEntityManager().createQuery(query);
         
        return q.getResultList();
    }

    public int getTRecursosUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TRecursosUsuarios> rt = cq.from(TRecursosUsuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

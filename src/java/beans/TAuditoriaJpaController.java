/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.TAuditoria;

/**
 *
 * @author Luana
 */
public class TAuditoriaJpaController implements Serializable {

    public TAuditoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TAuditoria TAuditoria) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(TAuditoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTAuditoria(TAuditoria.getIdAuditoria()) != null) {
                throw new PreexistingEntityException("TAuditoria " + TAuditoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TAuditoria TAuditoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TAuditoria = em.merge(TAuditoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = TAuditoria.getIdAuditoria();
                if (findTAuditoria(id) == null) {
                    throw new NonexistentEntityException("The tAuditoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TAuditoria TAuditoria;
            try {
                TAuditoria = em.getReference(TAuditoria.class, id);
                TAuditoria.getIdAuditoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TAuditoria with id " + id + " no longer exists.", enfe);
            }
            em.remove(TAuditoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TAuditoria> findTAuditoriaEntities() {
        return findTAuditoriaEntities(true, -1, -1);
    }

    public List<TAuditoria> findTAuditoriaEntities(int maxResults, int firstResult) {
        return findTAuditoriaEntities(false, maxResults, firstResult);
    }

    private List<TAuditoria> findTAuditoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TAuditoria.class));
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

    public TAuditoria findTAuditoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TAuditoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getTAuditoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TAuditoria> rt = cq.from(TAuditoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

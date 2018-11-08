/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import beans.exceptions.IllegalOrphanException;
import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.TRecursosUsuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.entities.TDuvidas;
import jpa.entities.TRecursos;

/**
 *
 * @author Luana
 */
public class TRecursosJpaController implements Serializable {

    public TRecursosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TRecursos TRecursos) throws PreexistingEntityException, Exception {
        if (TRecursos.getTDuvidasCollection() == null) {
            TRecursos.setTDuvidasCollection(new ArrayList<TDuvidas>());
        }
        if (TRecursos.getTRecursosUsuariosCollection() == null) {
            TRecursos.setTRecursosUsuariosCollection(new ArrayList<TRecursosUsuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TDuvidas> attachedTDuvidasCollection = new ArrayList<TDuvidas>();
            for (TDuvidas TDuvidasCollectionTDuvidasToAttach : TRecursos.getTDuvidasCollection()) {
                TDuvidasCollectionTDuvidasToAttach = em.getReference(TDuvidasCollectionTDuvidasToAttach.getClass(), TDuvidasCollectionTDuvidasToAttach.getIdDuvidas());
                attachedTDuvidasCollection.add(TDuvidasCollectionTDuvidasToAttach);
            }
            TRecursos.setTDuvidasCollection(attachedTDuvidasCollection);
            Collection<TRecursosUsuarios> attachedTRecursosUsuariosCollection = new ArrayList<TRecursosUsuarios>();
            for (TRecursosUsuarios TRecursosUsuariosCollectionTRecursosUsuariosToAttach : TRecursos.getTRecursosUsuariosCollection()) {
                TRecursosUsuariosCollectionTRecursosUsuariosToAttach = em.getReference(TRecursosUsuariosCollectionTRecursosUsuariosToAttach.getClass(), TRecursosUsuariosCollectionTRecursosUsuariosToAttach.getTRecursosUsuariosPK());
                attachedTRecursosUsuariosCollection.add(TRecursosUsuariosCollectionTRecursosUsuariosToAttach);
            }
            TRecursos.setTRecursosUsuariosCollection(attachedTRecursosUsuariosCollection);
            em.persist(TRecursos);
            for (TDuvidas TDuvidasCollectionTDuvidas : TRecursos.getTDuvidasCollection()) {
                TRecursos oldIdRecursoOfTDuvidasCollectionTDuvidas = TDuvidasCollectionTDuvidas.getIdRecurso();
                TDuvidasCollectionTDuvidas.setIdRecurso(TRecursos);
                TDuvidasCollectionTDuvidas = em.merge(TDuvidasCollectionTDuvidas);
                if (oldIdRecursoOfTDuvidasCollectionTDuvidas != null) {
                    oldIdRecursoOfTDuvidasCollectionTDuvidas.getTDuvidasCollection().remove(TDuvidasCollectionTDuvidas);
                    oldIdRecursoOfTDuvidasCollectionTDuvidas = em.merge(oldIdRecursoOfTDuvidasCollectionTDuvidas);
                }
            }
            for (TRecursosUsuarios TRecursosUsuariosCollectionTRecursosUsuarios : TRecursos.getTRecursosUsuariosCollection()) {
                TRecursos oldTRecursosOfTRecursosUsuariosCollectionTRecursosUsuarios = TRecursosUsuariosCollectionTRecursosUsuarios.getTRecursos();
                TRecursosUsuariosCollectionTRecursosUsuarios.setTRecursos(TRecursos);
                TRecursosUsuariosCollectionTRecursosUsuarios = em.merge(TRecursosUsuariosCollectionTRecursosUsuarios);
                if (oldTRecursosOfTRecursosUsuariosCollectionTRecursosUsuarios != null) {
                    oldTRecursosOfTRecursosUsuariosCollectionTRecursosUsuarios.getTRecursosUsuariosCollection().remove(TRecursosUsuariosCollectionTRecursosUsuarios);
                    oldTRecursosOfTRecursosUsuariosCollectionTRecursosUsuarios = em.merge(oldTRecursosOfTRecursosUsuariosCollectionTRecursosUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTRecursos(TRecursos.getIdRecursos()) != null) {
                throw new PreexistingEntityException("TRecursos " + TRecursos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TRecursos TRecursos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TRecursos persistentTRecursos = em.find(TRecursos.class, TRecursos.getIdRecursos());
            Collection<TDuvidas> TDuvidasCollectionOld = persistentTRecursos.getTDuvidasCollection();
            Collection<TDuvidas> TDuvidasCollectionNew = TRecursos.getTDuvidasCollection();
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionOld = persistentTRecursos.getTRecursosUsuariosCollection();
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionNew = TRecursos.getTRecursosUsuariosCollection();
            List<String> illegalOrphanMessages = null;
            for (TRecursosUsuarios TRecursosUsuariosCollectionOldTRecursosUsuarios : TRecursosUsuariosCollectionOld) {
                if (!TRecursosUsuariosCollectionNew.contains(TRecursosUsuariosCollectionOldTRecursosUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TRecursosUsuarios " + TRecursosUsuariosCollectionOldTRecursosUsuarios + " since its TRecursos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TDuvidas> attachedTDuvidasCollectionNew = new ArrayList<TDuvidas>();
            for (TDuvidas TDuvidasCollectionNewTDuvidasToAttach : TDuvidasCollectionNew) {
                TDuvidasCollectionNewTDuvidasToAttach = em.getReference(TDuvidasCollectionNewTDuvidasToAttach.getClass(), TDuvidasCollectionNewTDuvidasToAttach.getIdDuvidas());
                attachedTDuvidasCollectionNew.add(TDuvidasCollectionNewTDuvidasToAttach);
            }
            TDuvidasCollectionNew = attachedTDuvidasCollectionNew;
            TRecursos.setTDuvidasCollection(TDuvidasCollectionNew);
            Collection<TRecursosUsuarios> attachedTRecursosUsuariosCollectionNew = new ArrayList<TRecursosUsuarios>();
            for (TRecursosUsuarios TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach : TRecursosUsuariosCollectionNew) {
                TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach = em.getReference(TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach.getClass(), TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach.getTRecursosUsuariosPK());
                attachedTRecursosUsuariosCollectionNew.add(TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach);
            }
            TRecursosUsuariosCollectionNew = attachedTRecursosUsuariosCollectionNew;
            TRecursos.setTRecursosUsuariosCollection(TRecursosUsuariosCollectionNew);
            TRecursos = em.merge(TRecursos);
            for (TDuvidas TDuvidasCollectionOldTDuvidas : TDuvidasCollectionOld) {
                if (!TDuvidasCollectionNew.contains(TDuvidasCollectionOldTDuvidas)) {
                    TDuvidasCollectionOldTDuvidas.setIdRecurso(null);
                    TDuvidasCollectionOldTDuvidas = em.merge(TDuvidasCollectionOldTDuvidas);
                }
            }
            for (TDuvidas TDuvidasCollectionNewTDuvidas : TDuvidasCollectionNew) {
                if (!TDuvidasCollectionOld.contains(TDuvidasCollectionNewTDuvidas)) {
                    TRecursos oldIdRecursoOfTDuvidasCollectionNewTDuvidas = TDuvidasCollectionNewTDuvidas.getIdRecurso();
                    TDuvidasCollectionNewTDuvidas.setIdRecurso(TRecursos);
                    TDuvidasCollectionNewTDuvidas = em.merge(TDuvidasCollectionNewTDuvidas);
                    if (oldIdRecursoOfTDuvidasCollectionNewTDuvidas != null && !oldIdRecursoOfTDuvidasCollectionNewTDuvidas.equals(TRecursos)) {
                        oldIdRecursoOfTDuvidasCollectionNewTDuvidas.getTDuvidasCollection().remove(TDuvidasCollectionNewTDuvidas);
                        oldIdRecursoOfTDuvidasCollectionNewTDuvidas = em.merge(oldIdRecursoOfTDuvidasCollectionNewTDuvidas);
                    }
                }
            }
            for (TRecursosUsuarios TRecursosUsuariosCollectionNewTRecursosUsuarios : TRecursosUsuariosCollectionNew) {
                if (!TRecursosUsuariosCollectionOld.contains(TRecursosUsuariosCollectionNewTRecursosUsuarios)) {
                    TRecursos oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios = TRecursosUsuariosCollectionNewTRecursosUsuarios.getTRecursos();
                    TRecursosUsuariosCollectionNewTRecursosUsuarios.setTRecursos(TRecursos);
                    TRecursosUsuariosCollectionNewTRecursosUsuarios = em.merge(TRecursosUsuariosCollectionNewTRecursosUsuarios);
                    if (oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios != null && !oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios.equals(TRecursos)) {
                        oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios.getTRecursosUsuariosCollection().remove(TRecursosUsuariosCollectionNewTRecursosUsuarios);
                        oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios = em.merge(oldTRecursosOfTRecursosUsuariosCollectionNewTRecursosUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = TRecursos.getIdRecursos();
                if (findTRecursos(id) == null) {
                    throw new NonexistentEntityException("The tRecursos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TRecursos TRecursos;
            try {
                TRecursos = em.getReference(TRecursos.class, id);
                TRecursos.getIdRecursos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TRecursos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionOrphanCheck = TRecursos.getTRecursosUsuariosCollection();
            for (TRecursosUsuarios TRecursosUsuariosCollectionOrphanCheckTRecursosUsuarios : TRecursosUsuariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TRecursos (" + TRecursos + ") cannot be destroyed since the TRecursosUsuarios " + TRecursosUsuariosCollectionOrphanCheckTRecursosUsuarios + " in its TRecursosUsuariosCollection field has a non-nullable TRecursos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TDuvidas> TDuvidasCollection = TRecursos.getTDuvidasCollection();
            for (TDuvidas TDuvidasCollectionTDuvidas : TDuvidasCollection) {
                TDuvidasCollectionTDuvidas.setIdRecurso(null);
                TDuvidasCollectionTDuvidas = em.merge(TDuvidasCollectionTDuvidas);
            }
            em.remove(TRecursos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TRecursos> findTRecursosEntities() {
        return findTRecursosEntities(true, -1, -1);
    }

    public List<TRecursos> findTRecursosEntities(int maxResults, int firstResult) {
        return findTRecursosEntities(false, maxResults, firstResult);
    }

    private List<TRecursos> findTRecursosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TRecursos.class));
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

    public TRecursos findTRecursos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TRecursos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTRecursosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TRecursos> rt = cq.from(TRecursos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

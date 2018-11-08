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
import jpa.entities.TDuvidas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import jpa.entities.TRecursosUsuarios;
import jpa.entities.TUsuarios;
import jpa.entities.TUsuarios_;

/**
 *
 * @author Luana
 */
public class TUsuariosJpaController implements Serializable {

    public TUsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TUsuarios TUsuarios) throws PreexistingEntityException, Exception {
        if (TUsuarios.getTDuvidasCollection() == null) {
            TUsuarios.setTDuvidasCollection(new ArrayList<TDuvidas>());
        }
        if (TUsuarios.getTDuvidasCollection1() == null) {
            TUsuarios.setTDuvidasCollection1(new ArrayList<TDuvidas>());
        }
        if (TUsuarios.getTRecursosUsuariosCollection() == null) {
            TUsuarios.setTRecursosUsuariosCollection(new ArrayList<TRecursosUsuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TDuvidas> attachedTDuvidasCollection = new ArrayList<TDuvidas>();
            for (TDuvidas TDuvidasCollectionTDuvidasToAttach : TUsuarios.getTDuvidasCollection()) {
                TDuvidasCollectionTDuvidasToAttach = em.getReference(TDuvidasCollectionTDuvidasToAttach.getClass(), TDuvidasCollectionTDuvidasToAttach.getIdDuvidas());
                attachedTDuvidasCollection.add(TDuvidasCollectionTDuvidasToAttach);
            }
            TUsuarios.setTDuvidasCollection(attachedTDuvidasCollection);
            Collection<TDuvidas> attachedTDuvidasCollection1 = new ArrayList<TDuvidas>();
            for (TDuvidas TDuvidasCollection1TDuvidasToAttach : TUsuarios.getTDuvidasCollection1()) {
                TDuvidasCollection1TDuvidasToAttach = em.getReference(TDuvidasCollection1TDuvidasToAttach.getClass(), TDuvidasCollection1TDuvidasToAttach.getIdDuvidas());
                attachedTDuvidasCollection1.add(TDuvidasCollection1TDuvidasToAttach);
            }
            TUsuarios.setTDuvidasCollection1(attachedTDuvidasCollection1);
            Collection<TRecursosUsuarios> attachedTRecursosUsuariosCollection = new ArrayList<TRecursosUsuarios>();
            for (TRecursosUsuarios TRecursosUsuariosCollectionTRecursosUsuariosToAttach : TUsuarios.getTRecursosUsuariosCollection()) {
                TRecursosUsuariosCollectionTRecursosUsuariosToAttach = em.getReference(TRecursosUsuariosCollectionTRecursosUsuariosToAttach.getClass(), TRecursosUsuariosCollectionTRecursosUsuariosToAttach.getTRecursosUsuariosPK());
                attachedTRecursosUsuariosCollection.add(TRecursosUsuariosCollectionTRecursosUsuariosToAttach);
            }
            TUsuarios.setTRecursosUsuariosCollection(attachedTRecursosUsuariosCollection);
            em.persist(TUsuarios);
            for (TDuvidas TDuvidasCollectionTDuvidas : TUsuarios.getTDuvidasCollection()) {
                TUsuarios oldIdUsuariosROfTDuvidasCollectionTDuvidas = TDuvidasCollectionTDuvidas.getIdUsuariosR();
                TDuvidasCollectionTDuvidas.setIdUsuariosR(TUsuarios);
                TDuvidasCollectionTDuvidas = em.merge(TDuvidasCollectionTDuvidas);
                if (oldIdUsuariosROfTDuvidasCollectionTDuvidas != null) {
                    oldIdUsuariosROfTDuvidasCollectionTDuvidas.getTDuvidasCollection().remove(TDuvidasCollectionTDuvidas);
                    oldIdUsuariosROfTDuvidasCollectionTDuvidas = em.merge(oldIdUsuariosROfTDuvidasCollectionTDuvidas);
                }
            }
            for (TDuvidas TDuvidasCollection1TDuvidas : TUsuarios.getTDuvidasCollection1()) {
                TUsuarios oldIdUsuariosPOfTDuvidasCollection1TDuvidas = TDuvidasCollection1TDuvidas.getIdUsuariosP();
                TDuvidasCollection1TDuvidas.setIdUsuariosP(TUsuarios);
                TDuvidasCollection1TDuvidas = em.merge(TDuvidasCollection1TDuvidas);
                if (oldIdUsuariosPOfTDuvidasCollection1TDuvidas != null) {
                    oldIdUsuariosPOfTDuvidasCollection1TDuvidas.getTDuvidasCollection1().remove(TDuvidasCollection1TDuvidas);
                    oldIdUsuariosPOfTDuvidasCollection1TDuvidas = em.merge(oldIdUsuariosPOfTDuvidasCollection1TDuvidas);
                }
            }
            for (TRecursosUsuarios TRecursosUsuariosCollectionTRecursosUsuarios : TUsuarios.getTRecursosUsuariosCollection()) {
                TUsuarios oldTUsuariosOfTRecursosUsuariosCollectionTRecursosUsuarios = TRecursosUsuariosCollectionTRecursosUsuarios.getTUsuarios();
                TRecursosUsuariosCollectionTRecursosUsuarios.setTUsuarios(TUsuarios);
                TRecursosUsuariosCollectionTRecursosUsuarios = em.merge(TRecursosUsuariosCollectionTRecursosUsuarios);
                if (oldTUsuariosOfTRecursosUsuariosCollectionTRecursosUsuarios != null) {
                    oldTUsuariosOfTRecursosUsuariosCollectionTRecursosUsuarios.getTRecursosUsuariosCollection().remove(TRecursosUsuariosCollectionTRecursosUsuarios);
                    oldTUsuariosOfTRecursosUsuariosCollectionTRecursosUsuarios = em.merge(oldTUsuariosOfTRecursosUsuariosCollectionTRecursosUsuarios);
                }
            }
            List<TUsuarios> usuariosWithEmail = findTUsuariosByEmail(TUsuarios.getEmail()); 
            if(usuariosWithEmail != null && !usuariosWithEmail.isEmpty()){
                throw new PreexistingEntityException("O email informado já esta em uso.");
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTUsuarios(TUsuarios.getIdUsuarios()) != null) {
                throw new PreexistingEntityException("O usuário informado já existe.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TUsuarios TUsuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TUsuarios persistentTUsuarios = em.find(TUsuarios.class, TUsuarios.getIdUsuarios());
            Collection<TDuvidas> TDuvidasCollectionOld = persistentTUsuarios.getTDuvidasCollection();
            Collection<TDuvidas> TDuvidasCollectionNew = TUsuarios.getTDuvidasCollection();
            Collection<TDuvidas> TDuvidasCollection1Old = persistentTUsuarios.getTDuvidasCollection1();
            Collection<TDuvidas> TDuvidasCollection1New = TUsuarios.getTDuvidasCollection1();
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionOld = persistentTUsuarios.getTRecursosUsuariosCollection();
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionNew = TUsuarios.getTRecursosUsuariosCollection();
            List<String> illegalOrphanMessages = null;
            for (TDuvidas TDuvidasCollectionOldTDuvidas : TDuvidasCollectionOld) {
                if (!TDuvidasCollectionNew.contains(TDuvidasCollectionOldTDuvidas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TDuvidas " + TDuvidasCollectionOldTDuvidas + " since its idUsuariosR field is not nullable.");
                }
            }
            for (TDuvidas TDuvidasCollection1OldTDuvidas : TDuvidasCollection1Old) {
                if (!TDuvidasCollection1New.contains(TDuvidasCollection1OldTDuvidas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TDuvidas " + TDuvidasCollection1OldTDuvidas + " since its idUsuariosP field is not nullable.");
                }
            }
            for (TRecursosUsuarios TRecursosUsuariosCollectionOldTRecursosUsuarios : TRecursosUsuariosCollectionOld) {
                if (!TRecursosUsuariosCollectionNew.contains(TRecursosUsuariosCollectionOldTRecursosUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TRecursosUsuarios " + TRecursosUsuariosCollectionOldTRecursosUsuarios + " since its TUsuarios field is not nullable.");
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
            TUsuarios.setTDuvidasCollection(TDuvidasCollectionNew);
            Collection<TDuvidas> attachedTDuvidasCollection1New = new ArrayList<TDuvidas>();
            for (TDuvidas TDuvidasCollection1NewTDuvidasToAttach : TDuvidasCollection1New) {
                TDuvidasCollection1NewTDuvidasToAttach = em.getReference(TDuvidasCollection1NewTDuvidasToAttach.getClass(), TDuvidasCollection1NewTDuvidasToAttach.getIdDuvidas());
                attachedTDuvidasCollection1New.add(TDuvidasCollection1NewTDuvidasToAttach);
            }
            TDuvidasCollection1New = attachedTDuvidasCollection1New;
            TUsuarios.setTDuvidasCollection1(TDuvidasCollection1New);
            Collection<TRecursosUsuarios> attachedTRecursosUsuariosCollectionNew = new ArrayList<TRecursosUsuarios>();
            for (TRecursosUsuarios TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach : TRecursosUsuariosCollectionNew) {
                TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach = em.getReference(TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach.getClass(), TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach.getTRecursosUsuariosPK());
                attachedTRecursosUsuariosCollectionNew.add(TRecursosUsuariosCollectionNewTRecursosUsuariosToAttach);
            }
            TRecursosUsuariosCollectionNew = attachedTRecursosUsuariosCollectionNew;
            TUsuarios.setTRecursosUsuariosCollection(TRecursosUsuariosCollectionNew);
            TUsuarios = em.merge(TUsuarios);
            for (TDuvidas TDuvidasCollectionNewTDuvidas : TDuvidasCollectionNew) {
                if (!TDuvidasCollectionOld.contains(TDuvidasCollectionNewTDuvidas)) {
                    TUsuarios oldIdUsuariosROfTDuvidasCollectionNewTDuvidas = TDuvidasCollectionNewTDuvidas.getIdUsuariosR();
                    TDuvidasCollectionNewTDuvidas.setIdUsuariosR(TUsuarios);
                    TDuvidasCollectionNewTDuvidas = em.merge(TDuvidasCollectionNewTDuvidas);
                    if (oldIdUsuariosROfTDuvidasCollectionNewTDuvidas != null && !oldIdUsuariosROfTDuvidasCollectionNewTDuvidas.equals(TUsuarios)) {
                        oldIdUsuariosROfTDuvidasCollectionNewTDuvidas.getTDuvidasCollection().remove(TDuvidasCollectionNewTDuvidas);
                        oldIdUsuariosROfTDuvidasCollectionNewTDuvidas = em.merge(oldIdUsuariosROfTDuvidasCollectionNewTDuvidas);
                    }
                }
            }
            for (TDuvidas TDuvidasCollection1NewTDuvidas : TDuvidasCollection1New) {
                if (!TDuvidasCollection1Old.contains(TDuvidasCollection1NewTDuvidas)) {
                    TUsuarios oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas = TDuvidasCollection1NewTDuvidas.getIdUsuariosP();
                    TDuvidasCollection1NewTDuvidas.setIdUsuariosP(TUsuarios);
                    TDuvidasCollection1NewTDuvidas = em.merge(TDuvidasCollection1NewTDuvidas);
                    if (oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas != null && !oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas.equals(TUsuarios)) {
                        oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas.getTDuvidasCollection1().remove(TDuvidasCollection1NewTDuvidas);
                        oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas = em.merge(oldIdUsuariosPOfTDuvidasCollection1NewTDuvidas);
                    }
                }
            }
            for (TRecursosUsuarios TRecursosUsuariosCollectionNewTRecursosUsuarios : TRecursosUsuariosCollectionNew) {
                if (!TRecursosUsuariosCollectionOld.contains(TRecursosUsuariosCollectionNewTRecursosUsuarios)) {
                    TUsuarios oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios = TRecursosUsuariosCollectionNewTRecursosUsuarios.getTUsuarios();
                    TRecursosUsuariosCollectionNewTRecursosUsuarios.setTUsuarios(TUsuarios);
                    TRecursosUsuariosCollectionNewTRecursosUsuarios = em.merge(TRecursosUsuariosCollectionNewTRecursosUsuarios);
                    if (oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios != null && !oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios.equals(TUsuarios)) {
                        oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios.getTRecursosUsuariosCollection().remove(TRecursosUsuariosCollectionNewTRecursosUsuarios);
                        oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios = em.merge(oldTUsuariosOfTRecursosUsuariosCollectionNewTRecursosUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = TUsuarios.getIdUsuarios();
                if (findTUsuarios(id) == null) {
                    throw new NonexistentEntityException("O usuário com id " + id + " não foi encontrado. Contate o administrador.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TUsuarios TUsuarios;
            try {
                TUsuarios = em.getReference(TUsuarios.class, id);
                TUsuarios.getIdUsuarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TUsuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TDuvidas> TDuvidasCollectionOrphanCheck = TUsuarios.getTDuvidasCollection();
            for (TDuvidas TDuvidasCollectionOrphanCheckTDuvidas : TDuvidasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TUsuarios (" + TUsuarios + ") cannot be destroyed since the TDuvidas " + TDuvidasCollectionOrphanCheckTDuvidas + " in its TDuvidasCollection field has a non-nullable idUsuariosR field.");
            }
            Collection<TDuvidas> TDuvidasCollection1OrphanCheck = TUsuarios.getTDuvidasCollection1();
            for (TDuvidas TDuvidasCollection1OrphanCheckTDuvidas : TDuvidasCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TUsuarios (" + TUsuarios + ") cannot be destroyed since the TDuvidas " + TDuvidasCollection1OrphanCheckTDuvidas + " in its TDuvidasCollection1 field has a non-nullable idUsuariosP field.");
            }
            Collection<TRecursosUsuarios> TRecursosUsuariosCollectionOrphanCheck = TUsuarios.getTRecursosUsuariosCollection();
            for (TRecursosUsuarios TRecursosUsuariosCollectionOrphanCheckTRecursosUsuarios : TRecursosUsuariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TUsuarios (" + TUsuarios + ") cannot be destroyed since the TRecursosUsuarios " + TRecursosUsuariosCollectionOrphanCheckTRecursosUsuarios + " in its TRecursosUsuariosCollection field has a non-nullable TUsuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(TUsuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TUsuarios> findTUsuariosEntities() {
        return findTUsuariosEntities(true, -1, -1);
    }

    public List<TUsuarios> findTUsuariosEntities(int maxResults, int firstResult) {
        return findTUsuariosEntities(false, maxResults, firstResult);
    }
    
    public List<TUsuarios> findTUsuariosWithAptidaoNoRecurso(String idUsuarios, int idRecursos, String additionalWhere, boolean limit){        
       
        String query = "SELECT tu FROM TUsuarios tu WHERE EXISTS"
                + " (SELECT tru FROM TRecursosUsuarios tru"
                + " WHERE tu.idUsuarios = tru.tUsuarios.idUsuarios"
                + " AND tru.aptidao <> 0 and tru.tRecursos.idRecursos = " + idRecursos + ")"
                + " AND tu.idUsuarios <> \"" + idUsuarios + "\"";
        if(limit){            
            query+= " AND (SELECT COUNT(td.idDuvidas) FROM TDuvidas td"
                    + " WHERE tu.idUsuarios = td.idUsuariosR.idUsuarios AND (td.isQualificada = \"S\" OR td.isQualificada IS NULL)) < 6";
        }               
        
        if(additionalWhere != null && !"".equals(additionalWhere)){
            query += additionalWhere;
        }
        
        Query q = getEntityManager().createQuery(query);
         
        return q.getResultList();
    }
    
    public List<TUsuarios> findTUsuariosExcludeOne(String idUsuarios, String additionalWhere, boolean limit){        
        String query = "SELECT tu"
                    + " FROM TUsuarios tu"
                    + " WHERE tu.idUsuarios <> \"" + idUsuarios + "\"";
        if(limit){
            query+= " AND (SELECT COUNT(td.idDuvidas) FROM TDuvidas td"
                    + " WHERE tu.idUsuarios = td.idUsuariosR.idUsuarios AND (td.isQualificada = \"S\" OR td.isQualificada IS NULL)) < 6";
        }     
        if(additionalWhere != null && !"".equals(additionalWhere)){
            query += additionalWhere;
        }

        Query q = getEntityManager().createQuery(query);
         
        return q.getResultList();
    }
    
    private List<TUsuarios> findTUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TUsuarios.class));
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
    
    public List<TUsuarios> findTUsuariosByEmail(String email){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TUsuarios> cq = cb.createQuery(TUsuarios.class);
            
            Root<TUsuarios> f = cq.from(TUsuarios.class);
            
            cq.where(cb.like(f.get(TUsuarios_.email), "%" + email + "%"));
            return em.createQuery(cq).getResultList();

        } finally {
            em.close();
        }
    }

    public TUsuarios findTUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TUsuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getTUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TUsuarios> rt = cq.from(TUsuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.TDuvidas;
import jpa.entities.TDuvidas_;
import jpa.entities.TRecursos;
import jpa.entities.TUsuarios;

/**
 *
 * @author Luana
 */
public class TDuvidasJpaController implements Serializable {

    public TDuvidasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TDuvidas TDuvidas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TUsuarios idUsuariosR = TDuvidas.getIdUsuariosR();
            if (idUsuariosR != null) {
                idUsuariosR = em.getReference(idUsuariosR.getClass(), idUsuariosR.getIdUsuarios());
                TDuvidas.setIdUsuariosR(idUsuariosR);
            }
            TUsuarios idUsuariosP = TDuvidas.getIdUsuariosP();
            if (idUsuariosP != null) {
                idUsuariosP = em.getReference(idUsuariosP.getClass(), idUsuariosP.getIdUsuarios());
                TDuvidas.setIdUsuariosP(idUsuariosP);
            }
            TRecursos idRecurso = TDuvidas.getIdRecurso();
            if (idRecurso != null) {
                idRecurso = em.getReference(idRecurso.getClass(), idRecurso.getIdRecursos());
                TDuvidas.setIdRecurso(idRecurso);
            }
            em.persist(TDuvidas);
            if (idUsuariosR != null) {
                idUsuariosR.getTDuvidasCollection().add(TDuvidas);
                idUsuariosR = em.merge(idUsuariosR);
            }
            if (idUsuariosP != null) {
                idUsuariosP.getTDuvidasCollection().add(TDuvidas);
                idUsuariosP = em.merge(idUsuariosP);
            }
            if (idRecurso != null) {
                idRecurso.getTDuvidasCollection().add(TDuvidas);
                idRecurso = em.merge(idRecurso);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTDuvidas(TDuvidas.getIdDuvidas()) != null) {
                throw new PreexistingEntityException("TDuvidas " + TDuvidas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TDuvidas TDuvidas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TDuvidas persistentTDuvidas = em.find(TDuvidas.class, TDuvidas.getIdDuvidas());
            TUsuarios idUsuariosROld = persistentTDuvidas.getIdUsuariosR();
            TUsuarios idUsuariosRNew = TDuvidas.getIdUsuariosR();
            TUsuarios idUsuariosPOld = persistentTDuvidas.getIdUsuariosP();
            TUsuarios idUsuariosPNew = TDuvidas.getIdUsuariosP();
            TRecursos idRecursoOld = persistentTDuvidas.getIdRecurso();
            TRecursos idRecursoNew = TDuvidas.getIdRecurso();
            if (idUsuariosRNew != null) {
                idUsuariosRNew = em.getReference(idUsuariosRNew.getClass(), idUsuariosRNew.getIdUsuarios());
                TDuvidas.setIdUsuariosR(idUsuariosRNew);
            }
            if (idUsuariosPNew != null) {
                idUsuariosPNew = em.getReference(idUsuariosPNew.getClass(), idUsuariosPNew.getIdUsuarios());
                TDuvidas.setIdUsuariosP(idUsuariosPNew);
            }
            if (idRecursoNew != null) {
                idRecursoNew = em.getReference(idRecursoNew.getClass(), idRecursoNew.getIdRecursos());
                TDuvidas.setIdRecurso(idRecursoNew);
            }
            TDuvidas = em.merge(TDuvidas);
            if (idUsuariosROld != null && !idUsuariosROld.equals(idUsuariosRNew)) {
                idUsuariosROld.getTDuvidasCollection().remove(TDuvidas);
                idUsuariosROld = em.merge(idUsuariosROld);
            }
            if (idUsuariosRNew != null && !idUsuariosRNew.equals(idUsuariosROld)) {
                idUsuariosRNew.getTDuvidasCollection().add(TDuvidas);
                idUsuariosRNew = em.merge(idUsuariosRNew);
            }
            if (idUsuariosPOld != null && !idUsuariosPOld.equals(idUsuariosPNew)) {
                idUsuariosPOld.getTDuvidasCollection().remove(TDuvidas);
                idUsuariosPOld = em.merge(idUsuariosPOld);
            }
            if (idUsuariosPNew != null && !idUsuariosPNew.equals(idUsuariosPOld)) {
                idUsuariosPNew.getTDuvidasCollection().add(TDuvidas);
                idUsuariosPNew = em.merge(idUsuariosPNew);
            }
            if (idRecursoOld != null && !idRecursoOld.equals(idRecursoNew)) {
                idRecursoOld.getTDuvidasCollection().remove(TDuvidas);
                idRecursoOld = em.merge(idRecursoOld);
            }
            if (idRecursoNew != null && !idRecursoNew.equals(idRecursoOld)) {
                idRecursoNew.getTDuvidasCollection().add(TDuvidas);
                idRecursoNew = em.merge(idRecursoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = TDuvidas.getIdDuvidas();
                if (findTDuvidas(id) == null) {
                    throw new NonexistentEntityException("Nao foi possível encontrar a dúvida com o id " + id + ". Por favor, contate o administrador do sistema.");
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
            TDuvidas TDuvidas;
            try {
                TDuvidas = em.getReference(TDuvidas.class, id);
                TDuvidas.getIdDuvidas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TDuvidas with id " + id + " no longer exists.", enfe);
            }
            TUsuarios idUsuariosR = TDuvidas.getIdUsuariosR();
            if (idUsuariosR != null) {
                idUsuariosR.getTDuvidasCollection().remove(TDuvidas);
                idUsuariosR = em.merge(idUsuariosR);
            }
            TUsuarios idUsuariosP = TDuvidas.getIdUsuariosP();
            if (idUsuariosP != null) {
                idUsuariosP.getTDuvidasCollection().remove(TDuvidas);
                idUsuariosP = em.merge(idUsuariosP);
            }
            TRecursos idRecurso = TDuvidas.getIdRecurso();
            if (idRecurso != null) {
                idRecurso.getTDuvidasCollection().remove(TDuvidas);
                idRecurso = em.merge(idRecurso);
            }
            em.remove(TDuvidas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<TDuvidas> findTDuvidasEntities() {
        return findTDuvidasEntities(true, -1, -1);
    }

    public List<TDuvidas> findTDuvidasEntities(int maxResults, int firstResult) {
        return findTDuvidasEntities(false, maxResults, firstResult);
    }

    private List<TDuvidas> findTDuvidasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TDuvidas.class));
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

    public TDuvidas findTDuvidas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TDuvidas.class, id);
        } finally {
            em.close();
        }
    }
    
        public int countQualificações(String idUsuarios, String qual){        
        String query = "SELECT td"
                    + " FROM TDuvidas td"
                    + " WHERE td.resposta IS NOT NULL AND "
                + "td.idUsuariosR.idUsuarios like \"" + idUsuarios + "\" AND "
                + "td.isQualificada = \""
                + qual + "\" ";
               
        Query q = getEntityManager().createQuery(query);
         
        return q.getResultList().size(); 
    }
    
    public List<TDuvidas> findTDuvidasToUsuario(String idUsuarios){        
        String query = "SELECT td"
                    + " FROM TDuvidas td"
                    + " WHERE td.resposta IS NULL AND td.idUsuariosR.idUsuarios like \"" + idUsuarios + "\" ";
        
        Query q = getEntityManager().createQuery(query);
         
        return q.getResultList(); 
    }
    
    public List<TDuvidas> findTDuvidasFromUsuario(String idUsuarios){        
        String query = "SELECT td"
                    + " FROM TDuvidas td"
                    + " WHERE td.resposta IS NOT NULL AND td.isQualificada IS NULL AND td.idUsuariosP.idUsuarios like \"" + idUsuarios + "\"";
      
        Query q = getEntityManager().createQuery(query);
       
        return q.getResultList();
    }
    
        
    public List<TDuvidas> findAllTDuvidasQualificadas(boolean all, int maxResults, int firstResult){        
        String query = "SELECT td"
                    + " FROM TDuvidas td"
                    + " WHERE td.resposta IS NOT NULL AND td.isQualificada like \"S\"";
        
        Query q = getEntityManager().createQuery(query);        
                
        if (!all) {
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        }
        
        return q.getResultList();
    }

    public int getTDuvidasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TDuvidas> rt = cq.from(TDuvidas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public int getTDuvidasQualificadasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TDuvidas> rt = cq.from(TDuvidas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            cq.where(em.getCriteriaBuilder().like(rt.get(TDuvidas_.isQualificada), "%S%"));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

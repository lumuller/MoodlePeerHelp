package beans;

import jpa.entities.TDuvidas;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import beans.TDuvidasJpaController;
import beans.exceptions.IllegalOrphanException;
import beans.exceptions.NonexistentEntityException;
import beans.util.EmailSender;
import beans.util.FindUserResp;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;
import jpa.entities.TRecursos;
import jpa.entities.TUsuarios;
import org.apache.commons.mail.EmailException;

@ManagedBean(name = "tDuvidasController")
@SessionScoped
public class TDuvidasController implements Serializable {

    private TDuvidas current;
    private DataModel items = null;
    private TDuvidasJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Map<String, Object> recursosItem = null;
    private String qualificada;
    private int selectedIdRecurso;
    
    private int recursoFAQ;
    private int usuarioFAQ;

    public int getRecursoFAQ() {
        return recursoFAQ;
    }

    public void setRecursoFAQ(int recursoFAQ) {
        this.recursoFAQ = recursoFAQ;
    }

    public int getUsuarioFAQ() {
        return usuarioFAQ;
    }

    public void setUsuarioFAQ(int usuarioFAQ) {
        this.usuarioFAQ = usuarioFAQ;
    }    

    public TDuvidasController() {
        current = new TDuvidas();
    }
    
    public String getQualificada() {
        return qualificada;
    }

    public void setQualificada(String qualificada) {
        this.qualificada = qualificada;
    }

    public int getSelectedIdRecurso() {
        return selectedIdRecurso;
    }

    public void setSelectedIdRecurso(int selectedIdRecurso) {
        this.selectedIdRecurso = selectedIdRecurso;
    }

    public TDuvidas getSelected() {
        if (current == null) {
            current = new TDuvidas();           
        }
        return current;
    }

    private TDuvidasJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TDuvidasJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));                 
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getTDuvidasQualificadasCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findAllTDuvidasQualificadas(false, getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (TDuvidas) getItems().getRowData();
        selectedItemIndex = getPagination().getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TDuvidas();
        selectedItemIndex = -1;
        return "Create";
    }
    
   public DataModel getDuvidasAvailable(){
        items = new ListDataModel(getJpaController().findTDuvidasToUsuario(
                (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser")));
        return items;
    }
   
    public DataModel getRespostasAvailable(){
        items = new ListDataModel(getJpaController().findTDuvidasFromUsuario(
                (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser")));
        return items;
    }
    
    public DataModel getDuvidasQualificadas(){
        items = getPagination().createPageDataModel();
        return items;
    }

    public String create() {
        try {
            current.setIdDuvidas((getJpaController().getTDuvidasCount() + 1));
            
            TRecursosJpaController jpaControllerRecursos = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));        
            TRecursos recurso = jpaControllerRecursos.findTRecursos(getSelectedIdRecurso());
            
            current.setIdRecurso(recurso);
            
            TUsuariosJpaController jpaControllerUsuarios = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
            TUsuarios usuario = jpaControllerUsuarios.findTUsuarios((String)FacesContext.
                    getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
            current.setIdUsuariosP(usuario);            
            
            FindUserResp finduser = new FindUserResp(current);                        
            
            current.setIdUsuariosR(finduser.findUsuariosResposta());
            System.out.println("Usuário encontrado " + current.getIdUsuariosR());
            
            getJpaController().create(current);
            
            EmailSender email = new EmailSender();
            email.sendEmailDuvida(current.getIdUsuariosR().getNome(), current.getDuvida(), (current.getIdRecurso() != null ? current.getIdRecurso().getNome() : null), current.getIdUsuariosR().getEmail());
                        
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TDuvidasCreated"));
            
            current = new TDuvidas();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
            setSelectedIdRecurso(0);            
                        
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (TDuvidas) getItems().getRowData();
        selectedItemIndex = getPagination().getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String createFindOther() {
        try {
            current.setIdDuvidas((getJpaController().getTDuvidasCount() + 1));
            FindUserResp finduser = new FindUserResp(current);                                   
                        
            finduser.setFindOther(true);
            current.setIdUsuariosR(finduser.findUsuariosResposta());
            System.out.println("Usuário encontrado " + current.getIdUsuariosR());
            
            current.setResposta(null);
            current.setIsQualificada(null);
            
            getJpaController().create(current);
            
            EmailSender email = new EmailSender();
            email.sendEmailDuvida(current.getIdUsuariosR().getNome(), current.getDuvida(), (current.getIdRecurso() != null ? current.getIdRecurso().getNome() : null), current.getIdUsuariosR().getEmail());
                        
            JsfUtil.addSuccessMessage("Sua dúvida foi encaminhada com sucesso à um novo usuário");
            
            current = new TDuvidas();
    
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void updateFindOther() {
        try {
            
            FindUserResp findUserResp = new FindUserResp(current);
            findUserResp.setFindOther(true);
            current.setIdUsuariosR(findUserResp.findUsuariosResposta());
            current.setResposta(null);
            
            getJpaController().edit(current);
            EmailSender email = new EmailSender();
            email.sendEmailDuvida(current.getIdUsuariosR().getNome(), current.getDuvida(), (current.getIdRecurso() != null ? current.getIdRecurso().getNome() : null), current.getIdUsuariosR().getEmail());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TDuvidasUpdatedResposta"));    
            
            current = new TDuvidas();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public String updateResposta() {
        try {
            getJpaController().edit(current);
            EmailSender email = new EmailSender();
            email.sendEmailResposta(current.getIdUsuariosP().getNome(), current.getDuvida(), (current.getIdRecurso() != null ? current.getIdRecurso().getNome() : null), current.getIdUsuariosP().getEmail());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TDuvidasUpdatedResposta"));    
            
            current = new TDuvidas();
            
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updateQualificacao() {
        try {        
            
            updateNivelUsuarioR();
            getJpaController().edit(current); 
            

            if("N".equals(current.getIsQualificada())){
                createFindOther();
            } else {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TDuvidasUpdatedQualificacao"));
            }
            
            current = new TDuvidas();
            
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    private void updateNivelUsuarioR(){
        int qualPositivas = getJpaController().countQualificações(current.getIdUsuariosR().getIdUsuarios(), "S");
        int qualNegativas = getJpaController().countQualificações(current.getIdUsuariosR().getIdUsuarios(), "N");
        
        System.out.println("q. positivas: " + qualPositivas);
        System.out.println("q. negativas: " + qualNegativas);
        
        System.out.println("nivel: " + current.getIdUsuariosR().getNivel());
        
        int cat = (qualPositivas- qualNegativas) * current.getIdUsuariosR().getNivel();
        System.out.println("Categoria: " + cat);
        
        int nivelAdequado = 1 ;
        
        if(cat <=3){
            nivelAdequado = 1;
        } else if (cat >3 && cat <=6){
            nivelAdequado = 2;
        } else if (cat > 6){
            nivelAdequado = 3;
        }
        
        if(current.getIdUsuariosR().getNivel() != nivelAdequado){
            TUsuariosJpaController jpaUsuariosController = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));                 
            
            TUsuarios usuarioResposta = current.getIdUsuariosR();
            usuarioResposta.setNivel(nivelAdequado);
            
            try {
                jpaUsuariosController.edit(usuarioResposta);
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(TDuvidasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(TDuvidasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TDuvidasController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }       
    }

    public String destroy() {
        current = (TDuvidas) getItems().getRowData();
        selectedItemIndex = getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getIdDuvidas());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TDuvidasDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getTDuvidasCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findTDuvidasEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public Map<String, Object> getRecursosItens(){
            TRecursosJpaController jpaControllerRecursos = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));        
            recursosItem = new LinkedHashMap<String, Object>();
            recursosItem.put("--- Selecione um item ---", "0");
            for(Iterator<?> iter = jpaControllerRecursos.findTRecursosEntities().iterator(); iter.hasNext();){
                    TRecursos rec = (TRecursos) iter.next();
                    recursosItem.put(rec.getNome(), rec.getIdRecursos());
            }
            return recursosItem;            
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTDuvidasEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTDuvidasEntities(), true);
    }

    @FacesConverter(forClass = TDuvidas.class)
    public static class TDuvidasControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TDuvidasController controller = (TDuvidasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tDuvidasController");
            return controller.getJpaController().findTDuvidas(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TDuvidas) {
                TDuvidas o = (TDuvidas) object;
                return getStringKey(o.getIdDuvidas());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TDuvidas.class.getName());
            }
        }
    }
}

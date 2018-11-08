package beans;

import jpa.entities.TUsuarios;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import beans.TUsuariosJpaController;
import beans.exceptions.PreexistingEntityException;
import beans.util.EmailSender;

import java.io.Serializable;
import java.util.List;
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
import jpa.entities.TRecursosUsuarios;

@ManagedBean(name = "tUsuariosController")
@SessionScoped
public class TUsuariosController implements Serializable {

    private TUsuarios current;
    private DataModel items = null;
    private TUsuariosJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public TUsuariosController() {
    }

    public TUsuarios getSelected() {
        if (current == null) {
            current = new TUsuarios();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TUsuariosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getTUsuariosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findTUsuariosEntities(getPageSize(), getPageFirstItem()));
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
        current = (TUsuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TUsuarios();        
        selectedItemIndex = -1;
        return "Create";
    }
    
    public void addRecursosUsuarios(){
        TRecursosUsuariosJpaController recursosUsuariosController = new TRecursosUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
        TRecursosJpaController recursosController = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
        
        for(TRecursos r : recursosController.findTRecursosEntities()){
            TRecursosUsuarios recursosUsuarios = new TRecursosUsuarios();
            recursosUsuarios.setAptidao(0);
            recursosUsuarios.setTRecursos(r);
            recursosUsuarios.setTUsuarios(current);
            
            try {
                recursosUsuariosController.create(recursosUsuarios);
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(TUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

    public boolean validarEmail(String email) {  
        int parte1 = email.indexOf("@");  
        int parte2 = email.indexOf(".", parte1);  
        int parte3 = email.length();  
        if (!(parte1 >= 1 && parte2 >= 5 && parte3 >= 8 && parte1+1<parte2)) {  
            return false;  
        }  
        return true;  
  
    } 
  
    public String create() {
        try {
            current.setNivel(1);
            
            if(!validarEmail(current.getEmail())){
                JsfUtil.addErrorMessage("Você deve informar um email válido.");
                return null;
            }
            
            getJpaController().create(current);
            
            EmailSender email = new EmailSender();
            email.sendEmailBoasVindas(current.getNome(), current.getIdUsuarios(), current.getSenha(), current.getEmail());
            
            addRecursosUsuarios();
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TUsuariosCreated"));           
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", current.getIdUsuarios());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserName", current.getNome());
            
            return prepareCreate();
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (TUsuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TUsuariosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (TUsuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
            getJpaController().destroy(current.getIdUsuarios());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TUsuariosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getTUsuariosCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findTUsuariosEntities(1, selectedItemIndex).get(0);
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

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTUsuariosEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTUsuariosEntities(), true);
    }

    public List<TUsuarios> getAllUsersList(){
        return getJpaController().findTUsuariosEntities();
    }
    
    @FacesConverter(forClass = TUsuarios.class)
    public static class TUsuariosControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TUsuariosController controller = (TUsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tUsuariosController");
            return controller.getJpaController().findTUsuarios(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TUsuarios) {
                TUsuarios o = (TUsuarios) object;
                return getStringKey(o.getIdUsuarios());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TUsuarios.class.getName());
            }
        }
    }
}

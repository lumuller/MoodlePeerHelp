package beans;

import jpa.entities.TRecursosUsuarios;
import jpa.entities.TRecursos;
import beans.util.JsfUtil;
import beans.util.PaginationHelper;
import beans.TRecursosUsuariosJpaController;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
import jpa.entities.TUsuarios;

@ManagedBean(name = "tRecursosUsuariosController")
@SessionScoped
public class TRecursosUsuariosController implements Serializable {

    private TRecursosUsuarios current;
    private DataModel items = null;
    private TRecursosUsuariosJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Map<String, Object> recursosItem = null;
    
    private int selectedIdRecurso;

    public int getSelectedIdRecurso() {
        return selectedIdRecurso;
    }

    public void setSelectedIdRecurso(int selectedIdRecurso) {
        this.selectedIdRecurso = selectedIdRecurso;
    }    

    public TRecursosUsuariosController() {
        
    }  
    
    public Map<String, Object> getRecursosItens(){
            TRecursosJpaController jpaControllerRecursos = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));        
            recursosItem = new LinkedHashMap<String, Object>();
            recursosItem.put("--- Selecione um item ---", null);
            for(Iterator<?> iter = jpaControllerRecursos.findTRecursosEntities().iterator(); iter.hasNext();){
                    TRecursos rec = (TRecursos) iter.next();
                    recursosItem.put(rec.getNome(), rec.getIdRecursos());
            }
            return recursosItem;            
    }


    public TRecursosUsuarios getSelected() {
        if (current == null) {
            current = new TRecursosUsuarios();
            current.setTRecursosUsuariosPK(new jpa.entities.TRecursosUsuariosPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private TRecursosUsuariosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TRecursosUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(getJpaController().getTRecursosUsuariosCount()) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getTRecursosUsuariosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findTRecursosUsuariosEntities(getPageSize(), getPageFirstItem()));
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
        current = (TRecursosUsuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TRecursosUsuarios();
        current.setTRecursosUsuariosPK(new jpa.entities.TRecursosUsuariosPK());
        
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String destroyLine() {
        try{
            TRecursosUsuarios tru = getDataForEditOrDelete();
            jpaController.destroy(tru.getTRecursosUsuariosPK());	
            current = new TRecursosUsuarios();                
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TRecursosUsuariosDeleted"));
            recreateModel();
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return "Create";
        }
    }
    
    public String updateLine() {
       current = getDataForEditOrDelete();  
       
       return "Create";
    }
    
    public String create() {
        try {
            TUsuariosJpaController jpaControllerUsuarios = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
            TUsuarios usuario = jpaControllerUsuarios.findTUsuarios((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
            
            TRecursosJpaController jpaControllerRecursos = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));        
            TRecursos recurso = jpaControllerRecursos.findTRecursos(getSelectedIdRecurso());
            
            current.setTUsuarios(usuario);
            current.setTRecursos(recurso);
            
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TRecursosUsuariosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public TRecursosUsuarios getDataForEditOrDelete() {
            current = (TRecursosUsuarios) items.getRowData();
            return current;		
    }

    public String prepareEdit() {
        current = (TRecursosUsuarios) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            
            TRecursosJpaController jpaControllerRecursos = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));        
            TRecursos recurso = jpaControllerRecursos.findTRecursos(getSelectedIdRecurso());

            current.setTRecursos(recurso);
            
            TUsuariosJpaController jpaControllerUsuarios = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
            TUsuarios usuario = jpaControllerUsuarios.findTUsuarios((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
            
            current.setTUsuarios(usuario);
            
            getJpaController().edit(current);
            recreateModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TRecursosUsuariosUpdated"));
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (TRecursosUsuarios) getItems().getRowData();
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
            getJpaController().destroy(current.getTRecursosUsuariosPK());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TRecursosUsuariosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getTRecursosUsuariosCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findTRecursosUsuariosEntities(1, selectedItemIndex).get(0);
        }
    }
    
    public DataModel getItemsAvailable(){
        items = new ListDataModel(getJpaController().findTRecursosUsuariosByUsuarios(
                (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser")));
        return items;
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
        return JsfUtil.getSelectItems(getJpaController().findTRecursosUsuariosEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTRecursosUsuariosEntities(), true);        
    }

    @FacesConverter(forClass = TRecursosUsuarios.class)
    public static class TRecursosUsuariosControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TRecursosUsuariosController controller = (TRecursosUsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tRecursosUsuariosController");
            return controller.getJpaController().findTRecursosUsuarios(getKey(value));
        }

        jpa.entities.TRecursosUsuariosPK getKey(String value) {
            jpa.entities.TRecursosUsuariosPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.TRecursosUsuariosPK();
            key.setIdRecursos(Integer.parseInt(values[0]));
            key.setIdUsuarios(values[1]);
            return key;
        }

        String getStringKey(jpa.entities.TRecursosUsuariosPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getIdRecursos());
            sb.append(SEPARATOR);
            sb.append(value.getIdUsuarios());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TRecursosUsuarios) {
                TRecursosUsuarios o = (TRecursosUsuarios) object;
                return getStringKey(o.getTRecursosUsuariosPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TRecursosUsuarios.class.getName());
            }
        }
    }
}

package beans;


import beans.TUsuariosJpaController;
import beans.exceptions.NonexistentEntityException;
import beans.util.JsfUtil;
import java.util.ResourceBundle;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import jpa.entities.TUsuarios;

public class LoginBean {
  
  private String name;
  private String senha;
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getSenha() { return senha; }
  public void setSenha(String senha) { this.senha = senha; }
  
  public boolean isLoggedIn() {
    return FacesContext.getCurrentInstance().getExternalContext()
        .getSessionMap().get("currentUser") != null;
  }
  
  public String getLoggedUserName(){
      if(isLoggedIn()){
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserName");
      }
      return null;
  }

  public String login() {     
    
    System.out.println("Esta logando!");
      
    TUsuariosJpaController jpaController = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    TUsuarios usuario = jpaController.findTUsuarios(name);
    if(usuario == null){       
        Exception e = new Exception("O usuário informado " + name +  " não existe");
        JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        return null;
    } else {
        if(usuario.getSenha().equals(senha)){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", name);  
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserName", usuario.getNome());
        } else {
            Exception e = new Exception("O usuário e senha informados não correspondem");
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    return "OK";
  }

  public String logout() {
    FacesContext context = FacesContext.getCurrentInstance();    
    ExternalContext externalContext = context.getExternalContext();
    externalContext.getSessionMap().remove("currentUser");
    Object session = externalContext.getSession(false);
    HttpSession httpSession = (HttpSession) session;
    httpSession.invalidate();
    
    return "loginPage";
  }
}

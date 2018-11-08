/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.util;

import beans.TAuditoriaJpaController;
import beans.TDuvidasJpaController;
import beans.TRecursosJpaController;
import beans.TRecursosUsuariosJpaController;
import beans.TUsuariosJpaController;
import beans.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import jpa.entities.TAuditoria;
import jpa.entities.TDuvidas;
import jpa.entities.TRecursosUsuarios;
import jpa.entities.TUsuarios;

/**
 *
 * @author luana.muller
 */
public class FindUserResp {
    
    private List<TUsuarios> similares = new ArrayList<TUsuarios>();
    private List<TUsuarios> usuarios = new ArrayList<TUsuarios>();
    TDuvidas currentDuvida = null;
    private boolean findOther = false;
    GeradorLog gerador = GeradorLog.getInstance();

    public boolean isFindOther() {
        return findOther;
    }

    public void setFindOther(boolean findOther) {
        this.findOther = findOther;
    }   
    
    private TDuvidas getCurrentDuvida() {
        return currentDuvida;
    }

    private void setCurrentDuvida(TDuvidas currentDuvida) {
        this.currentDuvida = currentDuvida;
    }
    
    public FindUserResp(TDuvidas currentDuvida){
        this.currentDuvida = currentDuvida;
    }
    
    TUsuariosJpaController jpaUsuariosController = new TUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    TRecursosJpaController jpaRecursosController = new TRecursosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    TDuvidasJpaController jpaDuvidasController = new TDuvidasJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    TRecursosUsuariosJpaController jpaRecursosUsuariosController = new TRecursosUsuariosJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    TAuditoriaJpaController jpaAuditoriaController = new TAuditoriaJpaController(Persistence.createEntityManagerFactory("MoodlePeerHelpPU"));
    
    StringBuilder caminhoescolha = new StringBuilder("");
    
    public TUsuarios findUsuariosResposta(){
        
        TUsuarios foundUser = null;
        
        prepareData();    
        //se encontrou apenas 1 usuário similar
        if(similares.size() == 1){
            caminhoescolha.append("1");
            //retornou um usuário similar"; 
            foundUser = similares.iterator().next();            
        } else {
            if (similares.size() > 1) {
                caminhoescolha.append("2");
                //procurando no nivel dentre os similares 
                foundUser = getUsuariosNivelTempo(similares);                
            } else {
               caminhoescolha.append("3");
               //procurando no nivel dentre todos
               foundUser = getUsuariosNivelTempo(usuarios);
            }
        }  
        
        
        int maxAuditoria = jpaAuditoriaController.getTAuditoriaCount();
        TAuditoria auditoria = new TAuditoria();
        
        auditoria.setIdAuditoria(maxAuditoria);
        auditoria.setIdDuvidas(currentDuvida.getIdDuvidas());
        auditoria.setNivelEscolha(caminhoescolha.toString());
        auditoria.setFindOther(findOther ? "S" : "N");
        
        try {
            gerador.log(Level.INFO, "Auditoria criada com sucesso");
            jpaAuditoriaController.create(auditoria);
        } catch (PreexistingEntityException ex) {
            gerador.log(Level.SEVERE, "Dado preenxistente na tabela autidoria");            
        } catch (Exception ex) {
            gerador.log(Level.SEVERE, "Erro ao criar log");
        }
        
        return foundUser;
    }
    
    public TUsuarios getUsuariosNivelTempo(List<TUsuarios> us){
        
        List<TUsuarios> usuariosNivel = new ArrayList<TUsuarios>();
        int nivelUsuarioPergunta = getCurrentDuvida().getIdUsuariosP().getNivel();
            
        for(TUsuarios tu : us){
            if(tu.getNivel() == nivelUsuarioPergunta){
                usuariosNivel.add(tu);
            }                  
        }

        if (usuariosNivel.size() == 1){
            //retornou um usuário no mesmo nível 
            caminhoescolha.append("4");
            return usuariosNivel.iterator().next();
        } else if(usuariosNivel.size() > 1){
            //procurando o mais velho no mesmo nível 
            caminhoescolha.append("5");
            return selectOlderUser(usuariosNivel);
        } else {
            for(TUsuarios tu : us){
                if(tu.getNivel()  > nivelUsuarioPergunta){
                    usuariosNivel.add(tu);
                }                  
            }

            if(usuariosNivel.size() == 1){
                //retornou 1 em nível superior 
                caminhoescolha.append("6");
                return usuariosNivel.iterator().next();
            } else if (usuariosNivel.size() > 1) {
                //procurando o mais velho do nível superior 
                caminhoescolha.append("7");
                return selectOlderUser(usuariosNivel);
            } else {
                //procurando o mais velho de todos
                caminhoescolha.append("8");
                return selectOlderUser(us);
            }                   
        }   
    }
    
    public TUsuarios selectOlderUser(List<TUsuarios> us){
        TUsuarios olderUser = null;
        Collections.shuffle(us);
        for(TUsuarios tu : us){
            if(olderUser == null){
                olderUser = tu;
            } else {
                if (tu.getTempoUsoMoodle() > olderUser.getTempoUsoMoodle()) {
                    olderUser = tu;
                }                    
            }
        }
        //retornando o mais velho
        caminhoescolha.append("9");
        return olderUser;
    }
    
    public void prepareData(){     
        
        if(!findOther){        
            if(getCurrentDuvida().getIdRecurso() != null){
                usuarios = jpaUsuariosController.findTUsuariosWithAptidaoNoRecurso(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), getCurrentDuvida().getIdRecurso().getIdRecursos(), null, true);
                if(usuarios.isEmpty()){
                    usuarios = jpaUsuariosController.findTUsuariosWithAptidaoNoRecurso(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), getCurrentDuvida().getIdRecurso().getIdRecursos(), null, false);
                    if(usuarios.isEmpty()){
                        usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), null, true);
                        if(usuarios.isEmpty()){
                            usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), null, false);
                        }
                    }
                }
            } else {
                usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(),null, true);                      
                if(usuarios.isEmpty()){
                    usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(),null, false);                      
                }
            }        
        } else {
            String additionalWhere = " AND tu.idUsuarios <> \"" + getCurrentDuvida().getIdUsuariosR().getIdUsuarios() + "\"";
            if(getCurrentDuvida().getIdRecurso() != null){
                usuarios = jpaUsuariosController.findTUsuariosWithAptidaoNoRecurso(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), getCurrentDuvida().getIdRecurso().getIdRecursos(), additionalWhere, true);
                if(usuarios.isEmpty()){
                    usuarios = jpaUsuariosController.findTUsuariosWithAptidaoNoRecurso(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), getCurrentDuvida().getIdRecurso().getIdRecursos(), additionalWhere, false);
                    if(usuarios.isEmpty()){
                        usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), additionalWhere, true);
                        if(usuarios.isEmpty()){
                            usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(), additionalWhere, false);
                        }
                    }
                }
            } else {
                usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(),additionalWhere, true);                      
                if(usuarios.isEmpty()){
                    usuarios = jpaUsuariosController.findTUsuariosExcludeOne(getCurrentDuvida().getIdUsuariosP().getIdUsuarios(),additionalWhere, false);                      
                }
            }    
        }            
       
        //retorna a lista de recursos do usuário que fez a pergunta
        List<TRecursosUsuarios> recursosUsuariosPerguntaList = new ArrayList<TRecursosUsuarios>();
        recursosUsuariosPerguntaList = jpaRecursosUsuariosController.findTRecursosUsuariosByUsuarios(getCurrentDuvida().getIdUsuariosP().getIdUsuarios());               
        
        Map<Integer, Integer> aptidoesUsuarioPergunta = new HashMap<Integer, Integer>();
        
        //salvando todas as aptidões
        for(TRecursosUsuarios tru : recursosUsuariosPerguntaList){            
            if(tru.getAptidao()!=0){
                aptidoesUsuarioPergunta.put(tru.getTRecursos().getIdRecursos(), tru.getAptidao());
            }
        }        
        
        for(TUsuarios u : usuarios){
            List<Scores> aptidoesComum = new ArrayList<Scores>();
            
            //se o usuário não for o usuário da pergunta
            List<TRecursosUsuarios> recursosUsuariosRespList = jpaRecursosUsuariosController.findTRecursosUsuariosByUsuarios(u.getIdUsuarios());               

            for(TRecursosUsuarios tru : recursosUsuariosRespList){
                if(tru.getAptidao()!=0){
                    if(aptidoesUsuarioPergunta.containsKey(tru.getTRecursos().getIdRecursos())){
                        Integer aptidaoUserPerguntaNesteRecurso = aptidoesUsuarioPergunta.get(tru.getTRecursos().getIdRecursos());
                        aptidoesComum.add(new Scores(tru.getAptidao(), aptidaoUserPerguntaNesteRecurso));
                    }
                }
            } 

            if(aptidoesComum.size() > 2){
                double [] scores1 = new double[aptidoesComum.size()];
                double [] scores2 = new double[aptidoesComum.size()];

                for(int i=0; i<aptidoesComum.size(); i++){
                    scores1[i] = aptidoesComum.get(i).getScore1();
                    scores2[i] = aptidoesComum.get(i).getScore2();
                }

                if(getPearsonCorrelation(scores1, scores2) > 0){
                    similares.add(u);
                    System.out.println(u.getIdUsuarios());
                }
            }               
        }                      
    }
    
    public double getPearsonCorrelation(double[] scores1,double[] scores2){

        double result = 0;
        double sum_sq_x = 0;
        double sum_sq_y = 0;
        double sum_coproduct = 0;
        double mean_x = scores1[0];
        double mean_y = scores2[0];

        for(int i=2;i<scores1.length+1;i+=1){
            double sweep =Double.valueOf(i-1)/i;
            double delta_x = scores1[i-1]-mean_x;
            double delta_y = scores2[i-1]-mean_y;

            sum_sq_x += delta_x * delta_x * sweep;
            sum_sq_y += delta_y * delta_y * sweep;
            sum_coproduct += delta_x * delta_y * sweep;
            mean_x += delta_x / i;
            mean_y += delta_y / i;

        }

        double pop_sd_x = (double) Math.sqrt(sum_sq_x/scores1.length);
        double pop_sd_y = (double) Math.sqrt(sum_sq_y/scores1.length);
        double cov_x_y = sum_coproduct / scores1.length;

        result = cov_x_y / (pop_sd_x*pop_sd_y);

        return result;

    }
    
    private class Scores{
        
        Integer score1;
        Integer score2;

        public Scores(Integer score1, Integer score2){
            this.score1 = score1;
            this.score2 = score2;            
        }
        
        public Integer getScore1() {
            return score1;
        }

        public void setScore1(Integer score1) {
            this.score1 = score1;
        }

        public Integer getScore2() {
            return score2;
        }

        public void setScore2(Integer score2) {
            this.score2 = score2;
        }      
    }
}

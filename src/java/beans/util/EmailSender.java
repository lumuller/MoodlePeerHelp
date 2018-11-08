/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author Luana
 */
public class EmailSender {
    
        public void sendEmailBoasVindas(String nome, String id, String senha, String emailUser) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");    
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td colspan=\"2\">Olá ");
        sb.append(nome);            
        sb.append(". Seja bem vindo(a) ao Moodle Peer Help.</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>Nome de usuário:</td>");
        sb.append("<td>");
        sb.append(id);
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>Senha:</td>");
        sb.append("<td>");
        sb.append(senha);
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td colspan=\"2\"><a href=\"http://moodlepeerhelp.no-ip.org:8080/MoodlePeerHelp/index.xhtml\">Acesse o Moodle Peer Help.</a></td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");    
        sb.append("</html>");

        try{
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.addTo(emailUser, nome);
            email.setFrom("moodlepeerhelp@gmail.com", "Moodle Peer Help");
            email.setSubject("Seja bem vindo(a) ao Moodle Peer Help");
            email.setMsg(sb.toString());
            email.setSSL(true);
            email.setAuthentication("moodlepeerhelp", "moodlepeerhelp14luana311088!");
            //email.send();
        }catch(Exception e){
           System.out.println("Não foi possível enviar o email. Exception " + e);
       }

    }    
   
    public void sendEmailDuvida(String nome, String duvida, String recurso, String emailUser) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");    
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td colspan=\"2\">Olá ");
        sb.append(nome);            
        sb.append(". Você tem uma dúvida para responder.</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>Dúvida:</td>");
        sb.append("<td>");
        sb.append(duvida);
        sb.append("</td>");
        sb.append("</tr>");
        if(recurso != null){
            sb.append("<tr>");
            sb.append("<td>Recurso/Atividade relacionado:</td>");
            sb.append("<td>");
            sb.append(recurso);
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("<tr>");
        sb.append("<td colspan=\"2\"><a href=\"http://moodlepeerhelp.no-ip.org:8080/MoodlePeerHelp/index.xhtml\">Acesse o Moodle Peer Help.</a></td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");    
        sb.append("</html>");

        try{
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.addTo(emailUser, nome);
            email.setFrom("moodlepeerhelp@gmail.com", "Moodle Peer Help");
            email.setSubject("Nova dúvida aguardando ser respondida");
            email.setMsg(sb.toString());
            email.setSSL(true);
            email.setAuthentication("moodlepeerhelp", "moodlepeerhelp14luana311088!");
            //email.send();
        }catch(Exception e){
           System.out.println("Não foi possível enviar o email. Exception " + e);
       }

    }
    
     public void sendEmailResposta(String nome, String duvida, String recurso, String emailUser) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");    
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td colspan=\"2\">Olá ");
        sb.append(nome);            
        sb.append(". Sua dúvida foi respondida.</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td>Dúvida:</td>");
        sb.append("<td>");
        sb.append(duvida);
        sb.append("</td>");
        sb.append("</tr>");
        if(recurso != null){
            sb.append("<tr>");
            sb.append("<td>Recurso/Atividade relacionado:</td>");
            sb.append("<td>");
            sb.append(recurso);
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("<tr>");
        sb.append("<td colspan=\"2\"><a href=\"http://moodlepeerhelp.no-ip.org:8080/MoodlePeerHelp/index.xhtml\">Acesse o Moodle Peer Help.</a></td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");    
        sb.append("</html>");

        try{
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.addTo(emailUser, nome);
            email.setFrom("moodlepeerhelp@gmail.com", "Moodle Peer Help");
            email.setSubject("Dúvida respondida");
            email.setMsg(sb.toString());
            email.setSSL(true);
            email.setAuthentication("moodlepeerhelp", "moodlepeerhelp14luana311088!");
            //email.send();
        }catch(Exception e){
           System.out.println("Não foi possível enviar o email. Exception " + e);
       }

    }

}

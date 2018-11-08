/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.util;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 *
 * @author Luana
 */
public class GeradorLog {
    private static GeradorLog instancia = null;
    private String nomeArquivo = "C:\\log.txt";
    private Level level = null;
    private Logger logger = null;
    FileHandler fh = null;
    private GeradorLog(){
      if (level == null)
        level = Level.ALL;

      logger = Logger.getLogger("GeradorLog");
      try {
        fh = new FileHandler(nomeArquivo,true);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.setUseParentHandlers(true);
        logger.addHandler(fh);
        logger.setLevel(level);
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    public void setLevel(Level level){
      instancia.logger.setLevel(level);
    }
    public synchronized static GeradorLog getInstance(){
      if (instancia == null){
        instancia = new GeradorLog();
      }
      return instancia;
    }
    public void log(Level level, String mensagem){
      logger.log(level, mensagem);
    }
}

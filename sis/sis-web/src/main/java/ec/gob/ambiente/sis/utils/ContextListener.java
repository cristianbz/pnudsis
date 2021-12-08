/**
@autor proamazonia [Christian Báez]  2 dic. 2021

**/
package ec.gob.ambiente.sis.utils;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ec.gob.ambiente.sis.services.GenerarDatosSitioPublico;

public class ContextListener implements ServletContextListener {

	@EJB
	private GenerarDatosSitioPublico generaDatos;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();				
		GenerarDatosSitioPublico.webPath = context.getRealPath(generaDatos.FOLDER_NAME);
		generaDatos.generarResumen();
	}

}


/**
@autor proamazonia [Christian Báez]  22 oct. 2021

**/
package ec.gob.ambiente.tests;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.Test;

import ec.gob.ambiente.sigma.services.CatalogFacade;

public class CatalogFacadeTest {

	@Test
	public void test() throws Exception{
		 Properties p = new Properties();
	        p.put("movieDatabase", "new://Resource?type=DataSource");
	        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
	        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");	
	        Context context = EJBContainer.createEJBContainer(p).getContext();
	        
		CatalogFacade bean = (CatalogFacade) context.lookup("java:global/classes/CatalogFacade");
		int res= bean.listaLineasAccion().size();
		assertEquals(16, res);
		
	}

}


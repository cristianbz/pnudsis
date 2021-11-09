/**
@autor proamazonia [Christian Báez]  22 oct. 2021

**/
package ec.gob.ambiente.tests;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ec.gob.ambiente.sigma.model.Catalog;
import ec.gob.ambiente.sigma.services.CatalogFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;

@RunWith(Arquillian.class)
public class CatalogFacadeTest {

	@EJB
	private CatalogFacade catalogEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"CatalogFacadeTest.war")
				.addClass(CatalogFacade.class)
				.addClass(AbstractFacade.class)
				.addClass(Catalog.class)
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
				
	}
	
	@Test
	public void testCatalog(){
		try{
			System.out.println("Funcionado");
			Assert.assertTrue(catalogEjb.listaLineasAccion()!=null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void test() throws Exception{
//		 Properties p = new Properties();
//	        p.put("movieDatabase", "new://Resource?type=DataSource");
//	        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
//	        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");	
//	        Context context = EJBContainer.createEJBContainer(p).getContext();
//	        
//		CatalogFacade bean = (CatalogFacade) context.lookup("java:global/classes/CatalogFacade");
//		int res= bean.listaLineasAccion().size();
//		assertEquals(16, res);
//		
//	}

}


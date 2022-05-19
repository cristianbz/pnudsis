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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ec.gob.ambiente.sigma.model.Catalog;
import ec.gob.ambiente.sigma.model.CatalogType;
import ec.gob.ambiente.sigma.services.CatalogFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class CatalogFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogFacadeTest.class);
	
	@EJB
	private CatalogFacade catalogEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"CatalogFacadeTest.war")
				.addClass(CatalogFacade.class)
				.addClass(AbstractFacade.class)				
				.addClass(Catalog.class)
				.addClass(CatalogType.class)
				.addClass(ProjectsGenderInfo.class)
				.addPackage(Catalog.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
				
	}
	
	@Test
	public void testCatalog(){
		try{			
			Assert.assertTrue(catalogEjb.listaLineasAccion() != null);
			LOGGER.info("CLASE: CatalogFacade");
			LOGGER.info("METODO: List<Catalog> listaLineasAccion()");			
			LOGGER.info("RETORNA: List<Catalog>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}


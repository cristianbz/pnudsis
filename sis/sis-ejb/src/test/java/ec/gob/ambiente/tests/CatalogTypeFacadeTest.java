/**
@autor proamazonia [Christian Báez]  15 nov. 2021

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
import ec.gob.ambiente.sigma.services.CatalogTypeFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class CatalogTypeFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogTypeFacadeTest.class);
	@EJB
	private CatalogTypeFacade catalogTypeEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"CatalogTypeFacadeTest.war")
				.addClass(CatalogTypeFacade.class)
				.addClass(CatalogType.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Catalog.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testCatalogType(){
		try{			
			Assert.assertTrue(catalogTypeEjb.listaLineasGenero() != null);
			LOGGER.info("CLASE: CatalogTypeFacade");
			LOGGER.info("METODO: List<CatalogType> listaLineasGenero()");			
			LOGGER.info("RETORNA: List<CatalogType>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


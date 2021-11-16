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

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sigma.services.ComponentsFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class ComponentsFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ComponentsFacadeTest.class);
	
	@EJB
	private ComponentsFacade componentsEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"ComponentsFacadeTest.war")
				.addClass(ComponentsFacade.class)
				.addClass(Components.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Components.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testComponents(){
		try{			
			Assert.assertTrue(componentsEjb.listaComponentesActivos() != null);
			LOGGER.info("CLASE: ComponentsFacade");
			LOGGER.info("METODO: List<Components> listaComponentesActivos()");			
			LOGGER.info("RETORNA: List<Components>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}


/**
@autor proamazonia [Christian Báez]  16 nov. 2021

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

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.services.ValueAnswersFacade;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class TableResponsesFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableResponsesFacadeTest.class);
	@EJB
	private TableResponsesFacade tableEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"TableResponsesFacadeTest.war")				
				.addClass(TableResponsesFacade.class)
				.addClass(ValueAnswersFacade.class)
				.addClass(AbstractFacade.class)							
				.addPackage(Projects.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testTableResponses(){
		try{										
			Assert.assertTrue(tableEjb.listaMaximoHombresMujeresSalvaguardaB().size() == 1);
			LOGGER.info("CLASE: TableResponsesFacade");
			LOGGER.info("METODO: List<TableResponses> listaMaximoHombresMujeresSalvaguardaB()");			
			LOGGER.info("RETORNA: List<TableResponses>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}


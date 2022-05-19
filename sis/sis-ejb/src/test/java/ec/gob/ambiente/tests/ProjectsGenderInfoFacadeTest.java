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
import ec.gob.ambiente.sis.services.AdvanceExecutionProjectGenderFacade;
import ec.gob.ambiente.sis.services.ProjectGenderIndicatorFacade;
import ec.gob.ambiente.sis.services.ProjectsGenderInfoFacade;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class ProjectsGenderInfoFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsGenderInfoFacadeTest.class);
	@EJB
	private ProjectsGenderInfoFacade ejb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"ProjectsGenderInfoFacadeTest.war")				
				.addClass(ProjectsGenderInfoFacade.class)
				.addClass(AbstractFacade.class)			
				.addClass(ProjectGenderIndicatorFacade.class)
				.addClass(AdvanceExecutionProjectGenderFacade.class)
				.addPackage(Projects.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testProjectGenderInfo(){
		try{										
			Assert.assertTrue(ejb.listaProjectsGenderInfo(1).size() == 0);
			LOGGER.info("CLASE: ProjectsGenderInfoFacade");
			LOGGER.info("METODO: List<ProjectsGenderInfo> listaProjectsGenderInfo(int codigoProyecto)");			
			LOGGER.info("RETORNA: List<ProjectsGenderInfo>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}


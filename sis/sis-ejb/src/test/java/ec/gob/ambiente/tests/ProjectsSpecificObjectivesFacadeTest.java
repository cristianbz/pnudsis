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

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.model.ProjectsSpecificObjectives;
import ec.gob.ambiente.sigma.services.ProjectsSpecificObjectivesFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class ProjectsSpecificObjectivesFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsSpecificObjectivesFacadeTest.class);

	@EJB
	private ProjectsSpecificObjectivesFacade projectsEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"ProjectsSpecificObjectivesFacadeTest.war")
				.addClass(ProjectsSpecificObjectivesFacade.class)
				.addClass(ProjectsSpecificObjectives.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Projects.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testProjectsSpecificObjectives(){
		try{			
			Assert.assertTrue(projectsEjb.listaComponentesProyecto(1) != null);
			LOGGER.info("CLASE: ProjectsSpecificObjectivesFacade");
			LOGGER.info("METODO: List<ProjectsSpecificObjectives> listaComponentesProyecto(int codigoProyecto)");			
			LOGGER.info("RETORNA: List<ProjectsSpecificObjectives>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


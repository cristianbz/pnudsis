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
import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sigma.services.ProjectsStrategicPartnersFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class ProjectsStrategicPartnersFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectsStrategicPartnersFacadeTest.class);
	@EJB
	private ProjectsStrategicPartnersFacade projectStrategicEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"ProjectsStrategicPartnersFacadeTest.war")
				.addClass(ProjectsStrategicPartnersFacade.class)
				.addClass(ProjectsStrategicPartners.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Projects.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testProjectsStrategicPartner(){
		try{			
			Assert.assertTrue(projectStrategicEjb.listaPartnersActivos(1) != null);
			LOGGER.info("CLASE: ProjectsStrategicPartnersFacade");
			LOGGER.info("METODO: List<ProjectsStrategicPartners> listaPartnersActivos(Integer codigoProyecto)");			
			LOGGER.info("RETORNA: List<ProjectsStrategicPartners>");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}


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

import ec.gob.ambiente.sigma.model.Meetings;
import ec.gob.ambiente.sigma.services.MeetingsFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class MeetingsFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MeetingsFacadeTest.class);
	
	@EJB
	private MeetingsFacade meetingsEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"MeetingsFacadeTest.war")
				.addClass(MeetingsFacade.class)
				.addClass(Meetings.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Meetings.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testMeetings(){
		try{			
			Assert.assertTrue(meetingsEjb.listaEventosFortalecimientoHombres() == 0);
			LOGGER.info("CLASE: MeetingsFacade");
			LOGGER.info("METODO: int listaEventosFortalecimiento()");			
			LOGGER.info("RETORNA: int");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


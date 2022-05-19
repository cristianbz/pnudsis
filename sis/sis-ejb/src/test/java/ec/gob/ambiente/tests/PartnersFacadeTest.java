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

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class PartnersFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnersFacadeTest.class);

	@EJB
	private PartnersFacade partnersEjb;
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"PartnersFacadeTest.war")
				.addClass(PartnersFacade.class)
				.addClass(Partners.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Partners.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testPartners(){
		try{			
			Assert.assertTrue(partnersEjb.listarSociosImplementadores() != null);
			LOGGER.info("CLASE: PartnersFacade");
			LOGGER.info("METODO: List<Partners> listarSociosImplementadores()");			
			LOGGER.info("RETORNA: List<Partners> ");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


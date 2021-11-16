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

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@RunWith(Arquillian.class)
public class SafeguardsFacadeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SafeguardsFacadeTest.class);

	@EJB
	private SafeguardsFacade safeguardEjb;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap.create(WebArchive.class,"SafeguardsFacadeTest.war")
				.addClass(SafeguardsFacade.class)
				.addClass(Safeguards.class)
				.addClass(AbstractFacade.class)	
				.addPackage(Safeguards.class.getPackage())
				.addPackage(ProjectsGenderInfo.class.getPackage())
				.addPackage(GeographicalLocations.class.getPackage())
				.addClass(DaoException.class)
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");			
	}
	@Test
	public void testSafeguards(){
		try{			
			Safeguards sv = safeguardEjb.obtieneSalvaguarda(1);
			Assert.assertTrue(sv.getSafeCode().equals("A"));
			LOGGER.info("CLASE: SafeguardsFacade");
			LOGGER.info("METODO: Safeguards obtieneSalvaguarda(int codigoSalvaguarda)");			
			LOGGER.info("RETORNA: Safeguards");
			LOGGER.info("RESULTADO: EXITOSO");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


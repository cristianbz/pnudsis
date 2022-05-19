/**
@autor proamazonia [Christian Báez]  27 oct. 2021

**/
package ec.gob.ambiente.tests;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ec.gob.ambiente.sigma.services.CatalogFacade;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@RunWith(Arquillian.class)
public class PruebaTest {
	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive war = ShrinkWrap.create(JavaArchive.class,"test.jar")
				.addClass(CatalogFacade.class)
				.addClass(AbstractFacade.class)
				.addAsManifestResource("META-INF/persistence.xml","persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

		return war;
	}
	@Inject
	private CatalogFacade catalogos;
	
	@Test
	public void prueba(){
		Assert.assertEquals(1, 1);
		try{
		System.out.println("OK");
		System.out.println(this.catalogos.listaLineasAccion().size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


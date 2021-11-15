/**
@autor proamazonia [Christian Báez]  27 oct. 2021

**/
package ec.gob.ambiente.tests;

import static org.junit.Assert.*;

import javax.naming.NamingException;

import org.junit.Test;

import ec.gob.ambiente.sigma.services.CatalogFacade;

public class EjbTest {
	@Test
    public void testFactorial() {
        System.out.println("factorial");
        long base = 20;
        CatalogFacade instance;
		try {
			instance = (CatalogFacade)javax.ejb.embeddable.EJBContainer
			        .createEJBContainer()
			        .getContext()
			        .lookup("java:global/classes/CatalogFacade");
			 System.out.println(instance.listaLineasAccion().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
//        long expResult = 2432902008176640000L;
//        long result = instance.factorial(base);
        assertEquals(0, 2);
    }
}


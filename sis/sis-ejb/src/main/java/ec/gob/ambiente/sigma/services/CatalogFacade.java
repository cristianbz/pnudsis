/**
@autor proamazonia [Christian Báez]  1 jun. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Catalog;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class CatalogFacade extends AbstractFacade<Catalog, Integer>{

	public CatalogFacade() {
		super(Catalog.class,Integer.class);
	}
	/**
	 * Carga las lineas de accion por el tipo de linea estrategica
	 * @param codigoTipo
	 * @return
	 * @throws Exception
	 */
	public List<Catalog> listaLineasAccion()throws Exception{
		String sql="SELECT C FROM Catalog C WHERE C.cataStatus=true AND C.catyId.catyId IN(35,36,37) ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
	
}


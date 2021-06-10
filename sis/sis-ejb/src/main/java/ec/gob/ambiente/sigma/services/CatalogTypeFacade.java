/**
@autor proamazonia [Christian Báez]  1 jun. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.CatalogType;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class CatalogTypeFacade extends AbstractFacade<CatalogType, Integer>{
	public CatalogTypeFacade(){
		super(CatalogType.class,Integer.class);
	}
	/***
	 * Carga los tipos de catalogo de la linea de genero
	 * @return
	 * @throws Exception
	 */
	public List<CatalogType> listaLineasGenero()throws Exception{
		String sql="SELECT CT FROM CatalogType CT WHERE CT.catyStatus=true AND CT.catyId IN(35,36,37) ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
}


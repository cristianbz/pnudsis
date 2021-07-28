/**
@autor proamazonia [Christian Báez]  27 jul. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.CatalogsType;

@LocalBean
@Stateless
public class CatalogsTypeFacade extends AbstractFacade<CatalogsType, Integer>{
	public CatalogsTypeFacade(){
		super(CatalogsType.class,Integer.class);
	}
	public List<CatalogsType> listaTipoCatalogos() throws Exception{
		String sql="SELECT CT FROM CatalogsType CT ORDER BY CT.catyId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}

}


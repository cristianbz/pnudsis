/**
@autor proamazonia [Christian Báez]  20 sep. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ComponentsFacade extends AbstractFacade<Components,Integer> {
	public ComponentsFacade(){
		super(Components.class,Integer.class);
	}
	/**
	 * Lista de componentes Estrategicos
	 * @return
	 * @throws Exception
	 */
	public List<Components> listaComponentesActivos()throws Exception{
		String sql="SELECT C from Components C WHERE C.compStatus=TRUE AND C.compCode like 'CE%' ORDER BY C.compCode";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
}


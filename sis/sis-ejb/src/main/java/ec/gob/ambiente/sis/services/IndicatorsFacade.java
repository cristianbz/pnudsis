/**
@autor proamazonia [Christian Báez]  5 ago. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Indicators;

@Stateless
@LocalBean
public class IndicatorsFacade extends AbstractFacade<Indicators, Integer> {
	public IndicatorsFacade(){
		super(Indicators.class,Integer.class);
	}
	/**
	 * Carga los indicadores activos
	 * @return
	 * @throws Exception
	 */
	public List<Indicators> listaIndicadoresGenero()throws Exception{
		String sql="SELECT I from Indicators I WHERE I.indiStatus=TRUE ORDER BY I.indiId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();				
		return findByCreateQuery(sql, camposCondicion);
	}
	
}


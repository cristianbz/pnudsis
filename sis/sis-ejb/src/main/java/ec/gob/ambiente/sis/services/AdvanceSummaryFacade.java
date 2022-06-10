/**
@autor proamazonia [Christian Báez]  3 jun. 2022

**/
package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceSummary;

@Stateless
@LocalBean
public class AdvanceSummaryFacade extends AbstractFacade<AdvanceSummary, Integer> {

	public AdvanceSummaryFacade() {
		super(AdvanceSummary.class,Integer.class);
		// TODO Auto-generated constructor stub
	}
	
	public List<AdvanceSummary> listaAvancesPorAvanceEjecucion(int codigoAvance) throws Exception{
		String sql="";
		List<AdvanceSummary> listaTemp= new ArrayList<>();
		sql= "SELECT ASU FROM AdvanceSummary ASU WHERE ASU.advanceExecutionSafeguards.adexId = :codigoAvance";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvance", codigoAvance);
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}
}


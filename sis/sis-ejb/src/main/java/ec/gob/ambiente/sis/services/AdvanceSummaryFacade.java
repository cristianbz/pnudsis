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
	
	public List<AdvanceSummary> listaAvancesSalvaguarda_Anio(String anio, int codigoSalvaguarda) throws Exception{
		String sql="";
		List<AdvanceSummary> listaTemp= new ArrayList<>();
		sql= "SELECT ASU FROM AdvanceSummary ASU WHERE ASU.advanceExecutionSafeguards.adexTermFrom  = :anio AND ASU.safeguards.safeId =:codigo "
				+ "AND ASU.adsuStatus=TRUE AND ASU.advanceExecutionSafeguards.adexStatus =TRUE "
				+ "AND ASU.advanceExecutionSafeguards.adexStatus=TRUE  ORDER BY ASU.advanceExecutionSafeguards.projects.projShortName";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigo", codigoSalvaguarda);
		camposCondicion.put("anio", anio);
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}
	
	public List<AdvanceSummary> listaAvancesProyecto(int proyecto, String desde) throws Exception{
		String sql="";
		List<AdvanceSummary> listaTemp= new ArrayList<>();
		sql= "SELECT ASU FROM AdvanceSummary ASU WHERE ASU.advanceExecutionSafeguards.projects.projId =:proyecto AND ASU.adsuStatus=TRUE AND ASU.advanceExecutionSafeguards.adexTermFrom =:desde AND ASU.advanceExecutionSafeguards.adexStatus =TRUE ORDER BY ASU.safeguards.safeCode";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("proyecto", proyecto);
		camposCondicion.put("desde", desde);
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}
}


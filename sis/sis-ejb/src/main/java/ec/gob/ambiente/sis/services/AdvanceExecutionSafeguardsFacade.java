package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;

@Stateless
@LocalBean
public class AdvanceExecutionSafeguardsFacade extends AbstractFacade<AdvanceExecutionSafeguards, Integer>{


	public AdvanceExecutionSafeguardsFacade() {
		super(AdvanceExecutionSafeguards.class,Integer.class);
	}
	
	/**
	 * Devuelve el avance de ejecucion por proyecto
	 * @param codigoProyecto
	 * @return
	 */	
	public AdvanceExecutionSafeguards buscarPorProyecto(int codigoProyecto) throws Exception{
		String sql="SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto AND AP.adexIsReported=false";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuerySingleResult(sql, camposCondicion);
	}
	/**
	 * Graba el AvanceEjecucionSalvaguarda
	 * @param avanceEjecucion
	 * @throws Exception
	 */
	public void grabarAvanceEjecucionSalvaguarda(AdvanceExecutionSafeguards avanceEjecucion) throws Exception{		
		if(avanceEjecucion.getAdexId()==0)
				create(avanceEjecucion);			
		else
				edit(avanceEjecucion);		
	}

}

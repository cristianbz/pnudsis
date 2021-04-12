package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;

@Stateless
public class ExecutiveSummariesFacade extends AbstractFacade<ExecutiveSummaries, Integer>  {

	
	

	public ExecutiveSummariesFacade() {
		super(ExecutiveSummaries.class,Integer.class);
	}
	/**
	 * Permite grabar el resumen ejecutivo
	 * @param resumenEjecutivo
	 * @throws Exception
	 */
	public void grabarResumenEjecutivo(ExecutiveSummaries resumenEjecutivo)throws Exception{
		if(resumenEjecutivo.getExsuId()==null)
			create(resumenEjecutivo);
		else
			edit(resumenEjecutivo);
	}
	
	public ExecutiveSummaries buscaPorAvanceEjecucion(int codigo)throws DaoException{
		try{
			String sql="SELECT ES FROM ExecutiveSummaries ES WHERE ES.advanceExecutionSafeguards.adexId=:codigo";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigo", codigo);
		return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}

}

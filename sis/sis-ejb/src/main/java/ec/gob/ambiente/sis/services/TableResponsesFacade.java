package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.TableResponses;

@Stateless
@LocalBean
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> {




	public TableResponsesFacade() {
		super(TableResponses.class,Integer.class);
	}
	
	/**
	 * Carga los valores de respuesta tipo tabla por avance de ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 */
	public List<TableResponses> findByAdvanceExecution(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Eliminar registro de TableResponses
	 * @param tableResponses
	 * @throws Exception
	 */
	public void eliminarRespuestasTabla(TableResponses tableResponses) throws Exception{
		TableResponses objetoEliminar = getEntityManager().find(TableResponses.class, tableResponses.getTareId());
		remove(objetoEliminar);
	}

}

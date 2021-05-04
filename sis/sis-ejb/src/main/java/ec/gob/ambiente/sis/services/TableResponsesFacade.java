package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
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
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE";
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
//		TableResponses objetoEliminar = getEntityManager().find(TableResponses.class, tableResponses.getTareId());
//		remove(objetoEliminar);
		tableResponses.setTareStatus(false);
		edit(tableResponses);
	}
	/**
	 * Elimina los datos de la tabla (coloca estado de elimancion a false)
	 * @param tablaDatos
	 * @throws Exception
	 */
	public void eliminarDatosTabla(List<TableResponses> tablaDatos) throws Exception{
		for (TableResponses tableResponses : tablaDatos) {
			tableResponses.setTareStatus(false);
			edit(tableResponses);
		}
	}
	public List<TableResponses> buscarPorAvanceEjecucionYSalvaguarda(int codigoAvanceEjecucion, int codigoSalvaguarda) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.safeguards.safeId=:codigoSalvaguarda ORDER BY TR.questions.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		List<TableResponses> tabla=findByCreateQuery(sql, camposCondicion);
		for (TableResponses tableResponses : tabla) {
			Hibernate.initialize(tableResponses.getQuestions());
		}
//		return findByCreateQuery(sql, camposCondicion);
		return tabla;
	}

	public TableResponses buscaLeyPolitica(String codigoLeyPolitica,int codigoAvanceEjecucion,int codigoPregunta) throws DaoException{
		try{
			String sql="SELECT TR FROM TableResponses TR WHERE TR.tareColumnOne  =:codigoLeyPolitica AND TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesId =:codigoPregunta";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);		
			camposCondicion.put("codigoLeyPolitica", codigoLeyPolitica);
			camposCondicion.put("codigoPregunta", codigoPregunta);			
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new DaoException();
		}
	}
	public void agregarEditarNuevaTableResponse(TableResponses tableResponse) throws Exception{
		if(tableResponse.getTareId()==null)
			create(tableResponse);
		else
			edit(tableResponse);
	}
	
}

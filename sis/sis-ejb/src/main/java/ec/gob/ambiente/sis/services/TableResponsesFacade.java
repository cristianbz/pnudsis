package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
@LocalBean
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> {
	@EJB
	private ValueAnswersFacade valueAnswersFacade;



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
	public void eliminarDatosTablaActualizaValueAnswer(List<TableResponses> tablaDatos,ValueAnswers valueAnswer) throws Exception{
		for (TableResponses tableResponses : tablaDatos) {
			tableResponses.setTareStatus(false);
			edit(tableResponses);			
		}
		valueAnswer.setVaanYesnoAnswerValue(false);
		valueAnswersFacade.edit(valueAnswer);
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
	public boolean agregaTablaResponseValueAnswers(TableResponses tableResponse,ValueAnswers valueAnswers,List<ValueAnswers> listaValoresRespuestas) throws Exception{
		boolean seAgregaronValoresRespuestas=false;
		if(tableResponse.getTareId()==null)
			create(tableResponse);
		else
			edit(tableResponse);
		if (valueAnswers!=null && valueAnswers.getVaanId()==null){
			for (ValueAnswers valores : listaValoresRespuestas) {
				if(valores.getQuestions().getQuesId() == valueAnswers.getQuestions().getQuesId())
					valores = valueAnswers;
				valueAnswersFacade.create(valores);
			}
			seAgregaronValoresRespuestas = true;
		}else{
			valueAnswersFacade.edit(valueAnswers);
		}
		return seAgregaronValoresRespuestas;	
	}
	public void agregaRespuestaTabla(TableResponses respuesta,ValueAnswers valorRespuesta) throws Exception{
		if(respuesta.getTareId()==null){
			create(respuesta);
			valueAnswersFacade.edit(valorRespuesta);
		}
			
	}
	/**
	 * Busca por avanceEjecucion y Genero
	 * @param codigoAvanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> buscarPorAvanceEjecucionYGenero(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesIsGender = TRUE ORDER BY TR.questions.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		List<TableResponses> tabla=findByCreateQuery(sql, camposCondicion);
		for (TableResponses tableResponses : tabla) {
			Hibernate.initialize(tableResponses.getQuestions());
		}
//		return findByCreateQuery(sql, camposCondicion);
		return tabla;
	}
}

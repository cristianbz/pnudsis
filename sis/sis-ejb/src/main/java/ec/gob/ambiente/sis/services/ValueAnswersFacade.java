package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
@LocalBean
public class ValueAnswersFacade extends AbstractFacade<ValueAnswers, Integer> {

	public ValueAnswersFacade() {
		super(ValueAnswers.class,Integer.class);
	}
	/**
	 * Retorna los valores de respuestas por codigo de avance ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 */
	public List<ValueAnswers> buscarPorAvanceEjecucion(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT VA FROM ValueAnswers VA WHERE VA.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND VA.advanceExecutionSaveguards.adexDropState=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Busca valoresRespuesta por avance de ejecucion y salvaguarda
	 * @param codigoAvanceEjecucion
	 * @param codigoSalvaguarda
	 * @return
	 * @throws Exception
	 */
	public List<ValueAnswers> buscarPorAvanceEjecucionYSalvaguarda(int codigoAvanceEjecucion,int codigoSalvaguarda) throws Exception{
		String sql="SELECT VA FROM ValueAnswers VA WHERE VA.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND VA.advanceExecutionSaveguards.adexDropState=TRUE AND VA.questions.safeguards.safeId=:codigoSalvaguarda";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}

}

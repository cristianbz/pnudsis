package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Questions;

@Stateless
@LocalBean
public class QuestionsFacade extends AbstractFacade<Questions, Integer>  {


	public QuestionsFacade() {
		super(Questions.class, Integer.class);
	}
	/**
	 * Lista de todas las preguntas ingresadas
	 */
	
	public List<Questions> buscarTodasLasPreguntas() throws Exception{
		String sql="SELECT Q FROM Questions Q";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Consulta pregunta por codigo salvaguarda
	 * @return
	 */
	public List<Questions> buscaPreguntaPorSalvaguarda(int codigoSalvaguarda) throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeId=:codigoSalvaguarda";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Consulta preguntas por codigos de salvaguardas
	 * @param codigosSalvaguardas
	 * @return
	 * @throws Exception
	 */
	public List<Questions> buscarPreguntasPorSalvaguardas(List<Integer> codigosSalvaguardas) throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeId IN(:codigoSalvaguarda) ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigosSalvaguardas);
		return findByCreateQuery(sql, camposCondicion);
	}
}

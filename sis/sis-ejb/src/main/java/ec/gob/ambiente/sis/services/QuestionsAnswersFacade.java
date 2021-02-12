package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.QuestionsAnswers;

@Stateless
@LocalBean
public class QuestionsAnswersFacade extends AbstractFacade<QuestionsAnswers, Integer> {
	

	public QuestionsAnswersFacade() {
		super(QuestionsAnswers.class,Integer.class);
	}
	/**
	 * Consulta preguntas_respuestas por salvaguardas
	 * @param codigoSalvaguarda
	 * @return
	 */
	public List<QuestionsAnswers> buscarPorSalvaguarda(List<Integer> codigoSalvaguarda) throws Exception{
		String sql="SELECT QA FROM QuestionsAnswers QA WHERE QA.quanRemoveState=TRUE AND QA.questions.safeguards.safeId IN(:codigoSalvaguarda) ORDER BY QA.questions.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}

}
